package com.simpson.kafka2cep.config;

public class Eql {
    String listInfo;
    String clsid;
    String printDate;
    String locale;
    
    public Eql() {
    	
    }
    
    public Eql(String listInfo, String clsid, String printDate, String locale) {
    	this.listInfo = listInfo;
    	this.clsid = clsid;
    	this.printDate = printDate;
    	this.locale = locale;
    }
}
