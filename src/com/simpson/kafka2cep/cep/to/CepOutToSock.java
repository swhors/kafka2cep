package com.simpson.kafka2cep.cep.to;

public class CepOutToSock extends CepOutTarget
{
	public CepOutToSock( )
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
