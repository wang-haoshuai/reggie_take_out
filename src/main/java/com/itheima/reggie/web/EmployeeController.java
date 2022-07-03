package com.itheima.reggie.web;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.reggie.common.R;
import com.itheima.reggie.entity.Employee;
import com.itheima.reggie.common.PageBean;
import com.itheima.reggie.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

@Slf4j
@RestController
@RequestMapping("/employee")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @PostMapping("/login")
    public R<Employee> login(HttpServletRequest request, @RequestBody Employee employee) {
        //根据前台请求的数据获取密码并进行md5加密
        String password = employee.getPassword();
        password = DigestUtils.md5DigestAsHex(password.getBytes(StandardCharsets.UTF_8));

        //根据用户名进行用户查询
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Employee::getUsername, employee.getUsername());
        Employee emp = employeeService.getOne(queryWrapper);

        if (Objects.isNull(emp)) {
            log.info("该员工不存在");
            return R.error("员工不存在");
        }

        if (!emp.getPassword().equals(password)) {
            log.info("密码错误");
            return R.error("密码错误");
        }

        if (emp.getStatus() == 0) {
            log.info("员工状态已禁用");
            return R.error("该员工账号已被禁用");
        }

        //向session域中存入
        request.getSession().setAttribute("emp", emp.getId());

        return R.success(emp);
    }

    @PostMapping("/logout")
    public R<String> logout(HttpServletRequest request) {
        //销毁会话域中数据
        request.getSession().invalidate();
        return R.success("退出登录");
    }


    @PostMapping
    public R<String> save(@RequestBody Employee employee) {
        employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes(StandardCharsets.UTF_8)));

        employeeService.save(employee);

        return R.success("添加员工成功");
    }


    @GetMapping("/page")
    public R<PageBean<Employee>> page(int page, int pageSize, String name) {
        Page<Employee> pageInfo = new Page<>(page,pageSize);
        if (StringUtils.isNotEmpty(name)) {
            name = name.trim();
        }
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper
                .like(StringUtils.isNotEmpty(name), Employee::getUsername, name)
                .orderByDesc(Employee::getUpdateTime);
        employeeService.page(pageInfo, queryWrapper);
        PageBean<Employee> pageBean = new PageBean<>();
        pageBean.setRecords(pageInfo.getRecords());
        pageBean.setTotal(pageInfo.getTotal());
        return R.success(pageBean);
    }

    @PutMapping
    public R<String> update(@RequestBody Employee employee) {
        log.info("传过来的数据：{}", employee);

        employeeService.updateById(employee);

        return R.success("修改员工信息成功");
    }


    @GetMapping("/{id}")
    public R<Employee> getById(@PathVariable Long id) {
        Employee emp = employeeService.getById(id);

        if (emp != null) {
            return R.success(emp);
        }
        return R.error("没有查询到该员工的相关信息");
    }

}
