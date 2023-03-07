package com.lcj.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.Query;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.lcj.reggie.bean.AddressBook;
import com.lcj.reggie.common.BaseContext;
import com.lcj.reggie.common.R;
import com.lcj.reggie.service.AddressBookService;
import lombok.extern.slf4j.Slf4j;
import lombok.extern.slf4j.XSlf4j;
import org.apache.tomcat.jni.Address;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/addressBook")
public class AddressBookController {
    @Autowired
    private AddressBookService addressBookService;

    @PostMapping
    public R<String> save(@RequestBody AddressBook addressBook){
        Long userId = BaseContext.getCurrentId();
        QueryWrapper<AddressBook> addressBookQueryWrapper = new QueryWrapper<>();
        addressBookQueryWrapper.eq("user_id",userId);
        List<AddressBook> list = addressBookService.list(addressBookQueryWrapper);

        if(list.size()==0){
            addressBook.setIsDefault(1);
        }

        addressBook.setUserId(userId);
        addressBookService.save(addressBook);
        return R.success("添加成功");
    }

    @GetMapping("/list")
    public R<List<AddressBook>> list(){
        Long userId = BaseContext.getCurrentId();
        QueryWrapper<AddressBook> addressBookQueryWrapper = new QueryWrapper<>();
        addressBookQueryWrapper.eq("user_id",userId);
        List<AddressBook> list = addressBookService.list(addressBookQueryWrapper);

        return R.success(list);
    }

    @GetMapping("/{id}")
    public R<AddressBook> getAddressBookById(@PathVariable("id") Long id){
        AddressBook addressBook = addressBookService.getById(id);
        return R.success(addressBook);
    }

    @PutMapping
    public R<String> updateAddressBook(@RequestBody AddressBook addressBook){
        addressBookService.updateById(addressBook);
        return R.success("修改成功");
    }

    @PutMapping("/default")
    public R<String> updateIsDefault(@RequestBody AddressBook addressBook){
        Long userId = BaseContext.getCurrentId();
        UpdateWrapper<AddressBook> updateWrapper = new UpdateWrapper<>();
        updateWrapper.set("is_default",0);
        updateWrapper.eq("user_id",userId);
        addressBookService.update(updateWrapper);

        addressBook.setIsDefault(1);
        addressBookService.updateById(addressBook);
        return R.success("设置成功");
    }

    @GetMapping("/default")
    public R<AddressBook> getDefaultAddress(){
        Long userId = BaseContext.getCurrentId();

        QueryWrapper<AddressBook> addressBookQueryWrapper = new QueryWrapper<>();
        addressBookQueryWrapper.eq("user_id",userId);
        addressBookQueryWrapper.eq("is_default",1);
        AddressBook defaultAddress = addressBookService.getOne(addressBookQueryWrapper);
        if(defaultAddress==null){
            return R.error("没有设置默认地址");
        }
        return R.success(defaultAddress);
    }

    @DeleteMapping
    public R<String> deleteAddressBook(@RequestParam("ids") List<Long> ids){
        Long userId = BaseContext.getCurrentId();
        QueryWrapper<AddressBook> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("id",ids);
        queryWrapper.eq("user_id",userId);
        // delete from address_book where id in (x,x,x) and user_id = x;
        addressBookService.remove(queryWrapper);
        return R.success("删除成功");
    }
}
