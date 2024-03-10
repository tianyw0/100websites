package com.tianyongwei.controller;

import com.tianyongwei.entity.core.CardSheet;
import com.tianyongwei.service.CardSheetService;
import com.tianyongwei.utils.BaseController;
import com.tianyongwei.utils.JsonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/card")
public class CardController extends BaseController {

  @Autowired
  private CardSheetService cardService;

  @RequestMapping(value = "/edit/subject/{subjectId}")
  public String add(@PathVariable Long subjectId, Model model) {
    model.addAttribute("subjectId",subjectId);
    return "card/edit";
  }

  @RequestMapping(value = "/list", method = RequestMethod.POST)
  @ResponseBody
  public JsonResult list_post (@RequestParam Long subjectId){
    List<CardSheet> cards = cardService.myCards(subjectId);
    return renderSuccess(cards);
  }

  @RequestMapping(value = "/add",method = RequestMethod.POST)
  @ResponseBody
  public JsonResult add_post(@RequestParam Long subjectId, @RequestParam String question, @RequestParam String answer) {
    CardSheet cardSheet = cardService.add(subjectId,question,answer);
    return renderSuccess(cardSheet);
  }

  @RequestMapping(value = "/saveEdit", method = RequestMethod.POST)
  @ResponseBody
  public JsonResult saveEdit(@RequestParam Long id, @RequestParam String question, @RequestParam String answer) {
    CardSheet cardSheet = cardService.saveEdit(id, question, answer);
    return renderSuccess(cardSheet);
  }

  @RequestMapping(value = "/del", method = RequestMethod.POST)
  @ResponseBody
  public JsonResult del(@RequestParam Long id) {
    cardService.del(id);
    return renderSuccess("删除成功！");
  }

  @RequestMapping("/read/subject/{subjectId}")
  public String read(@PathVariable Long subjectId, Model model) {
    List<CardSheet> cards = cardService.myCards(subjectId);
    model.addAttribute("cards",cards);
    model.addAttribute("subjectId",subjectId);
    return "card/read";
  }
}
