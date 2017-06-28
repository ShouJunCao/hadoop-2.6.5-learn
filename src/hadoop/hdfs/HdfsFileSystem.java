package hadoop.hdfs;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;

public class HdfsFileSystem {

    public static void main(String[] args) throws Exception  {
        //String uri="hdfs://LL-167:8020/";   //hdfs 地址
        // String remote="hdfs://LL-167:8020/lin/in1/1.txt"; //hdfs上的路径
        String uri="hdfs://192.168.126.128:9000";   //hdfs 地址
        String local="D:/hello.txt";  //本地路径
        String remote="hdfs://192.168.126.128:9000/hello/";
        Configuration conf = new Configuration();
        conf.set("fs.defaultFS", uri);

        //cat(conf ,uri,"hdfs://LL-167:8020/lin/in1/1.txt");
        //download(conf ,uri,remote,local);
        //  delete(conf ,uri,"hdfs://192.168.0.173:8020/Workspace/houlinlin");
        // markDir(conf ,uri,"hdfs://192.168.0.151:8020/Workspace/houlinlin/file/apply/2014/8a8380c7459dd8b90145a1fafb500235");

        //  checkDir(uri,"d:/file");
        // getFile(conf ,uri,"","hdfs://192.168.0.151:8020/Workspace/houlinlin/a.txt");
        copyFile(conf,uri,local,remote+"ccc.txt");
        //   ls(conf ,"hdfs://fulonghadoop","hdfs://fulonghadoop/");
    }
    /**
     * 上传文件
     * @param conf
     * @param local
     * @param remote
     * @throws IOException
     */
    public static void copyFile(Configuration conf , String uri , String local, String remote) throws IOException {
        FileSystem fs = FileSystem.get(URI.create(uri), conf);
        fs.copyFromLocalFile(new Path(local), new Path(remote));
        System.out.println("copy from: " + local + " to " + remote);
        fs.close();
    }

    /**
     * 获取hdfs上文件流
     * @param conf
     * @param uri
     * @param local
     * @param remote
     * @throws IOException
     */
    public static  void getFileStream(Configuration conf , String uri , String local, String remote) throws IOException{
        FileSystem fs = FileSystem.get(URI.create(uri), conf);
        Path path= new Path(remote);
        FSDataInputStream in = fs.open(path);//获取文件流
        FileOutputStream fos = new FileOutputStream("C:/Users/Administrator/Desktop/b.txt");//输出流
        int ch = 0;
        while((ch=in.read()) != -1){
            fos.write(ch);
        }
        System.out.println("-----");
        in.close();
        fos.close();
    }

    /**
     * 创建文件夹
     * @param conf
     * @param uri
     * @param remoteFile
     * @throws IOException
     */
    public static void markDir(Configuration conf , String uri , String remoteFile ) throws IOException{
        FileSystem fs = FileSystem.get(URI.create(uri), conf);
        Path path = new Path(remoteFile);

        fs.mkdirs(path);
        System.out.println("创建文件夹"+remoteFile);

    }
    /**
     * 查看文件
     * @param conf
     * @param uri
     * @param remoteFile
     * @throws IOException
     */
    public static void cat(Configuration conf , String uri ,String remoteFile) throws IOException {
        Path path = new Path(remoteFile);
        FileSystem fs = FileSystem.get(URI.create(uri), conf);
        FSDataInputStream fsdis = null;
        System.out.println("cat: " + remoteFile);
        try {
            fsdis = fs.open(path);
            IOUtils.copyBytes(fsdis, System.out, 4096, false);
        } finally {
            IOUtils.closeStream(fsdis);
            fs.close();
        }
    }
    /**
     * 下载 hdfs上的文件
     * @param conf
     * @param uri
     * @param remote
     * @param local
     * @throws IOException
     */
    public static void download(Configuration conf , String uri ,String remote, String local) throws IOException {
        Path path = new Path(remote);
        FileSystem fs = FileSystem.get(URI.create(uri), conf);
        fs.copyToLocalFile(path, new Path(local));
        System.out.println("download: from" + remote + " to " + local);
        fs.close();
    }
    /**
     * 删除文件或者文件夹
     * @param conf
     * @param uri
     * @param filePath
     * @throws IOException
     */
    public static void delete(Configuration conf , String uri,String filePath) throws IOException {
        Path path = new Path(filePath);
        FileSystem fs = FileSystem.get(URI.create(uri), conf);
        fs.deleteOnExit(path);
        System.out.println("Delete: " + filePath);
        fs.close();
    }
    /**
     * 查看目录下面的文件
     * @param conf
     * @param uri
     * @param folder
     * @throws IOException
     */
    public static  void ls(Configuration conf , String uri , String folder) throws IOException {
        Path path = new Path(folder);
        FileSystem fs = FileSystem.get(URI.create(uri), conf);
        FileStatus[] list = fs.listStatus(path);
        System.out.println("ls: " + folder);
        System.out.println("==========================================================");
        for (FileStatus f : list) {
            System.out.printf("name: %s, folder: %s, size: %d\n", f.getPath(),f.isDir() , f.getLen());
        }
        System.out
                .println("==========================================================");
        fs.close();
    }

    /**
     *
     * @param parentName 绝对路径地址
     * @throws Exception
     */
    /*public static void checkDir(String uri,String parentName)	throws Exception{
        //D:\file
        Configuration conf = new Configuration();
        File file = new File(parentName);
        boolean flag = true;
        while (flag)	{
            //查出parentName下的所有文件
            File[] fileNames = file.listFiles(new FileFilter());
            if(fileNames != null)	{
                for (int i = 0; i < fileNames.length; i++) {
                    File f = fileNames[i];
                    //System.out.println("parent directory:"+f.getParent()+",file name:"+f.getName());
                    System.out.println("parent directory:"+f.getParent().replace("\\", "/").substring(2)+",file name:"+f.getName());
                    String remoteFolrd= "hdfs://192.168.0.173:8020/Workspace/houlinlin"+f.getParent().replace("\\", "/").substring(2);
                    markDir(conf ,uri,remoteFolrd);
                    copyFile(conf ,uri,f.getParent()+"\\"+f.getName(),remoteFolrd);
                }
            }
            //查出parentName下的所有目录
            File[] directories = file.listFiles(new DirectoryFileFilter());
            if(directories != null)	{
                for (int i = 0; i < directories.length; i++) {
                    File dir = directories[i];
                    //绝对路径
                    String path =  dir.getAbsolutePath();
                    //递归
                    checkDir(uri,path);
                }
            }
            flag = false;
        }
    }*/

}
