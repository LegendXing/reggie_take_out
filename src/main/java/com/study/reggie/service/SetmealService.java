package com.study.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.study.reggie.dto.SetmealDto;
import com.study.reggie.entity.Setmeal;

import java.util.List;

public interface SetmealService extends IService<Setmeal> {
    public void saveWithDish(SetmealDto setmealDto);
    public void removeWithDish(List<Long> ids);

    void updateSetmealStatusById(Integer status, List<Long> ids);


    SetmealDto getSetmeal(Long id);
}
