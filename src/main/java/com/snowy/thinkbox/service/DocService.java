package com.snowy.thinkbox.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.snowy.thinkbox.domain.Doc;
import com.snowy.thinkbox.domain.DocExample;
import com.snowy.thinkbox.mapper.DocMapper;
import com.snowy.thinkbox.req.DocQueryReq;
import com.snowy.thinkbox.req.DocSaveReq;
import com.snowy.thinkbox.resp.DocQueryResp;
import com.snowy.thinkbox.resp.PageResp;
import com.snowy.thinkbox.utils.CopyUtil;
import com.snowy.thinkbox.utils.SnowFlake;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;

@Service
public class DocService {
    private static final Logger LOG = LoggerFactory.getLogger(DocService.class);
    @Resource
    private DocMapper docMapper;

    @Resource
    private SnowFlake snowFlake;

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

    public void save(DocSaveReq req) {
        Doc doc = CopyUtil.copy(req, Doc.class);
        if (ObjectUtils.isEmpty(req.getId())) {
            // 新增
            doc.setId(snowFlake.nextId());//生成id的方法:自增,UUID,雪花算法...这里使用雪花算法
            docMapper.insert(doc);
        } else {
            // 更新
            docMapper.updateByPrimaryKey(doc);
        }
    }

    public void delete(List<String> ids) {
        DocExample docExample = new DocExample();
        DocExample.Criteria criteria = docExample.createCriteria();
        criteria.andIdIn(ids);
        docMapper.deleteByExample(docExample);
    }

    public List<DocQueryResp> all(DocQueryReq docQueryReq) {
        DocExample docExample = new DocExample();
        docExample.setOrderByClause("priority asc");
        DocExample.Criteria criteria = docExample.createCriteria();
        if (!ObjectUtils.isEmpty(docQueryReq.getName())) {
            criteria.andNameLike("%" + docQueryReq.getName() + "%");
        }
        List<Doc> docList =  docMapper.selectByExample(docExample);
        return CopyUtil.copyList(docList, DocQueryResp.class);
    }
}
