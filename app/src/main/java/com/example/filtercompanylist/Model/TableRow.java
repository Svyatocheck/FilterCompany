package com.example.filtercompanylist.Model;
import java.io.Serializable;

public class TableRow implements Serializable {

    private String tableRow;
    private String priceData;
    private String categoryData;

    public TableRow(String tableRow, String priceData, String categoryData) {
        this.tableRow = tableRow;
        this.priceData = priceData;
        this.categoryData = categoryData;
    }

    public String getTableRow() {
        return tableRow;
    }

    public void setTableRow(String name) {
        this.tableRow = name;
    }

    public String getPriceData() {
        return priceData;
    }

    public void setPriceData(String name) {
        this.priceData = name;
    }

    public String getCategoryData() {
        return categoryData;
    }

    public void setCategoryData(String name) {
        this.categoryData = name;
    }

    public static class ChildClass implements Serializable {

        public ChildClass() {}
    }
}