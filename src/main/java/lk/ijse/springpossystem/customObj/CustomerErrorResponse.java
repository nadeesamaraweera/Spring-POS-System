package lk.ijse.springpossystem.customObj;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CustomerErrorResponse implements CustomerResponse{
    private String status;
    private String message;
}
