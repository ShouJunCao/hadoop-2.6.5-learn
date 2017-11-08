package com.shoujun.learn.weibo;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.lib.partition.HashPartitioner;

/**
 * Created by shoujun on 2017/11/6.
 */
public class FirstPatition extends HashPartitioner<Text, IntWritable> {

    /**
     * 决定分区 N一个分区，tf三个分区，总共四个分区
     * @param key
     * @param value
     * @param numReduceTasks
     * @return
     */
    @Override
    public int getPartition(Text key, IntWritable value, int numReduceTasks) {
        if(key.equals(new Text("count"))){
            return 3;
        }
        return super.getPartition(key, value, numReduceTasks-1);
    }
}
