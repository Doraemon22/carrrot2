/*
import java.io.IOException;
import java.util.Random;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.util.Bytes;

public class CreatTableHbase {
    public static void main(String[] args) throws IOException {
        Configuration conf = HBaseConfiguration.create();
        conf.set("hbase.zookeeper.quorum",  "data1:2020,data2:2020,data3:2020");  //  Zookeeper的地址
//          conf.set("hbase.zookeeper.property.clientPort", "42182");
        Random random = new Random();
        long a = random.nextInt(1000000000);
        String tableName = "emp";
        String rowkey = "rowkey"+a ;
        String columnFamily = "basicinfo";
        String column = "empname";
        //String value = string;
        HTable table=new HTable(conf, tableName);
        Put put=new Put(Bytes.toBytes(rowkey));
        put.add(Bytes.toBytes(columnFamily), Bytes.toBytes(column), Bytes.toBytes(string));
        table.put(put);//放入表
        System.out.println("放入成功");
        table.close();//释放资源
    }
    //设置HBase据库的连接配置参数
//        Configuration conf = HBaseConfiguration.create();
//        conf.set("hbase.zookeeper.quorum", "192.168.13.133");  //  Zookeeper的地址
//        conf.set("hbase.zookeeper.property.clientPort", "2020");
//        String tableName = "emp";
//        String[] family = {"basicinfo", "deptinfo"};

//        HBaseAdmin hbaseAdmin = new HBaseAdmin(conf);
//        //创建表对象
//        HTableDescriptor hbaseTableDesc = new HTableDescriptor(tableName);
//        for (int i = 0; i < family.length; i++) {
//            //设置表字段
//            hbaseTableDesc.addFamily(new HColumnDescriptor(family[i]));
//        }
//
//        //判断表是否存在，不存在则创建，存在则打印提示信息
//        if (hbaseAdmin.tableExists(tableName)) {tableName
//            System.out.println("TableExists!");
//            System.exit(0);
//        } else {
//            hbaseAdmin.createTable(hbaseTableDesc);
//            System.out.println("Create table Success!");
//        }
    }

*/
