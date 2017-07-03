/**
 * @FileName: WorldCountRun.java
 * @Package: hadoop.mapreduce
 * @author caoshoujun
 * @created 2017/6/14 0:08
 * <p/>
 * Copyright 2016 ziroom
 */
package hadoop.mapreduce;

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
public class WorldCountRun {

    public static void main(String[] args) {
        final String input_path = "/hello/worldCount.txt";
        final String out_path = "/hello/worldCountResult";
        Configuration cfg = new Configuration();
        cfg.set("fs.defaultFS", "hdfs://ns");
        cfg.set("dfs.nameservices", "ns");
        cfg.set("dfs.ha.namenodes.ns","nn1,nn2");
        cfg.set("dfs.namenode.rpc-address.ns.nn1","node1:9000");
        cfg.set("dfs.namenode.rpc-address.ns.nn2","node2:9000");
        cfg.set("dfs.client.failover.proxy.provider.ns","org.apache.hadoop.hdfs.server.namenode.ha.ConfiguredFailoverProxyProvider");
        try {
            FileSystem fileSystem = FileSystem.get(new URI("hdfs://ns"),cfg,"root");
            if(fileSystem.exists(new Path(out_path))){
                fileSystem.delete(new Path(out_path), true);
            }
            Job job = Job.getInstance(cfg, "word count");
            job.setJarByClass(WorldCountRun.class);
            job.setMapperClass(WorldCountMapper.class);
            job.setReducerClass(WorldCountReduce.class);
            job.setMapOutputKeyClass(Text.class);
            job.setMapOutputValueClass(IntWritable.class);

            job.setNumReduceTasks(1);

            FileInputFormat.addInputPath(job, new Path(input_path));
            FileOutputFormat.setOutputPath(job, new Path(out_path));
            System.exit(job.waitForCompletion(true) ? 0 : 1);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
