package lk.ijse.springpossystem.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString(exclude = {"customer", "orderDetails"})
@Table(name = "orders")
@Entity
public class OrderEntity implements SuperEntity {
    @Id
    private String orderId;
    @ManyToOne
    @JoinColumn(name = "customerId", nullable = false)
    private CustomerEntity customer;
    private String orderDate;
    private double total;
    private double discount;
    private double subTotal;
    private double cash;
    private double balance;
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderDetailsEntity> orderDetails = new ArrayList<>();
}
