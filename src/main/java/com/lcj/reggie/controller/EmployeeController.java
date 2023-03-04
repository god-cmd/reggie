package com.lcj.reggie.controller;/*
    @author lcj
    @create -
*/

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lcj.reggie.bean.Employee;
import com.lcj.reggie.common.R;
import com.lcj.reggie.service.CategoryService;
import com.lcj.reggie.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.apache.tomcat.util.security.MD5Encoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;

@RestController
@Slf4j
@RequestMapping("/employee")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @PostMapping("/login")
    public R<Employee> login(HttpSession session,@RequestBody Employee employee) throws NoSuchAlgorithmException {
        QueryWrapper<Employee> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username",employee.getUsername());
        Employee e1 = employeeService.getOne(queryWrapper);
        if(e1 == null){
            return R.error("用户不存在");
        }

        String md5Pwd = DigestUtils.md5DigestAsHex(employee.getPassword().getBytes());
        log.info(md5Pwd);
        if(!e1.getPassword().equals(md5Pwd)){
            return R.error("密码错误");
        }
        if(e1.getStatus() == 0){
            return R.error("该用户已经被禁用");
        }
        session.setAttribute("employee",e1.getId());
        return R.success(e1);
    }

    @PostMapping("/logout")
    public R<String> logout(HttpSession session){
        session.removeAttribute("employee");
        return R.success("退出成功");
    }

    @PostMapping
    public R<String> save(HttpSession session,@RequestBody Employee employee){
        employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));

        employeeService.save(employee);

        return R.success("插入成功");
    }

    @GetMapping("/page")
    public R<Page> page(@RequestParam("page") Integer page,
                        @RequestParam("pageSize") Integer pageSize,
                        @RequestParam(value = "name",defaultValue = "") String name){
        IPage<Employee> employeeIPage = new Page<>(page,pageSize);
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.like(Strings.isNotEmpty(name),"name",name)
        .orderByDesc("id");

        employeeService.page(employeeIPage,queryWrapper);
        return R.success(employeeIPage);
    }

    @PutMapping
    public R<String> updateEmployee(@RequestBody Employee employee,HttpSession session){
        employeeService.updateById(employee);
        return R.success("修改成功");
    }

    @GetMapping("/{id}")
    public R<Employee> getEmployeeById(@PathVariable("id") Long id){
        log.info("要查询的员工id:{}",id);
        Employee employee = employeeService.getById(id);
        if (employee!=null){
            return R.success(employee);
        }
        return R.error("用户不存在");
    }
}
