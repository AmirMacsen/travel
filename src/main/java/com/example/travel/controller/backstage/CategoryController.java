package com.example.travel.controller.backstage;

import com.example.travel.pojo.Category;
import com.example.travel.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/backstage/category")
public class CategoryController {
    @Autowired
    CategoryService categoryService;

    @RequestMapping("/all")
    @PreAuthorize("hasAuthority('/category/all')")
    public ModelAndView all(@RequestParam(value = "page", defaultValue = "1")int page,
                            @RequestParam(value = "size", defaultValue = "10") int size) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("backstage/category_all");
        modelAndView.addObject("categoryPage", categoryService.findPage(page, size));
        return modelAndView;
    }

    @RequestMapping("/add")
    @PreAuthorize("hasAuthority('/category/add')")
    public String add(Category category) {
        categoryService.add(category);
        return "redirect:/backstage/category/all";
    }

    @RequestMapping("/edit")
    public ModelAndView edit(Integer cid){
        Category category = categoryService.findById(cid);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("category",category);
        modelAndView.setViewName("/backstage/category_edit");
        return modelAndView;
    }

    @RequestMapping("/update")
    public String update(Category category){
        categoryService.update(category);
        return "redirect:/backstage/category/all";
    }

    @RequestMapping("/delete")
    public String delete(Integer cid){
        categoryService.delete(cid);
        return "redirect:/backstage/category/all";
    }
}
