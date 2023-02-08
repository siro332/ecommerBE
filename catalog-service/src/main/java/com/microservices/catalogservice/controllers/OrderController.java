package com.microservices.catalogservice.controllers;

import com.microservices.catalogservice.models.dtos.CartDto;
import com.microservices.catalogservice.models.dtos.OrderDto;
import com.microservices.catalogservice.models.entities.Cart;
import com.microservices.catalogservice.models.entities.Order;
import com.microservices.catalogservice.models.entities.user.User;
import com.microservices.catalogservice.services.impl.AuthenticationService;
import com.microservices.catalogservice.services.impl.CartServiceImpl;
import com.microservices.catalogservice.services.impl.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

@RestController
@RequestMapping("/api/order/order")
@RequiredArgsConstructor
public class OrderController {
    private final CartServiceImpl cartService;
    private final OrderService orderService;
    private final CartController cartController;
    private final AuthenticationService authenticationService;
    @GetMapping("")
    public ResponseEntity<?> getAllOrders(@RequestParam(defaultValue = "0") int page,
                                          @RequestParam(defaultValue = "3") int size){
        return ResponseEntity.ok(orderService.getAllOrder(PageRequest.of(page, size)));
    }


    @GetMapping("/user")
    public ResponseEntity<?> userCart(@RequestHeader("Authorization") String authentication){
        try {
            User user = authenticationService.getUserFromToken(authentication.replace("bearer ",""));
            if (user == null) throw new Exception();
            String userCode = user.getEmail();
            return ResponseEntity.ok(orderService.getAllByUserCode(userCode));
        }catch (Exception e){
            return ResponseEntity.badRequest().body("Cannot get user info");
        }
    }
    @PostMapping("/create")
    public ResponseEntity<?> createOrder(@RequestHeader("Authorization") String authentication, @RequestBody OrderDto order){
        try{
            User user = authenticationService.getUserFromToken(authentication.replace("bearer ",""));
            if (user == null) throw new Exception();
            String userCode = user.getEmail();
            CartDto cartDto = cartService.getUserCart(userCode);
            order.setCartItems(cartDto.getCartItemDtos());
            order.setTotals(cartDto.getTotals());
            order.setUserCode(userCode);
            Order newOrder = orderService.createOrder(order);
            cartController.clearCart(authentication);
            return ResponseEntity.ok(newOrder);
        }catch (Exception e){
            return ResponseEntity.badRequest().body("Error creating order: "+ e.getMessage());
        }
    }
    @PostMapping("/change-status/{code}")
    public ResponseEntity<?> changeStatus(@PathVariable String code, @RequestParam String status){
        try{
            return ResponseEntity.ok(orderService.changeStatus(code,status));
        }catch (Exception e){
            return ResponseEntity.badRequest().body("Error creating order: "+ e.getMessage());
        }
    }
    @PostMapping("/delete/{code}")
    public ResponseEntity<?> deleteOrder(@PathVariable String code){
        try{
            orderService.deleteOrder(code);
            return ResponseEntity.ok().body("");
        }catch (Exception e){
            return ResponseEntity.badRequest().body("Error deleting order: "+ e.getMessage());
        }
    }

    @GetMapping("/stats")
    public ResponseEntity<?> getOrderStat() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        HashMap<Integer,Long> result = new HashMap<>();
        while (calendar.get(Calendar.YEAR) == year){
            calendar.set(Calendar.DAY_OF_MONTH,1);
            Date from = calendar.getTime();
            calendar.set(Calendar.DAY_OF_MONTH,-1);
            Date to = calendar.getTime();
            Long count = (long) orderService.findByCreatedDateBetween(from, to).size();
            result.put(calendar.get(Calendar.MONTH),count);
            calendar.add(Calendar.MONTH,-1);
        }
        return ResponseEntity.ok(result);
    }

    @GetMapping("/income")
    public ResponseEntity<?> getOrderIncomeStat() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        HashMap<Integer,Long> result = new HashMap<>();
        while (calendar.get(Calendar.YEAR) == year){
            calendar.set(Calendar.DAY_OF_MONTH,1);
            Date from = calendar.getTime();
            calendar.set(Calendar.DAY_OF_MONTH,-1);
            Date to = calendar.getTime();
            Long income = (long) orderService.findByCreatedDateBetween(from, to).stream().mapToDouble(Order::getTotals).sum();
            result.put(calendar.get(Calendar.MONTH),income);
            calendar.add(Calendar.MONTH,-1);
        }
        return ResponseEntity.ok(result);
    }
}
