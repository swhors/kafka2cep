package com.simpson.kafka2cep.cep.to;

public class CepOutToTerm extends CepOutTarget
{
	public CepOutToTerm()
	{
	}
	public boolean open()
	{
		return true;
	}
	public void close()
	{
	}
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
	public String toString()
	{
		return (String)"Object=ccpCepToTerm";
	}
	public int getType()
	{
		return mCepToTerm;
	}
}