import org.apache.ibatis.session.SqlSession;
import org.example.mapper.BookManage;
import org.example.tools.MybatisTool;

public class Test01 {
    public static void main(String[] args) {
        try (SqlSession session = MybatisTool.getSession(true)){
            BookManage manage = session.getMapper(BookManage.class);
            System.out.println(manage.selectRelationshipAll());
        }
    }
}
