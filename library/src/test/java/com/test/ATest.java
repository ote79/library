package com.test;

import com.main.entity.Relationship;
import com.main.entity.User;
import com.main.mapper.BookManage;

import com.main.tool.AccountChaker;
import com.main.tool.Administrator;
import com.main.tool.Common;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.RepeatedTest;

import java.io.IOException;
import java.util.List;


public class ATest {
    private static SqlSessionFactory factory;

    @BeforeAll
    public static void before() throws IOException {
        factory = new SqlSessionFactoryBuilder().build(Resources.getResourceAsStream("mybatis-config.xml"));
    }


    @RepeatedTest(1)
    public void test01() {
        Administrator.selectAllBooks();
    }
}
