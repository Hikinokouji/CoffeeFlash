package sta.cfbe.web.security;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;
import sta.cfbe.domain.exeption.resource.ResourceNotFoundException;

import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.security.SignatureException;

@AllArgsConstructor
public class JwtTokenFilter extends GenericFilterBean {

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        String bearerToken = ((HttpServletRequest)servletRequest).getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            bearerToken = bearerToken.substring(7);
        }
        if (bearerToken != null && jwtTokenProvider.validatToken(bearerToken)) {
            try{
                Authentication authentication = jwtTokenProvider.getAuthentication(bearerToken);
                if (authentication != null) {
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }catch (ResourceNotFoundException ignored){
                throw new ResourceNotFoundException("Token not found");
            }catch (ExpiredJwtException expiredJwtException) {
                throw new AccessDeniedException("Доступ заборонений");
            }
        }
        //Передає запит далі по цепі фільтрів
        filterChain.doFilter(servletRequest, servletResponse);
    }
}
