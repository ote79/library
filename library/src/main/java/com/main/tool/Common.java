package com.main.tool;

import com.main.entity.Relationship;
import com.main.entity.User;
import com.main.mapper.BookManage;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;
import java.util.logging.LogManager;

@lombok.extern.java.Log
public class Common {

    final static String string = "1-查询图书列表\n2-借书     3-还书 \n4-查询自己的借书列表 \n5-修改自己的密码\n13-更改账户\n14-退出系统\n选择你的操作：";
    static User user;
    static {
        try {
            LogManager manager = LogManager.getLogManager();
            manager.readConfiguration(Resources.getResourceAsStream("logging.properties"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public static void chat(User inputUser) {
        int count = 0;
        user = inputUser;
        Scanner input = new Scanner(System.in);
        System.out.println("欢迎登录！\n用户id：" + user.getUid() + "\n用户昵称：" + user.getName() + "\n登录时间：" + new Date());
        log.info("用户："+user+"登录成功");
        while (true) {
            if (count % 2 == 0)
                System.out.print(string);
            else System.out.print("选择你的操作：");
            int action = input.nextInt();
            switch (action) {
                case 1:
                    selectBook();
                    break;
                case 2:
                    borrowBook(user.getUid());
                    break;
                case 3:
                    returnBook(user.getUid());
                    break;
                case 4:
                    selectSelfRelationship(user.getUid());
                    break;
                case 5:
                    changePassword(user.getUid());
                    break;
                case 13:
                    log.info(user+"登出");
                    Log.log();
                    break;
                case 14:
                    log.info(user+"登出");
                    log.info("系统关闭");
                    System.exit(0);
                    break;
                default:
                    System.err.println("无效操作！！！");
            }
            count++;
        }
    }

    private static void change(Relationship relationship) {
        if (!Objects.nonNull(relationship.getName()))
            relationship.setName("未借出");
        else relationship.setName("已借出");
        System.out.println("bid:" + relationship.getBid() + "    bookName:" + relationship.getBookName() + "   状态:" + relationship.getName());
    }

    public static void borrowBook(int uid) {
        try (SqlSession session = MybatisTool.getSession(true)) {
            BookManage mapper = session.getMapper(BookManage.class);
            List<Relationship> relationshipList = mapper.selectRelationship();
            relationshipList.removeIf(relationship -> Objects.nonNull(relationship.getName()));
            if (relationshipList.isEmpty()) {
                System.err.println("所有书籍都被借走了！！！");
            } else {
                System.out.println("以下是图书馆存书");
                for (Relationship relationship : relationshipList) {
                    System.out.println(mapper.selectBookByBid(relationship.getBid()));
                }
                Scanner input = new Scanner(System.in);
                System.out.print("输入你要借的书籍Bid：");
                int bid = input.nextInt();
                for (Relationship relationship : relationshipList) {
                    if (relationship.getBid() == bid) {
                        borrowBook(bid, uid);
                        System.out.println("借书成功！！！");
                        log.info(mapper.selectUserByUid(uid)+"借走了"+mapper.selectBookByBid(bid));
                        break;
                    } else {
                        System.err.println("请检查输入的Bid是否正确！！！");
                    }
                }

            }
        }
    }

    public static void borrowBook(int bid, int uid) {
        try (SqlSession session = MybatisTool.getSession(true)) {
            BookManage mapper = session.getMapper(BookManage.class);
            mapper.addRelationship(bid, uid);
        }
    }

    protected static void returnBook(int uid) {
        try (SqlSession session = MybatisTool.getSession(true)) {
            BookManage mapper = session.getMapper(BookManage.class);
            List<Relationship> relationshipList = mapper.selectRelationshipByUid(uid);
            if (relationshipList.isEmpty())
                System.err.println("未查询到你借的书");
            else {
                selectSelfRelationship(uid);
                Scanner input = new Scanner(System.in);
                System.out.print("输入你要还的书籍Bid：");
                int bid = input.nextInt();
                boolean inIt = false;
                for (Relationship relationship : mapper.selectRelationshipByUid(uid)) {
                    if (inIt = (relationship.getBid() == bid)) {
                        System.out.println(mapper.deleteRelationshipByBid(bid));
                        System.out.println("还书成功！！！");
                        log.info(mapper.selectUserByUid(uid)+"还回了"+mapper.selectBookByBid(bid));
                        break;
                    }
                }
                if (!inIt) {
                    System.err.println("请检查输入的Bid是否正确！！！");
                }

            }
        }
    }

    protected static void selectSelfRelationship(int uid) {
        try (SqlSession session = MybatisTool.getSession(true)) {
            BookManage mapper = session.getMapper(BookManage.class);
            List<Relationship> relationshipList = mapper.selectRelationshipByUid(uid);
            if (relationshipList.isEmpty())
                System.err.println("未查询到你借的书");
            else {
                System.out.println("以下是你的借书列表：");
                for (Relationship relationship : relationshipList) {
                    System.out.println("bid:" + relationship.getBid() + "       bookName:" + relationship.getBookName());
                }
            }
            log.info(mapper.selectUserByUid(uid)+"查询了自己的借书列表");
        }
    }

    protected static void changePassword(int uid) {
        Scanner input = new Scanner(System.in);
        System.out.print("请输入新密码：");
        String s1 = input.nextLine();
        System.out.print("请再次输入密码：");
        String s2 = input.nextLine();
        if (s1.equals(s2) && !s1.isEmpty()) {
            try (SqlSession session = MybatisTool.getSession(true)) {
                BookManage mapper = session.getMapper(BookManage.class);
                mapper.updatePassword(uid, s1);
                System.out.println("密码更改成功！！！");
                log.info(user.getName()+"更改了自己的密码，新密码为："+s1);
            }
        } else System.err.println("两次输入密码不一致！！！");
    }

    protected static void selectBook() {
        System.out.print("(bid/bookName):(1/2)\n输入查询依据:");
        Scanner input = new Scanner(System.in);
        try (SqlSession session = MybatisTool.getSession(true)) {
            BookManage mapper = session.getMapper(BookManage.class);
            int action = input.nextInt();
            if (action == 1) {
                System.out.print("输入要查询书籍的bid:");
                Relationship relationship = mapper.selectRelationshipByBid(input.nextInt());
                input.nextLine();
                change(relationship);
            } else {
                input.nextLine();
                System.out.print("输入要查询书籍的名称:");
                String name = input.nextLine();
                name = "'" + name + "'";
                Relationship relationship = mapper.selectRelationshipByBName(name);
                change(relationship);
            }
            log.info(user+"查询了书籍");
        } catch (NullPointerException e) {
            System.err.println("所选书籍不存在！！！");
        }
    }
}
