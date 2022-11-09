package com.easyfood.community.services;

import com.easyfood.community.entities.member.UserEntity;
import com.easyfood.community.enums.CommonResult;
import com.easyfood.community.interfaces.IResult;
import com.easyfood.community.mappers.IMemberMapper;
import com.easyfood.community.utils.CryptoUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service(value = "com.easyfood.community.services.MemberService")
public class MemberService {

private final IMemberMapper memberMapper;

    public MemberService(IMemberMapper memberMapper) {
        this.memberMapper = memberMapper;
    }

    @Transactional
    public IResult createUser(UserEntity user) {
        if (user.getEmail() == null ||
                user.getPassword() == null ||
                user.getName() == null ||
                user.getContact() == null ) {
            return CommonResult.FAILURE;
        }
        if (this.memberMapper.selectUserCountByName(user.getName()) > 0){
            return CommonResult.DUPLICATE;
        }
        user.setPassword(CryptoUtils.hashSha512(user.getPassword()));
        if (this.memberMapper.insertUser(user) == 0) {
            return CommonResult.FAILURE;
        }
        return CommonResult.SUCCESS;
    }

    @Transactional
    public IResult loginUser(UserEntity user) {
        if (user.getEmail() == null ||
                user.getPassword() == null ) {
            return CommonResult.FAILURE;
        }
        user.setPassword(CryptoUtils.hashSha512(user.getPassword()));
        UserEntity existingUser = this.memberMapper.selectUserByEmailPassword(user);
        if (existingUser == null) {
            return CommonResult.FAILURE;
        }
        user.setEmail(existingUser.getEmail())
                .setPassword(existingUser.getPassword())
                .setName(existingUser.getName())
                .setContact(existingUser.getContact())
                .setPolicyPrivacyAt(existingUser.getPolicyPrivacyAt())
                .setPolicyMarketingAt(existingUser.getPolicyMarketingAt())
                .setPolicyTermsAt(existingUser.getPolicyTermsAt())
                .setCreatedAt(existingUser.getCreatedAt())
                .setEmailAuth(existingUser.isEmailAuth());
        return CommonResult.SUCCESS;
    }

    @Transactional
    public IResult secessionUser(String email) {
        return this.memberMapper.deleteUser(email) > 0
                ? CommonResult.SUCCESS
                : CommonResult.FAILURE;
    }

    public UserEntity getUser(String email) {
        return this.memberMapper.selectUserByEmail(
                UserEntity.build().setEmail(email)
        );
    }
    @Transactional
    public IResult checkEmail(String email){
        return this.memberMapper.selectUserCountByEmail(email) > 0
                ? CommonResult.DUPLICATE
                : CommonResult.SUCCESS;
    }

}

