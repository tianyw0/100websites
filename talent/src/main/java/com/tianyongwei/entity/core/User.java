package com.tianyongwei.entity.core;

import com.tianyongwei.entity.base.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table
public class User  extends BaseEntity {
    private String email;

    private String phone;

    private String loginThirdParty;

    private String weibo;

    private String qq;

    private String weixin;

    private String username;

    private String password;

    private String level;

    //验证码
    private String vCode;

    private Long viewCount;

    @Column(columnDefinition="tinyint(1)")
    private Boolean isVerified;

    private Long verifiedTime;

    //重置邮箱验证码
    private String psdResetCode;

    public User() {
    }

    public User(Long id) {
        super.setId(id);
    }

    @Column(columnDefinition="tinyint(1)")
    private Boolean psdResetVerified;

    private Long psdResetVerifiedTime;

    public String getPsdResetCode() {
        return psdResetCode;
    }

    public void setPsdResetCode(String psdResetCode) {
        this.psdResetCode = psdResetCode;
    }

    public Boolean getPsdResetVerified() {
        return psdResetVerified;
    }

    public void setPsdResetVerified(Boolean psdResetVerified) {
        this.psdResetVerified = psdResetVerified;
    }

    public Long getPsdResetVerifiedTime() {
        return psdResetVerifiedTime;
    }

    public void setPsdResetVerifiedTime(Long psdResetVerifiedTime) {
        this.psdResetVerifiedTime = psdResetVerifiedTime;
    }

    public Long getVerifiedTime() {
        return verifiedTime;
    }

    public void setVerifiedTime(Long verifiedTime) {
        this.verifiedTime = verifiedTime;
    }

    public Boolean getVerified() {
        return isVerified;
    }

    public void setVerified(Boolean verified) {
        isVerified = verified;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getLoginThirdParty() {
        return loginThirdParty;
    }

    public void setLoginThirdParty(String loginThirdParty) {
        this.loginThirdParty = loginThirdParty;
    }

    public String getWeibo() {
        return weibo;
    }

    public void setWeibo(String weibo) {
        this.weibo = weibo;
    }

    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }

    public String getWeixin() {
        return weixin;
    }

    public void setWeixin(String weixin) {
        this.weixin = weixin;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getvCode() {
        return vCode;
    }

    public void setvCode(String vCode) {
        this.vCode = vCode;
    }

    public Long getViewCount() {
        return viewCount;
    }

    public void setViewCount(Long viewCount) {
        this.viewCount = viewCount;
    }
}
