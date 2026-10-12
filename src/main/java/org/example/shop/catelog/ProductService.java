package org.example.shop.catelog;


import org.example.shop.common.ConflictException;
import org.example.shop.common.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class ProductService {

    private final ProductRepository products;
    private final CategoryRepository categories;

    public ProductService(ProductRepository products, CategoryRepository categories) {
        this.products = products;
        this.categories = categories;
    }

    //  reads
    public Page<ProductResponse> list(Long categoryId, Pageable pageable) {
        Page<Product> page = (categoryId == null)
                ? products.findAll(pageable)
                : products.findByCategoriesId(categoryId, pageable);
        return page.map(ProductResponse::from);
    }

    public ProductResponse get(Long id) {
        return ProductResponse.from(getProduct(id));
    }

    public List<CategoryResponse> listCategories() {
        return categories.findAll(Sort.by("name")).stream().map(CategoryResponse::from).toList();
    }

    // admin only
    @Transactional
    public ProductResponse create(ProductRequest r) {
        Product saved = products.save(new Product(r.name().trim(), r.description(), r.price(), r.stock(), r.featured()));
        return ProductResponse.from(saved);
    }

    @Transactional
    public ProductResponse update(Long id, ProductRequest r) {
        Product product = getProduct(id);
        product.update(r.name().trim(), r.description(), r.price(), r.stock(), r.featured());
        return ProductResponse.from(product);
    }

    @Transactional
    public void delete(Long id) {
        products.delete(getProduct(id));   // fails with 409 if any order references it (foreign key)
    }

    @Transactional
    public CategoryResponse createCategory(CategoryRequest r) {
        if (categories.existsByNameIgnoreCase(r.name().trim())) {
            throw new ConflictException("Category already exists: " + r.name());
        }
        return CategoryResponse.from(categories.save(new Category(r.name().trim())));
    }

    @Transactional
    public ProductResponse addToCategory(Long productId, Long categoryId) {
        Product product = getProduct(productId);
        product.addCategory(getCategory(categoryId));
        return ProductResponse.from(product);
    }

    @Transactional
    public ProductResponse removeFromCategory(Long productId, Long categoryId) {
        Product product = getProduct(productId);
        product.removeCategory(getCategory(categoryId));          // DELETE one join row
        return ProductResponse.from(product);
    }

    // ---------- used by the order feature ----------
    // MANDATORY: must run inside the caller's transaction, otherwise the stock change
    // would commit on its own even if the order later fails.
    @Transactional(propagation = Propagation.MANDATORY)
    public Product reserveStock(Long productId, int quantity) {
        Product product = getProduct(productId);
        product.reduceStock(quantity);
        return product;
    }

    private Product getProduct(Long id) {
        return products.findById(id).orElseThrow(() -> new NotFoundException("Product " + id + " not found"));
    }

    private Category getCategory(Long id) {
        return categories.findById(id).orElseThrow(() -> new NotFoundException("Category " + id + " not found"));
    }
}
