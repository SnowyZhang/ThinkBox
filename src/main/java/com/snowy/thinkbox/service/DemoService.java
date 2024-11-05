package com.snowy.thinkbox.service;

import com.snowy.thinkbox.domain.Demo;
import com.snowy.thinkbox.domain.DemoExample;
import com.snowy.thinkbox.mapper.DemoMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DemoService {
    @Resource
    private DemoMapper demoMapper;

    public List<Demo> list() {
        return demoMapper.selectByExample(null);
    }
}
