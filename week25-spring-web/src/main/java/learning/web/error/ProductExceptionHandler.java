package learning.web.error;

import learning.web.product.DuplicateSkuException;
import learning.web.product.ProductNotFoundException;
import learning.web.product.InvalidLimitException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.util.Map;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@RestControllerAdvice
public class ProductExceptionHandler {

    @ExceptionHandler(DuplicateSkuException.class)
    public ResponseEntity<Map<String,String>> handlerDuplicateSku(DuplicateSkuException ex) {
        Map<String,String> error=Map.of(
                "code","DUPLICATE_SKU",
                "message",ex.getMessage()
        );
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<Map<String,String>> handlerProductNotFound(ProductNotFoundException ex) {
        Map<String,String> error=Map.of(
                "code", "PRODUCT_NOT_FOUND",
                "message", ex.getMessage()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidation(MethodArgumentNotValidException ex) {
        return ResponseEntity.badRequest().body(Map.of(
                "code", "INVALID_PRODUCT_INPUT",
                "message", "Product fields are invalid"
        ));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, String>> handleInvalidJson(HttpMessageNotReadableException ex) {
        return ResponseEntity.badRequest().body(Map.of(
                "code", "INVALID_JSON",
                "message", "Invalid JSON request body"
        ));
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Map<String, String>> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        return ResponseEntity.badRequest().body(Map.of(
                "code", "INVALID_PARAMETER",
                "message", "Invalid request parameter type"
        ));
    }

    @ExceptionHandler(InvalidLimitException.class)
    public ResponseEntity<Map<String, String>> handleInvalidLimit(InvalidLimitException ex) {
        return ResponseEntity.badRequest().body(Map.of(
                "code", "INVALID_PARAMETER",
                "message", ex.getMessage()
        ));
    }
}
