package com.easyfood.community.entities.member;

import java.util.Date;
import java.util.Objects;

public class UserEntity {

    public static final String ATTRIBUTE_NAME = "memberUser";
    public static final String ATTRIBUTE_NAME_PLURAL = "memberUsers";

    public static UserEntity build() {
        return new UserEntity();
    }

    private String email;
    private String name;
    private String password;
    private String contact;
    private Date createdAt = new Date();
    private Date policyTermsAt = new Date();
    private Date policyPrivacyAt = new Date();
    private Date policyMarketingAt;
    private boolean isEmailAuth = false;

    public UserEntity() {

    }

    public UserEntity(String email, String name, String password, String contact, Date createdAt, Date policyTermsAt, Date policyPrivacyAt, Date policyMarketingAt, boolean isEmailVerified) {
        this.email = email;
        this.name = name;
        this.password = password;
        this.contact = contact;
        this.createdAt = createdAt;
        this.policyTermsAt = policyTermsAt;
        this.policyPrivacyAt = policyPrivacyAt;
        this.policyMarketingAt = policyMarketingAt;
        this.isEmailAuth = isEmailVerified;
    }

    public String getEmail() {
        return email;
    }

    public UserEntity setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getName() {
        return name;
    }

    public UserEntity setName(String name) {
        this.name = name;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public UserEntity setPassword(String password) {
        this.password = password;
        return this;
    }

    public String getContact() {
        return contact;
    }

    public UserEntity setContact(String contact) {
        this.contact = contact;
        return this;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public UserEntity setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public Date getPolicyTermsAt() {
        return policyTermsAt;
    }

    public UserEntity setPolicyTermsAt(Date policyTermsAt) {
        this.policyTermsAt = policyTermsAt;
        return this;
    }

    public Date getPolicyPrivacyAt() {
        return policyPrivacyAt;
    }

    public UserEntity setPolicyPrivacyAt(Date policyPrivacyAt) {
        this.policyPrivacyAt = policyPrivacyAt;
        return this;
    }

    public Date getPolicyMarketingAt() {
        return policyMarketingAt;
    }

    public UserEntity setPolicyMarketingAt(Date policyMarketingAt) {
        this.policyMarketingAt = policyMarketingAt;
        return this;
    }

    public boolean isEmailAuth() {
        return isEmailAuth;
    }

    public UserEntity setEmailAuth(boolean emailAuth) {
        isEmailAuth = emailAuth;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserEntity that = (UserEntity) o;
        return Objects.equals(email, that.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email);
    }
}
