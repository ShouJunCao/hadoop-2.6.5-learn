package com.shoujun.learn.qqrecommend;



import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;


/**
 * Created by shoujun on 2017/9/26.
 */

 public class QQRecommendMapper extends Mapper<LongWritable, Text, Text, Text> {

    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        String line = value.toString();
        String[] names = line.split("\t");
        context.write(new Text(names[0]), new Text(names[1]));
        context.write(new Text(names[1]), new Text(names[0]));
    }
}
