package lk.ijse.springpossystem.service.impl;

import lk.ijse.springpossystem.customObj.CustomerErrorResponse;
import lk.ijse.springpossystem.customObj.CustomerResponse;
import lk.ijse.springpossystem.dao.CustomerDAO;
import lk.ijse.springpossystem.dto.CustomerDTO;
import lk.ijse.springpossystem.entity.CustomerEntity;
import lk.ijse.springpossystem.exception.CustomerNotFoundException;
import lk.ijse.springpossystem.exception.DataPersistFailedException;
import lk.ijse.springpossystem.service.CustomerService;
import lk.ijse.springpossystem.util.AppUtil;
import lk.ijse.springpossystem.util.Mapping;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class CustomerServiceIMPL implements CustomerService {

    private static final Logger logger = LoggerFactory.getLogger(CustomerServiceIMPL.class);

    @Autowired
    private final CustomerDAO customerDAO;
    @Autowired
    private final Mapping mapping;

    @Override
    public void saveCustomer(CustomerDTO customerDTO) {
        customerDTO.setId(AppUtil.createCustomerId());
        CustomerEntity savedCustomer = customerDAO.save(mapping.convertToCustomerEntity(customerDTO));
        try {
            if (savedCustomer == null) {
                logger.error("Failed to save customer: {}", customerDTO);
                throw new DataPersistFailedException("Cannot save data");
            }
            logger.info("Customer saved successfully: {}", savedCustomer);
        } catch (Exception e) {
            logger.error("Error saving customer: {}", e.getMessage());
            throw new DataPersistFailedException("Cannot save customer data: " + e.getMessage());
        }
    }
    @Override
    public void updateCustomer(CustomerDTO customerDTO) {
        logger.info("Updating customer: {}", customerDTO);
        Optional<CustomerEntity> tmpCustomer = customerDAO.findById(customerDTO.getId());
        if (!tmpCustomer.isPresent()) {
            logger.warn("Customer not found for update: {}", customerDTO);
            throw new CustomerNotFoundException("Customer Not Found");
        } else {
            tmpCustomer.get().setName(customerDTO.getName());
            tmpCustomer.get().setAddress(customerDTO.getAddress());
            tmpCustomer.get().setContact(customerDTO.getContact());
            tmpCustomer.get().setProfilePic(customerDTO.getProfilePic());
            customerDAO.save(tmpCustomer.get());
            logger.info("Customer updated successfully: {}", tmpCustomer.get());
        }
    }
    @Override
    public void deleteCustomer(String customerId) {
        logger.info("Deleting customer with ID: {}", customerId);
        Optional<CustomerEntity> selectedCustomerId = customerDAO.findById(customerId);
        if (!selectedCustomerId.isPresent()) {
            logger.warn("Customer not found for deletion with ID: {}", customerId);
            throw new CustomerNotFoundException("Customer not found");
        } else {
            customerDAO.deleteById(customerId);
            logger.info("Customer with ID: {} deleted successfully", customerId);
        }
    }
    @Override
    public CustomerResponse getSelectedCustomer(String customerId) {
        logger.info("Fetching customer with ID: {}", customerId);
        if(customerDAO.existsById(customerId)){
            logger.info("Customer found with ID: {}", customerId);
            return mapping.convertToCUstomerDTO(customerDAO.getReferenceById(customerId));
        }else {
            logger.warn("Customer not found with ID: {}", customerId);
            return new CustomerErrorResponse("0","Customer not found");
        }
    }
    @Override
    public List<CustomerDTO> getAllCustomers() {
        logger.info("Fetching all customers");
        return mapping.convertCustomerListToDTO(customerDAO.findAll());
    }
}