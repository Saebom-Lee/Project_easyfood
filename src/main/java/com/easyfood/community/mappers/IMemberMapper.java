package com.easyfood.community.mappers;

import com.easyfood.community.entities.member.UserEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface IMemberMapper {

    int insertUser(UserEntity user);
    UserEntity selectUserByEmailPassword(UserEntity user);
    UserEntity selectUserByEmail(UserEntity user);
    int selectUserCountByEmail(@Param(value = "email") String email);
    int selectUserCountByName(@Param(value = "name") String name);
    int deleteUser(@Param(value = "email") String email);


}
