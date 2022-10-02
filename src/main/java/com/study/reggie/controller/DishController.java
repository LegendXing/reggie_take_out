package com.study.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.study.reggie.common.R;
import com.study.reggie.dto.DishDto;
import com.study.reggie.entity.Category;
import com.study.reggie.entity.Dish;
import com.study.reggie.entity.DishFlavor;
import com.study.reggie.service.CategoryService;
import com.study.reggie.service.DishFlavorService;
import com.study.reggie.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@Slf4j
@RequestMapping("/dish")
public class DishController {
    @Autowired
    private DishFlavorService dishFlavorService;
    @Autowired
    private DishService dishService;
    @Autowired
    private CategoryService categoryService;

    @PostMapping
    public R<String> save(@RequestBody DishDto dishDto) {
        log.info(dishDto.toString());
        dishService.saveWithFlavor(dishDto);
        return R.success("新增菜品成功");
    }

    @GetMapping("/page")
    public R<Page> page(int page, int pageSize, String name) {
        //添加分页构造器对象
        Page<Dish> pageInfo = new Page<Dish>(page, pageSize);
        Page<DishDto> dishDtoPage = new Page<>();
        //条件构造器
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        //过滤条件
        queryWrapper.like(name != null, Dish::getName, name);

        queryWrapper.orderByDesc(Dish::getUpdateTime);
        dishService.page(pageInfo, queryWrapper);
        BeanUtils.copyProperties(pageInfo, dishDtoPage, "records");
        List<Dish> records = pageInfo.getRecords();
        List<DishDto> list = records.stream().map((item) -> {
            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(item,dishDto);
            Long categoryId = item.getCategoryId();
            Category category = categoryService.getById(categoryId);
            if (category != null) {
                String categoryName = category.getName();
                dishDto.setCategoryName(categoryName);
            }

            return dishDto;
        }).collect(Collectors.toList());

        dishDtoPage.setRecords(list);

        return R.success(dishDtoPage);
    }
    @GetMapping("/{id}")
public R<DishDto> get(@PathVariable Long id){
    DishDto dishDto = dishService.getDishByIdWithFlavor(id);

    return R.success(dishDto);
}
    @PutMapping
    public R<String> update(@RequestBody DishDto dishDto) {
        log.info(dishDto.toString());
        dishService.updateWithFlavor(dishDto);
        return R.success("修改菜品成功");
    }
//    @GetMapping("/list")
//    public R<List<Dish>> list(Dish dish){
//        LambdaQueryWrapper<Dish> queryWrapper =new LambdaQueryWrapper<>();
//        queryWrapper.eq(dish.getCategoryId()!= null,Dish::getCategoryId,dish.getCategoryId());
//        queryWrapper.eq(Dish::getStatus,1);
//        queryWrapper.orderByAsc(Dish::getSort).orderByDesc(Dish::getCreateTime);
//        List<Dish> list = dishService.list(queryWrapper);
//
//        return R.success(list);
//
//    }
@GetMapping("/list")
public R<List<DishDto>> list(Dish dish){
    LambdaQueryWrapper<Dish> queryWrapper =new LambdaQueryWrapper<>();
    queryWrapper.eq(dish.getCategoryId()!= null,Dish::getCategoryId,dish.getCategoryId());
    queryWrapper.eq(Dish::getStatus,1);
    queryWrapper.orderByAsc(Dish::getSort).orderByDesc(Dish::getCreateTime);
    List<Dish> list = dishService.list(queryWrapper);

    List<DishDto> dishDtoList= list.stream().map((item) -> {
        DishDto dishDto = new DishDto();
        BeanUtils.copyProperties(item,dishDto);
        Long categoryId = item.getCategoryId();
        Category category = categoryService.getById(categoryId);
        if (category != null) {
            String categoryName = category.getName();
            dishDto.setCategoryName(categoryName);
        }
        Long dishId = item.getId();
        DishDto flavor = dishService.getDishByIdWithFlavor(dishId);

        return flavor;
    }).collect(Collectors.toList());

    return R.success(dishDtoList);

}
    @PostMapping("/status/{status}")

    public R<String> status(@PathVariable("status") Integer status,@RequestParam List<Long> ids){
        dishService.updateDishStatusById(status,ids);
        return R.success("售卖状态修改成功");
    }
    @DeleteMapping
    public R<String> delete(@RequestParam("ids") List<Long> ids){
        dishService.deleteByIds(ids);
        LambdaQueryWrapper<DishFlavor> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.in(DishFlavor::getDishId,ids);
        dishFlavorService.remove(queryWrapper);
        return R.success("删除菜品成功");
    }
}
