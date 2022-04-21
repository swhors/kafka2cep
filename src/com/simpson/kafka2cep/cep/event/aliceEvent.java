/***********************************************
 * aliceEvent.java :
 ***********************************************/

package com.simpson.kafka2cep.cep.event;

abstract public class aliceEvent
{
    public aliceEvent()
    {
    }
    
    abstract public boolean  setValues( String[] aArgv );
    abstract public int      length();
    abstract public String   toString();
    abstract public String[] getValues();
}
