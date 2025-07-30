package sta.cfbe.service.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sta.cfbe.entity.exeption.resource.ResourceNotFoundException;
import sta.cfbe.repository.admins.ProductRepository;
import sta.cfbe.repository.admins.TransactionRepository;
import sta.cfbe.service.configService.ImageService;
import sta.cfbe.web.dto.company.ProductRequestDTO;
import sta.cfbe.web.dto.company.ProductResponseDTO;
import sta.cfbe.web.dto.company.TransactionResponse;


import java.io.InputStream;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductService {
    private final ProductRepository productRepository;
    private final TransactionRepository transactionRepository;
    private final ImageService imageService;

    //Формат параметрів 1 Поле Long id, закриваюючий параметр String db, DTO пред останнім

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

    @Transactional(readOnly = true)
    public void deleteProduct(Long productId, String db) {
        productRepository.deleteProduct(productId, db);
    }

    public List<TransactionResponse> getAllTransactions(String db) {
        return transactionRepository.findAllTransactions(db);
    }

    @Transactional(readOnly = true)
    public InputStream getImageStream(String db, Long productId) {
        String fileName = productRepository.getImageNameByProductId(db, productId);
        try{
            String fullPath = "/" + db + "/" + fileName;
            return imageService.download(fullPath);
        }catch (Exception e){
            throw new ResourceNotFoundException("001");
        }
    }

}
