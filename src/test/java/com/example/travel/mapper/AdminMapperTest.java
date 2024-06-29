package com.example.travel.mapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.Assert.*;

@SpringBootTest
public class AdminMapperTest {

    @Autowired
    private AdminMapper adminMapper;

    @Test
    public void findDesc() {
        System.out.println(adminMapper.findDesc(1));
    }
}