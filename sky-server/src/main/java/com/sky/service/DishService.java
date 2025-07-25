package com.sky.service;


import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.result.PageResult;
import com.sky.vo.DishVO;

import java.util.List;

public interface DishService {
    public void saveWithFlavor(DishDTO dishDTO);

    PageResult pageQuery(DishPageQueryDTO dishPageQueryDTO);

    void deleteBatch(List<Long> ids);

    DishDTO getById(Long id);

    void updateWihtFlavor(DishDTO dishDTO);

    void updateStatus(Long id, Integer status);

    List<Dish> listByCategoryId(Long categoryId);

    List<DishVO> listWithFlavor(Dish dish);
}
