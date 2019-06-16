package com.kfit.dao;

import com.kfit.domain.Role;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface RoleMapper {
    @Insert({
        "insert into demoinfo (id, name, ",
        "pwd)",
        "values (#{id,jdbcType=INTEGER}, #{name,jdbcType=VARCHAR}, ",
        "#{pwd,jdbcType=VARCHAR})"
    })
    int insert(Role record);

    int insertSelective(Role record);
}