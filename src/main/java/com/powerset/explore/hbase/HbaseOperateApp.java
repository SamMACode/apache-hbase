package com.powerset.explore.hbase;

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

    }

}
