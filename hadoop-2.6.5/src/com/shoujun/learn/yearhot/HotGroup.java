package com.shoujun.learn.yearhot;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.io.WritableComparator;

/**
 * Created by shoujun on 2017/10/29.
 */
public class HotGroup extends WritableComparator {

    public HotGroup(){
        super(HotKey.class, true);
    }

    @Override
    public int compare(WritableComparable a, WritableComparable b) {
        HotKey hotKey1 = (HotKey) a;
        HotKey hotKey2 = (HotKey) b;
        return Integer.compare(hotKey1.getYear(), hotKey2.getYear());
    }
}
