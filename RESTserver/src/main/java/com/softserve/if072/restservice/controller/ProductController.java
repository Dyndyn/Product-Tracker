package com.softserve.if072.restservice.controller;

import com.softserve.if072.common.model.Product;
import com.softserve.if072.restservice.service.impl.ProductServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping(value = "/product")
public class ProductController {

    @Autowired
    private ProductServiceImpl productService;

    @RequestMapping(value = "/all", method = RequestMethod.GET)
    @ResponseBody
    @ResponseStatus(value = HttpStatus.OK)
    public List<Product> getAll(){return productService.getAll();}

    @RequestMapping(value = "/get/{id}", method = RequestMethod.GET)
    @ResponseBody
    @ResponseStatus(value = HttpStatus.OK)
    public Product getById(@PathVariable int id){return productService.getById(id);}

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ResponseBody
    @ResponseStatus(value = HttpStatus.CREATED)
    public void insert(@RequestBody Product product){productService.insert(product);}
}