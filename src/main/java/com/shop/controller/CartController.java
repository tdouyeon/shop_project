package com.shop.controller;

import com.shop.dto.CartDetailDto;
import com.shop.dto.CartItemDto;
import com.shop.dto.CartOrderDto;
import com.shop.service.CartService;
import jakarta.validation.Valid;
import kotlinx.serialization.Serializable;
import lombok.RequiredArgsConstructor;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import org.slf4j.Logger;

@Controller
@RequiredArgsConstructor
public class CartController {
    private final CartService cartService;
    private static final Logger logger = LoggerFactory.getLogger(CartController.class);


    @PostMapping(value = "/cart")
    public ResponseEntity<String> order(@RequestBody @Valid CartItemDto cartItemDto,
                         BindingResult bindingResult, Principal principal) {
        if (bindingResult.hasErrors()) {
            StringBuilder sb = new StringBuilder();
            List<FieldError> fieldErrors = bindingResult.getFieldErrors();
            for (FieldError fieldError : fieldErrors) {
                sb.append(fieldError.getDefaultMessage());
            }
            return new ResponseEntity<String>(sb.toString(), HttpStatus.BAD_REQUEST);
        }
        Long cartItemId;
        try {
            cartItemId = cartService.addCart(cartItemDto, principal);
            System.out.println("카트아이디가 뭐야?"+cartItemId);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            logger.error("An error occurred: {}", e.getMessage(), e);
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(value = "/cart")
    public String orderHist(Principal principal, Model model){
        List<CartDetailDto> cartDetailDtoList = cartService.getCartList(principal);
        model.addAttribute("cartItems",cartDetailDtoList);
        return "cart/cartList";
    }
    @PatchMapping(value = "/cartItem/{cartItemId}")
    public ResponseEntity<String> updateCartItem(@PathVariable("cartItemId") Long cartItemId,
                                                       int count, Principal principal) {
        System.out.println(cartItemId);
        if (count <= 0) {
            return new ResponseEntity<String>("최소 1개이상 담아주세요.", HttpStatus.BAD_REQUEST);
        } else if (!cartService.validateCartItem(cartItemId, principal)) {
            return new ResponseEntity<String>("수정권한이 없습니다.", HttpStatus.FORBIDDEN);
        }

        cartService.updateCartItemCount(cartItemId, count);
        return ResponseEntity.noContent().build();
    }
    @DeleteMapping(value = "/cartItem/{cartItemId}")
    public ResponseEntity<String> deleteCartItem(@PathVariable("cartItemId") Long cartItemId,
                                                       Principal principal){
        if (!cartService.validateCartItem(cartItemId, principal)) {
            return new ResponseEntity<String>("수정권한이 없습니다.", HttpStatus.FORBIDDEN);
        }
        cartService.deleteCartItem(cartItemId);
        return ResponseEntity.noContent().build();
    }
    // cartList.html -> CartController -> CartService -> OrderService
    // -> CartService -> CartController -> carList.html
    @PostMapping(value = "/cart/orders")
    public ResponseEntity<String> orderCartItem(@RequestBody CartOrderDto cartOrderDto,
                                                      Principal principal){
        System.out.println(cartOrderDto.getCartItemId());
        List<CartOrderDto> cartOrderDtoList = cartOrderDto.getCartOrderDtoList();

        if(cartOrderDtoList == null || cartOrderDtoList.size() == 0){
            return new ResponseEntity<String>("주문할 상품을 선택해주세요.",HttpStatus.FORBIDDEN);
        }
        for(CartOrderDto cartOrder : cartOrderDtoList){
            if(!cartService.validateCartItem(cartOrder.getCartItemId(), principal)){
                return new ResponseEntity<String>("주문 권한이 없습니다.",HttpStatus.FORBIDDEN);
            }
        }
        Long orderId;
        try {
            orderId = cartService.orderCartItem(cartOrderDtoList, principal);
        }
        catch (Exception e){
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.noContent().build();
    }
}
