package com.study.reggie.dto;

import com.study.reggie.entity.Setmeal;
import com.study.reggie.entity.SetmealDish;
import com.study.reggie.entity.Setmeal;
import lombok.Data;
import java.util.List;

@Data
public class SetmealDto extends Setmeal {

    private List<SetmealDish> setmealDishes;

    private String categoryName;
}
