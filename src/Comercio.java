import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;
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
        Produto[] produtosCadastrados = new Produto[MAX_NOVOS_PRODUTOS];

        try {
            arquivo = new Scanner(new File(nomeArquivoDados), Charset.forName("UTF-8"));
            numProdutos = Integer.parseInt(arquivo.nextLine());
            for (i=0; i<numProdutos && i < MAX_NOVOS_PRODUTOS;i++){
                linha = arquivo.nextLine();
                produto = Produto.criarDoTexto(linha);
                produtosCadastrados[i] = produto;
            }
        } catch (IOException e) {
            produtosCadastrados = null;
        } finally {
            arquivo.close();
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
        produtoALocalizar = new ProdutoNaoPerecivel(descricao, 0.01);
        for (int i = 0; i < quantosProdutos && !localizado; i++) {
            if (produtosCadastrados[i].equals(produtoALocalizar)) {
                localizado = true;
                produto = produtosCadastrados[i];
            }
        }
        if (!localizado) {
            System.out.println("Produto nao localizado");
        }
    }

}
