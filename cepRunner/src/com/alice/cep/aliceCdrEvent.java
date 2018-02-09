/***********************************************
 * aliceCdrEvent.java :
 ***********************************************/
package com.alice.cep;


public class aliceCdrEvent extends aliceEvent
{
    final int mCdrItmNum = 25;

    private String Date;              // date
    private String CallType;          // callType;
    private String SourceDevice;      // sourceDevice
    private String SourceRealm;       // sourceRealm
    private String DestinationDevice; // destinationDevice
    private String DestinationRealm;  // destinationRealm
    private String Direction;         // direction
    private String Caller;            // caller
    private String Carrier4Caller;    // carrier4Caller
    private String CallerNetworkType; // callerNetworkType
    private String Callee;            // callee
    private String Carrier4Callee;    // carrier4Callee
    private String CalleeNetworkType; // calleeNetworkType
    private int    Duration;          // duration
    private int    TCCode;
    private String TCReason;
    private long   StartCallTime;
    private long   EndCallTime;
    private String UserAgentInfo;
    private String RegiNumber;
    private String LastCallee;
    private String BillNumber;
    private String CID;
    private String Info;
    private String VMName;

    public aliceCdrEvent()
    {

    }

    public boolean setValues( String []aArgv )
    {
        int sCnt = 0;
        if( aArgv.length < 24 ||
            aArgv.length > 25 )
        {
            return false;
        }
        Date              = aArgv[sCnt++];
        CallType          = aArgv[sCnt++];
        SourceDevice      = aArgv[sCnt++];
        SourceReam        = aArgv[sCnt++];
        DestinationDevice = aArgv[sCnt++];
        DestinationRealm  = aArgv[sCnt++];
        Direction         = aArgv[sCnt++];
        Caller            = aArgv[sCnt++];
        Carrier4Caller    = aArgv[sCnt++];
        CallerNetworkType = aArgv[sCnt++];
        Callee            = aArgv[sCnt++];
        Carrier4Callee    = aArgv[sCnt++];
        CalleeNetworkType = aArgv[sCnt++];
        Duration          = Integer.parseInt( aArgv[sCnt++], 10 );
        TCCode            = Integer.parseInt( aArgv[sCnt++], 10 );
        TCReason          = aArgv[sCnt++];
        StartCallTime     = Long.parseLong( aArgv[sCnt++], 10 );
        EndCallTime       = Long.parseLong( aArgv[sCnt++], 10 );
        UserAgentInfo     = aArgv[sCnt++];
        RegiNumber        = aArgv[sCnt++];
        LastCallee        = aArgv[sCnt++];
        BillNumber        = aArgv[sCnt++];
        CID               = aArgv[sCnt++];
        Info              = aArgv[sCnt++];
        if( aArgv.length == 25 )
        {
            VMName        = aArgv[sCnt++];
        }
        else
        {
            VMName        = "";
        }
        return true;
    }

    public int length()
    {
        return mCdrItmNum;
    }

    public String getDate()
    {
        return Date;
    }

    public String getCallType()
    {
        return CallType;
    }

    public String getSourceDevice()
    {
        return SourceDevice;
    }

    public String getSourceRealm()
    {
        return SourceRealm;
    }

    public String getDestinationDevice()
    {
        return DestinationDevice;
    }

    public String getDestinationRealm()
    {
        return DestinationRealm;
    }

    public String getDirection()
    {
        return Direction;
    }

    public String getCaller()
    {
        return Caller;
    }

    public String getCarrier4Caller()
    {
        return Carrier4Caller;
    }

    public String getCallerNetworkType()
    {
        return CallerNetworkType;
    }

    public String getCallee()
    {
        return Callee;
    }

    public String getCarrier4Callee()
    {
        return Carrier4Callee;
    }

    public String getCalleeNetworkType()

    {
        return CalleeNetworkType;
    }

    public int getDuration()
    {
        return Duration;
    }

    public int getTCCode()
    {
        return TCCode;
    }

    public String getTCReason()
    {
        return TCReason;
    }

    public long getStartCallTime()
    {
        return StartCallTime;
    }

    public long getEndCallTime()
    {
        return EndCallTime;
    }

    public String getUserAgentInfo()
    {
        return UserAgentInfo;
    }

    public String getRegiNumber()
    {
        return RegiNumber;
    }

    public String getLastCallee()
    {
        return LastCallee;
    }

    public String getBillNumber()
    {
        return BillNumber;
    }

    public String getCID()
    {
        return CID;
    }

    public String getInfo()
    {
        return Info;
    }

    public String getVMName()
    {
        return VMName;
    }

    public String[] getValues()
    {
        int sCnt = 0;

        String [] sValues = new String[mCdrItemNum];
        
        aArgv[sCnt++] = Date;
        aArgv[sCnt++] = CallType;
        aArgv[sCnt++] = SourceDevice;
        aArgv[sCnt++] = SourceReam;
        aArgv[sCnt++] = DestinationDevice;
        aArgv[sCnt++] = DestinationRealm;
        aArgv[sCnt++] = Direction;
        aArgv[sCnt++] = Caller;
        aArgv[sCnt++] = Carrier4Caller;
        aArgv[sCnt++] = CallerNetworkType;
        aArgv[sCnt++] = Callee;
        aArgv[sCnt++] = Carrier4Callee;
        aArgv[sCnt++] = CalleeNetworkType;
        aArgv[sCnt++] = Duration;
        aArgv[sCnt++] = TCCode;
        aArgv[sCnt++] = TCReason;
        aArgv[sCnt++] = StartCallTime;
        aArgv[sCnt++] = EndCallTime;
        aArgv[sCnt++] = UserAgentInfo;
        aArgv[sCnt++] = RegiNumber;
        aArgv[sCnt++] = LastCallee;
        aArgv[sCnt++] = BillNumber;
        aArgv[sCnt++] = CID;
        aArgv[sCnt++] = Info;
        aArgv[sCnt++] = VMName;
    }

    public String toString()
    {
        return "{ccpCdrEvent, Item=" +mCdrItemNum +
               ", Date " +
               ", CallType" +
               ", SourceDevice" +
               ", SourceReam" +
               ", DestinationDevice" +
               ", DestinationRealm" +
               ", Direction" +
               ", Caller" +
               ", Carrier4Caller" +
               ", CallerNetworkType" +
               ", Callee" +
               ", Carrier4Callee" +
               ", CalleeNetworkType" +
               ", Duration" +
               ", TCCode" +
               ", TCReason" +
               ", StartCallTime" +
               ", EndCallTime" + 
               ", UserAgentInfo" +
               ", RegiNumber" +
               ", LastCallee" +
               ", BillNumber" +
               ", CID" +
               ", Info" +
               ", VMName";
    }
}
