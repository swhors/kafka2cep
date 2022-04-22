package com.simpson.kafka2cep.cep.to;

public class CepOutToTerm extends CepOutTarget
{
	public CepOutToTerm()
	{
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
	public boolean write( String []aDatas)
	{
		StringBuffer sTermData = new StringBuffer();
		for( String sData:aDatas )
		{
			sTermData.append(sData).append(" ");
		}
		System.out.println( sTermData );
		return true;
	}
	@Override
	public String toString()
	{
		return "Object=ccpCepToTerm";
	}
	@Override
	public int getType()
	{
		return mCepToTerm;
	}
}