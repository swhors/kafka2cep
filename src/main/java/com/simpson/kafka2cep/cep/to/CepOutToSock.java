package com.simpson.kafka2cep.cep.to;

public class CepOutToSock extends CepOutTarget
{
	public CepOutToSock( )
	{
	}
	@Override
	public boolean write( String []aDatas)
	{
		for( String sData:aDatas )
		{
			System.out.println( sData );
		}
		return true;
	}
	@Override
	public boolean open()
	{
		return true;
	}
	@Override
	public void close()
	{

	}
	@Override
	public String toString()
	{
		return "Object=ccpCepToSock";
	}
	@Override
	public int getType()
	{
		return mCepToSock;
	}
}
