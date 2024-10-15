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
public class ItemServiceIMPL implements ItemService {
    private static final Logger logger = LoggerFactory.getLogger(ItemServiceIMPL.class);

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
                logger.error("Failed to save item: {}", itemDTO);
                throw new DataPersistFailedException("Failed to save item data");
            }
            logger.info("Item saved successfully: {}", savedItem);
        } catch (Exception e) {
            logger.error("Error saving item: {}", e.getMessage());
            throw new DataPersistFailedException("Cannot save item data: " + e.getMessage());
        }
    }
    @Override
    public void updateItem(String code, ItemDTO itemDTO) {
        Optional<ItemEntity> tmpItemEntity = itemDAO.findById(code);
        if (!tmpItemEntity.isPresent()) {
            logger.warn("Item not found for update: {}", code);
            throw new ItemNotFoundException("Item not found");
        } else {
            tmpItemEntity.get().setDescription(itemDTO.getDescription());
            tmpItemEntity.get().setPrice(itemDTO.getPrice());
            tmpItemEntity.get().setQty(itemDTO.getQty());
            logger.info("Item updated successfully: {}", code);
        }

    }
    @Override
    public void deleteItem(String code) {
        Optional<ItemEntity> selectedId = itemDAO.findById(code);
        if (!selectedId.isPresent()) {
            logger.warn("Item not found for deletion: {}", code);
            throw new ItemNotFoundException("Item not found");
        } else {
            itemDAO.deleteById(code);
            logger.info("Item deleted: {}", code);
        }
    }
    @Override
    public ItemResponse getSelectedItem(String code) {
        if (itemDAO.existsById(code)) {
            logger.info("Fetching item details for: {}", code);
            return mapping.convertToItemDTO(itemDAO.getReferenceById(code));
        } else {
            logger.warn("Item not found: {}", code);
            return new ItemErrorResponse(0, "Item not found");
        }
    }

    @Override
    public List<ItemDTO> getAllItems() {
        logger.info("Fetching all items");
        return mapping.convertItemListToDTO(itemDAO.findAll());
    }
}

