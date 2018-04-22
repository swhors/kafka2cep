/************************************************
 * aliceCepToDB.java
 ************************************************/
package com.alice.cep;

import java.sql.*;

public class aliceCepToDB extends aliceCepTo
{
    private String mDBClassName;
    public  Class<?> mDBClass     = null;
    public  aliceCepDBUserDef mDB = null;
    
    public aliceCepToDB( String aDBClassName )
    {
        mDBClassName = aDBClassName;
    }
    
    public boolean open()
    {
        boolean sRet = false;
        try
        {
            System.out.println("ClassName = " + mDBClassName );
            if( mDBClassName != null && mDBClassName.length() >  0 )
            {
                mDBClass = Class.forName( mDBClassName );
                if( mDBClass != null )
                {
                    mDB = (aliceCepDBUserDef) mDBClass.newInstance();
                    if( mDB != null )
                    {
                        sRet = mDB.open();
                    }
                    else
                    {
                        System.out.println("error : fail to connect to db. (" + 
                                           mDBClassName + ")" );
                    }
                }
                else
                {
                    System.out.println( "error: can't find class. (" + 
                                        mDBClassName + ")" );
                }
            }
            else
            {
                System.out.println( "error : illegal dbClassName. (" +
                                    mDBClassName + ")" );
            }
        }
        catch( Exception e )
        {
            e.printStackTrace();
            sRet = false;
        }

        return sRet;
    }
    
    public void close()
    {
        try
        {
            if( mDB != null )
            {
                mDB.close();
            }
        }
        catch( Exception e )
        {
            System.out.println( "Exception : " + e );
        }
    }
    
    public boolean write( String[] aDatas )
    {
        boolean sRet = false;
        System.out.println( "Write : " + aDatas[0] );

        if( mDB != null )
        {
             sRet = mDB.insert( aDatas );
        }
        else
        {
            System.out.println( "mDB is null.\n");
        }
        return sRet;
    }

    public String toString()
    {
        return ("Object=aliceCepToDB, mDBClassName=" + mDBClassName );
    }

    public String getDBClassName()
    {
        return mDBClassName;
    }

    public void setDBClassName( String aDBClassName )
    {
        mDBClassName = aDBClassName;
    }

    public int getType()
    {
        return mCepToDB;
    }
}
