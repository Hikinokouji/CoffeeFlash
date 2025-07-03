package sta.cfbe.web.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import sta.cfbe.domain.exeption.AccessDeniedException;
import sta.cfbe.domain.user.User;
import sta.cfbe.service.UserService;
import sta.cfbe.service.props.JwtProperties;
import sta.cfbe.web.dto.auth.JwtResponse;

import java.security.Key;
import java.util.Date;


@Service
@RequiredArgsConstructor
public class JwtTokenProvider {

    private final JwtProperties jwtProperties;

    private final UserDetailsService userDetailsService;
    private final UserService userService;
    private Key key;

    @PostConstruct
    public void init() {
        this.key = Keys.hmacShaKeyFor(jwtProperties.getSecret().getBytes());
    }

    public String createAccessToken(Long userId,
                              String phoneNumber) {
        Date now = new Date();
        Date validity = new Date(now.getTime() + jwtProperties.getAccess());

        return Jwts.builder()
                .subject(phoneNumber)
                .claim("userId", userId)
                .issuedAt(now)
                .expiration(validity)
                .signWith(key)
                .compact();
    }

    public String createRefreshToken(Long userId,
                                     String phoneNumber) {
        Date now = new Date();
        Date validity = new Date(now.getTime() + jwtProperties.getRefresh());

        return Jwts.builder()
                .subject(phoneNumber)
                .claim("userId", userId)
                .issuedAt(now)
                .expiration(validity)
                .signWith(key)
                .compact();
    }

    public JwtResponse refreshUserToken(String refreshToken) {
        JwtResponse jwtResponse = new JwtResponse();
        if(!validatToken(refreshToken)){
            throw new AccessDeniedException();
        }
        System.out.println(getId(refreshToken));
        Long userId = Long.valueOf(getId(refreshToken));
        User user = userService.getById(userId);
        jwtResponse.setId(userId);
        jwtResponse.setPhoneNumber(user.getPhoneNumber());
        jwtResponse.setAccessToken(createAccessToken(userId, user.getPhoneNumber()));
        jwtResponse.setRefreshToken(createRefreshToken(userId, user.getPhoneNumber()));
        return jwtResponse;
    }

    public boolean validatToken(String token) {
        Jws<Claims> claims = Jwts
                .parser()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token);

        return !claims.getBody().getExpiration().before(new Date());
    }

    private String getId(String token){
        return Jwts
                .parser()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("userId")
                .toString();
    }

    public String getUSerPhoneNumber(String token){
        return Jwts
                .parser()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody().
                getSubject();
    }

    public Authentication getAuthentication(String token) {
        String userPhone = getUSerPhoneNumber(token);
        UserDetails userDetails = userDetailsService.loadUserByUsername(getId(token));
        return new UsernamePasswordAuthenticationToken(userPhone, "", userDetails.getAuthorities());
    }

}
