package com.shoujun.learn.weibo;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.wltea.analyzer.core.IKSegmenter;
import org.wltea.analyzer.core.Lexeme;

import java.io.IOException;
import java.io.StringReader;

/**
 * Created by shoujun on 2017/11/6.
 */
public class FirstMapper extends Mapper<LongWritable, Text, Text, IntWritable> {

    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        String line = value.toString();
        String[] kw = line.split("\t");
        if(kw.length >= 2){
            String k = kw[0];
            String w = kw[1];

            StringReader stringReader = new StringReader(w);
            IKSegmenter ikSegmenter = new IKSegmenter(stringReader, true);
            Lexeme word = null;
            while ((word = ikSegmenter.next()) != null){
                String wordValue = word.getLexemeText();
                context.write(new Text(wordValue + "_" + k), new IntWritable(1));
            }
            context.write(new Text("count"), new IntWritable(1));

        }else {
            System.out.println(kw.toString() + "------");
        }
    }
}
