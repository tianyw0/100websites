package com.tianyongwei.utils;

import org.apache.commons.mail.*;

import java.net.MalformedURLException;
import java.net.URL;

public class VerifyEmailUtil {


    private static final String USER = "869358189";
    private static final String PASS = "tmhxymhnjvmpbedi";
    private static final String FROMMAIL = "869358189@qq.com";
    private static final String HOSTNAME = "smtp.qq.com";
    private static final String DOMAIN = "talent.tianyongwei.com";



    public static void sendSignupEmail_Html(String destAddress ,String verifyCode) throws EmailException, MalformedURLException {
         String verifyUrl = "http://"+ DOMAIN +"/user/emailverify?email="+destAddress+"&vcode="+verifyCode;

        // Create the email message
        HtmlEmail email = new HtmlEmail();
        email.setHostName(HOSTNAME);
        email.setSmtpPort(25);
        email.setAuthenticator(new DefaultAuthenticator(USER, PASS));
        email.addTo(destAddress, destAddress);
        email.setFrom(FROMMAIL);
        email.setSubject("邮箱验证");
        email.setCharset("utf-8");

        // embed the image and get the content id
        URL url = new URL(verifyUrl);
        email.embed(url, "邮箱验证地址");

        // set the html message
        email.setHtmlMsg("<html>点击该地址进行邮箱验证：<a href='"+verifyUrl+"'>"+verifyUrl+"</a></html>");

        // set the alternative message
        email.setTextMsg("请访问此链接验证您的邮箱：");

        // send the email
        email.send();
    }

    public static void sendSignupEmail_text(String destAddress ,String verifyCode) throws EmailException {

        String verifyUrl = "http://"+DOMAIN+"/user/emailverify?email="+destAddress+"&vcode="+verifyCode;
        Email email = new SimpleEmail();
        email.setHostName(HOSTNAME);
        email.setSmtpPort(25);
        email.setAuthenticator(new DefaultAuthenticator(USER, PASS));
        email.setSSLOnConnect(true);
        email.setFrom(FROMMAIL);
        email.setSubject("邮件验证");
        email.setMsg("请访问下面地址进行验证邮箱:\n"+verifyUrl);
        email.addTo(destAddress);
        email.send();
    }

    public static void sendPsdResetEmail_text(String destAddress, String verifyCode) throws EmailException {
        String verifyUrl = "http://"+DOMAIN+"/user/psdresetverify?email="+destAddress+"&vcode="+verifyCode;
        Email email = new SimpleEmail();
        email.setHostName(HOSTNAME);
        email.setSmtpPort(25);
        email.setAuthenticator(new DefaultAuthenticator(USER, PASS));
        email.setSSLOnConnect(true);
        email.setFrom(FROMMAIL);
        email.setSubject("邮件验证");
        email.setMsg("请访问下面地址进行重置密码:\n"+verifyUrl);
        email.addTo(destAddress);
        email.send();
    }

//    public static void main(String[] args) throws MalformedURLException, EmailException {
//        sendSignupEmail("869358189@qq.com","xxxxxx");
//        sendSignupEmail_text("869358189@qq.com","xxxxxx");
//    }
}
