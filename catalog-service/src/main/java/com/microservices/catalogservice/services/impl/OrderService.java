package com.microservices.catalogservice.services.impl;

import com.microservices.catalogservice.common.Utils;
import com.microservices.catalogservice.converters.DtoConverter;
import com.microservices.catalogservice.models.dtos.CartItemDto;
import com.microservices.catalogservice.models.dtos.OrderDto;
import com.microservices.catalogservice.models.entities.Order;
import com.microservices.catalogservice.models.entities.product_inventory.ProductInventory;
import com.microservices.catalogservice.repositories.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.aspectj.weaver.ast.Or;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final ProductInventoryService productInventoryService;
    private final DtoConverter dtoConverter;
    public Order createOrder(OrderDto orderDto){
        Order order = dtoConverter.orderDtoToEntity(orderDto);
        UUID uuid =UUID.randomUUID();
        order.setCode("OD"+ Utils.uuidToBase64(uuid));
        return orderRepository.save(order);
    }
    public OrderDto getByCode(String code){
        return dtoConverter.orderEntityToDto(orderRepository.findByCodeAndIsDeletedIsFalse(code));
    };

    public OrderDto changeStatus(String code, String status){
        Order order = orderRepository.findByCodeAndIsDeletedIsFalse(code);
        order.setStatus(status);
        OrderDto orderDto = dtoConverter.orderEntityToDto(orderRepository.save(order));
        if (status.equalsIgnoreCase("delivered")){
            for (CartItemDto cartItemDto: orderDto.getCartItems()
                 ) {
                ProductInventory productInventory = productInventoryService.getBySku(cartItemDto.getInventoryItem().getSku());
                productInventory.setUnits(productInventory.getUnits() - cartItemDto.getUnits());
                productInventory.setUnitSold(productInventory.getUnitSold() + cartItemDto.getUnits());
                productInventoryService.save(productInventory);
            }
        }
        return orderDto;
    }
    public Page<OrderDto> getAllOrder(Pageable pageable){
        Page<Order> orderPage = orderRepository.findAll(pageable);
        return orderPage.map(dtoConverter::orderEntityToDto);
    }
    public List<Order> getAllByUserCode(String userCode){
        return orderRepository.findByUserCodeAndIsDeletedIsFalse(userCode);
    }


    public void deleteOrder(String code) {
        Order order = orderRepository.findByCodeAndIsDeletedIsFalse(code);
        order.setIsDeleted(true);
        orderRepository.save(order);
    }

    public List<Order> findByCreatedDateBetween(Date from, Date to) {
        return orderRepository.findByCreatedAtBetween(from,to);
    }
}
