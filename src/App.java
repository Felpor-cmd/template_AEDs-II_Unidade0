import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.nio.charset.Charset;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class App {

    /** Quantidade máxima de produtos que podem ser armazenados no vetor */
    static final int MAX_NOVOS_PRODUTOS = 10;

    /** Nome do arquivo de dados. O arquivo deve estar localizado na raiz do projeto */
    static String nomeArquivoDados;
    
    /** Scanner para leitura de dados do teclado */
    static Scanner teclado;

    /** Vetor de produtos cadastrados */
    static Produto[] produtosCadastrados;

    /** Quantidade de produtos cadastrados atualmente no vetor */
    static int quantosProdutos = 0;

    /** Gera um efeito de pausa na CLI. Espera por um enter para continuar */
    static void pausa() {
        System.out.println("Digite enter para continuar...");
        teclado.nextLine();
    }

    /** Cabeçalho principal da CLI do sistema */
    static void cabecalho() {
        System.out.println("AEDs II COMÉRCIO DE COISINHAS");
        System.out.println("=============================");
    }
    
    /** Imprime o menu principal, lê a opção do usuário e a retorna (int).
     * @return Um inteiro com a opção do usuário.
    */
    static int menu() {
        cabecalho();
        System.out.println("1 - Listar todos os produtos");
        System.out.println("2 - Procurar e imprimir os dados de um produto");
        System.out.println("3 - Cadastrar novo produto");
        System.out.println("0 - Sair");
        System.out.print("Digite sua opção: ");
        return Integer.parseInt(teclado.nextLine());
    }
    
    /**
     * Lê os dados de um arquivo-texto e retorna um vetor de produtos. Arquivo-texto no formato
     * N (quantidade de produtos) <br/>
     * tipo;descrição;preçoDeCusto;margemDeLucro;[dataDeValidade] <br/>
     * Deve haver uma linha para cada um dos produtos. Retorna um vetor vazio em caso de problemas com o arquivo.
     * @param nomeArquivoDados Nome do arquivo de dados a ser aberto.
     * @return Um vetor com os produtos carregados, ou vazio em caso de problemas de leitura.
     */
    static Produto[] lerProdutos(String nomeArquivoDados) {
        quantosProdutos = 0;

        try (Scanner scanner = new Scanner(new File(nomeArquivoDados), Charset.forName("UTF-8"))) {
            
            int quantidadeLinhas = Integer.parseInt(scanner.nextLine());
            Produto[] produtos = new Produto[quantidadeLinhas];
            
            for (int i = 0; i < quantidadeLinhas; i++){
                String linha = scanner.nextLine();
                produtos[i] = Produto.criarDoTexto(linha);
                quantosProdutos++;
            }

            return produtos;
            
        } catch (Exception e) {
            System.out.println("Erro ao ler o arquivo: " + e.getMessage());
            quantosProdutos = 0;
            return new Produto[0];
        }
    }
    
    /** Localiza um produto no vetor de produtos cadastrados, a partir do nome de produto informado pelo usuário, e imprime seus dados. 
     *  A busca não é sensível ao caso. Em caso de não encontrar o produto, imprime uma mensagem padrão */
    static void localizarProdutos() {
        System.out.println("Digite a descrição do produto a ser localizado: ");
        String nome = teclado.nextLine();
        boolean encontrado = false;

        for (int i = 0; i < quantosProdutos; i++) {
            if (produtosCadastrados[i].getDescricao().equalsIgnoreCase(nome)) {
                System.out.println("Produto encontrado:");
                System.out.println(produtosCadastrados[i].toString());
                encontrado = true;
                return;
            }
        }
        if (!encontrado) {
            System.out.println("Produto não encontrado.");
        }
    }
    
    /**
     * Salva os dados dos produtos cadastrados no arquivo csv informado. Sobrescreve todo o conteúdo do arquivo.
     * @param nomeArquivo Nome do arquivo a ser gravado.
     */
    public static void salvarProdutos(String nomeArquivo) {
        try (PrintWriter escritor = new PrintWriter(nomeArquivo, "UTF-8")) {
            escritor.println(quantosProdutos);
            for (int i = 0; i < quantosProdutos; i++) {
                escritor.println(produtosCadastrados[i].gerarDadosTexto());
            }
        } catch (Exception e) {
            System.out.println("Erro ao salvar o arquivo: " + e.getMessage());
        }
    }
    
    /** Lista todos os produtos cadastrados, numerados, um por linha */
    static void listarTodosOsProdutos() {
    	System.out.println("Produtos cadastrados:");
        System.out.println("=====================");
        for (int i = 0; i < quantosProdutos; i++) {
            System.out.println((i + 1) + " - " + produtosCadastrados[i].toString());
        }
    }
    
    /**
     * Rotina para cadastro de um novo produto: pergunta ao usuário o tipo do produto, lê os dados correspondentes,
     * cria o objeto adequado de acordo com o tipo, inclui o produto no vetor.
     */
    static void cadastrarProduto() {
        teclado = new Scanner(System.in, Charset.forName("UTF-8"));
        System.out.println("Cadastro de novo produto");
        System.out.println("=======================");
        System.out.println("Digite o tipo do produto (1 - não perecível, 2 - perecível): ");
        int tipo = Integer.parseInt(teclado.nextLine());

        if (tipo == 1) {
            System.out.println("Digite a descrição do produto: ");
            String descricao = teclado.nextLine();
            System.out.println("Digite o preço de custo do produto: ");
            double precoCusto = Double.parseDouble(teclado.nextLine());
            ProdutoNaoPerecivel produto = new ProdutoNaoPerecivel(descricao, precoCusto);
            produtosCadastrados[quantosProdutos] = produto;
            quantosProdutos++;
        } else if (tipo == 2) {
            System.out.println("Digite a descrição do produto: ");
            String descricao = teclado.nextLine();
            System.out.println("Digite o preço de custo do produto: ");
            double precoCusto = Double.parseDouble(teclado.nextLine());
            System.out.println("Digite a data de validade do produto (dd/MM/yyyy): ");
            String dataValidadeStr = teclado.nextLine();
            LocalDate dataValidade = LocalDate.parse(dataValidadeStr, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            ProdutoPerecivel produto = new ProdutoPerecivel(descricao, precoCusto, dataValidade);
            produtosCadastrados[quantosProdutos] = produto;
            quantosProdutos++;
        } else {
            System.out.println("Tipo de produto inválido.");
        }  
    }  
    
	public static void main(String[] args) {
		teclado = new Scanner(System.in, Charset.forName("UTF-8"));
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
        }while(opcao != 0);       

        salvarProdutos(nomeArquivoDados);
        teclado.close();    
    }
}
