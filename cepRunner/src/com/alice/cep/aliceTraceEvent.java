
/*****************************************
 * aliceTraceEvent.java :
 *****************************************/

package com.alice.cep;


public class aliceTraceEvent extends aliceEvent
{
    final int mTraceItemNum = 14;

    private String date;
    private String appid;
    private String className;
    private String sessionID;
    private String messageType;
    private String messageMethod;
    private String direction;
    private String source;
    private String destination;
    private String fromUri;
    private String toUri;
    private int    erroCode;
    private String reason;
    private String log;

    public aliceTraceEvent()
    {
    }

    public boolean setValues( String []aArgv )
    {
        int sCnt = 0;

        if( ( aArgv.length < mTraceItemNum - 1 ) ||
            ( aArgv.length > mTraceItemNum ) )
        {
            return false;
        }

        date          = aArgv[sCnt++];
        appid         = aArgv[sCnt++];
        className     = aArgv[sCnt++];
        sessionID     = aArgv[sCnt++];
        messageType   = aArgv[sCnt++];
        messageMethod = aArgv[sCnt++];
        direction     = aArgv[sCnt++];
        source        = aArgv[sCnt++];
        destination   = aArgv[sCnt++];
        fromUri       = aArgv[sCnt++];
        toUri         = aArgv[sCnt++];
        erroCode      = Integer.parseInt( aArgv[sCnt++], 10 );
        reason        = aArgv[sCnt++];
        
        if( aArgv.length == mTraceItemNum )
        {
            log       = aArgv[sCnt++];
        }
        else
        {
            log       = "";
        }
        return true;
    }

    public int length()
    {
        return mTraceItemNum;
    }

    public String getDate()
    {
        return date;
    }

    public String getAppid()
    {
        return appid;
    }

    public String getClassName()
    {
        return className;
    }

    public String getSessionID()
    {
        return sessionID;
    }

    public String getMessageType()
    {
        return messageType;
    }

    public String getMessageMethod()
    {
        return messageMethod;
    }

    public String getDirection()
    {
        return direction;
    }

    public String getSource()
    {
        return source;
    }

    public String getDestination()
    {
        return destination;
    }

    public String getFromUri()
    {
        return fromUri;
    }

    public String getToUri()
    {
        return toUri;
    }

    public int getErrorCode()
    {
        return errorCode;
    }

    public String getReason()
    {
        return reason;
    }

    public String getLog()
    {
        return log;
    }

    public String[] getValues()
    {
        int sCnt = 0;

        String [] sValues = new String[mCdrItemNum];

        aArgv[sCnt++] = date;
        aArgv[sCnt++] = appid;
        aArgv[sCnt++] = className;
        aArgv[sCnt++] = sessionID;
        aArgv[sCnt++] = messageType;
        aArgv[sCnt++] = messageMethod;
        aArgv[sCnt++] = direction;
        aArgv[sCnt++] = source;
        aArgv[sCnt++] = destination;
        aArgv[sCnt++] = fromUri;
        aArgv[sCnt++] = toUri;
        aArgv[sCnt++] = String.valueOf( errorCode );
        aArgv[sCnt++] = reason;
        aArgv[sCnt++] = log;
    }

    public String toString()
    {
        return "{ccpTraceEvent, Item=" +mTraceItemNum +
                   ", date " +
                   ", appid" +
                   ", className" +
                   ", sessionID" +
                   ", messageType" +
                   ", messageMethod" +
                   ", direction" +
                   ", source" +
                   ", destination" +
                   ", fromUri" +
                   ", toUri" +
                   ", errorCode" +
                   ", reason" +
                   ", log}";
    }
}
