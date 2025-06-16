package atividadeJwt.demo.controller;

import atividadeJwt.demo.entity.Usuario;
import atividadeJwt.demo.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    @Autowired
    private UsuarioRepository repository;

    @GetMapping("/usuarios")
    public List<Usuario> listarTodos() {
        return repository.findAll();
    }

    @PutMapping("/usuarios/{id}")
    public Usuario atualizarUsuario(@PathVariable String id, @RequestBody Usuario updatedData) {
        Usuario usuario = repository.findById(id).orElseThrow();

        usuario.setNome(updatedData.getNome());
        usuario.setEmail(updatedData.getEmail());
        usuario.setSenha(updatedData.getSenha());
        usuario.setRole(updatedData.getRole());

        return repository.save(usuario);
    }

    @DeleteMapping("/usuarios/{id}")
    public void deletarUsuario(@PathVariable String id) {
        repository.deleteById(id);
    }
}
