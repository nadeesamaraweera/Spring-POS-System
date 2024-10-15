package lk.ijse.springpossystem.service.impl;

import lk.ijse.springpossystem.dao.CustomerDAO;
import lk.ijse.springpossystem.dto.CustomerDTO;
import lk.ijse.springpossystem.entity.CustomerEntity;
import lk.ijse.springpossystem.exception.CustomerNotFoundException;
import lk.ijse.springpossystem.exception.DataPersistFailedException;
import lk.ijse.springpossystem.service.CustomerService;
import lk.ijse.springpossystem.util.AppUtil;
import lk.ijse.springpossystem.util.Mapping;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class CustomerServiceIMPL implements CustomerService {
    @Autowired
    private final CustomerDAO customerDAO;
    @Autowired
    private final Mapping mapping;

    @Override
    public void saveCustomer(CustomerDTO customerDTO) {
        customerDTO.setId(AppUtil.createCustomerId());
        CustomerEntity savedCustomer = customerDAO.save(mapping.convertToCustomerEntity(customerDTO));
        if (savedCustomer == null) {
            throw new DataPersistFailedException("Cannot save data");
        }
    }
    @Override
    public void updateCustomer(CustomerDTO customerDTO) {
        Optional<CustomerEntity> tmpCustomer = customerDAO.findById(customerDTO.getId());
        if (!tmpCustomer.isPresent()) {
            throw new CustomerNotFoundException("Customer Not Found");
        } else {
            tmpCustomer.get().setName(customerDTO.getName());
            tmpCustomer.get().setAddress(customerDTO.getAddress());
            tmpCustomer.get().setContact(customerDTO.getContact());
            tmpCustomer.get().setProfilePic(customerDTO.getProfilePic());
            customerDAO.save(tmpCustomer.get());
        }
    }
    @Override
    public void deleteCustomer(String customerId) {
        Optional<CustomerEntity> selectedCustomerId = customerDAO.findById(customerId);
        if (!selectedCustomerId.isPresent()) {
            throw new CustomerNotFoundException("Customer not found");
        } else {
            customerDAO.deleteById(customerId);
        }
    }
}