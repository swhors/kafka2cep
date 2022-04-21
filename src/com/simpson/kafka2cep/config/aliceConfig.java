/************************************************
 * aliceConfig.java
 ************************************************/
package com.kafka2esper.config;

import java.io.IOException;
import java.util.*;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.FileOutputStream;
import java.util.Properties;

public class aliceConfig
{
    static final private int mTag4WWWPort          = 0;
    static final private int mTag4EqlList          = 1;
    static final private int mTag4EqlClsID         = 2;
    static final private int mTag4EqlPrintDate     = 3;
    static final private int mTag4EqlLocale        = 4;
    static final private int mTag4KafkaZooList     = 5;
    static final private int mTag4KafkaGroupID     = 6;
    static final private int mTag4KafkaTopic       = 7;
    static final private int mTag4KafkaObjCls      = 8;
    static final private int mTag4KafkaConTimeout  = 9;
    static final private int mTag4KafkaReConInt    = 10;
    static final private int mTag4KafkaClientID    = 11;
    static final private int mTag4KafkaThreadNum   = 12;
    static final private int mTag4KafkaDataType    = 13;
    static final private int mTag4KafkaDataSep     = 14;
    static final private int mTag4KafkaOffsetReset = 15;
    static final private int mTagsNum              = 16;

    static final private String mTags[]={ "www.port",
        "eql.list",
        "eql.clsid",
        "eql.printDate",
        "eql.locale",
        "kafka.zoo.lists",
        "kafka.groupid",
        "kafka.topic",
        "kafka.obj.class",
        "kafka.connectionTimeout",
        "kafka.reconnectInterval",
        "kafka.clientid",
        "kafka.threadNum",
        "kafka.dataType",
        "kafka.dataSeperator",
        "kafka.offsetReset"
    };

    //private HashMap<String,ccpKafkaTopicInfo> mTopic =
    //              new HashMap<String, aliceKafkaTopicInfo >();

    static private Properties mProper;
    static private String     mConfFile;

    static public String getWWWPort()
    {
        return mProper.getProperty( mTags[mTag4WWWPort] );
    }

    static public String getEqlList()
    {
        return mProper.getProperty( mTags[mTag4EqlList] );
    }

    static public String getEqlClsID()
    {
        return mProper.getProperty( mTags[mTag4EqlClsID] );
    }

    static public boolean getEqlPrintDate()
    {
        String sPrintDate = mProper.getProperty(
                mTags[mTag4EqlPrintDate] );
        if( sPrintDate != null )
        {
            if( sPrintDate.equals( "true" ) == true  )
            {
                return true;
            }
        }
        return false;
    }

    static public String getEqlLocale()
    {
        return mProper.getProperty( mTags[mTag4EqlLocale] );
    }

    static public String getKafkaZooList()
    {
        return mProper.getProperty( mTags[mTag4KafkaZooList] );
    }

    static public String getKafkaGroupID()
    {
        return mProper.getProperty( mTags[mTag4KafkaGroupID] );
    }

    static public HashMap< String, aliceKafkaTopicInfo > getKafkaTopic()
    //static public HashMap< String, Object> getKafkaTopic()
    {
        String []sTopics;
        String []sFields;
        String sTopicLine =
            mProper.getProperty(mTags[mTag4KafkaTopic]);

        HashMap<String, aliceKafkaTopicInfo > sHashMap
            = new HashMap<String, aliceKafkaTopicInfo>();
        //HashMap<String, Object > sHashMap
        //    = new HashMap<String, Object>();

        if( sTopicLine.length() > 0 )
        {
            sTopics = sTopicLine.split(",");
            for( String sTopic : sTopics )
            {
                sFields = sTopic.split(":");

                if( sFields != null )
                {
                    sHashMap.put( sFields[0],
                            new aliceKafkaTopicInfo( sFields[0],
                                sFields[1],
                                sFields[2]) );
                }
            }
        }

        return sHashMap;
    }

    static public String getKafkaObjCls()
    {
        return mProper.getProperty( mTags[mTag4KafkaObjCls] );
    }

    static public String getKafkaConTimeOut()
    {
        return mProper.getProperty( mTags[mTag4KafkaConTimeout] );
    }

    static public String getKafkaReConInt()
    {
        return mProper.getProperty( mTags[mTag4KafkaReConInt] );
    }

    static public String getKafkaClientID()
    {
        return mProper.getProperty( mTags[mTag4KafkaClientID] );
    }

    static public String getKafkaThreadNum()
    {
        return mProper.getProperty( mTags[mTag4KafkaThreadNum] );
    }

    static public String getKafkaDataType()
    {
        return mProper.getProperty( mTags[mTag4KafkaDataType] );
    }

    static public String getKafkaDataSeperator()
    {
        return mProper.getProperty( mTags[mTag4KafkaDataSep] );
    }

    static public String getKafkaOffsetReset()
    {
        if( mProper.getProperty( mTags[mTag4KafkaOffsetReset] ) != null )
        {
            return mProper.getProperty(
                    mTags[mTag4KafkaOffsetReset]
                    );
        }
        else
        {
            return "smallest";
        }
    }

    static public void setWWWPort( String aPort )
    {
        mProper.setProperty( mTags[mTag4WWWPort],
                aPort );
    }

    static public void setEqlList( String aEqlListFile )
    {
        mProper.setProperty( mTags[mTag4EqlList],
                aEqlListFile );
    }

    static public void setEqlClsID( String aEqlClsID )
    {
        mProper.setProperty( mTags[mTag4EqlClsID],
                aEqlClsID );
    }

    static public void setEqlPrintDate( boolean aPrintDate )
    {
        if( aPrintDate == true )
        {
            mProper.setProperty( mTags[mTag4EqlPrintDate], "true" );
        }
        else
        {
        }
        mProper.setProperty( mTags[mTag4EqlPrintDate], "false" );
    }

    static public void setEqlLocale( String aLocale )
    {
        mProper.setProperty( mTags[mTag4EqlLocale],
                aLocale );
    }

    static public void setKafkaZooList( String aZooList )
    {
        mProper.setProperty( mTags[mTag4KafkaZooList],
                aZooList );
    }

    static public void setKafkaGroupID( String aKafkaGroupID )
    {
        mProper.setProperty( mTags[mTag4KafkaGroupID],
                aKafkaGroupID );
    }

    static public void setKafkaTopic( String aKafkaTopic )
    {
        mProper.setProperty( mTags[mTag4KafkaTopic],
                aKafkaTopic );
    }

    static public void setKafkaObjCls( String aKafkaObjCls )
    {
        mProper.setProperty( mTags[mTag4KafkaObjCls],
                aKafkaObjCls );
    }

    static public void setKafkaConTimeOut( String aKafkaConTimeOut )
    {
        mProper.setProperty( mTags[mTag4KafkaConTimeout],
                aKafkaConTimeOut );
    }

    static public void setKafkaReConInt( String aKafkaReConInt )
    {
        mProper.setProperty( mTags[mTag4KafkaReConInt],
                aKafkaReConInt );
    }

    static public void setKafkaClientID( String aKafkaCliID )
    {
        mProper.setProperty( mTags[mTag4KafkaClientID],
                aKafkaCliID );
    }

    static public void setKafkaThreadNum( String aKafkaThreadNum )
    {
        mProper.setProperty( mTags[mTag4KafkaThreadNum],
                aKafkaThreadNum );
    }

    static public void setKafkaDataType( String aKafkaDataType )
    {
        mProper.setProperty( mTags[mTag4KafkaDataType],
                aKafkaDataType );
    }

    static public void setKafkaDataSeperator( String aKafkaDataSeperator )
    {
        mProper.setProperty( mTags[mTag4KafkaDataSep],
                aKafkaDataSeperator );
    }

    static public void setKafkaOffsetReset( String aKafkaOffsetReset )
    {
        mProper.setProperty(
                mTags[mTag4KafkaOffsetReset],
                aKafkaOffsetReset );
    }

    static public boolean store()
    {
        boolean      sRet    = false;
        FileOutputStream sOutput = null;
        try
        {
            sOutput = new FileOutputStream(mConfFile );
            mProper.store( sOutput, null );
            sRet = true;
        }
        catch( Exception e )
        {
            System.out.println("Exception : " + e );
        }
        return sRet;
    }

    public aliceConfig()
    {
    }

    static public boolean loadConfig( String aFilePath )
    {
        mConfFile = aFilePath;
        mProper   = new Properties();
        try
        {
            // 한글이 깨지는 문제 때문에 인코딩을 지정해서 읽을수 있도록 함.
            mProper.load( new BufferedReader(
                        new InputStreamReader(
                            new FileInputStream( aFilePath ),
                            "MS949"
                            )
                        ) );
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
            return false;
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * Message
     * @param key
     * @return message
     */
    static public String read( String aKey )
    {
        return mProper.getProperty( aKey );
    }

    static public void print()
    {
        int sCnt = 0;

        for( sCnt = 0; sCnt < mTagsNum ; sCnt ++ )
        {
            System.out.println( mTags[sCnt] + "=" + read( mTags[sCnt] ) );
        }
    }

    /*
       public static void main( String aArgs[] )
       {
       aliceConfig.loadConfig( aArgs[0] );
       aliceConfig.print();
       }
     */
}
