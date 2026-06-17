package br.com.biblioteca2026.repository;

import br.com.biblioteca2026.model.Emprestimo;
import br.com.biblioteca2026.model.Livro;
import br.com.biblioteca2026.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmprestimoRepository extends JpaRepository<Emprestimo, Long> {

    long countByUsuarioAndStatus(Usuario usuario, Emprestimo.Status status);

    boolean existsByLivroAndStatus(Livro livro, Emprestimo.Status status);

    List<Emprestimo> findByUsuario(Usuario usuario);

    List<Emprestimo> findByUsuarioAndStatus(Usuario usuario, Emprestimo.Status status);
}
