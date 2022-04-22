/************************************************
 * KafkaTopicInfo.java
 ************************************************/
package com.simpson.kafka2cep.kafka;

public class KafkaTopicInfo
{
    public String mTopic;
    public String mClassName;
    public String mDataType;
    
    public KafkaTopicInfo( String aTopic, 
                              String aClassName,
                              String aDataType )
    {
        mTopic = aTopic;
        mClassName = aClassName;
        mDataType = aDataType;
    }

    @Override
    public String toString()
    {
        return "ccpKafkaTopicInfo, topic=" + mTopic +
               ", className=" + mClassName +
               ",datatype=" + mDataType ;
    }
    
    public String getClassName()
    {
        return mClassName;
    }

    public void setClassName(String aClassName)
    {
        mClassName = aClassName;
    }
}
