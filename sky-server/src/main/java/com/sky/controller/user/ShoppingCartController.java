package com.sky.controller.user;

import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.ShoppingCart;
import com.sky.result.Result;
import com.sky.service.ShoppingCartService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user/shoppingCart")
@Slf4j
@Api(tags = "C端 - 购物车模块")
public class ShoppingCartController {

    @Autowired
    private ShoppingCartService shoppingCartService;


    @PostMapping("/add")
    @ApiOperation(value = "添加购物车", notes = "添加购物车")
    public Result add(@RequestBody ShoppingCartDTO dto){
        log.info("添加购物车：{}", dto);
        shoppingCartService.add(dto);
        return Result.success();
    }

    @GetMapping("/list")
    @ApiOperation(value = "获取购物车列表", notes = "获取购物车列表")
    public Result<List<ShoppingCart>> list(){
        log.info("获取购物车列表");
        List<ShoppingCart> list = shoppingCartService.getCartItems();
        return Result.success(list);
    }

    @DeleteMapping("/clean")
    @ApiOperation(value = "清空购物车", notes = "清空购物车")
    public Result clean(){
        log.info("清空购物车");
        shoppingCartService.cleanShoppingCart();
        return Result.success();
    }
}
