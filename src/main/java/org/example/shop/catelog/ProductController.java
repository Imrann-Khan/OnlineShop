package org.example.shop.catelog;

import org.example.shop.common.PageResponse;
import org.example.shop.config.ShopProperties;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ProductController {

    private final ProductService service;
    private final ShopProperties props;

    public ProductController(ProductService service, ShopProperties props) {
        this.service = service;
        this.props = props;
    }

    @GetMapping("/products")
    public PageResponse<ProductResponse> list(@RequestParam(required = false) Long categoryId,
                                              @RequestParam(defaultValue = "0") int page,
                                              @RequestParam(required = false) Integer size) {
        int requested = (size != null) ? size : props.pagination().defaultSize();
        int safeSize = Math.min(requested, props.pagination().maxSize());
        return PageResponse.from(service.list(categoryId, PageRequest.of(page, safeSize, Sort.by("id"))));
    }

    @GetMapping("/products/{id}")
    public ProductResponse get(@PathVariable Long id) {
        return service.get(id);
    }

    @GetMapping("/categories")
    public List<CategoryResponse> categories() {
        return service.listCategories();
    }
}