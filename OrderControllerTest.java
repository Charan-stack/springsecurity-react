package com.springsecurity.react.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springsecurity.react.model.Order;
import com.springsecurity.react.order.OrderController;
import com.springsecurity.react.order.OrderServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class OrderControllerTest {

    @Mock
    OrderServiceImpl orderService;

    @Autowired
    MockMvc mockMvc;

    @InjectMocks
    OrderController orderController;

    static Order order = null;

    @BeforeEach
    public void setUp() {
       mockMvc = MockMvcBuilders.standaloneSetup(orderController).build();
    }

    @BeforeAll
    public static void beforeAll() {
        System.out.println("--Before All--");
        order = new Order();
        order.setId(1l);
        order.setStatus("Pending");
        order.setOrderItem("Product");
        order.setOrderDate("13/03/2025");
        order.setQuantity(10l);
        order.setAmount(100.00);
    }

    @Test
    void saveControllerTestCase() throws Exception {
        Mockito.when(orderService.save(Mockito.any(Order.class))).thenReturn(order);
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/order/save")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(order)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        Mockito.verify(orderService, Mockito.times(1)).save(order);
        Assertions.assertEquals(HttpStatus.OK.value(), mvcResult.getResponse().getStatus());
    }

    @Test
    void testGetAllTheData() throws Exception{
        List<Order> orderList = new ArrayList<>();
        orderList.add(order);
        Mockito.when(orderService.findAll()).thenReturn(orderList);
        MvcResult mockMvcObj = mockMvc.perform(MockMvcRequestBuilders.get("/order/findAll")).andExpect(status().isOk())
                .andReturn();
        Mockito.verify(orderService,Mockito.times(1)).findAll();
        Assertions.assertEquals(HttpStatus.OK.value(), mockMvcObj.getResponse().getStatus());
    }

    @Test
    void deleteTestCase() throws Exception{
        Mockito.doNothing().when(orderService).delete(2l);
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.delete("/order/delete/2"))
                .andReturn();
        Mockito.verify(orderService, Mockito.times(1)).delete(2l);
    }

    @Test
    void updateTestCase() throws Exception{
        Mockito.doNothing().when(orderService).update(2l, order);
        mockMvc.perform(MockMvcRequestBuilders.put("/order/update/2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(order)))
                .andReturn();
        Mockito.verify(orderService, Mockito.times(1)).update(2l, order);
    }
}
