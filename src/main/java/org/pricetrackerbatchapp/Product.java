package org.pricetrackerbatchapp;

import org.apache.commons.lang3.StringUtils;

import java.util.Objects;

public class Product {
    private String id;
    private String name;
    private String price;
    private String url;

    public Product() {}

    public Product(String id, String url) {
        this.id = id;
        this.url = url;
    }

    public Product(String id, String name, String price, String url) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.url = url;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return StringUtils.equals(id, product.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
