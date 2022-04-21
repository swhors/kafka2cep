/***************************************************
 * aliceCepDBUserDef.java
 ***************************************************/
package com.simpson.kafka2cep.service;

public interface aliceCepDBUserDef
{
    public abstract boolean open();
    public abstract void close();
    public abstract boolean insert( String[] aValues );
}

