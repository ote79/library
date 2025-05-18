package com.main.tool;

import com.main.entity.User;
import com.main.mapper.BookManage;
import org.apache.ibatis.session.SqlSession;

import java.util.Scanner;

public class Log {
    public static void log(){
        try (SqlSession session = MybatisTool.getSession(true)) {
            BookManage mapper = session.getMapper(BookManage.class);
            Scanner input = new Scanner(System.in);
            System.out.print("(UID/昵称)(1/0)  选择登录方式：");
            int action = input.nextInt();
            if (action == 1){
                System.out.print("输入UID：");
                int uid = input.nextInt();
                input.nextLine();
                System.out.print("输入密码：");
                String password = input.nextLine();
                if (AccountChaker.check(uid,password)){
                    if (uid<5)
                        Administrator.chat(new User().setName(mapper.selectUserByUid(uid).getName()).setUid(uid).setPassword(password));
                    else Common.chat(new User().setName(mapper.selectUserByUid(uid).getName()).setUid(uid).setPassword(password));
                }
                else {
                    System.out.println("密码或者UID错误！！！");
                    Log.log();
                }
            }
            else {
                input.nextLine();
                System.out.print("输入昵称：");
                String name = input.nextLine();
                System.out.print("输入密码：");
                String password = input.nextLine();
                if (AccountChaker.check(name,password)){
                    int uid = mapper.selectUserByName("'"+name+"'").getUid();
                    User user = new User().setName(name).setUid(uid).setPassword(password);
                    if (uid<5)
                        Administrator.chat(user);
                    else Common.chat(user);
                }
                else {
                    System.out.println("密码或者昵称错误！！！");
                    Log.log();
                }
            }
        }
    }
}
