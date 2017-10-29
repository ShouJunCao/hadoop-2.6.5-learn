package com.shoujun.learn.yearhot;


import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by shoujun on 2017/10/29.
 */
public class HotRunJob{

    public static void main(String[] args) {
        String inputPath = "/hello/hotInput/data";
        String outPath = "/hello/hotOut";

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
            Job job = Job.getInstance(cfg, "hotJob");

            job.setJarByClass(HotRunJob.class);
            job.setMapperClass(HotMapper.class);
            job.setReducerClass(HotReducer.class);

            job.setMapOutputKeyClass(HotKey.class);
            job.setMapOutputValueClass(Text.class);

            job.setSortComparatorClass(HotSort.class);
            job.setPartitionerClass(HotPartition.class);
            job.setGroupingComparatorClass(HotGroup.class);

            job.setNumReduceTasks(3);

            FileInputFormat.addInputPath(job, new Path(inputPath));
            FileOutputFormat.setOutputPath(job, new Path(outPath));
            System.exit(job.waitForCompletion(true)? 0 : 1);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}

 class HotMapper extends Mapper<LongWritable, Text, HotKey, Text> {

    private  static SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd");
     @Override
     protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
         String line = value.toString();
         String[] ss = line.split("  ");
         if(ss.length == 2){
             try {
                 Date date = SDF.parse(ss[0]);
                 Calendar c = Calendar.getInstance();
                 c.setTime(date);
                 int year = c.get(1);
                 int hot = Integer.parseInt(ss[1].substring(0, ss[1].indexOf("°C")));

                 HotKey hotKey = new HotKey();
                 hotKey.setYear(year);
                 hotKey.setHot(hot);

                 context.write(hotKey, value);
             } catch (Exception e) {
                 e.printStackTrace();
             }
         }
     }
 }

 class HotReducer extends Reducer<HotKey, Text, HotKey, Text>{

     @Override
     protected void reduce(HotKey key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
         for (Text value : values) {
             context.write(key, value);
         }
     }
 }

