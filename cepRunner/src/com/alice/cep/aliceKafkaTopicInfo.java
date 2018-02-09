/************************************************
 * aliceKafkaTopicInfo.java
 ************************************************/
package com.alice.cep;

public class aliceKafkaTopicInfo
{
    public String mTopic;
    public String mClassName;
    public String mDataType;
    
    public aliceKafkaTopicInfo( String aTopic, 
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
}
