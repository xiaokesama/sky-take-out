package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.mapper.DishFlavorMapper;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealDishMapper;
import com.sky.result.PageResult;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@EnableTransactionManagement
public class DishServiceImpl implements DishService {

    @Autowired
    private DishMapper dishMapper;

    @Autowired
    private DishFlavorMapper dishFlavorMapper;

    @Autowired
    private SetmealDishMapper setmealDishMapper;

    /**
     * 条件查询菜品和口味
     * @param dish
     * @return
     */
    public List<DishVO> listWithFlavor(Dish dish) {
        List<Dish> dishList = dishMapper.listByCategoryId(dish.getCategoryId());

        List<DishVO> dishVOList = new ArrayList<>();

        for (Dish d : dishList) {
            DishVO dishVO = new DishVO();
            BeanUtils.copyProperties(d,dishVO);

            //根据菜品id查询对应的口味
            List<DishFlavor> flavors = dishFlavorMapper.selectByDishId(d.getId());

            dishVO.setFlavors(flavors);
            dishVOList.add(dishVO);
        }

        return dishVOList;
    }

    @Override
    @Transactional
    public void saveWithFlavor(DishDTO dishDTO) {
        log.info("save dish with flavor: {}", dishDTO);
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO, dish);

        dishMapper.insert(dish);

        dishDTO.getFlavors().forEach(flavor -> {
            System.out.println(flavor);
        });

        dishDTO.getFlavors().forEach(flavor -> {
            dishFlavorMapper.insertFlavor(flavor,dish.getId());
        });

    }

    @Override
    public PageResult pageQuery(DishPageQueryDTO dishPageQueryDTO) {
        PageHelper.startPage(dishPageQueryDTO.getPage(), dishPageQueryDTO.getPageSize());
        Page<DishVO> page = dishMapper.pageQuery(dishPageQueryDTO);
        return new PageResult(page.getTotal(), page.getResult());
    }

    @Override
    public void deleteBatch(List<Long> ids) {
        // 在售中不能删
        ids.forEach(id -> {
            Dish dish = dishMapper.selectByPrimaryKey(id);
            if (dish.getStatus() == 1) {
                throw new DeletionNotAllowedException(MessageConstant.SETMEAL_ON_SALE);
            }
        });

        // 在套餐中不能删
        setmealDishMapper.selectByDishIds(ids).forEach(setmealDish -> {
            throw new DeletionNotAllowedException(MessageConstant.DISH_BE_RELATED_BY_SETMEAL);
        });

        // 删除菜品
        dishMapper.deleteBatch(ids);

        // 删除风味
        dishFlavorMapper.deleteByDishIds(ids);
    }

    @Override
    public DishDTO getById(Long id) {
        DishDTO dishDTO = new DishDTO();
        Dish dish = dishMapper.selectByPrimaryKey(id);
        BeanUtils.copyProperties(dish, dishDTO);
        dishDTO.setFlavors(dishFlavorMapper.selectByDishId(id));
        return dishDTO;
    }

    @Override
    public void updateWihtFlavor(DishDTO dishDTO) {
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO, dish);
        dishMapper.update(dish);

        // 删除原有风味
        dishFlavorMapper.deleteByDishId(dish.getId());

        // 新增风味
        dishDTO.getFlavors().forEach(flavor -> {
            dishFlavorMapper.insertFlavor(flavor, dish.getId());
        });
    }

    @Override
    public void updateStatus(Long id, Integer status) {
        dishMapper.updateStatus(id, status);
    }

    @Override
    public List<Dish> listByCategoryId(Long categoryId) {
        return dishMapper.listByCategoryId(categoryId);
    }
}
