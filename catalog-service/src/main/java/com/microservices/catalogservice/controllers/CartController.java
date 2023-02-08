package com.microservices.catalogservice.controllers;

import com.microservices.catalogservice.models.dtos.CartDto;
import com.microservices.catalogservice.models.entities.Cart;
import com.microservices.catalogservice.models.entities.CartItem;
import com.microservices.catalogservice.models.entities.user.User;
import com.microservices.catalogservice.services.impl.AuthenticationService;
import com.microservices.catalogservice.services.impl.CartServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping("/api/order/cart")
@RequiredArgsConstructor
public class CartController {

    @GetMapping("")
    public ResponseEntity<?> getAllCart(){
        return ResponseEntity.ok(redisTemplate.keys("*"));
    }
    private final CartServiceImpl cartService;
    private final AuthenticationService authenticationService;
    private final RedisTemplate<String, Cart> redisTemplate;

    @GetMapping("/user")
        public ResponseEntity<?> userCart(@RequestHeader("Authorization") String authentication){
        User user = authenticationService.getUserFromToken(authentication.replace("bearer ",""));
        if (user != null){
            CartDto cartDto = cartService.getUserCart(user.getEmail());
            return ResponseEntity.ok(cartDto);
        }
        else return ResponseEntity.badRequest().body("Token not valid");
    }

    @PostMapping("/addToCart")
    public ResponseEntity<?> addToCart(@RequestHeader("Authorization") String authentication,@RequestBody CartItem cartItem){
        try{
            User user = authenticationService.getUserFromToken(authentication.replace("bearer ",""));
            if (user == null) throw new Exception();
            String userCode = user.getEmail();
            if (Boolean.FALSE.equals(redisTemplate.hasKey(userCode))){
                Cart cart = new Cart();
                cart.setUserCode(userCode);
                cart.setCartItemList(new ArrayList<>());
                cart.getCartItemList().add(cartItem);
                redisTemplate.opsForValue().set(userCode,cart);
            }else {
                Cart cart = redisTemplate.opsForValue().get(userCode);
                assert cart != null;
                Optional<CartItem> optionalCartItem = cart.getCartItemList().stream().filter(cartItem1 -> Objects.equals(cartItem1.getProductCode(), cartItem.getProductCode()) && Objects.equals(cartItem1.getProductInventory(), cartItem.getProductInventory())).findFirst();
                if (optionalCartItem.isPresent()){
                    cartItem.setUnits(optionalCartItem.get().getUnits()+cartItem.getUnits());
                    cart.getCartItemList().remove(optionalCartItem.get());
                }
                cart.getCartItemList().add(cartItem);
                redisTemplate.opsForValue().set(userCode,cart);
            }
            return ResponseEntity.ok(redisTemplate.opsForValue().get(userCode));
        }catch (Exception e){
            return ResponseEntity.badRequest().body("Error adding to cart: "+ e.getMessage());
        }
    }
    @PostMapping("/updateCart")
    public ResponseEntity<?> updateCart(@RequestHeader("Authorization") String authentication, @RequestBody List<CartItem> cartItemList){
        try{
            User user = authenticationService.getUserFromToken(authentication.replace("bearer ",""));
            if (user == null) throw new Exception();
            String userCode = user.getEmail();
            if (Boolean.FALSE.equals(redisTemplate.hasKey(userCode))){
                Cart cart = new Cart();
                cart.setUserCode(userCode);
                cart.setCartItemList(new ArrayList<>());
                redisTemplate.opsForValue().set(userCode,cart);
            }else {
                Cart cart = redisTemplate.opsForValue().get(userCode);
                assert cart != null;
                cart.setCartItemList(cartItemList);
                redisTemplate.opsForValue().set(userCode,cart);
            }
            return ResponseEntity.ok(redisTemplate.opsForValue().get(userCode));
        }catch (Exception e){
            return ResponseEntity.badRequest().body("Error adding to cart: "+ e.getMessage());
        }
    }
    @PostMapping("/deleteItem")
    public ResponseEntity<?> deleteItem(@RequestHeader("Authorization") String authentication,@RequestBody CartItem cartItem){
        try{
            User user = authenticationService.getUserFromToken(authentication.replace("bearer ",""));
            if (user == null) throw new Exception();
            String userCode = user.getEmail();
            if (Boolean.FALSE.equals(redisTemplate.hasKey(userCode))){
                Cart cart = new Cart();
                cart.setUserCode(userCode);
                cart.setCartItemList(new ArrayList<>());
                cart.getCartItemList().remove(cartItem);
                redisTemplate.opsForValue().set(userCode,cart);
            }else {
                Cart cart = redisTemplate.opsForValue().get(userCode);
                assert cart != null;
                cart.getCartItemList().remove(cartItem);
                redisTemplate.opsForValue().set(userCode,cart);
            }
            return ResponseEntity.ok(redisTemplate.opsForValue().get(userCode));
        }catch (Exception e){
            return ResponseEntity.badRequest().body("Error deleting item from cart: "+ e.getMessage());
        }
    }
    @PostMapping("/updateItem")
    public ResponseEntity<?> updateItem(@RequestHeader("Authorization") String authentication,@RequestBody CartItem cartItem){
        try{
            User user = authenticationService.getUserFromToken(authentication.replace("bearer ",""));
            if (user == null) throw new Exception();
            String userCode = user.getEmail();
            if (Boolean.FALSE.equals(redisTemplate.hasKey(userCode))){
                Cart cart = new Cart();
                cart.setUserCode(userCode);
                cart.setCartItemList(new ArrayList<>());
                redisTemplate.opsForValue().set(userCode,cart);
            }else {
                Cart cart = redisTemplate.opsForValue().get(userCode);
                assert cart != null;
                CartItem oldCartItem = cart.getCartItemList().stream().filter(cartItem1 -> Objects.equals(cartItem1.getProductCode(), cartItem.getProductCode()) && Objects.equals(cartItem1.getProductInventory(), cartItem.getProductInventory())).findFirst().get();
                if(cartItem.getUnits() == 0){
                    cart.getCartItemList().remove(oldCartItem);
                }else {
                    cart.getCartItemList().remove(oldCartItem);
                    cart.getCartItemList().add(cartItem);
                }
                redisTemplate.opsForValue().set(userCode,cart);
            }
            return ResponseEntity.ok(redisTemplate.opsForValue().get(userCode));
        }catch (Exception e){
            return ResponseEntity.badRequest().body("Error adding to cart: "+ e.getMessage());
        }
    }
    @PostMapping("/clearCart")
    public ResponseEntity<?> clearCart(@RequestHeader("Authorization") String authentication){
        try{
            User user = authenticationService.getUserFromToken(authentication.replace("bearer ",""));
            if (user == null) throw new Exception();
            String userCode = user.getEmail();
            if (Boolean.FALSE.equals(redisTemplate.hasKey(userCode))){
                Cart cart = new Cart();
                cart.setUserCode(userCode);
                cart.setCartItemList(new ArrayList<>());
                redisTemplate.opsForValue().set(userCode,cart);
            }else {
                Cart cart = redisTemplate.opsForValue().get(userCode);
                assert cart != null;
                cart.setCartItemList(new ArrayList<>());
                redisTemplate.opsForValue().set(userCode,cart);
            }
            return ResponseEntity.ok(CartDto.builder().userCode(userCode).cartItemDtos(new ArrayList<>()).build());
        }catch (Exception e){
            return ResponseEntity.badRequest().body("Error clearing cart: "+ e.getMessage());
        }
    }
}
