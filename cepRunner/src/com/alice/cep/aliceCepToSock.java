package com.alice.cep;

public class aliceCepToSock extends aliceCepTo
{
	public aliceCepToSock( )
	{
	}
	public boolean write( String []aDatas)
	{
		for( String sData:aDatas )
		{            
			System.out.println( sData );
		}
		return true;
	}
	public boolean open()
	{
		return true;
	}
	public void close()
	{

	}
	public String toString()
	{
		return (String)"Object=ccpCepToSock";
	}
	public int getType()
	{
		return mCepToSock; 
	}
}
