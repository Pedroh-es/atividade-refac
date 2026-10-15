package com.poo.atividade_refac.controllers;

import com.poo.atividade_refac.contracts.LivroService;
import com.poo.atividade_refac.entities.Livro;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/livros")
public class LivroController {

    private final LivroService livroService;

    public LivroController(LivroService livroService) {
        this.livroService = livroService;
    }

    @GetMapping
    public List<Livro> listar() {
        return livroService.listar();
    }

    @PostMapping
    public Livro cadastrar(@RequestBody Livro livro) {
        return livroService.cadastrar(livro);
    }

    @PutMapping("/{id}")
    public Livro atualizar(@PathVariable Long id, @RequestBody Livro livro) {
        return livroService.atualizar(id, livro);
    }

    @DeleteMapping("/{id}")
    public void remover(@PathVariable Long id) {
        livroService.remover(id);
    }
}
