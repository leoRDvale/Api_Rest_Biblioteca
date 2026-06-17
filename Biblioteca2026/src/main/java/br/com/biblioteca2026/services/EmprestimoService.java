package br.com.biblioteca2026.services;

import br.com.biblioteca2026.exceptions.RequisicaoInvalidaException;
import br.com.biblioteca2026.model.Emprestimo;
import br.com.biblioteca2026.model.Livro;
import br.com.biblioteca2026.model.Usuario;
import br.com.biblioteca2026.repository.EmprestimoRepository;
import br.com.biblioteca2026.repository.LivroRepository;
import br.com.biblioteca2026.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class EmprestimoService {

    private static final int LIMITE_EMPRESTIMOS = 3;

    private final EmprestimoRepository emprestimoRepository;
    private final LivroRepository livroRepository;
    private final UsuarioRepository usuarioRepository;

    @Autowired
    public EmprestimoService(EmprestimoRepository emprestimoRepository,
                             LivroRepository livroRepository,
                             UsuarioRepository usuarioRepository) {
        this.emprestimoRepository = emprestimoRepository;
        this.livroRepository = livroRepository;
        this.usuarioRepository = usuarioRepository;
    }

    public List<Emprestimo> buscarTodos() {
        return emprestimoRepository.findAll();
    }

    public Emprestimo buscarPorId(Long id) {
        return emprestimoRepository.findById(id)
                .orElseThrow(() -> new RequisicaoInvalidaException("Empréstimo não encontrado com ID: " + id));
    }

    public List<Emprestimo> buscarPorUsuario(Long usuarioId) {
        Usuario usuario = buscarUsuario(usuarioId);
        return emprestimoRepository.findByUsuario(usuario);
    }

    public List<Emprestimo> buscarAtivosDoUsuario(Long usuarioId) {
        Usuario usuario = buscarUsuario(usuarioId);
        return emprestimoRepository.findByUsuarioAndStatus(usuario, Emprestimo.Status.ATIVO);
    }

    @Transactional
    public Emprestimo realizarEmprestimo(Long usuarioId, Long livroId, LocalDate dataPrevistaDevolucao) {

        Usuario usuario = buscarUsuario(usuarioId);
        Livro livro = buscarLivro(livroId);

        long emprestimosAtivos = emprestimoRepository
                .countByUsuarioAndStatus(usuario, Emprestimo.Status.ATIVO);

        if (emprestimosAtivos >= LIMITE_EMPRESTIMOS) {
            throw new RequisicaoInvalidaException(
                    "O usuário já possui " + LIMITE_EMPRESTIMOS +
                    " empréstimos ativos. Devolva um livro antes de realizar um novo empréstimo.");
        }

        if (!livro.getDisponivel()) {
            throw new RequisicaoInvalidaException(
                    "O livro '" + livro.getTitulo() + "' não está disponível para empréstimo.");
        }

        if (dataPrevistaDevolucao == null || dataPrevistaDevolucao.isBefore(LocalDate.now())) {
            throw new RequisicaoInvalidaException(
                    "A data prevista de devolução deve ser uma data futura.");
        }

        livro.setDisponivel(false);
        livroRepository.save(livro);

        Emprestimo emprestimo = new Emprestimo();
        emprestimo.setUsuario(usuario);
        emprestimo.setLivro(livro);
        emprestimo.setDataEmprestimo(LocalDate.now());
        emprestimo.setDataPrevistaDevolucao(dataPrevistaDevolucao);
        emprestimo.setStatus(Emprestimo.Status.ATIVO);

        return emprestimoRepository.save(emprestimo);
    }

    @Transactional
    public Emprestimo realizarDevolucao(Long emprestimoId) {

        Emprestimo emprestimo = buscarPorId(emprestimoId);

        if (emprestimo.getStatus() == Emprestimo.Status.DEVOLVIDO) {
            throw new RequisicaoInvalidaException("Este empréstimo já foi devolvido.");
        }

        Livro livro = emprestimo.getLivro();
        livro.setDisponivel(true);
        livroRepository.save(livro);

        emprestimo.setStatus(Emprestimo.Status.DEVOLVIDO);
        emprestimo.setDataDevolucao(LocalDate.now());

        return emprestimoRepository.save(emprestimo);
    }


    private Usuario buscarUsuario(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new RequisicaoInvalidaException("Usuário não encontrado com ID: " + id));
    }

    private Livro buscarLivro(Long id) {
        return livroRepository.findById(id)
                .orElseThrow(() -> new RequisicaoInvalidaException("Livro não encontrado com ID: " + id));
    }
}
