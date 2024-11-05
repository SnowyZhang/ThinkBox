package com.snowy.thinkbox.service;

import com.snowy.thinkbox.domain.Ebook;
import com.snowy.thinkbox.mapper.EbookMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EbookService {
    @Resource
    private EbookMapper ebookMapper;

    public List<Ebook> list() {
        return ebookMapper.selectByExample(null);
    }
}
