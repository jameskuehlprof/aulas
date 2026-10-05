package br.com.exemplo.lojavirtual.security;

import java.util.List;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import br.com.exemplo.lojavirtual.model.Usuario;
import br.com.exemplo.lojavirtual.repository.UsuarioRepository;

/**
 * Ponte entre o Spring Security e nossa própria tabela de usuários.
 * O Spring Security não sabe nada sobre JPA ou sobre a entidade Usuario —
 * ele só entende a interface UserDetails, então é esta classe que faz a
 * "tradução" de uma para a outra.
 */
@Service
public class UsuarioDetailsService implements UserDetailsService {

    private final UsuarioRepository repository;

    public UsuarioDetailsService(UsuarioRepository repository) {
        this.repository = repository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario usuario = repository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado: " + username));

        return User.builder()
                .username(usuario.getUsername())
                .password(usuario.getSenha())
                .disabled(!usuario.isHabilitado())
                .authorities(List.of(() -> "ROLE_USER"))
                .build();
    }
}
