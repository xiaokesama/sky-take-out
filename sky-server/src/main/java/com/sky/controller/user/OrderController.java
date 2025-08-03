package com.sky.controller.user;


import com.sky.dto.OrdersSubmitDTO;
import com.sky.result.Result;
import com.sky.service.OrderService;
import com.sky.vo.OrderSubmitVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("userOrderController")
@RequestMapping("/user/order")
@Api(tags = "用户-订单")
@Slf4j
public class OrderController {

    @Autowired
    private OrderService orderService;

    @RequestMapping("/submit")
    @ApiOperation(value = "提交订单", notes = "提交订单")
    public Result<OrderSubmitVO> submit(@RequestBody OrdersSubmitDTO OrdersSubmitDTO) {
        log.info("提交订单：{}", OrdersSubmitDTO);
        OrderSubmitVO orderSubmitVO = orderService.submitOrder(OrdersSubmitDTO);
        return Result.success(orderSubmitVO);
    }
}
