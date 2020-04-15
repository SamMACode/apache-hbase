package com.powerset.explore.hbase;

import com.alibaba.fastjson.JSONObject;
import com.powerset.explore.hbase.repository.HbaseRepository;
import com.powerset.explore.hbase.service.BulkImportFakenamesData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;

/**
 * @author Sam Ma
 * @date 2020-4-13
 * 通过Apache Hbase从csv中导入数据的应用启动类
 */
@SpringBootApplication
public class HbaseOperateApp implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(HbaseOperateApp.class);

    @Autowired
    private HbaseRepository repository;

    @Autowired
    private BulkImportFakenamesData bulkImportData;

    public static void main(String[] args) {
        SpringApplication.run(HbaseOperateApp.class, args);
    }

    /**
     * 可以从java -jar hbase-sample-0.0.1.jar中获取传递的参数信息
     * @param args
     * @throws Exception
     */
    @Override
    public void run(String... args) throws Exception {
        // 1.通过HbaseAdmin创建数据表，同时向数据表中初始化一部分数据
        // repository.insertDataToHbaseServer();

        // 2.通过指定family和column信息查询HbaseAdmin表中的数据
        /*byte[] familyKey = Bytes.toBytes("colfam1");
        byte[] columnKey = Bytes.toBytes("qual1");
        String queryValue = repository.getHbaseRowValue("row1", familyKey, columnKey);
        logger.info("query column value in specified hbase table: [{}]", queryValue);*/

        // 3.从命令行中接收 fakenames-sample-1000.csv文件的路径，并将其写入到hbase数据表中
        logger.info("command line args: {}", JSONObject.toJSONString(args));
        if (args.length == 0) {
            logger.warn("required fakenames-sample-1000.csv file path in arguments {}", JSONObject.toJSONString(args));
            System.exit(0);
        }
        // 对从jar命令中输入的文件路径进行校验，java -jar hbase-sample-0.0.1.jar fakenames-sample-1000.csv
        File csvFilePath = new File(args[0]);
        if (!csvFilePath.exists()) {
            logger.warn("csv filepath [{}] doesn't exist", args[0]);
            System.exit(0);
        }
        bulkImportData.bulkImportCsvData(args[0]);

        logger.info("complete HbaseOperateApp.run method, insert testdata to hbase server");
    }

}
