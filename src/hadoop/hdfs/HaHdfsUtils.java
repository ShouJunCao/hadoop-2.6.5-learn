/**
 * @FileName: HaHdfsUtils.java
 * @Package: hadoop.hdfs
 * @author caoshoujun
 * @created 2017/6/28 15:59
 * <p/>
 * Copyright 2016 ziroom
 */
package hadoop.hdfs;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

import java.io.IOException;

import static com.sun.scenario.Settings.set;

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

    private static final String CLUSTER_NAME = "ns";

    private static final String HADOOP_URL = "hdfs://"+CLUSTER_NAME;

    public static Configuration cfg;

    static {
        System.setProperty("hadoop.home.dir", "E:\\Hadoop\\hadoop-2.6.5");
        cfg = new Configuration();
        cfg.set("fs.defaultFS", HADOOP_URL);
        cfg.set("dfs.nameservices", CLUSTER_NAME);
        cfg.set("dfs.namenodes."+CLUSTER_NAME,"nn1,nn2");
        cfg.set("dfs.namenode.rpc-address."+CLUSTER_NAME+".nn1","node1:9000");
        cfg.set("dfs.namenode.rpc-address."+CLUSTER_NAME+".nn2","node2:9000");
        cfg.set("dfs.client.failover.proxy.provider."+CLUSTER_NAME,"org.apache.hadoop.hdfs.server.namenode.ha.ConfiguredFailoverProxyProvider");
            set("dfs.client.failover.proxy.provider."+CLUSTER_NAME,"org.apache.hadoop.hdfs.server.namenode.ha.ConfiguredFailoverProxyProvider");
    }

    /**
     * 创建目录
     * @param path
     * @return
     */
    public static boolean createHdfsPath(String path){
        boolean result;
        try {
            FileSystem fileSystem = FileSystem.get(cfg);
            result = fileSystem.mkdirs(new Path(path));
        } catch (IOException e) {
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
            FileSystem fileSystem = FileSystem.get(cfg);
            result = fileSystem.exists(new Path(path));
        } catch (IOException e) {
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
            FileSystem fileSystem = FileSystem.get(cfg);
            //删除文件或者文件夹，是否进行递归删除
            result = fileSystem.delete(new Path(path),false);
        } catch (IOException e) {
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
            FileSystem fileSystem = FileSystem.get(cfg);
            result = fileSystem.createNewFile(new Path(path));
        } catch (IOException e) {
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
            FileSystem fileSystem = FileSystem.get(cfg);
            fileSystem.copyFromLocalFile(new Path(localPath), new Path(remotePath));
        } catch (IOException e) {
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
            FileSystem fileSystem = FileSystem.get(cfg);
            fileSystem.copyToLocalFile(new Path(remotePath),new Path(localPath));
        } catch (IOException e) {
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
            FileSystem fileSystem = FileSystem.get(cfg);
            FileStatus[] fileStatuses = fileSystem.listStatus(new Path(remotePath));
            for(FileStatus status: fileStatuses){
                System.out.println(status.getPath());
                System.out.println(status.getBlockSize());
                System.out.println(status.getReplication());
                System.out.println(status.getPermission());
                System.out.println("******************************");
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

}
