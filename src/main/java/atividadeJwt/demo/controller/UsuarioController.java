package atividadeJwt.demo.controller;

import atividadeJwt.demo.dto.UsuarioCadastroDto;
import atividadeJwt.demo.entity.Usuario;
import atividadeJwt.demo.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioRepository repository;

    @Autowired
    private BCryptPasswordEncoder encoder;

    @PostMapping
    public ResponseEntity<Void> cadastrar(@RequestBody UsuarioCadastroDto dto) {
        Usuario usuario = Usuario.builder()
                .nome(dto.nome())
                .email(dto.email())
                .senha(encoder.encode(dto.senha()))
                .role(dto.role())
                .build();
        repository.save(usuario);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping
    public List<Usuario> listarTodos() {
        return repository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Usuario> buscarPorId(@PathVariable Long id, Principal principal) {
        return repository.findById(id)
                .filter(u -> principal.getName().equals(u.getEmail()) || temRoleAdmin(principal))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.FORBIDDEN).build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Usuario> editar(@PathVariable Long id, @RequestBody UsuarioCadastroDto dto, Principal principal) {
        return repository.findById(id)
                .filter(u -> principal.getName().equals(u.getEmail()) || temRoleAdmin(principal))
                .map(u -> {
                    u.setNome(dto.nome());
                    u.setEmail(dto.email());
                    u.setSenha(encoder.encode(dto.senha()));
                    u.setRole(dto.role());
                    repository.save(u);
                    return ResponseEntity.ok(u);
                }).orElse(ResponseEntity.status(HttpStatus.FORBIDDEN).build());
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    private boolean temRoleAdmin(Principal principal) {
        return SecurityContextHolder.getContext().getAuthentication().getAuthorities()
                .stream().anyMatch(a -> a.getAuthority().equals("ADMIN"));
    }
}
