/**
 * @FileName: T_CallHdfsUtils.java
 * @Package: com.csj.learn.hadoop
 * @author caoshoujun
 * @created 2017/6/11 23:51
 * <p/>
 * Copyright 2016 ziroom
 */
package hadoop.hdfs;

import junit.framework.Assert;
import org.junit.Test;

import java.io.IOException;

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
public class T_CallHdfsUtils {
    @Test
    public void test() throws IOException {
        CallHdfsUtils.askWebUrl();
    }

    @Test
    public void t_askHdfsUrl() throws IOException {
        //new CallHdfsUtils("hdfs://192.168.126.128:9000/usr/hadoop/data/hello.txt").askHdfsUrl();
        new CallHdfsUtils("hdfs://192.168.126.128:9000/usr/hadoop/data/tt.txt").askHdfsUrl();
    }

    /**
     * 创建目录
     * @throws IOException
     */
    @Test
    public void t_createFilePath() throws IOException {
        boolean trueOrfalse = new CallHdfsUtils("hdfs://ns").createFilePath("/hello");
        Assert.assertTrue(trueOrfalse);
    }

    /**
     * 检测目录是否存在
     * @throws IOException
     */
    @Test
    public void t_existsPath_01() throws IOException {
        boolean reault = new CallHdfsUtils("hdfs://ns:9000").existsPath("/hello");
        Assert.assertTrue(reault);
    }

    /**
     * 删除文件夹及文件内容 删除单个文件 删除空文件夹
     * @throws IOException
     */
    @Test
    public void t_delePath_01() throws IOException {
        boolean result = new CallHdfsUtils("hdfs://192.168.126.128:9000").delePath("/usr/hadoop/data/hello.txt");
        Assert.assertTrue(result);
    }

    /**
     * 上传文件
     * @throws IOException
     */
    @Test
    public void t_upload1() throws IOException {
        new CallHdfsUtils("hdfs://192.168.126.128:9000").upload("D:/worldCount.txt", "/usr/hadoop/data/input/worldCount.txt");
    }

    /**
     * 上传文件
     * @throws IOException
     */
    @Test
    public void t_upload2() throws IOException {
        new CallHdfsUtils("hdfs://192.168.126.128:9000").upload2("D:/hello.txt", "/usr/hadoop/data/cc.txt");
    }

    /**
     * 获取文件夹中的文件或文件夹列表信息 获取文件大小信息
     * @throws IOException
     */
    @Test
    public void t_getDesc_01() throws IOException {
        new CallHdfsUtils("hdfs://192.168.126.128:9000").getDesc("/hello/ff.txt");
    }

    /**
     * 从HDFS上下载文件
     * @throws IOException
     */
    @Test
    public void t_download() throws IOException {
        new CallHdfsUtils("hdfs://192.168.126.128:9000").download("D:/ccc.data", "/usr/hadoop/data/hello.txt");
    }
}
