package com.softserve.if072.common.model;

import java.util.List;

/**
 * This Store class contains information about current store and
 * about products that user can buy in current store
 *
 * @author Nazar Vynnyk
 */
public class Store {

    private int id;
    private String name;
    private String address;
    private User user;
    private boolean isEnable;
    private List<Product> products;

    public Store() {}

    public Store(int id, String name, String address, User user, boolean isEnable, List<Product> products) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.user = user;
        this.isEnable = isEnable;
        this.products = products;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public User getUser() {return this.user; }

    public void setUser(User user) {this.user = user;}

    public boolean isEnable() {
        return isEnable;
    }

    public void setEnable(boolean enable) {
        isEnable = enable;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    @Override
    public String toString() {
        return "Store{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", user=" + user +
                ", isEnable=" + isEnable +
                ", products=" + products.toString() +
                '}';
    }
}