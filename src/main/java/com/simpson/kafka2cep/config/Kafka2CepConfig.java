package com.simpson.kafka2cep.config;

public class Kafka2CepConfig {
	Eql eql;
	Kafka kafka;
	
	public Kafka2CepConfig(Eql eql, Kafka kafka) {
		this.eql = eql;
		this.kafka = kafka;
	}
}
