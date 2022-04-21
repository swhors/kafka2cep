package com.simpson.kafka2cep.cep;

import java.util.regex.*;

public class aliceCepUtil
{
    /******************************************************
     * getMatchedCount     
     *  : return count of mached pattern string.
     *
     *  aText    [IN] - Original text.
     *  aPattern [IN] - Pattern text.
     ******************************************************/
    static public int getMatchedCount( String aText , String aPattern )
    {
        int aMatchedCount = 0;
        Pattern sPattern = Pattern.compile( aPattern );
        Matcher sMatcher = sPattern.matcher( aText );
        while( sMatcher.find() )
        {
            aMatchedCount++;
        }
        return aMatchedCount;
    }
}
