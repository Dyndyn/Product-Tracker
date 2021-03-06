package com.softserve.if072.common.model.dto;

import javax.validation.constraints.Min;

/**
 * The StorageDTO class is used to simplify data transfer operations
 * between MVCapplication and RESTserver.
 *
 * @author Roman Dyndyn
 */
public class StorageDTO {
    private int userId;
    @Min(value = 1, message = "{error.storage.product}")
    private int productId;
    @Min(value = 0, message = "{error.storage.amount}")
    private int amount;
    private int previousAmount;
    private String productName;

    public StorageDTO() {
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(final int userId) {
        this.userId = userId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(final int productId) {
        this.productId = productId;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(final int amount) {
        this.amount = amount;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(final String productName) {
        this.productName = productName;
    }

    public int getPreviousAmount() {
        return previousAmount;
    }

    public void setPreviousAmount(final int previousAmount) {
        this.previousAmount = previousAmount;
    }

    @Override
    public String toString() {
        return "StorageDTO{" +
                "userId=" + userId +
                ", productId=" + productId +
                ", amount=" + amount +
                '}';
    }
}
