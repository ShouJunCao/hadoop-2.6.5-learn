package com.shoujun.learn.weibo;

import org.apache.commons.collections.map.HashedMap;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.text.NumberFormat;
import java.util.Map;

/**
 * Created by shoujun on 2017/11/7.
 */
public class ThirdMapper extends Mapper<LongWritable, Text, Text, Text> {

    //存放微博总数，将小数据缓存进内存，预加载
    private static Map<String, Integer> cmap = null;

    //存放DF
    public static Map<String, Integer> df = null;

    @Override
    protected void setup(Context context) throws IOException, InterruptedException {
        System.out.println("*************");
        if(cmap == null || cmap.isEmpty() || df == null || df.isEmpty()){
            URI[] cacheFiles = context.getCacheFiles();
            if(cacheFiles != null){
                for (URI uri : cacheFiles){
                    if(uri.getPath().endsWith("part-r-00003")){
                        Path path = new Path(uri.getPath());
                        //获取文件
                        Configuration configuration = context.getConfiguration();
                        FileSystem fileSystem = FileSystem.get(configuration);
                        FSDataInputStream open = fileSystem.open(path);
                        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(open));

                        String line = bufferedReader.readLine();
                        if(line.startsWith("count")){
                            String[] split = line.split("\t");
                            cmap = new HashedMap();
                            cmap.put(split[0], Integer.parseInt(split[1].trim()));
                        }
                        bufferedReader.close();

                    }else if (uri.getPath().endsWith("part-r-00000")){
                        df = new HashedMap();
                        Path path = new Path(uri.getPath());

                        //获取文件
                        Configuration configuration = context.getConfiguration();
                        FileSystem fileSystem = FileSystem.get(configuration);
                        FSDataInputStream open = fileSystem.open(path);
                        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(open));

                        String line = null;
                        while ((line=bufferedReader.readLine()) != null){
                            String[] split = line.split("\t");
                            df.put(split[0],Integer.parseInt(split[1].trim()));

                        }
                        bufferedReader.close();
                    }
                }
            }
        }
    }

    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        //获取分片
        FileSplit fileSplit = (FileSplit)context.getInputSplit();

        if(!fileSplit.getPath().getName().contains("part-r-00003")){
            String[] values = value.toString().split("\t");
            if(values.length>=2){
                int tf = Integer.parseInt(values[1].trim());
                String[] ss = values[0].split("_");
                if(ss.length>=2){
                    String word = ss[0];
                    String id  = ss[1];

                    //根据公式计算
                    Double s = tf*Math.log(cmap.get("count"))/df.get(word);
                    NumberFormat numberFormat = NumberFormat.getNumberInstance();

                    //取小数点后5位
                    numberFormat.setMaximumFractionDigits(5);

                    context.write(new Text(id), new Text(word+":"+numberFormat.format(s)));
                }else {
                    System.out.println(value.toString()+"-------");
                }
            }
        }
    }
}
