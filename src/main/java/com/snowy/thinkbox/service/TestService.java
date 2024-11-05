package com.snowy.thinkbox.service;

import com.snowy.thinkbox.domain.Test;
import com.snowy.thinkbox.mapper.TestMapper;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TestService {
    @Resource
    private TestMapper testMapper;

    public List<Test> list() {
        return testMapper.list();
    }
}
