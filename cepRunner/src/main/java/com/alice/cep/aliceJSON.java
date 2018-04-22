package com.alice.cep;

import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader; 
import java.util.*;
import java.util.regex.*; 

public class aliceJSON
{

    public aliceJSON()
    {
    }

    static final public String mExample =
          "{\"name\": smith,\"id\": 1}\n" +
          "{\"name\": james,\"id\": 2+3-1*4=1}\n" +
          "{\"name\": rose,\"id\": \"sip:swhors@naver.com\r\ncodec:g729,g7231\"}\n"+
          "{\"name\": jake,\"id\": 2/3=1?simpson@sss}\n";

    static final public String mSpecialChar =
                  " "     +
                  ","     +
                  "\\."   +
                  "_"     +
                  "\\\'"       +
                  "="         +
                  //"\\"       +
                  "@"        +
                  "\\+"      +
                  "\\-"      +
                  "\\*"      +
                  "/"         +
                  "\\n"      +
                  "\\r"      +
                  "\\f"      +
                  "\\t"      +
                  "\\x0B" +
                  "\\?"     +
                  //"\\\\." +
                  "\\^"    +
                  "\\$"    +
                  "\\#"    +
                  "\\!"     +
                  "\\~"   +
                  "\\`"    +
                  "\\&"   +
                  "<"     +
                  ">"     +
                  "\\|"    +
                  ";"      +
                  ":";    //\t\n\x0B\f\r

    static final public String mSpecialChar2 = mSpecialChar + "\"";

    static final public String mSpecialCharExt = mSpecialChar2 + "\\)" +"\\("; 

    static final public String mPattern4JSONLine = "\\{([a-zA-Z0-9" +
                                                                                mSpecialCharExt +
                                                                                "]*)\\}"; 
    static public String[] getLines( String aLine )
    {
        boolean sDebug = false;
        String [] sLines = null;

        if( sDebug == true )
        {
            System.out.println( "Pattern   = " + mPattern4JSONLine );
            System.out.println( "aLine     = " + aLine );
        }

        Pattern mLinePattern = Pattern.compile( mPattern4JSONLine );

        Matcher mLineMatcher = mLinePattern.matcher( aLine );

        while( mLineMatcher.find() )
        {
            System.out.println( "-0- " + mLineMatcher.group(0) );
        }
        return sLines;
    }

    static final public String mPattern4JSONField =
                                                    "\"([a-zA-Z0-9 ])*\":" +
                                                    "\"([a-zA-Z_0-9\\ "       +
                                                    mSpecialChar           +
                                                    "]*)\"";

    static public String[] getValues( String aLines , int aItemLength ) throws Exception
    {
        String[]   sValues   = null;
        boolean sDebug   = false;
        int          sCurItem = 0;

        if( sDebug == true )
        {
            System.out.println( "Length    = " + aItemLength );
            System.out.println( "Pattern   = " + mPattern4JSONField );
            System.out.println( "aLine     = " + aLines );
        }
     
        sValues = new String[aItemLength];

        Pattern mLinePattern = Pattern.compile( mPattern4JSONField,
                                                                          Pattern.COMMENTS);

        Matcher mLineMatcher = mLinePattern.matcher( aLines );

        while( mLineMatcher.find() )
        {
            if( sDebug == true )
            {
                System.out.println( "-1- " + mLineMatcher.group(1) );
                System.out.println( "-2- " + mLineMatcher.group(2) );
            }
            sValues[sCurItem++] = mLineMatcher.group(2);
            if( sCurItem == aItemLength )
            {
                break;
            }
        }

        if( sCurItem != aItemLength )
        {
            throw new Exception( "Illegal data.[ Wanted Data= " +
                                 aItemLength +
                                 " : Readed Data = " +
                                 sCurItem +
                                 "]" );
        }
        return sValues;
    }
    
    static public void main( String []aArgv )
    {
        FileReader sFileReader = null; 
        File       sFile       = null;

        if( aArgv.length != 2 )
        {
            System.out.println( "Usage : \n" +
                    "  java aliceJSON [file name] [data item number]\n" );
             aliceJSON.getLines( mExample );
        }
        else
        {
            try
            {
                sFile   = new File( aArgv[0] );
                int sItemCnt = Integer.parseInt( aArgv[1] );
                if( sFile.exists() == true )
                {
                    String sLine;
                    long   sDataSize = sFile.length();
                    char sLines[] = new char[(int)sDataSize];
                    sFileReader = new FileReader( aArgv[0] );
                    if( sFileReader.read( sLines ) > 0 )
                    {
                        String []sFields = null;
                        sLine = new String( sLines );

                        Pattern mLinePattern = Pattern.compile( aliceJSON.mPattern4JSONLine );
                        Matcher mLineMatcher = mLinePattern.matcher( sLine );

                        while( mLineMatcher.find() )
                        {
                            String sLineField = mLineMatcher.group(1);
                            int       sCnt          = 0;
                            if( sLineField.length() > 0 )
                            {
                                System.out.println("Parsed -- " + sLineField );
                                sFields = aliceJSON.getValues( sLineField , sItemCnt );
                                if( sFields != null )
                                {
                                    for( String sField : sFields )
                                    {
                                        System.out.println( sCnt++ +"'s = " + sField );
                                    }
                                }
                                else
                                {
                                    System.out.println( "Illegal data...");
                                }
                            }
                        }
                        //ccpJSON.getValues( sLine );
                    }
                    sFileReader.close();
                }
            }
            catch( Exception e )
            {
                System.out.println( "Exception : " + e );
            }
            finally
            {
            }
        }
    }
}
