/***********************************************
 * CepEvent.java
 * 
 * Author : swhors@naver.com
 * 
 ***********************************************/

package com.simpson.kafka2cep.cep.event;

abstract public class CepEvent
{
    public CepEvent()
    {
    }
    
    abstract public boolean  setValues( String[] aArgv );
    abstract public int      length();
    abstract public String   toString();
    abstract public String[] getValues();
}
