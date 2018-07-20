package com.example.eshop.controller;

import com.example.eshop.model.Brand;
import com.example.eshop.model.Category;
import com.example.eshop.model.Product;
import com.example.eshop.repository.BrandRepository;
import com.example.eshop.repository.CategoryRepository;
import com.example.eshop.repository.ProductRepository;
import com.example.eshop.repository.UserRepository;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

@Controller
public class AdminController {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private BrandRepository brandRepository;
    @Autowired
    private CategoryRepository categoryRepository;

    @RequestMapping(value = "/admin", method = RequestMethod.GET)
    public String adminPage(ModelMap map) {
        map.addAttribute("products", productRepository.findAll());
        map.addAttribute("brands", brandRepository.findAll());
        map.addAttribute("brand", new Brand());
        map.addAttribute("categories", categoryRepository.findAll());
        map.addAttribute("category", new Category());
        map.addAttribute("product", new Product());
        return "admin";
    }

    @PostMapping("/addProduct")
    public String saveAuthor(@Valid @ModelAttribute("product") Product product, BindingResult result, @RequestParam("picture") MultipartFile multipartFile) throws IOException {
        StringBuilder sb = new StringBuilder();
        if (result.hasErrors()) {
            for (ObjectError objectError : result.getAllErrors()) {
                sb.append(objectError.getDefaultMessage() + "<br>");
            }
            return "redirect:/admin?message=" + sb.toString();
        }
        String picName = System.currentTimeMillis() + "_" + multipartFile.getOriginalFilename();
        File file = new File("C:\\mvc\\" + picName);
        multipartFile.transferTo(file);
        product.setPicUrl(picName);
        productRepository.save(product);
        return "redirect:/admin";
    }

    @PostMapping("/addBrand")
    public String saveBrand(@Valid @ModelAttribute("brand") Brand brand, BindingResult result) {
        StringBuilder sb = new StringBuilder();
        if (result.hasErrors()) {
            for (ObjectError objectError : result.getAllErrors()) {
                sb.append(objectError.getDefaultMessage() + "<br>");
            }
            return "redirect:/admin?message=" + sb.toString();
        }
        brandRepository.save(brand);
        return "redirect:/admin";
    }

    @PostMapping("/addCategory")
    public String saveCategory(@Valid @ModelAttribute("category") Category category, BindingResult result) {
        StringBuilder sb = new StringBuilder();
        if (result.hasErrors()) {
            for (ObjectError objectError : result.getAllErrors()) {
                sb.append(objectError.getDefaultMessage() + "<br>");
            }
            return "redirect:/admin?message=" + sb.toString();
        }
        categoryRepository.save(category);
        return "redirect:/admin";
    }

    @RequestMapping(value = "/image", method = RequestMethod.GET)
    public void getImageAsByteArray(HttpServletResponse response, @RequestParam("fileName") String fileName) throws IOException {
        InputStream in = new FileInputStream("C:\\mvc\\" + fileName);
        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        IOUtils.copy(in, response.getOutputStream());
    }
}
