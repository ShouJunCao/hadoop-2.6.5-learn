/**
 * @FileName: HaHdfsUtils.java
 * @Package: hadoop.hdfs
 * @author caoshoujun
 * @created 2017/6/28 15:59
 * <p/>
 * Copyright 2016 ziroom
 */
package hadoop.hdfs;

import org.junit.Test;

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
public class HaHdfsUtilsTest {

    @Test
    public void createHdfsPath(){
        boolean result = HaHdfsUtils.createHdfsPath("/haha");
        System.out.println(result);
    }

    @Test
    public void exitHdfsPath(){
        boolean result = HaHdfsUtils.exitHdfsPath("/haha");
        System.out.println(result);
    }

    @Test
    public void deleteHdfsPath(){
        boolean result = HaHdfsUtils.deleteHdfsPath("/haha");
        System.out.println(result);
    }


    @Test
    public void createHdfsFile(){
        boolean result = HaHdfsUtils.createHdfsFile("/hello/hello.txt");
        System.out.println(result);
    }


    @Test
    public void copyFromLocalFile(){
        boolean result = HaHdfsUtils.copyFromLocalFile("E:\\hadoop-learn-java.jar","/hello/hadoop-learn-java.jar");
        System.out.println(result);
    }


    @Test
    public void copyToLocalFile(){
        boolean result = HaHdfsUtils.copyToLocalFile("/hello/hello.txt","E:\\hello.txt");
        System.out.println(result);
    }

    @Test
    public void getHdfsFile(){
        boolean result = HaHdfsUtils.getHdfsFile("/hello/hadoop-learn-java.jar");
        System.out.println(result);
    }


}
