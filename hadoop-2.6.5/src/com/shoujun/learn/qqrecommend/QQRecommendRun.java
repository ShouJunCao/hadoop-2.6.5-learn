package com.shoujun.learn.qqrecommend;


import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.net.URI;


/**
 * Created by shoujun on 2017/9/26.
 */
public class QQRecommendRun {

    public static void main(String[] args) {
        String inputPath = "/hello/qqRecommendInput";
        String outPath = "/hello/qqRecommendOut";

        Configuration cfg = new Configuration();
        cfg.set("fs.defaultFS", "hdfs://ns");
        cfg.set("dfs.nameservices","ns");
        cfg.set("dfs.ha.namenodes.ns", "nn1,nn2");
        cfg.set("dfs.namenode.rpc-address.ns.nn1","node1:9000");
        cfg.set("dfs.namenode.rpc-address.ns.nn2","node2:9000");
        cfg.set("dfs.client.failover.proxy.provider.ns","org.apache.hadoop.hdfs.server.namenode.ha.ConfiguredFailoverProxyProvider");


        /*Configuration cfg = new Configuration();
        cfg.set("fs.defaultFS", "hdfs://ns");
        cfg.set("dfs.nameservices", "ns");
        cfg.set("dfs.ha.namenodes.ns","nn1,nn2");
        cfg.set("dfs.namenode.rpc-address.ns.nn1","node1:9000");
        cfg.set("dfs.namenode.rpc-address.ns.nn2","node2:9000");
        cfg.set("dfs.client.failover.proxy.provider.ns","org.apache.hadoop.hdfs.server.namenode.ha.ConfiguredFailoverProxyProvider");*/

        try{
            FileSystem fileSystem = FileSystem.get(new URI("hdfs://ns"), cfg, "root");
            if(fileSystem.exists(new Path(outPath))){
                fileSystem.delete(new Path(outPath), true);
            }
            Job job = Job.getInstance(cfg, "QQRecommend");

            job.setJarByClass(QQRecommendRun.class);
            job.setMapperClass(QQRecommendMapper.class);
            job.setReducerClass(QQRecommendReducer.class);

            job.setMapOutputKeyClass(Text.class);
            job.setMapOutputValueClass(Text.class);

            job.setNumReduceTasks(1);

            FileInputFormat.addInputPath(job, new Path(inputPath));
            FileOutputFormat.setOutputPath(job, new Path(outPath));
            System.exit(job.waitForCompletion(true)? 0 : 1);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
