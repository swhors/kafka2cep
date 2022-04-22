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
		return true;
	}
	@Override
	public String toString()
	{
		return "Object=ccpCepToFile,mPath=" +
				mPath +
				",mFileName=" +
				mFileName;
	}
	@Override
	public int getType()
	{
		return mCepToFile;
	}
}