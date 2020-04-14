# hbase-stranger-example
apache hbase bulk import data from csv files and include basic operate example

> `apache hadoop`能够在合理的时间范围内处理`PB`级的数据，在研读`hadoop`的过程中发现了一个处理随机读写的系统，它叫做`apache hbase`。或者将其称为目前流行的一种新的数据存储架构，传统数据库解决大数据问题时成本更高，`hbase`利用了`hdfs`存储海量数据能力（不用考虑集群扩容及分库、分表内容），并提供像传统`RDBMS`查询操作。

`Google Big Table`论文为`apache hbase`设计的理论依据：https://static.googleusercontent.com/media/research.google.com/zh-CN//archive/bigtable-osdi06.pdf

安装依赖：`hbase`底层使用`hdfs`作为数据存储，使用`zookeeper`进行集群消息注册信息

`apache hadoop`安装指南：https://www.jianshu.com/p/4c81a1e32161

`apache hbase`安装指南：https://www.guru99.com/hbase-installation-guide.html

