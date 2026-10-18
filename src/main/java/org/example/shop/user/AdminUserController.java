package org.example.shop.user;

import org.example.shop.common.PageResponse;
import org.example.shop.config.ShopProperties;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/users")
public class AdminUserController {

    private final UserService service;
    private final ShopProperties props;

    public AdminUserController(UserService service, ShopProperties props) {
        this.service = service;
        this.props = props;
    }

    @GetMapping
    public PageResponse<UserResponse> list(@RequestParam(defaultValue = "0") int page,
                                           @RequestParam(required = false) Integer size) {
        int requested = (size != null) ? size : props.pagination().defaultSize();
        int safeSize = Math.min(requested, props.pagination().maxSize());
        return PageResponse.from(service.findAll(PageRequest.of(page, safeSize, Sort.by("id"))));
    }

    @GetMapping("/{id}")
    public UserResponse get(@PathVariable Long id) {
        return service.findById(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
