package com.kfit.dao;

import com.kfit.domain.PushDO;
import org.apache.ibatis.annotations.Insert;

public interface PushDOMapper {
    @Insert({
        "insert into demoinfo (id, name, ",
        "pwd)",
        "values (#{id,jdbcType=INTEGER}, #{name,jdbcType=VARCHAR}, ",
        "#{pwd,jdbcType=VARCHAR})"
    })
    int insert(PushDO record);

    int insertSelective(PushDO record);
}