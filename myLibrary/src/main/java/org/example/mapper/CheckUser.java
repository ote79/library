package org.example.mapper;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.example.entitys.User;

public interface CheckUser {

    @Select("select * from book_manage.user where name = #{name}")
    User selectUserByName(@Param("name") String name);

}
