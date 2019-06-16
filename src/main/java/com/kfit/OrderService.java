package com.kfit;

import com.kfit.cache.JedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;

import java.util.List;

import static java.lang.Thread.sleep;

/**
 * @author ：youq
 * @date ：Created in 2019/6/16 13:55
 * @modified By：
 */
// 三.redis事务之watch
//  首先要了解redis事务中watch的作用，watch命令可以监控一个或多个键，一旦其中有一个键被修改（或删除），
// 之后的事务就不会执行。监控一直持续到exec命令（事务中的命令是在exec之后才执行的，
// 所以在multi命令后可以修改watch监控的键值）。假设我们通过watch命令在事务执行之前监控了多个Keys
// ，倘若在watch之后有任何Key的值发生了变化，exec命令执行的事务都将被放弃，
//  同时返回Null multi-bulk应答以通知调用者事务执行失败。
//  所以，需要注意的是watch监控键之后，再去操作这些键，否则watch可能会起不到效果
//  原文：https://blog.csdn.net/u010180031/article/details/73695426
//通过redis的事务机制实现秒杀机制,使用redis的watch命令进行实现
@Service
public class OrderService {
    @Autowired
    private JedisUtil jedis;
    public void initInventory(){
        String inventory = jedis.get("inventory");
        if (Integer.parseInt(inventory) <= 0){
            jedis.set("inventory","10");
        }
        System.out.println("原始库存"+jedis.get("inventory"));
    }

    public   /*synchronized */ void goodSecKill() {
        Transaction transaction = null;
        List<Object> result;
        try {
            Jedis j = jedis.getJedis();
            j.watch("inventory");
            int inventory = Integer.parseInt(j.get("inventory"));
            try {
                sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (inventory > 0) {
//                jedis.set("inventory", String.valueOf(inventory-1));
                transaction = j.multi();//开启事务
                transaction.set("inventory", String.valueOf(inventory - 1));//设置库存，事务未提交
//                transaction.set("product_sku", String.valueOf(inventory - 1));
                result = transaction.exec();//提交事务，如果被别的线程改过值，则会提交失败。
                if (result == null || result.size()==0) {
                    System.out.println("抢购失败！ 被其他人抢先了 ： " + (inventory-1));
//                    System.out.println("Transaction error...");// 可能是watch-key被外部修改，或者是数据操作被驳回
//                    transaction.discard();  //watch-key被外部修改时，discard操作会被自动触发
                }else{
                    System.out.println("抢购成功 剩余库存" + (inventory-1));
                }
//            System.out.println(list);
            } else {
                System.out.println("抢购失败！ 库存为 ： " + inventory);
            }
        } catch (Exception e) {
            transaction.discard();
            e.printStackTrace();
        }
    }
}
