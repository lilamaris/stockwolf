package com.lilamaris.stockwolf.order.infrastructure.web.controller;

import com.lilamaris.stockwolf.order.application.port.in.CreateOrderCommand;
import com.lilamaris.stockwolf.order.application.port.in.OrderManager;
import com.lilamaris.stockwolf.order.infrastructure.web.request.OrderRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/order")
@RequiredArgsConstructor
public class OrderController {
    private final OrderManager orderManager;

    @PostMapping
    public ResponseEntity<?> post(
            @RequestBody OrderRequest.Create body
    ) {
        var command = new CreateOrderCommand(
                body.items()
        );
        var result = orderManager.create(command);

        return ResponseEntity.ok(result);
    }
}
