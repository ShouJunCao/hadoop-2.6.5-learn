/**
 * @FileName: UserRoadMapper.java
 * @Package: com.shoujun.learn.userroad
 * @author caoshoujun
 * @created 2017/11/9 15:39
 * <p/>
 * Copyright 2016 ziroom
 */
package com.shoujun.learn.userroad;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;

import java.io.IOException;

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
public class UserRoadMapper extends Mapper<LongWritable, Text, Text, Text> {

    enum Counter{
        TIMESKIP,
        OUTOFTIMESKIP,
        LINESKIP,
        USERSKIP
    }
    String date;
    String[] timepoint;
    boolean dataSource;

    @Override
    protected void setup(Context context) throws IOException, InterruptedException {
        this.date = context.getConfiguration().get("date");
        this.timepoint = context.getConfiguration().get("timepoint").split("-");
        FileSplit fileSplit = (FileSplit)context.getInputSplit();
        String fileName = fileSplit.getPath().getName();
        if(fileName.startsWith("POS")){
            dataSource = true;
        }else if(fileName.startsWith("NET")){
            dataSource = false;
        }else {
            throw new IOException("file name incorrect!");
        }
    }

    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        String line = value.toString();
        TableLine tableLine = new TableLine();
        try {
            tableLine.set(line,this.dataSource, this.date, this.timepoint);
        }catch (LineException e){
            if(e.getFlag() == -1){
                context.getCounter(Counter.OUTOFTIMESKIP).increment(1);
            }else{
                context.getCounter(Counter.TIMESKIP).increment(1);
            }
            return;
        }catch (Exception e){
            context.getCounter(Counter.LINESKIP).increment(1);
            return;
        }
        context.write(tableLine.outKey(), tableLine.outValue());
    }
}
