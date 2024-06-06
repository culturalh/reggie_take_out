package com.itheima.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.reggie.entity.Category;
import org.springframework.stereotype.Service;

@Service
public interface CategoryService extends IService<Category> {

    /**
     * 根据id删除
     * @param id
     */
    public void remove(Long id);
}
