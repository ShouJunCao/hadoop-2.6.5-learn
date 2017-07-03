/**
 * @FileName: WorldCountReduce.java
 * @Package: hadoop.mapreduce
 * @author caoshoujun
 * @created 2017/6/13 23:53
 * <p/>
 * Copyright 2016 ziroom
 */
package hadoop.mapreduce;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

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
public class WorldCountReduce extends Reducer<Text, IntWritable, Text, IntWritable> {

    protected void reduce(Text key, Iterable<IntWritable> iterable, Context context)throws IOException, InterruptedException{
        int sum = 0;
        for (IntWritable i : iterable){
            sum = sum + i.get();
        }
        context.write(key, new IntWritable(sum));
    }
}
