package com.tianyongwei.entity.social;

import com.tianyongwei.entity.base.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table
public class LikeCheatSheet  extends BaseEntity {
    private Long userId;

    private Long cheatSheet;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getCheatSheet() {
        return cheatSheet;
    }

    public void setCheatSheet(Long cheatSheet) {
        this.cheatSheet = cheatSheet;
    }
}
