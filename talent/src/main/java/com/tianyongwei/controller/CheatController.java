package com.tianyongwei.controller;

import com.tianyongwei.entity.core.CheatSheet;
import com.tianyongwei.entity.core.Subject;
import com.tianyongwei.service.CheatSheetService;
import com.tianyongwei.service.SubjectService;
import com.tianyongwei.utils.BaseController;
import com.tianyongwei.utils.JsonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/cheat")
public class CheatController extends BaseController {

  @Autowired
  private CheatSheetService cheatSheetService;

  @Autowired
  private SubjectService subjectService;

  @RequestMapping("/read/subject/{subjectId}")
  public String read(@PathVariable Long subjectId, Model model) {
    System.out.println(new Date());
    Subject subject = subjectService.subjectInfo(subjectId);
    model.addAttribute("subject",subject);
    return "cheat/read";
  }

  @RequestMapping(value = "/list",method = RequestMethod.POST)
  @ResponseBody
  public JsonResult list_post(@RequestParam Long subjectId) {
    List<CheatSheet> cheatSheets = cheatSheetService.myCheats(subjectId);
    return renderSuccess(cheatSheets);
  }

  @RequestMapping("/edit/subject/{subjectId}")
  public String edit(@PathVariable Long subjectId, Model model) {
    Subject subject = subjectService.subjectInfo(subjectId);
    model.addAttribute("subject",subject);
    return "cheat/edit";
  }

  @RequestMapping(value = "/del",method = RequestMethod.POST)
  @ResponseBody
  public JsonResult del(@RequestParam Long id) {
    cheatSheetService.del(id);
    return renderSuccess();
  }

  @RequestMapping(value = "/add",method = RequestMethod.POST)
  @ResponseBody
  public JsonResult add(@RequestParam Long subjectId, @RequestParam String title, @RequestParam String content) {
    CheatSheet cheatSheet = cheatSheetService.add(subjectId,title,content);
    return renderSuccess(cheatSheet);
  }

  @RequestMapping(value = "/saveEdit",method = RequestMethod.POST)
  @ResponseBody
  public JsonResult saveEdit(@RequestParam Long id, @RequestParam String title,@RequestParam String content) {
    CheatSheet cheatSheet = cheatSheetService.saveEdit(id,title,content);
    return renderSuccess(cheatSheet);
  }
}
