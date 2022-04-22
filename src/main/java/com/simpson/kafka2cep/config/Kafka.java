package com.simpson.kafka2cep.config;

public class Kafka {
	public class Zoo {
		String lists;
		public Zoo(String lists) {
			this.lists = lists;
		}
	}
	
	public class Obj {
		String className;
		public Obj(String className) {
			this.className = className;
		}
	}
	
	Zoo zoo;
	String groupId;
	String topic;
	Obj obj;
	int connetionTimeout;
	int reconnectInterval;
	String clientId;
	int threadNum;
	String dataType;
	String dataSeperator;
	boolean offsetReset;
	
	public Kafka(
			Zoo zoo, String groupId, String topic,
			Obj obj, int connetionTimeout,
			int reconnectInterval, String clientId,
			int threadNum, String dataType,
			String dataSeperator, boolean offsetReset) {
		this.zoo = zoo;
		this.groupId = groupId;
		this.topic = topic;
		this.obj = obj;
		this.connetionTimeout = connetionTimeout;
		this.reconnectInterval = reconnectInterval;
		this.clientId = clientId;
		this.threadNum = threadNum;
		this.dataType = dataType;
		this.dataSeperator = dataSeperator;
		this.offsetReset = offsetReset;
	}
}
