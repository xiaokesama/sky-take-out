package com.sky.controller.user;

import com.sky.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

@RestController("userShopController")
@RequestMapping("/user/shop")
@Slf4j
@Api(tags = "前台-商铺")
public class ShopController {

    private static final String KEY_SHOP_STATUS = "shop_status";

    @Autowired
    private RedisTemplate redisTemplate;

    @GetMapping("/status")
    @ApiOperation(value = "获取商铺状态", notes = "获取商铺状态")
    public Result<Integer> getShopStatue(){
        log.info("获取商铺状态成功");
        Integer status = (Integer) redisTemplate.opsForValue().get(KEY_SHOP_STATUS);
        return Result.success(status);
    }
}
