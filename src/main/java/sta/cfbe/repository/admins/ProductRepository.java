package sta.cfbe.repository.admins;



import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import sta.cfbe.entity.exeption.resource.ResourceDuplicateException;
import sta.cfbe.entity.exeption.resource.ResourceNotFoundException;
import sta.cfbe.config.MultiBaseConnectorConfiguration;
import sta.cfbe.web.dto.company.ProductRequestDTO;
import sta.cfbe.web.dto.company.ProductResponseDTO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Repository
@RequiredArgsConstructor
public class ProductRepository{
    private final MultiBaseConnectorConfiguration multiBaseConnectorConfiguration;

    private final String CREATE_PRODUCT = """
            INSERT INTO main.product (productname, price)
            VALUES (?, ?);
            """;

    private final String READ_PRODCUT_BY_ID = """
            SELECT * FROM main.product WHERE id = ?;
            """;
    public final String READ_ALL_PRODUCT = """
        SELECT * FROM main.product;
    """;

    private final String UPDATE_PRODUCT = """
        UPDATE main.product 
        SET productname = ?, price = ? 
        WHERE id = ?
        RETURNING id, productname, price;
    """;

    private final String DELETE_PRODUCT_BY_ID = """
        DELETE FROM main.product WHERE id = ?;
    """;

    private final String SELECT_BY_TRANSACTION_ID = """
            SELECT 
            """;



    public ProductResponseDTO createProduct(ProductRequestDTO productRequestDTO, String db){
        try(Connection connection = multiBaseConnectorConfiguration.getCustomConnection(db);
            PreparedStatement statement = connection.prepareStatement(CREATE_PRODUCT, Statement.RETURN_GENERATED_KEYS)){
            statement.setString(1, productRequestDTO.getProductName());
            statement.setInt(2, productRequestDTO.getPrice());
            statement.executeUpdate();

            try(ResultSet rs = statement.getGeneratedKeys()) {
                rs.next();
                Long id = rs.getLong(1);
                return new ProductResponseDTO(id, productRequestDTO.getProductName(), productRequestDTO.getPrice());
            }
        }catch (SQLException sqlException) {
            log.error("ProductRepository::createProdcut: ", sqlException);
            throw new ResourceDuplicateException("Товар з такою назвою існує.");
        }
    }

    public Optional<ProductResponseDTO> findProductById(Long id, String db) {
        try(Connection connection = multiBaseConnectorConfiguration.getCustomConnection(db);
            PreparedStatement statement = connection.prepareStatement(READ_PRODCUT_BY_ID)){
            statement.setLong(1, id);
            statement.execute();

            try(ResultSet rs = statement.getResultSet()) {
                if(rs.next()) {
                    return Optional.of(new ProductResponseDTO(id, rs.getString(2), rs.getInt(3)));
                }
                throw new ResourceNotFoundException("Товар за зазначеним id - відсутній");
            }
        }catch (SQLException sqlException) {
            log.error("ProductRepository::findProductById: ", sqlException);
            throw new ResourceNotFoundException("Товар за зазначеним id - відсутній");
        }
    }

    public List<ProductResponseDTO> findAllProduct(String db) {
        try(Connection connection = multiBaseConnectorConfiguration.getCustomConnection(db);
            PreparedStatement statement = connection.prepareStatement(READ_ALL_PRODUCT)){
            statement.execute();

            try(ResultSet rs = statement.getResultSet()) {
                List<ProductResponseDTO> products = new ArrayList<>();
                while(rs.next()) {
                    products.add(new ProductResponseDTO(rs.getLong(1), rs.getString(2), rs.getInt(3)));
                }
                return products;
            }
        }catch (SQLException sqlException) {
            log.error("ProductRepository::findAllProduct: ", sqlException);
            throw new ResourceNotFoundException("Товар не знайдено");
        }
    }

    public Optional<ProductResponseDTO> updateProduct(Long productId, ProductRequestDTO productRequestDTO, String db) {
        try(Connection connection = multiBaseConnectorConfiguration.getCustomConnection(db);
            PreparedStatement statement = connection.prepareStatement(UPDATE_PRODUCT)){
            statement.setString(1, productRequestDTO.getProductName());
            statement.setInt(2, productRequestDTO.getPrice());
            statement.setLong(3, productId);
            try(ResultSet rs = statement.executeQuery()) {
                if(rs.next()) {
                    return Optional.of(new ProductResponseDTO(rs.getLong(1), rs.getString(2), rs.getInt(3)));
                }
                throw new ResourceNotFoundException("Товар за зазначеним id - відсутній");
            }
        } catch (SQLException sqlException) {
            log.error("ProductRepository::updateProduct: ", sqlException);
            throw new ResourceNotFoundException("Товар за зазначеним id - відсутній");
        }
    }

    public void deleteProduct(Long productId, String db) {
        try(Connection connection = multiBaseConnectorConfiguration.getCustomConnection(db);
            PreparedStatement statement = connection.prepareStatement(DELETE_PRODUCT_BY_ID)){
            statement.setLong(1, productId);
            statement.execute();

        } catch (SQLException sqlException) {
            log.error("ProductRepository::updateProduct: ", sqlException);
            throw new ResourceNotFoundException("Товар за зазначеним id - відсутній");
        }
    }
}
