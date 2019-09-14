package com.sinobest.eshbasetest.util;

import com.sinobest.eshbasetest.domain.Doc;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.*;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.filter.CompareFilter.CompareOp;
import org.apache.hadoop.hbase.filter.RegexStringComparator;
import org.apache.hadoop.hbase.filter.RowFilter;
import org.apache.hadoop.hbase.util.Bytes;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class HbaseUtils {
    private static final Logger LOG = LoggerFactory.getLogger(HbaseUtils.class);

    /**
     * HBASE 表名称
     */
    public final String TABLE_NAME = "doc";
    /**
     * 列簇1 文章信息
     */
    public final String COLUMNFAMILY_1 = "cf";
    /**
     * 列簇1中的列
     */
    public final String COLUMNFAMILY_1_TITLE = "title";
    public final String COLUMNFAMILY_1_AUTHOR = "author";
    public final String COLUMNFAMILY_1_CONTENT = "content";
    public final String COLUMNFAMILY_1_DESCRIBE = "describe";
    public final String COLUMNFAMILY_1_CJSJ = "cjsj";


    public static Admin admin = null;
    public static Connection conn = null;

    /**
     * 构造函数加载配置
     */
    public HbaseUtils() {
        Configuration conf = HBaseConfiguration.create();
        conf.set("hbase.zookeeper.quorum", "hadoop01,hadoop02,hadoop03");
        try {
            conn = ConnectionFactory.createConnection(conf);
            admin = conn.getAdmin();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws Exception {
        HbaseUtils hbase = new HbaseUtils();
        //创建一张表
		hbase.createTable("doc","cf");
		//查询所有表名
		hbase.getALLTable();
		//往表中添加一条记录
		hbase.put(hbase.TABLE_NAME, "1", hbase.COLUMNFAMILY_1, hbase.COLUMNFAMILY_1_AUTHOR, "sxt");
		//hbase.addOneRecord("stu","key1","cf","age","24");
		//查询一条记录
        Doc doc = hbase.getDoc("doc", "1");
        System.out.println(doc.getId());

		//获取表的所有数据
        hbase.getALLData("doc");
		//删除一条记录
		hbase.deleteOneRecord("stu","key1");
		//删除表
		hbase.deleteTable("stu");
        //scan过滤器的使用
		hbase.getScanData("stu","cf","age");
        //rowFilter的使用
        //84138413_20130313145955
        hbase.getRowFilter("waln_log","^*_201303131400\\d*$");
    }

    /**
     * rowFilter的使用
     *
     * @param tableName
     * @param reg
     * @throws Exception
     */
    public void getRowFilter(String tableName, String reg) throws Exception {
        Table table = conn.getTable(TableName.valueOf(tableName));
        Scan scan = new Scan();
//		Filter
        RowFilter rowFilter = new RowFilter(CompareOp.NOT_EQUAL, new RegexStringComparator(reg));
        scan.setFilter(rowFilter);
        ResultScanner scanner = table.getScanner(scan);
        for (Result result : scanner) {
            System.out.println(new String(result.getRow()));
        }
    }

    public void getScanData(String tableName, String family, String qualifier) throws Exception {
        Table table = conn.getTable(TableName.valueOf(tableName));
        Scan scan = new Scan();
        scan.addColumn(family.getBytes(), qualifier.getBytes());
        ResultScanner scanner = table.getScanner(scan);
        for (Result result : scanner) {
            if (result.listCells().size() == 0) {
                LOG.info(tableName + " 表数据为空！");
            } else {
                for (Cell cell : result.listCells()) {
                    LOG.info(Bytes.toString(CellUtil.cloneRow(cell)) +
                            "\t" + Bytes.toString(CellUtil.cloneValue(cell)));
                }
            }
        }
    }

    private void deleteTable(String tableName) {
        try {
            if (admin.tableExists(TableName.valueOf(tableName))) {
                admin.disableTable(TableName.valueOf(tableName));
                admin.deleteTable(TableName.valueOf(tableName));
                LOG.info(tableName + "表删除成功！");
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println(tableName + "表删除失败！");
        }

    }

    /**
     * 删除一条记录
     *
     * @param tableName
     * @param rowKey
     */
    public void deleteOneRecord(String tableName, String rowKey) throws IOException {
        Table table = conn.getTable(TableName.valueOf(tableName));
        Delete delete = new Delete(rowKey.getBytes());
        try {
            table.delete(delete);
            LOG.info(rowKey + "记录删除成功！");
        } catch (IOException e) {
            e.printStackTrace();
            LOG.info(rowKey + "记录删除失败！");
        }
    }

    /**
     * 获取表的所有数据
     *
     * @param tableName
     */
    public void getALLData(String tableName) {
        try {
            Table table = conn.getTable(TableName.valueOf(tableName));
            Scan scan = new Scan();
            ResultScanner scanner = table.getScanner(scan);
            for (Result result : scanner) {
                if (result.rawCells().length == 0) {
                    System.out.println(tableName + " 表数据为空！");
                } else {
                    for (Cell cell : result.rawCells()) {
                        LOG.info(Bytes.toString(CellUtil.cloneRow(cell)) +
                                "\t" + Bytes.toString(CellUtil.cloneValue(cell)));
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    // 读取一条记录
    @SuppressWarnings({"deprecation", "resource"})
    public Doc getDoc(String tableName, String rowkey) throws IOException {
        Table table = conn.getTable(TableName.valueOf(tableName));
        Get get = new Get(rowkey.getBytes());
        Doc doc = null;
        try {
            Result result = table.get(get);
            List<Cell> cells = result.listCells();
            if(cells == null || cells.size() == 0) return null;
            doc = new Doc();
            for (Cell cell : cells) {
                String qualifierStr = new String(cell.getQualifierArray(), cell.getQualifierOffset(), cell.getQualifierLength());
                String value = new String(cell.getValueArray(), cell.getValueOffset(), cell.getValueLength());

                doc.set(qualifierStr, value);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return doc;
    }


    // 添加一条记录
    public void put(String tableName, String rowkey, String columnFamily, String column, Object data) throws IOException {
        Table table = conn.getTable(TableName.valueOf(tableName));

        byte[] dataByte;
        Put p1 = new Put(Bytes.toBytes(rowkey));
        if (data instanceof Integer) {
            int iv = (Integer) data;
            dataByte = Bytes.toBytes(iv);
        } else if (data instanceof String) {
            String sv = (String) data;
            dataByte = sv.getBytes();
        } else if (data instanceof Double) {
            double dv = (Double) data;
            dataByte = Bytes.toBytes(dv);
        } else if (data instanceof Float) {
            float fv = (Float) data;
            dataByte = Bytes.toBytes(fv);
        } else if (data instanceof Long) {
            long lv = (Long) data;
            dataByte = Bytes.toBytes(lv);
        } else if (data instanceof Boolean) {
            boolean bv = (Boolean) data;
            dataByte = Bytes.toBytes(bv);
        } else if (data instanceof Date) {
            Date dv = (Date) data;
            dataByte = dv.toString().getBytes();
        }else {
            throw new RuntimeException("传入Hbase数据的数据类型异常");
        }
        p1.addColumn(columnFamily.getBytes(), column.getBytes(), dataByte);
        table.put(p1);
    }

    @Before
    public void setup() throws IOException {
        Configuration config = HBaseConfiguration.create();
        config.set("hbase.zookeeper.quorum", "node1,node2,node3");
        conn = ConnectionFactory.createConnection(config);
    }

    @Test
    public void insert() throws IOException {
        Table ta = conn.getTable(TableName.valueOf("doc"));
        Put put = new Put(("1234").getBytes());
        put.addColumn("cf1".getBytes(), "data1".getBytes(), "abcdf111".getBytes());
        ta.put(put);
    }

    /**
     * 查询所有表名
     *
     * @return
     * @throws Exception
     */
    public List<String> getALLTable() throws Exception {
        List<String> tables = new ArrayList<String>();
        if (admin != null) {
            HTableDescriptor[] listTables = admin.listTables();
            if (listTables.length > 0) {
                for (HTableDescriptor tableDesc : listTables) {
                    tables.add(tableDesc.getNameAsString());
                    System.out.println(tableDesc.getNameAsString());
                }
            }
        }
        return tables;
    }

    /**
     * 创建一张表
     *
     * @param tableName
     * @param column
     * @throws Exception
     */
    public void createTable(String tableName, String column) throws Exception {
        if (admin.tableExists(TableName.valueOf(tableName))) {
            System.out.println(tableName + "表已经存在！");
        } else {
            HTableDescriptor tableDesc = new HTableDescriptor(TableName.valueOf(tableName));
            tableDesc.addFamily(new HColumnDescriptor(column.getBytes()));
            admin.createTable(tableDesc);
            System.out.println(tableName + "表创建成功！");
        }
    }
}
