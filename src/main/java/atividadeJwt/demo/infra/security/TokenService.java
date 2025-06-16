package atividadeJwt.demo.infra.security;

import atividadeJwt.demo.entity.Usuario;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class TokenService {

    @Value("${api.security.token.secret}")
    private String secreto;

    public String geradorToken(Usuario usuario) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secreto);

            String token = JWT.create()
                    .withIssuer("atividadeJwt")
                    .withSubject(usuario.getEmail())
                    .withClaim("role", usuario.getRole().ordinal())
                    .withExpiresAt(this.geradorExpiracao())
                    .sign(algorithm);

            return token;
        } catch (JWTCreationException ex) {
            throw new RuntimeException("Erro ao gerar o token", ex);
        }
    }

    public String validarToken(String token){
        try{
            Algorithm algorithm = Algorithm.HMAC256(secreto);
            return JWT.require(algorithm)
                    .withIssuer("atividadeJwt")
                    .build()
                    .verify(token)
                    .getSubject();
        } catch (JWTVerificationException ex) {
            return null;
        }
    }

    public String getRole(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secreto);
            return JWT.require(algorithm)
                    .withIssuer("atividadeJwt")
                    .build()
                    .verify(token)
                    .getClaim("role")
                    .asString();
        } catch (JWTVerificationException ex) {
            return null;
        }
    }

    private Instant geradorExpiracao (){
        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00"));
    }
}
