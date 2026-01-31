import java.util.Scanner;
import java.util.ArrayList;

public class Cliente extends Pessoa {
    
    // Construtores
    public Cliente(){
        super();
    }

    public Cliente(long id, String nome, String cpf){
        super(id, nome, cpf);
    }

    //Métodos
    public static void adicionar(Scanner input){
        String nome;
        String cpf;
        String op;
        String numTelefone;
        boolean executandoTelefone;
        Cliente novoCliente;
        Telefone t;

        System.out.print("\n\nDigite o nome do cliente (lim. 1024 char): ");
        nome = input.nextLine();
        System.out.print("Digite CPF do cliente (11 char, somente números): ");
        cpf = input.nextLine();
        
        novoCliente = new Cliente(0, nome, cpf);

        if(!novoCliente.validaAtributos(nome, cpf)){
            System.out.println("Valor(es) Inválido(s)! Retornando ao menu Cliente!");
            return;
        }

        executandoTelefone = true;
        while(executandoTelefone){
            System.out.print("Deseja adicionar um telefone? (S - Sim / N - Não): ");
            op = input.nextLine();

            if(op.equalsIgnoreCase("S")){
                System.out.print("Digite o número (Apenas dígitos): ");
                numTelefone = input.nextLine();
                
                if(Telefone.validaNumero(numTelefone)){
                    t = new Telefone(0, 0, numTelefone);
                    novoCliente.adicionarTelefone(t);
                    System.out.println("Telefone adicionado à lista.");
                } else {
                    System.out.println("Número inválido.");
                }
            } else {
                executandoTelefone = false;
            }
        }

        ClienteDAO.salvar(novoCliente);
    }

    public static void buscarCpf(Scanner input){
        String cpf;
        Cliente resultado;
        ArrayList<Telefone> telefones;

        System.out.print("\nDigite o CPF para buscar: ");
        cpf = input.nextLine();
        
        resultado = ClienteDAO.buscarPorCpf(cpf);
        
        if(resultado != null){
            System.out.println("\nCliente encontrado:");
            System.out.println("ID: " + resultado.getId());
            System.out.println("Nome: " + resultado.getNome());
            System.out.println("CPF: " + resultado.getCpf());
            
            telefones = resultado.getTelefones();
            if(telefones != null && !telefones.isEmpty()){
                System.out.println("Telefones:");
                for(Telefone t : telefones){
                    System.out.println(" - " + t.getNumero());
                }
            } else {
                System.out.println("Telefones: (Nenhum cadastrado)");
            }

        } else {
            System.out.println("Cliente não encontrado com este CPF.");
        }
    }

    public static void buscarId(Scanner input){
        long id;
        Cliente resultado;
        ArrayList<Telefone> telefones;

        System.out.print("\nDigite o ID para buscar: ");
        id = input.nextLong(); input.nextLine();

        resultado = ClienteDAO.buscarPorId(id);

        if(resultado != null){
            System.out.println("\nCliente encontrado:");
            System.out.println("ID: " + resultado.getId());
            System.out.println("Nome: " + resultado.getNome());
            System.out.println("CPF: " + resultado.getCpf());

            telefones = resultado.getTelefones();
            if(telefones != null && !telefones.isEmpty()){
                System.out.println("Telefones:");
                for(Telefone t : telefones){
                    System.out.println(" - " + t.getNumero());
                }
            } else {
                System.out.println("Telefones: (Nenhum cadastrado)");
            }
        } else {
            System.out.println("Cliente não encontrado com este ID.");
        }
    }

    public static void listarTodos(){
        ArrayList<Cliente> lista;
        ArrayList<Telefone> telefones;

        lista = ClienteDAO.listarTodos();
        
        if(lista.isEmpty()){
            System.out.println("\nNão há clientes cadastrados.");
        } else {
            System.out.println("\n--- Lista de Clientes ---");
            for(Cliente c : lista){
                System.out.println("ID: " + c.getId() + " | Nome: " + c.getNome() + " | CPF: " + c.getCpf());
                
                telefones = c.getTelefones();
                if(telefones != null && !telefones.isEmpty()){
                    System.out.print("   Telefones: ");
                    for(Telefone t : telefones){
                        System.out.print("[" + t.getNumero() + "] ");
                    }
                    System.out.println("");
                }
                System.out.println("--------------------------------");
            }
        }
    }

    public static void modificar(Scanner input){
        long id;
        String novoNome;
        String novoCpf;
        Cliente cliente;

        System.out.print("\nDigite o ID do cliente que deseja modificar: ");
        id = input.nextLong(); input.nextLine();

        cliente = ClienteDAO.buscarPorId(id);

        if(cliente != null){
            System.out.println("Cliente atual: " + cliente.getNome());
            
            System.out.print("Digite o novo nome: ");
            novoNome = input.nextLine();
            
            System.out.print("Digite o novo CPF: ");
            novoCpf = input.nextLine();

            if(!novoNome.isEmpty()) cliente.setNome(novoNome);
            if(!novoCpf.isEmpty()) cliente.setCpf(novoCpf);

            ClienteDAO.modificar(cliente);
        } else {
            System.out.println("Cliente não encontrado para modificação.");
        }
    }

    public static void deletar(Scanner input){
        long id;
        int op;

        System.out.print("\nDigite o ID do cliente que deseja deletar: ");
        id = input.nextLong(); input.nextLine();

        System.out.print("Tem certeza? (1 - Sim / 0 - Não): ");
        op = input.nextInt(); input.nextLine();

        if(op == 1){
            ClienteDAO.deletar(id);
        } else {
            System.out.println("Operação cancelada.");
        }
    }

    public static void nuncaComprou(){
        ArrayList<Cliente> lista;
        ArrayList<Telefone> telefones;

        lista = ClienteDAO.listarSemCompras();
        
        if(lista.isEmpty()){
            System.out.println("\nTodos os clientes ja realizaram pelo menos uma compra.");
        } else {
            System.out.println("\n--- Clientes que NUNCA compraram ---");
            for(Cliente c : lista){
                System.out.println("ID: " + c.getId() + " | Nome: " + c.getNome() + " | CPF: " + c.getCpf());
                
                telefones = c.getTelefones();
                if(telefones != null && !telefones.isEmpty()){
                    System.out.print("   Telefones: ");
                    for(Telefone t : telefones){
                        System.out.print("[" + t.getNumero() + "] ");
                    }
                    System.out.println("");
                }
                System.out.println("--------------------------------");
            }
            System.out.println("Total de clientes inativos: " + lista.size());
        }
    }
}