package com.example.demo;

import org.junit.Test;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
public class ConnectionTest {
    //方式一：
    @Test
    public void testConnection1() throws SQLException {
        //获取Driver实现类对象
        Driver driver =new com.mysql.jdbc.Driver() ;

        String url ="jdbc:mysql://localhost:3306/test";

        Properties info = new Properties();
        info.setProperty("user","root");
        info.setProperty("password","123456");
        Connection connect = driver.connect(url, info);

        System.out.println(connect);
    }
    //方式二：对方式一的迭代：在如下的程序中部出现第三方的api，使得程序具有更好的可移植性
    @Test
    public void testConnection2() throws Exception{
        //1获取Driver实现类对象，使用反射
        Class clazz = Class.forName("com.mysql.jdbc.Driver");
        Driver driver = (Driver) clazz.newInstance();

        //提供要连接的数据库
        String url ="jdbc:mysql://localhost:3306/test";

        //提供连接需要的用户名和密码
        Properties info =new Properties();
        info.setProperty("user","root");
        info.setProperty("password","123456");

        //获取连接
        Connection connect = driver.connect(url, info);
        System.out.println(connect);
    }

    //方式三：使用DriverManager替换Driver
    @Test
    public void testConnection3() throws Exception{
        //1.获取Driver的实现类对象
        Class clazz =Class.forName("com.mysql.jdbc.Driver");
        Driver driver = (Driver) clazz.newInstance();
        //2.提供另外三个连接的基本信息
        String url ="jdbc:mysql://localhost:3306/test";
        String user ="root";
        String password ="123456";
        //注册驱动
        DriverManager.registerDriver(driver);

        //获取连接
        Connection connect = DriverManager.getConnection(url, user, password);
        System.out.println(connect);
    }
    //方式四,可以只是加载驱动，不用显示的注册驱动了
    @Test
    public void testConnection4() throws Exception {
        //1.提供三个连接的基本信息
        String url ="jdbc:mysql://localhost:3306/test";
        String user ="root";
        String password ="123456";

        //2.加载Driver
        Class.forName("com.mysql.jdbc.Driver");
        //相对于方式三，可省略以下操作
//        Driver driver =(Driver) clazz.newInstance();
        //注册驱动
//        DriverManager.registerDriver(driver);
        //为什么可以省略上述操作？
        //因为在mysql中Driver实现类中，声明了上述的操作。
        Connection connection = DriverManager.getConnection(url, user, password);
        System.out.println(connection);
    }
    //方式五（fanal版），将数据库连接需要的四个基本信息声明在配置文件中，通过读取配置文件的方式，获取基本信息。
    @Test
    public void getConnection5() throws Exception {
        //1.读取配置文件中的四个基本信息
        InputStream is = ConnectionTest.class.getClassLoader().getResourceAsStream("jdbc.properties");
        Properties pros =new Properties();
        pros.load(is);

        String user =pros.getProperty("user");
        String password =pros.getProperty("password");
        String url =pros.getProperty("url");
        String driver =pros.getProperty("driver");

        //2.加载驱动
        Class.forName(driver);
        //3.获取连接
        Connection connection = DriverManager.getConnection(url,user, password);
        System.out.println(connection);

    }
}
