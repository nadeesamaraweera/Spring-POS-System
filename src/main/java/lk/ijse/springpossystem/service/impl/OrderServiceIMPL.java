package lk.ijse.springpossystem.service.impl;

import lk.ijse.springpossystem.dao.CustomerDAO;
import lk.ijse.springpossystem.dao.ItemDAO;
import lk.ijse.springpossystem.dao.OrderDAO;
import lk.ijse.springpossystem.dto.OrderDTO;
import lk.ijse.springpossystem.entity.CustomerEntity;
import lk.ijse.springpossystem.entity.ItemEntity;
import lk.ijse.springpossystem.entity.OrderDetailsEntity;
import lk.ijse.springpossystem.entity.OrderEntity;
import lk.ijse.springpossystem.exception.InsufficientCashException;
import lk.ijse.springpossystem.service.OrderService;
import lk.ijse.springpossystem.util.AppUtil;
import lk.ijse.springpossystem.util.Mapping;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderServiceIMPL implements OrderService {

    private static final Logger logger = LoggerFactory.getLogger(OrderService.class);
    @Autowired
    private final OrderDAO orderDAO;
    @Autowired
    private final CustomerDAO customerDAO;
    @Autowired
    private final ItemDAO itemDAO;
    @Autowired
    private final Mapping mapping;
    @Override
    public OrderDTO saveOrder(OrderDTO orderDTO) {
        logger.info("Saving order for customer ID: {}", orderDTO.getCustomerId());
        if (orderDTO.getOrderId() == null || orderDTO.getOrderId().isEmpty()) {
            orderDTO.setOrderId(AppUtil.createOrderId());
            logger.debug("Generated new order ID: {}", orderDTO.getOrderId());
        }

        CustomerEntity customer = customerDAO.findById(orderDTO.getCustomerId())
                .orElseThrow(() -> {
                    logger.error("Customer ID {} not found", orderDTO.getCustomerId());
                    return new RuntimeException("Customer not found with ID: " + orderDTO.getCustomerId());
                });
        logger.debug("Customer name: {}", customer.getName());

        OrderEntity orderEntity = mapping.convertToOrderEntity(orderDTO);
        orderEntity.setCustomer(customer);

        logger.debug("Processing order details");
        List<OrderDetailsEntity> orderDetails = orderDTO.getOrderDetails().stream().map(orderDetailDTO -> {
            OrderDetailsEntity orderDetail = new OrderDetailsEntity();
            orderDetail.setOrder(orderEntity);

            ItemEntity item = itemDAO.findById(orderDetailDTO.getItemCode())
                    .orElseThrow(() -> {
                        logger.error("Item ID {} not found", orderDetailDTO.getItemCode());
                        return new RuntimeException("Item not found with code: " + orderDetailDTO.getItemCode());
                    });
            logger.debug("Found item: {} with price {}", item.getDescription(), item.getPrice());

            if (item.getQty() < orderDetailDTO.getQuantity()) {
                logger.error("Insufficient quantity for item: {} (requested: {}, available: {})",
                        item.getCode(), orderDetailDTO.getQuantity(), item.getQty());
                throw new RuntimeException("Insufficient quantity for item: " + item.getCode());
            }

            item.setQty(item.getQty() - orderDetailDTO.getQuantity());
            logger.info("Updated item quantity for item: {}. New quantity: {}", item.getCode(), item.getQty());
            itemDAO.save(item);

            double unitPrice = item.getPrice();
            orderDetail.setItem(item);
            orderDetail.setQuantity(orderDetailDTO.getQuantity());
            orderDetail.setUnitPrice(unitPrice);

            double detailSubtotal = orderDetailDTO.getQuantity() * unitPrice;
            orderDetail.setTotalPrice(detailSubtotal);

            return orderDetail;
        }).collect(Collectors.toList());

        orderEntity.setOrderDetails(orderDetails);

        double subTotal = orderDetails.stream()
                .mapToDouble(OrderDetailsEntity::getTotalPrice)
                .sum();
        orderEntity.setTotal(subTotal);
        logger.debug("Calculated subtotal: {}", subTotal);

        double discountPercent = orderDTO.getDiscountPrice();
        double discountAmount = subTotal * discountPercent / 100;
        orderEntity.setDiscountPrice(discountAmount);
        logger.debug("Calculated discount amount ({}%): {}", discountPercent, discountAmount);

        double total = subTotal - discountAmount;
        orderEntity.setSubTotal(total);
        logger.debug("Total after discount: {}", total);

        double cash = orderDTO.getCash();
        double balance = cash - total;
        orderEntity.setBalance(balance);
        logger.debug("Calculated balance: {}", balance);

        if (balance < 0) {
            logger.warn("Insufficient cash for the order. Order cannot be placed.");
            throw new InsufficientCashException("Insufficient cash. Order cannot be placed.");
        }

        if (subTotal > 0 && discountAmount > 0) {
            for (OrderDetailsEntity detail : orderDetails) {
                double proportion = detail.getTotalPrice() / subTotal;
                double detailDiscount = discountAmount * proportion;
                double finalDetailTotal = detail.getTotalPrice() - detailDiscount;
                detail.setTotalPrice(finalDetailTotal);
                logger.debug("Applied discount to order detail ID {}: {} -> {}",
                        detail.getId(), detail.getTotalPrice() + detailDiscount, finalDetailTotal);
            }
        }

        OrderEntity savedOrder = orderDAO.save(orderEntity);
        logger.info("Saved order: {}", savedOrder.getOrderId());

        return mapping.convertToOrderDTO(savedOrder);
    }
}