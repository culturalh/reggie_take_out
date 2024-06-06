package com.itheima.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.reggie.common.R;
import com.itheima.reggie.entity.Category;
import com.itheima.reggie.service.CategoryService;
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

    /**
     * 增加菜品分类方法
     * @param category
     * @return
     */
    @PostMapping
    public R<String> save(@RequestBody Category category){

        categoryService.save(category);

        return R.success("菜品增加成功！");
    }

    /**
     * 分页查询
     * @param page
     * @param pageSize
     * @return
     */
    @GetMapping("/page")
    public R<Page> get(int page,int pageSize){


        //分页构造器
        Page<Category> pageInfo = new Page<>(page,pageSize);

        //条件构造器
        LambdaQueryWrapper<Category> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.orderByDesc(Category::getSort);

        categoryService.page(pageInfo,lambdaQueryWrapper);

        return R.success(pageInfo);
    }

    /**
     * 根据id删除
     * @param ids
     * @return
     */
    @DeleteMapping
    public R<String> delete(Long ids){

        categoryService.remove(ids);

        return R.success("菜品删除成功！");
    }

    /**
     * 根据id修改
     * @param category
     * @return
     */
    @PutMapping
    public R<String> update(@RequestBody Category category){

        categoryService.updateById(category);

        return R.success("菜品修改成功！");
    }

    /**
     * 根据type获取菜品分类
     * @param type
     * @return
     */
    @GetMapping("/list")
    public R<List<Category>> list(int type){
        LambdaQueryWrapper<Category> lambdaQueryWrapper = new LambdaQueryWrapper<>();

        lambdaQueryWrapper.eq(Category::getType,type);

        lambdaQueryWrapper.orderByAsc(Category::getSort).orderByDesc(Category::getUpdateTime);

        List<Category> list = categoryService.list(lambdaQueryWrapper);

        return R.success(list);
    }
}
