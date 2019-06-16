package com.kfit.dao;

import com.kfit.domain.Stu;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface StuMapper {
    @Insert({
        "insert into stu (name, sex, ",
        "age)",
        "values (#{name,jdbcType=VARCHAR}, #{sex,jdbcType=VARCHAR}, ",
        "#{age,jdbcType=VARCHAR})"
    })
    int insert(Stu record);

    int insertSelective(Stu record);
}