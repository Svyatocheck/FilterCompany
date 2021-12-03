package com.example.filtercompanylist.Model;
import java.io.Serializable;

public class Note implements Serializable {

    private String companyName;
    private String founder;
    private String product;
    private String category;
    private String price;

    public Note(String companyName, String founder, String product, String category, String price) {
        this.companyName = companyName;
        this.founder = founder;
        this.product = product;
        this.category = category;
        this.price = price;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String name) {
        this.companyName = name;
    }

    public String getFounder() {
        return founder;
    }

    public void setFounder(String founder) {
        this.founder = founder;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct (String product) {
        this.product = product;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String name) {
        this.category = category;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public static class ChildClass implements Serializable {

        public ChildClass() {}
    }
}