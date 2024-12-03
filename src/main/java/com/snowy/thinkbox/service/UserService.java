package com.snowy.thinkbox.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.snowy.thinkbox.domain.User;
import com.snowy.thinkbox.domain.UserExample;
import com.snowy.thinkbox.mapper.UserMapper;
import com.snowy.thinkbox.req.UserQueryReq;
import com.snowy.thinkbox.req.UserSaveReq;
import com.snowy.thinkbox.resp.PageResp;
import com.snowy.thinkbox.resp.UserQueryResp;
import com.snowy.thinkbox.utils.CopyUtil;
import com.snowy.thinkbox.utils.SnowFlake;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;

@Service
public class UserService {
    private static final Logger LOG = LoggerFactory.getLogger(UserService.class);
    @Resource
    private UserMapper userMapper;

    @Resource
    private SnowFlake snowFlake;

    public PageResp<UserQueryResp> list(UserQueryReq userQueryReq) {
        UserExample userExample = new UserExample();
        UserExample.Criteria criteria = userExample.createCriteria();
        if (!ObjectUtils.isEmpty(userQueryReq.getLoginName())) {
            criteria.andNameLike("%" + userQueryReq.getLoginName() + "%");
        }
//        if (!ObjectUtils.isEmpty(userQueryReq.getCategory2Id())) {
//            criteria.andCategory2IdEqualTo(userQueryReq.getCategory2Id());
//        }
        PageHelper.startPage(userQueryReq.getPage(), userQueryReq.getSize());
        List<User> userList =  userMapper.selectByExample(userExample);

        PageInfo<User> pageInfo = new PageInfo<>(userList);
        LOG.info("总行数: {}", pageInfo.getTotal());
        LOG.info("总页数: {}", pageInfo.getPages());
//        List<UserResp> userResps = new ArrayList<>();
//        for(User user: userList) {
//            UserResp userResp = new UserResp();
//            BeanUtils.copyProperties(user,userResp);
//            userResps.add(userResp);
//        }
        List<UserQueryResp> userQueryResps = CopyUtil.copyList(userList, UserQueryResp.class);
        PageResp<UserQueryResp> userPageResp = new PageResp<>();
        userPageResp.setTotal(pageInfo.getTotal());
        userPageResp.setList(userQueryResps);
        return userPageResp;
    }

    public void save(UserSaveReq req) {
        User user = CopyUtil.copy(req, User.class);
        if (ObjectUtils.isEmpty(req.getId())) {
            // 新增
            user.setId(snowFlake.nextId());//生成id的方法:自增,UUID,雪花算法...这里使用雪花算法
            userMapper.insert(user);
        } else {
            // 更新
            userMapper.updateByPrimaryKey(user);
        }
    }

    public void delete(Long id) {
        userMapper.deleteByPrimaryKey(id);
    }
}
