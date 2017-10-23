package org.paasta.servicebroker.deliverypipeline.config;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openpaas.servicebroker.model.Catalog;
import org.openpaas.servicebroker.model.Plan;
import org.openpaas.servicebroker.model.ServiceDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CatalogConfig {
	
	@Bean
	public Catalog catalog() {		
		return new Catalog( Arrays.asList(
				new ServiceDefinition(
					"af86588c-6212-11e7-907b-a6006ad3dba0",
					"delivery-pipeline",
					"A paasta source control service for application development.provision parameters : parameters {owner : owner}",
					false, // bindable
					false, // updatable
					Arrays.asList(
							new Plan("a5930564-6212-11e7-907b-a6006ad3dba0",
									"default",
									"This is a default service plan. All services are created equally.",
									null)),
					Arrays.asList("delivery-pipeline"),
					getServiceDefinitionMetadata(),
					null,
					null)));
	}
	
	/* Used by Pivotal CF console */
	private Map<String,Object> getServiceDefinitionMetadata() {
		Map<String,Object> sdMetadata = new HashMap<String,Object>();
		sdMetadata.put("displayName", "delivery-pipeline");
		sdMetadata.put("imageUrl","http://www.openpaas.org/rs/mysql/images/MysqlDB_Logo_Full.png");
		sdMetadata.put("longDescription","Paas-TA Delivery Pipeline");
		sdMetadata.put("providerDisplayName","PaaS-TA");
		sdMetadata.put("documentationUrl","https://paas-ta.kr");
		sdMetadata.put("supportUrl","https://paas-ta.kr");
		return sdMetadata;
	}
	
	private Map<String,Object> getPlanMetadata() {		
		Map<String,Object> planMetadata = new HashMap<String,Object>();
		planMetadata.put("costs", getCosts());
		planMetadata.put("bullets", getBullets());
		return planMetadata;
	}
	
	private List<Map<String,Object>> getCosts() {
		Map<String,Object> costsMap = new HashMap<String,Object>();
		
		Map<String,Object> amount = new HashMap<String,Object>();
		amount.put("usd", new Double(0.0));
	
		costsMap.put("amount", amount);
		costsMap.put("unit", "MONTHLY");
		
		return Arrays.asList(costsMap);
	}
	
	private List<String> getBullets() {
		return Arrays.asList("Delivery Pipeline",
				"Manage Artifact",
				"Inspection");
	}
	
}