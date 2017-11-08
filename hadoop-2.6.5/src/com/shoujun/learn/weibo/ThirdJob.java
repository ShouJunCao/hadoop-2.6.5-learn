package com.shoujun.learn.weibo;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.net.URI;

/**
 * Created by shoujun on 2017/11/7.
 */
public class ThirdJob {

    public static void main(String[] args) {
        String inputPath = "/hello/weiboOut1";
        String outPath = "/hello/weiboOut3";

        Configuration cfg = new Configuration();
        cfg.set("fs.defaultFS", "hdfs://ns");
        cfg.set("dfs.nameservices","ns");
        cfg.set("dfs.ha.namenodes.ns", "nn1,nn2");
        cfg.set("dfs.namenode.rpc-address.ns.nn1","node1:9000");
        cfg.set("dfs.namenode.rpc-address.ns.nn2","node2:9000");
        cfg.set("dfs.client.failover.proxy.provider.ns","org.apache.hadoop.hdfs.server.namenode.ha.ConfiguredFailoverProxyProvider");


        try{
            FileSystem fileSystem = FileSystem.get(new URI("hdfs://ns"), cfg, "root");
            if(fileSystem.exists(new Path(outPath))){
                fileSystem.delete(new Path(outPath), true);
            }
            Job job = Job.getInstance(cfg, "third");

            job.setJarByClass(ThirdJob.class);
            job.setMapperClass(ThirdMapper.class);
            job.setReducerClass(ThirdReducer.class);

            //把微博总数加载到内存
            job.addCacheFile(new Path("/hello/weiboOut1/part-r-00003").toUri());
            //把df加载到内存
            job.addCacheFile(new Path("/hello/weiboOut2/part-r-00000").toUri());

            job.setMapOutputKeyClass(Text.class);
            job.setMapOutputValueClass(Text.class);


            FileInputFormat.addInputPath(job, new Path(inputPath));
            FileOutputFormat.setOutputPath(job, new Path(outPath));

            boolean waitForCompletion  = job.waitForCompletion(true);
            if (waitForCompletion){
                System.out.println("执行job成功");
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
