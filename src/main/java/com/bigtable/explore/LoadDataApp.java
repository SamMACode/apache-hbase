package com.bigtable.explore;

import org.apache.hadoop.hbase.NamespaceDescriptor;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.io.encoding.DataBlockEncoding;
import org.apache.hadoop.hbase.regionserver.BloomType;
import org.apache.hadoop.hbase.util.Bytes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Sam Ma
 * @date 2021-08-01
 * 通过hBase client创建数据表, 并初始化部分数据
 */
@SpringBootApplication
public class LoadDataApp implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(LoadDataApp.class);

    @Autowired
    private HBaseAdmin hBaseAdmin;

    @Autowired
    private Connection connection;

    public static void main(String[] args) {
        SpringApplication.run(LoadDataApp.class, args);
    }

    /**
     * 可以从java -jar hbase-sample-0.0.1.jar中获取传递的参数信息
     *
     * @param args
     * @throws Exception
     */
    @Override
    public void run(String... args) throws Exception {
        String tableName = "madong_test:student";
        String[] familyNames = {"info", "score"};
        // 有抛出error NamespaceNotFoundException: madong_test，先创建namespace
        hBaseAdmin.createNamespace(NamespaceDescriptor.create("madong_test").build());

        logger.info("start create table [{}] on hbase and initial some data", tableName);
        createTable(tableName, familyNames);
        // 向刚创建的"madong_test:student"表中写入数据
        Table stuTable = connection.getTable(TableName.valueOf(tableName));
        List<Put> rows = new ArrayList<>();
        Put put = new Put(Bytes.toBytes("Sam Ma"));
        put.addColumn(Bytes.toBytes("info"), Bytes.toBytes("student_id"), Bytes.toBytes("G20210735010369"));
        put.addColumn(Bytes.toBytes("info"), Bytes.toBytes("class"), Bytes.toBytes("5"));
        put.addColumn(Bytes.toBytes("score"), Bytes.toBytes("understanding"), Bytes.toBytes("79"));
        put.addColumn(Bytes.toBytes("score"), Bytes.toBytes("programming"), Bytes.toBytes("87"));
        stuTable.put(rows);
        logger.info("insert data to hbase table [{}] successfully", tableName);
        hBaseAdmin.close();
        connection.close();
    }

    /**
     * 在hbase中创建数据表，用TableDescriptor和ColumnFamilyDescriptor创建描述信息
     *
     * @param table    表名称
     * @param families 列族
     */
    private void createTable(String table, String[] families) throws IOException {
        TableName stuTable = TableName.valueOf(table);
        if (hBaseAdmin.tableExists(stuTable)) {
            logger.warn("table [{}] already exists, i will list all tables: ", table);
            TableName[] existsTables = hBaseAdmin.listTableNames();
            Arrays.stream(existsTables).forEach(tableName -> logger.warn("{}", tableName));
        } else {
            // 依据列族名称、表名称创建Descriptor对象，使用HbaseAdmin执行创建
            List<ColumnFamilyDescriptor> descriptors = new ArrayList<>();
            for (String family : families) {
                ColumnFamilyDescriptor descriptor = ColumnFamilyDescriptorBuilder.newBuilder(Bytes.toBytes(family))
                        .setDataBlockEncoding(DataBlockEncoding.PREFIX)
                        .setBloomFilterType(BloomType.ROW)
                        .build();
                descriptors.add(descriptor);
            }
            TableDescriptor tableDescriptor = TableDescriptorBuilder.newBuilder(stuTable)
                    .setColumnFamilies(descriptors)
                    .build();
            hBaseAdmin.createTable(tableDescriptor);
            if (hBaseAdmin.tableExists(stuTable)) {
                logger.info("success create table [{}] with column families: [{}]", table, Arrays.asList(families));
            } else {
                logger.error("fail to create table [{}]", table);
            }
        }
    }

}
