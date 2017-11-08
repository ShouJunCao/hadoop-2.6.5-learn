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
//第一个map计算TF 和 总数N
public class FirstMapper extends Mapper<LongWritable, Text, Text, IntWritable> {

    /**
     * TF 在一个文章中出现的词频 N总共多少文章
     * @param key
     * @param value
     * @param context
     * @throws IOException
     * @throws InterruptedException
     */
    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        String line = value.toString();
        String[] kw = line.split("\t");
        if(kw.length >= 2){
            String id = kw[0];
            String w = kw[1];

            StringReader stringReader = new StringReader(w);
            //对读入的内容进行分词处理，利用iK分词器
            IKSegmenter ikSegmenter = new IKSegmenter(stringReader, true);
            Lexeme word = null;
            while ((word = ikSegmenter.next()) != null){
                String wordValue = word.getLexemeText();

                context.write(new Text(wordValue + "_" + id), new IntWritable(1));
            }
            //一个mapper中可以有多向输出
            context.write(new Text("count"), new IntWritable(1));

        }else {
            System.out.println(kw.toString() + "------");
        }
    }
}
