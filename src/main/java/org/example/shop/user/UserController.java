package org.example.shop.user;

import jakarta.validation.Valid;
import org.example.shop.common.PageResponse;
import org.example.shop.config.ShopProperties;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService service;
    private final ShopProperties props;

    public UserController(UserService service, ShopProperties props) {
        this.service = service;
        this.props = props;
    }

    @PostMapping
    public ResponseEntity<UserResponse> create(@Valid @RequestBody CreateUserRequest request) {
        UserResponse created = service.create(request);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(created.id()).toUri();
        return ResponseEntity.created(location).body(created);   // 201 + Location header
    }

    @GetMapping("/{id}")
    public UserResponse get(@PathVariable Long id) {
        return service.findById(id);
    }

    @GetMapping
    public PageResponse<UserResponse> list(@RequestParam(defaultValue = "0") int page,
                                           @RequestParam(required = false) Integer size) {
        int requested = (size != null) ? size : props.pagination().defaultSize();
        int safeSize = Math.min(requested, props.pagination().maxSize());
        return PageResponse.from(service.findAll(PageRequest.of(page, safeSize, Sort.by("id"))));
    }

    @PutMapping("/{id}")
    public UserResponse update(@PathVariable Long id, @Valid @RequestBody UpdateUserRequest request) {
        return service.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)                        // 204
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}