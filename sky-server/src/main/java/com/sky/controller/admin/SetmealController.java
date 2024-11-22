package com.sky.controller.admin;

import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.SetmealService;
import com.sky.vo.SetmealVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/setmeal")
@Api(tags = "后台-套餐管理")
@Slf4j
public class SetmealController {

    @Autowired
    private SetmealService setmealService;

    @PostMapping
    @ApiOperation(value = "保存套餐", notes = "保存套餐")
    public Result save(@RequestBody SetmealDTO setmealDTO){
        log.info("save setmealDTO: {}", setmealDTO);
        setmealService.save(setmealDTO);
        return Result.success();
    }

    @GetMapping("/page")
    @ApiOperation(value = "分页查询套餐", notes = "分页查询套餐")
    public Result<PageResult> page(@ModelAttribute SetmealPageQueryDTO setmealPageQueryDTO){
        log.info("page setmealPageQueryDTO: {}", setmealPageQueryDTO);
        PageResult pageResult = setmealService.page(setmealPageQueryDTO);
        return Result.success(pageResult);
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "根据id查询套餐", notes = "根据id查询套餐")
    public Result<SetmealVO> getById(@PathVariable Long id){
        log.info("get setmealDTO: {}", id);
        SetmealVO setmealVO = setmealService.getById(id);
        return Result.success(setmealVO);
    }

    @PutMapping
    @ApiOperation(value = "更新套餐", notes = "更新套餐")
    public Result update(@RequestBody SetmealDTO setmealDTO){
        log.info("update setmealDTO: {}", setmealDTO);
        setmealService.update(setmealDTO);
        return Result.success();
    }
}
