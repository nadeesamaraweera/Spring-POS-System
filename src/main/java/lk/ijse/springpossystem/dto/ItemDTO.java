package lk.ijse.springpossystem.dto;

import lk.ijse.springpossystem.customObj.ItemResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ItemDTO implements SuperDTO, ItemResponse {
    private String code;
    private String description;
    private double price;
    private int qty;
}
