package com.tianyongwei.controller;

import com.tianyongwei.config.MyWebUtil;
import com.tianyongwei.entity.core.User;
import com.tianyongwei.repo.UserRepo;
import com.tianyongwei.service.UserService;
import com.tianyongwei.utils.BaseController;
import com.tianyongwei.utils.JsonResult;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/user")
public class UserController extends BaseController{

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private UserService userService;

    //注册页面
    @RequestMapping("/signup")
    public String signup(Model model, HttpSession session, HttpServletResponse response) {
        return "user/signup";
    }

    @RequestMapping(value = "/signup", method = RequestMethod.POST)
    @ResponseBody
    public JsonResult signup_post(@RequestParam String email, @RequestParam String psd1, @RequestParam String psd2) {
        /**
         * 异常情况：
         * 0、邮箱格式错误
         * 1、邮箱已注册成功,被占用
         * 2、密码不一致
         * 3、密码长度
         *
         * 步骤：
         * 1、验证异常情况
         * 2、已注册未验证，验证是否过期
         * 3、发送邮箱验证
         * 4、插入数据或更新数据
         */
        //使用apache的邮箱验证类
        EmailValidator emailValidator = EmailValidator.getInstance();
        if(!emailValidator.isValid(email)) {
            return renderError("邮箱不合法");
        }

        if(userService.hasSignupByEmail(email)) {
            return renderError("该邮箱已经注册");
        }

        if(StringUtils.isEmpty(psd1) || StringUtils.isEmpty(psd2)){
            return renderError("密码不能为空");
        }

        if(!psd1.equals(psd2)) {
            return renderError("两次密码不一致");
        }

        if(psd1.length() < 6 || psd1.length() > 18) {
            return renderError("长度需要在6-18位之间");
        }

        User user = userService.signup(email,psd1);
        System.out.println(user.getId());
        return renderSuccess("http://mail." + email.split("@")[1]);
    }

    @RequestMapping(value = "/emailverify")
    public String verify_post(Model model , @RequestParam String email, @RequestParam String vcode) {
        /**
         * 0、没有这条记录
         * 1、验证码错误
         * 2、验证码过期
         * 3、验证成功
         */
        Integer code = userService.emailVerify(email, vcode);
        if(code == 0) {
            model.addAttribute("message","没有这条记录");
        } else if (code == 1) {
            model.addAttribute("message","验证码错误");
        } else if (code == 2) {
            model.addAttribute("message","验证码过期");
        } else if (code == 3) {
            model.addAttribute("message","验证成功");
        }
        return "user/emailverify";
    }

    //重置密码页面
    @RequestMapping("/findpsd")
    public String findpsd() {
        return "user/findpsd";
    }

    //重置密码页面
    @RequestMapping(value = "/findpsd", method = RequestMethod.POST)
    @ResponseBody
    public JsonResult findpsd(@RequestParam String email) {
        Integer code = userService.findpsd(email);
        if(code == 0) {
            return renderError("无此账号");
        } else {
            return renderSuccess("重置链接已经发送，请前往邮箱重置！");
        }
    }

    @RequestMapping("/psdresetverify")
    public String psdresetverify (Model model,@RequestParam String email , @RequestParam("vcode") String psdresetCode) {
        model.addAttribute("psdresetCode",psdresetCode);
        model.addAttribute("email",email);
        return "user/psdreset";
    }

    @RequestMapping(value = "/psdresetverify",method = RequestMethod.POST)
    @ResponseBody
    public JsonResult psdresetverify_post (@RequestParam String email ,
                                           @RequestParam String psdresetCode,
                                           @RequestParam String psd1,
                                           @RequestParam String psd2) {
        /**
         * 1、密码重置成功
         * 2、验证码过期
         * 3、两次密码不一致
         */
        if(StringUtils.isEmpty(psd1) || StringUtils.isEmpty(psd2)) {
            return renderError("密码为空");
        }
        if(!psd1.equals(psd2)) {
            return renderError("密码不一致");
        }
        if(!userService.psdRestCodeIsValid(email,psdresetCode)) {
            return renderError("验证码无效");
        }
        userService.psdReset(email,psd1);
        return renderSuccess("重置密码成功");
    }

    //登录页面
    @RequestMapping("/signin")
    public String signin() {
        return "user/signin";
    }

    //登录页面
    @RequestMapping(value = "/signin",method = RequestMethod.POST)
    @ResponseBody
    public JsonResult signin_post(@RequestParam String email, @RequestParam String password) {
        User user = userService.signin(email,password);

        if(user != null) {
            User sessionUser = new User(user.getId());
            sessionUser.setUsername(user.getUsername());
            MyWebUtil.saveUser2Session(sessionUser);
            return renderSuccess("登录成功",MyWebUtil.getSession().getAttribute("BlockedURI"));
        } else {
            return renderError("账号或密码错误");
        }
    }

    @RequestMapping(value = "/checklogin", method = RequestMethod.POST)
    @ResponseBody
    public JsonResult checkLogin() {
        if(MyWebUtil.getCurrentUser() != null) {
            return renderSuccess(MyWebUtil.getCurrentUser());
        }
        return renderError();
    }

    @RequestMapping(value = "/signout", method = RequestMethod.POST)
    @ResponseBody
    public JsonResult signOut () {
        MyWebUtil.removeUserFromSession();
        return renderSuccess("安全退出");
    }
}
