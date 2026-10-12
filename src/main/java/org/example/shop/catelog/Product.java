package org.example.shop.catelog;


import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    private String name;

    @Column(length = 2000)
    private String description;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Column(nullable = false)
    private int stock;

    @Column(nullable = false)
    private boolean featured;

    @Version
    private Long version;

    @ManyToMany                                    // owning side: defines the join table
    @JoinTable(name = "product_categories",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id"))
    private Set<Category> categories = new HashSet<>();

    protected Product() {}

    public Product(String name, String description, BigDecimal price, int stock, boolean featured) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.stock = stock;
        this.featured = featured;
    }

    public void update(String name, String description, BigDecimal price, int stock, boolean featured) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.stock = stock;
        this.featured = featured;
    }

    public void reduceStock(int quantity) {
        if (stock < quantity) {
            throw new OutOfStockException(name, stock, quantity);
        }
        stock -= quantity;
    }

    public void addCategory(Category category) {
        categories.add(category);
        category.getProducts().add(this);
    }

    public void removeCategory(Category category) {
        categories.remove(category);
        category.getProducts().remove(this);
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public BigDecimal getPrice() { return price; }
    public int getStock() { return stock; }
    public boolean isFeatured() { return featured; }
    public Set<Category> getCategories() {
        return Collections.unmodifiableSet(categories);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Product other)) return false;
        return id != null && id.equals(other.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
