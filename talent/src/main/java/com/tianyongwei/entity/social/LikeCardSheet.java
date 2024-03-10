package com.tianyongwei.entity.social;

import com.tianyongwei.entity.base.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table
public class LikeCardSheet extends BaseEntity {
    private Long userId;

    private Long cardSheetId;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getCardSheetId() {
        return cardSheetId;
    }

    public void setCardSheetId(Long cardSheetId) {
        this.cardSheetId = cardSheetId;
    }
}
