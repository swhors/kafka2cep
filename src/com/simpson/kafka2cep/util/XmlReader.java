/***********************************************
 * aliceXmlReader.java
 ***********************************************/
package com.simpson.kafka2cep.util; 

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.simpson.kafka2cep.cep.EqlObject;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.OutputKeys;

public class XmlReader
{
    String mFilePath;
 
    static private final String mXmlTag4Root     = "CCP_CEP";
    static private final String mXmlTag4EQLLISTS = "EQL_LISTS";
    static private final String mXmlTag4EQL      = "EQL";
    static private final String mXmlTag4ID       = "ID";
    static private final String mXmlTag4Main     = "MAIN";
    static private final String mXmlTag4From     = "FROM";
    static private final String mXmlTag4Where    = "WHERE";
    static private final String mXmlTag4To       = "TO";
 
    //HashMap< String , EqlObject > mMap4Eql;
 
    public XmlReader( String aFilePath )
    {
        mFilePath = aFilePath;
    }
 
    static public boolean write( String aOutFileName, HashMap<String, EqlObject > aMap4Eql )
    {
        try
        {
            File sFile = new File ( aOutFileName );
            sFile.delete();
            System.out.println("Delete File : " + aOutFileName );
        }
        catch( Exception e )
        {
        }
 
        try
        {
            DocumentBuilderFactory docFactory
                        = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder
                        = docFactory.newDocumentBuilder();
 
            // 루트 엘리먼트
            Document sDoc      = docBuilder.newDocument();
            Element sRootElmnt = sDoc.createElement( mXmlTag4Root );
            sDoc.appendChild( sRootElmnt );
 
            // EQL_LISTS 엘리먼트
            Element sEqlListsElmnt = sDoc.createElement( mXmlTag4EQLLISTS );
            sRootElmnt.appendChild( sEqlListsElmnt );
 
            // SQL Element
            for(Map.Entry<String, EqlObject > sEntry : aMap4Eql.entrySet())
            {
                Element sEQL        = sDoc.createElement( mXmlTag4EQL  );
                Element sIDElmnt    = sDoc.createElement( mXmlTag4ID   );
                Element sMAINElmnt  = sDoc.createElement( mXmlTag4Main );
                Element sFROMElmnt  = sDoc.createElement( mXmlTag4From );
                Element sWHEREElmnt = sDoc.createElement( mXmlTag4Where );
                Element sTOElmnt    = sDoc.createElement( mXmlTag4To   );
 
                EqlObject sObject = sEntry.getValue();

                System.out.println( sObject.toString() );

                sIDElmnt.appendChild(    sDoc.createTextNode( sEntry.getKey() ) );
                sMAINElmnt.appendChild(  sDoc.createTextNode( sObject.getMain()  ) );
                sFROMElmnt.appendChild(  sDoc.createTextNode( sObject.getFrom()  ) );

                if( sObject.getWhere() == null )
                {
                    sWHEREElmnt.appendChild( sDoc.createTextNode( "" ) );
                }
                else
                {
                    sWHEREElmnt.appendChild( sDoc.createTextNode( sObject.getWhere() ) );
                }

                sTOElmnt.appendChild(    sDoc.createTextNode( sObject.getTo()    ) );
                sEQL.appendChild( sIDElmnt    );
                sEQL.appendChild( sMAINElmnt  );
                sEQL.appendChild( sFROMElmnt  );
                sEQL.appendChild( sWHEREElmnt );
                sEQL.appendChild( sTOElmnt    );
                sEqlListsElmnt.appendChild( sEQL );
            }

            TransformerFactory sTransformerFactory
                     = TransformerFactory.newInstance();
            Transformer sTransformer
                     = sTransformerFactory.newTransformer();

            sTransformer.setOutputProperty(OutputKeys.INDENT, "yes");

            sTransformer.setOutputProperty( "{http://xml.apache.org/xslt}indent-amount", "2" );

            DOMSource sSource = new DOMSource( sDoc );

            StreamResult sResult = new StreamResult( new File( aOutFileName ) );

            sTransformer.transform( sSource, sResult );
        }
        catch( ParserConfigurationException pce )
        {
            pce.printStackTrace();
        }
        catch( TransformerException tfe )
        {
            tfe.printStackTrace();
        }
        catch( Exception e )
        {
        }
        return true;
    }

    static public HashMap<String,EqlObject> read( String aInFileName )
    {
        HashMap< String, EqlObject> sMap4Eql
                      = new HashMap< String, EqlObject >();
        try
        {
            System.out.println( "enter..\n");
            File sFile = new File( aInFileName );
 
            DocumentBuilderFactory sDocBuildFact
                = DocumentBuilderFactory.newInstance();

            DocumentBuilder sDocBuild
                = sDocBuildFact.newDocumentBuilder();

            Document sDoc
                       = sDocBuild.parse( sFile );

            sDoc.getDocumentElement().normalize();

            NodeList sEQLLists = sDoc.getElementsByTagName( mXmlTag4EQLLISTS );

            if( sEQLLists.getLength() > 0 )
            {
                Node sEQLListsNode = sEQLLists.item(0);
 
                Element sEQLsElmnt = (Element) sEQLListsNode;
 
                NodeList sEQLNodeList = sEQLsElmnt.getElementsByTagName( mXmlTag4EQL );

                for( int b = 0; b < sEQLNodeList.getLength(); b++ )
                {
                    Node sEQLNode = sEQLNodeList.item(b);
                    if( sEQLNode.getNodeType() == Node.ELEMENT_NODE )
                    {
                        Element sEQLElmnt    = (Element) sEQLNode;
 
                        String   sID    = "";
                        String   sMain  = "";
                        String   sFrom  = "";
                        String   sWhere = "";
                        String   sTo    = "";
 
                        NodeList sIDList   
                              = sEQLElmnt.getElementsByTagName( mXmlTag4ID    );
                        NodeList sMainList
                              = sEQLElmnt.getElementsByTagName( mXmlTag4Main  );
                        NodeList sFromList
                              = sEQLElmnt.getElementsByTagName( mXmlTag4From  );
                        NodeList sWhereList
                              = sEQLElmnt.getElementsByTagName( mXmlTag4Where );
                        NodeList sToList
                              = sEQLElmnt.getElementsByTagName( mXmlTag4To    );
 
                        Element sIDElmnt    = (Element) sIDList.item(0);
                        Element sMainElmnt  = (Element) sMainList.item(0);
                        Element sFromElmnt  = (Element) sFromList.item(0);
                        Element sWhereElmnt = (Element) sWhereList.item(0);
                        Element sToElmnt    = (Element) sToList.item(0);
 
                        if( sIDElmnt != null )
                        {
                            NodeList sIDNode = sIDElmnt.getChildNodes();
                            if( sIDNode != null )
                            {
                                sID = (sIDNode.item(0)).getNodeValue();
                            }
                            else
                            {
                                sID = "";
                            }
                        }
                        else
                        {
                            sID = "";
                        }
 
                        if( sMainElmnt != null )
                        {
                            NodeList sMainNode = sMainElmnt.getChildNodes();

                            if( sMainNode != null )
                            {
                                sMain = (sMainNode.item(0)).getNodeValue();
                            }
                            else
                            {
                                sMain = "";
                            }
                        }
                        else
                        {
                            sMain = "";
                        }

                        if( sFromElmnt != null )
                        {
                            NodeList sFromNode = sFromElmnt.getChildNodes();
                            if( sFromNode != null )
                            {
                                sFrom = (sFromNode.item(0)).getNodeValue();
                            }
                            else
                            {
                                sFrom = "";
                            }
                        }
                        else
                        {
                            sFrom = "";
                        }
 
                        if( sWhereElmnt != null )
                        {
                            NodeList sWhereNode = sWhereElmnt.getChildNodes();
                            if( sWhereNode != null )
                            {
                                if( sWhereNode.getLength() > 0 )
                                {
                                    sWhere =(sWhereNode.item(0)).getNodeValue();
                                }
                                else
                                {
                                    sWhere = "";
                                }
                            }
                            else
                            {
                                 sWhere = "";
                            }
                        }
                        else
                        {
                            sWhere = "";
                        }

                        if( sToElmnt != null )
                        {
                            NodeList sToNode = sToElmnt.getChildNodes();

                            if( sToNode != null )
                            {
                                sTo = (sToNode.item(0)).getNodeValue();
                            }
                            else
                            {
                                sTo = "";
                            }
                        }
                        else
                        {
                            sTo = "";
                        }
 
                        System.out.println( "id="+sID +
                                            ",main="+sMain +
                                            ",from="+sFrom+
                                            ",where="+sWhere +
                                            ",to=" + sTo );

                        sMap4Eql.put( sID,
                                      new EqlObject( sID,
                                                        sMain,
                                                        sFrom,
                                                        sWhere,
                                                        sTo,
                                                        null ) );
                    }
                }
            }
        }
        catch( Exception e )
        {
            //if( sMap4Eql.size() != 0 )
            //{
            //    sMap4Eql.clear();
            //}
            e.printStackTrace();
        }
        return sMap4Eql;
    }
 
    public static void main(String aArgv[])
    {
        HashMap<String, EqlObject > sMap4Eql = null;
        String sInFileName = "backup/test.xml";
        String sOutFileName = "test1.xml";
 
        if( aArgv.length ==  1 )
        {
            sInFileName = aArgv[0];
        }
        else if( aArgv.length == 2 )
        {
            sInFileName  = aArgv[0];
            sOutFileName = aArgv[1];
        }
 
        sMap4Eql = XmlReader.read(  sInFileName  );
        XmlReader.write( sOutFileName, sMap4Eql );
    }
}
