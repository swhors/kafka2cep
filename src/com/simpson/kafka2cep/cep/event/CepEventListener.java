/***********************************************
 * CepEventListener.java
 ***********************************************/

package com.simpson.kafka2cep.cep.event;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import com.espertech.esper.client.EventBean;
import com.espertech.esper.client.UpdateListener;
import com.simpson.kafka2cep.cep.to.CepOutTarget;

public class CepEventListener implements UpdateListener
{
    private CepOutTarget mCcpCepTo;
    private String[]   mKeys;
    private Locale     mLocale;
    private boolean    mPrintDate = false;
    private int        mValueNumber = 0;

    public CepEventListener( CepOutTarget   aCcpCepTo,
                             String   []aKeys,
                             boolean    aPrintDate,
                             Locale     aLocale,
                             int        aItemNumber )
    {
        mCcpCepTo  = aCcpCepTo;
        mKeys      = aKeys;
        mPrintDate = aPrintDate;
        mLocale    = aLocale;

        mValueNumber = aKeys.length;

        for( String sKey : aKeys )
        {
            if( sKey.equals("*") )
            {
                mValueNumber += ( aItemNumber - 1 );
                break;
            }
        }

        if( mPrintDate )
        {
            mValueNumber++;
        }
    }

    @Override
	public void update( EventBean[] aNewEvents,
                                    EventBean[] aOldEvents )
    {
        EventBean        sEvent     = aNewEvents[0];

        String[]         sValues    = null;
        int              sCnt         = 0;
        Date             sCurTime = null;
        SimpleDateFormat sTimeFormat = null;

        if( mPrintDate )
        {
            sTimeFormat = new SimpleDateFormat( "yyyy.MM.dd HH:mm:ss",
                                                mLocale );
            sCurTime = new Date();
        }

        if( mCcpCepTo != null )
        {
            sValues = new String[mValueNumber];
            if( mPrintDate )
            {
                sValues[sCnt++] = sTimeFormat.format( sCurTime );
            }

            for( String sKey : mKeys )
            {
                if( sKey.equals("*") )
                {
                    String [] sAllValue;
                    CepEvent sEvent1 =
                                (CepEvent) aNewEvents[0].getUnderlying();
                    sAllValue = sEvent1.getValues();
                    for( String sValue: sAllValue )
                    {
                       sValues[sCnt++] = sValue;
                       //System.out.println(sCnt + "'s item=" + sValue);
                    }
                }
                else
                {
                    if( sEvent.get( sKey ) != null )
                    {
                        sValues[sCnt++] = sEvent.get( sKey ).toString();
                    }
                    else
                    {
                        sValues[sCnt++] =  null;
                    }
                }
            }
        }
        try
        {
            mCcpCepTo.write( sValues );
        }
        catch( Exception e )
        {
            System.out.println( "Exception : " + e );
        }
    }
}
