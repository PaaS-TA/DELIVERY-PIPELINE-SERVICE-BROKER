package org.paasta.servicebroker.apiplatform.test;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.openpaas.servicebroker.model.CreateServiceInstanceRequest;
import org.openpaas.servicebroker.model.ServiceInstance;
import org.paasta.servicebroker.apiplatform.common.TestConstants;
import org.paasta.servicebroker.apiplatform.model.JpaRepositoryFixture;
import org.paasta.servicebroker.apiplatform.model.RequestFixture;
import org.paasta.servicebroker.apiplatform.model.ServiceInstanceFixture;
import org.paasta.servicebroker.deliverypipeline.model.JpaServiceInstance;
import org.paasta.servicebroker.deliverypipeline.repo.JpaServiceInstanceRepository;
import org.paasta.servicebroker.deliverypipeline.service.impl.DeliveryPipelineAdminService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

/**
 * Created by user on 2017-09-13.
 */
public class DeliveryPipelineAdminServiceTest {

    @Mock
    JpaServiceInstanceRepository jpaServiceInstanceRepository;

    @Mock
    DeliveryPipelineAdminService deliveryPipelineAdminService;


    private static String spyTestUser = null;

    @Before
    public void setup() throws Exception {
        MockitoAnnotations.initMocks(this);

//        ReflectionTestUtils.setField(deliveryPipelineAdminService, "param_owner", TestConstants.PARAM_KEY_OWNER);

        Random rnd = new Random();
        StringBuilder sb = new StringBuilder();
        for(int i=0;i<10;i++) {
            sb.append(String.valueOf((char)((int)(rnd.nextInt(26)) + 97)));
        }
        spyTestUser = "mokito_"+sb.toString() + "@" + sb.toString() +".com";
    }







    @Test
    public void test_createDashboard() throws Exception{
        JpaServiceInstance jpaServiceInstance = JpaRepositoryFixture.getJpaServiceInstance();
        when(jpaServiceInstanceRepository.save(any(JpaServiceInstance.class))).thenReturn(jpaServiceInstance);
        deliveryPipelineAdminService.createDashboard(ServiceInstanceFixture.getServiceInstance(),spyTestUser);
        verify(jpaServiceInstanceRepository).save(any(JpaServiceInstance.class));

    }


    @Test
    public void test_findById_case_null() throws Exception{
        when(jpaServiceInstanceRepository.findOne(anyString())).thenReturn(null);
        ServiceInstance serviceInstance = deliveryPipelineAdminService.findById(TestConstants.SV_INSTANCE_ID_001);
        assertThat(serviceInstance, is(nullValue()));
    }


    @Test
    public void test_findByOrgGuid() throws Exception{
        JpaServiceInstance jpaServiceInstance = JpaRepositoryFixture.getJpaServiceInstance();
        when(jpaServiceInstanceRepository.findByOrganizationGuid(anyString())).thenReturn(jpaServiceInstance);
        ServiceInstance serviceInstance = deliveryPipelineAdminService.findByOrganizationGuid(jpaServiceInstance.getOrganizationGuid());
        assertThat(serviceInstance.getServiceInstanceId(), is(jpaServiceInstance.getServiceInstanceId()));
        assertThat(serviceInstance.getPlanId(), is(jpaServiceInstance.getPlanId()));
        assertThat(serviceInstance.getOrganizationGuid(), is(jpaServiceInstance.getOrganizationGuid()));
        assertThat(serviceInstance.getSpaceGuid(), is(jpaServiceInstance.getSpaceGuid()));
    }


    @Test
    public void test_findByOrgGuid_case_null() throws Exception{
        when(jpaServiceInstanceRepository.findByOrganizationGuid(anyString())).thenReturn(null);
        ServiceInstance serviceInstance = deliveryPipelineAdminService.findByOrganizationGuid(TestConstants.SV_INSTANCE_ID_001);
        assertThat(serviceInstance, is(nullValue()));
    }



    @Test
    public void test_isExistsService() throws Exception{
        JpaServiceInstance jpaServiceInstance = JpaRepositoryFixture.getJpaServiceInstance();
        when(jpaServiceInstanceRepository.save(any(JpaServiceInstance.class))).thenReturn(jpaServiceInstance);
        deliveryPipelineAdminService.isExistsService(ServiceInstanceFixture.getServiceInstance());
        verify(jpaServiceInstanceRepository).save(any(JpaServiceInstance.class));
    }

    @Test
    public void findById_Test() throws Exception{
        JpaServiceInstance jpaServiceInstance = new JpaServiceInstance();
        jpaServiceInstance.setServiceInstanceId(TestConstants.SV_INSTANCE_ID_001);
        when(jpaServiceInstanceRepository.findOne(jpaServiceInstance.getServiceInstanceId())).thenReturn(jpaServiceInstance);
        ServiceInstance serviceInstance = deliveryPipelineAdminService.findById(jpaServiceInstance.getServiceInstanceId());
        assertThat(serviceInstance.getServiceInstanceId(),is(jpaServiceInstance.getServiceInstanceId()));
    }

    @Test
    public void test_deleteDashboard() throws Exception{
        doNothing().when(jpaServiceInstanceRepository).delete(TestConstants.SV_INSTANCE_ID_001);
        deliveryPipelineAdminService.deleteDashboard(ServiceInstanceFixture.getServiceInstance());
        verify(jpaServiceInstanceRepository).delete(TestConstants.SV_INSTANCE_ID_001);
    }
}
