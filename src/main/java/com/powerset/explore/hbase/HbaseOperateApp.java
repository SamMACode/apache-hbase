package com.powerset.explore.hbase;

import com.powerset.explore.hbase.repository.HbaseRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

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
        repository.insertDataToHbaseServer();
        logger.info("complete HbaseOperateApp.run method, insert testdata to hbase server");
    }

}
