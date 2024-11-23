package com.sky.controller.admin;

import com.sky.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

@RestController("adminShopController")
@RequestMapping("/admin/shop")
@Slf4j
@Api(tags = "后台-商铺管理")
public class ShopController {

    private static final String KEY_SHOP_STATUS = "shop_status";

    @Autowired
    private RedisTemplate redisTemplate;

    @PutMapping("/{status}")
    @ApiOperation(value = "更新商铺状态", notes = "更新商铺状态，0-关闭，1-开启")
    public Result updateShopStatue(@PathVariable Integer status){
        log.info("更新商铺状态成功，新状态为：{}", status);
        redisTemplate.opsForValue().set(KEY_SHOP_STATUS, status);
        return Result.success();
    }

    @GetMapping("/status")
    @ApiOperation(value = "获取商铺状态", notes = "获取商铺状态，0-关闭，1-开启")
    public Result<Integer> getShopStatue(){
        log.info("获取商铺状态成功");
        Integer status = (Integer) redisTemplate.opsForValue().get(KEY_SHOP_STATUS);
        return Result.success(status);
    }
}
