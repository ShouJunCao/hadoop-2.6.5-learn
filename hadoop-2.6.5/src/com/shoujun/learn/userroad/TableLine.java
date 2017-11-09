/**
 * @FileName: TableLine.java
 * @Package: com.shoujun.learn.userroad
 * @author caoshoujun
 * @created 2017/11/9 15:55
 * <p/>
 * Copyright 2016 ziroom
 */
package com.shoujun.learn.userroad;

import org.apache.hadoop.io.Text;

import java.text.ParseException;
import java.text.SimpleDateFormat;
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
public class TableLine {

    private String imis;
    private String position;
    private String time;
    private String timeflag;
    private Date day;

    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public void set(String line, boolean source, String date, String[] timepoit) throws LineException {
        String[] linesplit = line.split("\t");
        if(source){
            this.imis = linesplit[0];
            this.position = linesplit[3];
            this.time = linesplit[4];
        }else {
            this.imis = linesplit[0];
            this.position = linesplit[2];
            this.time = linesplit[3];
        }

        if(!this.time.startsWith(date)){
            throw new LineException("日期错误",0);
        }
        try {
            this.day = simpleDateFormat.parse(this.time);
        }catch (ParseException e){
            throw new LineException(e.getMessage(), 0);
        }
        int i = 0, n = timepoit.length;
        int hour = Integer.valueOf(this.time.split(" ")[1].split(":")[0]);

        while (i<n && Integer.valueOf(timepoit[i])<hour){
            i++;
        }
        if(i<n){
            if(i==0){
                this.timeflag = "00-"+timepoit[i];
            }else {
                this.timeflag = timepoit[i-1] + timepoit[i];
            }
        }else {
            throw new  LineException("", -1);
        }
    }

    public Text outKey(){
        return  new Text(this.imis+"|"+this.timeflag);
    }

    public Text outValue(){
        long t = day.getTime()/1000L;
        return new Text(this.position+"|"+String.valueOf(t));
    }
}
