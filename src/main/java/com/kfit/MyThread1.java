package com.kfit;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author ：youq
 * @date ：Created in 2019/6/16 13:35
 * @modified By：
 */
public class MyThread1 extends Thread{

    private Object lock;
    OrderService orderService;
    public MyThread1(Object lock, OrderService order)
    {
        this.lock = lock;
        this.orderService = order;
    }
    public void run()
    {
        synchronized (lock)
        {
//            System.out.println("开始------notify time = " + System.currentTimeMillis());
            lock.notifyAll();
            for (int i=0;i<5;i++){
                orderService.goodSecKill();
            }
//            System.out.println("结束------notify time = " + System.currentTimeMillis());
        }
    }
}
