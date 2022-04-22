package com.simpson.kafka2cep.http;
/***************************************************
 * HttpServer.java
 ***************************************************/
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executors;

import org.apache.log4j.Logger;

import com.simpson.kafka2cep.cep.EPLRunner;
import com.simpson.kafka2cep.cep.EqlObject;
import com.simpson.kafka2cep.cep.to.CepOutTarget;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

public class SimpleHttpServer
{
    final private String mTag4Command   = "command";
    final private String mTag4ID        = "id";
    final private String mTag4Main      = "main";
    final private String mTag4From      = "from";
    final private String mTag4Where     = "where";
    final private String mTag4To        = "to";
    final private String mTag4DBInfo    = "dbinfo";

    final private String mTag4CmdAdd        = "start";
    final private String mTag4CmdDel        = "stop";
    final private String mTag4CmdShowEqlLst = "showeqllist";
    final private String mTag4CmdAddDB      = "adddb";

    class aliceHttpServerException extends Exception
    {
        public String msg=null;
        public aliceHttpServerException(String msg)
        {
            this.msg = msg;
        }
    }

    public Logger mLog = Logger.getLogger( this.getClass() );

    public SimpleHttpServer()
    {
    }

    class aliceHttpHandler implements HttpHandler
    {
        @Override
		public void handle(HttpExchange exchange) throws IOException
        {
            String requestMethod
                = exchange.getRequestMethod();
            System.out.println("aliceHttpHandler : " + requestMethod );

            if( requestMethod.equalsIgnoreCase( "GET" ) )
            {
                Headers responseHeaders
                    = exchange.getResponseHeaders();

                System.out.println("handle="+requestMethod );
                responseHeaders.set("Content-Type", "text/plain");

                exchange.sendResponseHeaders(200, 0);

                OutputStream responseBody = exchange.getResponseBody();
                Headers requestHeaders = exchange.getRequestHeaders();

                Set<String> keySet = requestHeaders.keySet();

                Iterator<String> iter = keySet.iterator();

                while (iter.hasNext())
                {
                    String key = iter.next();
                    List values = requestHeaders.get(key);
                    String s = key + " = " + values.toString() + "\n";
                    responseBody.write(s.getBytes());
                }
                responseBody.close();
            }
        }
    }

    // http://localhost:8000/info
    class aliceInfoHandler implements HttpHandler
    {
        @Override
		public void handle(HttpExchange aHttpExchange) throws IOException
        {
            String response = "Use /get?hello=word&foo=bar to see how to handle url parameters";
            SimpleHttpServer.writeResponse(aHttpExchange, response.toString());
            System.out.println("InfoHandler : " + response );
        }
    }

    class aliceGetHandler implements HttpHandler
    {
        @Override
		public void handle( HttpExchange aHttpExchange ) throws IOException
        {
            boolean  sRet      = false;
            boolean  sDebug    = false;
            CepOutTarget sCcpCepTo = null;

            StringBuilder sResponse = new StringBuilder();

            Map <String,String> sParams
                = SimpleHttpServer.queryToMap(aHttpExchange.getRequestURI().getQuery());

            String sCommand   = sParams.get( mTag4Command );
            String sID        = sParams.get( mTag4ID      );
            String sMain      = sParams.get( mTag4Main    );
            String sFrom      = sParams.get( mTag4From    );
            String sWhere     = sParams.get( mTag4Where   );
            String sTo        = sParams.get( mTag4To      );
            String sResult  = "success";

            try
            {
                if( sCommand == null )
                {
                    throw new aliceHttpServerException("Command is null.");
                }

                if( sCommand.equals( mTag4CmdAdd ) )
                {
                    if( sID == null )
                    {
                        throw new aliceHttpServerException("ID is null.");
                    }
                    else if( sMain == null )
                    {
                        throw new aliceHttpServerException("main is null.");
                    }
                    else if( sFrom == null )
                    {
                        throw new aliceHttpServerException("from is null.");
                    }
                    else if( sTo == null )
                    {
                        throw new aliceHttpServerException("to is null.");
                    }
                }
                else if( sCommand.equals( mTag4CmdDel ) )
                {
                    if( sID == null )
                    {
                        throw new aliceHttpServerException("ID is null.");
                    }
                }

                if( sDebug )
                {
                    System.out.println( "command=" + sCommand +
                                        ", id="    + sID      +
                                        ", eql="   + sMain    +
                                        " from "   + sFrom    +
                                        " where "  + sWhere   +
                                        " to "     + sTo );
                }

                if( sCommand.equals( mTag4CmdShowEqlLst ) )
                {
                    showEPLService( aHttpExchange );
                 }
                else if( sCommand.equals( mTag4CmdAdd ) )
                {
                    addEPLService( sID,
                                   sMain,
                                   sFrom,
                                   sWhere,
                                   sTo,
                                   aHttpExchange );
                }
                else if( sCommand.equals( mTag4CmdDel ) )
                {
                    sRet = delEPLService( sID,
                                          aHttpExchange );
                    if( sRet )
                    {
                        System.out.println("Success to remove service.\n");
                    }
                    else
                    {
                        System.out.println("Fail to remove service.\n");
                    }
                }
                else
                {
                    System.out.println("illegal command.\n");
                    sResult = "fail : illefal command";
                    sendExceptionError( aHttpExchange, sResult );
                }
            }
            catch( aliceHttpServerException e)
            {
                sResult = "Error : " + e.msg;
                sendExceptionError( aHttpExchange, sResult );
            }
            catch( Exception e )
            {
                sResult = "Error : " + e;
                sendExceptionError( aHttpExchange, sResult );
            }
        }
    }
    public void sendExceptionError( HttpExchange aHttpExchange, String       aMessage )
    {
        StringBuilder sResponse = new StringBuilder();
        try
        {
            sResponse.append("<html><body>\n");
            sResponse.append("<h3>error</h3><br/>\n");
            sResponse.append("<h4>msg:"+ aMessage +"</h4><br/>\n");
            sResponse.append("</body></html>");
            SimpleHttpServer.writeResponse( aHttpExchange, sResponse.toString());
        }
        catch( IOException e )
        {
            System.out.println( "Exception : showEPLService (" + e + ")" );
        }
    }

    public void addDBInformation( HttpExchange aHttpExchange, String aDBInfo )
    {
        StringBuilder sResponse = new StringBuilder();
        try
        {
            sResponse.append("<html><body>\n");
            sResponse.append("<h3>add db information</h3><br/>\n");
            sResponse.append("</body></html>");
            SimpleHttpServer.writeResponse( aHttpExchange, sResponse.toString());
        }
        catch( IOException e )
        {
            System.out.println( "Exception : addDBInformation(" + e + ")" );
        }
    }

    public void showEPLService( HttpExchange aHttpExchange )
    {
        StringBuilder sResponse = new StringBuilder();
        try
        {
            sResponse.append( "<html><body>\n" );
            sResponse.append( "<h3>eql list</h3><br/>\n" );
            sResponse.append( "<h4>Total:" +
                                           EPLRunner.mMap4Eql.size() +
                                            "</h4><br/>\n" );
            for( Map.Entry<String,EqlObject> entry :
                               EPLRunner.mMap4Eql.entrySet() )
            {
                sResponse.append( "<h4>List: " +
                                  (entry.getValue()).toString() +
                                  "</h4><br/>\n" );
            }
            sResponse.append("</body></html>");
            SimpleHttpServer.writeResponse( aHttpExchange,
                                         sResponse.toString() );
        }
        catch( IOException e )
        {
            System.out.println( "Exception : showEPLService (" + e + ")" );
        }
    }

    public boolean addEPLService( String       aID,
                                  String       aMain,
                                  String       aFrom,
                                  String       aWhere,
                                  String       aTo,
                                  HttpExchange aHttpExchange )
    {
        boolean       sRet      = false;
        StringBuilder sResponse = new StringBuilder();
        System.out.println("addEPLService..\n" );
        try
        {
            sRet = EPLRunner.runEPL( aID, aMain, aFrom, aWhere, aTo );
            sResponse.append("<html><body>\n");
            sResponse.append("<h4>command : start/h4><br/>\n");
            sResponse.append("<h4>result  : " + sRet + "</h4><br/>\n");
            sResponse.append("</body></html>");
            writeResponse( aHttpExchange, sResponse.toString());
        }
        catch( IOException e )
        {
            System.out.println( "IOException : addEPLService (" + e + ")" );
        }
        catch( Exception e )
        {
            System.out.println( "EPLException : addEPLService (" + e + ")" );
        }
        return sRet;
    }

    public boolean delEPLService( String       aID,
                                  HttpExchange aHttpExchange )
    {
        boolean sRet = false;
        StringBuilder sResponse = new StringBuilder();

        if( aID != null )
        {
             sRet = EPLRunner.stopEPL( aID );
        }

        try
        {
            sResponse.append("<html><body>\n");
            sResponse.append("<h4>command : stop/h4><br/>\n");
            sResponse.append("<h4>result  : " + sRet + "</h4><br/>\n");
            sResponse.append("</body></html>");
            writeResponse( aHttpExchange, sResponse.toString());
        }
        catch( IOException e )
        {
            System.out.println( "Exception : addEPLService (" + e + ")" );
        }

        return sRet;
    }

    public boolean run( int aPort ) throws IOException
    {
        InetSocketAddress addr = new InetSocketAddress( aPort );
        HttpServer server = HttpServer.create( addr, 0 );
        server.createContext("/get",  new aliceGetHandler()  );
        server.createContext("/info", new aliceInfoHandler() );
        server.createContext("/",     new aliceHttpHandler() );
        server.setExecutor(Executors.newCachedThreadPool() );
        server.start();
        System.out.println("Server is listening on port " + aPort +"." );

        mLog.info( "Server is listening on port " + aPort + "." );

        return true;
    }

    public static void writeResponse( HttpExchange aHttpExchange,
            String       response) throws IOException
    {
        aHttpExchange.sendResponseHeaders(200, response.length());
        OutputStream os = aHttpExchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }

    /**
     * returns the url parameters in a map
     * @param query
     * @return map
     */
    public static Map<String, String> queryToMap(String aQuery)
    {
        Map<String, String> sResult = new HashMap<String, String>();
        for( String sParam : aQuery.split("&") )
        {
            int sIndexOfSep = sParam.indexOf( "=", 0 );

            if( sIndexOfSep > 0 )
            {
                String sKey = sParam.substring( 0, sIndexOfSep );
                String sValue = sParam.substring( sIndexOfSep + 1, sParam.length() );
                sResult.put(sKey, sValue );
            }
            else
            {
                sResult.put(sParam, "");
            }
        }
        return sResult;
    }
}

