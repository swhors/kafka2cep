/************************************************
 * aliceConsumer.java
 ************************************************/
package com.alice.cep;

import java.util.*;
import java.util.regex.*;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Properties;

import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;

import com.espertech.esper.client.Configuration;
import com.espertech.esper.client.EPServiceProvider;
import com.espertech.esper.client.EPServiceProviderManager;
import com.espertech.esper.client.EPStatement;

public class aliceConsumer implements Runnable
{
    final private int mDefCdrFieldItemNum = 25;
    final private int mDataType4Unknown   = 0;
    final private int mDataType4CSV       = 1;
    final private int mDataType4JSON      = 2;

    private KafkaStream       mStream;
    private Integer           mThreadNum;
    private EPServiceProvider mEpService;
    private String            mStreamEventName;
    private String            mNewDataSeperator;
    private int               mDataType;

    private String mTopicName;
    private String mClassName;

    public aliceConsumer( KafkaStream       aKafkaStream,
                          EPServiceProvider aEpService,
                          Integer           aThreadNum,
                          String            aStreamEventName,
                          String            aDataType,
                          String            aNewDataSeperator,
                          String            aTopicName,
                          String            aClassName )
    {
        mThreadNum        = aThreadNum;
        mStreamEventName  = aStreamEventName;
        mDataType         = Integer.parseInt(aDataType);
        mNewDataSeperator = aNewDataSeperator;       
        mTopicName        = aTopicName;
        mClassName        = aClassName;
        mEpService        = aEpService;

        if( aDataType.equals( "JSON" ) == true )
        {
            mDataType = mDataType4JSON;
        }
        else if( aDataType.equals( "CSV" ) == true )
        {
            mDataType = mDataType4CSV;
        }
        else
        {
            mDataType = mDataType4Unknown;
        }

    }

    public void run()
    {
        String sFields[];
        String sLines[];
        int    sDataCnt    = 0;
        int    sCurLine    = 0;
        int    sItemLength  = 0;
        
        Class<?> sActClass = null;
        
        boolean sDebug0    = false;
        boolean sDebug1    = false;
         
        aliceEvent sEvent    = null;
        
        boolean sSkipFirst = false;
        ConsumerIterator<byte[], byte[]> it = mStream.iterator();

        sActClass = aliceEPLRunner.getEventClass( mTopicName );
        
        if( sActClass != null )
        {
            while( true )
            {
                while( it.hasNext() )
                {
                    if( mDataType == mDataType4CSV )
                    {
                        sLines = ( new String( it.next().message() ) )
                                            .split( mNewDataSeperator );
                        if( sLines.length > 0 )
                        {
                            sDataCnt++;
                            for( final String sLine : sLines )
                            {
                                sFields = sLine.split( "," );
                                try{
                                    sEvent = (aliceEvent)sActClass.newInstance();
                                    sEvent.setValues( sFields );
                                    sItemLength = sEvent.length();
                                    mEpService.getEPRuntime().sendEvent(sEvent);
                                    sDataCnt ++;
                                    if( sDebug1 == true )
                                    {

                                        System.out.println( mThreadNum +
                                                            "'s thread : " +
                                                            sDataCnt      +
                                                            "'s data" );
                                    }
                                }
                                catch( Exception e )
                                {
                                    System.out.println( mThreadNum +
                                                        "'s thread : " + e );
                                }
                            }
                        }
                        else
                        {
                            System.out.println( 
                                           mThreadNum +
                                           "'s thread : received null data" );
                        }


                    }
                    else if( mDataType == mDataType4JSON )
                    {
                        Pattern mLinePattern = Pattern.compile(
                                                   aliceJSON.mPattern4JSONLine );
                        Matcher mLineMatcher = mLinePattern.matcher(
                                            new String( it.next().message() ) );
                        while( mLineMatcher.find() )
                        {
                            try
                            {
                                sEvent = ( aliceEvent) sActClass.newInstance();
                                sItemLength = sEvent.length();
                                if( sDebug1 == true )
                                {
                                    System.out.println( sEvent.toString() );
                                }
                                sFields = aliceJSON.getValues( 
                                                 mLineMatcher.group(1),
                                                 sItemLength );

                                if( sFields != null )
                                {
                                    sEvent.setValues( sFields ); 
                                    mEpService.getEPRuntime().sendEvent(sEvent);
                                    sDataCnt ++;
                                    if( sDebug1 == true )
                                    {

                                        System.out.println( mThreadNum +
                                                            "'s thread : " +
                                                            sDataCnt      +
                                                            "'s data" );
                                    }
                                }
                            }

                            catch( Exception e )

                            {
                                System.out.println( mThreadNum +
                                                    "'s thread : " + e );
                            }
                        }
                    }
                    else
                    {
                        System.out.println( mThreadNum +
                                      "'s thread : do not supported data." );
                    }
                }
                try
                {
                    Thread.sleep( 100 );
                }
                catch( InterruptedException e )
                {
                    System.out.println( "exception : thread_num=" +
                                        mThreadNum +
                                        ","+
                                        e );
                }
            }
        }
    }
}
