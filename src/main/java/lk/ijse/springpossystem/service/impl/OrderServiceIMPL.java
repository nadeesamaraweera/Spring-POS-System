package lk.ijse.springpossystem.service.impl;

import lk.ijse.springpossystem.dao.CustomerDAO;
import lk.ijse.springpossystem.dao.ItemDAO;
import lk.ijse.springpossystem.dao.OrderDAO;
import lk.ijse.springpossystem.dto.OrderDTO;
import lk.ijse.springpossystem.entity.CustomerEntity;
import lk.ijse.springpossystem.entity.ItemEntity;
import lk.ijse.springpossystem.entity.OrderDetailsEntity;
import lk.ijse.springpossystem.entity.OrderEntity;
import lk.ijse.springpossystem.service.OrderService;
import lk.ijse.springpossystem.util.AppUtil;
import lk.ijse.springpossystem.util.Mapping;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderServiceIMPL implements OrderService {
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
        if (orderDTO.getOrderId() == null || orderDTO.getOrderId().isEmpty()) {
            orderDTO.setOrderId(AppUtil.createOrderId());
        }
        CustomerEntity customer = customerDAO.findById(orderDTO.getCustomerId())
                .orElseThrow(() -> {
                    return new RuntimeException("Customer not found with ID: " + orderDTO.getCustomerId());
                });
        OrderEntity orderEntity = mapping.convertToOrderEntity(orderDTO);
        orderEntity.setCustomer(customer);
        List<OrderDetailsEntity> orderDetails = orderDTO.getOrderDetails().stream().map(orderDetailDTO -> {
            OrderDetailsEntity orderDetail = new OrderDetailsEntity();
            orderDetail.setOrder(orderEntity);
            Optional<ItemEntity> itemOpt = itemDAO.findById(orderDetailDTO.getItemCode());
            if (!itemOpt.isPresent()) {
                throw new RuntimeException("Item not found with code: " + orderDetailDTO.getItemCode());
            }
            ItemEntity item = itemOpt.get();
            //check if  sufficient qty is available
            if (item.getQty() < orderDetailDTO.getQuantity()) {
                throw new RuntimeException("Insufficient quantity for item: " + item.getCode());
            }
            //update item qty
            item.setQty(item.getQty() - orderDetailDTO.getQuantity());
            itemDAO.save(item);
            orderDetail.setItem(item);
            //set order detail
            orderDetail.setItem(item);
            orderDetail.setQuantity(orderDetailDTO.getQuantity());
            orderDetail.setUnitPrice(orderDetailDTO.getUnitPrice());

            double detailSubtotal = orderDetailDTO.getQuantity() * orderDetailDTO.getUnitPrice();
            orderDetail.setTotalPrice(detailSubtotal);
            return orderDetail;
        }).collect(Collectors.toList());
        orderEntity.setOrderDetails(orderDetails);
        double subTotal = orderDetails.stream()
                .mapToDouble(OrderDetailsEntity::getTotalPrice)
                .sum();
        orderEntity.setTotal(subTotal);
        double discountPercent = orderDTO.getDiscountPrice();
        double discountAmount = subTotal * discountPercent / 100;
        orderEntity.setDiscountPrice(discountAmount);
        double total = subTotal - discountAmount;
        orderEntity.setSubTotal(total);
        double cash = orderDTO.getCash();
        double balance = cash - total;
        orderEntity.setBalance(balance);

        if (subTotal > 0 && discountAmount > 0) {
            for (OrderDetailsEntity detail : orderDetails) {
                double proportion = detail.getTotalPrice() / subTotal;
                double detailDiscount = discountAmount * proportion;
                double finalDetailTotal = detail.getTotalPrice() - detailDiscount;
                detail.setTotalPrice(finalDetailTotal);
                        detail.getId(), detail.getTotalPrice() + detailDiscount, finalDetailTotal);
            }
        }


        OrderEntity savedOrder = orderDAO.save(orderEntity);
        return mapping.convertToOrderDTO(savedOrder);

    }
}
