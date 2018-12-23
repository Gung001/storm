package com.lxgy.storm.bigdata.integration.kafka;

import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.hbase.bolt.HBaseBolt;
import org.apache.storm.hbase.bolt.mapper.SimpleHBaseMapper;
import org.apache.storm.jdbc.bolt.JdbcInsertBolt;
import org.apache.storm.jdbc.common.ConnectionProvider;
import org.apache.storm.jdbc.common.HikariCPConnectionProvider;
import org.apache.storm.jdbc.mapper.JdbcMapper;
import org.apache.storm.jdbc.mapper.SimpleJdbcMapper;
import org.apache.storm.kafka.BrokerHosts;
import org.apache.storm.kafka.KafkaSpout;
import org.apache.storm.kafka.SpoutConfig;
import org.apache.storm.kafka.ZkHosts;
import org.apache.storm.shade.com.google.common.collect.Maps;
import org.apache.storm.topology.TopologyBuilder;
import org.apache.storm.tuple.Fields;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Kafka整合Storm测试
 * @author Gryant
 */
public class StormKafkaTopo {

    public static void main(String[] args) {

        TopologyBuilder builder = new TopologyBuilder();

        // Kafka使用的zk地址
        BrokerHosts hosts = new ZkHosts("bd71:2181");

        // Kafka存储数据的topic名称
        String topic = "stormTopic";

        // 指定ZK中的一个根目录，存储的是KafkaSpout读取数据的位置信息(offset)
        String zkRoot = "/" + topic;
        String id = UUID.randomUUID().toString();
        SpoutConfig spoutConfig = new SpoutConfig(hosts, topic, zkRoot, id);

        // 设置读取偏移量的操作
        spoutConfig.startOffsetTime = kafka.api.OffsetRequest.LatestTime();

        KafkaSpout kafkaSpout = new KafkaSpout(spoutConfig);


        String SPOUT_ID = KafkaSpout.class.getSimpleName();
        builder.setSpout(SPOUT_ID, kafkaSpout);

        String BOLD_ID = LogProcessBolt.class.getSimpleName();
        builder.setBolt(BOLD_ID, new LogProcessBolt()).shuffleGrouping(SPOUT_ID);

        Config config = new Config();

        Map<String, Object> hbaseConf = new HashMap<String, Object>();
        hbaseConf.put("hbase.rootdir","hdfs://bd71:8020/hbase");
        hbaseConf.put("hbase.zookeeper.quorum", "bd71:2181");
        config.put("hbase.conf",hbaseConf);

        SimpleHBaseMapper mapper = new SimpleHBaseMapper()
                .withRowKeyField("rowKey")
                .withColumnFields(new Fields("latitude", "longitude"))
                .withCounterFields(new Fields("time"))
                .withColumnFamily("info");

        HBaseBolt hbaseBolt = new HBaseBolt("stat", mapper)
                .withConfigKey("hbase.conf");
        builder.setBolt("HBaseBolt", hbaseBolt).shuffleGrouping(BOLD_ID);

        /*
        // java.sql.SQLException: null,  message from server: "Host '192.168.30.1' is not allowed to connect to this MySQL server"

        Map hikariConfigMap = Maps.newHashMap();
        hikariConfigMap.put("dataSourceClassName","com.mysql.jdbc.jdbc2.optional.MysqlDataSource");
        hikariConfigMap.put("dataSource.url", "jdbc:mysql://192.168.30.71/storm");
        hikariConfigMap.put("dataSource.user","root");
        hikariConfigMap.put("dataSource.password","191010");
        ConnectionProvider connectionProvider = new HikariCPConnectionProvider(hikariConfigMap);

        String tableName = "stat";
        JdbcMapper simpleJdbcMapper = new SimpleJdbcMapper(tableName, connectionProvider);

        JdbcInsertBolt userPersistanceBolt = new JdbcInsertBolt(connectionProvider, simpleJdbcMapper)
                .withTableName(tableName)
                .withQueryTimeoutSecs(30);

        builder.setBolt("JdbcInsertBolt", userPersistanceBolt).shuffleGrouping(BOLD_ID);
        */

        LocalCluster cluster = new LocalCluster();
        cluster.submitTopology(StormKafkaTopo.class.getSimpleName(),
                config,
                builder.createTopology());

    }
}
