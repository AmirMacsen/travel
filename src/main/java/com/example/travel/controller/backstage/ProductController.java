package com.example.travel.controller.backstage;

import com.example.travel.bean.WangEditorResult;
import com.example.travel.pojo.Category;
import com.example.travel.pojo.Product;
import com.example.travel.service.CategoryService;
import com.example.travel.service.ProductService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/backstage/product")
public class ProductController {
    @Autowired
    ProductService productService;

    @Autowired
    CategoryService categoryService;

    @RequestMapping("/all")
    @PreAuthorize("hasAuthority('/product/all')")
    public ModelAndView all(@RequestParam(defaultValue = "1")int page,
                            @RequestParam(defaultValue = "10") int size) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("backstage/product_all");
        modelAndView.addObject("productPage", productService.findProductPage(page, size));
        return modelAndView;
    }

    @RequestMapping("/addPage")
    @PreAuthorize("hasAuthority('/product/addPage')")
    public ModelAndView addPage() {
        List<Category> categories = categoryService.findAll();
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("categoryList", categories);
        modelAndView.setViewName("backstage/product_add");
        return modelAndView;
    }

    @RequestMapping("/add")
    @PreAuthorize("hasAuthority('/product/add')")
    public String add(Product product) {
        productService.add(product);
        return "redirect:/backstage/product/all";
    }

    @RequestMapping("/upload")
    @PreAuthorize("hasAuthority('/product/upload')")
    @ResponseBody
    public WangEditorResult upload(HttpServletRequest request, MultipartFile file) throws IOException {
        // 创建文件夹，存放上传文件
        // 1.设置上传文件夹的真实路径
        String realPath = ResourceUtils.getURL("classpath:").getPath() + "/static/upload";
        // 2.判断该文件夹是否存在，如果不存在，新建文件夹
        File dir = new File(realPath);
        if (!dir.exists()){
            dir.mkdirs();
        }
        // 拿到上传文件名
        String filename = file.getOriginalFilename();
        filename = UUID.randomUUID()+filename;
        // 创建空文件
        File newFile = new File(dir, filename);
        // 将上传的文件写到空文件中
        file.transferTo(newFile);

        // 构造返回结果
        WangEditorResult wangEditorResult = new WangEditorResult();
        wangEditorResult.setErrno(0);
        String[] data = {"/upload/"+filename};
        wangEditorResult.setData(data);
        return wangEditorResult;
    }

    @RequestMapping("/edit")
    @PreAuthorize("hasAuthority('/product/edit')")
    public ModelAndView edit(int pid) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("backstage/product_edit");
        modelAndView.addObject("product", productService.findById(pid));
        return modelAndView;
    }

    @RequestMapping("/update")
    @PreAuthorize("hasAuthority('/product/update')")
    public String update(Product product) {
        productService.update(product);
        return "redirect:/backstage/product/all";
    }

    @RequestMapping("/updateStatus")
    @PreAuthorize("hasAuthority('/product/updateStatus')")
    public String updateStatus(Integer pid,@RequestHeader("Referer") String referer){
        productService.updateStatus(pid);
        return "redirect:"+referer;
    }
}
