/***********************************************
 * aliceEventListener.java
 ***********************************************/

package com.alice.cep;

import java.util.*;
import java.text.*;

import com.espertech.esper.client.EventBean;
import com.espertech.esper.client.UpdateListener;

public class aliceEventListener implements UpdateListener
{
    private aliceCepTo   mCcpCepTo;
    private String   []mKeys;
    private Locale     mLocale;
    private boolean    mPrintDate = false;
    private int        mValueNumber = 0;

    public aliceEventListener( aliceCepTo   aCcpCepTo,
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
            if( sKey.equals("*") == true )
            {
                mValueNumber += ( aItemNumber - 1 );
                break;
            }
        }
 
        if( mPrintDate == true )
        {
            mValueNumber++;
        }
    }

    public void update( EventBean[] aNewEvents,
                                    EventBean[] aOldEvents )
    {
        EventBean        sEvent     = aNewEvents[0];

        String[]         sValues    = null;
        int              sCnt         = 0;
        int              sItemSize = mKeys.length;
        Date             sCurTime = null;
        SimpleDateFormat sTimeFormat = null;

        if( mPrintDate == true )
        {
            sTimeFormat = new SimpleDateFormat( "yyyy.MM.dd HH:mm:ss",
                                                mLocale );
            sCurTime = new Date();
        }

        if( mCcpCepTo != null )
        {
            sValues = new String[mValueNumber];
            if( mPrintDate == true )
            {
                sValues[sCnt++] = sTimeFormat.format( sCurTime );
            }
 
            for( String sKey : mKeys )
            {
                if( sKey.equals("*") == true )
                {
                    String [] sAllValue;
                    aliceEvent sEvent1 = (ccpEvent) aNewEvents[0].getUnderlying();
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
