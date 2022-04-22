/***********************************************
 * ConsumerThreadPool.java
 ***********************************************/
package com.simpson.kafka2cep.kafka;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.espertech.esper.client.EPServiceProvider;
import com.simpson.kafka2cep.config.Config;

import kafka.consumer.ConsumerConfig;
import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;

public class ConsumerThreadPool
{
    private final ConsumerConnector mConsumer;
    private final HashMap<String, KafkaTopicInfo> mTopic;


    private EPServiceProvider mEpService;
    private int               mOpThreadNum;
    private String            mEventClsName;
    private String            mNewDataSeperator;
    private String            mDataType;    //private

    HashMap<String, ConsumerConnector> mConsumers;

    private ExecutorService []mExecutor;

    public ConsumerThreadPool(
              HashMap<String,KafkaTopicInfo> aTopic,
              EPServiceProvider aEpService,
              Integer           aThreadNum,
              String            aEventClsName,
              String            aDataType,
              String            aNewDataSeperator )
    {
        mConsumer         = kafka.consumer.
                                  Consumer.
                                  createJavaConsumerConnector(
                                  createConsumerConfig() );
        mTopic            = aTopic;
        mEpService        = aEpService;
        mOpThreadNum      = aThreadNum;
        mEventClsName     = aEventClsName;
        mDataType         = aDataType;
        mNewDataSeperator = aNewDataSeperator;
    }

    private static ConsumerConfig createConsumerConfig()
    {
        Properties props = new Properties();
        props.put("zookeeper.connect", Config.getKafkaZooList() );
        props.put("group.id",          Config.getKafkaGroupID() );
        props.put("zookeeper.session.timeout.ms", "400"  );
        props.put("zookeeper.sync.time.ms",       "200"  );
        props.put("auto.commit.interval.ms",      "1000" );
        props.put("auto.offset.reset", Config.getKafkaOffsetReset() );
        return new ConsumerConfig(props);
    }

    public void shutdown()
    {
        if( mConsumer != null )
        {
            mConsumer.shutdown();
        }

        for( ExecutorService sExecutor:mExecutor )
        {
            if( sExecutor != null )
            {
                sExecutor.shutdown();
            }
        }
    }

    public void run( )
    {
        Map<String, Integer> sTopicCountMap
                               = new HashMap<String,Integer>();
        int sThreadNum   = 0;
        int sCnt         = 0;
        String sTopic    = "";
        int    sTopicNum = mTopic.size();

        KafkaTopicInfo sTopicInfo;

        mExecutor =  new ExecutorService[sTopicNum];

        Map<String, List<KafkaStream<byte[], byte[]>>> sConsumerMap =
                mConsumer.createMessageStreams( sTopicCountMap );
        for( Map.Entry<String,KafkaTopicInfo> sEntry:mTopic.entrySet() )
        {
            List<KafkaStream<byte[], byte[]>> sTopicListeners =
                               sConsumerMap.get( sTopic );

            sTopic     = sEntry.getKey();
            sTopicInfo = sEntry.getValue();

            System.out.println( "Topic=" + sTopic +
                                ",Calss Name=" + sTopicInfo.getClassName());

            sTopicCountMap.put( sTopic, mOpThreadNum );

            mExecutor[sCnt] = Executors.newFixedThreadPool( mOpThreadNum );

            for( final KafkaStream stream: sTopicListeners )
            {
                System.out.println( "Will be start " +
                                    sThreadNum +
                                    "'s thread." );
                mExecutor[sCnt].submit( new Consumer(
                                               stream,
                                               mEpService,
                                               sThreadNum++,
                                               mEventClsName,
                                               mDataType,
                                               mNewDataSeperator,
                                               sTopic,
                                               sTopicInfo.getClassName() ) );
            }
        }
    }
}
