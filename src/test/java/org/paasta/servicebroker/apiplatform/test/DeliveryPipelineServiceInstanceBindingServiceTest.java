package org.paasta.servicebroker.apiplatform.test;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.openpaas.servicebroker.exception.ServiceBrokerException;
import org.openpaas.servicebroker.model.CreateServiceInstanceBindingRequest;
import org.openpaas.servicebroker.model.DeleteServiceInstanceBindingRequest;
import org.paasta.servicebroker.apiplatform.model.RequestFixture;
import org.paasta.servicebroker.deliverypipeline.service.impl.DeliveryPipelineServiceInstanceBindingService;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * DELIVERY-PIPELINE-SERVICE-BROKER
 *
 * 배포파이프라인 - binding 지원 안함.
 *
 * Created by user on 2017-09-12.
 */
public class DeliveryPipelineServiceInstanceBindingServiceTest {

    @InjectMocks
    DeliveryPipelineServiceInstanceBindingService deliveryPipelineServiceInstanceBindingService;


    @Before
    public void setup() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void test_createServiceInstanceBinding() throws Exception {

        CreateServiceInstanceBindingRequest request = RequestFixture.getCreateServiceInstanceBindingRequest();

        assertThatThrownBy(() -> deliveryPipelineServiceInstanceBindingService.createServiceInstanceBinding(request))
                .isInstanceOf(ServiceBrokerException.class).hasMessageContaining("Not Supported");
    }

    @Test
    public void test_deleteServiceInstanceBinding() throws Exception {

        DeleteServiceInstanceBindingRequest request = RequestFixture.getDeleteServiceInstanceBindingRequest();

        assertThatThrownBy(() -> deliveryPipelineServiceInstanceBindingService.deleteServiceInstanceBinding(request))
                .isInstanceOf(ServiceBrokerException.class).hasMessageContaining("Not Supported");
    }

}
