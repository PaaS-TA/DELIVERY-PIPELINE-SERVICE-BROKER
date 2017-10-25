package org.paasta.servicebroker.deliverypipeline.service.impl;

import org.openpaas.servicebroker.model.CreateServiceInstanceRequest;
import org.openpaas.servicebroker.model.ServiceInstance;
import org.paasta.servicebroker.deliverypipeline.exception.DeliveryPipelineServiceException;
import org.paasta.servicebroker.deliverypipeline.model.JpaServiceInstance;
import org.paasta.servicebroker.deliverypipeline.repo.JpaServiceInstanceRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.Base64Utils;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * @author whalsrn0710@bluedigm.com
 */
@Service
public class DeliveryPipelineAdminService {


    private Logger logger = LoggerFactory.getLogger(DeliveryPipelineAdminService.class);

    @Value("${paasta.delivery.pipeline.api.url}")
    private String apiUrl;
    @Value("${paasta.delivery.pipeline.api.username}")
    String apiUsername;
    @Value("${paasta.delivery.pipeline.api.password}")
    String apiPassword;

    private static final String AUTHORIZATION_HEADER_KEY = "Authorization";
    private static final String CONTENT_TYPE_HEADER_KEY = "Content-Type";


    @Autowired
    private JpaServiceInstanceRepository jpaServiceInstanceRepository;

    @Autowired
    RestTemplate restTemplate;

    private String authorization;

    public boolean isExistsService(ServiceInstance instance) {
        if (instance == null) return false;

        return (jpaServiceInstanceRepository.findByOrganizationGuid(instance.getOrganizationGuid()) != null);
    }


    public ServiceInstance findById(String id) {
        JpaServiceInstance newJpaServiceInstance = jpaServiceInstanceRepository.findOne(id);

        if (newJpaServiceInstance == null) return null;

        return new ServiceInstance(new CreateServiceInstanceRequest(newJpaServiceInstance.getServiceDefinitionId(),
                newJpaServiceInstance.getPlanId(),
                newJpaServiceInstance.getOrganizationGuid(),
                newJpaServiceInstance.getSpaceGuid()).withServiceInstanceId(newJpaServiceInstance.getServiceInstanceId()));
    }

    public ServiceInstance findByOrganizationGuid(String id) {
        JpaServiceInstance newJpaServiceInstance = jpaServiceInstanceRepository.findByOrganizationGuid(id);

        if (newJpaServiceInstance == null) return null;

        return new ServiceInstance(new CreateServiceInstanceRequest(newJpaServiceInstance.getServiceDefinitionId(),
                newJpaServiceInstance.getPlanId(),
                newJpaServiceInstance.getOrganizationGuid(),
                newJpaServiceInstance.getSpaceGuid()).withServiceInstanceId(newJpaServiceInstance.getServiceInstanceId()));
    }


    public void delete(String id) throws DeliveryPipelineServiceException {
        try {
            jpaServiceInstanceRepository.delete(id);
        } catch (Exception e) {
            throw handleException(e);
        }
    }


    public void save(ServiceInstance serviceInstance) throws DeliveryPipelineServiceException {
        try {
            jpaServiceInstanceRepository.save(new JpaServiceInstance(serviceInstance));
        } catch (Exception e) {
            throw handleException(e);
        }
    }


    public boolean deleteDashboard(ServiceInstance serviceInstance) throws DeliveryPipelineServiceException {
        try {

            String reqUrl = apiUrl + "/serviceInstance/" + serviceInstance.getServiceInstanceId();

            if (apiUsername.isEmpty()) this.authorization = "";
            else
                this.authorization = "Basic " + Base64Utils.encodeToString((apiUsername + ":" + apiPassword).getBytes(StandardCharsets.UTF_8));

            HttpHeaders reqHeaders = new HttpHeaders();
            if (!"".equals(authorization)) reqHeaders.add(AUTHORIZATION_HEADER_KEY, authorization);
            reqHeaders.add(CONTENT_TYPE_HEADER_KEY, "application/json");

            HttpEntity<Object> reqEntity = new HttpEntity<>(reqHeaders);

            logger.info("POST >> Request: {}, {baseUrl} : {}, Content-Type: {}", HttpMethod.POST, reqUrl, reqHeaders.get(CONTENT_TYPE_HEADER_KEY));
            ResponseEntity<String> resEntity = restTemplate.exchange(reqUrl, HttpMethod.DELETE, reqEntity, String.class);
            logger.info("send :: Response Status: {}", resEntity.getStatusCode());

            if (resEntity.getStatusCode().equals(HttpStatus.OK)) {
                return true;
            } else {
                return false;
            }

        } catch (Exception e) {
            throw handleException(e);
        }
    }

    public boolean createDashboard(ServiceInstance serviceInstance, String owner) throws DeliveryPipelineServiceException {
        try {
            Map params = new HashMap();

            params.put("id", serviceInstance.getServiceInstanceId());
            params.put("owner", owner);

            String reqUrl = apiUrl + "/serviceInstance";

            if (apiUsername.isEmpty()) this.authorization = "";
            else
                this.authorization = "Basic " + Base64Utils.encodeToString((apiUsername + ":" + apiPassword).getBytes(StandardCharsets.UTF_8));

            HttpHeaders reqHeaders = new HttpHeaders();
            if (!"".equals(authorization)) reqHeaders.add(AUTHORIZATION_HEADER_KEY, authorization);
            reqHeaders.add(CONTENT_TYPE_HEADER_KEY, "application/json");

            HttpEntity<Object> reqEntity = new HttpEntity<>(params, reqHeaders);

            logger.info("POST >> Request: {}, {baseUrl} : {}, Content-Type: {}", HttpMethod.POST, reqUrl, reqHeaders.get(CONTENT_TYPE_HEADER_KEY));
            ResponseEntity<String> resEntity = restTemplate.exchange(reqUrl, HttpMethod.POST, reqEntity, String.class);
            logger.info("send :: Response Status: {}", resEntity.getStatusCode());

            if (resEntity.getStatusCode().equals(HttpStatus.OK)) {
                return true;
            } else {
                return false;
            }

        } catch (Exception e) {
            throw handleException(e);
        }
    }


    private DeliveryPipelineServiceException handleException(Exception e) {
        logger.warn(e.getLocalizedMessage(), e);
        return new DeliveryPipelineServiceException(e.getLocalizedMessage());
    }


    public void send(String reqUrl, HttpMethod httpMethod, Object bodyObject) {


    }

    public void send(String reqUrl, HttpMethod httpMethod) {


    }
}

//	public boolean isExistsUser(String userId){
//		return true;
//	}

//	public ServiceInstanceBinding findBindById(String id){
//		return null;
//	}

//	public void deleteBind(String id) throws DeliveryPipelineServiceException {
//		try{
//
//		} catch (Exception e) {
//			throw handleException(e);
//		}
//	}

//	public void saveBind(ServiceInstanceBinding serviceInstanceBinding) throws DeliveryPipelineServiceException {
//		try{
//
//		} catch (Exception e) {
//			throw handleException(e);
//		}
//	}

//	public void createUser(String database, String userId, String password) throws DeliveryPipelineServiceException {
//		try{
//
//		} catch (Exception e) {
//			throw handleException(e);
//		}
//	}

//	public void deleteUser(String database, String userId) throws DeliveryPipelineServiceException {
//		try{
//
//		} catch (Exception e) {
//			throw handleException(e);
//		}
//	}

//	public String getConnectionString(String database, String username, String password) {
//		StringBuilder builder = new StringBuilder();
//		return builder.toString();
//	}

//	public String getServerAddresses() {
//		StringBuilder builder = new StringBuilder();
//		return builder.toString();
//	}