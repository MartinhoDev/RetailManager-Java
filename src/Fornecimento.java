import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;

public class Fornecimento {
    //Atributos
    private long id;
    private long idProduto;
    private long idFornecedor;
    private long qtd;
    private LocalDateTime dataHora;
    private double precoCusto;

    //Setters
    public void setId(long id){ if(id >= 0) this.id = id; }
    public void setIdProduto(long idProduto){ if(idProduto >= 0) this.idProduto = idProduto; }
    public void setIdFornecedor(long id){ if(id >= 0) this.idFornecedor = id; }
    public void setQtd(long qtd){ if(qtd >= 0) this.qtd = qtd; }
    public void setDataHora(LocalDateTime dataHora){ if(dataHora != null) this.dataHora = dataHora; }
    public void setPrecoCusto(double precoCusto){ if(precoCusto >= 0.0) this.precoCusto = precoCusto; }

    //Getters
    public long getId(){ return this.id; }
    public long getIdProduto(){ return this.idProduto; }
    public long getIdFornecedor(){ return this.idFornecedor; }
    public long getQtd(){ return this.qtd; }
    public LocalDateTime getDataHora(){ return this.dataHora; }
    public double getPrecoCusto(){ return this.precoCusto; }

    //Construtores
    public Fornecimento(){
        this.id = 0;
        this.idFornecedor = 0;
        this.idProduto = 0;
        this.qtd = 0;
        this.precoCusto = 0.0;
        this.dataHora = null;
    }

    public Fornecimento(long id, long idFornecedor, long idProduto, long qtd, double precoCusto, LocalDateTime dataHora){
        this.setId(id);
        this.setIdFornecedor(idFornecedor);
        this.setIdProduto(idProduto);
        this.setQtd(qtd);
        this.setPrecoCusto(precoCusto);
        this.setDataHora(dataHora);
    }

    public static void adicionar(Scanner input){
        long idForn, idProd, qtd;
        double custo;
        String op;
        Fornecedor forn;
        Produto prod;
        Fornecimento f;

        System.out.println("\n--- Novo Registro de Fornecimento (Entrada de Estoque) ---");

        System.out.print("ID do Fornecedor: ");
        idForn = input.nextLong(); input.nextLine();
        
        forn = FornecedorDAO.buscarPorId(idForn);
        if(forn == null){
            System.out.println("Fornecedor não encontrado! Operação cancelada.");
            return;
        }
        System.out.println("Fornecedor: " + forn.getNomeFantasia() + " (CNPJ: " + forn.getCnpj() + ")");

        System.out.print("\nID do Produto: ");
        idProd = input.nextLong(); input.nextLine();

        prod = ProdutoDAO.buscarPorId(idProd);
        if(prod == null){
            System.out.println("Produto não encontrado! Operação cancelada.");
            return;
        }
        System.out.println("Produto: " + prod.getNome() + " | Estoque Atual: " + prod.getQtdEstoque());

        System.out.print("\nQuantidade recebida: ");
        qtd = input.nextLong();

        System.out.print("Preço de Custo Total (da nota): ");
        custo = input.nextDouble(); input.nextLine();

        if(qtd <= 0 || custo < 0){
            System.out.println("Valores inválidos (Quantidade ou Preço).");
            return;
        }

        System.out.println("\nResumo da Entrada:");
        System.out.println("Produto: " + prod.getNome());
        System.out.println("Qtd a adicionar: " + qtd);
        System.out.println("Novo Estoque será: " + (prod.getQtdEstoque() + qtd));
        
        System.out.print("Confirmar entrada? (S/N): ");
        op = input.nextLine();

        if(op.equalsIgnoreCase("S")){
            f = new Fornecimento(0, idForn, idProd, qtd, custo, LocalDateTime.now());
            FornecimentoDAO.salvar(f);
        } else {
            System.out.println("Operação cancelada.");
        }
    }

    public static void listar(){
        ArrayList<Fornecimento> lista;
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        Fornecedor forn;
        Produto prod;
        String nomeForn, nomeProd;

        lista = FornecimentoDAO.listarTodos();

        if(lista.isEmpty()){
            System.out.println("\nNenhum fornecimento registrado.");
        } else {
            System.out.println("\n======================================================================================");
            System.out.println(String.format("| %-4s | %-16s | %-20s | %-20s | %-8s |", "ID", "DATA", "FORNECEDOR", "PRODUTO", "QTD"));
            System.out.println("======================================================================================");

            for(Fornecimento f : lista){
                forn = FornecedorDAO.buscarPorId(f.getIdFornecedor());
                prod = ProdutoDAO.buscarPorId(f.getIdProduto());

                nomeForn = (forn != null) ? forn.getNomeFantasia() : "Desc. (" + f.getIdFornecedor() + ")";
                nomeProd = (prod != null) ? prod.getNome() : "Desc. (" + f.getIdProduto() + ")";


                if(nomeForn.length() > 20) nomeForn = nomeForn.substring(0, 17) + "...";
                if(nomeProd.length() > 20) nomeProd = nomeProd.substring(0, 17) + "...";

                System.out.printf("| %-4d | %-16s | %-20s | %-20s | %-8d |\n", 
                    f.getId(), f.getDataHora().format(fmt), nomeForn, nomeProd, f.getQtd());
            }
            System.out.println("======================================================================================");
        }
    }

    public static void listarEspecifico(Scanner input){
        long id;
        Fornecimento f;
        Fornecedor forn;
        Produto prod;
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");


        System.out.print("\nDigite o ID do Fornecimento: ");
        id = input.nextLong(); input.nextLine();

        f = FornecimentoDAO.buscarPorId(id);

        if(f != null){
            forn = FornecedorDAO.buscarPorId(f.getIdFornecedor());
            prod = ProdutoDAO.buscarPorId(f.getIdProduto());

            System.out.println("\n--- Detalhes do Fornecimento #" + f.getId() + " ---");
            System.out.println("Data:       " + f.getDataHora().format(fmt));
            System.out.println("Fornecedor: " + ((forn != null) ? forn.getNomeFantasia() : "ID " + f.getIdFornecedor()));
            System.out.println("Produto:    " + ((prod != null) ? prod.getNome() : "ID " + f.getIdProduto()));
            System.out.println("Quantidade: " + f.getQtd());
            System.out.printf("Custo Total: R$ %.2f\n", f.getPrecoCusto());
        } else {
            System.out.println("Fornecimento não encontrado.");
        }
    }
}