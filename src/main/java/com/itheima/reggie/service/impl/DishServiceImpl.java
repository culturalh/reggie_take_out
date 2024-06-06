package com.itheima.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.reggie.dto.DishDto;
import com.itheima.reggie.entity.Dish;
import com.itheima.reggie.entity.DishFlavor;
import com.itheima.reggie.mapper.DishMapper;
import com.itheima.reggie.service.DishFlavorService;
import com.itheima.reggie.service.DishService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {

    @Autowired
    private DishFlavorService dishFlavorService;

    @Transactional
    @Override
    public void saveWithFlavor(DishDto dishDto) {

        //增加菜品信息
        this.save(dishDto);

        //获取生成菜品的dish_id
        Long dishId = dishDto.getId();

        List<DishFlavor> flavors = dishDto.getFlavors();

         flavors = flavors.stream().map((item) ->   {
            item.setDishId(dishId);
            return item;
        }).collect(Collectors.toList());


        //增加对应菜品的菜品口味信息
        dishFlavorService.saveBatch(flavors);

    }

    /**
     * 获取回显数据
     * @param id
     * @return
     */
    @Override
    public DishDto getByIdWithFlavors(Long id) {

        DishDto dishDto = new DishDto();
        Dish dish = this.getById(id);

        BeanUtils.copyProperties(dish,dishDto);

        LambdaQueryWrapper<DishFlavor> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(DishFlavor::getDishId,dish.getId());

        List<DishFlavor> list = dishFlavorService.list(lambdaQueryWrapper);

        dishDto.setFlavors(list);

        return dishDto;
    }

    /**
     * 修改菜品信息
     * @param dishDto
     */
    @Transactional
    @Override
    public void updateWithFlavor(DishDto dishDto) {

        //修改dish中的数据
        this.updateById(dishDto);
        //修改flavor中的数据
        LambdaQueryWrapper<DishFlavor> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(DishFlavor::getDishId,dishDto.getId());
        //先删除
        dishFlavorService.remove(lambdaQueryWrapper);
        //再添加
        List<DishFlavor> flavors = dishDto.getFlavors();

        flavors = flavors.stream().map((item) ->   {
            item.setDishId(dishDto.getId());
            return item;
        }).collect(Collectors.toList());
        dishFlavorService.saveBatch(flavors);
    }
}
