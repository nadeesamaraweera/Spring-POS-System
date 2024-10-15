package lk.ijse.springpossystem.dto;

import lk.ijse.springpossystem.customObj.CustomerResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CustomerDTO implements SuperDTO, CustomerResponse {
    private String id;
    private String name;
    private String address;
    private String contact;
    private String profilePic;
    private List<OrderDTO> orders = new ArrayList<>();
}
