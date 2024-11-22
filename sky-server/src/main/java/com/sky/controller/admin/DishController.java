package com.sky.controller.admin;


import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.DishService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/dish")
@Slf4j
@Api(tags = "后台菜品管理")
public class DishController {

    @Autowired
    private DishService dishService;

    @PostMapping
    @ApiOperation(value = "新增菜品", notes = "新增菜品")
    public Result save(@RequestBody DishDTO dishDTO){
        log.info("save dishDTO: {}", dishDTO);
        dishService.saveWithFlavor(dishDTO);
        return Result.success();
    }

    @GetMapping("/page")
    @ApiOperation(value = "分页查询菜品", notes = "分页查询菜品")
    public Result<PageResult> page(DishPageQueryDTO dishPageQueryDTO){
        log.info("page dishVO: {}", dishPageQueryDTO);
        PageResult pageResult = dishService.pageQuery(dishPageQueryDTO);
        return Result.success(pageResult);
    }

    @DeleteMapping
    @ApiOperation(value = "删除菜品", notes = "删除菜品")
    public Result deleteBatch(@RequestParam List<Long> ids){
        log.info("delete dish by ids: {}", ids);
        dishService.deleteBatch(ids);
        return Result.success();
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "根据ID查询菜品", notes = "根据ID查询菜品")
    public Result<DishDTO> getById(@PathVariable Long id){
        log.info("get dish by id: {}", id);
        DishDTO dishDTO = dishService.getById(id);
        return Result.success(dishDTO);
    }

    @PutMapping
    @ApiOperation(value = "修改菜品", notes = "修改菜品")
    public Result update(@RequestBody DishDTO dishDTO){
        log.info("update dishDTO: {}", dishDTO);
        dishService.updateWihtFlavor(dishDTO);
        return Result.success();
    }

    @PostMapping("/status/{status}")
    @ApiOperation(value = "修改菜品状态", notes = "修改菜品状态")
    public Result updateStatus(@RequestParam Long id, @PathVariable Integer status){
        log.info("update dish status by id: {}, status: {}", id, status);
        dishService.updateStatus(id, status);
        return Result.success();
    }

    @GetMapping("/list")
    @ApiOperation(value = "查询所有菜品", notes = "查询所有菜品")
    public Result<List<Dish>> list(@RequestParam Long categoryId){
        log.info("list dish by categoryId: {}", categoryId);
        List<Dish> dishList = dishService.listByCategoryId(categoryId);
        return Result.success(dishList);
    }
}
