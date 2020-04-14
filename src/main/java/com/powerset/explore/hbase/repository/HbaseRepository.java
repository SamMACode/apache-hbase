package com.powerset.explore.hbase.repository;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.util.Bytes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * @author Sam Ma
 * @date 2020-4-14
 * apache hbase的数据操作类
 */
@Repository
public class HbaseRepository {

    private static Logger logger = LoggerFactory.getLogger(HbaseRepository.class);

    @Autowired
    private Configuration hbaseConfigure;

    /**
     * 通过hbase-client将数据写入到hbase server中
     */
    public void insertDataToHbaseServer() throws Exception {
        /**
         * 通过hbase admin客户端创建数据表"custom-table"
         */
        HBaseAdmin admin = new HBaseAdmin(hbaseConfigure);
        HTableDescriptor tableDescriptor = new HTableDescriptor(TableName.valueOf("custom-table"));
        tableDescriptor.addFamily(new HColumnDescriptor("colfam1"));
        tableDescriptor.addFamily(new HColumnDescriptor("colfam2"));
        tableDescriptor.addFamily(new HColumnDescriptor("colfam3"));
        admin.createTable(tableDescriptor);
        boolean tableAvailable = admin.isTableAvailable("custom-table");
        logger.info(" whether custom-table is Available: [{}] ", tableAvailable);

        HTable customTable = new HTable(hbaseConfigure, "custom-table");
        Put putRow = new Put(Bytes.toBytes("row1"));
        // 将数据添加到hbase "custom-table"中, 并将列数据写入到hbase数据表中
        putRow.add(Bytes.toBytes("colfam1"), Bytes.toBytes("qual1"), Bytes.toBytes("value1"));
        putRow.add(Bytes.toBytes("colfam1"), Bytes.toBytes("qual2"), Bytes.toBytes("value2"));
        putRow.add(Bytes.toBytes("colfam2"), Bytes.toBytes("qual1"), Bytes.toBytes("value3"));

        // 将构造的列式数据写入到hbase中，操作完成后关闭连接对象
        customTable.put(putRow);
        customTable.flushCommits();
        customTable.close();
    }

}
