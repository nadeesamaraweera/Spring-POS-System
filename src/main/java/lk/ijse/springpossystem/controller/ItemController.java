package lk.ijse.springpossystem.controller;

import lk.ijse.springpossystem.customObj.ItemResponse;
import lk.ijse.springpossystem.dto.ItemDTO;
import lk.ijse.springpossystem.exception.DataPersistFailedException;
import lk.ijse.springpossystem.exception.ItemNotFoundException;
import lk.ijse.springpossystem.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/items")
@RequiredArgsConstructor
public class ItemController {
    @Autowired
    private ItemService itemService;
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> saveItem(@RequestBody ItemDTO item) {
        if (item == null || item.getDescription() == null || item.getPrice() <= 0 || item.getQty() < 0) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        try {
            itemService.saveItem(item);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (DataPersistFailedException e) {
            System.err.println("Data persistence failed: " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            System.err.println("Internal server error: " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PatchMapping(value = "/{code}",produces = MediaType.APPLICATION_JSON_VALUE)  // http://localhost:8080/NoteTaker_war_exploded/api/v1/notes/4f8a0a67-2ebc-41b2-9de6-4e9bcdba65bb
    public ResponseEntity<Void> updateNote(@PathVariable ("code") String itemCode, @RequestBody ItemDTO item) {
        try {
            if (item == null && (itemCode == null || item.equals(""))) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            itemService.updateItem(itemCode, item);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (ItemNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @DeleteMapping("/{code}")
    public ResponseEntity<Void> deleteItem(@PathVariable("code") String code) {
        try {
            itemService.deleteItem(code);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (ItemNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping(value = "/{code}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ItemResponse getSelecteItem(@PathVariable ("code") String code)  {
        return itemService.getSelectedItem(code);
    }
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<ItemDTO> getAllItems() {
        return itemService.getAllItems();
    }
}