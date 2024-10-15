package lk.ijse.springpossystem.service;

import lk.ijse.springpossystem.customObj.ItemResponse;
import lk.ijse.springpossystem.dto.ItemDTO;

import java.util.List;

public interface ItemService {
    void saveItem(ItemDTO itemDTO);

    void updateItem(String code, ItemDTO itemDTO);

    void deleteItem(String code);

    ItemResponse getSelectedItem(String code);

    List<ItemDTO> getAllItems();
}
