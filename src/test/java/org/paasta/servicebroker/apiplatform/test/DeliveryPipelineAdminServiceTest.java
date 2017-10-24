package org.paasta.servicebroker.apiplatform.test;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.openpaas.servicebroker.model.ServiceInstance;
import org.paasta.servicebroker.apiplatform.common.TestConstants;
import org.paasta.servicebroker.apiplatform.model.JpaRepositoryFixture;
import org.paasta.servicebroker.apiplatform.model.ServiceInstanceFixture;
import org.paasta.servicebroker.deliverypipeline.model.JpaServiceInstance;
import org.paasta.servicebroker.deliverypipeline.repo.JpaServiceInstanceRepository;
import org.paasta.servicebroker.deliverypipeline.service.impl.DeliveryPipelineAdminService;
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

import java.net.URI;
import java.util.Map;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

/**
 * Created by user on 2017-09-13.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@TestPropertySource("classpath:test.properties")
public class DeliveryPipelineAdminServiceTest {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Mock
    JpaServiceInstanceRepository jpaServiceInstanceRepository;

    @InjectMocks
    DeliveryPipelineAdminService deliveryPipelineAdminService;


    @Value("${paasta.delivery.pipeline.api.url}")
    private String apiUrl;
    @Value("${paasta.delivery.pipeline.api.username}")
    String apiUsername;
    @Value("${paasta.delivery.pipeline.api.password}")
    String apiPassword;

    private MockRestServiceServer mockServer;

    @Mock
    private RestTemplate restTemplate;

    @Before
    public void setup() throws Exception {
        MockitoAnnotations.initMocks(this);
        print();

        mockServer = MockRestServiceServer.createServer(restTemplate);

        ReflectionTestUtils.setField(deliveryPipelineAdminService, "apiUrl", apiUrl);
        ReflectionTestUtils.setField(deliveryPipelineAdminService, "apiUsername", apiUsername);
        ReflectionTestUtils.setField(deliveryPipelineAdminService, "apiPassword", apiPassword);
        ReflectionTestUtils.setField(deliveryPipelineAdminService, "restTemplate", restTemplate);

    }

    @Test
    public void test_createDashboard() throws Exception {
        ServiceInstance serviceInstance = ServiceInstanceFixture.getServiceInstance();
        ResponseEntity responseEntity = new ResponseEntity<Map>(HttpStatus.OK);
        when(restTemplate.exchange(
                Matchers.anyString(),
                Matchers.any(HttpMethod.class),
                Matchers.<HttpEntity<?>>any(),
                Matchers.<Class<Map>>any())).thenReturn(responseEntity);

        deliveryPipelineAdminService.createDashboard(serviceInstance, TestConstants.PARAM_KEY_OWNER);

    }

    @Test
    public void test_findById_case() throws Exception {
        JpaServiceInstance jpaServiceInstance = JpaRepositoryFixture.getJpaServiceInstance();
        jpaServiceInstance.setServiceInstanceId(TestConstants.SV_INSTANCE_ID_001);
        when(jpaServiceInstanceRepository.findOne(anyString())).thenReturn(jpaServiceInstance);
        ServiceInstance serviceInstance = deliveryPipelineAdminService.findById(jpaServiceInstance.getServiceInstanceId());
        assertThat(serviceInstance.getServiceInstanceId(), is(TestConstants.SV_INSTANCE_ID_001));
    }


    @Test
    public void test_findById_case_null() throws Exception {
        when(jpaServiceInstanceRepository.findOne(anyString())).thenReturn(null);
        ServiceInstance serviceInstance = deliveryPipelineAdminService.findById(TestConstants.SV_INSTANCE_ID_001);
        assertThat(serviceInstance, is(nullValue()));
    }

    @Test
    public void test_findByOrgGuid() throws Exception {
        JpaServiceInstance jpaServiceInstance = JpaRepositoryFixture.getJpaServiceInstance();
        when(jpaServiceInstanceRepository.findByOrganizationGuid(anyString())).thenReturn(jpaServiceInstance);
        ServiceInstance serviceInstance = deliveryPipelineAdminService.findByOrganizationGuid(jpaServiceInstance.getOrganizationGuid());
        assertThat(serviceInstance.getServiceInstanceId(), is(jpaServiceInstance.getServiceInstanceId()));
        assertThat(serviceInstance.getPlanId(), is(jpaServiceInstance.getPlanId()));
        assertThat(serviceInstance.getOrganizationGuid(), is(jpaServiceInstance.getOrganizationGuid()));
        assertThat(serviceInstance.getSpaceGuid(), is(jpaServiceInstance.getSpaceGuid()));
    }


    @Test
    public void test_findByOrgGuid_case_null() throws Exception {
        when(jpaServiceInstanceRepository.findByOrganizationGuid(anyString())).thenReturn(null);
        ServiceInstance serviceInstance = deliveryPipelineAdminService.findByOrganizationGuid(TestConstants.SV_INSTANCE_ID_001);
        assertThat(serviceInstance, is(nullValue()));
    }


    @Test
    public void test_isExistsService_false() throws Exception {
        JpaServiceInstance jpaServiceInstance = JpaRepositoryFixture.getJpaServiceInstance();
        when(jpaServiceInstanceRepository.findByOrganizationGuid(jpaServiceInstance.getServiceInstanceId())).thenReturn(jpaServiceInstance);
        boolean isExistsService = deliveryPipelineAdminService.isExistsService(ServiceInstanceFixture.getServiceInstance());
        assertThat(isExistsService, is(false));
    }

    @Test
    public void test_deleteDashboard() throws Exception {

        ServiceInstance serviceInstance = ServiceInstanceFixture.getServiceInstance();
        ResponseEntity responseEntity = new ResponseEntity<String>(HttpStatus.OK);
        when(restTemplate.exchange(
                Matchers.anyString(),
                Matchers.any(HttpMethod.class),
                Matchers.<HttpEntity<?>>any(),
                Matchers.<Class<String>>any())).thenReturn(ResponseEntity.ok("ok"));

        deliveryPipelineAdminService.deleteDashboard(serviceInstance);
    }


    private void print() {
        logger.info("apiUrl : " + apiUrl);
        logger.info("apiUsername : " + apiPassword);
        logger.info("apiPassword : " + apiPassword);
    }
}
