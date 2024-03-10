package com.tianyongwei.service;

import com.tianyongwei.entity.core.User;
import com.tianyongwei.repo.UserRepo;
import com.tianyongwei.utils.VerifyEmailUtil;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.mail.EmailException;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepo userRepo;

    /**
     * 查看邮箱是否被占用
     * @param email
     * @return
     */
    public boolean hasSignupByEmail(String email) {
        Boolean isVerified = true;
        List<User> users = userRepo.findByEmailAndIsVerified(email,isVerified);
        return users.size() > 0;
    }

    public User signup(String email, String psd1) {
        List<User> users = userRepo.findByEmailAndIsVerified(email,false);

        //发送验证邮件
        String randomCode = RandomStringUtils.randomAlphanumeric(5);
        try {
            VerifyEmailUtil.sendSignupEmail_text(email,randomCode);
        } catch (EmailException e) {
            e.printStackTrace();
        }

        //保存用户
        User user = null;
        if(users.size() == 1) {
            user = users.get(0);
        } else if(users.size() == 0) {
            user = new User();
        } else {
            throw new IllegalStateException("同一个邮箱查出多个用户");
        }
        user.setEmail(email);
        user.setPassword(DigestUtils.md5Hex("sa" + psd1 + "lt"));
        user.setUsername(email);
        user.setCreateTime(new DateTime().getMillis());
        user.setLevel("1");
        user.setvCode(randomCode);
        user.setVerified(false);
        user.setVerifiedTime(new DateTime().plusMinutes(5).getMillis());
        user = userRepo.saveAndFlush(user);
        return user;
    }

    public Integer emailVerify(String email, String vcode) {
        List<User> users = userRepo.findByEmail(email);
        if(users.size() > 1) {
            throw new IllegalStateException("一个邮箱查出多于一个账户");
        } else if(users.size() == 0) {
            return 0;//无此用户
        } else {
            User user = users.get(0);
            DateTime dt = new DateTime();

            if(!vcode.equals(user.getvCode())) {
                return 1;//验证码不对
            } else if(dt.isAfter(user.getVerifiedTime())) {
                return 2;//验证码过期
            } else {
                user.setVerified(true);
                userRepo.saveAndFlush(user);
                return 3;//成功
            }
        }
    }

    public User signin(String email, String password) {
        List<User> users = userRepo.findByEmail(email);
        if(users.size() == 1) {
            User user = users.get(0);
            System.out.println(DigestUtils.md5Hex("sa" + password + "lt"));
            if(user.getPassword().equals(DigestUtils.md5Hex("sa" + password + "lt"))) {
                return user;
            }
        }
        return null;
    }

    public Integer findpsd(String email) {
        List<User> users = userRepo.findByEmail(email);
        if(users.size() == 1) {
            String randomCode = RandomStringUtils.randomAlphanumeric(5);
            try {
                VerifyEmailUtil.sendPsdResetEmail_text(email,randomCode);
            } catch (EmailException e) {
                e.printStackTrace();
            }
            User user = users.get(0);
            user.setPsdResetCode(randomCode);
            user.setPsdResetVerified(false);
            user.setPsdResetVerifiedTime(new DateTime().plusMinutes(5).getMillis());
            userRepo.saveAndFlush(user);
            return 1;//成功
        }
        return 0;//失败
    }

    public boolean psdRestCodeIsValid(String email, String psdresetCode) {
        List<User> users = userRepo.findByEmail(email);
        if(users.size() != 1) {
            return false;
        }
        User user = users.get(0);
        if(!user.getPsdResetVerified()) {
            if(user.getPsdResetCode().equals(psdresetCode)) {
                if(new DateTime().isBefore(user.getPsdResetVerifiedTime())) {
                    return true;
                }
            }
        }
        return false;
    }

    public void psdReset(String email, String psd1) {
        User user = userRepo.findByEmail(email).get(0);
        user.setPassword(DigestUtils.md5Hex("sa"+psd1+"lt"));
        user.setPsdResetVerified(true);
        userRepo.saveAndFlush(user);
    }
}
