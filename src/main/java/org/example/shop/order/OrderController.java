package org.example.shop.order;

import jakarta.validation.Valid;
import org.example.shop.order.receipt.ReceiptService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService service;
    private final ReceiptService receipts;

    public OrderController(OrderService service,  ReceiptService receipts) {
        this.service = service;
        this.receipts = receipts;
    }

    @PostMapping
    public ResponseEntity<OrderResponse> place(@Valid @RequestBody PlaceOrderRequest request) {
        OrderResponse created = service.placeOrder(request);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(created.id()).toUri();
        return ResponseEntity.created(location).body(created);
    }

    @GetMapping("/{id}/receipt")
    public ResponseEntity<String> receipt(@PathVariable Long id,
                                          @RequestParam(defaultValue = "text") String format) {
        ReceiptService.Receipt receipt = receipts.render(id, format);
        return ResponseEntity.ok().contentType(receipt.mediaType()).body(receipt.content());
    }

    @GetMapping("/{id}")
    public OrderResponse get(@PathVariable Long id) {
        return service.getOrder(id);
    }

    @GetMapping
    public List<OrderSummary> history(@RequestParam Long userId) {   // temporary until Phase 4
        return service.history(userId);
    }
}
