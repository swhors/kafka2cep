/************************************************
 * CepOutToDB.java
 ************************************************/
package com.simpson.kafka2cep.cep.to;

public abstract class CepOutTarget
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

    public CepOutTarget()
    {
    }

    // Definitions of abstract function.
    abstract public int     getType();
    abstract public boolean write( String []aDatas );
    @Override
	abstract public String  toString();
    abstract public void    close();
    abstract public boolean open();

    static public CepOutTarget getInstance( String aCepToString )
    {
    	int        sCepToType = mCepToUnknown;
    	CepOutTarget   sCepTo     = null;
    	String   []sArgs      = null;
    	String     sFileName  = "";
    	String     sPath      = "";
    	String     sClassName = "";
    	if( aCepToString.startsWith( CepOutTarget.mTag4File ) )
    	{
    		sCepToType = mCepToFile;
    		sArgs = ( aCepToString.substring( 5,
    				aCepToString.length() ) ).
    				split(",");
    		for( String sArg : sArgs )
    		{
    			if( sArg.startsWith( mTag4Name ) )
    			{
    				sFileName = sArg.substring( mLen4Name, sArg.length() );
    			}
    			else if( sArg.startsWith( mTag4Path ) )
    			{
    				sPath = sArg.substring( mLen4Path, sArg.length() );
    			}
    		}
    	}
    	else if( aCepToString.startsWith( CepOutTarget.mTag4Sock ) )
    	{
    		sCepToType = mCepToSock;
    	}
    	else if( aCepToString.startsWith( CepOutTarget.mTag4DB ) )
    	{
    		sCepToType = mCepToDB;

    		String sArg = aCepToString.substring( 3, aCepToString.length() );

    		if( sArg != null )
    		{
    			if( sArg.startsWith( mTag4Class ) )
    			{
    				sClassName = sArg.substring( mLen4Class, sArg.length() );
    			}
    		}
    	}
    	else if( aCepToString.startsWith( CepOutTarget.mTag4Term) )
    	{
    		sCepToType = mCepToTerm;
    	}

    	switch( sCepToType )
    	{
    	case mCepToFile:
    		sCepTo = (new CepOutToFile( sPath, sFileName ));
    		break;
    	case mCepToTerm:
    		sCepTo = (new CepOutToTerm());
    		break;
    	case mCepToDB:
    	//case mCepToClass:
    		String []sArgs4Cls = sClassName.split(",");
    		System.out.println( "Case mCepToDB ("  + sArgs4Cls[1] + ")");
    		sCepTo = (new CepOutToDB( sArgs4Cls[1].substring(5,sArgs4Cls[1].length())));
    		if( sCepTo != null )
    		{
    			if( !sCepTo.open() )
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

       System.out.println( "Test for CepOutToDB.....");
       CepOutTarget saliceCepToDB= CepOutTarget.getInstance( sArg4DB );
       if( saliceCepToDB != null )
       {
       System.out.println( saliceCepToDB.toString() );
       System.out.println( "Type=" + saliceCepToDB.getType() );
       }
       else
       {
       System.out.println( "Object is null.("+sArg4DB+")" );
       }
       System.out.println( "Test for CepOutToTerm.....");
       CepOutTarget saliceCepToTerm= CepOutTarget.getInstance( sArg4Term );
       if( saliceCepToDB != null )
       {
       System.out.println( saliceCepToTerm.toString() );
       System.out.println( "Type=" + saliceCepToTerm.getType() );
       }
       else
       {
       System.out.println( "Object is null.("+ sArg4Term + ")" );
       }
       }
     */
}
