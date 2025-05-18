package com.main.tool;

import com.main.entity.User;
import com.main.mapper.BookManage;
import org.apache.ibatis.session.SqlSession;

import java.util.Objects;

public class AccountChaker {


    public static boolean check(int uid, String password) {
        try (SqlSession session = MybatisTool.getSession(true)) {
            BookManage mapper = session.getMapper(BookManage.class);
            if (Objects.isNull(mapper.selectUserByUid(uid)))
                return false;
            return mapper.selectUserByUid(uid).getPassword().equals(password);
        }
    }

    public static boolean check(String name, String password) {
        name = "'" + name + "'";
        try (SqlSession session = MybatisTool.getSession(true)) {
            BookManage mapper = session.getMapper(BookManage.class);
            User user = mapper.selectUserByName(name);
            if (Objects.isNull(user))
                return false;
            return mapper.selectUserByUid(user.getUid()).getPassword().equals(password);
        }
    }
}
