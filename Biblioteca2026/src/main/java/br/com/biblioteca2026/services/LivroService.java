package br.com.biblioteca2026.services;

import br.com.biblioteca2026.exceptions.RequisicaoInvalidaException;
import br.com.biblioteca2026.model.Autor;
import br.com.biblioteca2026.model.Categoria;
import br.com.biblioteca2026.model.Livro;
import br.com.biblioteca2026.repository.AutorRepository;
import br.com.biblioteca2026.repository.CategoriaRepository;
import br.com.biblioteca2026.repository.LivroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LivroService {

    private final LivroRepository livroRepository;
    private final AutorRepository autorRepository;
    private final CategoriaRepository categoriaRepository;

    @Autowired
    public LivroService(LivroRepository livroRepository,
                        AutorRepository autorRepository,
                        CategoriaRepository categoriaRepository) {
        this.livroRepository = livroRepository;
        this.autorRepository = autorRepository;
        this.categoriaRepository = categoriaRepository;
    }

    public List<Livro> buscarTodos() {
        return livroRepository.findAll();
    }

    public Livro buscarPorId(Long id) {
        return livroRepository.findById(id)
                .orElseThrow(() -> new RequisicaoInvalidaException("Livro não encontrado com ID: " + id));
    }

    public Livro criar(Livro livro) {
        validar(livro);
        livro.setId(null);
        livro.setDisponivel(true);
        resolverRelacionamentos(livro);
        return livroRepository.save(livro);
    }

    public Livro atualizar(Livro livro) {
        if (livro.getId() == null)
            throw new RequisicaoInvalidaException("O ID é obrigatório para atualização.");
        buscarPorId(livro.getId());
        validar(livro);
        resolverRelacionamentos(livro);
        return livroRepository.save(livro);
    }

    public void deletar(Long id) {
        buscarPorId(id);
        livroRepository.deleteById(id);
    }

    private void validar(Livro livro) {
        if (livro.getTitulo() == null || livro.getTitulo().isBlank())
            throw new RequisicaoInvalidaException("O título do livro é obrigatório.");
        if (livro.getAutor() == null || livro.getAutor().getId() == null)
            throw new RequisicaoInvalidaException("O autor do livro é obrigatório.");
    }

    private void resolverRelacionamentos(Livro livro) {
        Autor autor = autorRepository.findById(livro.getAutor().getId())
                .orElseThrow(() -> new RequisicaoInvalidaException(
                        "Autor não encontrado com ID: " + livro.getAutor().getId()));
        livro.setAutor(autor);

        if (livro.getCategoria() != null && livro.getCategoria().getId() != null) {
            Categoria categoria = categoriaRepository.findById(livro.getCategoria().getId())
                    .orElseThrow(() -> new RequisicaoInvalidaException(
                            "Categoria não encontrada com ID: " + livro.getCategoria().getId()));
            livro.setCategoria(categoria);
        }
    }
}
