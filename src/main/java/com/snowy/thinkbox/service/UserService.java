package com.snowy.thinkbox.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.snowy.thinkbox.domain.User;
import com.snowy.thinkbox.domain.UserEmailVerification;
import com.snowy.thinkbox.domain.UserEmailVerificationExample;
import com.snowy.thinkbox.domain.UserExample;
import com.snowy.thinkbox.exception.BusinessException;
import com.snowy.thinkbox.exception.BusinessExceptionCode;
import com.snowy.thinkbox.mapper.UserEmailVerificationMapper;
import com.snowy.thinkbox.mapper.UserMapper;
import com.snowy.thinkbox.req.*;
import com.snowy.thinkbox.resp.PageResp;
import com.snowy.thinkbox.resp.UserLoginResp;
import com.snowy.thinkbox.resp.UserQueryResp;
import com.snowy.thinkbox.utils.CopyUtil;
import com.snowy.thinkbox.utils.EmailUtil;
import com.snowy.thinkbox.utils.SnowFlake;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.Date;
import java.util.List;
import java.util.UUID;


import static net.sf.jsqlparser.util.validation.metadata.NamedObject.user;

@Service
public class UserService {
    private static final Logger LOG = LoggerFactory.getLogger(UserService.class);
    @Resource
    private UserMapper userMapper;

    @Resource
    private SnowFlake snowFlake;

    @Resource
    private EmailUtil emailUtil;

    @Resource
    private UserEmailVerificationMapper userEmailVerificationMapper;

    public PageResp<UserQueryResp> list(UserQueryReq userQueryReq) {
        UserExample userExample = new UserExample();
        UserExample.Criteria criteria = userExample.createCriteria();
        if (!ObjectUtils.isEmpty(userQueryReq.getEmail())) {
            criteria.andNameLike("%" + userQueryReq.getEmail() + "%");
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
//        if (ObjectUtils.isEmpty(req.getId())) {
//            // 新增
//            if(selectByLoginName(user.getEmail())!=null){
//                throw new BusinessException(BusinessExceptionCode.USER_LOGIN_NAME_EXIST);
//
//            }else{
//                user.setId(snowFlake.nextId());//生成id的方法:自增,UUID,雪花算法...这里使用雪花算法
//                user.setEmail("default");
//                userMapper.insert(user);
//            }
//        } else {
//            // 更新
//            user.setLoginName(null);
//            user.setPassword(null);
//            userMapper.updateByPrimaryKeySelective(user);
//        }
        user.setEmail(null);
        user.setPassword(null);
        userMapper.updateByPrimaryKeySelective(user);
    }

    public void delete(Long id) {
        userMapper.deleteByPrimaryKey(id);
    }

    public User selectByEmail(String email) {
        UserExample userExample = new UserExample();
        UserExample.Criteria criteria = userExample.createCriteria();
        criteria.andEmailEqualTo(email);
        List<User> userList = userMapper.selectByExample(userExample);
        if (userList.size() == 0) {
            return null;
        } else {
            return userList.get(0);
        }
    }

    public void resetPassword(@Valid UserResetPasswordReq req) {
        User user = CopyUtil.copy(req, User.class);
        userMapper.updateByPrimaryKeySelective(user);
    }

    public UserLoginResp login(@Valid UserLoginReq req) {
        User user = selectByEmail(req.getEmail());
        if(user == null) {
            LOG.info("username does not exist {}", req.getEmail());
            throw new BusinessException(BusinessExceptionCode.LOGIN_USER_ERROR);
        } else {
            if(user.getPassword().equals(req.getPassword())) {
                // 登录成功
                return CopyUtil.copy(user, UserLoginResp.class);
            } else {
                LOG.info("password is incorrect, database password is {} and input password is {}", user.getPassword(), req.getPassword());
                throw new BusinessException(BusinessExceptionCode.LOGIN_USER_ERROR);
            }
        }
    }

    public void register(UserRegisterReq req) {
        //检查邮箱是否已经注册
        User user = selectByEmail(req.getEmail());
        if(user!=null){
            throw new BusinessException(BusinessExceptionCode.USER_LOGIN_NAME_EXIST);
        }
        //保存注册信息，设置激活状态为未激活
        User saveUser = CopyUtil.copy(req, User.class);
        saveUser.setIsActive(0);
        saveUser.setId(snowFlake.nextId());
        saveUser.setRole("user");
        userMapper.insert(saveUser);


        //生成激活码
        String code = UUID.randomUUID().toString();
        //设置激活码创建时间
        Date createTime = new Date();
        //设置激活码有效期
        Date expireTime = new Date(System.currentTimeMillis() + 3600*1000);
        //保存激活码
        UserEmailVerification userEmailVerification = new UserEmailVerification();
        userEmailVerification.setCreatedAt(createTime);
        userEmailVerification.setExpiresAt(expireTime);
        userEmailVerification.setToken(code);
        userEmailVerification.setUserId(saveUser.getId());
        userEmailVerification.setId(snowFlake.nextId());
        userEmailVerificationMapper.insert(userEmailVerification);

        //发送邮件
        String url = "http://localhost:8090/user/verify?email="+req.getEmail()+"&code="+code;
        emailUtil.sendEmail(req.getEmail(),url);
    }

    public void veryfyEmail(String token) {
        UserEmailVerificationExample example = new UserEmailVerificationExample();
        UserEmailVerificationExample.Criteria criteria = example.createCriteria();
        criteria.andTokenEqualTo(token);
        List<UserEmailVerification> verifications = userEmailVerificationMapper.selectByExample(example);
        if(verifications.isEmpty()){
            throw new BusinessException(BusinessExceptionCode.VERYFY_TOKEN_ERROR);
        }else{
            UserEmailVerification verification = verifications.get(0);
            if(verification.getExpiresAt().before(new Date())){
                throw new BusinessException(BusinessExceptionCode.VERYFY_TOKEN_ERROR);
            }else{
                User user = userMapper.selectByPrimaryKey(verification.getUserId());
                if(user == null){
                    throw new BusinessException(BusinessExceptionCode.VERYFY_USER_NOT_FOUND);
                }
                user.setIsActive(1);
                userMapper.updateByPrimaryKeySelective(user);
            }
        }
    }
}
