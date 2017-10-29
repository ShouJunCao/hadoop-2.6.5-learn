package com.shoujun.learn.yearhot;

import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.io.WritableComparator;

/**
 * Created by shoujun on 2017/10/29.
 */
public class HotSort extends WritableComparator {

    public HotSort(){
        super(HotKey.class, true);
    }

    @Override
    public int compare(WritableComparable a, WritableComparable b) {
        HotKey hotKey1 = (HotKey) a;
        HotKey hotKey2 = (HotKey) b;
        int res = Integer.compare(hotKey1.getYear(),hotKey2.getYear());
        if(res != 0){
            return res;
        }
        return -Integer.compare(hotKey1.getHot(), hotKey2.getHot());
    }
}
