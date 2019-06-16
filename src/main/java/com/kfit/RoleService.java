package com.kfit;

import com.kfit.dao.StuMapper;
import com.kfit.domain.Stu;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * @author ：youq
 * @date ：Created in 2019/2/16 1:23
 * @modified By：
 */
@Service
public class RoleService {
    @Resource
    private StuMapper stuMapper;
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    void test2(Stu stu){
        stuMapper.insert(stu);
       int z = 1/0;
    }
}
