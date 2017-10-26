package org.paasta.servicebroker.apiplatform.test;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.paasta.servicebroker.apiplatform.model.ServiceInstanceFixture;
import org.paasta.servicebroker.deliverypipeline.model.JpaServiceInstance;
import org.paasta.servicebroker.deliverypipeline.repo.JpaServiceInstanceRepository;
import org.paasta.servicebroker.deliverypipeline.service.impl.DeliveryPipelineAdminService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

/**
 * DELIVERY-PIPELINE-SERVICE-BROKER
 *
 * Created by user on 2017-09-13.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@TestPropertySource("classpath:test.properties")
public class DeliveryPipelineModelTest {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Mock
    JpaServiceInstanceRepository jpaServiceInstanceRepository;

    @InjectMocks
    DeliveryPipelineAdminService deliveryPipelineAdminService;


    @Value("${paasta.delivery.pipeline.api.url}")
    private String apiUrl;
    @Value("${paasta.delivery.pipeline.api.username}")
    private String apiUsername;
    @Value("${paasta.delivery.pipeline.api.password}")
    private String apiPassword;
    @Value("${service.dashboard.url}")
    private String dashboardUrl;


    @Mock
    private MockRestServiceServer mockServer;

    @Mock
    private RestTemplate restTemplate;

    @Before
    public void setup() throws Exception {
        MockitoAnnotations.initMocks(this);
        mockServer = MockRestServiceServer.createServer(restTemplate);



    }



    @Test
    public void test_JpaServiceInstance1() throws Exception {
        JpaServiceInstance jpaServiceInstance = new JpaServiceInstance();
    }
    @Test
    public void test_JpaServiceInstance2() throws Exception {
        JpaServiceInstance jpaServiceInstance = new JpaServiceInstance(ServiceInstanceFixture.getServiceInstance());
    }

}
