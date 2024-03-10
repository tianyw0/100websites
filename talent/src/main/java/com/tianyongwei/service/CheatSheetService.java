package com.tianyongwei.service;

import com.tianyongwei.config.MyWebUtil;
import com.tianyongwei.entity.core.CheatSheet;
import com.tianyongwei.repo.CheatSheetRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CheatSheetService {

  @Autowired
  private CheatSheetRepo cheatSheetRepo;

  public List<CheatSheet> myCheats(Long subjectId) {
    return cheatSheetRepo.findByUserIdAndSubjectIdAndIsDrop(MyWebUtil.getCurrentUser().getId(),subjectId, false);
  }

  public void del(Long id) {
    CheatSheet cheatSheet = cheatSheetRepo.findOne(id);
    cheatSheet.setDrop(true);
    cheatSheetRepo.saveAndFlush(cheatSheet);
  }


  public CheatSheet add(Long subjectId, String title, String content) {
    CheatSheet cheatSheet = new CheatSheet();
    cheatSheet.setTitle(title);
    cheatSheet.setSubjectId(subjectId);
    cheatSheet.setContent(content);
    cheatSheet.setUserId(MyWebUtil.getCurrentUser().getId());
    cheatSheet = cheatSheetRepo.saveAndFlush(cheatSheet);
    return cheatSheet;
  }

  public CheatSheet saveEdit(Long id, String title, String content) {
    CheatSheet cheatSheet = cheatSheetRepo.findOne(id);
    cheatSheet.setContent(content);
    cheatSheet.setTitle(title);
    cheatSheet = cheatSheetRepo.saveAndFlush(cheatSheet);
    return cheatSheet;
  }
}
