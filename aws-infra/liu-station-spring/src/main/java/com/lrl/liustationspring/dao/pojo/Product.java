package com.lrl.liustationspring.dao.pojo;

import java.util.Date;

public class Product {

    private Integer id;
    private String name;
    private String description;
    private String sku;
    private String manufacturer;
    private Date dateAdded;
    private Date dateLastModified;
    private int ownerUserId;

    private int quantity;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public Date getDateAdded() {
        return dateAdded;
    }

    public void setDateAdded(Date dateAdded) {
        this.dateAdded = dateAdded;
    }

    public Date getDateLastModified() {
        return dateLastModified;
    }

    public void setDateLastModified(Date dateLastModified) {
        this.dateLastModified = dateLastModified;
    }

    public int getOwnerUserId() {
        return ownerUserId;
    }

    public void setOwnerUserId(int ownerUserId) {
        this.ownerUserId = ownerUserId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Product(Integer id, String name, String description, String sku, String manufacturer, Date dateAdded, Date dateLastModified, int ownerUserId, int quantity) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.sku = sku;
        this.manufacturer = manufacturer;
        this.dateAdded = dateAdded;
        this.dateLastModified = dateLastModified;
        this.ownerUserId = ownerUserId;
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", sku='" + sku + '\'' +
                ", manufacturer='" + manufacturer + '\'' +
                ", dateAdded=" + dateAdded +
                ", dateLastModified=" + dateLastModified +
                ", ownerUserId=" + ownerUserId +
                ", quantity=" + quantity +
                '}';
    }
}
