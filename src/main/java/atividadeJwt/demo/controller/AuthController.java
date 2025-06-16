package atividadeJwt.demo.controller;

import atividadeJwt.demo.dto.LoginRequestDTO;
import atividadeJwt.demo.dto.RegisterRequestDTO;
import atividadeJwt.demo.dto.ResponseDTO;
import atividadeJwt.demo.entity.Usuario;
import atividadeJwt.demo.infra.security.TokenService;
import atividadeJwt.demo.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UsuarioRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequestDTO body) {
        Optional<Usuario> usuarioExistente = repository.findByEmail(body.email());

        if (usuarioExistente.isPresent()) {
            return ResponseEntity.badRequest().body("Usuário já existe.");
        }

        Usuario novoUsuario = new Usuario();
        novoUsuario.setNome(body.nome());
        novoUsuario.setEmail(body.email());
        novoUsuario.setSenha(passwordEncoder.encode(body.senha()));
        novoUsuario.setRole(body.role());

        repository.save(novoUsuario);

        String token = tokenService.geradorToken(novoUsuario);
        return ResponseEntity.ok(new ResponseDTO(novoUsuario.getNome(), token));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDTO body) {
        Optional<Usuario> usuarioOptional = repository.findByEmail(body.email());

        if (usuarioOptional.isEmpty()) {
            return ResponseEntity.status(401).body("Credenciais inválidas.");
        }

        Usuario usuario = usuarioOptional.get();
        if (!passwordEncoder.matches(body.senha(), usuario.getSenha())) {
            return ResponseEntity.status(401).body("Credenciais inválidas.");
        }

        String token = tokenService.geradorToken(usuario);
        return ResponseEntity.ok(new ResponseDTO(usuario.getNome(), token));
    }
}
