/***********************************************
 * ConsumerThreadPool.java
 ***********************************************/
package com.kafka2esper.kafka;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Properties;
import java.io.Reader;
import java.io.StringReader;

import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;
import kafka.consumer.ConsumerConfig;
import kafka.javaapi.consumer.ConsumerConnector;

import com.espertech.esper.client.Configuration;
import com.espertech.esper.client.EPServiceProvider;
import com.espertech.esper.client.EPServiceProviderManager;
import com.espertech.esper.client.EPStatement;

import java.util.Map;
import java.util.List;
import java.util.HashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;

public class ConsumerThreadPool
{
    private final ConsumerConnector mConsumer;
    private final HashMap<String, aliceKafkaTopicInfo> mTopic;


    private EPServiceProvider mEpService;
    private int               mOpThreadNum; 
    private String            mEventClsName;
    private String            mNewDataSeperator; 
    private String            mDataType;    //private
    
    HashMap<String, ConsumerConnector> mConsumers;

    private ExecutorService []mExecutor;

    public ConsumerThreadPool(
              HashMap<String,aliceKafkaTopicInfo> aTopic,
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
        props.put("zookeeper.connect", aliceConfig.getKafkaZooList() );
        props.put("group.id",          aliceConfig.getKafkaGroupID() );
        props.put("zookeeper.session.timeout.ms", "400"  );
        props.put("zookeeper.sync.time.ms",       "200"  );
        props.put("auto.commit.interval.ms",      "1000" );
        props.put("auto.offset.reset", aliceConfig.getKafkaOffsetReset() );
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
        String sClsName  = "";
        int    sTopicNum = mTopic.size();

        aliceKafkaTopicInfo sTopicInfo;

        mExecutor =  new ExecutorService[sTopicNum];

        Map<String, List<KafkaStream<byte[], byte[]>>> sConsumerMap =
                mConsumer.createMessageStreams( sTopicCountMap );
        for( Map.Entry<String,aliceKafkaTopicInfo> sEntry:mTopic.entrySet() )
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
                mExecutor[sCnt].submit( new aliceConsumer(
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
