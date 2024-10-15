package lk.ijse.springpossystem.service.impl;

import lk.ijse.springpossystem.customObj.ItemErrorResponse;
import lk.ijse.springpossystem.customObj.ItemResponse;
import lk.ijse.springpossystem.dao.ItemDAO;
import lk.ijse.springpossystem.dto.ItemDTO;
import lk.ijse.springpossystem.entity.ItemEntity;
import lk.ijse.springpossystem.exception.DataPersistFailedException;
import lk.ijse.springpossystem.exception.ItemNotFoundException;
import lk.ijse.springpossystem.service.ItemService;
import lk.ijse.springpossystem.util.AppUtil;
import lk.ijse.springpossystem.util.Mapping;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class ItemServiceIMPL implements ItemService {
    @Autowired
    private final ItemDAO itemDAO;
    @Autowired
    private final Mapping mapping;

    @Override
    public void saveItem(ItemDTO itemDTO) {
        itemDTO.setCode(AppUtil.createItemCode());
        try {
            ItemEntity itemEntity = mapping.convertToItemEntity(itemDTO);
            ItemEntity savedItem = itemDAO.save(itemEntity);
            if (savedItem == null) {
                throw new DataPersistFailedException("Failed to save item data");
            }
        } catch (Exception e) {
            System.err.println("Error saving item: " + e.getMessage());
            throw new DataPersistFailedException("Cannot save item data: " + e.getMessage());
        }
    }
    @Override
    public void updateItem(String code, ItemDTO itemDTO) {
        Optional<ItemEntity> tmpItemEntity= itemDAO.findById(code);
        if(!tmpItemEntity.isPresent()){
            throw new ItemNotFoundException("Item not found");
        }else {
            tmpItemEntity.get().setDescription(itemDTO.getDescription());
            tmpItemEntity.get().setPrice(itemDTO.getPrice());
            tmpItemEntity.get().setQty(itemDTO.getQty());
        }
    }
    @Override
    public void deleteItem(String code) {
        Optional<ItemEntity> selectedId = itemDAO.findById(code);
        if (!selectedId.isPresent()) {
            throw new ItemNotFoundException("Item not found");
        } else {
            itemDAO.deleteById(code);
        }
    }
    @Override
    public ItemResponse getSelectedItem(String code) {
            if(itemDAO.existsById(code)){
                return mapping.convertToItemDTO(itemDAO.getReferenceById(code));
            }else {
                return new ItemErrorResponse(0,"Item not found");
            }
    }

    @Override
    public List<ItemDTO> getAllItems() {
        return mapping.convertItemListToDTO(itemDAO.findAll());
    }
}

