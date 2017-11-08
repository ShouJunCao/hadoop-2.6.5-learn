package com.shoujun.learn.weibo;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;

import java.io.IOException;

/**
 * Created by shoujun on 2017/11/7.
 */
public class SecondMapper extends Mapper<LongWritable, Text, Text, IntWritable> {

    /**
     * 计算 DF的值 在多少个文章中出现过
     * @param key
     * @param value
     * @param context
     * @throws IOException
     * @throws InterruptedException
     */
    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {

        //获取当前maptask 的数据片段
        FileSplit fileSplit = (FileSplit)context.getInputSplit();

        //count 不被统计
        if(!fileSplit.getPath().getName().contains("part-r-00003")){
            String[] values = value.toString().trim().split("\t");
            if(values.length>=2){
                String[] split = values[0].split("_");
                if(split.length>=2){
                    String id = split[0];
                    context.write(new Text(id), new IntWritable(1));
                }
            }
        }else {
            System.out.println(value.toString()+"---");
        }
    }
}
