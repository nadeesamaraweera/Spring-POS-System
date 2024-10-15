package lk.ijse.springpossystem.util;

import lk.ijse.springpossystem.dto.CustomerDTO;
import lk.ijse.springpossystem.dto.ItemDTO;
import lk.ijse.springpossystem.entity.CustomerEntity;
import lk.ijse.springpossystem.entity.ItemEntity;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
@RequiredArgsConstructor
@Component
public class Mapping {
    @Autowired
    private ModelMapper modelMapper;

    //Customer matters mapping
    public CustomerEntity convertToCustomerEntity(CustomerDTO customerDTO) {
        return modelMapper.map(customerDTO, CustomerEntity.class);
    }

    public CustomerDTO convertToCUstomerDTO(CustomerEntity customerEntity) {
        return modelMapper.map(customerEntity, CustomerDTO.class);
    }

    public List<CustomerDTO> convertCustomerListToDTO(List<CustomerEntity> customerEntities) {
        return modelMapper.map(customerEntities, new TypeToken<List<CustomerDTO>>() {
        }.getType());
    }

    //Item  matters mapping
    public ItemEntity convertToItemEntity(ItemDTO itemDTO) {
        return modelMapper.map(itemDTO, ItemEntity.class);
    }
    public ItemDTO convertToItemDTO(ItemEntity itemEntity) {
        return modelMapper.map(itemEntity, ItemDTO.class);
    }
    public List<ItemDTO> convertItemListToDTO(List<ItemEntity> itemEntities) {
        return modelMapper.map(itemEntities, new TypeToken<List<ItemDTO>>() {}.getType());
    }
}