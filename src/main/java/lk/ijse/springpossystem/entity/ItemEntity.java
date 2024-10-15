package lk.ijse.springpossystem.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString(exclude = "orderDetails")
@Entity
@Table(name = "items")
public class ItemEntity implements SuperEntity {
    @Id
    private String code;
    private String description;
    private double price;
    private int qty;
    @OneToMany(mappedBy = "item", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderDetailsEntity> orderDetails = new ArrayList<>();
}
