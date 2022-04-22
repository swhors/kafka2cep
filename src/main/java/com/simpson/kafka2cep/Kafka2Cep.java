/**********************************************
 * Kafka2Cep.java
 **********************************************/
package com.simpson.kafka2cep;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import com.espertech.esper.client.Configuration;
import com.espertech.esper.client.EPServiceProvider;
import com.espertech.esper.client.EPServiceProviderManager;
import com.simpson.kafka2cep.cep.EPLRunner;
import com.simpson.kafka2cep.config.Config;
import com.simpson.kafka2cep.config.KafkaConfig;
import com.simpson.kafka2cep.http.SimpleHttpServer;
import com.simpson.kafka2cep.kafka.ConsumerThreadPool;

import com.simpson.kafka2cep.cep.event.CdrEvent;

public class Kafka2Cep implements KafkaConfig
{
    final static String mTag4UseHttp  = "use_http";
    final static String mTag4ConfFile = "conf";

    final static String mDefConfFile = "conf/kafka2cep.conf";

    private boolean mUsingMultiThreadPool = true;
    private boolean mUsingHttpServer      = false;

    //public Config mConfig;

    public String  mEqlXmlFileName = "eqllist.xml";

    public Kafka2Cep( String aConfFileName )
    {
        Config.loadConfig( aConfFileName );

        mUsingMultiThreadPool = true;
        mUsingHttpServer      = false;
    }

    public boolean getUsingMultiThreadPool()
    {
        return mUsingMultiThreadPool;
    }

    public boolean getUsingHttpServer()
    {
        return mUsingHttpServer;
    }

    public void setUsingMultiThreadPool( boolean aVal )
    {
        mUsingMultiThreadPool = aVal;
    }

    public void setUsingHttpServer( boolean aVal )
    {
        mUsingHttpServer = aVal;
    }

    public static void printArgs( HashMap<String, String> aMap )
    {
    	Set<String> keys = aMap.keySet();
    	Iterator<String> it = keys.iterator();
    	
    	while(it.hasNext()) {
    		String key = it.next();
    		String value = aMap.get(key);
    		System.out.println("Key:"+key +
                               ", Value:" + value);
    	}
    }

    public static HashMap<String, String> parseArgs( String []aArgs )
    {
        String  sArgOpt   = "";
        String  sArgVal   = "";
        HashMap<String, String> sArgsMap  = new HashMap<String, String>();

        for( int i = 0; i < aArgs.length; i++)
        {
            switch( aArgs[i].charAt(0) )
            {
                case '-':
                    if( aArgs[i].length() < 2)
                    {
                        throw new IllegalArgumentException(
                                         "Not a valid argument: "+aArgs[i]);
                    }
                    if( aArgs[i].charAt(1) == '-')
                    {
                        if (aArgs[i].length() < 3)
                        {
                            throw new IllegalArgumentException(
                                         "Not a valid argument: "+aArgs[i]);
                        }
                        // --opt
                        sArgOpt = aArgs[i].substring(2, aArgs[i].length() );
                    }
                    else
                    {
                        if( aArgs[i].length()-1 == i )
                        {
                            throw new IllegalArgumentException(
                                         "Expected arg after: "+aArgs[i]);
                        }
                        sArgOpt = aArgs[i].substring(1, aArgs[i].length() );
                    }
                    break;
                default:
                    // arg
                    sArgVal = aArgs[i];
                    sArgsMap.put( sArgOpt, sArgVal );
                    break;
            }
        }

        return sArgsMap;
    }

    public static void main( String []aArgs )
    {
        int     sStepCnt  = 0;
        boolean sUseHttp  = false;
        HashMap <String, String> sArgsMap;
        String  sConfFile = Kafka2Cep.mDefConfFile;


        System.out.println( "[Step " + sStepCnt++ +"] "  +
                            "Start to initialization of" +
                            " aliceCdrRunner.\n");

        // Argument Parse.
        System.out.println( "[Step " + sStepCnt++ +"] " +
                            "Parse argument.[1]\n");
        sArgsMap = parseArgs( aArgs );
        printArgs( sArgsMap );

        if( sArgsMap != null )
        {
            if( sArgsMap.get(mTag4UseHttp) != null )
            {
                String sArgVal = sArgsMap.get(mTag4UseHttp).toString();
                if( sArgVal != null )
                {
                    if( sArgVal.equals("yes") )
                    {
                        sUseHttp = true;
                    }
                    else
                    {
                        sUseHttp = false;
                    }
                }
                else
                {
                    sUseHttp = false;
                }
            }
            if( sArgsMap.get(mTag4ConfFile) != null )
            {
                sConfFile = sArgsMap.get(mTag4ConfFile).toString();
            }
        }

        /**********************************
         * Initilaization Kafka2Cep   *
         **********************************/
        Kafka2Cep sCounter = new Kafka2Cep( sConfFile );
        sCounter.setUsingHttpServer( sUseHttp );

        /**********************************
         * Initilaization CEP Engine      *
         **********************************/
        System.out.println( "[Step " + sStepCnt++ +"] " +
                            "Start CEP Engine.\n");
        
        Configuration sConfiguration = new Configuration();
        sConfiguration.addEventTypeAutoName(
                   Config.getEqlClsID());
        sConfiguration.addEventType("CdrEvent", CdrEvent.class);

        EPServiceProvider sEpService =
             EPServiceProviderManager.getDefaultProvider(
                                            sConfiguration );
        		
        /**********************************
         * Regist EPL Lists from File.    *
         **********************************/
        if( !EPLRunner.initEPLService( Config.getEqlList(),
                                         sEpService,
                                         Config.getEqlPrintDate(),
                                         Config.getEqlLocale(),
                                         Config.getKafkaTopic() ) )
        {
            System.out.println("Error : fail to regist epl lists.");
            System.exit(0);
        }


        /**********************************
         * Start Kafka Engine             *
         **********************************/
        System.out.println( "[Step " + sStepCnt++ +"] " +
                            "Start Kafka Engine.\n");
        System.out.printf("Starting ... [com.ccp.cep]\n" );
        ConsumerThreadPool consumerThread
                    = new ConsumerThreadPool(
                               Config.getKafkaTopic(),
                               sEpService,
                               Integer.parseInt(
                                   Config.getKafkaThreadNum()
                               ),
                               Config.getEqlClsID(),
                               Config.getKafkaDataType(),
                               Config.getKafkaDataSeperator()
                           );
        consumerThread.run();

        /**********************************
         * Start WWW Service              *
         **********************************/
        if( sCounter.getUsingHttpServer() )
        {
            System.out.println( "[Step " + sStepCnt++ +"] " +
                                "Start WWW Service.\n");
            try
            {
                SimpleHttpServer sHttpServer = new SimpleHttpServer();
                sHttpServer.run( Integer.parseInt(
                                          Config.getWWWPort() ) );
            }
            catch( IOException e )
            {
                System.out.println("Exception : " + e );
            }
        }

        while( true )
        {
            try
            {
                Thread.sleep(4000);
            }
            catch( InterruptedException e )
            {
                 //We've been interrupted: no more messages.
                 break;
            }
        }
    }
}
