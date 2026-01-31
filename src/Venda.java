import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Map;
import java.sql.Date;
import java.text.SimpleDateFormat;

public class Venda {
    private long id;
    private long idCliente;
    private long idFuncionario;
    private float comissao;
    private LocalDateTime dataHora;
    private ArrayList<ItemVenda> itens;

    // --- GETTERS & SETTERS ---
    public void setId(long id){ if(id >= 0) this.id = id; }
    public void setIdCliente(long idCliente){ if(idCliente >= 0) this.idCliente = idCliente; }
    public void setIdFuncionario(long idFuncionario){ if(idFuncionario >= 0) this.idFuncionario = idFuncionario; }
    public void setComissao(float comissao){ if(comissao >= 0) this.comissao = comissao; }
    public void setDataHora(LocalDateTime dataHora){ this.dataHora = dataHora; }
    public void setItens(ArrayList<ItemVenda> itens){ this.itens = itens; }

    public void adicionarItem(ItemVenda item){
        if(this.itens == null) this.itens = new ArrayList<>();
        this.itens.add(item);
    }

    public long getId(){ return this.id; }
    public long getIdFuncionario(){ return this.idFuncionario; }
    public long getIdCliente(){ return this.idCliente; }
    public float getComissao(){ return this.comissao; }
    public LocalDateTime getDataHora(){ return this.dataHora; }
    public ArrayList<ItemVenda> getItens(){ return this.itens; }

    // --- CONSTRUTORES ---
    public Venda(){
        this.id = 0;
        this.idCliente = 0;
        this.idFuncionario = 0;
        this.dataHora = LocalDateTime.now();
        this.comissao = 0;
        this.itens = new ArrayList<>();
    }

    public Venda(long id, long idCliente, long idFuncionario, LocalDateTime dataHora, float comissao){
        this();
        this.setId(id);
        this.setIdCliente(idCliente);
        this.setIdFuncionario(idFuncionario);
        this.setDataHora(dataHora);
        this.setComissao(comissao);
    }

    public static void realizar(Scanner input){
        long idCli, idFunc, idProd, qtd;
        String op;
        boolean adicionando;
        double totalVenda = 0.0, subtotal;
        float comissaoInput;
        Venda venda;
        Cliente cli;
        Funcionario func;
        Produto prod;
        ItemVenda item;

        System.out.println("\n===================================");
        System.out.println("       NOVA VENDA - PDV           ");
        System.out.println("===================================");
        
        System.out.print("Digite o ID do Cliente: ");
        idCli = input.nextLong(); input.nextLine();
        
        cli = ClienteDAO.buscarPorId(idCli);
        if(cli == null){
            System.out.println("Cliente nao encontrado! Operacao cancelada.");
            return;
        }
        System.out.println("Cliente Selecionado: " + cli.getNome() + " (CPF: " + cli.getCpf() + ")");

        System.out.print("\nDigite o ID do Funcionario: ");
        idFunc = input.nextLong(); input.nextLine();

        func = FuncionarioDAO.buscarPorIdInterno(idFunc);
        if(func == null){
            System.out.println("Funcionario nao encontrado! Operacao cancelada.");
            return;
        }
        System.out.println("Vendedor: " + func.getNome());

        System.out.print("Comissao para esta venda (0 a 100%): ");
        comissaoInput = input.nextFloat(); input.nextLine();

        if(comissaoInput < 0 || comissaoInput > 100){
            System.out.println("Porcentagem invalida! Usando padrao de 0%.");
            comissaoInput = 0;
        }

        venda = new Venda();
        venda.setIdCliente(idCli);
        venda.setIdFuncionario(func.getIdFuncionario());
        venda.setDataHora(LocalDateTime.now());
        venda.setComissao(comissaoInput / 100f); 

        adicionando = true;
        while(adicionando){
            System.out.println("\n--- Adicionar Item ---");
            System.out.print("Digite o ID do Produto (-1 para listar todos): ");
            idProd = input.nextLong(); input.nextLine();

            if (idProd == -1) {
                Produto.listarTodos();
                continue; 
            }

            prod = ProdutoDAO.buscarPorId(idProd);

            if(prod != null){
                System.out.printf("Produto: %-20s | Estoque: %d | Preco: R$ %.2f\n", 
                    prod.getNome(), prod.getQtdEstoque(), prod.getPrecoAtual());
                
                System.out.print("Quantidade desejada: ");
                qtd = input.nextLong(); input.nextLine();

                if(qtd > 0 && qtd <= prod.getQtdEstoque()){
                    item = new ItemVenda(0, idProd, 0, qtd, prod.getPrecoAtual());
                    venda.adicionarItem(item);
                    
                    subtotal = qtd * prod.getPrecoAtual();
                    totalVenda += subtotal;
                    
                    System.out.printf("Item adicionado! Subtotal do item: R$ %.2f\n", subtotal);
                    System.out.printf("Total Parcial da Venda: R$ %.2f\n", totalVenda);
                } else {
                    System.out.println("Quantidade invalida ou estoque insuficiente.");
                }
            } else {
                System.out.println("Produto nao encontrado.");
            }

            System.out.print("\nAdicionar outro produto? (S/N): ");
            op = input.nextLine();
            if(!op.equalsIgnoreCase("S")){
                adicionando = false;
            }
        }

        if(!venda.getItens().isEmpty()){
            System.out.println("\n===================================");
            System.out.printf(" TOTAL FINAL DA VENDA: R$ %.2f\n", totalVenda);
            System.out.printf(" COMISSAO A PAGAR: R$ %.2f (%.1f%%)\n", (totalVenda * venda.getComissao()), comissaoInput);
            System.out.println("===================================");
            System.out.print("Confirmar e Salvar Venda? (S/N): ");
            op = input.nextLine();

            if(op.equalsIgnoreCase("S")){
                VendaDAO.salvar(venda);
            } else {
                System.out.println("Venda cancelada pelo usuario.");
            }
        } else {
            System.out.println("Venda vazia cancelada.");
        }
    }

    public static void mediaVenda(Scanner input){
        Map<Date, Double> mapaMedias = VendaDAO.listarMediaVendasPorDia();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        if(mapaMedias.isEmpty()){
            System.out.println("\nNenhum dado de venda disponivel para calculo.");
        } else {
            System.out.println("\n=== Media de Vendas (Ticket Medio) por Dia ===");
            for (Map.Entry<Date, Double> entry : mapaMedias.entrySet()) {
                System.out.printf("Data: %s | Media: R$ %.2f\n", 
                    sdf.format(entry.getKey()), entry.getValue());
            }
            System.out.println("==============================================");
        }
    }

    public static void relatorio(){
        ArrayList<Venda> lista;
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        Cliente c;
        Funcionario f;
        String nomeCli, nomeFunc;

        lista = VendaDAO.listarTodas();

        if(lista.isEmpty()){
            System.out.println("Nenhuma venda registrada.");
        } else {
            System.out.println("\n==========================================================================================");
            System.out.println(String.format("| %-4s | %-16s | %-25s | %-20s | %-8s |", "ID", "DATA", "CLIENTE", "VENDEDOR", "COMISSAO"));
            System.out.println("==========================================================================================");
            
            for(Venda v : lista){
                c = ClienteDAO.buscarPorId(v.getIdCliente());
                f = FuncionarioDAO.buscarPorIdInterno(v.getIdFuncionario());
                
                nomeCli = (c != null) ? c.getNome() : "Desconhecido (" + v.getIdCliente() + ")";
                nomeFunc = (f != null) ? f.getNome() : "Desconhecido (" + v.getIdFuncionario() + ")";

                if(nomeCli.length() > 25) nomeCli = nomeCli.substring(0, 22) + "...";
                if(nomeFunc.length() > 20) nomeFunc = nomeFunc.substring(0, 17) + "...";

                System.out.printf("| %-4d | %-16s | %-25s | %-20s | %-7.1f%% |\n", 
                    v.getId(), v.getDataHora().format(fmt), nomeCli, nomeFunc, v.getComissao() * 100);
            }
            System.out.println("==========================================================================================");
        }
    }

    public static void relatorioEspecifico(Scanner input){
        long id;
        Venda v;
        Cliente c;
        Funcionario f;
        Produto p;
        double total = 0, subtotal;
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

        System.out.print("\nDigite o ID da Venda para detalhes: ");
        id = input.nextLong(); input.nextLine();

        v = VendaDAO.buscarPorId(id);

        if(v != null){
            c = ClienteDAO.buscarPorId(v.getIdCliente());
            f = FuncionarioDAO.buscarPorIdInterno(v.getIdFuncionario());

            System.out.println("\n-------------------------------------------");
            System.out.println("           CUPOM NAO FISCAL             ");
            System.out.println("-------------------------------------------");
            System.out.println("Venda N: " + v.getId());
            System.out.println("Data:     " + v.getDataHora().format(fmt));
            System.out.println("Cliente:  " + ((c!=null) ? c.getNome() : "ID "+v.getIdCliente()));
            System.out.println("Vendedor: " + ((f!=null) ? f.getNome() : "ID "+v.getIdFuncionario()));
            System.out.printf("Comissao: %.1f%%\n", v.getComissao() * 100);
            System.out.println("-------------------------------------------");
            System.out.println("ITENS:");
            
            for(ItemVenda item : v.getItens()){
                p = ProdutoDAO.buscarPorId(item.getIdProduto());
                String nomeProd = (p != null) ? p.getNome() : "Produto ID " + item.getIdProduto();
                
                subtotal = item.getQtd() * item.getPrecoPraticado();
                total += subtotal;

                System.out.printf("%-25s \n   %d x R$ %.2f = R$ %.2f\n", 
                    nomeProd, item.getQtd(), item.getPrecoPraticado(), subtotal);
            }
            System.out.println("-------------------------------------------");
            System.out.printf("TOTAL A PAGAR:              R$ %.2f\n", total);
            System.out.println("-------------------------------------------");

        } else {
            System.out.println("Venda nao encontrada.");
        }
    }

    public static void vendasMesAno(Scanner input){
        int mes, ano;
        ArrayList<Venda> lista;
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

        System.out.print("\nDigite o Mes (1-12): ");
        mes = input.nextInt();
        System.out.print("Digite o Ano (ex: 2023): ");
        ano = input.nextInt(); input.nextLine();

        lista = VendaDAO.buscarPorMesAno(mes, ano);

        if(lista.isEmpty()){
            System.out.println("Nenhuma venda encontrada neste periodo.");
        } else {
            System.out.println("\n--- Vendas em " + mes + "/" + ano + " ---");
            for(Venda v : lista){
                System.out.println("ID: " + v.getId() + " | Data: " + v.getDataHora().format(fmt));
            }
            System.out.println("Total de registros: " + lista.size());
        }
    }

    public static void idadeDias(){
        ArrayList<Venda> lista;
        long dias;
        lista = VendaDAO.listarTodas();

        System.out.println("\n--- Idade das Vendas ---");
        for(Venda v : lista){
            dias = ChronoUnit.DAYS.between(v.getDataHora(), LocalDateTime.now());
            System.out.printf("Venda #%d: realizada ha %d dias.\n", v.getId(), dias);
        }
    }

    public static void diaSemanaMaisVendas(){
        String dia = VendaDAO.buscarDiaComMaisVendas();
        System.out.println("\nDia da semana com maior movimento: " + (dia != null ? dia : "Sem dados"));
    }

    public static void relatorioMaiorVenda(){
        Venda v = VendaDAO.buscarMaiorVenda();
        if(v != null){
            System.out.println("\n--- MAIOR VENDA JA REGISTRADA ---");
            System.out.println("ID: " + v.getId() + " | Data: " + v.getDataHora());
        } else {
            System.out.println("Nenhuma venda registrada para ranking.");
        }
    }
}