package com.tianyongwei.controller;

import com.tianyongwei.config.MyWebUtil;
import com.tianyongwei.utils.BaseController;
import com.tianyongwei.utils.JsonResult;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 该controller访问需要登录权限，拦截器生效
 */
@Controller
@RequestMapping("/user/permission")
public class UserPermissionController extends BaseController{

    @RequestMapping(value = "/info", method = RequestMethod.POST)
    @ResponseBody
    public JsonResult info(@RequestParam String id) {
        return renderSuccess("success",MyWebUtil.getCurrentUser());
    }
}