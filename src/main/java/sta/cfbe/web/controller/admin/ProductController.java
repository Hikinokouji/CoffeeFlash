package sta.cfbe.web.controller.admin;

import liquibase.exception.LiquibaseException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import sta.cfbe.service.configService.LiquibaseService;
import sta.cfbe.service.admin.ProductService;
import sta.cfbe.web.dto.company.ProductRequestDTO;
import sta.cfbe.web.dto.company.ProductResponseDTO;
import sta.cfbe.web.dto.company.TransactionResponse;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/product")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;
    private final LiquibaseService liquibaseService;

    //@RreAuthorize - перед виконанням ендпоінту перевіряємо доступ до бази по токену
    @PreAuthorize("@accessChecked.existsByUserIdAndCompanyId(#authHeader, #companyId)")
    @PostMapping("/{companyId}")
    public ResponseEntity<?> addProduct(@RequestHeader("Authorization") String authHeader,
                                        @PathVariable String companyId,
                                        @RequestBody ProductRequestDTO product) throws SQLException, LiquibaseException {
        //Кожен запит перевіряє актуальність бази, для автоматичного оновлення бази, в випадку якщо база є застаріла
        liquibaseService.runLiquibaseForTenant(companyId);
        ProductResponseDTO dto = productService.createProduct(product, companyId);
        return new ResponseEntity<>(dto, HttpStatus.CREATED);
    }

    @PreAuthorize("@accessChecked.existsByUserIdAndCompanyId(#authHeader, #companyId)")
    @GetMapping("/{companyId}/{productId}")
    public ResponseEntity<?> findProduct(@RequestHeader("Authorization") String authHeader,
                                         @PathVariable String companyId,
                                         @PathVariable Long productId) throws SQLException, LiquibaseException {
        liquibaseService.runLiquibaseForTenant(companyId);
        ProductResponseDTO dto = productService.findProductById(productId, companyId);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @PreAuthorize("@accessChecked.existsByUserIdAndCompanyId(#authHeader, #companyId)")
    @GetMapping("/{companyId}")
    public ResponseEntity<?> findAllProduct(@RequestHeader("Authorization") String authHeader,
                                         @PathVariable String companyId) throws SQLException, LiquibaseException {
        liquibaseService.runLiquibaseForTenant(companyId);
        List<ProductResponseDTO> products = productService.findAllProducts(companyId);
        return ResponseEntity.ok(products);
    }

    @PreAuthorize("@accessChecked.existsByUserIdAndCompanyId(#authHeader, #companyId)")
    @PutMapping("/{companyId}/{productId}")
    public ResponseEntity<?> updateProduct(@RequestHeader("Authorization") String authHeader,
                                           @PathVariable String companyId,
                                           @PathVariable Long productId,
                                           @RequestBody ProductRequestDTO product) throws SQLException, LiquibaseException {
        liquibaseService.runLiquibaseForTenant(companyId);
        ProductResponseDTO dto = productService.updateProduct(productId, product, companyId);
        return new ResponseEntity<>(dto, HttpStatus.ACCEPTED);
    }

    @PreAuthorize("@accessChecked.existsByUserIdAndCompanyId(#authHeader, #companyId)")
    @DeleteMapping("/{companyId}/{productId}")
    public HttpStatus deleteProduct(@RequestHeader("Authorization") String authHeader,
                                    @PathVariable String companyId,
                                    @PathVariable Long productId) throws SQLException, LiquibaseException {
        liquibaseService.runLiquibaseForTenant(companyId);
        productService.deleteProduct(productId, companyId);
        return HttpStatus.ACCEPTED;
    }

    @PreAuthorize("@accessChecked.existsByUserIdAndCompanyId(#authHeader, #companyId)")
    @GetMapping("/transaction/{companyId}")
    public ResponseEntity<?> getTransactionProduct(@RequestHeader("Authorization") String authHeader,
                                                   @PathVariable String companyId) throws SQLException, LiquibaseException {
        liquibaseService.runLiquibaseForTenant(companyId);
        List<TransactionResponse> products = productService.getAllTransactions(companyId);
        return ResponseEntity.ok(Map.of("data", products));
    }


}
