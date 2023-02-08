package com.microservices.catalogservice.services.impl;

import com.microservices.catalogservice.models.dtos.CartDto;
import com.microservices.catalogservice.models.dtos.CartItemDto;
import com.microservices.catalogservice.models.dtos.ProductDto;
import com.microservices.catalogservice.models.entities.Cart;
import com.microservices.catalogservice.models.entities.CartItem;
import com.microservices.catalogservice.models.entities.product_inventory.ProductInventory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartServiceImpl {
    private final ProductServiceImpl productClient;
    private final RedisTemplate<String, Cart> redisTemplate;


    public CartItemDto getDetailCartItem(CartItem cartItem){
            CartItemDto cartItemDto = new CartItemDto();
        Optional<ProductDto> productDto = productClient.getProductDtoByCode(cartItem.getProductCode());
        if (productDto.isPresent()){
            cartItemDto.setProductDto(productDto.get());
        }
        Optional<ProductInventory> optionalProductInventory = productDto.get().getProductInventories().stream().filter(productInventory -> productInventory.getSku().equals(cartItem.getProductInventory())).findFirst();
        if (optionalProductInventory.isPresent()){
            cartItemDto.setInventoryItem(optionalProductInventory.get());
        }
        cartItemDto.setUnits(cartItem.getUnits());
        return cartItemDto;
    }
    public CartDto getUserCart(String userCode){
        CartDto cartDto = CartDto.builder().userCode(userCode).build();
        if (Boolean.FALSE.equals(redisTemplate.hasKey(userCode))){
            Cart cart = new Cart();
            cart.setUserCode(userCode);
            cart.setCartItemList(new ArrayList<>());
            redisTemplate.opsForValue().set(userCode,cart);
            cartDto.setCartItemDtos(new ArrayList<>());
        }else {
            Cart cart = redisTemplate.opsForValue().get(userCode);
            List<CartItemDto> cartItemDtos = new ArrayList<>();
            for (CartItem cartItem: cart.getCartItemList()
                 ) {
                cartItemDtos.add(getDetailCartItem(cartItem));
            }
            cartDto.setCartItemDtos(cartItemDtos);
        }
        double sum = 0;
        for (CartItemDto cartItemDto: cartDto.getCartItemDtos()
             ) {
           sum += cartItemDto.getUnits() * cartItemDto.getInventoryItem().getRetailPrice();
        }
        cartDto.setTotals(sum);
        return cartDto;
    }
}
