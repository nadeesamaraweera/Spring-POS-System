package lk.ijse.springpossystem.controller;

import lk.ijse.springpossystem.dto.OrderDTO;
import lk.ijse.springpossystem.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {
    private static final Logger logger = LoggerFactory.getLogger(OrderController.class);

    @Autowired
    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<OrderDTO> placeOrder(@RequestBody OrderDTO orderDTO) {
        logger.info("Received order placement request for customer ID: {}", orderDTO.getCustomerId());
        OrderDTO placedOrder = orderService.saveOrder(orderDTO);
        logger.info("Order placed for customer ID: {}", placedOrder.getCustomerId());
        return new ResponseEntity<>(placedOrder, HttpStatus.CREATED);
    }
}
