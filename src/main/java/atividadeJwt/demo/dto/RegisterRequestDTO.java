package atividadeJwt.demo.dto;

import atividadeJwt.demo.entity.UsuarioRole;

public record RegisterRequestDTO(String nome, String email, String senha, UsuarioRole role) {
}
