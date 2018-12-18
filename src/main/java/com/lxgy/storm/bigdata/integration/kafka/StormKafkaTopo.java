package com.lxgy.storm.bigdata.integration.kafka;

import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.kafka.BrokerHosts;
import org.apache.storm.kafka.KafkaSpout;
import org.apache.storm.kafka.SpoutConfig;
import org.apache.storm.kafka.ZkHosts;
import org.apache.storm.topology.TopologyBuilder;

import java.util.UUID;

/**
 * Kafka整合Storm测试
 * @author Gryant
 */
public class StormKafkaTopo {

    public static void main(String[] args) {

        TopologyBuilder builder = new TopologyBuilder();

        // Kafka使用的zk地址
        BrokerHosts hosts = new ZkHosts("data01:2181");

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

        LocalCluster cluster = new LocalCluster();
        cluster.submitTopology(StormKafkaTopo.class.getSimpleName(),
                new Config(),
                builder.createTopology());
    }
}
