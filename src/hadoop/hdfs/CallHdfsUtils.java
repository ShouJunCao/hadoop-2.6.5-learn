/**
 * @FileName: CallHdfsUtils.java
 * @Package: com.csj.learn.hadoop
 * @author caoshoujun
 * @created 2017/6/11 23:49
 * <p/>
 * Copyright 2016 ziroom
 */
package hadoop.hdfs;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.FsUrlStreamHandlerFactory;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

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
public class CallHdfsUtils {
    private static String hdfsUrl;

    public CallHdfsUtils(String url) {
        System.setProperty("hadoop.home.dir", "E:\\Hadoop\\hadoop-2.6.5");
        this.hdfsUrl = url;
    }

    public static void askWebUrl() throws IOException {
        URL url = new URL("http://www.baidu.com");
        InputStream inputStream = url.openStream();
        IOUtils.copyBytes(inputStream, System.out, 4096, false);
    }

    public static void askHdfsUrl() throws IOException {
        //需要设置下访问hdfs的url
        URL.setURLStreamHandlerFactory(new FsUrlStreamHandlerFactory());
        URL url = new URL(hdfsUrl);
        InputStream inputStream = url.openStream();
        IOUtils.copyBytes(inputStream, System.out, 4096, false);
    }

    /**
     * 使用Configuration
     *
     * @param path
     */
    public static boolean createFilePath(String path) throws IOException {
        Configuration configuration = new Configuration();
        configuration.set("fs.defaultFS",hdfsUrl);
        FileSystem fileSystem = FileSystem.get(configuration);
        boolean result = fileSystem.mkdirs(new Path(path));
        return result;
    }

    /**
     * 文件名或路径是否存在
     *
     * @param path
     * @return
     */
    public static boolean existsPath(String path) throws IOException {
        Configuration conf = new Configuration();
        conf.set("fs.defaultFS", hdfsUrl);
        FileSystem system = FileSystem.get(conf);
        return system.exists(new Path(path));
    }

    public static boolean delePath(String path) throws IOException {
        Configuration conf = new Configuration();
        conf.set("fs.defaultFS", hdfsUrl);
        FileSystem system = FileSystem.get(conf);
        //return system.delete(new Path(path),true);
        return system.deleteOnExit(new Path(path));//存在返回删除结果，不存在则直接删除
    }

    /**
     * 上传本地到远程
     *
     * @param localPath
     * @param remotePath
     * @return
     * @throws IOException
     */
    public static void upload(String localPath, String remotePath) throws IOException {
        Configuration conf = new Configuration();
        conf.set("fs.defaultFS", hdfsUrl);
        FileSystem system = FileSystem.get(conf);
        FSDataOutputStream fsDataOutputStream = system.create(new Path(remotePath), true);
        FileInputStream in = new FileInputStream(localPath);
        IOUtils.copyBytes(in, fsDataOutputStream, 4096, false);
    }

    /**
     * 替换IOUtils
     *
     * @param localPath
     * @param remotePath
     */
    public static void upload2(String localPath, String remotePath) throws IOException {
        Configuration conf = new Configuration();
        conf.set("fs.defaultFS", hdfsUrl);
        FSDataOutputStream fsDataOutputStream = null;
        FileInputStream in = null;
        try {
            in = new FileInputStream(localPath);
            FileSystem system = FileSystem.get(conf);
            fsDataOutputStream = system.create(new Path(remotePath), true);
            byte[] buff = new byte[4096];
            int len = in.read(buff);
            while (len != -1) {
                fsDataOutputStream.write(buff, 0, len);
                len = in.read(buff);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fsDataOutputStream != null) {
                fsDataOutputStream.close();
            }
            if (in != null) {
                in.close();
            }
        }


    }

    public void download(String localPath,String hdfsPath) throws IOException {
        URL.setURLStreamHandlerFactory(new FsUrlStreamHandlerFactory());
        URL url = new URL(hdfsUrl+hdfsPath);
        InputStream inputStream = url.openStream();
        FileOutputStream fos=new FileOutputStream(localPath);
        byte[] buffer=new byte[4096];
        int len=inputStream.read(buffer);
        while (len!=-1){
            fos.write(buffer,0,len);
            len=inputStream.read(buffer);
        }
        fos.close();
        inputStream.close();
    }


    public void getDesc(String path) throws IOException {
        Configuration conf = new Configuration();
        conf.set("fs.defaultFS", hdfsUrl);
        FileSystem system = FileSystem.get(conf);
        FileStatus[] statuses=system.listStatus(new Path(path));
        for(FileStatus status:statuses){
            System.out.println(status.getPath());
            System.out.println(status.getBlockSize());
            System.out.println(status.getReplication());
            System.out.println(status.getPermission());
            System.out.println("******************************");
        }
    }
}
