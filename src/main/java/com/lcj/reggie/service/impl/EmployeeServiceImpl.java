package com.lcj.reggie.service.impl;/*
    @author lcj
    @create -
*/

import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lcj.reggie.bean.Employee;
import com.lcj.reggie.mapper.EmployeeMapper;
import com.lcj.reggie.service.EmployeeService;
import org.springframework.stereotype.Service;

@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements EmployeeService {
}
