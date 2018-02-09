/***********************************************
 * aliceDebugEvent.java :
 ***********************************************/
package com.alice.cep;


public class aliceDebugEvent extends aliceEvent
{
    final int mTraceItemNum = 8;

    private String date;
    private String protocol;
    private String logType;
    private String subSystemID;
    private String applicationName;
    private String className;
    private String sessionID;
    private String log;

    public aliceDebugEvent()
    {

    }

    public boolean setValues( String []aArgv )
    {
        int sCnt = 0;
        if( ( aArgv.length < mDebugItemNum - 1 ) ||
            ( aArgv.length > mDebugItemNum ) )
        {
            return false;
        }

        date            = aArgv[sCnt++];
        protocol        = aArgv[sCnt++];
        logType         = aArgv[sCnt++];
        subSystemID     = aArgv[sCnt++];
        applicationName = aArgv[sCnt++];
        className       = aArgv[sCnt++];
        sessionID       = aArgv[sCnt++];
        
        if( aArgv.length == mDebugItemNum )
        {
            log         = aArgv[sCnt++];
        }
        else
        {
            log         = "";

        }
        return true;
    }

    public int length()
    {
        return mDebugItemNum;
    }

    public String getDate()
    {
        return date;
    }

    public String getProtocol()
    {
        return protocol;
    }

    public String getClassName()
    {
        return className;
    }

    public String getSessionID()
    {
        return sessionID;
    }

    public String getlogType()
    {
        return logType;
    }

    public String getSubSystemID()
    {
        return subSystemID;
    }

    public String getApplicationName()
    {
        return applicationName;
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
        aArgv[sCnt++] = logType;
        aArgv[sCnt++] = subSystemID;
        aArgv[sCnt++] = applicationName;
        aArgv[sCnt++] = className;
        aArgv[sCnt++] = sessionID;
        aArgv[sCnt++] = log;
    }

    public String toString()
    {
        return "{ccpDebugEvent, Item=" +mDebugItemNum +
                   ", date " +
                   ", protocol" +
                   ", logType" +
                   ", subSystemID" +
                   ", applicationName" +
                   ", className" +
                   ", sessionID" +
                   ", log}";
    }
}

