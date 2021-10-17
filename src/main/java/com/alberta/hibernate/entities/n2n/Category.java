package com.alberta.hibernate.entities.n2n;

import java.util.HashSet;
import java.util.Set;

public class Category {
    private Integer id;
    private String name;
    private Set<Item> items = new HashSet<>();


    public Category() {
    }

    public Category(Integer id, String name, Set<Item> items) {
        this.id = id;
        this.name = name;
        this.items = items;
    }

    @Override
    public String toString() {
        return "Category{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", items=" + items +
                '}';
    }

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

    public Set<Item> getItems() {
        return items;
    }

    public void setItems(Set<Item> items) {
        this.items = items;
    }
}
