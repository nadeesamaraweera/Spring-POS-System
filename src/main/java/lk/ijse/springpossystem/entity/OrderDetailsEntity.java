package lk.ijse.springpossystem.entity;

import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString(exclude = "order")
@Entity
@Table(name = "order_details")
public class OrderDetailsEntity implements SuperEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private OrderEntity order;
    @ManyToOne
    @JoinColumn(name = "item_code", nullable = false)
    private ItemEntity item;
    private int quantity;
    private double unitPrice;
    private double totalPrice;
}
