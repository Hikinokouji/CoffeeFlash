package sta.cfbe.repository.pos;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import sta.cfbe.entity.exeption.resource.ResourceNotFoundException;
import sta.cfbe.config.MultiBaseConnectorConfiguration;
import sta.cfbe.web.dto.pos.ProductSellingDTO;
import sta.cfbe.web.dto.pos.SellingRequest;

import java.sql.*;
import java.time.LocalDateTime;

@Slf4j
@Repository
@RequiredArgsConstructor
public class PosRepository {
    private final MultiBaseConnectorConfiguration multiBaseConnectorConfiguration;

    private final String INSERT_TO_TRANSACTIONALPAYMENT = """
            INSERT INTO main.transactionpayment (methodpayment, created_at, totalprice)
            VALUES (?, ?, ?)
            """;

    private final String INSERT_TO_TRANSACTIONAL = """
            INSERT INTO main.transactional (transaction_id, product_id, quantity, price_at_purchase)
            VALUES (?, ?, ?, ?)
            """;


    public void selling(SellingRequest sellDTO, String db){
        try(Connection connection = multiBaseConnectorConfiguration.getCustomConnection(db)){
            connection.setAutoCommit(false);
            try{
                PreparedStatement pS1 = connection.prepareStatement(INSERT_TO_TRANSACTIONALPAYMENT,
                        Statement.RETURN_GENERATED_KEYS);
                pS1.setInt(1, sellDTO.getMethodPayment());
                pS1.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));
                pS1.setBigDecimal(3, sellDTO.getTotalPrice());
                pS1.executeUpdate();
                ResultSet rs = pS1.getGeneratedKeys();
                Long transaction_id = null;
                if(rs.next()){
                    transaction_id = rs.getLong(1);
                }else{
                    throw new ResourceNotFoundException("З бази не було отримано transaction_id");
                }

                PreparedStatement pS2 = connection.prepareStatement(INSERT_TO_TRANSACTIONAL);
                for (ProductSellingDTO product : sellDTO.getProducts()){
                    pS2.setLong(1, transaction_id);
                    pS2.setLong(2, product.getId());
                    pS2.setInt(3, product.getQuantity());
                    pS2.setBigDecimal(4, sellDTO.getTotalPrice());
                    pS2.executeUpdate();
                }

                connection.commit();

            }catch (SQLException e){
                connection.rollback();
                log.error("Rollback method: selling", e);
                throw new SQLException("Помилка бази даних");
            }finally {
                connection.setAutoCommit(true);
            }
        } catch (SQLException e) {
            throw new ResourceNotFoundException("Помилка бази даних");
        }
    }

}
