/***************************************************
 * CepOutToDB2Mysql.java
 ***************************************************/
package com.simpson.kafka2cep.cep.to;

import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import com.simpson.kafka2cep.config.DBInfo;
import com.simpson.kafka2cep.db.CepDBInterface;
import com.simpson.kafka2cep.util.StringUtil;

public class CepOutToDB2Mysql extends DBInfo implements CepDBInterface
{
    private final String DB_DRIVER         = "com.mysql.jdbc.Driver";
    private final String DB_CONNECTION_PRE = "jdbc:mysql://";

    private Connection        mDBConnection = null;
    private PreparedStatement mPreparedStatement = null;
    private int               mValueCount4Prepared = 0;

    public CepOutToDB2Mysql()
    {
    }

    public CepOutToDB2Mysql( String aUser,
                             String aPasswd,
                             String aHost,
                             int    aPort,
                             String aDBName,
                             String aQuery )
    {
        setDBAddress( aUser, aPasswd, aHost, aPort, aDBName, aQuery );
    }
    
    public boolean open( String aHost,
                         int    aPort,
                         String aDBUser,
                         String aDBPasswd,
                         String aDBName,
                         String aQuery )
    {
        boolean sRet = false;
        setDBAddress( aDBUser, aDBPasswd, aHost, aPort, aDBName, aQuery );
        
        try
        {
            sRet = open();
        }
        catch( Exception e )
        {
            System.out.println("Exception : " + e );
            sRet = false;
        }
        return sRet;

    }

    public boolean open() throws SQLException
    {
        try
        {
            Class.forName( DB_DRIVER );
            System.out.println( "DBURL = " + getDBConInfo() + "\n" +
                                "User      = " + getDBUser() + "\n" +
                                "Pass      = " + getDBPassword() + "\n" );
            mDBConnection = DriverManager.getConnection(
                                               getDBConInfo(),
                                               getDBUser(),
                                               getDBPassword() );
            if( mDBConnection != null )
            {
                if( getPreparedMode() == STMT_PREPARED )
                {
                    mPreparedStatement = mDBConnection.
                                           prepareStatement( getQuery() );
                }
                else
                {
                }
            }
        }
        catch( SQLException e )
        {
            System.out.println( e.getMessage() );
            return false;
        }
        catch( Exception e )
        {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public void close()
    {
        try
        {
            if( mPreparedStatement != null )
            {
                 mPreparedStatement.close();
            }
            if( mDBConnection != null )
            {
                 mDBConnection.close();
            }
        }
        catch( SQLException e )
        {
            System.out.println( e.getMessage() );
        }
    }

    public void setDBAddress( String aUser,
                              String aPasswd,
                              String aHost,
                              int    aPort,
                              String aDBName,
                              String aQuery )
    {
        setDBHost( aHost );
        setDBPort( aPort );
        setDBUser( aUser );
        setDBPassword( aPasswd );
        if( aQuery != null )
        {
            setQuery( aQuery );
            mValueCount4Prepared = StringUtil.getMatchedCount( aQuery, "\\?" );
            if( mValueCount4Prepared > 0 )
            {
                setPreparedMode( STMT_PREPARED );
            }
            else
            {
                 setPreparedMode(  STMT_NONEPREPARED );
            }
        }
        else
        {
        }
        setDBName( aDBName );
        
        if( aPort == 0 )
        {
            setDBConInfo( DB_CONNECTION_PRE + aHost + "/" + aDBName );
        }
        else
        {
            setDBConInfo( DB_CONNECTION_PRE +
                          aHost + ":" + aPort + "/" + aDBName );

        }
    }
    
    public boolean insert( String[] aValues )
    {
        int sCnt = 1;
        boolean sRet = true;
        try
        {
             System.out.println("insert " + aValues );
             if( aValues.length == mValueCount4Prepared )
             {
                 for( String sValue : aValues )
                 {
                     mPreparedStatement.setString( sCnt++, sValue );
                 }
                 // Execute insert SQL statement
                 mPreparedStatement.executeUpdate();
             }
        }
        catch( Exception e )
        {
            System.out.println( "Exception : " + e );
            sRet = false;
        }
        return sRet;
    }
    
    public boolean isConnected()
    {
        return false;
    }
}
