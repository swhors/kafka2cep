package com.simpson.kafka2cep.config;

public interface KafkaProperties
{
    final static String zkXonnect       = "127.0.0.1:2181";
    final static String groupId         = "console-consumer-90636";
    final static String topic           = "topicCDR";
    final static String kakaServerURL   = "localhost";
    final static int    kafkaServerPort = 9092;

    final static int    kafkaProducerBufferSize =64*1024;
    final static int    connectionTimeOut       = 100000;
    final static int    reconnectInterval       = 10000;
    final static String topic2   = "topicDEBUG";
    final static String topic3   = "topicTRACE";
    final static String clientId = "ccpCepRunner";
}
