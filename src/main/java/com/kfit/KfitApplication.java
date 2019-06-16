package com.kfit;

import com.kfit.cache.JedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import com.kfit.Role;
import redis.clients.jedis.Transaction;
import redis.clients.jedis.Jedis;
import javax.annotation.Resource;

import java.util.List;

import static java.lang.Thread.sleep;

@RestController
@SpringBootApplication
public class KfitApplication {

    @Resource
    private Role roledd;
    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    @Autowired
    OrderService orderService;
    @Autowired
    private JedisUtil jedis;

    @RequestMapping("/")
    public String hello() {
//        Jedis j = jedis.getJedis();
//        j.watch("inventory");
//        Transaction transaction = j.multi();
//        transaction.set("inventory", String.valueOf(100));
//        List<Object> result = transaction.exec();
//        System.out.println(result);

        Jedis j = jedis.getJedis();
        Long count = j.incr("yyy");
        count = j.incr("yyy");
        count = j.incr("yyy");


        orderService.initInventory();
//        Object lock = new Object();
//        for (int i=0; i<100; i++){
//            MyThread mt0 = new MyThread(lock, orderService);
//            mt0.start();
//        }
//        try {
//            sleep(1000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        MyThread1 mt1 = new MyThread1(lock, orderService);
//        mt1.start();
//        return "Hello world!";

        Runnable x = new Runnable() {
            @Override
            public void run() {
//                System.out.println(Thread.currentThread().getName() + "启动时间是" + System.currentTimeMillis());
                int  num = 5;
                while (num>0) {
                    num--;
                    orderService.goodSecKill();
                }
            }
        };
//        System.out.println("开始创建线程");
        for (int i=0; i<100; i++) {
//            System.out.println("创建线程" + i);
            Thread t = new Thread(x);
            t.start();
        }
//        System.out.println("#####线程完成#####");
//        System.out.println("hello" + roledd.a);
        return "ok";
    }

    public static void main(String[] args) {
        SpringApplication.run(KfitApplication.class, args);
//        KfitApplication a = new KfitApplication();
//        a.hello();
    }

}

