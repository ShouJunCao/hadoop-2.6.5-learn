/**
 * @FileName: HotMapper.java
 * @Package: com.shoujun.learn.yearhot
 * @author caoshoujun
 * @created 2017/11/8 15:09
 * <p/>
 * Copyright 2016 ziroom
 */
package com.shoujun.learn.yearhot;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

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
class HotMapper extends Mapper<LongWritable, Text, HotKey, Text> {

   private  static SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd");
    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        String line = value.toString();
        String[] ss = line.split("  ");
        if(ss.length == 2){
            try {
                Date date = SDF.parse(ss[0]);
                Calendar c = Calendar.getInstance();
                c.setTime(date);
                int year = c.get(1);
                int hot = Integer.parseInt(ss[1].substring(0, ss[1].indexOf("°C")));

                HotKey hotKey = new HotKey();
                hotKey.setYear(year);
                hotKey.setHot(hot);

                context.write(hotKey, value);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
