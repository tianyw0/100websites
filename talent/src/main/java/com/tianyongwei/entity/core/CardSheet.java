package com.tianyongwei.entity.core;

import com.tianyongwei.entity.base.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table
public class CardSheet  extends BaseEntity {
    private Long subjectId;

    private Long userId;

    private String question;

    private String answer;

    private Long rankNum;

    private Boolean isTop;

    private Boolean isFromAnki;

    private Long viewCount;

    public Long getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(Long subjectId) {
        this.subjectId = subjectId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public Long getRankNum() {
        return rankNum;
    }

    public void setRankNum(Long rank) {
        this.rankNum = rank;
    }

    public Boolean getTop() {
        return isTop;
    }

    public void setTop(Boolean top) {
        isTop = top;
    }

    public Boolean getFromAnki() {
        return isFromAnki;
    }

    public void setFromAnki(Boolean fromAnki) {
        isFromAnki = fromAnki;
    }

    public Long getViewCount() {
        return viewCount;
    }

    public void setViewCount(Long viewCount) {
        this.viewCount = viewCount;
    }
}
