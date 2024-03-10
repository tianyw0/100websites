package com.tianyongwei.entity.social;


import com.tianyongwei.entity.base.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table
public class CollectCheatSheet  extends BaseEntity {

    private Long userId;

    private Long cheatSheetId;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getCheatSheetId() {
        return cheatSheetId;
    }

    public void setCheatSheetId(Long cheatSheetId) {
        this.cheatSheetId = cheatSheetId;
    }
}
