package com.kfit;

import javax.annotation.Resource;
//import javax.transaction.Transactional;
//import javax.persistence.Cacheable;

import com.kfit.dao.RoleMapper;
import com.kfit.dao.StuMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
//import org.apache.el.stream.Optional;
//import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.domain.Example;
import org.springframework.data.redis.core.StringRedisTemplate;
//import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import com.kfit.domain.Stu;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DemoInfoServiceImpl implements DemoInfoService {
    //这里的单引号不能少，否则会报错，被识别是一个对象;
//    public static final String CACHE_KEY = "'demoInfo'";
//    public static final String DEMO_CACHE_NAME = "demo";

//    @Autowired
//    private RedisTemplate<String, String> redisTemplate;

    //@Resource
    private DemoInfoRepository demoInfoRepository;

    @Resource
    private StuMapper stuMapper;

    @Resource
    private RoleService roleService;

    //@Resource
    // private RedisTemplate<String, String> redisTemplate;

    @Override
    //@Transactional //事务
    public void save(demoinfo User) {


       // return demoInfoRepository.save(User);
    }

    //@Transactional(isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
//    @Transactional(propagation = Propagation.REQUIRED)
//    void test2(Stu stu){
//        stuMapper.insert(stu);
//        int z = 1/0;
//    }
    //@Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void test() {

        int num = new Double(Math.random() * 1000000).intValue();
        String str = String.valueOf(num);
        Stu stu = new Stu();
        stu.setName("YOUQIANG" + str);
        stu.setSex("male"+str);
        stu.setAge("35"+str);
        stuMapper.insert(stu);

        List<Stu> list = new ArrayList<>();
        for (int i=0; i<2; i++){
            num = new Double(Math.random() * 1000000).intValue();
            str = String.valueOf(num);
            stu = new Stu();
            if(i==1) {
                stu.setName("hell0" + str);
            }
            if(i==2) {
                stu.setName("hell2" + str);
            }
            stu.setSex("male"+str);
            stu.setAge("35"+str);
            try{
                roleService.test2(stu);
           }catch (Exception e){
               e.printStackTrace();
            }

        }


        // ValueOperations<String, String> valueOperations =
        // redisTemplate.opsForValue();
        // valueOperations.set("mykey4", "random1=" + Math.random());
        // System.out.println(valueOperations.get("mykey4"));
    }



    // keyGenerator="myKeyGenerator"
    // @Cacheable(value="demoInfo") //缓存,这里没有指定key.
    //Cacheable(cacheNames="EvrAlarms",key="'EvrAlarm-'+#accountId")
    //@Cacheable(cacheNames="EvrAlarms")
    //@EnableElastiCache({@CacheClusterConfig(name = "fs-redis-cache")})
    @Override
    @Cacheable(value = "users", key = "'user_'+#id", unless="#result == null")
    //@Cacheable(value="mysql:findByid:role", keyGenerator="simpleKey")
    public demoinfo findById(int id) {

        demoinfo info1 = new demoinfo();
        info1.setId(1);
        Example<demoinfo> exam = Example.of(info1);
        //List<demoinfo> lists = demoInfoRepository.findAll(exam);

        System.err.println("DemoInfoServiceImpl.findById()=========从数据库中进行获取的....id=" + id);
//        return demoInfoRepository.findById(id).get();
        demoinfo info = null;
        try {
           // info = demoInfoRepository.findById(id).get();
        } catch (Exception e) {
            System.err.println("程序抛出异常 youqiang");
            e.printStackTrace();
        }

       // demoInfoRepository.findById(id);

        return info;
    }

    // @CacheEvict(value = "demoInfo")
    //@Override
    public void deleteFromCache(int id) {
        System.out.println("DemoInfoServiceImpl.delete().从缓存中删除.");
    }

//    public  demoinfo getdemoinfoById(int id){
//        return demoInfoRepository.getdemoinfoById(id);
//    }
//    public List<demoinfo> querydemoinfosByNameLike(String name){
//        return demoInfoRepository.querydemoinfosByNameLike(name);
//    }

}
