package com.snowy.thinkbox.service;

import com.snowy.thinkbox.domain.Ebook;
import com.snowy.thinkbox.domain.EbookExample;
import com.snowy.thinkbox.mapper.EbookMapper;
import com.snowy.thinkbox.req.EbookReq;
import com.snowy.thinkbox.resp.EbookResp;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class EbookService {
    @Resource
    private EbookMapper ebookMapper;

    public List<EbookResp> list(EbookReq ebookReq) {
        EbookExample ebookExample = new EbookExample();
        EbookExample.Criteria criteria = ebookExample.createCriteria();
        criteria.andNameLike("%" + ebookReq.getName() + "%");
        List<Ebook> ebookList =  ebookMapper.selectByExample(ebookExample);

        List<EbookResp> ebookResps = new ArrayList<>();
        for(Ebook ebook: ebookList) {
            EbookResp ebookResp = new EbookResp();
            BeanUtils.copyProperties(ebook,ebookResp);
            ebookResps.add(ebookResp);
        }
        return ebookResps;
    }
}
