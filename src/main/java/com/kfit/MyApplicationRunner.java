package com.kfit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Date;

import static java.lang.Thread.sleep;

/**
 * @author ：youq
 * @date ：Created in 2019/6/15 21:59
 * @modified By：
 */
@Component//被spring容器管理
public class MyApplicationRunner implements ApplicationRunner {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    void  testfun(){
        redisTemplate.opsForValue().increment("inventory",100);
        System.out.println("***************"+redisTemplate.opsForValue().get("inventory"));
    }

    @Override
    public void run(ApplicationArguments applicationArguments) throws Exception {
        System.out.println("-------------->" + "项目启动，now=" + new Date());
//        MyApplicationRunner a = new MyApplicationRunner();
//        a.testfun();

/*
        Runnable x = new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        sleep(1000);
                    } catch (InterruptedException e) {

                    }
                    System.out.println(this.getClass());
                }
            }
        };
        Thread t = new Thread(x);
        t.start();*/
    }
}
