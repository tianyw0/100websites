package com.tianyongwei.controller;

import com.tianyongwei.config.MyWebUtil;
import com.tianyongwei.entity.core.Subject;
import com.tianyongwei.repo.SubjectRepo;
import com.tianyongwei.service.SubjectService;
import com.tianyongwei.utils.BaseController;
import com.tianyongwei.utils.JsonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/subject")
public class SubjectController  extends BaseController {

    @Autowired
    private SubjectService subjectService;

    @Autowired
    private SubjectRepo subjectRepo;

    @RequestMapping("/list")
    public String list () {
        return "subject/list";
    }

    @RequestMapping("/add")
    public String add() {
        return "subject/add";
    }

    @RequestMapping(value = "/add",method = RequestMethod.POST)
    @ResponseBody
    public JsonResult add_post(@RequestParam String name, @RequestParam String intro) {
        Long id = subjectService.add(name, intro, MyWebUtil.getCurrentUser().getId());
        if(id == null) {return renderError("新建专题失败！");}
        return renderSuccess("新建专题成功！", id);
    }

    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ResponseBody
    public JsonResult list_post (@PageableDefault(sort = { "id" }, direction = Sort.Direction.DESC)
                                         Pageable pageable) {
        Page<Subject> page = subjectService.mySubjectList(pageable);
        return renderSuccess(page);
    }


    @RequestMapping(value = "/del", method = RequestMethod.POST)
    @ResponseBody
    public JsonResult del(@RequestParam Long id) {
        Subject subject = subjectRepo.findOne(id);
        if(subject == null || MyWebUtil.getCurrentUser().getId() != subject.getUserId()) {
            return renderNopermission();
        }
        subjectService.del(id);
        return renderSuccess("删除成功！");
    }

    @RequestMapping(value = "saveEdit",method = RequestMethod.POST)
    @ResponseBody
    public JsonResult saveEdit(@RequestParam Long id,@RequestParam String name, @RequestParam String intro) {
        Subject subject = subjectRepo.findOne(id);
        if(subject == null || MyWebUtil.getCurrentUser().getId() != subject.getUserId()) {
            return renderNopermission();
        }
        subjectService.saveEdit(id,name,intro);
        return renderSuccess("编辑成功");

    }
}
