package com.lcj.reggie.service;/*
    @author lcj
    @create -
*/

import com.baomidou.mybatisplus.extension.service.IService;
import com.lcj.reggie.bean.Category;
import com.lcj.reggie.common.R;

public interface CategoryService extends IService<Category> {
    public R<String> remove(Long id);
}
