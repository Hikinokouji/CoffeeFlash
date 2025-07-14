package sta.cfbe.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sta.cfbe.domain.exeption.resource.ResourceNotFoundException;
import sta.cfbe.repository.ProductRepository;
import sta.cfbe.web.dto.company.ProductRequestDTO;
import sta.cfbe.web.dto.company.ProductResponseDTO;


import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;

    //Формат параметрів 1 Поле Long id, закриваюючий параметр String db, DTO пред останнім
    @Transactional
    public ProductResponseDTO createProduct(ProductRequestDTO product, String db) {
        return productRepository.createProduct(product, db);
    }

    @Transactional(readOnly = true)
    public ProductResponseDTO findProductById(Long id, String db) {
        try{
            Optional<ProductResponseDTO> dto = productRepository.findProductById(id, db);
            return dto.get();
        }catch (RuntimeException e){
            throw new ResourceNotFoundException("Товар за зазначеним id - відсутній");
        }
    }

    @Transactional(readOnly = true)
    public List<ProductResponseDTO> findAllProducts(String db) {
        try{
            return productRepository.findAllProduct(db);
        }catch (Exception e){
            throw new ResourceNotFoundException("Товар не знайдено");
        }
    }

    @Transactional
    public ProductResponseDTO updateProduct(Long productId, ProductRequestDTO productRequestDTO, String db) {
        try{
            Optional<ProductResponseDTO> dto = productRepository.findProductById(productId, db);
            if (dto.isPresent()) {
                return dto.get();
            }
            throw new ResourceNotFoundException("Товар за зазначеним id - відсутній");
        }catch (Exception e){
            throw new ResourceNotFoundException("Товар за зазначеним id - відсутній");
        }
    }

    @Transactional
    public void deleteProduct(Long productId, String db) {
        productRepository.deleteProduct(productId, db);
    }

}
