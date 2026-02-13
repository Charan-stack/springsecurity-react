package com.springsecurity.react.service;

import com.springsecurity.react.exception.OrderRunTimeException;
import com.springsecurity.react.model.Inventory;
import com.springsecurity.react.model.Order;
import com.springsecurity.react.order.OrderServiceImpl;
import com.springsecurity.react.inventory.InventoryRepository;
import com.springsecurity.react.order.OrderRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

    @Mock
    InventoryRepository inventoryRepository;

    @Mock
    OrderRepository orderRepository; //Mock Bean will create mock object in for repository

    @InjectMocks
    OrderServiceImpl orderService; //Mock objects will inject in service by using @InjectMocks

    static Order order = null;

    @BeforeAll
    public static void init() {
        System.out.println("--Before All--");
        order = new Order();
        order.setId(1l);
        order.setOrderDate("13/03/2025");
        order.setOrderItem("Product");
        order.setAmount(100.00);
        order.setStatus("Pending");
        order.setQuantity(100l);
        order.setCreatedBy("Test");
        order.setCreatedDate("10/03/2025");
    }

    @BeforeEach
    public void beforeEach() {
        System.out.println("---Before Each---");
        MockitoAnnotations.openMocks(this);
    }
    @Test
    void saveOrdersTestCase() {
        order.setId(1L);
        order.setQuantity(10L);
        order.setOrderItem("Product");
        Inventory inventory = mock(Inventory.class);
        when(inventory.getQuantity()).thenReturn(20L);
        when(inventoryRepository.findByProduct("Product")).thenReturn(inventory);
        when(orderRepository.save(order)).thenReturn(order);
        Order savedOrder = orderService.save(order);
        Assertions.assertEquals(order.getId(), savedOrder.getId());  // Ensure the order ID matches
        Assertions.assertEquals(order.getQuantity(), savedOrder.getQuantity());  // Ensure the order quantity matches
    }

    @Test
    void negativeQuantityTestCase() {

        Order orderObj = new Order();
        orderObj.setQuantity(300l);
        orderObj.setOrderItem("Product");

        Inventory inventory = Mockito.mock(Inventory.class);
        inventory.setProduct("Product");
        inventory.setQuantity(200l);
        inventory.setId(1l);

        Mockito.when(inventoryRepository.findByProduct(orderObj.getOrderItem())).thenReturn(inventory);
        OrderRunTimeException orderRunTimeException = Assertions.assertThrows(OrderRunTimeException.class, () -> {
            orderService.save(orderObj);
        });
        Assertions.assertEquals("Quantity is Exceeded please check in Inventory..!", orderRunTimeException.getMessage());
    }

    @Test
    void findAllTestCase() {
        List<Order> orderList = new ArrayList<>();
        when(orderRepository.findAll()).thenReturn(orderList);
        List<Order> orders = orderService.findAll();
        Assertions.assertTrue(orders.isEmpty(), "The list is empty");
    }

    @Test
    void deleteTestCase() {
        Mockito.doNothing().when(orderRepository).deleteById(1l);
        orderService.delete(1l);
        Mockito.verify(orderRepository, Mockito.times(1)).deleteById(1l);
    }

    @Test
    void updateTestCase() {
        Mockito.when(orderRepository.findById(1l)).thenReturn(Optional.of(order));
        Mockito.when(orderRepository.save(order)).thenReturn(order);
        orderService.update(1l, order);
        Mockito.verify(orderRepository, times(1)).save(order);
    }

    @Test
    void updateTestCaseWhenOrderNotFound() {
        Order orderUpdateObj = new Order();
        orderUpdateObj.setId(1l);
        Mockito.when(orderRepository.findById(1l)).thenReturn(Optional.empty());
        orderService.update(1l, orderUpdateObj);
        Mockito.verify(orderRepository, Mockito.times(0)).save(orderUpdateObj);
    }

}
