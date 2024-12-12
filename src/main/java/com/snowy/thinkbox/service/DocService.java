package com.snowy.thinkbox.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.snowy.thinkbox.domain.*;
import com.snowy.thinkbox.exception.BusinessException;
import com.snowy.thinkbox.exception.BusinessExceptionCode;
import com.snowy.thinkbox.mapper.CommentsMapper;
import com.snowy.thinkbox.mapper.ContentMapper;
import com.snowy.thinkbox.mapper.DocMapper;
import com.snowy.thinkbox.mapper.MyDocMapper;
import com.snowy.thinkbox.req.CommentSaveReq;
import com.snowy.thinkbox.req.DocQueryReq;
import com.snowy.thinkbox.req.DocSaveReq;
import com.snowy.thinkbox.resp.CommentsQueryResp;
import com.snowy.thinkbox.resp.CommonResp;
import com.snowy.thinkbox.resp.DocQueryResp;
import com.snowy.thinkbox.resp.PageResp;
import com.snowy.thinkbox.utils.CopyUtil;
import com.snowy.thinkbox.utils.RecordIPAddress;
import com.snowy.thinkbox.utils.RedisUtil;
import com.snowy.thinkbox.utils.SnowFlake;
import com.snowy.thinkbox.websocket.WebSocketServer;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.io.IOException;
import java.util.Date;
import java.util.List;

@Service
public class DocService {
    private static final Logger LOG = LoggerFactory.getLogger(DocService.class);
    @Resource
    private DocMapper docMapper;

    @Resource
    private CommentsMapper commentsMapper;

    @Resource
    private ContentMapper contentMapper;

    @Resource
    private SnowFlake snowFlake;

    @Resource
    private MyDocMapper myDocMapper;

    @Resource
    private RedisUtil redisUtil;

    @Resource
    private KafkaProducerService kafkaProducerService;


    public PageResp<DocQueryResp> list(DocQueryReq docQueryReq) {
        DocExample docExample = new DocExample();
        docExample.setOrderByClause("priority asc");
        DocExample.Criteria criteria = docExample.createCriteria();
        if (!ObjectUtils.isEmpty(docQueryReq.getName())) {
            criteria.andNameLike("%" + docQueryReq.getName() + "%");
        }
        PageHelper.startPage(docQueryReq.getPage(), docQueryReq.getSize());
        List<Doc> docList =  docMapper.selectByExample(docExample);

        PageInfo<Doc> pageInfo = new PageInfo<>(docList);
        LOG.info("总行数: {}", pageInfo.getTotal());
        LOG.info("总页数: {}", pageInfo.getPages());
        List<DocQueryResp> docQueryResps = CopyUtil.copyList(docList, DocQueryResp.class);
        PageResp<DocQueryResp> docPageResp = new PageResp<>();
        docPageResp.setTotal(pageInfo.getTotal());
        docPageResp.setList(docQueryResps);
        return docPageResp;
    }

    @Transactional
    public void save(DocSaveReq req) {
        Doc doc = CopyUtil.copy(req, Doc.class);
        Content content = CopyUtil.copy(req, Content.class);
        if (ObjectUtils.isEmpty(req.getId())) {
            // 新增
            doc.setId(snowFlake.nextId());//生成id的方法:自增,UUID,雪花算法...这里使用雪花算法
            doc.setViewCount(0);
            doc.setVoteCount(0);
            docMapper.insert(doc);
            content.setId(doc.getId());
            contentMapper.insert(content);
        }else{
            //更新
            docMapper.updateByPrimaryKey(doc);
            int count = contentMapper.updateByPrimaryKeyWithBLOBs(content);
            if (count == 0) {
                contentMapper.insert(content);
            }
        }
    }

    public void delete(List<String> ids) {
        DocExample docExample = new DocExample();
        DocExample.Criteria criteria = docExample.createCriteria();
        criteria.andIdIn(ids);
        docMapper.deleteByExample(docExample);
    }

    public List<DocQueryResp> all(String ebookId) {
        DocExample docExample = new DocExample();
        docExample.setOrderByClause("priority asc");
        DocExample.Criteria criteria = docExample.createCriteria();
        criteria.andEbookIdEqualTo(Long.valueOf(ebookId));
        List<Doc> docList =  docMapper.selectByExample(docExample);
        return CopyUtil.copyList(docList, DocQueryResp.class);
    }

    public  String findContent(String id) {
        Content content = contentMapper.selectByPrimaryKey(Long.valueOf(id));
        myDocMapper.updateViewCount(Long.valueOf(id));
        //判断是否为空
        if (ObjectUtils.isEmpty(content)) {
            return "";
        }
        return content.getContent();
    }
    public  List<CommentsQueryResp> findComment(String doc_id) {

        List<CommentsQueryResp> commentsList = myDocMapper.findComment(Long.valueOf(doc_id));
        if(commentsList.isEmpty()){
            LOG.info("评论为空");
            return commentsList;
        }else{
            LOG.info("评论不为空");
            return commentsList;
        }

    }

    public void vote(String id) {
        LOG.info("点赞id: {}", id);
        String KEY = RecordIPAddress.getRemoteAddr();
        if(redisUtil.validateRepeat("vote:"+id+"_"+KEY, 60)){
            myDocMapper.updateVoteCount(Long.valueOf(id));
            LOG.info("点赞成功");
        }else{
            LOG.info("点赞失败");
            throw new BusinessException(BusinessExceptionCode.VOTE_REPEAT);
        }
//        try {
//            Doc doc = docMapper.selectByPrimaryKey(Long.valueOf(id));
//            webSocketServer.broadcastMessage("文档《"+ doc.getName()+"》被点赞啦！");
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
        Doc doc = docMapper.selectByPrimaryKey(Long.valueOf(id));
        kafkaProducerService.sendVoteEvent("vote-topic","文档《"+ doc.getName()+"》被点赞啦！");
    }

    public void updateEbookInfo() {
        myDocMapper.updateEbookInfo();
    }

    public void saveComment(@Valid CommentSaveReq req) {
        Comments comment = CopyUtil.copy(req, Comments.class);
        if (ObjectUtils.isEmpty(req.getId())) {
            // 新增
            comment.setId(snowFlake.nextId());//生成id的方法:自增,UUID,雪花算法...这里使用雪花算法
            comment.setDocId(req.getDocId());
            comment.setUserId(req.getUserId());
            comment.setContent(req.getContent());
            Date createdAt = new Date();
            comment.setCreatedAt(createdAt);
            commentsMapper.insert(comment);
        }else{
            //更新
            comment.setUpdatedAt(new Date());
            commentsMapper.updateByPrimaryKey(comment);
            int count = commentsMapper.updateByPrimaryKey(comment);
            if (count == 0) {
                commentsMapper.insert(comment);
            }
        }
    }
}
