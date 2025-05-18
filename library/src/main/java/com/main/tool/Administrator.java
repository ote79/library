package com.main.tool;

import com.main.entity.Book;
import com.main.entity.User;
import com.main.mapper.BookManage;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;

import java.io.IOException;
import java.util.Date;
import java.util.Objects;
import java.util.Scanner;
import java.util.logging.LogManager;

@lombok.extern.java.Log
public class Administrator extends Common {

    final static String string1 = "0-查询所有书籍状态\n1-查询图书列表\n2-借书     3-还书 \n4-查询自己的借书列表 \n5-修改自己的密码\n";

    final static String string2 = "!!!!!以下为管理员操作!!!!!\n6-查看所有借阅信息\n7-查看所有用户详细信息\n8-添加用户\n9-添加书籍\n10-删除用户\n11-删除书籍\n12-更改书籍信息\n13-更改账户\n14-退出系统\n\n选择你的操作：";

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
        user = inputUser;
        int count = 0;
        Scanner input = new Scanner(System.in);
        System.err.println("欢迎登录!!!管理员id：" + user.getUid() + "\n管理员昵称：" + user.getName() + "\n登录时间：" + new Date());
        log.info("管理员id：" + user.getUid() + "管理员昵称：" + user.getName());
        while (true) {
            if (count % 2 == 0)
                System.out.print(string1+string2);
            else System.out.print("选择你的操作：");
            int action = input.nextInt();
            switch (action) {
                case 0:
                    selectAllBooks();
                    break;
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
                case 6:
                    selectAllRelationship();
                    break;
                case 7:
                    selectAllUser();
                    break;
                case 8:
                    addUser();
                    break;
                case 9:
                    addBook();
                    break;
                case 10:
                    deleteUser();
                    break;
                case 11:
                    deleteBook();
                    break;
                case 12:
                    changeBook();
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

    public static void selectAllRelationship() {
        try (SqlSession session = MybatisTool.getSession(true)) {
            BookManage mapper = session.getMapper(BookManage.class);
            System.out.println("以下为所有借阅信息:");
            mapper.selectRelationshipAll().forEach(System.out::println);
            log.info(user.getName()+"查询所有借阅信息");
        }
    }

    public static void selectAllUser() {
        try (SqlSession session = MybatisTool.getSession(true)) {
            BookManage mapper = session.getMapper(BookManage.class);
            System.out.println("以下为所有用户信息:");
            mapper.selectAllUsers().forEach(System.out::println);
            log.info(user.getName()+"查询所有用户信息");
        }
    }

    public static void selectAllBooks() {
        try (SqlSession session = MybatisTool.getSession(true)) {
            BookManage mapper = session.getMapper(BookManage.class);
            System.out.println("以下为所有书籍信息:");
            System.out.println("-------------------------------");
            mapper.selectRelationshipAll().forEach(System.out::println);
            System.out.println("-------------------------------");
            log.info(user.getName()+"查询所有书籍信息");
        }
    }

    public static void addUser() {
        try (SqlSession session = MybatisTool.getSession(true)) {
            BookManage mapper = session.getMapper(BookManage.class);
            Scanner input = new Scanner(System.in);
            System.out.print("输入用户昵称：");
            String name = input.nextLine();
            System.out.print("是否设置密码？（1|2）（默认为123456）");
            int action = input.nextInt();
            input.nextLine();
            if (action == 1) {
                System.out.print("输入密码：");
                String password = input.nextLine();
                mapper.addUserByNameAndPassword(name, password);
            } else mapper.addUserOnlyByName(name);
            System.out.println("注册成功！！！");
            System.out.println("用户信息：" + mapper.selectUserByName("'" + name + "'"));
            System.out.println("请牢记你的uid！！！");
            log.info(user.getName()+"添加了用户:"+mapper.selectUserByName("'" + name + "'"));
        }
    }

    public static void addBook() {
        Scanner input = new Scanner(System.in);
        System.out.print("输入书籍名称：");
        String name = input.nextLine();
        System.out.print("输入作者名字：");
        String author = input.nextLine();
        try (SqlSession session = MybatisTool.getSession(true)) {
            BookManage mapper = session.getMapper(BookManage.class);
            System.out.println(mapper.addBook(name, author) == 1 ? "添加成功！！！" : "添加失败！！！");
            System.out.println("书籍信息：" + mapper.selectBookByName("'" + name + "'"));
            log.info(user.getName()+"添加了书籍："+mapper.selectBookByName("'" + name + "'"));
        }
    }

    public static void deleteUser() {
        try (SqlSession session = MybatisTool.getSession(true)) {
            BookManage mapper = session.getMapper(BookManage.class);
            Scanner input = new Scanner(System.in);
            System.out.print("输入要删除的用户ID：");
            int uid = input.nextInt();
            if (Objects.nonNull(mapper.selectUserByUid(uid))) {
                User newUser = mapper.selectUserByUid(uid);
                if (!Objects.nonNull(mapper.selectRelationshipByUid(uid))) {
                    System.out.println("当前用户存在以下书籍尚未归还：\n" + mapper.selectRelationshipByUid(uid));
                    System.out.print("是否强行删除？（删除后书籍将自动归还）(1|0)");
                    int action = input.nextInt();
                    if (action == 1) {
                        System.out.println("用户已删除！成功取回" + mapper.deleteRelationshipByUid(uid) + "本书籍！！！");
                        mapper.deleteUserByUid(uid);
                    }
                } else {
                    mapper.deleteUserByUid(uid);
                    System.out.println("用户已删除！！！");
                }
                log.info(user.getName()+"删除了用户："+newUser);
            } else System.out.println("所选择用户不存在！！！");
        }
    }

    public static void deleteBook() {
        try (SqlSession session = MybatisTool.getSession(true)) {
            BookManage mapper = session.getMapper(BookManage.class);
            Scanner input = new Scanner(System.in);
            System.out.print("输入要删除的书籍ID：");
            int bid = input.nextInt();
            if (Objects.nonNull(mapper.selectBookByBid(bid))) {
                Book book = mapper.selectBookByBid(bid);
                if (Objects.nonNull(mapper.selectRelationshipByBid(bid))) {
                    System.out.println("该书籍目前被用户" + mapper.selectRelationshipByBid(bid).getName() + "占用！");
                    System.out.print("是否强行删除？(1|0)");
                    int action = input.nextInt();
                    if (action == 1) {
                        mapper.deleteRelationshipByBid(bid);
                        System.out.println("书籍已删除！");
                        mapper.deleteBookByBid(bid);
                    }
                } else {
                    mapper.deleteBookByBid(bid);
                    System.out.println("书籍已删除！！！");
                }
                log.info(user.getName()+"删除了书籍："+book);
            } else System.out.println("所选择书籍不存在！！！");
        }
    }

    public static void changeBook() {
        try (SqlSession session = MybatisTool.getSession(true)) {
            BookManage mapper = session.getMapper(BookManage.class);
            Scanner input = new Scanner(System.in);
            System.out.print("输入要修改书籍的Bid：");
            int bid = input.nextInt();
            Book book = mapper.selectBookByBid(bid);
            System.out.print("（1->书籍名称 2->作者名字）输入要修改的内容：");
            int action = input.nextInt();
            input.nextLine();
            System.out.print("输入新的名称：");
            String name = input.nextLine();
            if (action == 1) {
                mapper.updateBookNameByName(bid, name);
            } else {
                mapper.updateBookAuthorByName(bid, name);
            }
            log.info(user.getName()+"将"+book+"修改为"+mapper.selectBookByBid(bid));
            System.out.println("修改成功！！！");
            System.out.println(mapper.selectBookByBid(bid));
        }catch (NullPointerException e){
            System.out.println("所选书籍不存在！！！");
        }
    }
}
