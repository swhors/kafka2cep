/***********************************************
 * CepToDBUserDef.java
 ***********************************************/
package com.simpson.kafka2cep.plugin;

import com.simpson.kafka2cep.cep.to.CepOutToDB2Mysql;
import com.simpson.kafka2cep.db.CepDBUserDef;

public class CepToDBUserDef implements CepDBUserDef
{
    final private String mHostIP     = "localhost";
    final private int    mHostPort   = 3306;
    final private String mDBName     = "dbuser";
    final private String mHostUser   = "dbuser";
    final private String mHostPasswd = "dbuser";
    final private String mTableName  = "callCdrCount";
    final private String mCol1       = "TS";
    final private String mCol2       = "count";
    final private String mDBQuery = "insert into " +
                                    mDBName +
                                    "." +
                                    mTableName +
                                    "(" + mCol1 + "," + mCol2 + ")" + 
                                    "values(?,?);";
    
    public CepOutToDB2Mysql mDBDriver;
    
    public CepToDBUserDef()
    {
    }

    public boolean open()
    {
        mDBDriver = new CepOutToDB2Mysql();
        
        return mDBDriver.open( mHostIP,
                               mHostPort,
                               mHostUser,
                               mHostPasswd,
                               mDBName,
                               mDBQuery );
    }

    public void close()
    {
        if( mDBDriver != null )
        {
            mDBDriver.close();
        }
    }

    public boolean insert( String[] aValues )
    {
        if( mDBDriver != null )
        {
            return mDBDriver.insert( aValues );
        }
        return false;
    }
}
