package com.hlgf.wx;

import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author ccr12312@163.com at 2018-10-29
 */

@RunWith(SpringRunner.class)
@SpringBootTest(classes = WeixinSMSApplication.class)
public class TestTemplate {
    @Before
    public void init() {
        System.out.println("开始测试-----------------");
    }

    @After
    public void after() {
        System.out.println("测试结束-----------------");
    }

}
