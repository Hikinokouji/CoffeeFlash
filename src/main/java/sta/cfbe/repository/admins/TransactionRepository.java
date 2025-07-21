package sta.cfbe.repository.admins;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import sta.cfbe.entity.exeption.resource.ResourceNotFoundException;
import sta.cfbe.config.MultiBaseConnectorConfiguration;
import sta.cfbe.web.dto.company.TransactionResponse;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Repository
@RequiredArgsConstructor
public class TransactionRepository {

    private final MultiBaseConnectorConfiguration multiBaseConnectorConfiguration;

    private final String SELECT_ALL_TRANSACTIONS = """
            SELECT
                t.id,
                t.created_at,
                mtp.method,
                t.totalprice
            
            FROM 
                main.transactionpayment t
            JOIN
                main.methodpayment mtp ON t.methodpayment = mtp.id
            """;

    public List<TransactionResponse> findAllTransactions(String db) {
        List<TransactionResponse> transactions = new ArrayList<>();
        try(Connection connection = multiBaseConnectorConfiguration.getCustomConnection(db);
            PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_TRANSACTIONS);
            ResultSet resultSet = preparedStatement.executeQuery()){

            while(resultSet.next()) {
                TransactionResponse transactionResponse = new TransactionResponse();

                transactionResponse.setId(resultSet.getString("id"));
                transactionResponse.setCreated_at(resultSet.getTimestamp("created_at"));
                transactionResponse.setMethodPayment(resultSet.getString("method"));
                transactionResponse.setTotalprice(resultSet.getBigDecimal("totalprice"));

                transactions.add(transactionResponse);
            }
            return transactions;

        } catch (SQLException e) {
            log.error("TransactionRepository::findAllTransactions: ", e);
            throw new ResourceNotFoundException("Помилка пошуку транзакції");
        }
    }
}
