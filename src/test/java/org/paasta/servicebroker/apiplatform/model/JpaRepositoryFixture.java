package org.paasta.servicebroker.apiplatform.model;

import org.openpaas.servicebroker.model.CreateServiceInstanceRequest;
import org.openpaas.servicebroker.model.ServiceInstance;
import org.paasta.servicebroker.deliverypipeline.model.JpaServiceInstance;

/**
 * Created by user on 2017-09-14.
 */
public class JpaRepositoryFixture {

    public static JpaServiceInstance getJpaServiceInstance() {
        CreateServiceInstanceRequest createServiceInstanceRequest = RequestFixture.getCreateServiceInstanceRequest();
        ServiceInstance serviceInstance = ServiceInstanceFixture.getServiceInstance();
        JpaServiceInstance jpaServiceInstance = new JpaServiceInstance(serviceInstance);
        return jpaServiceInstance;

    }
}