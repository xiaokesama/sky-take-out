package com.sky.mapper;

import com.sky.entity.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper {

    // 根据openid查询用户信息
    @Select("SELECT * FROM user WHERE openid = #{openId}")
    User getByOpenid(String openId);

    int insert(User user);

    @Select("SELECT * FROM user WHERE id = #{id}")
    User getById(Long userId);
}
