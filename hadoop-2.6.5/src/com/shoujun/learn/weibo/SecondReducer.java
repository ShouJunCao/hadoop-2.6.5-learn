package com.shoujun.learn.weibo;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

/**
 * Created by shoujun on 2017/11/7.
 */
public class SecondReducer extends Reducer<Text, IntWritable, Text, IntWritable> {

    @Override
    protected void reduce(Text key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {
        int sum = 0;
        for (IntWritable intWritable : values){
            sum += intWritable.get();
        }
        context.write(new Text(key), new IntWritable(sum));
    }
}
