/***************************************************
 * aliceCepDBUserDef.java
 ***************************************************/
package com.kafka2esper.service;

public interface aliceCepDBUserDef
{
    public abstract boolean open();
    public abstract void close();
    public abstract boolean insert( String[] aValues );
}
