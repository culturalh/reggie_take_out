package com.itheima.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.reggie.common.CustomException;
import com.itheima.reggie.entity.Category;
import com.itheima.reggie.entity.Dish;
import com.itheima.reggie.entity.Setmeal;
import com.itheima.reggie.mapper.CategoryMapper;
import com.itheima.reggie.service.CategoryService;
import com.itheima.reggie.service.DishService;
import com.itheima.reggie.service.SetmealService;
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

        //根据id查询菜品分类是否有菜品
        LambdaQueryWrapper<Dish> lambdaQueryWrapper1 = new LambdaQueryWrapper<>();
        lambdaQueryWrapper1.eq(Dish::getCategoryId,id);
        int count1 = dishService.count(lambdaQueryWrapper1);

        if(count1>0){
            //菜品分类含有有菜品,不能删除
            throw new CustomException("已关联菜品,不能删除");
        }
        LambdaQueryWrapper<Setmeal> lambdaQueryWrapper2 = new LambdaQueryWrapper<>();
        lambdaQueryWrapper2.eq(Setmeal::getCategoryId,id);

        //根据id查询菜品分类是否有套餐
        int count2 = setmealService.count(lambdaQueryWrapper2);
        if(count1>0){
            //菜品分类含有有套餐,不能删除
            throw new CustomException("已关联套餐,不能删除");
        }


        //没有关联
        super.removeById(id);

    }
}
