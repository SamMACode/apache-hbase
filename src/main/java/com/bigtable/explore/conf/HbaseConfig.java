package com.bigtable.explore.conf;

import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.io.IOException;

/**
 * @author Sam Ma
 * @date 2020-4-13
 * apache hbase的核心配置类
 */
@Configuration
public class HbaseConfig {

    private static final Logger logger = LoggerFactory.getLogger(HbaseConfig.class);

    private static final String ZK_CONNECT_KEY = "hbase.zookeeper.quorum";

    /**
     * zookeeper的host地址以及port端口号
     */
    @Value("${apache.zookeeper.address}")
    private String zkAddress;

    private HBaseAdmin admin;
    private Connection connection;

    /**
     * 通过zookeeper信息配置hbase连接
     *
     * @return
     */
    @PostConstruct
    public org.apache.hadoop.conf.Configuration getHbaseConfiguration() {
        org.apache.hadoop.conf.Configuration config = HBaseConfiguration.create();
        // 在hbase configuration中配置zookeeper地址、端口号信息（hbase-client 1.0从conf/hbase-site.xml的文件读取）
        /*config.set("hbase.zookeeper.quorum", "hadoop1,hadoop2,hadoop3");
        config.set("hbase.zookeeper.property.clientPort", "2181");
        config.set("zookeeper.znode.parent", "/hbase1");*/

        // hbase-client 2.0写法，直接使用zookeeper集群地址就可以创建Connection对象
        config.set(ZK_CONNECT_KEY, zkAddress);
        try {
            connection = ConnectionFactory.createConnection(config);
            admin = (HBaseAdmin) connection.getAdmin();
        } catch (IOException ex) {
            logger.error("use zk cluster address [{}] to create Connection cause exception", zkAddress, ex);
        }
        logger.info("connect to apache hbase server, zk address: [{}]", zkAddress);
        return config;
    }

    @Bean
    public HBaseAdmin hBaseAdmin() {
        return this.admin;
    }

    @Bean
    public Connection hBaseConnection() {
        return this.connection;
    }

}
