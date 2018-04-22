package com.alice.cep;

public class aliceEqlObject
{
    private int    mID;
    private String mMain;
    private String mFrom;
    private String mWhere;
    private String mTo;
    private Object mStmt;

    public aliceEqlObject( String aID,
                         String aMain,
                         String aFrom,
                         String aWhere,
                         String aTo,
                         Object aStmt )
    {
        mID    = Integer.parseInt( aID );
        mMain  = aMain;
        mFrom  = aFrom;
        mWhere = aWhere;
        mTo    = aTo;
        mStmt  = aStmt;
    }

    public void setTo( String aTo )
    {
        mTo = aTo;
    }

    public void setMain( String aMain )
    {
        mMain = aMain;
    }

    public void setFrom( String aFrom )
    {
        mFrom = aFrom;
    }

    public void setWhere( String aWhere )
    {
        mWhere = aWhere;
    }

    public void setID( int aID )
    {
        mID = aID;
    }

    public void setStmt( Object aStmt )
    {
        mStmt = aStmt;
    }

    public String getTo()
    {
        return mTo;
    }

    public String getMain()
    {
        return mMain;
    }

    public String getFrom()
    {
        return mFrom;
    }

    public String getWhere()
    {
        return mWhere;
    }

    public int getID()
    {
        return mID;
    }

    public Object getStmt()
    {
        return mStmt;
    }

    public String toString()
    {
        return "ccpEqlObject, " + mID    +
               ", "             + mMain  +
               " from "         + mFrom  +
               " where "        + mWhere +
               " to "           + mTo;
    }
}
