package com.powerset.explore.hbase;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author Sam Ma
 * @date 2020-4-13
 * 通过Apache Hbase从csv中导入数据的应用启动类
 */
@SpringBootApplication
public class HbaseOperateApp {

    public static void main(String[] args) {
        SpringApplication.run(HbaseOperateApp.class, args);
    }

}
