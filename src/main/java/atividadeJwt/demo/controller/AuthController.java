package atividadeJwt.demo.controller;

import atividadeJwt.demo.dto.LoginDto;
import atividadeJwt.demo.dto.TokenDto;
import atividadeJwt.demo.entity.Usuario;
import atividadeJwt.demo.repository.UsuarioRepository;
import atividadeJwt.demo.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {

    @Autowired
    private UsuarioRepository repository;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private BCryptPasswordEncoder encoder;

    @PostMapping("/login")
    public ResponseEntity<TokenDto> login(@RequestBody LoginDto dto) {
        Usuario usuario = repository.findByEmail(dto.email())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        if (encoder.matches(dto.senha(), usuario.getSenha())) {
            String token = jwtUtil.gerarToken(usuario);
            return ResponseEntity.ok(new TokenDto(token));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
}
