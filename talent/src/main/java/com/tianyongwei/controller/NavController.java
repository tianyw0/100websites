package com.tianyongwei.controller;

import com.tianyongwei.config.MyWebUtil;
import com.tianyongwei.entity.core.Navigation;
import com.tianyongwei.entity.core.Subject;
import com.tianyongwei.repo.SubjectRepo;
import com.tianyongwei.service.NavigationService;
import com.tianyongwei.utils.BaseController;
import com.tianyongwei.utils.JsonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping("/nav")
public class NavController extends BaseController {

  @Autowired
  private SubjectRepo subjectRepo;

  @Autowired
  private NavigationService navigationService;

  @RequestMapping("/edit/subject/{subjectId}")
  public String edit(@PathVariable Long subjectId, ModelAndView modelAndView, Model model) {
    Subject subject = subjectRepo.findOne(subjectId);
    if(subject == null || MyWebUtil.getCurrentUser().getId() != subject.getUserId()) {
      return "nopermission";
    }
    model.addAttribute("subject",subject);
    return "nav/edit";
  }

  @RequestMapping("/read/subject/{subjectId}")
  public String read(@PathVariable Long subjectId, ModelAndView modelAndView, Model model) {
    Subject subject = subjectRepo.findOne(subjectId);
    if(subject == null || MyWebUtil.getCurrentUser().getId() != subject.getUserId()) {
      return "nopermission";
    }
    model.addAttribute("subject",subject);
    return "nav/read";
  }

  @RequestMapping(value = "/list",method = RequestMethod.POST)
  @ResponseBody
  public JsonResult list_post(@RequestParam Long subjectId) {
    List<Navigation> navigations = navigationService.getNavListBySubjectId(subjectId);
    return renderSuccess(navigations);
  }

  @RequestMapping(value = "/add",method = RequestMethod.POST)
  @ResponseBody
  public JsonResult add(@RequestParam Long subjectId,@RequestParam String title, @RequestParam String url) {
    Navigation navigation = navigationService.add(subjectId,title,url);
    if(navigation == null) {
      return renderError("新建导航失败");
    }
    return renderSuccess(navigation);
  }

  @RequestMapping(value = "/del",method = RequestMethod.POST)
  @ResponseBody
  public JsonResult del(@RequestParam Long id) {
    navigationService.del(id);
    return renderSuccess();
  }

  @RequestMapping(value = "/saveEdit",method = RequestMethod.POST)
  @ResponseBody
  public JsonResult saveEdit(@RequestParam Long id ,@RequestParam String title ,@RequestParam String url){
    Navigation navigation = navigationService.saveEdit(id,title,url);
    return renderSuccess(navigation);
  }
}
