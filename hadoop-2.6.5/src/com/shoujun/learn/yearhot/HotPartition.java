package com.shoujun.learn.yearhot;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Partitioner;

/**
 * Created by shoujun on 2017/10/29.
 */
public class HotPartition extends Partitioner<HotKey, Text> {

    @Override
    public int getPartition(HotKey hotKey, Text text, int num) {
        return (hotKey.getYear() * 127) % num;
    }
}


