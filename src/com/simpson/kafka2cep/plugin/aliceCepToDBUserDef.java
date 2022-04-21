/***********************************************
 * aliceCepToDBUserDef.java
 ***********************************************/
package com.kafka2esper.plugin;

import com.kafka2esper.cep.aliceCepDBUserDef;
import com.kafka2esper.cep.aliceCepToDB2Mysql;

public class aliceCepToDBUserDef implements aliceCepDBUserDef
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
    
    public aliceCepToDB2Mysql mDBDriver;
    
    public aliceCepToDBUserDef()
    {
    }

    public boolean open()
    {
        mDBDriver = new aliceCepToDB2Mysql();
        
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
