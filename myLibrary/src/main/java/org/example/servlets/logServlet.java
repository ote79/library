package org.example.servlets;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.ibatis.session.SqlSession;
import org.example.entitys.User;
import org.example.mapper.CheckUser;
import org.example.tools.MybatisTool;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;

@WebServlet("/login")
public class logServlet extends HttpServlet {

    TemplateEngine engine;

    @Override
    public void init() throws ServletException {
        engine = new TemplateEngine();
        ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
        engine.setTemplateResolver(templateResolver);

    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html;charset=UTF-8");
        resp.getWriter().write("(╬ﾟдﾟ)▄︻┻┳═一");
        resp.getWriter().write("不可饶恕");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html;charset=UTF-8");
        Map<String,String[]> map = req.getParameterMap();
        if (map.containsKey("username") && map.containsKey("password")){
            String username = req.getParameter("username");
            String password = req.getParameter("password");
            User user;
            try (SqlSession session = MybatisTool.getSession(true)){
                CheckUser mapper = session.getMapper(CheckUser.class);
                user = mapper.selectUserByName(username);
                if (Objects.isNull(user) || password.isEmpty() || username.isEmpty()){
                    resp.getWriter().write("<html><head><title>登录提示</title><style>" +
                            ":root { --primary: #607D8B; --secondary: #90A4AE; --accent: #4FC3F7; --light: #ECEFF1; --dark: #263238; --error: #E57373; --neutral: #CFD8DC; }" +
                            "body { font-family: 'Inter', sans-serif; background-color: var(--light); margin: 0; padding: 0; color: var(--dark); display: flex; justify-content: center; align-items: center; min-height: 100vh; }" +
                            ".card { background: white; border-radius: 16px; box-shadow: 0 10px 30px rgba(0, 0, 0, 0.05); padding: 40px; max-width: 420px; width: 100%; margin: 20px; animation: fadeInUp 0.6s cubic-bezier(0.34, 1.56, 0.64, 1); }" +
                            ".header { text-align: center; margin-bottom: 40px; }" +
                            ".logo { font-size: 28px; font-weight: 600; color: var(--primary); margin-bottom: 5px; }" +
                            ".subtitle { font-size: 14px; color: var(--secondary); }" +
                            ".icon-container { width: 80px; height: 80px; background-color: rgba(229, 115, 115, 0.1); border-radius: 50%; display: flex; justify-content: center; align-items: center; margin: 0 auto 30px; }" +
                            ".icon-container i { font-size: 32px; color: var(--error); }" +
                            "h1 { font-size: 24px; font-weight: 500; color: var(--dark); text-align: center; margin-bottom: 15px; }" +
                            "p { font-size: 16px; color: var(--primary); text-align: center; margin-bottom: 35px; line-height: 1.5; }" +
                            ".btn { display: block; width: 100%; padding: 14px; background-color: var(--primary); color: white; text-align: center; text-decoration: none; border-radius: 8px; transition: all 0.3s ease; font-weight: 500; border: none; cursor: pointer; letter-spacing: 0.5px; }" +
                            ".btn:hover { background-color: var(--accent); transform: translateY(-2px); box-shadow: 0 4px 12px rgba(79, 195, 247, 0.2); }" +
                            ".btn:active { transform: translateY(0); box-shadow: 0 2px 6px rgba(79, 195, 247, 0.2); }" +
                            "@keyframes fadeInUp { from { opacity: 0; transform: translateY(20px); } to { opacity: 1; transform: translateY(0); } }" +
                            "</style></head><body>" +
                            "<div class='card'>" +
                            "<div class='header'>" +
                            "<div class='logo'>BookLibrary</div>" +
                            "<div class='subtitle'>优雅阅读，智慧生活</div>" +
                            "</div>" +
                            "<div class='icon-container'><i class='fa fa-exclamation-circle'></i></div>" +
                            "<h1>请检查你的昵称</h1>" +
                            "<p>可能的原因：昵称不存在或密码为空</p>" +
                            "<button class='btn' onclick=\"window.location.href='index'\">返回登录</button>" +
                            "</div></body></html>");
                }else if (user.getPassword().equals(password)){
                    req.getSession().setAttribute("user",user);
                    resp.sendRedirect("home");
                }else {
                    resp.getWriter().write("<html><head><title>登录提示</title><style>" +
                            ":root { --primary: #607D8B; --secondary: #90A4AE; --accent: #4FC3F7; --light: #ECEFF1; --dark: #263238; --error: #E57373; --neutral: #CFD8DC; }" +
                            "body { font-family: 'Inter', sans-serif; background-color: var(--light); margin: 0; padding: 0; color: var(--dark); display: flex; justify-content: center; align-items: center; min-height: 100vh; }" +
                            ".card { background: white; border-radius: 16px; box-shadow: 0 10px 30px rgba(0, 0, 0, 0.05); padding: 40px; max-width: 420px; width: 100%; margin: 20px; animation: fadeInUp 0.6s cubic-bezier(0.34, 1.56, 0.64, 1); }" +
                            ".header { text-align: center; margin-bottom: 40px; }" +
                            ".logo { font-size: 28px; font-weight: 600; color: var(--primary); margin-bottom: 5px; }" +
                            ".subtitle { font-size: 14px; color: var(--secondary); }" +
                            ".icon-container { width: 80px; height: 80px; background-color: rgba(229, 115, 115, 0.1); border-radius: 50%; display: flex; justify-content: center; align-items: center; margin: 0 auto 30px; }" +
                            ".icon-container i { font-size: 32px; color: var(--error); }" +
                            "h1 { font-size: 24px; font-weight: 500; color: var(--dark); text-align: center; margin-bottom: 15px; }" +
                            "p { font-size: 16px; color: var(--primary); text-align: center; margin-bottom: 35px; line-height: 1.5; }" +
                            ".btn { display: block; width: 100%; padding: 14px; background-color: var(--primary); color: white; text-align: center; text-decoration: none; border-radius: 8px; transition: all 0.3s ease; font-weight: 500; border: none; cursor: pointer; letter-spacing: 0.5px; }" +
                            ".btn:hover { background-color: var(--accent); transform: translateY(-2px); box-shadow: 0 4px 12px rgba(79, 195, 247, 0.2); }" +
                            ".btn:active { transform: translateY(0); box-shadow: 0 2px 6px rgba(79, 195, 247, 0.2); }" +
                            "@keyframes fadeInUp { from { opacity: 0; transform: translateY(20px); } to { opacity: 1; transform: translateY(0); } }" +
                            "</style></head><body>" +
                            "<div class='card'>" +
                            "<div class='header'>" +
                            "<div class='logo'>BookLibrary</div>" +
                            "<div class='subtitle'>优雅阅读，智慧生活</div>" +
                            "</div>" +
                            "<div class='icon-container'><i class='fa fa-lock'></i></div>" +
                            "<h1>账号或者密码错误</h1>" +
                            "<p>请检查你的账号和密码是否正确</p>" +
                            "<button class='btn' onclick=\"window.location.href='index'\">返回登录</button>" +
                            "</div></body></html>");
                }
            }
        }else {
            resp.getWriter().write("<html><head><title>登录提示</title><style>" +
                    ":root { --primary: #607D8B; --secondary: #90A4AE; --accent: #4FC3F7; --light: #ECEFF1; --dark: #263238; --error: #E57373; --neutral: #CFD8DC; }" +
                    "body { font-family: 'Inter', sans-serif; background-color: var(--light); margin: 0; padding: 0; color: var(--dark); display: flex; justify-content: center; align-items: center; min-height: 100vh; }" +
                    ".card { background: white; border-radius: 16px; box-shadow: 0 10px 30px rgba(0, 0, 0, 0.05); padding: 40px; max-width: 420px; width: 100%; margin: 20px; animation: fadeInUp 0.6s cubic-bezier(0.34, 1.56, 0.64, 1); }" +
                    ".header { text-align: center; margin-bottom: 40px; }" +
                    ".logo { font-size: 28px; font-weight: 600; color: var(--primary); margin-bottom: 5px; }" +
                    ".subtitle { font-size: 14px; color: var(--secondary); }" +
                    ".icon-container { width: 80px; height: 80px; background-color: rgba(229, 115, 115, 0.1); border-radius: 50%; display: flex; justify-content: center; align-items: center; margin: 0 auto 30px; }" +
                    ".icon-container i { font-size: 32px; color: var(--error); }" +
                    "h1 { font-size: 24px; font-weight: 500; color: var(--dark); text-align: center; margin-bottom: 15px; }" +
                    "p { font-size: 16px; color: var(--primary); text-align: center; margin-bottom: 35px; line-height: 1.5; }" +
                    ".btn { display: block; width: 100%; padding: 14px; background-color: var(--primary); color: white; text-align: center; text-decoration: none; border-radius: 8px; transition: all 0.3s ease; font-weight: 500; border: none; cursor: pointer; letter-spacing: 0.5px; }" +
                    ".btn:hover { background-color: var(--accent); transform: translateY(-2px); box-shadow: 0 4px 12px rgba(79, 195, 247, 0.2); }" +
                    ".btn:active { transform: translateY(0); box-shadow: 0 2px 6px rgba(79, 195, 247, 0.2); }" +
                    "@keyframes fadeInUp { from { opacity: 0; transform: translateY(20px); } to { opacity: 1; transform: translateY(0); } }" +
                    "</style></head><body>" +
                    "<div class='card'>" +
                    "<div class='header'>" +
                    "<div class='logo'>BookLibrary</div>" +
                    "<div class='subtitle'>优雅阅读，智慧生活</div>" +
                    "</div>" +
                    "<div class='icon-container'><i class='fa fa-exclamation-triangle'></i></div>" +
                    "<h1>你提交的表单有点小问题</h1>" +
                    "<p>请确保填写了用户名和密码</p>" +
                    "<button class='btn' onclick=\"window.location.href='index'\">返回登录</button>" +
                    "</div></body></html>");
        }
    }
}
