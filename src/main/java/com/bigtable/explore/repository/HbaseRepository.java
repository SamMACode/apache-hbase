package com.bigtable.explore.repository;

import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.NavigableMap;

/**
 * @author Sam Ma
 * @date 2020-4-14
 * apache hbase的数据操作类
 */
@Repository
public class HbaseRepository {

    private static Logger logger = LoggerFactory.getLogger(HbaseRepository.class);

    @Autowired
    private HBaseAdmin admin;

    /**
     * 通过hbase-client将数据写入到hbase server中
     */
    public void insertDataToHbaseServer() throws Exception {
        /**
         * 通过hbase admin客户端创建数据表"custom-table"
         */
        HTableDescriptor tableDescriptor = new HTableDescriptor(TableName.valueOf("custom-table"));
        tableDescriptor.addFamily(new HColumnDescriptor("colfam1"));
        tableDescriptor.addFamily(new HColumnDescriptor("colfam2"));
        tableDescriptor.addFamily(new HColumnDescriptor("colfam3"));
        admin.createTable(tableDescriptor);
        boolean tableAvailable = false;  /* warning: admin.isTableAvailable("custom-table");*/
        logger.info(" whether custom-table is Available: [{}] ", tableAvailable);

        HTable customTable = null; /* warning: new HTable(hbaseConfigure, "custom-table"); */
        Put putRow = new Put(Bytes.toBytes("row1"));
        // 将数据添加到hbase "custom-table"中, 并将列数据写入到hbase数据表中 (warning: hbase-client 1.0写法有问题)
        /*putRow.add(Bytes.toBytes("colfam1"), Bytes.toBytes("qual1"), Bytes.toBytes("value1"));
        putRow.add(Bytes.toBytes("colfam1"), Bytes.toBytes("qual2"), Bytes.toBytes("value2"));
        putRow.add(Bytes.toBytes("colfam2"), Bytes.toBytes("qual1"), Bytes.toBytes("value3"));*/

        // 将构造的列式数据写入到hbase中，操作完成后关闭连接对象
        customTable.put(putRow);
        /*warning: customTable.flushCommits(); */
        customTable.close();
    }

    /**
     * 获取hbase指定行中的数据列内容
     *
     * @param rowKey
     * @param family
     * @param column
     * @return
     */
    public String getHbaseRowValue(String rowKey, byte[] family, byte[] column) throws Exception {
        HTable customTable = null; /*warning: new HTable(hbaseConfigure, "custom-table");*/
        Get getRowKey = new Get(Bytes.toBytes(rowKey));
        getRowKey.setMaxVersions(3);
        getRowKey.addFamily(family);
        // 也可以设置获取给定列族family中 给定列的数据
        getRowKey.addColumn(family, column);

        Result hbaseResult = customTable.get(getRowKey);
        logger.info("hbase query result: [{}]", Bytes.toString(hbaseResult.getRow()));
        // 获取返回结果中指定的value值, 通过指定family和column信息
        String specifiedValue = Bytes.toString(hbaseResult.getValue(family, column));
        logger.info("hbase specified latest value {}:{} is: [{}]", Bytes.toString(family), Bytes.toString(column),
                specifiedValue);

        // 循环迭代获取hbase查询结果中所有的返回信息
        NavigableMap<byte[], NavigableMap<byte[], NavigableMap<Long, byte[]>>> navigableMap = hbaseResult.getMap();
        for (Map.Entry<byte[], NavigableMap<byte[], NavigableMap<Long, byte[]>>> navigableMapEntry : navigableMap.entrySet()) {
            String familyKey = Bytes.toString(navigableMapEntry.getKey());
            logger.info("\t familyKey: [{}]", familyKey);
            NavigableMap<byte[], NavigableMap<Long, byte[]>> familyContents = navigableMapEntry.getValue();
            // 对family列族中的内容进行迭代遍历, 打印出列族中key以及value信息
            for (Map.Entry<byte[], NavigableMap<Long, byte[]>> mapEntry : familyContents.entrySet()) {
                String qualifier = Bytes.toString(mapEntry.getKey());
                logger.info("\t\t qualifier: [{}]", qualifier);
                NavigableMap<Long, byte[]> qualifierContents = mapEntry.getValue();
                for (Map.Entry<Long, byte[]> entry : qualifierContents.entrySet()) {
                    Long timestamp = entry.getKey();
                    String value = Bytes.toString(entry.getValue());
                    logger.info("\t\t\t [{}] [{}]", value, timestamp);
                }
            }
        }

        customTable.close();
        return Bytes.toString(hbaseResult.getRow());
    }

}
