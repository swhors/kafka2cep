/************************************************
 * aliceKafkaInfo.java
 ************************************************/
package com.kafka2esper.model;

import java.util.*;
import java.util.regex.*;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Properties;

import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;

import com.espertech.esper.client.Configuration;
import com.espertech.esper.client.EPServiceProvider;
import com.espertech.esper.client.EPServiceProviderManager;
import com.espertech.esper.client.EPStatement;

abstract public class aliceKafkaInfo
{
    private String mTopic;
    private String mObjClassName;
    private Object mObjClass;

    public aliceKafkaInfo()
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

    abstract public String toString();
}
