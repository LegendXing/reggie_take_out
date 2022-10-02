package com.study.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.study.reggie.common.CustomException;
import com.study.reggie.entity.Category;
import com.study.reggie.entity.Dish;
import com.study.reggie.entity.Setmeal;
import com.study.reggie.mapper.CategoryMapper;
import com.study.reggie.service.CategoryService;
import com.study.reggie.service.DishService;
import com.study.reggie.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {
    @Autowired
    private DishService dishService;
    @Autowired
    private SetmealService setmealService;
    @Override
    public void remove(Long id) {
        LambdaQueryWrapper<Dish> DishQueryWrapper=new LambdaQueryWrapper<>();
        DishQueryWrapper.eq(Dish::getCategoryId, id);
        int count1 = dishService.count(DishQueryWrapper);
        if (count1 > 0) {
throw new CustomException("当前分类下关联了菜品，不可删除");
        }
        LambdaQueryWrapper<Setmeal> SetmealQueryWrapper=new LambdaQueryWrapper<>();
        SetmealQueryWrapper.eq(Setmeal::getCategoryId, id);
        int count2 = setmealService.count(SetmealQueryWrapper);
        if (count2 > 0) {
            throw new CustomException("当前分类下关联了套餐，不可删除");
        }
        super.removeById(id);
    }
}
