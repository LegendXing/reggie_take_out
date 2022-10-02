package com.study.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.study.reggie.common.R;

import com.study.reggie.dto.DishDto;
import com.study.reggie.dto.SetmealDto;
import com.study.reggie.entity.*;
import com.study.reggie.service.CategoryService;
import com.study.reggie.service.DishService;
import com.study.reggie.service.SetmealDishService;
import com.study.reggie.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


@RestController
@Slf4j
@RequestMapping("/setmeal")
public class SetmealController {
    @Autowired
    private DishService dishService;
    @Autowired
    private SetmealDishService setmealDishService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private SetmealService setmealService;
    @PostMapping
    public R<String> save(@RequestBody SetmealDto setmealDto){
setmealService.saveWithDish(setmealDto);
        return R.success("套餐新增成功");
    }
    @GetMapping("/page")
    public R<Page> page(int page, int pageSize, String name){
        Page<Setmeal> pageInfo = new Page<>(page,pageSize);
        Page<SetmealDto> pageDto = new Page<>();
        LambdaQueryWrapper<Setmeal> queryWrapper =new LambdaQueryWrapper<>();
queryWrapper.like(name!= null,Setmeal::getName,name);
        queryWrapper.orderByDesc(Setmeal::getUpdateTime);
        setmealService.page(pageInfo, queryWrapper);
        BeanUtils.copyProperties(pageInfo, pageDto,"records");
        List<Setmeal> records = pageInfo.getRecords();
        List<SetmealDto> list=records.stream().map((item) ->{
            SetmealDto setmealDto = new SetmealDto();
            BeanUtils.copyProperties(item, setmealDto);
            Long categoryId = item.getCategoryId();
            Category category = categoryService.getById(categoryId);
            if (category != null) {
                String categoryName = category.getName();
                setmealDto.setCategoryName(categoryName);
            }
            return setmealDto;
        }).collect(Collectors.toList());
       pageDto.setRecords(list);
        return R.success(pageDto);
    }
    @DeleteMapping
    public R<String> delete(@RequestParam List<Long> ids){
        log.info("ids:{}",ids);
        setmealService.removeWithDish(ids);
        return R.success("套餐删除成功");
    }
    /**
     * 对菜品批量或者是单个 进行停售或者是起售
     * @return
     */
    @PostMapping("/status/{status}")
//这个参数这里一定记得加注解才能获取到参数，否则这里非常容易出问题
    public R<String> status(@PathVariable("status") Integer status,@RequestParam List<Long> ids){
        setmealService.updateSetmealStatusById(status,ids);
        return R.success("售卖状态修改成功");
    }
    @GetMapping("/list")
    public R<List<Setmeal>> list(Setmeal setmeal) {
LambdaQueryWrapper<Setmeal> queryWrapper =new LambdaQueryWrapper<>();
queryWrapper.eq(setmeal.getCategoryId()!= null,Setmeal::getCategoryId,setmeal.getCategoryId());
queryWrapper.eq(setmeal.getStatus()!= null,Setmeal::getStatus,setmeal.getStatus());
queryWrapper.orderByDesc(Setmeal::getUpdateTime);
        List<Setmeal> list = setmealService.list(queryWrapper);
        return R.success(list);
    }

    /**
     * 移动端点击套餐图片查看套餐具体内容
     * 这里返回的是dto 对象，因为前端需要copies这个属性
     * 前端主要要展示的信息是:套餐中菜品的基本信息，图片，菜品描述，以及菜品的份数
     * @param SetmealId
     * @return
     */
    //这里前端是使用路径来传值的，要注意，不然你前端的请求都接收不到，就有点尴尬哈
    @GetMapping("/dish/{id}")
    public R<List<DishDto>> dish(@PathVariable("id") Long SetmealId){
        LambdaQueryWrapper<SetmealDish> queryWrapper =new LambdaQueryWrapper<>();
        queryWrapper.eq(SetmealDish::getSetmealId,SetmealId);
        List<SetmealDish> list = setmealDishService.list(queryWrapper);
        List<DishDto> dishDtos=list.stream().map((item) -> {
            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(item,dishDto);
            Long dishId = item.getDishId();
            Dish dish = dishService.getById(dishId);
            BeanUtils.copyProperties(dish,dishDto);
            return dishDto;

        }).collect(Collectors.toList());
        return R.success(dishDtos);
    }
@GetMapping("/{id}")
    public R<SetmealDto> getSetmeal(@PathVariable Long id){
    SetmealDto setmealDto = setmealService.getSetmeal(id);
    return R.success(setmealDto);
}
    @PutMapping
    public R<String> edit(@RequestBody SetmealDto setmealDto) {
if (setmealDto==null){
    return R.error("请求错误");
}
if (setmealDto.getSetmealDishes()==null){
    return R.error("套餐中没有菜品，请添加");
}
        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
        Long setmealId = setmealDto.getId();
        LambdaQueryWrapper<SetmealDish> queryWrapper =new LambdaQueryWrapper<>();
        queryWrapper.eq(SetmealDish::getSetmealId, setmealId);
        setmealDishService.remove(queryWrapper);//先全部删除后增加
        //为setmeal_dish表填充相关的属性
        for (SetmealDish setmealDish:setmealDishes){
            setmealDish.setSetmealId(setmealId);
        }
        //批量把setmealDish保存到setmeal_dish表
        setmealDishService.saveBatch(setmealDishes);
        setmealService.updateById(setmealDto);
        return R.success("套餐修改成功");
    }


}
