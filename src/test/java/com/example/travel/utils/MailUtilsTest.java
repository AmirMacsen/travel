package com.example.travel.utils;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
public class MailUtilsTest {
    @Autowired
    MailUtils mailUtils;

    @Test
    public void sendMail() {
        mailUtils.sendMail("jokerjasonme@gmail.com","测试邮件","测试邮件");
    }
}