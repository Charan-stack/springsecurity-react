package com.springsecurity.react.service;

import com.springsecurity.react.inventory.InventoryServiceImpl;
import com.springsecurity.react.model.Inventory;
import com.springsecurity.react.inventory.InventoryRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class InventoryServiceImplTest {

    @Mock
    InventoryRepository inventoryRepository;

    @InjectMocks
    InventoryServiceImpl inventoryService;

    @Value("${spring.profiles.active}")
    private String activeProfile;

    static Inventory inventory = null;

    @BeforeAll
    static void init() {
        inventory = new Inventory();
        inventory.setId(1l);
        inventory.setQuantity(100l);
        inventory.setPrice(100.00);
        inventory.setCreatedBy("Test");
        inventory.setCreatedDate("13/08/2025");
        inventory.setProduct("Test Product");
    }

    @Test
    void findAllTestCase() {
        List<Inventory> inventoryLists = new ArrayList<>();
        inventoryLists.add(inventory);
        Mockito.when(inventoryRepository.findAll()).thenReturn(inventoryLists);
        List<Inventory> inventoryList = inventoryService.findAll();
        Mockito.verify(inventoryRepository, Mockito.times(1)).findAll();
        Assertions.assertEquals(inventoryLists, inventoryList);
        Assertions.assertEquals("jdbc", activeProfile);
        Assertions.assertNotNull(activeProfile);
    }

    @Test
    void saveTestCase() {
        Mockito.when(inventoryRepository.save(inventory)).thenReturn(inventory);
        Inventory inventoryObj = inventoryService.save(inventory);
        Assertions.assertEquals(inventory, inventoryObj);
    }

    @Test
    void updateTestCase() {
        Inventory inventoryObj = new Inventory();
        inventoryObj.setId(1l);
        Mockito.when(inventoryRepository.findById(1l)).thenReturn(Optional.of(inventoryObj));
        inventoryService.update(inventoryObj,1l);
        Mockito.verify(inventoryRepository, Mockito.times(1)).findById(1l);
    }

    @Test
    void updateNegativeTestCase() {
        Inventory inventoryObj = new Inventory();
        inventoryObj.setId(1l);
        Mockito.when(inventoryRepository.findById(1l)).thenReturn(Optional.empty());
        inventoryService.update(inventoryObj, 1l);
        Mockito.verify(inventoryRepository, Mockito.times(0)).save(inventoryObj);
    }

    @Test
    void deleteTestCase() {
        Mockito.doNothing().when(inventoryRepository).deleteById(1l);
        inventoryService.delete(1l);
        Mockito.verify(inventoryRepository, Mockito.times(1)).deleteById(1l);
    }
}
