package org.paasta.servicebroker.apiplatform.test;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.openpaas.servicebroker.exception.ServiceBrokerException;
import org.openpaas.servicebroker.exception.ServiceInstanceExistsException;
import org.openpaas.servicebroker.model.CreateServiceInstanceRequest;
import org.openpaas.servicebroker.model.ServiceInstance;
import org.paasta.servicebroker.apiplatform.common.TestConstants;
import org.paasta.servicebroker.apiplatform.model.RequestFixture;
import org.paasta.servicebroker.apiplatform.model.ServiceInstanceFixture;
import org.paasta.servicebroker.deliverypipeline.service.impl.DeliveryPipelineAdminService;
import org.paasta.servicebroker.deliverypipeline.service.impl.DeliveryPipelineServiceInstanceService;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

/**
 * Created by user on 2017-09-12.
 */
public class DeliveryPipelineServiceInstanceServiceTest {

    @InjectMocks
    DeliveryPipelineServiceInstanceService deliveryPipelineServiceInstanceService;

    @Mock
    DeliveryPipelineAdminService deliveryPipelineAdminService;

    @Before
    public void setup() throws Exception {
        MockitoAnnotations.initMocks(this);

        ReflectionTestUtils.setField(deliveryPipelineServiceInstanceService, "dashboard_url", TestConstants.DASHBOARD_URL);
        ReflectionTestUtils.setField(deliveryPipelineServiceInstanceService, "param_owner", TestConstants.PARAM_KEY_OWNER);

    }

    //----------------[ createServiceInstance Test]

    @Test
    public void test_validation_case1() throws Exception {

        // CreateServiceInstanceRequest Parameter 유효성 체크
        // case 1. request.getParameters() == null
        CreateServiceInstanceRequest request = RequestFixture.getCreateServiceInstanceRequest();
        request.setParameters(null);
        assertThatThrownBy(() -> deliveryPipelineServiceInstanceService.createServiceInstance(request))
                .isInstanceOf(ServiceBrokerException.class).hasMessageContaining("Required");

        // case 2. request.getParameters().isEmpty()
        request.setParameters(new HashMap<>());
        assertThatThrownBy(() -> deliveryPipelineServiceInstanceService.createServiceInstance(request))
                .isInstanceOf(ServiceBrokerException.class).hasMessageContaining("Required");

        // case3.
        //!(request.getParameters().containsKey(param_owner) && request.getParameters().containsKey(param_orgname))
        Map testParam = new HashMap<>();
        testParam.put("testparam", "testparam");
        request.setParameters(testParam);
        assertThatThrownBy(() -> deliveryPipelineServiceInstanceService.createServiceInstance(request))
                .isInstanceOf(ServiceBrokerException.class).hasMessageContaining("Required");

        // case4.
        // !(request.getParameters().containsKey(param_owner) && request.getParameters().containsKey(param_orgname))
        testParam.put("owner", "testparam");
        request.setParameters(testParam);
        assertThatThrownBy(() -> deliveryPipelineServiceInstanceService.createServiceInstance(request))
                .isInstanceOf(ServiceBrokerException.class).hasMessageContaining("Required");


    }

    @Test
    public void test_validation_case2() throws Exception {

        CreateServiceInstanceRequest request = RequestFixture.getCreateServiceInstanceRequest();
        ServiceInstance serviceInstance = ServiceInstanceFixture.getServiceInstance();

        when(deliveryPipelineAdminService.findById(anyString())).thenReturn(serviceInstance);

        assertThatThrownBy(() -> deliveryPipelineServiceInstanceService.createServiceInstance(request))
                .isInstanceOf(ServiceInstanceExistsException.class).hasMessageContaining("ServiceInstance with the given ID already exists");

    }

    @Test
    public void test_validation_case3() throws Exception {

        CreateServiceInstanceRequest request = RequestFixture.getCreateServiceInstanceRequest();
        ServiceInstance serviceInstance = ServiceInstanceFixture.getServiceInstance();

        when(deliveryPipelineAdminService.findById(anyString())).thenReturn(null);
        when(deliveryPipelineAdminService.findByOrganizationGuid(anyString())).thenReturn(serviceInstance);

        assertThatThrownBy(() -> deliveryPipelineServiceInstanceService.createServiceInstance(request))
                .isInstanceOf(ServiceBrokerException.class).hasMessageContaining("ServiceInstance already exists in your organization");

    }

//    @Test
//    public void test_createServiceInstance_case1() throws Exception {
//
//        //사용자 계정 체크 : 사용자 계정이 존재 하지 않는 경우 - 사용자 계정 생성 처리
//
//        CreateServiceInstanceRequest request = RequestFixture.getCreateServiceInstanceRequest();
//
//        when(deliveryPipelineAdminService.findById(anyString())).thenReturn(null);
//        when(deliveryPipelineAdminService.findByOrganizationGuid(anyString())).thenReturn(null);
//
//        when(deliveryPipelineAdminService.isExistsService(any(CreateServiceInstanceRequest.class))).thenReturn(boolean);
//
//        doNothing().when(deliveryPipelineAdminService).createUser(any(CreateServiceInstanceRequest.class));
//        doNothing().when(deliveryPipelineAdminService).createScInstanceUser(any(CreateServiceInstanceRequest.class));
//
//        doNothing().when(deliveryPipelineAdminService).createServiceInstance(any(ServiceInstance.class), any(CreateServiceInstanceRequest.class));
//
//        doNothing().when(deliveryPipelineAdminService).createUserApi(any(CreateServiceInstanceRequest.class));
//
//        request.withServiceInstanceId(TestConstants.SV_INSTANCE_ID_001);
//        deliveryPipelineServiceInstanceService.createServiceInstance(request);
//
//        verify(deliveryPipelineAdminService).findById(request.getServiceInstanceId());
//        verify(deliveryPipelineAdminService).findByOrgGuid(request.getOrganizationGuid());
//        verify(deliveryPipelineAdminService).isExistUser(request);
//        verify(deliveryPipelineAdminService).createUser(request);
//        verify(deliveryPipelineAdminService).createScInstanceUser(request);
//        verify(deliveryPipelineAdminService).createServiceInstance(any(ServiceInstance.class), any(CreateServiceInstanceRequest.class));
//        verify(deliveryPipelineAdminService).createUserApi(request);
//
//    }


}
