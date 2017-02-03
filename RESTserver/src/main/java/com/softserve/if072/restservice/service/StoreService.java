package com.softserve.if072.restservice.service;

import com.softserve.if072.common.model.Product;
import com.softserve.if072.common.model.Store;
import com.softserve.if072.restservice.exception.DataNotFoundException;
import com.softserve.if072.restservice.dao.mybatisdao.StoreDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@PropertySource(value = {"classpath:message.properties"})
public class StoreService {

    private final StoreDAO storeDAO;

    @Value("${store.notFound}")
    private String storeNotFound;

    @Autowired
    public StoreService(StoreDAO storeDAO) {
        this.storeDAO = storeDAO;
    }

    @Transactional
    public List<Store> getAllStores() throws DataNotFoundException {
        List<Store> stores = storeDAO.getAll();
        if (!stores.isEmpty()){
            return stores;
        } else {
            throw new DataNotFoundException("Stores not found");
        }
    }

    @Transactional
    public Store getStoreByID(int id) throws DataNotFoundException {
        Store store = storeDAO.getByID(id);
        if (store != null){
            return store;
        } else {
            throw new DataNotFoundException(String.format(storeNotFound, id));
        }
    }

    @Transactional
    public void addStore(Store store) {storeDAO.insert(store);}

    @Transactional
    public void updateStore(Store store) {
        storeDAO.update(store);
    }

    @Transactional
    public void deleteStore(int id) throws DataNotFoundException {
        Store store = storeDAO.getByID(id);
        if (store != null){
            storeDAO.delete(id);
        } else {
            throw new DataNotFoundException(String.format(storeNotFound, id));
        }
    }

    @Transactional
    public List<Product> getProductsByStoreId(int storeId) throws DataNotFoundException {
        List<Product> products = storeDAO.getProductsByStoreId(storeId);
        if (!products.isEmpty()){
            return products;
        } else {
            throw new DataNotFoundException("Products not found");
        }
    }
}

