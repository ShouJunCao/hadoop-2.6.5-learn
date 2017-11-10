/**
 * @FileName: UserRoadReduce.java
 * @Package: com.shoujun.learn.userroad
 * @author caoshoujun
 * @created 2017/11/9 17:10
 * <p/>
 * Copyright 2016 ziroom
 */
package com.shoujun.learn.userroad;

import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

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
public class UserRoadReduce extends Reducer<Text, Text, NullWritable, Text> {

    private String date;

    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Override
    protected void setup(Context context) throws IOException, InterruptedException {
        this.date = context.getConfiguration().get("date");
    }

    @Override
    protected void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
        String[] ks = key.toString().split("\\|");
        String ismi = ks[0];
        String timeFlag = ks[1];

        TreeMap<Long, String> uploads = new TreeMap<>();
        String valueString;
        for(Text text : values){
             valueString = text.toString();
            try {
                uploads.put(Long.valueOf(valueString.split("\\|")[1]),valueString.split("\\|")[0]);
            }catch (NumberFormatException e){
                context.getCounter(UserRoadMapper.Counter.TIMESKIP).increment(1);
                continue;
            }
        }

        try {
            Date tmp = this.simpleDateFormat.parse(date+" "+timeFlag.split("-")[1]+":00:00");
            uploads.put(tmp.getTime()/1000L, "OFF");
            HashMap<String,Float> locs = getStayTime(uploads);
            for(Map.Entry<String,Float> entry : locs.entrySet()){
                StringBuilder sb = new StringBuilder();
                sb.append(ismi);
                sb.append("|");
                sb.append(entry.getKey());
                sb.append("|");
                sb.append(timeFlag);
                sb.append("|");
                sb.append(entry.getValue());
                sb.append("|");
                context.write(NullWritable.get(),new Text(sb.toString()));
            }
        }catch (ParseException e){
            context.getCounter(UserRoadMapper.Counter.USERSKIP).increment(1);
            return;
        }

    }

    private HashMap<String,Float> getStayTime(TreeMap<Long, String> uploads) {
        Map.Entry<Long, String> upload, nextupload;

        HashMap<String, Float> locs = new HashMap<>();
        Iterator<Map.Entry<Long, String>> it = uploads.entrySet().iterator();

        upload = it.next();
        while (it.hasNext()){
            nextupload = it.next();
            float diff = nextupload.getKey()/60F-upload.getKey()/60F;
            if(diff<=60.0){
                if(locs.containsKey(upload.getValue())){
                     locs.put(upload.getValue(), locs.get(upload.getValue())+diff);
                }else {
                    locs.put(upload.getValue(), diff);
                }
            }
             upload = nextupload;
        }
        return locs;
    }
}
