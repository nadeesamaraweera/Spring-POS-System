package lk.ijse.springpossystem.service;

import lk.ijse.springpossystem.customObj.CustomerResponse;
import lk.ijse.springpossystem.dto.CustomerDTO;

import java.util.List;

public interface CustomerService {
    void saveCustomer(CustomerDTO customerDTO);

    void updateCustomer(CustomerDTO customerDTO);

    void deleteCustomer(String customerId);

    CustomerResponse getSelectedCustomer(String customerId);

    List<CustomerDTO> getAllCustomers();
}
