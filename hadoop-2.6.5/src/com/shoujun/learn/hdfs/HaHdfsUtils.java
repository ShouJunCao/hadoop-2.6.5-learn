/**
 * @FileName: HaHdfsUtils.java
 * @Package: hadoop.hdfs
 * @author caoshoujun
 * @created 2017/6/28 15:59
 * <p/>
 * Copyright 2016 ziroom
 */
package com.shoujun.learn.hdfs;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

import java.net.URI;

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
public class HaHdfsUtils {

    private static final String HADOOP_USER_NAME="root";

    public static Configuration cfg;

    public static URI uri;

    static {
        //System.setProperty("hadoop.home.dir", "E:\\Hadoop\\hadoop-2.6.5");
        cfg = new Configuration();
        cfg.set("fs.defaultFS", "hdfs://ns");
        cfg.set("dfs.nameservices", "ns");
        cfg.set("dfs.ha.namenodes.ns","nn1,nn2");
        cfg.set("dfs.namenode.rpc-address.ns.nn1","node1:9000");
        cfg.set("dfs.namenode.rpc-address.ns.nn2","node2:9000");
        cfg.set("dfs.client.failover.proxy.provider.ns","org.apache.hadoop.hdfs.server.namenode.ha.ConfiguredFailoverProxyProvider");

        try {
            uri = new URI( "hdfs://ns");
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    /**
     * 创建目录
     * @param path
     * @return
     */
    public static boolean createHdfsPath(String path){
        boolean result;
        try {
            FileSystem fileSystem = FileSystem.get(uri, cfg, HADOOP_USER_NAME);
            result = fileSystem.mkdirs(new Path(path));
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return result;
    }

    /**
     * 判断HDFS目录是否存在
     * @param path
     * @return
     */
    public static boolean exitHdfsPath(String path){
        boolean result;
        try {
            FileSystem fileSystem = FileSystem.get(uri, cfg, HADOOP_USER_NAME);
            result = fileSystem.exists(new Path(path));
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return result;
    }

    /**
     * 删除HDFS目录或文件
     * @param path
     * @return
     */
    public static boolean deleteHdfsPath(String path){
        boolean result;
        try {
            FileSystem fileSystem = FileSystem.get(uri, cfg, HADOOP_USER_NAME);
            //删除文件或者文件夹，是否进行递归删除
            result = fileSystem.delete(new Path(path),true);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return result;
    }


    /**
     * 创建文件
     * @param path
     * @return
     */
    public static boolean createHdfsFile(String path){
        boolean result;
        try {
            FileSystem fileSystem = FileSystem.get(uri, cfg, HADOOP_USER_NAME);
            result = fileSystem.createNewFile(new Path(path));
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return result;
    }

    /**
     * 本地文件上传到HFDS
     * @param localPath
     * @param remotePath
     * @return
     */
    public static boolean copyFromLocalFile(String localPath, String remotePath){
        try {
            FileSystem fileSystem = FileSystem.get(uri, cfg, HADOOP_USER_NAME);
            fileSystem.copyFromLocalFile(new Path(localPath), new Path(remotePath));
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 从HFDS下载文件到本地
     * @param localPath
     * @param remotePath
     * @return
     */
    public static boolean copyToLocalFile(String remotePath, String localPath){
        try {
            FileSystem fileSystem = FileSystem.get(uri, cfg, HADOOP_USER_NAME);
            fileSystem.copyToLocalFile(new Path(remotePath),new Path(localPath));
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }


    /**
     * 从HFDS读取文件内容
     * @param remotePath
     * @return
     */
    public static boolean getHdfsFile(String remotePath){
        try {
            FileSystem fileSystem = FileSystem.get(uri, cfg, HADOOP_USER_NAME);
            FileStatus[] fileStatuses = fileSystem.listStatus(new Path(remotePath));
            for(FileStatus status: fileStatuses){
                System.out.println(status.getPath());
                System.out.println(status.getBlockSize());
                System.out.println(status.getReplication());
                System.out.println(status.getPermission());
                System.out.println("******************************");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static void main(String[] args) throws Exception {
        Configuration conf = new Configuration();
        conf.set("fs.defaultFS", "hdfs://ns");
        conf.set("dfs.nameservices", "ns");
        conf.set("dfs.ha.namenodes.ns", "nn1,nn2");
        conf.set("dfs.namenode.rpc-address.ns.nn1", "node1:9000");
        conf.set("dfs.namenode.rpc-address.ns.nn2", "node2:9000");
        //conf.setBoolean(name, value);
        conf.set("dfs.client.failover.proxy.provider.ns", "org.apache.hadoop.hdfs.server.namenode.ha.ConfiguredFailoverProxyProvider");

        FileSystem fs = FileSystem.get(new URI("hdfs://ns"), conf, "root");
        boolean result = fs.exists(new Path("/hello/oem8.log"));
        System.out.println(result);
        /*InputStream in =new FileInputStream("c://oem8.log");
        OutputStream out = fs.create(new Path("/hello/oem8.log"));
        IOUtils.copyBytes(in, out, 4096, true);*/

        exitHdfsPath("/hello/oem8.log");
    }



}
