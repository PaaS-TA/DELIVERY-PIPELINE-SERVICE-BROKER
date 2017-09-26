package org.paasta.servicebroker.apiplatform.test;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.openpaas.servicebroker.controller.ServiceInstanceBindingController;
import org.openpaas.servicebroker.exception.ServiceBrokerException;
import org.openpaas.servicebroker.model.CreateServiceInstanceBindingRequest;
import org.openpaas.servicebroker.model.DeleteServiceInstanceBindingRequest;
import org.openpaas.servicebroker.service.ServiceInstanceBindingService;
import org.openpaas.servicebroker.service.ServiceInstanceService;
import org.paasta.servicebroker.apiplatform.common.TestConstants;
import org.paasta.servicebroker.apiplatform.model.RequestFixture;
import org.paasta.servicebroker.apiplatform.model.ServiceInstanceFixture;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.util.UriTemplate;

import java.util.Base64;

import static org.hamcrest.Matchers.is;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * paastaSourceControlServiceBroker
 * org.paasta.servicebroker.sourcecontrol.service.impl
 *
 * 형상관리 - binding 지원 안함.
 *
 * Created by user on 2017-09-12.
 */
public class DeliveryPipelineServiceInstanceBindingServiceTest {

    @InjectMocks
    ServiceInstanceBindingController controller;

    @Mock
    ServiceInstanceBindingService serviceInstanceBindingService;

    @Mock
    ServiceInstanceService serviceInstanceService;

    private MockMvc mockMvc;

    private String basicAuth;
    private String requestUrl;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        // Controller 를 MockMvC 객체로 만듬.
        this.mockMvc = MockMvcBuilders.standaloneSetup(controller).build();

        basicAuth = "Basic " + (Base64.getEncoder().encodeToString((TestConstants.BASIC_AUTH_USER_NAME + ":" + TestConstants.BASIC_AUTH_PASSWORD).getBytes()));
        requestUrl = new UriTemplate(ServiceInstanceBindingController.BASE_PATH + "/{bindingId}").expand(TestConstants.SV_INSTANCE_ID_001,TestConstants.BINDING_ID).toString();

    }

    @Test
    public void createServiceInstanceBinding() throws Exception {

        when(serviceInstanceService.getServiceInstance(TestConstants.SV_INSTANCE_ID_001)).thenReturn(ServiceInstanceFixture.getServiceInstance());
        when(serviceInstanceBindingService.createServiceInstanceBinding(any(CreateServiceInstanceBindingRequest.class))).thenThrow(new ServiceBrokerException("Not Supported"));

        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(RequestFixture.getCreateServiceInstanceBindingRequest());

        MvcResult result = this.mockMvc.perform(put(requestUrl)
                .header(TestConstants.Headers_KEY_API_VERSION, TestConstants.Headers_VALUE_API_VERSION)
                .header(TestConstants.Headers_KEY_AUTHORIZATION, basicAuth)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().is5xxServerError())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.description", is("Not Supported")))
                .andDo(print())
                .andReturn();
    }

    @Test
    public void deleteServiceInstanceBinding() throws Exception {

        when(serviceInstanceService.getServiceInstance(TestConstants.SV_INSTANCE_ID_001)).thenReturn(ServiceInstanceFixture.getServiceInstance());
        when(serviceInstanceBindingService.deleteServiceInstanceBinding(any(DeleteServiceInstanceBindingRequest.class))).thenThrow(new ServiceBrokerException("Not Supported"));

        MvcResult result = this.mockMvc.perform(delete(requestUrl)
                .header(TestConstants.Headers_KEY_API_VERSION, TestConstants.Headers_VALUE_API_VERSION)
                .header(TestConstants.Headers_KEY_AUTHORIZATION, basicAuth)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .param("service_id", TestConstants.SERVICEDEFINITION_ID)
                .param("plan_id", TestConstants.SERVICEDEFINITION_PLAN_ID))
                .andExpect(status().is5xxServerError())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.description", is("Not Supported")))
                .andDo(print())
                .andReturn();
    }

}
