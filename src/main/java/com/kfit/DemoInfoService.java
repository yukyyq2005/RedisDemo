package com.kfit;

import java.util.List;

public interface DemoInfoService {
    public void save(demoinfo User);

    public demoinfo findById(int id);

    public void deleteFromCache(int id);

    void test();

    //public  demoinfo getdemoinfoById(int id);
//    public List<demoinfo> querydemoinfosByNameLike(String name);
}
