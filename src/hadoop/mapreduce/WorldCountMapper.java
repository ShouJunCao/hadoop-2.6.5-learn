/**
 * @FileName: WorldCountMapper.java
 * @Package: hadoop.mapreduce
 * @author caoshoujun
 * @created 2017/6/13 22:41
 * <p/>
 * Copyright 2016 ziroom
 */
package hadoop.mapreduce;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;
import java.util.StringTokenizer;

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
public class WorldCountMapper extends Mapper<LongWritable,Text,Text,IntWritable> {

    @Override
    protected void map(LongWritable key, Text value, Context context)throws IOException,InterruptedException{
        String line = value.toString();
        StringTokenizer st = new StringTokenizer(line);
        while(st.hasMoreTokens()){
            String world = st.nextToken();
            context.write(new Text(world), new IntWritable(1));
        }
    }
}
