package com.shoujun.learn.weibo;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

/**
 * Created by shoujun on 2017/11/7.
 */
public class ThirdReducer extends Reducer<Text, Text, Text, Text> {

    @Override
    protected void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
        StringBuffer stringBuffer = new StringBuffer();

        for (Text text : values){
            stringBuffer.append(text.toString()+"\t");
        }
        context.write(key, new Text(stringBuffer.toString()));
    }
}
