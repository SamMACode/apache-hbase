package com.powerset.explore.hbase.conf;

import org.apache.hadoop.hbase.HBaseConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Sam Ma
 * @date 2020-4-13
 * apache hbase的核心配置类
 */
@Configuration
public class HbaseConfig {

    private static final Logger logger = LoggerFactory.getLogger(HbaseConfig.class);

    /**
     * zookeeper的host地址以及port端口号
     */
    @Value("${apache.zookeeper.host}")
    private String zookeeperHost;

    @Value("${apache.zookeeper.port}")
    private String zookeeperPort;

    /**
     * 配置apache habse连接的master地址
     */
    @Value("${apache.hbase.address}")
    private String hbaseAddress;

    /**
     * 通过zookeeper信息配置hbase连接
     * @return
     */
    @Bean
    public org.apache.hadoop.conf.Configuration getHbaseConfiguration() {
        org.apache.hadoop.conf.Configuration config = HBaseConfiguration.create();
        // 在hbase configuration中配置zookeeper地址、端口号信息（目前从conf/hbase-site.xml的文件读取）
        /*config.set("hbase.zookeeper.quorum", "hadoop1,hadoop2,hadoop3");
        config.set("hbase.zookeeper.property.clientPort", "2181");
        config.set("zookeeper.znode.parent", "/hbase1");*/

        logger.info("connect to apache hbase server, config: [{}] zkhost: [{}] zkport: [{}] ", config, zookeeperHost,
                zookeeperPort);
        return config;
    }

}
