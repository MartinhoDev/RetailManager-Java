import java.util.Scanner;
import java.util.ArrayList;

public class Fornecedor {
    //Atributos
    private long id;
    private String cnpj;
    private String nomeFantasia;
    private String telefoneTelegram;
    private String telefoneContato;

    //Métodos do Menu

    public static void adicionar(Scanner input){
        String nome, cnpj, telContato, telTelegram;
        Fornecedor f;

        System.out.print("\nNome Fantasia: ");
        nome = input.nextLine();

        System.out.print("CNPJ (14 dígitos, somente números): ");
        cnpj = input.nextLine();

        System.out.print("Telefone Contato: ");
        telContato = input.nextLine();

        System.out.print("Telefone Telegram (ou ENTER para vazio): ");
        telTelegram = input.nextLine();

        if(!Fornecedor.validaCnpj(cnpj)){
            System.out.println("CNPJ Inválido! Operação cancelada.");
            return;
        }

        f = new Fornecedor(0, cnpj, nome, telTelegram, telContato);
        FornecedorDAO.salvar(f);
    }

    public static void listarTodos(){
        ArrayList<Fornecedor> lista;

        lista = FornecedorDAO.listarTodos();

        if(lista.isEmpty()){
            System.out.println("\nNenhum fornecedor cadastrado.");
        } else {
            System.out.println("\n--- Lista de Fornecedores ---");
            for(Fornecedor f : lista){
                System.out.println("ID: " + f.getId() + " | " + f.getNomeFantasia() + 
                                   " | CNPJ: " + f.getCnpj() + " | Tel: " + f.getTelefoneContato());
            }
        }
    }

    public static void buscarId(Scanner input){
        long id;
        Fornecedor f;

        System.out.print("\nID do Fornecedor: ");
        id = input.nextLong(); input.nextLine();

        f = FornecedorDAO.buscarPorId(id);

        if(f != null){
            System.out.println("\nFornecedor Encontrado:");
            System.out.println("Nome: " + f.getNomeFantasia());
            System.out.println("CNPJ: " + f.getCnpj());
            System.out.println("Contato: " + f.getTelefoneContato());
            System.out.println("Telegram: " + f.getTelefoneTelegram());
        } else {
            System.out.println("Fornecedor não encontrado.");
        }
    }

    public static void buscarCnpj(Scanner input){
        String cnpj;
        Fornecedor f;

        System.out.print("\nCNPJ para buscar: ");
        cnpj = input.nextLine();

        f = FornecedorDAO.buscarPorCnpj(cnpj);

        if(f != null){
            System.out.println("\nFornecedor Encontrado:");
            System.out.println("ID: " + f.getId());
            System.out.println("Nome: " + f.getNomeFantasia());
        } else {
            System.out.println("Fornecedor não encontrado.");
        }
    }

    public static void modificar(Scanner input){
        long id;
        String novoNome, novoCnpj, novoTel, novoTelTelegram;
        Fornecedor f;

        System.out.print("\nID do Fornecedor para editar: ");
        id = input.nextLong(); input.nextLine();

        f = FornecedorDAO.buscarPorId(id);

        if(f != null){
            System.out.println("Editando: " + f.getNomeFantasia());

            System.out.print("Novo Nome (ENTER para manter): ");
            novoNome = input.nextLine();
            
            System.out.print("Novo CNPJ (ENTER para manter): ");
            novoCnpj = input.nextLine();

            System.out.print("Novo Telefone Contato (ENTER para manter): ");
            novoTel = input.nextLine();

            System.out.print("Novo Telegram (ENTER para manter): ");
            novoTelTelegram = input.nextLine();

            if(!novoNome.isEmpty()) f.setNomeFantasia(novoNome);
            if(!novoCnpj.isEmpty() && Fornecedor.validaCnpj(novoCnpj)) f.setCnpj(novoCnpj);
            if(!novoTel.isEmpty()) f.setTelefoneContato(novoTel);
            if(!novoTelTelegram.isEmpty()) f.setTelefoneTelegram(novoTelTelegram);

            FornecedorDAO.modificar(f);
        } else {
            System.out.println("Fornecedor não encontrado.");
        }
    }

    public static void deletar(Scanner input){
        long id;
        int op;

        System.out.print("\nID do Fornecedor para deletar: ");
        id = input.nextLong(); input.nextLine();

        System.out.print("Confirmar deleção? (1-Sim / 0-Não): ");
        op = input.nextInt(); 
        input.nextLine();

        if(op == 1){
            FornecedorDAO.deletar(id);
        } else {
            System.out.println("Operação cancelada.");
        }
    }

    //Setters
    public void setId(long id){
        if(id >= 0) this.id = id;
    }

    public void setCnpj(String cnpj){
        if(Fornecedor.validaCnpj(cnpj)) this.cnpj = cnpj;
    }

    public void setNomeFantasia(String nomeFantasia){
        if(nomeFantasia != null && !nomeFantasia.isEmpty() && nomeFantasia.length() <= 1024){
            this.nomeFantasia = nomeFantasia;
        }
    }

    public void setTelefoneTelegram(String telefone){
        if(Telefone.validaNumero(telefone)) this.telefoneTelegram = telefone;
    }

    public void setTelefoneContato(String telefone){
        if(Telefone.validaNumero(telefone)) this.telefoneContato = telefone;
    }

    //Getters
    public long getId(){ return this.id; }
    public String getCnpj(){ return this.cnpj; }
    public String getNomeFantasia(){ return this.nomeFantasia; }
    public String getTelefoneTelegram(){ return this.telefoneTelegram; }
    public String getTelefoneContato(){ return this.telefoneContato; }

    //Metodos Auxiliares
    public static boolean validaCnpj(String cnpj){
        char caractere;
        if(cnpj == null || cnpj.isEmpty() || cnpj.length() != 14) return false;
        
        for(int i = 0; i < cnpj.length(); i++){
            caractere = cnpj.charAt(i);
            if(!Character.isDigit(caractere)) return false;
        }
        return true;
    }

    //Construtores
    public Fornecedor(){
        this.id = 0;
        this.cnpj = "00000000000000";
        this.nomeFantasia = "Nome Fantasia Generico";
        this.telefoneTelegram = "00000000000000000000";
        this.telefoneContato = "00000000000000000000";
    }

    public Fornecedor(long id, String cnpj, String nomeFantasia, String telefoneTelegram, String telefoneContato){
        this.setId(id);
        this.setCnpj(cnpj);
        this.setNomeFantasia(nomeFantasia);
        this.setTelefoneContato(telefoneContato);
        this.setTelefoneTelegram(telefoneTelegram);
    }
}