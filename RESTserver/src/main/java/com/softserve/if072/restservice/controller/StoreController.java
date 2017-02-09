package com.softserve.if072.restservice.controller;

import com.softserve.if072.common.model.Product;
import com.softserve.if072.common.model.Store;

import com.softserve.if072.restservice.exception.DataNotFoundException;
import com.softserve.if072.restservice.service.StoreService;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

import java.util.List;

/**
 * Serve requests used for working with Store model
 *
 * @author Nazar Vynnyk
 */

@RestController
@RequestMapping("/stores")
@PropertySource(value = {"classpath:message.properties"})
public class StoreController {
    public static final Logger LOGGER =  LogManager.getLogger(StoreController.class);
    private StoreService storeService;

    @Autowired
    public StoreController(StoreService storeService){
        this.storeService = storeService;
    }

    @Value("${store.notFound}")
    private String storeNotFound;

    @GetMapping ("/user/{userId}")
    @ResponseBody
    @ResponseStatus(value = HttpStatus.OK)
    public List<Store> getAllStoresByUserId(@PathVariable int userId, HttpServletResponse response) {
        try {
            List<Store> stores = storeService.getAllStores(userId);
            LOGGER.info("All Stores were found");
            return stores;
        } catch (DataNotFoundException e) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            LOGGER.error("Stores were not found", e);
            return null;
        }
    }

    @GetMapping("/{id}")
    @ResponseBody
    @ResponseStatus(value = HttpStatus.OK)
        public Store getStoreByID(@PathVariable int id, HttpServletResponse response) {
        try {
            Store store = storeService.getStoreByID(id);
            LOGGER.info(String.format("Store with id %d was retrieved", id));
            return store;
        } catch (DataNotFoundException e) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            LOGGER.error(String.format(storeNotFound, id), e);
            return null;
        }
    }

   @PostMapping("/")
   @ResponseStatus(value = HttpStatus.CREATED)
   public void addStore(@RequestBody Store store) {
       storeService.addStore(store);
       LOGGER.info("New Store was created");
  }

   @PutMapping("/")
   @ResponseBody
   @ResponseStatus(value = HttpStatus.OK)
   public Store updateStore(@RequestBody Store store, HttpServletResponse response) {
       int id = store.getId();
       try {
            storeService.updateStore(store);
            LOGGER.info(String.format("Store with id %d was updated", id));
            store = storeService.getStoreByID(store.getId());
            return store;
        } catch (DataNotFoundException e) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            LOGGER.error(String.format(storeNotFound, id), e);
            return null;
        }
    }

   @DeleteMapping("/{id}")
   @ResponseStatus(value = HttpStatus.NO_CONTENT)
   public void deleteStore(@PathVariable int id, HttpServletResponse response) {
        try {
            storeService.deleteStore(id);
            LOGGER.info(String.format("Store with id %d was deleted", id));
        } catch (DataNotFoundException e) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            LOGGER.error(String.format(storeNotFound, id), e);
        }
    }

    /**
     * This method shows all products that sell at the current store
     *
     * @param storeId current store_id
     * @param response list of products
     * @return list of products that sell at the current store
     */

   @GetMapping("/{storeId}/products")
   @ResponseBody
   @ResponseStatus(value = HttpStatus.OK)
   public List<Product> getAllProductsFromStore(@PathVariable int storeId, HttpServletResponse response) {
        try {
            List<Product> products = storeService.getProductsByStoreId(storeId);
            LOGGER.info("All Products were found");
            return products;
        } catch (DataNotFoundException e) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            LOGGER.error("Products were not found", e);
            return null;
        }
   }

    @GetMapping("/{storeId}/products/{productId}")
    @ResponseBody
    @ResponseStatus(value = HttpStatus.OK)
    public Product getProductFromStore(@PathVariable Integer storeId, @PathVariable  Integer productId,
                                       HttpServletResponse response) {
        try {
            Product product = storeService.getProductFromStoreById(storeId, productId);
            LOGGER.info("All Products were found");
            return product;
        } catch (DataNotFoundException e) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            LOGGER.error((String.format("Product %d from Store %d not found", productId, storeId)), e);
            return null;
        }
    }

    @DeleteMapping("/{storeId}/products/{productId}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteProductFromStore(@PathVariable Integer storeId, @PathVariable Integer
            productId, HttpServletResponse response) {
        try {
            storeService.deleteProductFromStoreById (storeId, productId);
            LOGGER.info(String.format("Product %d from Store %d was deleted", productId, storeId));
        } catch (DataNotFoundException e) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            LOGGER.error((String.format("Product %d from Store %d not found", productId, storeId)), e);
        }
    }

    @PostMapping("/products/")
    @ResponseStatus(value = HttpStatus.CREATED)
    public void addProductToStore(@RequestBody Store store, Product product) {
        storeService.addProductToStore(store, product);
        LOGGER.info(String.format("Product %d was added to Store %d", product.getId(), store.getId()));
    }

}
