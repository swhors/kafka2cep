package com.simpson.kafka2cep.cep.to;

public class CepOutToFile extends CepOutTarget
{
	String mPath;
	String mFileName;
	public CepOutToFile( String aPath, String aFileName )
	{
		mPath     = aPath;
		mFileName = aFileName;
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
		return true;
	}
	public String toString()
	{ 
		return (String)"Object=ccpCepToFile,mPath=" + 
				mPath +
				",mFileName=" +
				mFileName;
	}
	public int getType()
	{
		return mCepToFile;
	}
}