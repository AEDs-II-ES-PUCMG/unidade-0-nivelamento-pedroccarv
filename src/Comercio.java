import java.io.BufferedReader;
import java.io.File;


import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class Comercio {
    /** Para inclusão de novos produtos no vetor */
    static final int MAX_NOVOS_PRODUTOS = 10;
    /** Nome do arquivo de dados. O arquivo deve estar localizado na raiz do projeto */
    static String nomeArquivoDados;
    /** Scanner para leitura do teclado */
    static Scanner teclado;
    /** Vetor de produtos cadastrados. Sempre terá espaço para 10 novos produtos a cada execução */
    static Produto[] produtosCadastrados;
    /** Quantidade produtos cadastrados atualmente no vetor */
    static int quantosProdutos;
    /** Gera um efeito de pausa na CLI. Espera por um enter para continuar */
    static void pausa(){
        System.out.println("Digite enter para continuar...");
        teclado.nextLine();
    }
    /** Cabeçalho principal da CLI do sistema */
    static void cabecalho(){
        System.out.println("AEDII COMÉRCIO DE COISINHAS");
        System.out.println("===========================");
    }
    /** Imprime o menu principal, lê a opção do usuário e a retorna (int).
    * Perceba que poderia haver uma melhor modularização com a criação de uma classe Menu.
    * @return Um inteiro com a opção do usuário.
    */
    static int menu(){
        cabecalho();
        System.out.println("1 - Listar todos os produtos");
        System.out.println("2 - Procurar e listar um produto");
        System.out.println("3 - Cadastrar novo produto");
        System.out.println("0 - Sair");
        System.out.print("Digite sua opção: ");
        return Integer.parseInt(teclado.nextLine());
    }

    /**
    * Lê os dados de um arquivo texto e retorna um vetor de produtos. Arquivo no formato
    * N (quantiade de produtos) <br/>
    * tipo; descrição;preçoDeCusto;margemDeLucro;[dataDeValidade] <br/>
    * Deve haver uma linha para cada um dos produtos. Retorna um vetor vazio em caso de problemas com o arquivo.
    * @param nomeArquivoDados Nome do arquivo de dados a ser aberto.
    * @return Um vetor com os produtos carregados, ou vazio em caso de problemas de leitura.
    */
    static Produto[] lerProdutos(String nomeArquivoDados) {
        Scanner arquivo = null;
        int i, numProdutos;
        String linha;
        Produto produto;
        Produto[] produtosCadastrados = null;

        try {
            arquivo = new Scanner(new File(nomeArquivoDados), Charset.forName("UTF-8"));
            numProdutos = Integer.parseInt(arquivo.nextLine());
            produtosCadastrados = new Produto[numProdutos + MAX_NOVOS_PRODUTOS];
            for (i=0; i<numProdutos && i < numProdutos + MAX_NOVOS_PRODUTOS;i++){
                linha = arquivo.nextLine();
                produto = Produto.criarDoTexto(linha);
                produtosCadastrados[i] = produto;
            }
            quantosProdutos = numProdutos;
        } catch (IOException e) {
            produtosCadastrados = new Produto[0];
            quantosProdutos = 0;
        } finally {
            if (arquivo != null) arquivo.close();
        }
        return produtosCadastrados;
    }
    static void listarTodosOsProdutos(){
        cabecalho();
        for (int i = 0; i < quantosProdutos; i++) {
            System.out.println((i+1) + " - " + produtosCadastrados[i].toString());
        }
    }
    static void localizarProdutos(){
        String descricao;
        ProdutoNaoPerecivel produtoALocalizar;
        Boolean localizado = false;
        Produto produto = null;

        cabecalho();
        System.out.println("Informe a descricao do produto desejado");
        descricao = teclado.nextLine();
        produtoALocalizar = new ProdutoNaoPerecivel(descricao, 0.01, 0.01);
        for (int i = 0; i < quantosProdutos && !localizado; i++) {
            if (produtosCadastrados[i].equals(produtoALocalizar)) {
                localizado = true;
                produto = produtosCadastrados[i];
            }
        }
        if (localizado) {
            System.out.println(produto.toString());
        } else {
            System.out.println("Produto nao localizado");
        }
    }
    static void cadastrarProduto(){
        cabecalho();
        System.out.println("Cadastro de novo produto");
        System.out.println("1 - Produto Nao Perecivel");
        System.out.println("2 - Produto Perecivel");
        System.out.print("Digite o tipo: ");
        int tipo = Integer.parseInt(teclado.nextLine());
        System.out.print("Descricao: ");
        String descricao = teclado.nextLine();
        System.out.print("Preco de Custo: ");
        double precoCusto = Double.parseDouble(teclado.nextLine());
        System.out.print("Margem de Lucro: ");
        double margemLucro = Double.parseDouble(teclado.nextLine());
        Produto novoProduto = null;
        if (tipo == 1) {
            novoProduto = new ProdutoNaoPerecivel(descricao, precoCusto, margemLucro);
        } else if (tipo == 2) {
            System.out.print("Data de Validade (dd/MM/yyyy): ");
            String dataStr = teclado.nextLine();
            LocalDate dataValidade = LocalDate.parse(dataStr, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            novoProduto = new ProdutoPerecivel(descricao, precoCusto, margemLucro, dataValidade);
        }
        if (novoProduto != null && quantosProdutos < produtosCadastrados.length) {
            produtosCadastrados[quantosProdutos] = novoProduto;
            quantosProdutos++;
            System.out.println("Produto cadastrado com sucesso!");
        } else {
            System.out.println("Erro ao cadastrar produto.");
        }
    }
    public static void salvarProdutos(String nomeArquivo){
        try (PrintWriter writer = new PrintWriter(new FileWriter(nomeArquivo, Charset.forName("UTF-8")))) {
            writer.println(quantosProdutos);
            for (int i = 0; i < quantosProdutos; i++) {
                writer.println(produtosCadastrados[i].gerarDadosTexto());
            }
        } catch (IOException e) {
            System.out.println("Erro ao salvar produtos: " + e.getMessage());
        }
    }
    public static void main(String[] args) throws Exception {
        teclado = new Scanner(System.in, Charset.forName("ISO-8859-2"));
        nomeArquivoDados = "dadosProdutos.csv";
        produtosCadastrados = lerProdutos(nomeArquivoDados);
        int opcao = -1;
        do{
            opcao = menu();
            switch (opcao) {
                case 1 -> listarTodosOsProdutos();
                case 2 -> localizarProdutos();
                case 3 -> cadastrarProduto();
            }
            pausa();
        }while(opcao !=0);
        salvarProdutos(nomeArquivoDados);
        teclado.close();
    }
}
