package atividadeJwt.demo.controller;

import atividadeJwt.demo.entity.Usuario;
import atividadeJwt.demo.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/usuario")
public class UsuarioController {

    @Autowired
    private UsuarioRepository repository;

    @GetMapping("/profile")
    public Usuario getProfile(@AuthenticationPrincipal UserDetails userDetails) {
        return repository.findByEmail(userDetails.getUsername()).orElseThrow();
    }

    @PutMapping("/profile")
    public Usuario updateProfile(@AuthenticationPrincipal UserDetails userDetails, @RequestBody Usuario updatedData) {
        Usuario usuario = repository.findByEmail(userDetails.getUsername()).orElseThrow();

        usuario.setNome(updatedData.getNome());
        usuario.setSenha(updatedData.getSenha());
        return repository.save(usuario);
    }
}
