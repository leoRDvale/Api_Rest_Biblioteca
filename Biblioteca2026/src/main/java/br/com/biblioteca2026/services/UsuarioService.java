package br.com.biblioteca2026.services;

import br.com.biblioteca2026.exceptions.RequisicaoInvalidaException;
import br.com.biblioteca2026.model.Usuario;
import br.com.biblioteca2026.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    @Autowired
    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public List<Usuario> buscarTodos() {
        return usuarioRepository.findAll();
    }

    public Usuario buscarPorId(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new RequisicaoInvalidaException("Usuário não encontrado com ID: " + id));
    }

    public Usuario criar(Usuario usuario) {
        validar(usuario);
        usuario.setId(null);
        return usuarioRepository.save(usuario);
    }

    public Usuario atualizar(Usuario usuario) {
        if (usuario.getId() == null)
            throw new RequisicaoInvalidaException("O ID é obrigatório para atualização.");
        buscarPorId(usuario.getId());
        validar(usuario);
        return usuarioRepository.save(usuario);
    }

    public void deletar(Long id) {
        buscarPorId(id);
        usuarioRepository.deleteById(id);
    }

    private void validar(Usuario usuario) {
        if (usuario.getNome() == null || usuario.getNome().isBlank())
            throw new RequisicaoInvalidaException("O nome do usuário é obrigatório.");
        if (usuario.getEmail() == null || usuario.getEmail().isBlank())
            throw new RequisicaoInvalidaException("O e-mail do usuário é obrigatório.");
    }
}
