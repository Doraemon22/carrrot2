import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.CellUtil;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.filter.CompareFilter;
import org.apache.hadoop.hbase.filter.FilterList;
import org.apache.hadoop.hbase.filter.PrefixFilter;
import org.apache.hadoop.hbase.filter.SingleColumnValueFilter;
import org.apache.hadoop.hbase.util.Bytes;
import org.junit.Test;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class HbaseDemo {
    private static Configuration conf = HBaseConfiguration.create();
    private static Admin admin;
    public static Connection connection;
    static {
        conf.set("hbase.zookeeper.quorum",  "data1:2020,data2:2020,data3:2020");
        conf.set("hbase.master", "192.168.13.133:17121");
//        conf.set("hbase.zookeeper.property.clientPort", "2020");
        try {
           connection = ConnectionFactory.createConnection(conf);
            admin = connection.getAdmin();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void createTable(String tableName, String... columnFamily) {
        TableName tableNameObj = TableName.valueOf(tableName);
        try {
            if (admin.tableExists(tableNameObj)) {
                admin.disableTable(tableNameObj);
                admin.deleteTable(tableNameObj);
                System.out.println("Table: " + tableName + " already exists! so deleted successful");
            }
//            else {
//                HTableDescriptor tb = new HTableDescriptor(tableNameObj);
                TableDescriptorBuilder tbuilder= TableDescriptorBuilder.newBuilder(tableNameObj);
                for (int i = 0; i < columnFamily.length; i++) {
//                    HColumnDescriptor family = new HColumnDescriptor(columnFamily[i]);
                    ColumnFamilyDescriptor info=ColumnFamilyDescriptorBuilder.of(columnFamily[i]);
                    tbuilder.setColumnFamily(info);
//                    tb.addFamily(family);
                }
                TableDescriptor tdesc=tbuilder.build();
                admin.createTable(tdesc);
//                admin.createTable(tb);
                System.out.println(tableName + "创建成功");
//            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println(tableName + "创建失败");
        }
    }

    /**
     * 产生时间
     */
    private String getDate(String string){
        //月-日-小时-分钟-秒
        return string + String.format("%02d%02d%02d%02d%02d", r.nextInt(12) + 1,
                r.nextInt(31), r.nextInt(24), r.nextInt(60), r.nextInt(60));
    }
    /**
     * 产生手机号后9位
     */
    Random r = new Random();
    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmmss");
    private String getPhone(String phonePrefix){
        return phonePrefix + String.format("%08d", r.nextInt(99999999));
    }
    /**
     * 10个用户，每个用户每年产生1000条通话记录
     * dnum:对方手机号
     * type:类型：0主叫，1被叫
     * length：长度
     * data：时间
     */
    @Test
    public void insert(String tableName, String... columnFamily)  {
        List<Put> puts = new ArrayList<Put>();
        TableName tableNameObj = TableName.valueOf(tableName);
//        Table table= null;
        try {
            Table  table = connection.getTable(tableNameObj);
        for (int i = 0; i < 5; i++){
            String phoneNumber = getPhone("158");
            for (int j = 0; j< 2; j++){
                // 属性
                String dnum = getPhone("177");
                String length = String.valueOf(r.nextInt(99)); // 产生[0, n)的随机数
                String type = String.valueOf(r.nextInt(2)); // [0, 2)
                String date = getDate("2019");
                //rowkey设计
                String rowkey = phoneNumber + "_" + (Long.MAX_VALUE - sdf.parse(date).getTime());
                Put put = new Put(rowkey.getBytes());
                System.out.println(dnum + "成功");
                put.addColumn("tel".getBytes(), "dnum".getBytes(), dnum.getBytes());
                System.out.println( put.addColumn("tel".getBytes(), "dnum".getBytes(), dnum.getBytes()) + "插入成功");
                put.addColumn(columnFamily[0].getBytes(), "length".getBytes(), length.getBytes());
                put.addColumn(columnFamily[0].getBytes(), "type".getBytes(), type.getBytes());
                put.addColumn(columnFamily[1].getBytes(), "date".getBytes(), date.getBytes());
//                puts.add(put);
                table.put(put);
            }
        }
        // 全部插入
//            System.out.println(puts + "puts");
//        table.put(puts);
        System.out.println(tableName + "插入成功");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {  //   Table table= null;
            e.printStackTrace();
        }
    }

    public void add() throws IOException {
        TableName tname=TableName.valueOf("hbaseTest");
        Table t=connection.getTable(tname);
        Put put=new Put("001".getBytes());
        put.addColumn("tel".getBytes(), "dnum".getBytes(),"11101101110".getBytes());
        put.addColumn("tel".getBytes(), "type".getBytes(),"0".getBytes());
        put.addColumn("tel".getBytes(), "length".getBytes(),"11".getBytes());
        put.addColumn("datetime".getBytes(), "datetime".getBytes(),"2020-12-09".getBytes());
        t.put(put);
        t.close();
        System.out.println("human表中插入数据成功！hehe");
    }
    /**  用到过滤器加快查询速度
     * 查询每一个用户，所有的主叫电话
     * 条件：
     * 1.电话号码
     * 2.type=0
     */
    @Test
    public void scan2(String tableName, String... columnFamily)  {
        // MUST_PASS_ONE只要scan的数据行符合其中一个filter就可以返回结果(但是必须扫描所有的filter)，
        //另外一种MUST_PASS_ALL必须所有的filter匹配通过才能返回数据行(但是只要有一个filter匹配没通过就算失败，后续的filter停止匹配)
        // 因为要查询所有主叫号码，所以选择MUST_PASS_ALL
        TableName tableNameObj = TableName.valueOf(tableName);
        Table  table = null;
        try {
            table = connection.getTable(tableNameObj);

        FilterList filters = new FilterList(FilterList.Operator.MUST_PASS_ALL);
        SingleColumnValueFilter filter1 = new SingleColumnValueFilter(columnFamily[0].getBytes(), "type".getBytes(),
                CompareFilter.CompareOp.EQUAL, "0".getBytes());
        SingleColumnValueFilter filter3 = new SingleColumnValueFilter(columnFamily[1].getBytes(), "type".getBytes(),
                CompareFilter.CompareOp.EQUAL, "0".getBytes());
        PrefixFilter filter2 = new PrefixFilter("15891411775".getBytes());
        filters.addFilter(filter1);
        filters.addFilter(filter2);
        filters.addFilter(filter3);
        Scan scan = new Scan();
        scan.setFilter(filters);
        ResultScanner scanner = table.getScanner(scan);
        for (Result result : scanner){
            System.out.println(tableName + "查询成功222");
            System.out.print(Bytes.toString(CellUtil.cloneValue(result.getColumnLatestCell(columnFamily[0].getBytes(), "dnum".getBytes()))));
            System.out.print("--" + Bytes.toString(CellUtil.cloneValue(result.getColumnLatestCell(columnFamily[0].getBytes(), "type".getBytes()))));
            System.out.print("--" + Bytes.toString(CellUtil.cloneValue(result.getColumnLatestCell(columnFamily[1].getBytes(), "date".getBytes()))));
            System.out.println("--" + Bytes.toString(CellUtil.cloneValue(result.getColumnLatestCell(columnFamily[0].getBytes(), "length".getBytes()))));
        }
        scanner.close();
        System.out.println(tableName + "查询成功");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) throws IOException {
        HbaseDemo hbaseDemo = new HbaseDemo();
//        hbaseDemo.createTable("hbaseTest", "tel", "datetime");
//        hbaseDemo.insert("hbaseTest", "tel", "datetime");
        hbaseDemo.add();
        hbaseDemo.scan2("hbaseTest", "tel", "datetime");
    }

}

