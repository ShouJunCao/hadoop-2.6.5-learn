/**
 * @FileName: HotReducer.java
 * @Package: com.shoujun.learn.yearhot
 * @author caoshoujun
 * @created 2017/11/8 15:09
 * <p/>
 * Copyright 2016 ziroom
 */
package com.shoujun.learn.yearhot;

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
class HotReducer extends Reducer<HotKey, Text, HotKey, Text> {

     @Override
     protected void reduce(HotKey key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
         for (Text value : values) {
             context.write(key, value);
         }
     }
 }
