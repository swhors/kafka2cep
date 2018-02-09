/**********************************************
 * aliceCepRunner.java
 **********************************************/
package com.alice.cep;
 
import java.io.IOException;
 
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
 
import com.espertech.esper.client.Configuration;
import com.espertech.esper.client.EPServiceProvider;
import com.espertech.esper.client.EPServiceProviderManager;
import com.espertech.esper.client.EPStatement;
 
public class aliceCepRunner implements KafkaProperties
{
    final static String mTag4UseHttp  = "use_http";
    final static String mTag4ConfFile = "conf";
 
    final static String mDefConfFile = "conf/ccpCep.conf";
 
    private boolean mUsingMultiThreadPool = true;
    private boolean mUsingHttpServer      = false;
 
    //public aliceConfig mConfig;
 
    public String  mEqlXmlFileName = "eqllist.xml";
 
    public aliceCepRunner( String aConfFileName )
    {
        aliceConfig.loadConfig( aConfFileName );
 
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
 
    public static void printArgs( HashMap aMap )
    {
        Iterator iter = aMap.entrySet().iterator();
        while( iter.hasNext() )
        {
            Entry entry = (Entry) iter.next();
            System.out.println( "Key:"+entry.getKey() +
                                ", Value:" + entry.getValue() );
        }
    }
 
    public static HashMap parseArgs( String []aArgs )
    {
        String  sArgOpt   = "";
        String  sArgVal   = "";
        HashMap sArgsMap  = new HashMap();
 
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
        HashMap sArgsMap  = null;
        String  sConfFile = aliceCepRunner.mDefConfFile;
 
 
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
                    if( sArgVal.equals("yes") == true )
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
         * Initilaization aliceCepRunner   *
         **********************************/
        aliceCepRunner sCounter = new aliceCepRunner( sConfFile );
        sCounter.setUsingHttpServer( sUseHttp );
 
        /**********************************
         * Initilaization CEP Engine      *
         **********************************/
        System.out.println( "[Step " + sStepCnt++ +"] " +
                            "Start CEP Engine.\n");
 
        Configuration sConfiguration = new Configuration();
        sConfiguration.addEventTypeAutoName(
                   aliceConfig.getEqlClsID() );
        //sConfiguration.addEventTypeAlias(
        //          "ccpCdrEvent",
        //          aliceCdrEvent.class );
 
        EPServiceProvider sEpService =
             EPServiceProviderManager.getDefaultProvider(
                                            sConfiguration );
        /**********************************
         * Regist EPL Lists from File.    *
         **********************************/
        if( aliceEPLRunner.initEPLService( aliceConfig.getEqlList(),
                                         sEpService,
                                         aliceConfig.getEqlPrintDate(),
                                         aliceConfig.getEqlLocale(),
                                         aliceConfig.getKafkaTopic() )
            == false )
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
                               aliceConfig.getKafkaTopic(),
                               sEpService,
                               Integer.parseInt(
                                   aliceConfig.getKafkaThreadNum()
                               ),
                               aliceConfig.getEqlClsID(),
                               aliceConfig.getKafkaDataType(),
                               aliceConfig.getKafkaDataSeperator()
                           );
        consumerThread.run();
 
        /**********************************
         * Start WWW Service              *
         **********************************/
        if( sCounter.getUsingHttpServer() == true )
        {
            System.out.println( "[Step " + sStepCnt++ +"] " +
                                "Start WWW Service.\n");
            try
            {
                aliceHttpServer sHttpServer = new aliceHttpServer();
                sHttpServer.run( Integer.parseInt(
                                          aliceConfig.getWWWPort() ) );
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
