# hbase-stranger-example
apache hbase bulk import data from csv files and include basic operate example

> `apache hadoop`能够在合理的时间范围内处理`PB`级的数据，在研读`hadoop`的过程中发现了一个处理随机读写的系统，它叫做`apache hbase`。或者将其称为目前流行的一种新的数据存储架构，传统数据库解决大数据问题时成本更高，`hbase`利用了`hdfs`存储海量数据能力（不用考虑集群扩容及分库、分表内容），并提供像传统`RDBMS`查询操作。

`Google Big Table`论文为`Apache Hbase`设计提供了理论依据：https://static.googleusercontent.com/media/research.google.com/zh-CN//archive/bigtable-osdi06.pdf

安装依赖：`hbase`底层使用`hdfs`作为数据存储，使用`zookeeper`作为注册中心进行集群服务注册

`apache hadoop`安装指南：https://www.jianshu.com/p/4c81a1e32161

`apache hbase`安装指南：https://www.guru99.com/hbase-installation-guide.html



### 1. 应用本地部署流程：

1）将仓库代码克隆到本地 `git clone https://github.com/SamMACode/hbase-stranger-example.git `；

2）修改`conf`目录下的`hbase-site.xml`文件中的`hbase.zookeeper.quorum`属性、`zookeeper.znode.parent`属性为所连接`hbase`服务配置（需修改本地`host`文件）；

```xml
<property>
    <name>hbase.zookeeper.quorum</name>
    <value>hadoop1,hadoop2,hadoop3</value>
</property>
<property>
    <name>zookeeper.znode.parent</name>
    <value>/hbase1</value>
</property>
```

`warning`: 需要特别注意，之前在本地开发时设置`zookeeper`地址为具体`ip`，导致了一直连不上`hbase`服务器。其原因是`hbase client`客户端在一直进行连接重试，等到超时后会进行报错（排查此问题花费了很长时间）。

3）进入`hbase`数据库服务器并创建`fakenames`数据表：

```shell
hbase(main):002:0> create 'fakenames', 'personal', 'contactinfo', 'creditcard'
0 row(s) in 2.4060 seconds
=> Hbase::Table - fakenames
```

4）进入`pom.xml`同级文件目录构建项目，并将`data`文件夹下的`fakenames-sample-1000.csv`单独放在同一目录中，随后将`csv`文件中的数据进行导入：

```shell
sam@sam-virtual-machine:~/repositories$ java -jar hbase-sample-0.0.1.jar fakenames-sample-1000.csv
  .   ____          _            __ _ _
 /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
 \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
  '  |____| .__|_| |_|_| |_\__, | / / / /
 =========|_|==============|___/=/_/_/_/
 :: Spring Boot ::       (v2.1.11.RELEASE)

2020-04-15 15:22:48.361  INFO 37109 --- [           main] c.p.explore.hbase.HbaseOperateApp        : Starting HbaseOperateApp v0.0.1 on sam-virtual-machine with PID 37109 (/home/sam/repositories/hbase-sample-0.0.1.jar started by sam in /home/sam/repositories)
2020-04-15 15:22:48.367  INFO 37109 --- [           main] c.p.explore.hbase.HbaseOperateApp        : No active profile set, falling back to default profiles: default
2020-04-15 15:22:49.363  INFO 37109 --- [           main] c.p.explore.hbase.conf.HbaseConfig       : connect to apache hbase server, config: [Configuration: core-default.xml, core-site.xml, hbase-default.xml, hbase-site.xml] zkhost: [192.168.xxx.xxx] zkport: [2181] 
2020-04-15 15:22:49.519  INFO 37109 --- [           main] c.p.explore.hbase.HbaseOperateApp        : Started HbaseOperateApp in 1.681 seconds (JVM running for 2.067)
2020-04-15 15:22:49.648  INFO 37109 --- [           main] c.p.explore.hbase.HbaseOperateApp        : command line args: ["fakenames-sample-1000.csv"]
2020-04-15 15:22:50.341 DEBUG 37109 --- [           main] o.a.hadoop.hbase.ipc.AbstractRpcClient   : Codec=org.apache.hadoop.hbase.codec.KeyValueCodec@491cc5c9, compressor=null, tcpKeepAlive=true, tcpNoDelay=true, connectTO=10000, readTO=20000, writeTO=60000, minIdleTimeBeforeClose=120000, maxRetries=0, fallbackAllowed=false, bind address=null
2020-04-15 15:22:50.408  INFO 37109 --- [           main] c.p.e.h.service.BulkImportFakenamesData  : now at line [100] rowKey [elsass-june-g-100]
2020-04-15 15:22:50.458  INFO 37109 --- [           main] c.p.e.h.service.BulkImportFakenamesData  : now at line [200] rowKey [franz-michael-b-200]
2020-04-15 15:22:50.493  INFO 37109 --- [           main] c.p.e.h.service.BulkImportFakenamesData  : now at line [300] rowKey [pua-karl-m-300]
2020-04-15 15:22:50.508  INFO 37109 --- [           main] c.p.e.h.service.BulkImportFakenamesData  : now at line [400] rowKey [truax-robin-a-400]
2020-04-15 15:22:50.528  INFO 37109 --- [           main] c.p.e.h.service.BulkImportFakenamesData  : now at line [500] rowKey [cashin-karla-s-500]
2020-04-15 15:22:50.532  INFO 37109 --- [           main] c.p.e.h.service.BulkImportFakenamesData  : now at line [600] rowKey [woodson-benjamin-n-600]
2020-04-15 15:22:50.618 DEBUG 37109 --- [s-master1:2181)] o.a.h.h.s.o.apache.zookeeper.ClientCnxn  : Reading reply sessionid:0x100cf6bbe02003b, packet:: clientPath:null serverPath:null finished:false header:: 3,8  replyHeader:: 3,12884902281,0  request:: '/hbase1,F  response:: v{'replication,'meta-region-server,'rs,'splitWAL,'backup-masters,'table-lock,'flush-table-proc,'region-in-transition,'online-snapshot,'master,'running,'recovering-regions,'draining,'namespace,'hbaseid,'table} 
2020-04-15 15:22:51.229  INFO 37109 --- [           main] c.p.e.h.service.BulkImportFakenamesData  : now at line [700] rowKey [raglin-judy-k-700]
2020-04-15 15:22:51.271  INFO 37109 --- [           main] c.p.e.h.service.BulkImportFakenamesData  : now at line [800] rowKey [gilley-clifton-m-800]
2020-04-15 15:22:51.334  INFO 37109 --- [           main] c.p.e.h.service.BulkImportFakenamesData  : now at line [900] rowKey [baillie-lucy-b-900]
2020-04-15 15:22:51.440  INFO 37109 --- [           main] c.p.e.h.service.BulkImportFakenamesData  : now at line [1000] rowKey [cruz-charles-a-1000]
```

5）导入完成后可以通过`hbase shell`查看已经导入的数据：

```shell
[root@k8s-master1 ~]# hbase shell
SLF4J: Class path contains multiple SLF4J bindings.
SLF4J: Found binding in [jar:file:/root/hbase-1.1.2/lib/slf4j-log4j12-1.7.5.jar!/org/slf4j/impl/StaticLoggerBinder.class]
SLF4J: Found binding in [jar:file:/root/hadoop-2.7.1/share/hadoop/common/lib/slf4j-log4j12-1.7.10.jar!/org/slf4j/impl/StaticLoggerBinder.class]
SLF4J: See http://www.slf4j.org/codes.html#multiple_bindings for an explanation.
SLF4J: Actual binding is of type [org.slf4j.impl.Log4jLoggerFactory]
HBase Shell; enter 'help<RETURN>' for list of supported commands.
Type "exit<RETURN>" to leave the HBase Shell
Version 1.1.2, rcc2b70cf03e3378800661ec5cab11eb43fafe0fc, Wed Aug 26 20:11:27 PDT 2015

hbase(main):001:0> list
TABLE                                                                                     
ConfigurationManagementGraph                                                             
credio
custom-table                                                                  
fakenames                                                                                 
user                                                                                                                                                          
6 row(s) in 0.4680 seconds
=> ["ConfigurationManagementGraph", "credio", "custom-table", "fakenames", "user"]
hbase(main):002:0> scan 'fakenames'
ROW                                          COLUMN+CELL                                  acosta-kacy-p-343                           column=contactinfo:city, timestamp=1586935372171, value=Laurens
acosta-kacy-p-343                           column=contactinfo:country, timestamp=1586935372171, value=US
```

### 2. hbase shell常用命令：

* `hbase shell`：用于使用触发命令行交互脚本，客户可以通过此`shell`进行数据操作；

* `list`用于查看当前`hbase`数据库中数据表的列表；

* 创建数据表`user`，其包括`info`和`data`两个列族；

  ```shell
  hbase(main):010:0> create 'user', 'info', 'data'
  ```

* 向`user`表中插入信息，`row key`为`rk0001`，列族`info`中添加名为`name`的列，值为`zhangsan`

  ```shell
  hbase(main):011:0> put 'user', 'rk0001', 'info:name', 'zhangsan'
  ```

* 通过`rowkey`进行数据查询 `get`命令：

  ```shell
  # 获取user表中row key为rk0001的所有信息（即所有cell的数据）
  hbase(main):015:0> get 'user', 'rk0001'
  ```

* 通过`scan`命令查询数据表中所有数据信息：

  ```shell
  hbase(main):032:0>  scan 'user'
  ```

  