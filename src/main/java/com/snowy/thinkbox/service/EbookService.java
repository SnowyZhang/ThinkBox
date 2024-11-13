package com.snowy.thinkbox.service;

import ch.qos.logback.core.util.StringUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.snowy.thinkbox.domain.Ebook;
import com.snowy.thinkbox.domain.EbookExample;
import com.snowy.thinkbox.mapper.EbookMapper;
import com.snowy.thinkbox.req.EbookReq;
import com.snowy.thinkbox.resp.EbookResp;
import com.snowy.thinkbox.utils.CopyUtil;
import io.micrometer.common.util.StringUtils;
import jakarta.annotation.Resource;
//import org.mybatis.logging.Logger;
//import org.mybatis.logging.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

@Service
public class EbookService {
    private static final Logger LOG = LoggerFactory.getLogger(EbookService.class);
    @Resource
    private EbookMapper ebookMapper;

    public List<EbookResp> list(EbookReq ebookReq) {
        EbookExample ebookExample = new EbookExample();
        EbookExample.Criteria criteria = ebookExample.createCriteria();
        if (!ObjectUtils.isEmpty(ebookReq.getName())) {
            criteria.andNameLike("%" + ebookReq.getName() + "%");
        }
        PageHelper.startPage(1, 2);
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
        List<EbookResp> ebookResps = CopyUtil.copyList(ebookList, EbookResp.class);
        return ebookResps;
    }
}
