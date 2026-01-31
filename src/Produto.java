import java.util.Scanner;
import java.util.ArrayList;

public class Produto {
    //Atributos
    private long id;
    private String nome;
    private long qtdEstoque;
    private double precoAtual;

    //Construtores
    public Produto(){
        this.id = 0;
        this.nome = "Produto Generico";
        this.qtdEstoque = 0;
        this.precoAtual = 0.0;
    }

    public Produto(long id, long qtdEstoque, String nome, double preco){
        this.setId(id);
        this.setNome(nome);
        this.setPrecoAtual(preco);
        this.setQtdEstoque(qtdEstoque);
    }

    // Métodos do Menu

    public static void adicionar(Scanner input){
        String nome;
        long qtd;
        double preco;
        Produto p;

        System.out.print("\nNome do Produto: ");
        nome = input.nextLine();

        System.out.print("Quantidade em Estoque: ");
        qtd = input.nextLong();

        System.out.print("Preço Atual: ");
        preco = input.nextDouble(); input.nextLine();

        p = new Produto(0, qtd, nome, preco);
        
        if(p.getPrecoAtual() < 0 || p.getQtdEstoque() < 0){
             System.out.println("Dados inválidos! Preço ou Estoque negativos.");
             return;
        }

        ProdutoDAO.salvar(p);
    }

    public static void listarTodos(){
        ArrayList<Produto> lista;

        lista = ProdutoDAO.listarTodos();

        if(lista.isEmpty()){
            System.out.println("\nNenhum produto cadastrado.");
        } else {
            System.out.println("\n--- Lista de Produtos ---");
            for(Produto p : lista){
                System.out.println("ID: " + p.getId() + " | Nome: " + p.getNome() + 
                                   " | Qtd: " + p.getQtdEstoque() + " | R$ " + p.getPrecoAtual());
            }
        }
    }

    public static void buscarId(Scanner input){
        long id;
        Produto p;

        System.out.print("\nID do Produto: ");
        id = input.nextLong(); input.nextLine();

        p = ProdutoDAO.buscarPorId(id);

        if(p != null){
            System.out.println("\nProduto Encontrado:");
            System.out.println("Nome: " + p.getNome());
            System.out.println("Estoque: " + p.getQtdEstoque());
            System.out.println("Preço: R$ " + p.getPrecoAtual());
        } else {
            System.out.println("Produto não encontrado.");
        }
    }

    public static void buscarNome(Scanner input){
        String termo;
        ArrayList<Produto> lista;

        System.out.print("\nDigite parte do nome do produto: ");
        termo = input.nextLine();

        lista = ProdutoDAO.buscarPorNome(termo);

        if(lista.isEmpty()){
            System.out.println("Nenhum produto encontrado com esse nome.");
        } else {
            System.out.println("\n--- Produtos Encontrados ---");
            for(Produto p : lista){
                System.out.println("ID: " + p.getId() + " | " + p.getNome() + " | R$ " + p.getPrecoAtual());
            }
        }
    }

    public static void modificar(Scanner input){
        long id;
        String novoNome;
        long novoQtd;
        double novoPreco;
        Produto p;

        System.out.print("\nID do Produto para editar: ");
        id = input.nextLong(); input.nextLine();

        p = ProdutoDAO.buscarPorId(id);

        if(p != null){
            System.out.println("Editando: " + p.getNome());

            System.out.print("Novo Nome (ENTER para manter): ");
            novoNome = input.nextLine();

            System.out.print("Novo Estoque (-1 para manter): ");
            novoQtd = input.nextLong();

            System.out.print("Novo Preço (-1 para manter): ");
            novoPreco = input.nextDouble(); input.nextLine();

            if(!novoNome.isEmpty()) p.setNome(novoNome);
            if(novoQtd >= 0) p.setQtdEstoque(novoQtd);
            if(novoPreco >= 0) p.setPrecoAtual(novoPreco);

            ProdutoDAO.modificar(p);
        } else {
            System.out.println("Produto não encontrado.");
        }
    }

    public static void deletar(Scanner input){
        long id;
        int op;

        System.out.print("\nID do Produto para deletar: ");
        id = input.nextLong(); input.nextLine();

        System.out.print("Confirmar deleção? (1-Sim / 0-Não): ");
        op = input.nextInt(); 
        input.nextLine();

        if(op == 1){
            ProdutoDAO.deletar(id);
        } else {
            System.out.println("Operação cancelada.");
        }
    }

    // --- MÉTODOS NOVOS ---

    public static void maisCaroMedia(){
        ArrayList<Produto> lista;

        lista = ProdutoDAO.listarMaisCarosQueMedia();

        if(lista.isEmpty()){
            System.out.println("\nNenhum produto acima da média de preço.");
        } else {
            System.out.println("\n--- Produtos Mais Caros que a Média ---");
            for(Produto p : lista){
                System.out.println("ID: " + p.getId() + " | Nome: " + p.getNome() + 
                                   " | R$ " + p.getPrecoAtual());
            }
        }
    }

    public static void menosVinteEstoque(){
        ArrayList<Produto> lista;

        lista = ProdutoDAO.listarEstoqueBaixo();

        if(lista.isEmpty()){
            System.out.println("\nNenhum produto com estoque crítico (<20).");
        } else {
            System.out.println("\n--- Produtos com Estoque Baixo (<20) ---");
            for(Produto p : lista){
                System.out.println("ID: " + p.getId() + " | Nome: " + p.getNome() + 
                                   " | Qtd: " + p.getQtdEstoque());
            }
        }
    }

    //Setters
    public void setId(long id){
        if(id >= 0) this.id = id;
    }

    public void setNome(String nome){
        if(nome != null && !nome.isEmpty() && nome.length() <= 1024){
            this.nome = nome;
        }
    }

    public void setQtdEstoque(long qtdEstoque){
        if(qtdEstoque >= 0) this.qtdEstoque = qtdEstoque;
    }

    public void setPrecoAtual(double precoAtual){
        if(precoAtual >= 0.0) this.precoAtual = precoAtual;
    }

    //Getters
    public long getId(){ return this.id; }
    public String getNome(){ return this.nome; }
    public long getQtdEstoque(){ return this.qtdEstoque; }
    public double getPrecoAtual(){ return this.precoAtual; }
}