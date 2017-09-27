package com.shoujun.learn.qqrecommend;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.awt.*;
import java.io.IOException;
import java.util.HashSet;

/**
 * Created by shoujun on 2017/9/26.
 */
public class QQRecommendReducer extends Reducer<Text, Text, Text, Text> {

    @Override
    protected void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
        HashSet<String> hashSet = new HashSet<>();
        for(Text text : values){
            hashSet.add(text.toString());
        }
        if(hashSet.size()>1){
            for (String p1 : hashSet){
                for (String p2 : hashSet){
                    if(!p1.equals(p2)){
                        context.write(new Text(p1),new Text(p2));
                    }
                }
            }
        }
    }
}
