package com.study.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.study.reggie.dto.DishDto;
import com.study.reggie.entity.Dish;

import java.util.List;


public interface DishService extends IService<Dish> {
    void saveWithFlavor(DishDto dishDto);
    public DishDto getDishByIdWithFlavor(Long id);

    void updateWithFlavor(DishDto dishDto);

    void updateDishStatusById(Integer status, List<Long> ids);

    void deleteByIds(List<Long> ids);
}
