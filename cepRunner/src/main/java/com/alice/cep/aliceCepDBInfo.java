/********************************************
 * ccpCepDBInfo.java
 ********************************************/
package com.alice.cep;

public class aliceCepDBInfo
{
    final public int STMT_NONEPREPARED = 0;
    final public int STMT_PREPARED = 1;

    private String mDBUser       = "";
    private String mDBPassword   = "";
    private String mDBName       = "";
    private int    mDBPort       = 0;
    private String mDBHost       = "";
    private String mDBConInfo    = "";
    private String mQuery        = "";
    private int    mPreparedMode = STMT_NONEPREPARED;

    public void setDBName( String aDBName )
    {
        mDBName = aDBName;
    }

    public String getDBName()
    {
        return mDBName;
    }

    public void setDBPassword( String aDBPassword )
    {
      mDBPassword = aDBPassword;
    }
    
    public String getDBPassword()
    {
        return mDBPassword;
    }

    public void setDBUser( String aDBUser )
    {
        mDBUser = aDBUser;
    }
    
    public String getDBUser()
    {
        return mDBUser;
    }
    
    public void setDBPort( int aDBPort )
    {
        mDBPort = aDBPort;
    }

    public int getDBPort()
    {
        return mDBPort;
    }

    public void setDBHost( String aDBHost )
    {
        mDBHost = aDBHost;
    }

    public String getDBHost()
    {
        return mDBHost;
    }

    public String getDBConInfo()
    {
        return mDBConInfo;
    }

    public void setDBConInfo(String aConInfo)
    {
        mDBConInfo = aConInfo;
    }

    public void setQuery( String aQuery )
    {
        mQuery = aQuery;
    }

    public String getQuery()
    {
        return mQuery;
    }

    public void setPreparedMode( int aMode )
    {
        mPreparedMode = aMode;
    }

    public int getPreparedMode()
    {
        return mPreparedMode;
    }
}

