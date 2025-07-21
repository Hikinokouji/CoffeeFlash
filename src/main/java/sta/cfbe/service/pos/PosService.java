package sta.cfbe.service.pos;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sta.cfbe.repository.pos.PosRepository;
import sta.cfbe.web.dto.pos.SellingRequest;

@Service
@RequiredArgsConstructor
public class PosService {
    private final PosRepository posRepository;

    public void selling(SellingRequest sellingRequest, String db) {
        posRepository.selling(sellingRequest, db);
    }
}
