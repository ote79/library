# library


#2025/6/13更新
适配了Web，现在可以直接在相应的服务器上运行，maven架构

![image](https://github.com/user-attachments/assets/4d056306-624c-4635-a853-caf9e17e6bf9)

![image](https://github.com/user-attachments/assets/3e1652b5-3b8f-4880-a685-7084780c0cd7)

![image](https://github.com/user-attachments/assets/9dc1f9ea-1532-4b71-b1f5-dd724de809de)

![image](https://github.com/user-attachments/assets/dcc33ed3-7466-4433-a77f-15c7e533bf56)

功能上相对于控制台版本有所阉割，本人尽力了

我靠终于他妈的写完了

#2025/5/19更新
加入了日志系统

目前实现的功能有：

登录系统（根据uid识别并赋予用户权限（目前分为管理员和普通用户），支持账户（uid）和昵称（name）登录，切换账户）

业务系统（借书，还书，查询图书信息，查询所有图书信息（管理员），搜索图书（Bid/BookName），修改密码（管理员），删除/添加/修改图书信息（管理员），查询自身借阅列表，查询所有用户详细信息（管理员），添加/删除用户（管理员），查看所有借阅详细信息（管理员））

于com.main.Main.java启动项目

本项目的项目管理工具为Maven，使用的依赖为lombok，mybatis，junit，mysql。

数据库结构如下
![1747553965317](https://github.com/user-attachments/assets/35889bc1-28ca-458e-9603-ee5a8d084f95)

联系邮箱：ote79ote@163.com

未适配前端，适配了会更新
