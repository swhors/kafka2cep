/********************************************
 * aliceCepDBInterface.java
 ********************************************/
package com.simpson.kafka2cep.service;

import java.sql.SQLException;

public interface aliceCepDBInterface
{
    public abstract boolean open() throws SQLException;
    public abstract boolean open( String aHost,
                                  int    aPort,
                                  String aDBUser,
                                  String aDBPasswd,
                                  String aDBName,
                                  String aQuery );
    public abstract void    close();
    public abstract boolean insert( String[] aValues);
    public abstract boolean isConnected();
}
