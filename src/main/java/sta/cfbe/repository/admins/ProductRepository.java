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
        SET productname = ?, price = ?, 
        WHERE id = ?
        RETURNING id, productname, price;
    """;

    private final String DELETE_PRODUCT_BY_ID = """
        DELETE FROM main.product WHERE id = ?;
    """;

    private final String SELECT_BY_TRANSACTION_ID = """
            SELECT 
            """;

    private final String SAVE_IMAGE = """
            UPDATE main.product
            SET image_url = ?
            WHERE (id = ?);
            """;

    private final String GET_IMAGE_BY_PRODUCT_ID = """
            SELECT image_url
            FROM main.product
            WHERE id = ?;
            """;


    public ProductResponseDTO createProduct(ProductRequestDTO productRequestDTO, String db){
        log.info("Create product, for DataBase: {}", db);
        try(Connection connection = multiBaseConnectorConfiguration.getCustomConnection(db);
            PreparedStatement statement = connection.prepareStatement(CREATE_PRODUCT, Statement.RETURN_GENERATED_KEYS)){
            statement.setString(1, productRequestDTO.getProductName());
            statement.setInt(2, productRequestDTO.getPrice());
            statement.executeUpdate();

            try(ResultSet rs = statement.getGeneratedKeys()) {
                rs.next();
                Long id = rs.getLong(1);
                return new ProductResponseDTO(id, productRequestDTO.getProductName(), productRequestDTO.getPrice(), null);
            }
        }catch (SQLException sqlException) {
            log.error("ProductRepository::createProduct: ", sqlException);
            throw new ResourceDuplicateException("Товар з такою назвою існує.");
        }
    }

    public Optional<ProductResponseDTO> findProductById(Long productId, String db) {
        log.info("Find Product by id, for DataBase: {}, by id: {}", db, productId);
        try(Connection connection = multiBaseConnectorConfiguration.getCustomConnection(db);
            PreparedStatement statement = connection.prepareStatement(READ_PRODCUT_BY_ID)){
            statement.setLong(1, productId);
            statement.execute();

            try(ResultSet rs = statement.getResultSet()) {
                if(rs.next()) {
                    return Optional.of(new ProductResponseDTO(productId, rs.getString(2), rs.getInt(3), rs.getString(4)));
                }
                throw new ResourceNotFoundException("Товар за зазначеним id - відсутній");
            }
        }catch (SQLException sqlException) {
            log.error("ProductRepository::findProductById: ", sqlException);
            throw new ResourceNotFoundException("Товар за зазначеним id - відсутній");
        }
    }

    public List<ProductResponseDTO> findAllProduct(String db) {
        log.info("Find all product, for DataBase: {}", db);
        try(Connection connection = multiBaseConnectorConfiguration.getCustomConnection(db);
            PreparedStatement statement = connection.prepareStatement(READ_ALL_PRODUCT)){
            statement.execute();

            try(ResultSet rs = statement.getResultSet()) {
                List<ProductResponseDTO> products = new ArrayList<>();
                while(rs.next()) {
                    products.add(new ProductResponseDTO(rs.getLong(1), rs.getString(2), rs.getInt(3), rs.getString(4)));
                }
                return products;
            }
        }catch (SQLException sqlException) {
            log.error("ProductRepository::findAllProduct: ", sqlException);
            throw new ResourceNotFoundException("Товар не знайдено");
        }
    }

    public Optional<ProductResponseDTO> updateProduct(Long productId, ProductRequestDTO productRequestDTO, String db) {
        log.info("Update product, for DataBase: {}, by id: {}", db, productId);
        try(Connection connection = multiBaseConnectorConfiguration.getCustomConnection(db);
            PreparedStatement statement = connection.prepareStatement(UPDATE_PRODUCT)){
            statement.setString(1, productRequestDTO.getProductName());
            statement.setInt(2, productRequestDTO.getPrice());
            statement.setLong(3, productId);
            try(ResultSet rs = statement.executeQuery()) {
                if(rs.next()) {
                    return Optional.of(new ProductResponseDTO(rs.getLong(1), rs.getString(2), rs.getInt(3), null));
                }
                throw new ResourceNotFoundException("Товар за зазначеним id - відсутній");
            }
        } catch (SQLException sqlException) {
            log.error("ProductRepository::updateProduct: ", sqlException);
            throw new ResourceNotFoundException("Товар за зазначеним id - відсутній");
        }
    }

    public void deleteProduct(Long productId, String db) {
        log.info("Delete product, for DataBase: {}, by id: {}", db, productId);
        try(Connection connection = multiBaseConnectorConfiguration.getCustomConnection(db);
            PreparedStatement statement = connection.prepareStatement(DELETE_PRODUCT_BY_ID)){
            statement.setLong(1, productId);
            statement.execute();

        } catch (SQLException sqlException) {
            log.error("ProductRepository::updateProduct: ", sqlException);
            throw new ResourceNotFoundException("Товар за зазначеним id - відсутній");
        }
    }

    public void saveImage(String fileName, Long productId, String db) {
        log.info("Save image, for DataBase: {}, by id: {}", db, productId);
        Optional<ProductResponseDTO> checked = findProductById(productId, db);
        if(checked.isPresent()) {
            try(Connection connection = multiBaseConnectorConfiguration.getCustomConnection(db);
                PreparedStatement statement = connection.prepareStatement(SAVE_IMAGE)){
                statement.setString(1, fileName);
                statement.setLong(2, productId);
                statement.execute();
            }catch (SQLException sqlException) {
                log.error("ProductRepository::saveImage: ", sqlException);
                throw new ResourceNotFoundException("004");
            }
        }
    }

    public String getImageNameByProductId(String db, Long productId) {
        log.info("Get image name, for DataBase: {}, by id: {}", db, productId);
        try(Connection connection = multiBaseConnectorConfiguration.getCustomConnection(db);
            PreparedStatement statement = connection.prepareStatement(GET_IMAGE_BY_PRODUCT_ID)){
            statement.setLong(1, productId);
            statement.execute();

            try(ResultSet rs = statement.getResultSet()) {
                if(rs.next()) {
                    return rs.getString(1);
                }
                throw new ResourceNotFoundException("Товар за зазначеним id - відсутній");
            }catch (SQLException sqlException) {
                log.error("ProductRepository::getImageNameByProdcutId: ", sqlException);
                throw new ResourceNotFoundException("002");
            }
        }catch (Exception exception){
            throw new ResourceNotFoundException("003");
        }
    }
}
