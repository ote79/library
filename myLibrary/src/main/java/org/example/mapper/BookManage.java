package org.example.mapper;

import org.apache.ibatis.annotations.*;
import org.example.entitys.Book;
import org.example.entitys.Relationship;
import org.example.entitys.User;

import java.util.List;

public interface BookManage {

    @Select("select * from book_manage.book order by bid")
    List<Book> selectAllBooks();

    @Select("select * from book_manage.user order by uid")
    List<User> selectAllUsers();

    @Select("select book.book_name,book.bid,user.name,relationship.uid,book.author " +
            "from book_manage.book " +
            "left join book_manage.relationship on relationship.bid = book.bid " +
            "left join book_manage.user on user.uid = relationship.uid")
    List<Relationship> selectRelationshipAll();

    @Select("select book.book_name,book.bid,user.name " +
            "from book_manage.book " +
            "left join book_manage.relationship on relationship.bid = book.bid " +
            "left join book_manage.user on user.uid = relationship.uid")
    List<Relationship> selectRelationship();


    @Select("select book.book_name,book.bid,user.name,relationship.uid,book.author " +
            "from book_manage.book " +
            "left join book_manage.relationship on relationship.bid = book.bid " +
            "left join book_manage.user on user.uid = relationship.uid " +
            "where relationship.uid = #{uid}")
    List<Relationship> selectRelationshipByUid(int uid);


    @Select("select book.book_name,book.bid,user.name,relationship.uid,book.author " +
            "from book_manage.book " +
            "left join book_manage.relationship on relationship.bid = book.bid " +
            "left join book_manage.user on user.uid = relationship.uid " +
            "where relationship.uid IS NULL")
    List<Relationship> selectCanBorrowRelationshipByUid(int uid);

    @Select("select book.book_name,book.bid,user.name,relationship.uid,book.author " +
            "from book_manage.book " +
            "left join book_manage.relationship on relationship.bid = book.bid " +
            "left join book_manage.user on user.uid = relationship.uid " +
            "where book.bid = #{bid}")
    Relationship selectRelationshipByBid(int bid);

    @Select("select book.book_name,book.bid,user.name,relationship.uid " +
            "from book_manage.book " +
            "left join book_manage.relationship on relationship.bid = book.bid " +
            "left join book_manage.user on user.uid = relationship.uid " +
            "where book.book_name = ${BName}")
    Relationship selectRelationshipByBName(@Param("BName") String Name);

    @Select("select book.book_name,book.bid,user.name,relationship.uid " +
            "from book_manage.book " +
            "left join book_manage.relationship on relationship.bid = book.bid " +
            "left join book_manage.user on user.uid = relationship.uid " +
            "where user.name = #{UName}")
    List<Relationship> selectRelationshipByUName(String UName);

    @Select("select book.book_name,book.bid,user.name,relationship.uid,book.author " +
            "from book_manage.book " +
            "left join book_manage.relationship on relationship.bid = book.bid " +
            "left join book_manage.user on user.uid = relationship.uid " +
            "where book.book_name like #{keyWord} or book.author like #{keyWord}")
    List<Relationship> searchBooksByBookName(String keyWord);

    @Select("select * from book_manage.user u where u.name = ${userName}")
    User selectUserByName(@Param("userName") String name);

    @Select("select * from book_manage.book b where b.book_name = ${bookName}")
    Book selectBookByName(@Param("bookName") String name);

    @Select("select * from book_manage.user u where u.uid = #{uid}")
    User selectUserByUid(@Param("uid") int uid);

    @Select("select * from book_manage.book b where b.bid = #{bid}")
    Book selectBookByBid(@Param("bid") int bid);

    @Insert("INSERT INTO book_manage.user (name, password)\n" +
            "VALUES (#{name}, DEFAULT)")
    int addUserOnlyByName(String name);

    @Insert("INSERT INTO book_manage.user (name, password)\n" +
            "VALUES (#{name}, #{password})")
    int addUserByNameAndPassword(@Param("name") String name, @Param("password") String password);

    @Insert("INSERT INTO book_manage.book (book_name, author) VALUES (#{bookName},#{author})")
    int addBook(@Param("bookName") String bookName, @Param("author") String author);

    @Insert("insert into book_manage.relationship (bid,uid) VALUES (#{bid},#{uid})")
    int addRelationship(@Param("bid") int bid, @Param("uid") int uid);

    @Delete("delete from book_manage.user where uid = #{uid}")
    int deleteUserByUid(int uid);

    @Delete("delete from book_manage.book where bid = #{bid}")
    int deleteBookByBid(int bid);

    @Delete("delete from book_manage.relationship where bid = #{bid}")
    int deleteRelationshipByBid(int bid);

    @Delete("delete from book_manage.relationship where uid = #{uid}")
    int deleteRelationshipByUid(int uid);

    @Update("UPDATE book_manage.book t SET t.book_name = #{newName} WHERE t.bid = #{bid};")
    int updateBookNameByName(@Param("bid") int bid, @Param("newName") String name);

    @Update("UPDATE book_manage.book t SET t.author = #{newAuthor} WHERE t.bid = #{bid};")
    int updateBookAuthorByName(@Param("bid") int bid, @Param("newAuthor") String author);

    @Update("UPDATE book_manage.user t SET t.password = #{password} WHERE t.uid = #{uid};")
    int updatePassword(@Param("uid") int uid, @Param("password") String password);

    @Update("UPDATE book_manage.user t SET t.name = #{name} WHERE t.uid = #{uid};")
    int updateUserName(@Param("uid") int uid, @Param("name") String name);


}
