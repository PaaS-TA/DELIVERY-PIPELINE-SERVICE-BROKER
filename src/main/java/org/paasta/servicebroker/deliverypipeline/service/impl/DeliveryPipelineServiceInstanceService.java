package org.paasta.servicebroker.deliverypipeline.service.impl;


import org.openpaas.servicebroker.exception.ServiceBrokerException;
import org.openpaas.servicebroker.exception.ServiceInstanceDoesNotExistException;
import org.openpaas.servicebroker.exception.ServiceInstanceExistsException;
import org.openpaas.servicebroker.exception.ServiceInstanceUpdateNotSupportedException;
import org.openpaas.servicebroker.model.CreateServiceInstanceRequest;
import org.openpaas.servicebroker.model.DeleteServiceInstanceRequest;
import org.openpaas.servicebroker.model.ServiceInstance;
import org.openpaas.servicebroker.model.UpdateServiceInstanceRequest;
import org.paasta.servicebroker.deliverypipeline.exception.DeliveryPipelineServiceException;
import org.openpaas.servicebroker.service.ServiceInstanceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author whalsrn0710@bluedigm.com
 */
@Service
public class DeliveryPipelineServiceInstanceService implements ServiceInstanceService {

    private static final Logger logger = LoggerFactory.getLogger(DeliveryPipelineServiceInstanceService.class);

    public static final String TOKEN_SUID = "[SUID]";
    public static final String TOKEN_OWNER = "owner";

    @Value("${service.dashboard.url}")
    private String dashboardUrl;

    @Autowired
    private DeliveryPipelineAdminService deliveryPipelineAdminService;

    @Override
    public ServiceInstance createServiceInstance(CreateServiceInstanceRequest request)
            throws ServiceInstanceExistsException, ServiceBrokerException {

        logger.debug("DeliveryPipelineServiceInstanceService CLASS createServiceInstance");



        if (request.getParameters() == null || request.getParameters().isEmpty() ||
                !(request.getParameters().containsKey(TOKEN_OWNER) && request.getParameters().containsKey(TOKEN_OWNER))) {
            logger.debug("Required " + TOKEN_OWNER + " parameter.\nTIP: cf create-service SERVICE PLAN SERVICE_INSTANCE -c '{\"" + TOKEN_OWNER + "\": \"username\"}'");
            throw new ServiceBrokerException("Required " + TOKEN_OWNER + " parameter.\nTIP: cf create-service SERVICE PLAN SERVICE_INSTANCE -c '{\"" + TOKEN_OWNER + "\": \"username\"}'");
        }


        // 서비스 인스턴스 체크
        ServiceInstance serviceInstance = deliveryPipelineAdminService.findById(request.getServiceInstanceId());

        if (serviceInstance != null) {
            logger.debug("This instances already has one or more service instances.", request.getServiceInstanceId());
            throw new ServiceInstanceExistsException(new ServiceInstance(request));
        }

        // TODO dashboard
        // 서비스 인스턴스 Guid Check
        ServiceInstance instance = deliveryPipelineAdminService.findByOrganizationGuid(request.getOrganizationGuid());

        if (instance != null) {
            logger.debug("This organization already has one or more service instances.", request.getServiceInstanceId());
            throw new ServiceBrokerException("This organization already has one or more service instances.");
        }


        String serviceInstanceDashboardUrl = dashboardUrl.replace(TOKEN_SUID, request.getServiceInstanceId());
        ServiceInstance result = new ServiceInstance(request).withDashboardUrl(serviceInstanceDashboardUrl);

        deliveryPipelineAdminService.createDashboard(result, request.getParameters().get(TOKEN_OWNER).toString());
        deliveryPipelineAdminService.save(result);

        return result;
    }


    @Override
    public ServiceInstance getServiceInstance(String id) {
        CreateServiceInstanceRequest createServiceInstanceRequest = new CreateServiceInstanceRequest();
        createServiceInstanceRequest.setOrganizationGuid(id);
        return new ServiceInstance(createServiceInstanceRequest);
    }

    @Override
    public ServiceInstance deleteServiceInstance(DeleteServiceInstanceRequest request) throws DeliveryPipelineServiceException {
        ServiceInstance instance = deliveryPipelineAdminService.findById(request.getServiceInstanceId());
        deliveryPipelineAdminService.deleteDashboard(instance);
        deliveryPipelineAdminService.delete(instance.getServiceInstanceId());
        return instance;
    }

    @Override
    public ServiceInstance updateServiceInstance(UpdateServiceInstanceRequest request)
            throws ServiceInstanceUpdateNotSupportedException, ServiceBrokerException, ServiceInstanceDoesNotExistException {
//		ServiceInstance instance = deliveryPipelineAdminService.findById(request.getServiceInstanceId());
//		deliveryPipelineAdminService.delete(instance.getServiceInstanceId());
//		ServiceInstance updatedInstance = new ServiceInstance(request);
//		deliveryPipelineAdminService.save(updatedInstance);
//		return updatedInstance;

        throw new ServiceBrokerException("Not Supported");

    }

}