package com.study.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.study.reggie.common.R;
import com.study.reggie.entity.Category;
import com.study.reggie.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;
@PostMapping
    public R<String> save(@RequestBody Category category){
    categoryService.save(category);
    return R.success("新增分类成功");
}
@GetMapping("/page")
    public R<Page> page(int page,int pageSize){
    Page<Category> pageInfo=new Page<>(page,pageSize);
    LambdaQueryWrapper<Category> queryWrapper=new LambdaQueryWrapper<>();
    queryWrapper.orderByAsc(Category::getSort);
    categoryService.page(pageInfo,queryWrapper);
    return R.success(pageInfo);
}
@DeleteMapping
    public R<String> delete(Long id){
    log.info("删除分类，id为：{}",id);
    categoryService.remove(id);
    return R.success("分类信息删除成功");
}
@PutMapping
    public R<String> update(@RequestBody Category category){
    categoryService.updateById(category);
    return R.success("修改分类信息成功");
}
    @GetMapping("/list")
    public R<List<Category>> list(Category category){
        LambdaQueryWrapper<Category> queryWrapper =new LambdaQueryWrapper<>();
        queryWrapper.eq(category.getType() !=null,Category::getType,category.getType());
        queryWrapper.orderByAsc(Category::getSort).orderByDesc(Category::getCreateTime);
        List<Category> list = categoryService.list(queryWrapper);
        return R.success(list);

    }
}
