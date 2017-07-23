package com.shoujun.learn.hbase;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Put;

import java.io.IOException;
import java.util.UUID;

/**
 * Created by shoujun on 2017/7/23.
 */
public class BasicHbaseOprate {

    public static void main(String[] args) throws IOException {

        boolean create = createTable("myTable",new String[]{"user"});
        System.out.println("create result : " + create);

        boolean insert = insertTable("myTable");
        System.out.println("insert result : " + insert);
    }

    public static boolean createTable(String tableName, String[] cfs) throws IOException {

        Configuration config = HBaseConfiguration.create();

        config.set("hbase.zookeeper.quorum", "node1,node2,node3");

        Admin admin = new HBaseAdmin(config);

        TableName table = TableName.valueOf(tableName);

        if(admin.isTableAvailable(table)){

            admin.disableTable(table);

            admin.deleteTable(table);
        }

        HTableDescriptor tableDescriptor = new HTableDescriptor(table);

        for(String cfName : cfs){

            HColumnDescriptor cf = new HColumnDescriptor(cfName.getBytes());

            tableDescriptor.addFamily(cf);
        }

        admin.createTable(tableDescriptor);

        admin.close();

        return true;
    }


    public static boolean insertTable(String tableName) throws IOException {

        Configuration config = HBaseConfiguration.create();

        config.set("hbase.zookeeper.quorum", "node1,node2,node3");

        HTable hTable = new HTable(config, tableName);

        String rowKey = UUID.randomUUID()+"_"+System.currentTimeMillis();
        Put put = new Put(rowKey.getBytes());

        put.add("user".getBytes(),"name".getBytes(),"csj".getBytes());
        put.add("user".getBytes(),"age".getBytes(),"18".getBytes());
        hTable.put(put);

        rowKey = UUID.randomUUID()+"_"+System.currentTimeMillis();
        put = new Put(rowKey.getBytes());

        put.add("user".getBytes(),"name".getBytes(),"csj".getBytes());
        put.add("user".getBytes(),"age".getBytes(),"18".getBytes());
        hTable.put(put);

        hTable.close();
        return true;
    }

}
