package br.com.biblioteca2026.controller;

import br.com.biblioteca2026.model.Emprestimo;
import br.com.biblioteca2026.services.EmprestimoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/emprestimos")
public class EmprestimoController {

    private final EmprestimoService emprestimoService;

    @Autowired
    public EmprestimoController(EmprestimoService emprestimoService) {
        this.emprestimoService = emprestimoService;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Emprestimo>> buscarTodos() {
        return ResponseEntity.ok(emprestimoService.buscarTodos());
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Emprestimo> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(emprestimoService.buscarPorId(id));
    }

    @GetMapping(value = "/usuario/{usuarioId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Emprestimo>> buscarPorUsuario(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(emprestimoService.buscarPorUsuario(usuarioId));
    }

    @GetMapping(value = "/usuario/{usuarioId}/ativos", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Emprestimo>> buscarAtivosDoUsuario(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(emprestimoService.buscarAtivosDoUsuario(usuarioId));
    }

    /**
     * Realiza um novo empréstimo.
     * Body esperado:
     * {
     *   "usuarioId": 1,
     *   "livroId": 2,
     *   "dataPrevistaDevolucao": "2026-07-15"
     * }
     */
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Emprestimo> realizarEmprestimo(@RequestBody Map<String, String> body) {
        Long usuarioId = Long.parseLong(body.get("usuarioId"));
        Long livroId = Long.parseLong(body.get("livroId"));
        LocalDate dataPrevistaDevolucao = LocalDate.parse(body.get("dataPrevistaDevolucao"));

        Emprestimo emprestimo = emprestimoService.realizarEmprestimo(usuarioId, livroId, dataPrevistaDevolucao);
        return ResponseEntity.status(201).body(emprestimo);
    }

    /**
     * Realiza a devolução de um empréstimo ativo.
     * PATCH /emprestimos/{id}/devolver
     */
    @PatchMapping(value = "/{id}/devolver", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Emprestimo> realizarDevolucao(@PathVariable Long id) {
        return ResponseEntity.ok(emprestimoService.realizarDevolucao(id));
    }
}
