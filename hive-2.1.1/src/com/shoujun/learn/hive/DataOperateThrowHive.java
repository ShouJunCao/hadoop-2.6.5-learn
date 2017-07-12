/**
 * @FileName: DataOperateThrowHive.java
 * @Package: com.shoujun.learn.hive
 * @author caoshoujun
 * @created 2017/7/12 10:33
 * <p/>
 * Copyright 2016 ziroom
 */
package com.shoujun.learn.hive;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * <p></p>
 *
 * <PRE>
 * <BR>	修改记录
 * <BR>-----------------------------------------------
 * <BR>	修改日期			修改人			修改内容
 * </PRE>
 *
 * @author caoshoujun
 * @version 1.0
 * @since 1.0
 */
public class DataOperateThrowHive {
    private static final String driverName = "org.apache.hive.jdbc.HiveDriver";
    private static final String url = "jdbc:hive2://node1:10000/hive_db";
    private static final String username = "root";
    private static final String password = "";
    public static void main(String[] args)throws Exception {
        Class.forName(driverName);
        Connection conn = DriverManager.getConnection(url, username, password);
        Statement statement = conn.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT COUNT(1) FROM userinfo");
        if(resultSet.next()){
            System.out.println(resultSet.getInt(1));
        }
        resultSet.close();
        statement.close();
        conn.close();
    }
}
