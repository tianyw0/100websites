package com.tianyongwei.service;

import com.tianyongwei.config.MyWebUtil;
import com.tianyongwei.entity.core.CardSheet;
import com.tianyongwei.repo.CardSheetRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CardSheetService {

    @Autowired
    private CardSheetRepo cardSheetRepo;

    public List<CardSheet> myCards(Long subjectId) {
        return cardSheetRepo.findByUserIdAndSubjectIdAndIsDrop(MyWebUtil.getCurrentUser().getId(),subjectId, false);
    }

    public CardSheet add(Long subjectId, String question, String answer) {
        CardSheet cardSheet = new CardSheet();
        cardSheet.setSubjectId(subjectId);
        cardSheet.setQuestion(question);
        cardSheet.setAnswer(answer);
        cardSheet.setUserId(MyWebUtil.getCurrentUser().getId());
        cardSheet = cardSheetRepo.saveAndFlush(cardSheet);
        return cardSheet;
    }

    public CardSheet saveEdit(Long id, String question, String answer) {
        CardSheet cardSheet = cardSheetRepo.findOne(id);
        cardSheet.setAnswer(answer);
        cardSheet.setQuestion(question);
        cardSheet = cardSheetRepo.saveAndFlush(cardSheet);
        return cardSheet;
    }

    public void del(Long id) {
        CardSheet cardSheet = cardSheetRepo.findOne(id);
        cardSheet.setDrop(true);
        cardSheetRepo.saveAndFlush(cardSheet);
    }
}
