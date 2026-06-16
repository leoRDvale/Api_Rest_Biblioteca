package br.com.biblioteca2026.services;

import br.com.biblioteca2026.exceptions.RequisicaoInvalidaException;
import br.com.biblioteca2026.model.Categoria;
import br.com.biblioteca2026.repository.CategoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoriaService {

    private final CategoriaRepository categoriaRepository;

    @Autowired
    public CategoriaService(CategoriaRepository categoriaRepository) {
        this.categoriaRepository = categoriaRepository;
    }

    public List<Categoria> buscarTodas() {
        return categoriaRepository.findAll();
    }

    public Categoria buscarPorId(Long id) {
        return categoriaRepository.findById(id)
                .orElseThrow(() -> new RequisicaoInvalidaException("Categoria não encontrada com ID: " + id));
    }

    public Categoria criar(Categoria categoria) {
        if (categoria.getNome() == null || categoria.getNome().isBlank())
            throw new RequisicaoInvalidaException("O nome da categoria é obrigatório.");
        categoria.setId(null);
        return categoriaRepository.save(categoria);
    }

    public Categoria atualizar(Categoria categoria) {
        if (categoria.getId() == null)
            throw new RequisicaoInvalidaException("O ID é obrigatório para atualização.");
        buscarPorId(categoria.getId());
        return categoriaRepository.save(categoria);
    }

    public void deletar(Long id) {
        buscarPorId(id);
        categoriaRepository.deleteById(id);
    }
}
