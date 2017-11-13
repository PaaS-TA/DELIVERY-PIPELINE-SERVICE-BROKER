package org.paasta.servicebroker.deliverypipeline.config;

import org.openpaas.servicebroker.model.Catalog;
import org.openpaas.servicebroker.model.Plan;
import org.openpaas.servicebroker.model.ServiceDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.*;

@Configuration
public class CatalogConfig {

	@Bean
	public Catalog catalog() {
		return new Catalog(Arrays.asList(
				new ServiceDefinition(
						"af86588c-6212-11e7-907b-b6006ad3dps0",
						"delivery-pipeline-dev",
						"A paasta source control service for application development.provision parameters : parameters {owner : owner}",
						false, // bindable
						false, // updatable
						Arrays.asList(
								new Plan("a5930564-6212-11e7-907b-b6006ad3dps1",
										"delivery-pipeline-shared",
										"This is a shared service plan. All services are created equally.",
										getPlanMetadata("A")),
								new Plan("a5930564-6212-11e7-907b-b6006ad3dps2",
										"delivery-pipeline-dedicated",
										"This is a dedicated service plan. All services are created equally.",
										getPlanMetadata("B"))),
						Arrays.asList("delivery-pipeline-shared", "delivery-pipeline-dedicated"),
						getServiceDefinitionMetadata(),
						null,
						null)));
	}

	/* Used by Pivotal CF console */
	private Map<String, Object> getServiceDefinitionMetadata() {
		Map<String, Object> sdMetadata = new HashMap<String, Object>();
		sdMetadata.put("displayName", "delivery-pipeline-dev");
		sdMetadata.put("imageUrl", "");
		sdMetadata.put("longDescription", "Paas-TA Delivery Pipeline");
		sdMetadata.put("providerDisplayName", "PaaS-TA");
		sdMetadata.put("documentationUrl", "https://paas-ta.kr");
		sdMetadata.put("supportUrl", "https://paas-ta.kr");
		return sdMetadata;
	}

	private Map<String, Object> getPlanMetadata(String planType) {
		Map<String, Object> planMetadata = new HashMap<>();
		planMetadata.put("costs", getCosts(planType));
		planMetadata.put("bullets", getBullets(planType));

		return planMetadata;
	}

	private List<Map<String, Object>> getCosts(String planType) {
		Map<String, Object> costsMap = new HashMap<>();
		Map<String, Object> amount = new HashMap<>();

		switch (planType) {
			case "A":
				amount.put("usd", 0.0);
				costsMap.put("amount", amount);
				costsMap.put("unit", "MONTHLY");

				break;
			case "B":
				amount.put("usd", 0.0);
				costsMap.put("amount", amount);
				costsMap.put("unit", "MONTHLY");

				break;
			default:
				amount.put("usd", 0.0);
				costsMap.put("amount", amount);
				costsMap.put("unit", "MONTHLY");
				break;
		}

		return Collections.singletonList(costsMap);
	}

	private List<String> getBullets(String planType) {
		if (planType.equals("A")) {
			return Arrays.asList("Delivery pipeline shared build server use",
					"Deployment pipeline build service using a shared server");
		} else if (planType.equals("B")) {
			return Arrays.asList("Delivery pipeline dedicated build server use",
					"Deployment pipeline build service using a dedicated server");
		}
		return Arrays.asList("Delivery pipeline shared build server use",
				"Deployment pipeline build service using a shared server");
	}
}