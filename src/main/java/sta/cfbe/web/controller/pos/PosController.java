package sta.cfbe.web.controller.pos;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import sta.cfbe.service.pos.PosService;
import sta.cfbe.web.dto.pos.SellingRequest;

@RestController
@RequestMapping("/pos")
@RequiredArgsConstructor
@Transactional
public class PosController {
    private final PosService posService;

    @PreAuthorize("@accessChecked.existsByUserIdAndCompanyId(#authHeader, #companyId)")
    @PostMapping("selling/{companyId}")
    public HttpStatus sellProduct(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable String companyId,
            @RequestBody SellingRequest sellingRequest){
        posService.selling(sellingRequest, companyId);
        return HttpStatus.ACCEPTED;
    }
}
