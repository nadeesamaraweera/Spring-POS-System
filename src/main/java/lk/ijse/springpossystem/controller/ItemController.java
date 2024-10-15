package lk.ijse.springpossystem.controller;

import lk.ijse.springpossystem.customObj.ItemErrorResponse;
import lk.ijse.springpossystem.customObj.ItemResponse;
import lk.ijse.springpossystem.dto.ItemDTO;
import lk.ijse.springpossystem.exception.DataPersistFailedException;
import lk.ijse.springpossystem.exception.ItemNotFoundException;
import lk.ijse.springpossystem.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger logger = LoggerFactory.getLogger(ItemController.class);

    @Autowired
    private ItemService itemService;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ItemErrorResponse> createItem(@RequestBody ItemDTO item) {
        if (item == null) {
            logger.warn("Item is null");
            return new ResponseEntity<>(new ItemErrorResponse("error", "Item cannot be null"),
                    HttpStatus.BAD_REQUEST);
        }
        if (item.getDescription() == null || item.getDescription().isEmpty()) {
            logger.warn("Item description is missing");
            return new ResponseEntity<>(new ItemErrorResponse("error", "Description is required"),
                    HttpStatus.BAD_REQUEST);
        }
        if (item.getDescription().length() > 50) {
            logger.warn("Item description exceeds max length: {}", item.getDescription());
            return new ResponseEntity<>(new ItemErrorResponse("error",
                    "Description must be 50 characters or less"), HttpStatus.BAD_REQUEST);
        }
        if (item.getPrice() <= 0) {
            logger.warn("Invalid price: Price must be a positive number");
            return new ResponseEntity<>(new ItemErrorResponse("error",
                    "Price must be a positive number"), HttpStatus.BAD_REQUEST);
        }
        if (Double.toString(item.getPrice()).matches(".*[a-zA-Z]+.*")) {
            logger.warn("Invalid price format: Price should not contain letters");
            return new ResponseEntity<>(new ItemErrorResponse("error",
                    "Price must be a valid number without letters"), HttpStatus.BAD_REQUEST);
        }
        if (item.getQty() < 0) {
            logger.warn("Invalid quantity: Quantity cannot be negative");
            return new ResponseEntity<>(new ItemErrorResponse("error",
                    "Quantity cannot be negative"), HttpStatus.BAD_REQUEST);
        }
        if (Integer.toString(item.getQty()).matches(".*[a-zA-Z]+.*")) {
            logger.warn("Invalid quantity format: Quantity should not contain letters");
            return new ResponseEntity<>(new ItemErrorResponse("error",
                    "Quantity must be a valid number without letters"), HttpStatus.BAD_REQUEST);
        }
        try {
            itemService.saveItem(item);
            logger.info("Item created successfully: {}", item);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (DataPersistFailedException e) {
            logger.error("Data persistence failed: {}", e.getMessage());
            return new ResponseEntity<>(new ItemErrorResponse("error",
                    "Data persistence failed"), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            logger.error("Internal server error: {}", e.getMessage());
            return new ResponseEntity<>(new ItemErrorResponse("error",
                    "Internal server error"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PatchMapping(value = "/{code}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ItemErrorResponse> updateItem(
            @PathVariable("code") String itemCode, @RequestBody ItemDTO item) {
        try {
            if (item == null || itemCode == null || itemCode.isEmpty()) {
                logger.warn("Invalid update request: itemCode={}, item={}", itemCode, item);
                return new ResponseEntity<>(new ItemErrorResponse("error",
                        "Item and itemCode cannot be null or empty"), HttpStatus.BAD_REQUEST);
            }
            if (item.getDescription() == null || item.getDescription().isEmpty()) {
                logger.warn("Item description is missing during update for itemCode: {}", itemCode);
                return new ResponseEntity<>(new ItemErrorResponse("error",
                        "Description is required"), HttpStatus.BAD_REQUEST);
            }
            if (item.getDescription().length() > 50) {
                logger.warn("Item description exceeds max length for itemCode: {}", itemCode);
                return new ResponseEntity<>(new ItemErrorResponse("error",
                        "Description must be 50 characters or less"), HttpStatus.BAD_REQUEST);
            }
            if (item.getPrice() <= 0) {
                logger.warn("Invalid price: Price must be a positive number for itemCode: {}", itemCode);
                return new ResponseEntity<>(new ItemErrorResponse("error",
                        "Price must be a positive number"), HttpStatus.BAD_REQUEST);
            }
            if (Double.toString(item.getPrice()).matches(".*[a-zA-Z]+.*")) {
                logger.warn("Invalid price format: Price should not contain letters for itemCode: {}", itemCode);
                return new ResponseEntity<>(new ItemErrorResponse("error",
                        "Price must be a valid number without letters"), HttpStatus.BAD_REQUEST);
            }
            if (item.getQty() < 0) {
                logger.warn("Invalid quantity: Quantity cannot be negative for itemCode: {}", itemCode);
                return new ResponseEntity<>(new ItemErrorResponse("error",
                        "Quantity cannot be negative"), HttpStatus.BAD_REQUEST);
            }
            if (Integer.toString(item.getQty()).matches(".*[a-zA-Z]+.*")) {
                logger.warn("Invalid quantity format: Quantity should not contain letters for itemCode: {}", itemCode);
                return new ResponseEntity<>(new ItemErrorResponse("error",
                        "Quantity must be a valid number without letters"), HttpStatus.BAD_REQUEST);
            }
            itemService.updateItem(itemCode, item);
            logger.info("Item updated successfully: {}", itemCode);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);

        } catch (ItemNotFoundException e) {
            logger.error("Item not found: {}", itemCode);
            return new ResponseEntity<>(new ItemErrorResponse("error",
                    "Item not found"), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            logger.error("Internal server error while updating item: {}", e.getMessage());
            return new ResponseEntity<>(new ItemErrorResponse("error",
                    "Internal server error"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{code}")
    public ResponseEntity<Void> deleteItem(@PathVariable("code") String code) {
        try {
            itemService.deleteItem(code);
            logger.info("Item deleted successfully: {}", code);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (ItemNotFoundException e) {
            logger.error("Item not found: {}", code);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            logger.error("Internal server error: {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping(value = "/{code}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ItemResponse getSelectedItem(@PathVariable ("code") String code)  {
        ItemResponse itemResponse = itemService.getSelectedItem(code);
        if (itemResponse instanceof ItemErrorResponse) {
            logger.warn("Item not found: {}", code);
        } else {
            logger.info("Fetched item details for: {}", code);
        }
        return itemResponse;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<ItemDTO> getAllItems() {
        logger.info("Fetching all items");
        return itemService.getAllItems();
    }
}