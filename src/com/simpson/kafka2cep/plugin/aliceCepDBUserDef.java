package com.simpson.kafka2cep.plugin;

public interface aliceCepDBUserDef
{
    public abstract boolean open();
    public abstract void    close();
    public abstract boolean insert( String [] aValues );
}
