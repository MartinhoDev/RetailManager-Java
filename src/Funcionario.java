import java.util.Scanner;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

public class Funcionario extends Pessoa {
    private long id_funcionario;
    private double salario;

    public Funcionario() {
        super();
        this.id_funcionario = 0;
        this.salario = 0.0;
    }

    public Funcionario(long id, String nome, String cpf, long id_funcionario, double salario) {
        super(id, nome, cpf);
        this.setIdFuncionario(id_funcionario);
        this.setSalario(salario);
    }

    // --- MÉTODOS DO MENU ---

    public static void adicionar(Scanner input) {
        String nome;
        String cpf;
        double salario;
        String op;
        String numTelefone;
        boolean executandoTelefone;
        Funcionario novoFunc;
        Telefone t;

        System.out.print("\n\nDigite o nome do funcionario: ");
        nome = input.nextLine();
        
        System.out.print("Digite CPF do funcionario (somente numeros): ");
        cpf = input.nextLine();

        System.out.print("Digite o Salario: ");
        salario = input.nextDouble(); input.nextLine();

        novoFunc = new Funcionario(0, nome, cpf, 0, salario);

        if (!Pessoa.validaCpf(cpf) || salario < 0) {
            System.out.println("Dados invalidos (CPF ou Salario)! Operacao cancelada.");
            return;
        }

        executandoTelefone = true;
        while(executandoTelefone){
            System.out.print("Deseja adicionar um telefone? (S - Sim / N - Nao): ");
            op = input.nextLine();

            if(op.equalsIgnoreCase("S")){
                System.out.print("Digite o numero (Apenas digitos): ");
                numTelefone = input.nextLine();
                
                if(Telefone.validaNumero(numTelefone)){
                    t = new Telefone(0, 0, numTelefone);
                    novoFunc.adicionarTelefone(t);
                    System.out.println("Telefone adicionado a lista.");
                } else {
                    System.out.println("Numero invalido.");
                }
            } else {
                executandoTelefone = false;
            }
        }

        FuncionarioDAO.salvar(novoFunc);
    }

    public static void listarTodos() {
        ArrayList<Funcionario> lista;
        ArrayList<Telefone> telefones;

        lista = FuncionarioDAO.listarTodos();

        if (lista.isEmpty()) {
            System.out.println("\nNao ha funcionarios cadastrados.");
        } else {
            System.out.println("\n--- Lista de Funcionarios ---");
            for (Funcionario f : lista) {
                System.out.println("ID Pessoa: " + f.getId() + 
                                   " | Nome: " + f.getNome() + 
                                   " | CPF: " + f.getCpf() + 
                                   " | Salario: R$ " + f.getSalario());
                
                telefones = f.getTelefones();
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

    public static void buscarCpf(Scanner input) {
        String cpf;
        Funcionario f;
        ArrayList<Telefone> telefones;

        System.out.print("\nDigite o CPF para buscar: ");
        cpf = input.nextLine();

        f = FuncionarioDAO.buscarPorCpf(cpf);

        if (f != null) {
            System.out.println("\nFuncionario encontrado:");
            System.out.println("ID: " + f.getId());
            System.out.println("Nome: " + f.getNome());
            System.out.println("Salario: " + f.getSalario());

            telefones = f.getTelefones();
            if(telefones != null && !telefones.isEmpty()){
                System.out.println("Telefones:");
                for(Telefone t : telefones){
                    System.out.println(" - " + t.getNumero());
                }
            } else {
                System.out.println("Telefones: (Nenhum cadastrado)");
            }
        } else {
            System.out.println("Funcionario nao encontrado.");
        }
    }

    public static void buscarId(Scanner input) {
        long id;
        Funcionario f;
        ArrayList<Telefone> telefones;

        System.out.print("\nDigite o ID (Pessoa) para buscar: ");
        id = input.nextLong(); input.nextLine();

        f = FuncionarioDAO.buscarPorId(id);

        if (f != null) {
            System.out.println("\nFuncionario encontrado:");
            System.out.println("Nome: " + f.getNome());
            System.out.println("CPF: " + f.getCpf());
            System.out.println("Salario: " + f.getSalario());

            telefones = f.getTelefones();
            if(telefones != null && !telefones.isEmpty()){
                System.out.println("Telefones:");
                for(Telefone t : telefones){
                    System.out.println(" - " + t.getNumero());
                }
            } else {
                System.out.println("Telefones: (Nenhum cadastrado)");
            }
        } else {
            System.out.println("Funcionario nao encontrado.");
        }
    }

    public static void modificar(Scanner input) {
        long id;
        String novoNome, novoCpf;
        double novoSalario;
        Funcionario f;

        System.out.print("\nDigite o ID do funcionario para modificar: ");
        id = input.nextLong(); input.nextLine();

        f = FuncionarioDAO.buscarPorId(id);

        if (f != null) {
            System.out.println("Editando: " + f.getNome() + " | Salario Atual: " + f.getSalario());

            System.out.print("Novo Nome (ENTER para manter): ");
            novoNome = input.nextLine();

            System.out.print("Novo CPF (ENTER para manter): ");
            novoCpf = input.nextLine();
            
            System.out.print("Novo Salario (-1 para manter): ");
            novoSalario = input.nextDouble(); input.nextLine();

            if (!novoNome.isEmpty()) f.setNome(novoNome);
            if (!novoCpf.isEmpty() && Pessoa.validaCpf(novoCpf)) f.setCpf(novoCpf);
            if (novoSalario >= 0) f.setSalario(novoSalario);

            FuncionarioDAO.modificar(f);
        } else {
            System.out.println("Funcionario nao encontrado.");
        }
    }

    public static void deletar(Scanner input) {
        long id;
        int op;

        System.out.print("\nDigite o ID do funcionario para deletar: ");
        id = input.nextLong(); input.nextLine();

        System.out.print("Tem certeza? (1 - Sim / 0 - Nao): ");
        op = input.nextInt(); input.nextLine();

        if (op == 1) {
            FuncionarioDAO.deletar(id);
        } else {
            System.out.println("Operacao cancelada.");
        }
    }

    public static void rankingReceita() {
        LinkedHashMap<String, Double> ranking;
        int posicao = 1;

        ranking = FuncionarioDAO.listarRankingReceita();

        if (ranking.isEmpty()) {
            System.out.println("\nNenhuma venda registrada para gerar ranking.");
        } else {
            System.out.println("\n=== RANKING DE FUNCIONARIOS (RECEITA GERADA) ===");
            System.out.println(String.format("| %-3s | %-30s | %-15s |", "POS", "FUNCIONARIO", "RECEITA TOTAL"));
            System.out.println("-------------------------------------------------------");
            
            for (Map.Entry<String, Double> entry : ranking.entrySet()) {
                String nome = entry.getKey();
                if (nome.length() > 30) nome = nome.substring(0, 27) + "...";
                
                System.out.printf("| %-3d | %-30s | R$ %-12.2f |\n", 
                    posicao, nome, entry.getValue());
                posicao++;
            }
            System.out.println("-------------------------------------------------------");
        }
    }

    public static void relatorioSalariosAltos() {
        ArrayList<Funcionario> lista = FuncionarioDAO.listarSalariosAcimaMedia();
        if(lista.isEmpty()){
            System.out.println("Nenhum salario acima da media.");
        } else {
            System.out.println("\n--- Funcionarios com Salario Acima da Media ---");
            for(Funcionario f : lista){
                System.out.printf("%s | R$ %.2f\n", f.getNome(), f.getSalario());
            }
        }
    }

    public void setIdFuncionario(long id_funcionario) {
        if (id_funcionario >= 0) {
            this.id_funcionario = id_funcionario;
        }
    }

    public void setSalario(double salario) {
        if (salario >= 0.0) {
            this.salario = salario;
        }
    }

    public long getIdFuncionario() {
        return this.id_funcionario;
    }

    public double getSalario() {
        return this.salario;
    }
}