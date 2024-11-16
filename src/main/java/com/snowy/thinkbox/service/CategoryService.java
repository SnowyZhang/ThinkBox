package com.snowy.thinkbox.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.snowy.thinkbox.domain.Category;
import com.snowy.thinkbox.domain.CategoryExample;
import com.snowy.thinkbox.mapper.CategoryMapper;
import com.snowy.thinkbox.req.CategoryQueryReq;
import com.snowy.thinkbox.req.CategorySaveReq;
import com.snowy.thinkbox.resp.CategoryQueryResp;
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
public class CategoryService {
    private static final Logger LOG = LoggerFactory.getLogger(CategoryService.class);
    @Resource
    private CategoryMapper categoryMapper;

    @Resource
    private SnowFlake snowFlake;

    public PageResp<CategoryQueryResp> list(CategoryQueryReq categoryQueryReq) {
        CategoryExample categoryExample = new CategoryExample();
        categoryExample.setOrderByClause("priority asc");
        CategoryExample.Criteria criteria = categoryExample.createCriteria();
        if (!ObjectUtils.isEmpty(categoryQueryReq.getName())) {
            criteria.andNameLike("%" + categoryQueryReq.getName() + "%");
        }
        PageHelper.startPage(categoryQueryReq.getPage(), categoryQueryReq.getSize());
        List<Category> categoryList =  categoryMapper.selectByExample(categoryExample);

        PageInfo<Category> pageInfo = new PageInfo<>(categoryList);
        LOG.info("总行数: {}", pageInfo.getTotal());
        LOG.info("总页数: {}", pageInfo.getPages());
        List<CategoryQueryResp> categoryQueryResps = CopyUtil.copyList(categoryList, CategoryQueryResp.class);
        PageResp<CategoryQueryResp> categoryPageResp = new PageResp<>();
        categoryPageResp.setTotal(pageInfo.getTotal());
        categoryPageResp.setList(categoryQueryResps);
        return categoryPageResp;
    }

    public void save(CategorySaveReq req) {
        Category category = CopyUtil.copy(req, Category.class);
        if (ObjectUtils.isEmpty(req.getId())) {
            // 新增
            category.setId(snowFlake.nextId());//生成id的方法:自增,UUID,雪花算法...这里使用雪花算法
            categoryMapper.insert(category);
        } else {
            // 更新
            categoryMapper.updateByPrimaryKey(category);
        }
    }

    public void delete(Long id) {
        categoryMapper.deleteByPrimaryKey(id);
    }

    public List<CategoryQueryResp> all(CategoryQueryReq categoryQueryReq) {
        CategoryExample categoryExample = new CategoryExample();
        categoryExample.setOrderByClause("priority asc");
        CategoryExample.Criteria criteria = categoryExample.createCriteria();
        if (!ObjectUtils.isEmpty(categoryQueryReq.getName())) {
            criteria.andNameLike("%" + categoryQueryReq.getName() + "%");
        }
        List<Category> categoryList =  categoryMapper.selectByExample(categoryExample);
        return CopyUtil.copyList(categoryList, CategoryQueryResp.class);
    }
}
