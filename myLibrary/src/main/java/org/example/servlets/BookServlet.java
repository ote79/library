package org.example.servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


import com.fasterxml.jackson.databind.ObjectMapper;


import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.ibatis.session.SqlSession;
import org.example.entitys.Book;
import org.example.entitys.Relationship;
import org.example.entitys.User;
import org.example.mapper.BookManage;
import org.example.tools.MybatisTool;

@WebServlet("/book")
public class BookServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    BookManage bookManage;

    // 初始化方法
    @Override
    public void init() throws ServletException {
        super.init();

    }

    @Override
    protected void doPost(HttpServletRequest 请求, HttpServletResponse response)
            throws ServletException, IOException {
        // 设置响应内容类型
        response.setContentType("application/json;charset=UTF-8");

        // 获取请求参数
        String action = request.getParameter("action");

        // 用于JSON序列化的对象
        ObjectMapper mapper = new ObjectMapper();


        try (SqlSession session = MybatisTool.getSession(true)) {
            bookManage = session.getMapper(BookManage.class);
            try {
                if ("list".equals(action)) {
                    List<Relationship> books = bookManage.selectRelationshipAll();
                    System.out.println(books);
                    for (Relationship relationship : books) {
                        if (relationship.getName() == null) {
                            relationship.setAvailable(true);
                            relationship.setUid(0);
                        }
                    }
                    // 查询所有图书

                    System.out.println(1);

                    // 将结果转换为JSON并返回
                    String jsonResponse = mapper.writeValueAsString(books);
                    System.out.println(jsonResponse);
                    response.getWriter().write(jsonResponse);
                } else if ("get".equals(action)) {
                    // 按ID查询图书
                    System.out.println("get");
                    String idStr = request.getParameter("id");
                    if (idStr != null && !idStr.isEmpty()) {
                        int bookId = Integer.parseInt(idStr);
                        Relationship book = bookManage.selectRelationshipByBid(bookId);

                        if (book != null) {
                            // 返回单本图书信息
                            book.setAvailable(book.getName() == null);

                            String jsonResponse = mapper.writeValueAsString(book);
                            System.out.println(jsonResponse);
                            response.getWriter().write(jsonResponse);
                        } else {
                            // 图书不存在
                            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                            response.getWriter().write("{\"message\":\"Book not found\"}");
                        }
                    } else {
                        // 参数错误
                        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                        response.getWriter().write("{\"message\":\"Invalid book ID\"}");
                    }
                } else if ("add".equals(action)) {
                    // 添加图书

                    String title = request.getParameter("title");
                    String author = request.getParameter("author");


                    if (title != null && !title.isEmpty() &&
                            author != null && !author.isEmpty()) {


                        // 调用服务添加图书
                        int addedBook = bookManage.addBook(title, author);

                        if (addedBook == 1) {
                            // 添加成功
                            String jsonResponse = mapper.writeValueAsString(addedBook);
                            response.getWriter().write(jsonResponse);
                        } else {
                            // 添加失败
                            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                            response.getWriter().write("{\"message\":\"Failed to add book\"}");
                        }
                    } else {
                        // 参数错误
                        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                        response.getWriter().write("{\"message\":\"Missing required parameters\"}");
                    }
                } else if ("delete".equals(action)) {
                    // 删除图书
                    String idStr = request.getParameter("id");
                    if (idStr != null && !idStr.isEmpty()) {
                        int bookId = Integer.parseInt(idStr);

                        // 检查图书是否存在
                        Book book = bookManage.selectBookByBid(bookId);
                        Relationship relationship = bookManage.selectRelationshipByBid(bookId);
                        if (book == null) {
                            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                            response.getWriter().write("{\"message\":\"Book not found\"}");
                            return;
                        }

                        // 检查图书是否可借（未被借出）
                        if (relationship.getName() != null) {
                            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                            response.getWriter().write("{\"message\":\"Cannot delete borrowed book\"}");
                            return;
                        }

                        // 调用服务删除图书
                        int success = bookManage.deleteBookByBid(bookId);

                        if (success == 1) {
                            // 删除成功
                            response.getWriter().write("{\"message\":\"Book deleted successfully\"}");
                        } else {
                            // 删除失败
                            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                            response.getWriter().write("{\"message\":\"Failed to delete book\"}");
                        }
                    } else {
                        // 参数错误
                        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                        response.getWriter().write("{\"message\":\"Invalid book ID\"}");
                    }
                } else if ("update".equals(action)) {
                    // 更新图书
                    String idStr = request.getParameter("id");
                    String title = request.getParameter("title");
                    String author = request.getParameter("author");


                    if (idStr != null && !idStr.isEmpty() &&
                            title != null && !title.isEmpty() &&
                            author != null && !author.isEmpty()) {

                        int bookId = Integer.parseInt(idStr);

                        // 检查图书是否存在
                        Book book = bookManage.selectBookByBid(bookId);
                        if (book == null) {
                            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                            response.getWriter().write("{\"message\":\"Book not found\"}");
                            return;
                        }


                        // 调用服务更新图书
                        int updatedAuthor = bookManage.updateBookAuthorByName(bookId, author);
                        int updateBookName = bookManage.updateBookNameByName(bookId, title);

                        if (updateBookName == 1 || updatedAuthor == 1) {
                            // 更新成功
                            Book updatedBook = bookManage.selectBookByBid(bookId);
                            String jsonResponse = mapper.writeValueAsString(updatedBook);
                            response.getWriter().write(jsonResponse);
                        } else {
                            // 更新失败
                            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                            response.getWriter().write("{\"message\":\"Failed to update book\"}");
                        }
                    } else {
                        // 参数错误
                        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                        response.getWriter().write("{\"message\":\"Missing required parameters\"}");
                    }
                } else if ("search".equals(action)) {
                    // 搜索图书
                    String keyword = request.getParameter("keyword");
                    if (keyword != null && !keyword.isEmpty()) {
                        keyword = "%"+keyword+"%";
                        System.out.println(keyword);
                        List<Relationship> results = bookManage.searchBooksByBookName(keyword);
                        for (Relationship relationship : results){
                            relationship.setAvailable(relationship.getName() == null);
                        }
                        // 将结果转换为JSON并返回
                        String jsonResponse = mapper.writeValueAsString(results);
                        response.getWriter().write(jsonResponse);
                    } else {
                        // 参数错误
                        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                        response.getWriter().write("{\"message\":\"Keyword cannot be empty\"}");
                    }
                } else if ("borrow".equals(action)) {
                    // 借阅图书
                    System.out.println("borrow");
                    String bookIdStr = request.getParameter("bookId");
                    String userIdStr = request.getParameter("userId");

                    if (bookIdStr != null && !bookIdStr.isEmpty() &&
                            userIdStr != null && !userIdStr.isEmpty()) {

                        int bookId = Integer.parseInt(bookIdStr);
                        int userId = Integer.parseInt(userIdStr);

                        // 调用服务借阅图书
                        int success = bookManage.addRelationship(bookId, userId);

                        if (success == 1) {
                            // 借阅成功
                            response.getWriter().write("{\"message\":\"Book borrowed successfully\"}");
                        } else {
                            // 借阅失败
                            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                            response.getWriter().write("{\"message\":\"Failed to borrow book\"}");
                        }
                    } else {
                        // 参数错误
                        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                        response.getWriter().write("{\"message\":\"Missing required parameters\"}");
                    }
                } else if ("return".equals(action)) {
                    // 归还图书
                    String bookIdStr = request.getParameter("bookId");
                    String userIdStr = request.getParameter("userId");

                    if (bookIdStr != null && !bookIdStr.isEmpty() &&
                            userIdStr != null && !userIdStr.isEmpty()) {

                        int bookId = Integer.parseInt(bookIdStr);
                        int userId = Integer.parseInt(userIdStr);

                        // 调用服务归还图书
                        int success = bookManage.deleteRelationshipByBid(bookId);

                        if (success == 1) {
                            // 归还成功
                            response.getWriter().write("{\"message\":\"Book returned successfully\"}");
                        } else {
                            // 归还失败
                            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                            response.getWriter().write("{\"message\":\"Failed to return book\"}");
                        }
                    } else {
                        // 参数错误
                        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                        response.getWriter().write("{\"message\":\"Missing required parameters\"}");
                    }
                } else if ("userBorrowedBooks".equals(action)) {
                    // 获取用户借阅的图书
                    String userIdStr = request.getParameter("userId");
                    if (userIdStr != null && !userIdStr.isEmpty()) {
                        int userId = Integer.parseInt(userIdStr);
                        List<Relationship> borrowedBooks = bookManage.selectRelationshipByUid(userId);
                        System.out.println(borrowedBooks);

                        // 将结果转换为JSON并返回
                        String jsonResponse = mapper.writeValueAsString(borrowedBooks);
                        System.out.println(jsonResponse);
                        response.getWriter().write(jsonResponse);
                    } else {
                        // 参数错误
                        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                        response.getWriter().write("{\"message\":\"User ID cannot be empty\"}");
                    }
                }else if ("canBorrow".equals(action)){
                    int uid = Integer.parseInt(request.getParameter("id"));

                    List<Relationship> books = bookManage.selectCanBorrowRelationshipByUid(uid);

                    for (Relationship relationship : books) {
                        if (relationship.getName() == null) {
                            relationship.setAvailable(true);
                            relationship.setUid(0);
                        }
                    }
                    // 将结果转换为JSON并返回
                    String jsonResponse = mapper.writeValueAsString(books);
                    System.out.println(jsonResponse);
                    response.getWriter().write(jsonResponse);
                }else {
                    // 不支持的操作
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    response.getWriter().write("{\"message\":\"Unsupported action\"}");
                }
            } catch (Exception e) {
                // 处理异常
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.getWriter().write("{\"message\":\"" + e.getMessage() + "\"}");
                e.printStackTrace();
            }
        }
    }

}
