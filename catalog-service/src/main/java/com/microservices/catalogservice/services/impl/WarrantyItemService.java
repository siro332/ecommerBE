package com.microservices.catalogservice.services.impl;

import com.microservices.catalogservice.models.dtos.OrderDto;
import com.microservices.catalogservice.models.dtos.ProductDto;
import com.microservices.catalogservice.models.dtos.WarrantyItemDto;
import com.microservices.catalogservice.models.entities.WarrantyItem;
import com.microservices.catalogservice.repositories.WarrantyItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class WarrantyItemService {
    private final ProductServiceImpl productService;
    private final OrderService orderService;
    private final WarrantyItemRepository warrantyItemRepository;
    public List<WarrantyItemDto> getByProductSerial(String serial){
        return warrantyItemRepository.findByProductSerialContains(serial).stream().map(this::getWarrantyItemDetail).collect(Collectors.toList());
    }

    public WarrantyItemDto getById(Long id){
        return getWarrantyItemDetail(warrantyItemRepository.findById(id));
    }

    public WarrantyItemDto addWarrantyItem(WarrantyItem warrantyItemInput){
        WarrantyItem warrantyItem = WarrantyItem.builder().productSerial(warrantyItemInput.getProductSerial()).productCode(warrantyItemInput.getProductCode()).orderId(warrantyItemInput.getOrderId()).build();
        return getWarrantyItemDetail(warrantyItemRepository.save(warrantyItem));
    }

    public void deleteItem(Long id){
        WarrantyItem warrantyItem = warrantyItemRepository.findById(id);
        warrantyItemRepository.delete(warrantyItem);
    }

    public WarrantyItemDto getWarrantyItemDetail(WarrantyItem warrantyItem) {
        Optional<ProductDto> optionalProductDto = productService.getProductDtoByCode(warrantyItem.getProductCode());
        if (optionalProductDto.isEmpty()){
            return null;
        }
        Optional<OrderDto> optionalOrderDto = orderService.getById(warrantyItem.getOrderId());
        if (optionalOrderDto.isEmpty()){
            return null;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(optionalOrderDto.get().getCreatedAt());
        calendar.add(Calendar.MONTH, optionalProductDto.get().getWarrantyPeriod());
        Date currentDate = new Date();
        if (calendar.getTime().compareTo(currentDate)<0){
            warrantyItem.setIsExpired(true);
            warrantyItemRepository.save(warrantyItem);
        }
        return WarrantyItemDto.builder()
                .id(warrantyItem.getId())
                .productSerial(warrantyItem.getProductSerial())
                .productDto(optionalProductDto.get())
                .orderDto(optionalOrderDto.get())
                .expiredDate(calendar.getTime())
                .isExpired(warrantyItem.getIsExpired())
                .build();
    }
}
