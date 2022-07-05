package com.itheima.reggie.web;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.reggie.common.PageBean;
import com.itheima.reggie.common.R;
import com.itheima.reggie.dto.DishDto;
import com.itheima.reggie.entity.Category;
import com.itheima.reggie.entity.Dish;
import com.itheima.reggie.service.CategoryService;
import com.itheima.reggie.service.DishService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/dish")
public class DishController {
    @Autowired
    private DishService dishService;

    @Autowired
    private CategoryService categoryService;

    @PostMapping
    public R<String> save(@RequestBody DishDto dishDto) {
        dishService.saveWithFlavor(dishDto);
        return R.success("新增菜品成功");
    }

    @GetMapping("/page")
    public R<PageBean<DishDto>> page(int page, int pageSize, String name) {
        Page<Dish> dishPage = new Page<>(page, pageSize);
        Page<DishDto> dtoPage = new Page<>();

        if (name != null) {
            name = name.trim();
        }

        LambdaQueryWrapper<Dish> dishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        dishLambdaQueryWrapper.like(name != null, Dish::getName, name)
                .orderByAsc(Dish::getSort)
                .orderByDesc(Dish::getUpdateTime);
        dishService.page(dishPage, dishLambdaQueryWrapper);

        BeanUtils.copyProperties(dishPage, dtoPage, "records");

        List<Dish> records = dishPage.getRecords();
        List<DishDto> dtoList = records.stream().map(item -> {
            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(item, dishDto);
            Long categoryId = item.getCategoryId();
            if (categoryId != null) {
                QueryWrapper<Category> categoryQueryWrapper = new QueryWrapper<>();
                categoryQueryWrapper.eq(categoryId != null, "id", categoryId)
                        .select("name");
                Category category = categoryService.getOne(categoryQueryWrapper);
                dishDto.setCategoryName(category.getName());
            }
            return dishDto;
        }).collect(Collectors.toList());

        dtoPage.setRecords(dtoList);
        PageBean<DishDto> pageBean = new PageBean<>();
        BeanUtils.copyProperties(dtoPage,pageBean);
        return R.success(pageBean);
    }

    @GetMapping("/{id}")
    public R<DishDto> getById(@PathVariable Long id) {
        DishDto dishDto = dishService.getByIdWithFlavor(id);
        return R.success(dishDto);
    }

    @PutMapping
    public R<String> update(@RequestBody DishDto dishDto) {
        dishService.updateWithFlavor(dishDto);
        return R.success("修改菜品成功");
    }
}
