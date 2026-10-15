package com.poo.atividade_refac.services;

import com.poo.atividade_refac.contracts.LivroService;
import com.poo.atividade_refac.entities.Livro;
import com.poo.atividade_refac.repositories.LivroRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LivroServiceImpl implements LivroService {

    private final LivroRepository livroRepository;
    private final EmailService emailService;

    public LivroServiceImpl(LivroRepository livroRepository, EmailService emailService) {
        this.livroRepository = livroRepository;
        this.emailService = emailService;
    }

    @Override
    public List<Livro> listar() {
        System.out.println("Listando livros");
        return livroRepository.findAll();
    }

    @Override
    public Livro cadastrar(Livro livro) {
        if (livro.getTitulo() == null || livro.getTitulo().isBlank()) {
            throw new RuntimeException("Título obrigatório");
        }
        if (livro.getPreco() == null || livro.getPreco() <= 0) {
            throw new RuntimeException("Preço inválido");
        }

        // Aplicação do desconto
        if ("ROMANCE".equalsIgnoreCase(livro.getCategoria())) {
            livro.setPreco(livro.getPreco() * 0.9);
        } else if ("TECNOLOGIA".equalsIgnoreCase(livro.getCategoria())) {
            livro.setPreco(livro.getPreco() * 0.8);
        } else if ("INFANTIL".equalsIgnoreCase(livro.getCategoria())) {
            livro.setPreco(livro.getPreco() * 0.7);
        } else if ("HQ".equalsIgnoreCase(livro.getCategoria())) {
            livro.setPreco(livro.getPreco() * 0.6);
        }

        System.out.println("Salvando livro: " + livro.getTitulo());
        Livro livroSalvo = livroRepository.save(livro);

        System.out.println("Quantidade de livros: " + livroRepository.count());
        emailService.enviarEmailPromocional();

        return livroSalvo;
    }

    @Override
    public Livro atualizar(Long id, Livro livro) {
        Livro livroBanco = livroRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Livro não encontrado"));

        livroBanco.setTitulo(livro.getTitulo());
        livroBanco.setAutor(livro.getAutor());
        livroBanco.setPreco(livro.getPreco());
        livroBanco.setCategoria(livro.getCategoria());

        return livroRepository.save(livroBanco);
    }

    @Override
    public void remover(Long id) {
        Livro livro = livroRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Livro não encontrado"));

        if ("RARO".equalsIgnoreCase(livro.getCategoria())) {
            throw new RuntimeException("Livros raros não podem ser removidos");
        }

        livroRepository.delete(livro);
    }
}
