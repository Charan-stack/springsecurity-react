package com.springsecurity.react.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springsecurity.react.inventory.InventoryController;
import com.springsecurity.react.model.Inventory;
import com.springsecurity.react.inventory.InventoryServiceImpl;
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

@ExtendWith(MockitoExtension.class)
public class InventoryControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Mock
    InventoryServiceImpl inventoryService;

    @InjectMocks
    InventoryController inventoryController;

    static Inventory inventory = null;

    @BeforeAll
    public static void init() {
        inventory = new Inventory();
        inventory.setId(1l);
        inventory.setQuantity(100l);
        inventory.setPrice(100.00);
        inventory.setCreatedBy("Test");
        inventory.setCreatedDate("13/08/2025");
        inventory.setProduct("Test Product");
    }

    @BeforeEach
    public void beforeEach() {
        mockMvc = MockMvcBuilders.standaloneSetup(inventoryController).build();
    }

    @Test
    void findAllTestCase() throws Exception {
        List<Inventory> inventoryLists = new ArrayList<>();
        inventoryLists.add(inventory);
        Mockito.when(inventoryService.findAll()).thenReturn(inventoryLists);
        List<Inventory> inventoryList = inventoryService.findAll();
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/inventory/findAll"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        Assertions.assertEquals(HttpStatus.OK.value(), mvcResult.getResponse().getStatus());
    }

    @Test
    void saveTestCase() throws Exception {

        Mockito.when(inventoryService.save(inventory)).thenReturn(inventory);
        inventoryService.save(inventory);
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/inventory/save")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(inventory)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        Assertions.assertEquals(HttpStatus.OK.value(), mvcResult.getResponse().getStatus());
    }

    @Test
    void updateTestCase() throws Exception {
        Mockito.doNothing().when(inventoryService).update(inventory,2l);
        mockMvc.perform(MockMvcRequestBuilders.put("/inventory/update/2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(inventory)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        Mockito.verify(inventoryService, Mockito.times(1)).update(inventory, 2l);
    }
//
//    @Test
//    void updateNegativeCase() throws Exception{
//        Mockito.doNothing().when()
//        mockMvc.perform(MockMvcRequestBuilders.put("/inventory/update/2")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(new ObjectMapper().writeValueAsString(inventory)))
//                .andReturn();
//    }

    @Test
    void deleteTestCase() throws Exception {
        Mockito.doNothing().when(inventoryService).delete(1l);
        mockMvc.perform(MockMvcRequestBuilders.delete("/inventory/delete/1"))
                .andReturn();
        Mockito.verify(inventoryService, Mockito.times(1)).delete(1l);
    }


}
