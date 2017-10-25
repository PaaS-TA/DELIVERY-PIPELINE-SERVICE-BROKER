package org.paasta.servicebroker.apiplatform.test;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by user on 2017-09-12.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@TestPropertySource("classpath:test.properties")
public class DeliveryPipelineServiceInstanceServiceTest {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @InjectMocks
    DeliveryPipelineServiceInstanceService deliveryPipelineServiceInstanceService;

    @Mock
    DeliveryPipelineAdminService deliveryPipelineAdminService;


    @Value("${paasta.delivery.pipeline.api.url}")
    private String apiUrl;
    @Value("${paasta.delivery.pipeline.api.username}")
    private String apiUsername;
    @Value("${paasta.delivery.pipeline.api.password}")
    private String apiPassword;
    @Value("${service.dashboard.url}")
    private String dashboardUrl;


    private MockRestServiceServer mockServer;

    @Mock
    private RestTemplate restTemplate;

    @Before
    public void setup() throws Exception {
        MockitoAnnotations.initMocks(this);


        ReflectionTestUtils.setField(deliveryPipelineServiceInstanceService, "dashboardUrl", dashboardUrl);

        mockServer = MockRestServiceServer.createServer(restTemplate);

        ReflectionTestUtils.setField(deliveryPipelineAdminService, "apiUrl", apiUrl);
        ReflectionTestUtils.setField(deliveryPipelineAdminService, "apiUsername", apiUsername);
        ReflectionTestUtils.setField(deliveryPipelineAdminService, "apiPassword", apiPassword);
        ReflectionTestUtils.setField(deliveryPipelineAdminService, "restTemplate", restTemplate);

    }

    //----------------[ createServiceInstance Test]

    @Test
    public void test_validation_case1() throws Exception {
        // CreateServiceInstanceRequest Parameter 유효성 체크
        CreateServiceInstanceRequest request = RequestFixture.getCreateServiceInstanceRequest();
        request.withServiceInstanceId(TestConstants.SV_INSTANCE_ID_001);
        logger.debug("###############################################################");
        logger.debug("###############################################################");
        logger.debug("###############################################################");
        logger.debug(request.getOrganizationGuid());
        logger.debug(request.getPlanId());
        logger.debug(request.getServiceDefinitionId());
        logger.debug(request.getServiceInstanceId());
        logger.debug(request.getSpaceGuid());
        logger.debug(request.getParameters().toString());
        logger.debug("###############################################################");
        logger.debug("###############################################################");
        logger.debug("###############################################################");


        // case 1. request.getParameters() == null
        logger.info("case 1. request.getParameters() == null");
        request.setParameters(null);
        assertThatThrownBy(() -> deliveryPipelineServiceInstanceService.createServiceInstance(request))
                .isInstanceOf(ServiceBrokerException.class).hasMessageContaining("Required");


        // case 2. request.getParameters().isEmpty()
        logger.info("case 2. request.getParameters().isEmpty()");
        request.setParameters(new HashMap<>());
        assertThatThrownBy(() -> deliveryPipelineServiceInstanceService.createServiceInstance(request))
                .isInstanceOf(ServiceBrokerException.class).hasMessageContaining("Required");

        // case3.
        //!(request.getParameters().containsKey(param_owner) && request.getParameters().containsKey(param_orgname))
        logger.info("case 3. !(request.getParameters().containsKey(param_owner)");
        Map testParam = new HashMap<>();
        testParam.put("testparam", "testparam");
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
                .isInstanceOf(ServiceBrokerException.class).hasMessageContaining("more service instances");

    }

    @Test
    public void test_createServiceInstance_case1() throws Exception {

        //인스턴스 생성
        //ORG 등록 안되어 있고, SPACE 등록 안되어 있음
        //서비스는 등록되어 있음

//        ServiceInstance serviceInstance = ServiceInstanceFixture.getServiceInstance();
//        CreateServiceInstanceRequest request = RequestFixture.getCreateServiceInstanceRequest();
//        request.withServiceInstanceId(TestConstants.SV_INSTANCE_ID_001);
//
//        when(deliveryPipelineAdminService.findByOrganizationGuid(anyString())).thenReturn(serviceInstance);
//        when(deliveryPipelineAdminService.findById(anyString())).thenReturn(null);
//        when(deliveryPipelineAdminService.isExistsService(serviceInstance)).thenReturn(false);
//
//        ResponseEntity responseEntity = new ResponseEntity<Map>(HttpStatus.OK);
//        when(restTemplate.exchange(
//                Matchers.anyString(),
//                any(HttpMethod.class),
//                Matchers.<HttpEntity<?>>any(),
//                Matchers.<Class<Map>>any())).thenReturn(responseEntity);
//        when(deliveryPipelineAdminService.createDashboard(any(ServiceInstance.class),anyString())).thenReturn(false);
//
//        doNothing().when(deliveryPipelineAdminService).save(any(ServiceInstance.class));
//
//
//        deliveryPipelineServiceInstanceService.createServiceInstance(request);
//
//
//        verify(deliveryPipelineAdminService).findByOrganizationGuid(request.getOrganizationGuid());
//        verify(deliveryPipelineAdminService).findById(request.getServiceInstanceId());
//        verify(deliveryPipelineAdminService).isExistsService(any(ServiceInstance.class));
//        verify(deliveryPipelineAdminService).createDashboard(any(ServiceInstance.class), request.getParameters().get(TestConstants.PARAM_KEY_OWNER).toString());
//        verify(deliveryPipelineAdminService).save(serviceInstance);

    }

    @Test
    public void test_createServiceInstance_case2() throws Exception {

        //인스턴스 생성
        //ORG 등록 안되어 있고, SPACE 등록 안되어 있음
        //서비스는 등록되어 있음

//        ServiceInstance serviceInstance = ServiceInstanceFixture.getServiceInstance();
//        CreateServiceInstanceRequest request = RequestFixture.getCreateServiceInstanceRequest();
//        request.withServiceInstanceId(TestConstants.SV_INSTANCE_ID_001);
//
//        when(deliveryPipelineAdminService.findByOrganizationGuid(anyString())).thenReturn(null);
//        when(deliveryPipelineAdminService.findById(anyString())).thenReturn(serviceInstance);
//        when(deliveryPipelineAdminService.isExistsService(serviceInstance)).thenReturn(false);
//
//        ResponseEntity responseEntity = new ResponseEntity<Map>(HttpStatus.OK);
//        when(restTemplate.exchange(
//                Matchers.anyString(),
//                any(HttpMethod.class),
//                Matchers.<HttpEntity<?>>any(),
//                Matchers.<Class<Map>>any())).thenReturn(responseEntity);
//        when(deliveryPipelineAdminService.createDashboard(any(ServiceInstance.class),anyString())).thenReturn(false);
//
//        doNothing().when(deliveryPipelineAdminService).save(any(ServiceInstance.class));
//
//
//        deliveryPipelineServiceInstanceService.createServiceInstance(request);
//
//
//        verify(deliveryPipelineAdminService).findByOrganizationGuid(request.getOrganizationGuid());
//        verify(deliveryPipelineAdminService).findById(request.getServiceInstanceId());
//        verify(deliveryPipelineAdminService).isExistsService(any(ServiceInstance.class));
//        verify(deliveryPipelineAdminService).createDashboard(any(ServiceInstance.class), request.getParameters().get(TestConstants.PARAM_KEY_OWNER).toString());
//        verify(deliveryPipelineAdminService).save(serviceInstance);

    }

    @Test
    public void test_createServiceInstance_case3() throws Exception {

        //인스턴스 생성
        //ORG 등록 안되어 있고, SPACE 등록 안되어 있음
        //서비스는 등록되어 있음


//        ServiceInstance serviceInstanceFixture = ServiceInstanceFixture.getServiceInstance();
//        ResponseEntity responseEntity = new ResponseEntity<Map>(HttpStatus.OK);
//        when(restTemplate.exchange(
//                Matchers.anyString(),
//                Matchers.any(HttpMethod.class),
//                Matchers.<HttpEntity<?>>any(),
//                Matchers.<Class<Map>>any())).thenReturn(responseEntity);
//
//        CreateServiceInstanceRequest request = RequestFixture.getCreateServiceInstanceRequest();
//
//        when(deliveryPipelineAdminService.findById(anyString())).thenReturn(null);
//        when(deliveryPipelineAdminService.findByOrganizationGuid(anyString())).thenReturn(null);
//        when(deliveryPipelineAdminService.isExistsService(serviceInstanceFixture)).thenReturn(true);
//        doNothing().when(deliveryPipelineAdminService).createDashboard(serviceInstanceFixture, any(String.class));
//
//        request.withServiceInstanceId(TestConstants.SV_INSTANCE_ID_001);
//        deliveryPipelineServiceInstanceService.createServiceInstance(request);
//
//        verify(deliveryPipelineAdminService).findById(request.getServiceInstanceId());
//        verify(deliveryPipelineAdminService).findByOrganizationGuid(request.getOrganizationGuid());
//        verify(deliveryPipelineAdminService).createDashboard(serviceInstanceFixture, request.getParameters().get(TestConstants.PARAM_KEY_OWNER).toString());
//        verify(deliveryPipelineServiceInstanceService).createServiceInstance(any(CreateServiceInstanceRequest.class));

    }


}
