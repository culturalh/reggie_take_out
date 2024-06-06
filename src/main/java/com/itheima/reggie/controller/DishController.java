package com.itheima.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.reggie.common.R;
import com.itheima.reggie.dto.DishDto;
import com.itheima.reggie.dto.SetmealDto;
import com.itheima.reggie.entity.Category;
import com.itheima.reggie.entity.Dish;
import com.itheima.reggie.service.CategoryService;
import com.itheima.reggie.service.DishFlavorService;
import com.itheima.reggie.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/dish")
@Slf4j
public class DishController {

    @Autowired
    private DishService dishService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private DishFlavorService dishFlavorService;

    /**
     * 新增菜品功能
     * @param dishDto
     * @return
     */
    @PostMapping
    public R<String> save(@RequestBody DishDto dishDto){
        log.info(dishDto.toString());

        dishService.saveWithFlavor(dishDto);
        return R.success("新增菜品成功！");
    }


    /**
     * 分页查询功能
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    public R<Page> page(int page,int pageSize ,String name){

        //分页构造器
        Page<Dish> pageInfo = new Page<>(page,pageSize);

        //dish中只有category_id需要转换为category_name,dish实体类不够用，需要转换为dishDto实体类型
        Page<DishDto> dishDtoPageInfo = new Page<>();

        //条件构造器
        LambdaQueryWrapper<Dish> lambdaQueryWrapper = new LambdaQueryWrapper<>();

        lambdaQueryWrapper.like(name != null ,Dish::getName,name);

        lambdaQueryWrapper.orderByDesc(Dish::getUpdateTime);

        dishService.page(pageInfo,lambdaQueryWrapper);

        //处理records，其他的值拷贝
        BeanUtils.copyProperties(pageInfo,dishDtoPageInfo,"records");

        //对records进行处理
        List<Dish> records = pageInfo.getRecords();

        List<DishDto> list = records.stream().map((item)->{

            DishDto dishDto = new DishDto();

            BeanUtils.copyProperties(item,dishDto);
            //获取categoryId
            Long categoryId = item.getCategoryId();
            //通过categoryId获取categoryName
            Category category = categoryService.getById(categoryId);
            if(category != null) {
                dishDto.setCategoryName(category.getName());
            }
            return dishDto;

        }).collect(Collectors.toList());

        dishDtoPageInfo.setRecords(list);

        return R.success(dishDtoPageInfo);
    }

    /**
     * 修改页面回显功能
     * @return
     */
    @GetMapping("/{id}")
    public R<DishDto> get(@PathVariable Long id){

       DishDto dishDto = dishService.getByIdWithFlavors(id);

        return R.success(dishDto);
    }

    @PutMapping
    public R<String> update(@RequestBody DishDto dishDto){
        log.info(dishDto.toString());

        dishService.updateWithFlavor(dishDto);
        return R.success("新增菜品成功！");
    }

    /**
     * 根据categoryId查询菜品
     * @param setmealDto
     * @return
     */
    @GetMapping("/list")
    public R<List<Dish>> list(SetmealDto setmealDto){
        LambdaQueryWrapper<Dish> lambdaQueryWrapper = new LambdaQueryWrapper<>();

        lambdaQueryWrapper.eq(Dish::getCategoryId,setmealDto.getCategoryId());

        lambdaQueryWrapper.orderByAsc(Dish::getSort).orderByDesc(Dish::getUpdateTime);

        lambdaQueryWrapper.eq(Dish::getStatus , 1);

        List<Dish> list = dishService.list(lambdaQueryWrapper);

        return R.success(list);
    }

}
