package sta.cfbe.web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import sta.cfbe.service.ProductService;
import sta.cfbe.web.dto.company.ProductRequestDTO;
import sta.cfbe.web.dto.company.ProductResponseDTO;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/product")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @PreAuthorize("@accessChecked.existsByUserIdAndCompanyId(#authHeader, #companyId)")
    @PostMapping("/{companyId}")
    public ResponseEntity<?> addProduct(@RequestHeader("Authorization") String authHeader,
                                        @PathVariable String companyId,
                                        @RequestBody ProductRequestDTO product) {
        ProductResponseDTO dto = productService.createProduct(product, companyId);
        return new ResponseEntity<>(dto, HttpStatus.CREATED);
    }

    @PreAuthorize("@accessChecked.existsByUserIdAndCompanyId(#authHeader, #companyId)")
    @GetMapping("/{companyId}/{productId}")
    public ResponseEntity<?> findProduct(@RequestHeader("Authorization") String authHeader,
                                         @PathVariable String companyId,
                                         @PathVariable Long productId) {
        ProductResponseDTO dto = productService.findProductById(productId, companyId);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @PreAuthorize("@accessChecked.existsByUserIdAndCompanyId(#authHeader, #companyId)")
    @GetMapping("/{companyId}")
    public ResponseEntity<?> findAllProduct(@RequestHeader("Authorization") String authHeader,
                                         @PathVariable String companyId) {
        List<ProductResponseDTO> products = productService.findAllProducts(companyId);
        return ResponseEntity.ok(products);
    }

    @PreAuthorize("@accessChecked.existsByUserIdAndCompanyId(#authHeader, #companyId)")
    @PutMapping("/{companyId}/{productId}")
    public ResponseEntity<?> updateProduct(@RequestHeader("Authorization") String authHeader,
                                           @PathVariable String companyId,
                                           @PathVariable Long productId,
                                           @RequestBody ProductRequestDTO product) {
        ProductResponseDTO dto = productService.updateProduct(productId, product, companyId);
        return new ResponseEntity<>(dto, HttpStatus.ACCEPTED);
    }

    @PreAuthorize("@accessChecked.existsByUserIdAndCompanyId(#authHeader, #companyId)")
    @DeleteMapping("/{companyId}/{productId}")
    public HttpStatus deleteProduct(@RequestHeader("Authorization") String authHeader,
                                    @PathVariable String companyId,
                                    @PathVariable Long productId){

        productService.deleteProduct(productId, companyId);
        return HttpStatus.ACCEPTED;
    }


}
