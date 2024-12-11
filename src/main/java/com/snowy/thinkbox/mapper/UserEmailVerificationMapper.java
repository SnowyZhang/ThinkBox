package com.snowy.thinkbox.mapper;

import com.snowy.thinkbox.domain.UserEmailVerification;
import com.snowy.thinkbox.domain.UserEmailVerificationExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface UserEmailVerificationMapper {
    long countByExample(UserEmailVerificationExample example);

    int deleteByExample(UserEmailVerificationExample example);

    int deleteByPrimaryKey(Long id);

    int insert(UserEmailVerification row);

    int insertSelective(UserEmailVerification row);

    List<UserEmailVerification> selectByExample(UserEmailVerificationExample example);

    UserEmailVerification selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("row") UserEmailVerification row, @Param("example") UserEmailVerificationExample example);

    int updateByExample(@Param("row") UserEmailVerification row, @Param("example") UserEmailVerificationExample example);

    int updateByPrimaryKeySelective(UserEmailVerification row);

    int updateByPrimaryKey(UserEmailVerification row);
}