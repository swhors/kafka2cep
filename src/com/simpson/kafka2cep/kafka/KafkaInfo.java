/************************************************
 * KafkaInfo.java
 ************************************************/
package com.simpson.kafka2cep.kafka;

abstract public class KafkaInfo
{
    private String mTopic;
    private String mObjClassName;
    private Object mObjClass;

    public KafkaInfo()
    {
        mTopic = "";
        mObjClassName = "";
        mObjClass  = null;
    }

    public String getTopic()
    {
        return mTopic;
    }

    public String getObjClassName()
    {
        return mObjClassName;
    }

    public Object getObjClass()
    {
        return mObjClass;
    }

    public void setTopic( String aTopic )
    {
        mTopic = aTopic;
    }

    public void setObjClassName( String aObjClassName )
    {
        mObjClassName = aObjClassName;
    }

    public void setObjClass( Object aObjClass )
    {
        mObjClass = aObjClass;
    }

    @Override
	abstract public String toString();
}
