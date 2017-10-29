package com.shoujun.learn.yearhot;

import org.apache.hadoop.io.WritableComparable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

/**
 * Created by shoujun on 2017/10/29.
 */
public class HotKey implements WritableComparable<HotKey>{

    private int year;

    private int hot;

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getHot() {
        return hot;
    }

    public void setHot(int hot) {
        this.hot = hot;
    }


    @Override
    public int compareTo(HotKey key) {
        int res = Integer.compare(this.year, key.getYear());
        if(res != 0){
            return res;
        }
        return Integer.compare(this.hot, key.getHot());
    }

    @Override
    public void write(DataOutput dataOutput) throws IOException {
        dataOutput.writeInt(this.year);
        dataOutput.writeInt(this.hot);
    }

    @Override
    public void readFields(DataInput dataInput) throws IOException {
        this.year = dataInput.readInt();
        this.hot = dataInput.readInt();
    }

    @Override
    public int hashCode() {
        return new Integer(this.year + this.hot).hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof HotKey){
            HotKey hotKey = (HotKey)obj;
            return this.year * this.hot == hotKey.getYear() * hotKey.getHot();
        }
        return super.equals(obj);
    }

    @Override
    public String toString() {
        return "year:" + this.year + "," + "hot:" + this.hot;
    }
}
