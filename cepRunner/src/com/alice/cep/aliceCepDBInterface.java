/********************************************
 * aliceCepDBInterface.java
 ********************************************/
package com.alice.cep;

import java.sql.SQLException;

interface aliceCepDBInterface
{
    public abstract boolean open() throws SQLException;
    public abstract boolean open( String aHost,
                                  int    aPort,
                                  String aDBUser,
                                  String aDBPasswd,
                                  String aDBName,
                                  String aQuery );
    public abstract void close();
    public abstract boolean insert( String[] aValues);
    public abstract boolean isConnected();
}
