/**************************************************
 * aliceEPLRunner.java
 *
 * author : swhors@naver.com
 **************************************************/

package com.simpson.kafka2cep.cep;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import com.espertech.esper.client.EPServiceProvider;
import com.espertech.esper.client.EPStatement;
import com.simpson.kafka2cep.cep.event.CepEvent;
import com.simpson.kafka2cep.cep.event.CepEventListener;
import com.simpson.kafka2cep.cep.to.CepOutTarget;
import com.simpson.kafka2cep.kafka.KafkaTopicInfo;
import com.simpson.kafka2cep.util.XmlReader;

public class EPLRunner
{
    static final private String mTag4LocaleUS     = "Locale.US";
    static final private String mTag4LocaleJAPAN  = "Locale.JAPAN";
    static final private String mTag4LocaleCHINA  = "Locale.CHINA";

    static public HashMap<String, EqlObject > mMap4Eql;

    static private String mEqlListFileName   = "";

    static public EPServiceProvider    mEpService;

    static private Locale  mLocale    = Locale.KOREA;
    static private boolean mPrintDate = false;

    static private HashMap< String, EPLRunner >
                                           mEPLRunners = null;

    public String   mTopic;
    public String   mClassName;
    public String   mDataType;
    public Class<?> mActClass   = null;
    public int      mEventItemNumber = 0;

    public EPLRunner( String aTopic,
                         String aClassName,
                         String aDataType )
    {
        mTopic     = aTopic;
        mClassName = aClassName;
        mDataType  = aDataType;
        try
        {
            mActClass = Class.forName( mClassName );
            mEventItemNumber = ((CepEvent)mActClass.newInstance()).length();
        }
        catch (ClassNotFoundException e) {
            System.out.println( "ClassNotFoundException : " + e );        	
        }
        catch( Exception e) {
            System.out.println( "Exception : " + e );
        }
    }

    static public boolean stopEPL( String aID )
    {
        boolean     sRet       = false;
        EPStatement sStatement = null;
        EqlObject sObject = mMap4Eql.get( aID );

        if( sObject != null )
        {
            sStatement = (EPStatement) sObject.getStmt();
            if( sStatement != null )
            {
                sStatement.stop();
                sStatement.destroy();
                mMap4Eql.remove( aID );
                XmlReader.write( mEqlListFileName, mMap4Eql );
                sRet = true;
            }
        }

        return sRet;
    }

    static int getClassItemCountByFrom( String aFrom )
    {
        int sItemCntNum = 0;

        for( Map.Entry<String, EPLRunner> sEntry : mEPLRunners.entrySet() )
        {
            EPLRunner sRunner = sEntry.getValue();
            if( aFrom.startsWith( sRunner.mClassName ) )
            {
                sItemCntNum = sRunner.mEventItemNumber;
                break;
            }
        }

        return sItemCntNum;
    }

    static int getClassItemCount( String aClsName )
    {
        int sItemCntNum = 0;

        for( Map.Entry<String, EPLRunner> sEntry : mEPLRunners.entrySet() )
        {
            EPLRunner sRunner = sEntry.getValue();
            if( sRunner.mClassName == aClsName )
            {
                sItemCntNum = sRunner.mEventItemNumber;
                break;
            }
        }

        return sItemCntNum;
    }

    static public boolean runEPL( String aID,
                                  String aMain,
                                  String aFrom,
                                  String aWhere,
                                  String aTo ) throws Exception
    {
        String   sEql  = aMain + " from " + aFrom;
        String   sMain = "";
        String []sKeys = null;

        CepOutTarget     sCcpCepTo  = null;
        EPStatement  sStatement = null;

        boolean      sRet        = false;
        int          sSpaceIndex = 0;

        sSpaceIndex = aMain.indexOf(" ", 0 );

        if( sSpaceIndex > 0 )
        {
            sMain = aMain.substring( sSpaceIndex+1, aMain.length() );
        }
        else
        {
            sMain = aMain;
        }

        sKeys = sMain.split(",");

        if( aWhere != null )
        {
            if( !aWhere.isEmpty() )
            {
                if( aWhere.length() > 0 )
                {
                    sEql += " where " + aWhere;
                }
            }
        }

        EqlObject sObject = mMap4Eql.get( aID );

        if( sObject == null || sObject.getStmt() == null )
        {
            try
            {
                int sEventItemNumber = 0;
                sCcpCepTo = CepOutTarget.getInstance( aTo );
                if( sCcpCepTo == null )
                {
                    throw new Exception("to is illegal.");
                }

                sEventItemNumber = getClassItemCount( aFrom );

                sStatement = mEpService.getEPAdministrator().createEPL( sEql );

                CepEventListener sListener
                           = new CepEventListener( sCcpCepTo,
                                                   sKeys,
                                                   mPrintDate,
                                                   mLocale,
                                                   sEventItemNumber );
                sStatement.addListener( sListener );
                sRet = true;

                if( sObject == null )
                {
                    sObject = new EqlObject( aID,
                                                aMain,
                                                aFrom,
                                                aWhere,
                                                aTo,
                                                sStatement ) ;
                    mMap4Eql.put( aID, sObject );
                    XmlReader.write( mEqlListFileName, mMap4Eql );
                }
                else
                {
                    sObject.setStmt( sStatement );
                    mMap4Eql.put( aID, sObject );
                }
                System.out.println("Running EQL = " + sEql );

            }
            catch( Exception e )
            {
                if( sStatement != null )
                {
                    sStatement.stop();
                    sStatement.destroy();
                }
                if( sCcpCepTo != null )
                {
                    sCcpCepTo.close();
                }

                sObject = mMap4Eql.get( aID );
                if( sObject != null )
                {
                    mMap4Eql.remove( aID );
                }
            }
        }
        else
        {
            System.out.println("addEPLService error, pre-exist.\n" );
        }
        return sRet;
    }

    public static Class<?> getEventClass( String aTopic )
    {
        EPLRunner sRunner;
        for( Map.Entry<String, EPLRunner> sEntry : mEPLRunners.entrySet() )
        {
            sRunner = sEntry.getValue();
            if( sRunner.mTopic == aTopic )
            {
                return sRunner.mActClass;
            }
        }
        return null;
    }

    public static boolean initEPLService(
                       String                  aEqlListFile,
                       EPServiceProvider       aServiceProvider,
                       boolean                 aPrintDate,
                       String                  aLocale,
                       HashMap<String, KafkaTopicInfo> aTopic )
    {
        boolean sDebug   = false;
        mEpService       = aServiceProvider;
        mEqlListFileName = aEqlListFile;
        boolean sRet     = true;

        mPrintDate = aPrintDate;

        mEPLRunners = new HashMap< String, EPLRunner >();

        try
        {
            for( Map.Entry<String, KafkaTopicInfo > sEntry:
                                     aTopic.entrySet() )
            {
                String sTopic                = sEntry.getKey();
                KafkaTopicInfo sTopicInfo = sEntry.getValue();
                EPLRunner sRunInfo = new EPLRunner(
                                                sTopic,
                                                sTopicInfo.mClassName,
                                                sTopicInfo.mDataType );
                mEPLRunners.put( sTopic, sRunInfo );
            }
            if( aLocale != null )
            {
                if( aLocale.equals( mTag4LocaleCHINA ) )
                {
                    mLocale = Locale.CHINA;
                }
                else if( aLocale.equals( mTag4LocaleUS ) )
                {
                    mLocale = Locale.US;
                }
                else if( aLocale.equals( mTag4LocaleJAPAN ) )
                {
                    mLocale = Locale.JAPAN;
                }
                else
                {
                    mLocale = Locale.KOREA;
                }
            }

            mMap4Eql = XmlReader.read(  aEqlListFile );

            if( sDebug )
            {
                System.out.println("aEqlList = " + aEqlListFile );
            }

            for( Map.Entry<String, EqlObject> sEntry : mMap4Eql.entrySet() )
            {
                EqlObject sEQL = sEntry.getValue();
                if( !runEPL( String.valueOf(sEQL.getID()),
                            sEQL.getMain(),
                            sEQL.getFrom(),
                            sEQL.getWhere(),
                            sEQL.getTo() ) )
                {
                    sRet = false;
                    break;
                }
            }
        }
        catch( Exception e )
        {
            sRet = false;
            System.out.println( "Exception : " + e );
        }

        return sRet;
    }
}
