package org.example.shop.catelog;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
public class AdminCatalogController {

    private final ProductService service;

    public AdminCatalogController(ProductService service) {
        this.service = service;
    }

    @PostMapping("/products")
    @ResponseStatus(HttpStatus.CREATED)
    public ProductResponse create(@Valid @RequestBody ProductRequest request) {
        return service.create(request);
    }

    @PutMapping("/products/{id}")
    public ProductResponse update(@PathVariable Long id, @Valid @RequestBody ProductRequest request) {
        return service.update(id, request);
    }

    @DeleteMapping("/products/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }

    @PostMapping("/categories")
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryResponse createCategory(@Valid @RequestBody CategoryRequest request) {
        return service.createCategory(request);
    }

    @PutMapping("/products/{productId}/categories/{categoryId}")
    public ProductResponse addToCategory(@PathVariable Long productId, @PathVariable Long categoryId) {
        return service.addToCategory(productId, categoryId);
    }

    @DeleteMapping("/products/{productId}/categories/{categoryId}")
    public ProductResponse removeFromCategory(@PathVariable Long productId, @PathVariable Long categoryId) {
        return service.removeFromCategory(productId, categoryId);
    }
}
