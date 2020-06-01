import java.io.IOException;
import java.text.DecimalFormat;

import jodd.util.PropertiesUtil;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.client.ColumnFamilyDescriptor;
import org.apache.hadoop.hbase.client.ColumnFamilyDescriptorBuilder;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.client.Delete;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.client.TableDescriptor;
import org.apache.hadoop.hbase.client.TableDescriptorBuilder;
import org.apache.hadoop.hbase.util.Bytes;

import static jodd.util.PropertiesUtil.*;
//import org.apache.logging.log4j.util.PropertiesUtil;

//public class HbaseDao {
//    public static Connection con;
//
//    public static void main(String[] args) throws IOException {
//        //设置HBase据库的连接配置参数
//        Configuration conf = HBaseConfiguration.create();
//        conf.set("hbase.zookeeper.quorum", "192.168.13.133");  //  Zookeeper的地址
//        conf.set("hbase.zookeeper.property.clientPort", "2020");
//        String tableName = "emp";
//        String[] family = {"basicinfo", "deptinfo"};
//    }
//}

/**
 * Hbase数据访问对象
 *//*


public class HbaseDao {
    private DecimalFormat df = new DecimalFormat() ;
    private Table table = null ;
    private int partitions ;
    private String flag  ;
    public HbaseDao(){
        try {
            Configuration conf = HBaseConfiguration.create();
            Connection conn = ConnectionFactory.createConnection(conf);
            TableName name = TableName.valueOf(getProp("table.name"));
            table = conn.getTable(name);
             df.applyPattern(getProp("hashcode.pattern"));
            partitions = Integer.parseInt(getProp("partition.number"));
            flag = getProp("caller.flag") ;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    */
/**

     * put数据到hbase

     *//*


    public void put(String log){
        if (log == null || log.equals("")) {
            return;
        }
        try {
            //解析日志
            String[] arr = log.split(",");
            if (arr != null && arr.length == 4) {
                String caller = arr[0];
                String callee = arr[1];
                String callTime = arr[2];
                callTime = callTime.replace("/","") ;       //删除/
                callTime = callTime.replace(" ","") ;       //删除空格
                callTime = callTime.replace(":","") ;       //删除空格
                String callDuration = arr[3];
                //结算区域号
                //构造put对象
                String rowkey = genRowkey(getHashcode(caller, callTime), caller, callTime, flag, callee, callDuration);
                Put put = new Put(Bytes.toBytes(rowkey));
                put.addColumn(Bytes.toBytes("f1"), Bytes.toBytes("caller"), Bytes.toBytes(caller));
                put.addColumn(Bytes.toBytes("f1"), Bytes.toBytes("callee"), Bytes.toBytes(callee));
                put.addColumn(Bytes.toBytes("f1"), Bytes.toBytes("callTime"), Bytes.toBytes(callTime));
                put.addColumn(Bytes.toBytes("f1"), Bytes.toBytes("callDuration"), Bytes.toBytes(callDuration));
                table.put(put);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getHashcode(String caller ,String callTime){
        int len = caller.length();
        //取出后四位电话号码
        String last4Code = caller.substring(len - 4);
        //取出时间单位,年份和月份.
        String mon = callTime.substring(0,6);
        int hashcode = (Integer.parseInt(mon) ^ Integer.parseInt(last4Code)) % partitions ;
        return df.format(hashcode);
    }
    */
/**
     * 生成rowkey
     *//*

    public String genRowkey(String hash,String caller,String time,String flag,String callee,String duration){
        return hash + "," + caller + "," + time + "," + flag + "," + callee + "," + duration ;
    }
*/

//}

