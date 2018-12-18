package com.lxgy.storm.bigdata.integration.kafka;

import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichBolt;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.tuple.Values;

import java.util.Map;

/**
 * 接收Kafka的数据进行处理的BOLT
 * @author Gryant
 */
public class LogProcessBolt extends BaseRichBolt {

    private OutputCollector collector;

    @Override
    public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {
        this.collector = collector;
    }

    @Override
    public void execute(Tuple input) {
        try {
            byte[] binaryByField = input.getBinaryByField("bytes");
            String value = new String(binaryByField);
            System.err.println("input data:" + value);

            Thread.sleep(1000);
//            /**
//             * 13977777777	116.399466,39.989743	[2018-01-14 11:22:34]
//             *
//             * 解析出来日志信息
//             */
//
//            String[] splits = value.split("\t");
//            String phone = splits[0];
//            String[] temp = splits[1].split(",");
//            String longitude = temp[0];
//            String latitude = temp[1];
//            long time = DateUtils.getInstance().getTime(splits[2]);
//
//            System.out.println(phone + "," + longitude + "," + latitude + "," + time);
//
//            collector.emit(new Values(time, Double.parseDouble(longitude), Double.parseDouble(latitude)));

            this.collector.ack(input);
        } catch (Exception e) {
            this.collector.fail(input);
        }
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        // declarer.declare(new Fields("time", "longitude", "latitude"));
    }
}
