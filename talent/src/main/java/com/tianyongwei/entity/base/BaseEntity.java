package com.tianyongwei.entity.base;

import javax.persistence.*;

@MappedSuperclass
public class BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long createTime;

    private Long updateTime;

    private Long createUser;

    private Long updateUser;

    //逻辑删除
    @Column(columnDefinition="tinyint(1)")
    private Boolean isDrop = false;

    //上下架
    @Column(columnDefinition="tinyint(1)")
    private Boolean isShangjia = true;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public Long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Long updateTime) {
        this.updateTime = updateTime;
    }

    public Long getCreateUser() {
        return createUser;
    }

    public void setCreateUser(Long createUser) {
        this.createUser = createUser;
    }

    public Long getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(Long updateUser) {
        this.updateUser = updateUser;
    }

    public Boolean getDrop() {
        return isDrop;
    }

    public void setDrop(Boolean drop) {
        isDrop = drop;
    }

    public Boolean getShangjia() {
        return isShangjia;
    }

    public void setShangjia(Boolean shangjia) {
        isShangjia = shangjia;
    }
}
