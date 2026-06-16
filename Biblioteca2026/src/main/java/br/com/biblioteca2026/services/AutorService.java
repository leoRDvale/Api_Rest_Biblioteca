package br.com.biblioteca2026.services;

import br.com.biblioteca2026.exceptions.RequisicaoInvalidaException;
import br.com.biblioteca2026.model.Autor;
import br.com.biblioteca2026.repository.AutorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AutorService {

    private final AutorRepository autorRepository;

    @Autowired
    public AutorService(AutorRepository autorRepository) {
        this.autorRepository = autorRepository;
    }

    public List<Autor> buscarTodos() {
        return autorRepository.findAll();
    }

    public Autor buscarPorId(Long id) {
        return autorRepository.findById(id)
                .orElseThrow(() -> new RequisicaoInvalidaException("Autor não encontrado com ID: " + id));
    }

    public Autor criar(Autor autor) {
        if (autor.getNome() == null || autor.getNome().isBlank())
            throw new RequisicaoInvalidaException("O nome do autor é obrigatório.");
        autor.setId(null);
        return autorRepository.save(autor);
    }

    public Autor atualizar(Autor autor) {
        if (autor.getId() == null)
            throw new RequisicaoInvalidaException("O ID é obrigatório para atualização.");
        buscarPorId(autor.getId());
        return autorRepository.save(autor);
    }

    public void deletar(Long id) {
        buscarPorId(id);
        autorRepository.deleteById(id);
    }
}
