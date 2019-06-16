package com.kfit;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author ：youq
 * @date ：Created in 2019/6/16 13:35
 * @modified By：
 */
public class MyThread extends Thread{
    private Object lock;

//    @Autowired
    OrderService orderService;

    public MyThread(Object lock, OrderService order)
    {
        this.lock = lock;
        this.orderService = order;
    }
    public void run()
    {
        try
        {
            synchronized (lock)
            {
//                System.out.println("开始------wait time = " + System.currentTimeMillis());
                lock.wait();
                for (int i=0;i<5;i++){
                    orderService.goodSecKill();
                }
//                System.out.println("结束------wait time = " + System.currentTimeMillis());
            }
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
    }
}
