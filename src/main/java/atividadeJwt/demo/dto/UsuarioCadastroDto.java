package atividadeJwt.demo.dto;

import atividadeJwt.demo.entity.Usuario;

public record UsuarioCadastroDto(
        String nome,
        String email,
        String senha,
        Usuario.Role role
) {}