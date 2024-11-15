package com.snowy.thinkbox.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.snowy.thinkbox.domain.Ebook;
import com.snowy.thinkbox.domain.EbookExample;
import com.snowy.thinkbox.mapper.EbookMapper;
import com.snowy.thinkbox.req.EbookQueryReq;
import com.snowy.thinkbox.req.EbookSaveReq;
import com.snowy.thinkbox.resp.EbookQueryResp;
import com.snowy.thinkbox.resp.PageResp;
import com.snowy.thinkbox.utils.CopyUtil;
import com.snowy.thinkbox.utils.SnowFlake;
import jakarta.annotation.Resource;
//import org.mybatis.logging.Logger;
//import org.mybatis.logging.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@Service
public class EbookService {
    private static final Logger LOG = LoggerFactory.getLogger(EbookService.class);
    @Resource
    private EbookMapper ebookMapper;

    @Resource
    private SnowFlake snowFlake;

    public PageResp<EbookQueryResp> list(EbookQueryReq ebookQueryReq) {
        EbookExample ebookExample = new EbookExample();
        EbookExample.Criteria criteria = ebookExample.createCriteria();
        if (!ObjectUtils.isEmpty(ebookQueryReq.getName())) {
            criteria.andNameLike("%" + ebookQueryReq.getName() + "%");
        }
        PageHelper.startPage(ebookQueryReq.getPage(), ebookQueryReq.getSize());
        List<Ebook> ebookList =  ebookMapper.selectByExample(ebookExample);

        PageInfo<Ebook> pageInfo = new PageInfo<>(ebookList);
        LOG.info("总行数: {}", pageInfo.getTotal());
        LOG.info("总页数: {}", pageInfo.getPages());
//        List<EbookResp> ebookResps = new ArrayList<>();
//        for(Ebook ebook: ebookList) {
//            EbookResp ebookResp = new EbookResp();
//            BeanUtils.copyProperties(ebook,ebookResp);
//            ebookResps.add(ebookResp);
//        }
        List<EbookQueryResp> ebookQueryResps = CopyUtil.copyList(ebookList, EbookQueryResp.class);
        PageResp<EbookQueryResp> ebookPageResp = new PageResp<>();
        ebookPageResp.setTotal(pageInfo.getTotal());
        ebookPageResp.setList(ebookQueryResps);
        return ebookPageResp;
    }

    public void save(EbookSaveReq req) {
        Ebook ebook = CopyUtil.copy(req, Ebook.class);
        if (ObjectUtils.isEmpty(req.getId())) {
            // 新增
            ebook.setId(snowFlake.nextId());//生成id的方法:自增,UUID,雪花算法...这里使用雪花算法
            ebookMapper.insert(ebook);
        } else {
            // 更新
            ebookMapper.updateByPrimaryKey(ebook);
        }
    }
}
