/************************************************
 * aliceCepToDB.java
 ************************************************/
package com.alice.cep;

abstract class aliceCepTo
{
    final static public String mTag4File    = "file:";
    static final public String mTag4Unknown = "??:";
    static final public String mTag4Term    ="term:";
    static final public String mTag4DB      ="db:";
    static final public String mTag4Sock    ="sock:";

    public final static String mTag4Class = "class=";
    public final static String mTag4Name  = "name=";
    public final static String mTag4Path  = "path=";

    public final static int    mLen4Class = 6;
    public final static int    mLen4Name  = 5;
    public final static int    mLen4Path  = 5;

    public final static int mCepToUnknown = 0;
    public final static int mCepToFile    = 1;
    public final static int mCepToTerm    = 2;
    public final static int mCepToDB      = 3;
    public final static int mCepToSock    = 4;

    public aliceCepTo()
    {
    };

    // Definitions of abstract function.
    abstract public int     getType();
    abstract public boolean write( String []aDatas );
    abstract public String  toString();
    abstract public void    close();
    abstract public boolean open();

    static public aliceCepTo getInstance( String aCepToString )
    {
    	int        sCepToType = mCepToUnknown;
    	aliceCepTo   sCepTo     = null;
    	String   []sArgs      = null;
    	String     sArgVal    = null;
    	String     sFileName  = "";
    	String     sPath      = "";
    	String     sClassName = "";
    	if( aCepToString.startsWith( aliceCepTo.mTag4File ) == true )
    	{
    		sCepToType = mCepToFile;
    		sArgs = ( aCepToString.substring( 5,
    				aCepToString.length() ) ).
    				split(",");
    		for( String sArg : sArgs )
    		{
    			if( sArg.startsWith( mTag4Name ) == true )
    			{
    				sFileName = sArg.substring( mLen4Name, sArg.length() );
    			}
    			else if( sArg.startsWith( mTag4Path ) == true )
    			{
    				sPath = sArg.substring( mLen4Path, sArg.length() );
    			}
    		}
    	}
    	else if( aCepToString.startsWith( aliceCepTo.mTag4Sock ) == true )
    	{
    		sCepToType = mCepToSock;
    	}
    	else if( aCepToString.startsWith( aliceCepTo.mTag4DB ) == true )
    	{
    		sCepToType = mCepToDB;

    		String sArg = aCepToString.substring( 3, aCepToString.length() );

    		if( sArg != null )
    		{
    			if( sArg.startsWith( mTag4Class ) == true )
    			{
    				sClassName = sArg.substring( mLen4Class, sArg.length() );
    			}
    		}
    	}
    	else if( aCepToString.startsWith( aliceCepTo.mTag4Term) == true )
    	{
    		sCepToType = mCepToTerm;
    	}

    	switch( sCepToType )
    	{
    	case mCepToFile:
    		sCepTo = (ccpCepTo)(new aliceCepToFile( sPath, sFileName ));
    		break;
    	case mCepToTerm:
    		sCepTo = (ccpCepTo)(new aliceCepToTerm());
    		break;
    	case mCepToDB:
    	case mCepToClass:

    		String []sArgs4Cls = sClassName.split(",");
    		System.out.println( "Case mCepToDB ("  + sArgs4Cls[1] + ")");
    		sCepTo = (ccpCepTo)(new ccpCepToDB( sArgs4Cls[1].substring(5,sArgs4Cls[1].length())));
    		if( sCepTo != null )
    		{
    			if( sCepTo.open() == false )
    			{
    				sCepTo = null;
    			}
    		}
    		break;
    	case mCepToSock:
    	case mCepToUnknown:
    	default:
    		break;
    	}
    	return sCepTo;
    }
    /*
       public static void main( String [] aArgs )
       {
       String sArg4DB    = "db:dbcon=1";
       String sArg4Term  = "term:";

       System.out.println( "Test for aliceCepToDB.....");
       aliceCepTo sCcpCepToDB= aliceCepTo.getInstance( sArg4DB );
       if( sCcpCepToDB != null )
       {
       System.out.println( sCcpCepToDB.toString() );
       System.out.println( "Type=" + sCcpCepToDB.getType() );
       }
       else
       {
       System.out.println( "Object is null.("+sArg4DB+")" );
       }
       System.out.println( "Test for aliceCepToTerm.....");
       aliceCepTo sCcpCepToTerm= aliceCepTo.getInstance( sArg4Term );
       if( sCcpCepToDB != null )
       {
       System.out.println( sCcpCepToTerm.toString() );
       System.out.println( "Type=" + sCcpCepToTerm.getType() );
       }
       else
       {
       System.out.println( "Object is null.("+ sArg4Term + ")" );
       }
       }
     */
}
