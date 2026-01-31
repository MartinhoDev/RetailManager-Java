import java.util.Scanner;

public class Menu {
    public static void menuVenda(Scanner input){
        boolean exe = true;
        int intBuffer;
        while(exe){
            System.out.print(
                "\nMenu Venda:\n" +
                "1 - Realizar Venda\n" +
                "2 - Relatório de Vendas\n" +
                "3 - Relatório de Venda específica\n" +
                "4 - Média de Vendas por data\n" +
                "5 - Vendas de um mês e ano específico\n" +
                "6 - Idade de Vendas em dias\n" +
                "7 - Dia da semana com mais Vendas\n" +
                "8 - Relatório maior Venda já feita\n" +
                "0 - Voltar\n" +
                "Digite o número da opção desejada: "
            );
            intBuffer = input.nextInt(); input.nextLine();
            switch(intBuffer){
                case 1:
                    Venda.realizar(input);
                    break;
                case 2:
                    Venda.relatorio();
                    break;
                case 3:
                    Venda.relatorioEspecifico(input);
                    break;
                case 4:
                    Venda.mediaVenda(input);
                    break;
                case 5:
                    Venda.vendasMesAno(input);
                    break;
                case 6:
                    Venda.idadeDias();
                    break;
                case 7:
                    Venda.diaSemanaMaisVendas();
                    break;
                case 8:
                    Venda.relatorioMaiorVenda();
                    break;
                case 0:
                    exe = false;
                    break;
                default:
                    System.out.println("Opção Inválida! Retornando ao Menu Cliente!");
            }
        }
    }

    public static void menuFornecimento(Scanner input){
        boolean exe = true;
        int intBuffer;
        while(exe){
            System.out.print(
                "\nMenu Fornecimento:\n" +
                "1 - Adicionar fornecimento\n" +
                "2 - Listar todos os fornecimentos\n" +
                "3 - Listar Fornecimento específico\n" +
                "0 - Voltar\n" +
                "Digite o número da opção desejada: "
            );
            intBuffer = input.nextInt(); input.nextLine();
            switch(intBuffer){
                case 1:
                    Fornecimento.adicionar(input);
                    break;
                case 2:
                    Fornecimento.listar();
                    break;
                case 3:
                    Fornecimento.listarEspecifico(input);
                    break;
                case 0:
                    exe = false;
                    break;
                default:
                    System.out.println("Opção Inválida! Voltando ao menu Fornecimento!");
            }
        }
    }

    public static void menuCliente(Scanner input){
        boolean exe = true;
        int intBuffer;
        while(exe){
            System.out.print(
                "\nMenu Cliente:\n" +
                "1 - Adicionar novo cliente\n" +
                "2 - Buscar por CPF\n" +
                "3 - Buscar por ID\n" +
                "4 - Listar todos\n" +
                "5 - Modificar Cliente\n" +
                "6 - Deletar Cliente\n" +
                "7 - Listar Clientes que nunca compraram\n" +
                "0 - Voltar\n" +
                "Digite o número da opção desejada: "
            );
            intBuffer = input.nextInt(); input.nextLine();
            switch(intBuffer){
                case 1:
                    Cliente.adicionar(input);
                    break;
                case 2:
                    Cliente.buscarCpf(input);
                    break;
                case 3:
                    Cliente.buscarId(input);
                    break;
                case 4:
                    Cliente.listarTodos();
                    break;
                case 5:
                    Cliente.modificar(input);
                    break;
                case 6:
                    Cliente.deletar(input);
                    break;
                case 7:
                    Cliente.nuncaComprou();
                    break;
                case 0:
                    exe = false;
                    break;
                default:
                    System.out.println("Opção Inválida! Retornando ao Menu Cliente!");
            }
        }
    }

    public static void menuFuncionario(Scanner input){
        boolean exe = true;
        int intBuffer;

        while(exe){
            System.out.print(
                "\nMenu Funcionario:\n" +
                "1 - Adicionar novo funcionario\n" +
                "2 - Buscar por CPF\n" +
                "3 - Buscar por ID\n" +
                "4 - Listar todos\n" +
                "5 - Modificar funcionario\n" +
                "6 - Deletar funcionario\n" +
                "7 - Rankear Funcionarios por receita gerada\n" +
                "8 - Listar Funcionarios com salário acima da média\n" +
                "0 - Voltar\n" +
                "Digite o número da opção desejada: "
            );
            intBuffer = input.nextInt(); input.nextLine();
            switch(intBuffer){
                case 1:
                    Funcionario.adicionar(input);
                    break;
                case 2:
                    Funcionario.buscarCpf(input);
                    break;
                case 3:
                    Funcionario.buscarId(input);
                    break;
                case 4:
                    Funcionario.listarTodos();
                    break;
                case 5:
                    Funcionario.modificar(input);
                    break;
                case 6:
                    Funcionario.deletar(input);
                    break;
                case 7:
                    Funcionario.rankingReceita();
                    break;
                case 8:
                    Funcionario.relatorioSalariosAltos();
                    break;
                case 0:
                    exe = false;
                    break;
                default:
                    System.out.println("Opção Inválida! Retornando ao Menu Funcionario!");
            }
        }
    }

    public static void menuProduto(Scanner input){
        boolean exe = true;
        int intBuffer;

        while(exe){
            System.out.print(
                "\nMenu Produtos:\n" +
                "1 - Adicionar Produto\n" +
                "2 - Buscar por ID\n" +
                "3 - Buscar por Nome\n" +
                "4 - Listar todos\n" +
                "5 - Modificar Produto\n" +
                "6 - Deletar Produto\n" +
                "7 - Listar Produtos mais caros que a media\n" +
                "8 - Listar Produtos com <20 no estoque\n" +
                "0 - Voltar\n" +
                "Digite o número da opção desejada: "
            );
            intBuffer = input.nextInt(); input.nextLine();
            switch(intBuffer){
                case 1:
                    Produto.adicionar(input);
                    break;
                case 2:
                    Produto.buscarId(input);
                    break;
                case 3:
                    Produto.buscarNome(input);
                    break;
                case 4:
                    Produto.listarTodos();
                    break;
                case 5:
                    Produto.modificar(input);
                    break;
                case 6:
                    Produto.deletar(input);
                    break;
                case 7:
                    Produto.maisCaroMedia();
                    break;
                case 8:
                    Produto.menosVinteEstoque();
                    break;
                case 0:
                    exe = false;
                    break;
                default:
                    System.out.println("Opção Inválida! Retornando ao Menu Cliente!");
            }
        }
    }

    public static void menuFornecedor(Scanner input){
        boolean exe = true;
        int intBuffer;

        while(exe){
            System.out.print(
                "\nMenu Fornecedor:\n" +
                "1 - Adicionar Fornecedor\n" +
                "2 - Buscar por ID\n" +
                "3 - Buscar por CNPJ\n" +
                "4 - Listar todos\n" +
                "5 - Modificar Fornecedor\n" +
                "6 - Deletar Fornecedor\n" +
                "0 - Voltar\n" +
                "Digite o número da opção desejada: "
            );
            intBuffer = input.nextInt(); input.nextLine();
            switch(intBuffer){
                case 1:
                    Fornecedor.adicionar(input);
                    break;
                case 2:
                    Fornecedor.buscarId(input);
                    break;
                case 3:
                    Fornecedor.buscarCnpj(input);
                    break;
                case 4:
                    Fornecedor.listarTodos();
                    break;
                case 5:
                    Fornecedor.modificar(input);
                    break;
                case 6:
                    Fornecedor.deletar(input);
                    break;
                case 0:
                    exe = false;
                    break;
                default:
                    System.out.println("Opção Inválida! Retornando ao Menu Fornecedor!");
            }
        }
    }

    public static void menuPrincipal(){
        boolean exe = true;
        Scanner input = new Scanner(System.in);
        int intBuffer;

        System.out.println(
            "=================================================\n" +
            "| Seja bem vindo ao sistema de gerenciamento do |\n" +
            "|          MERCADO VAREJO GENÉRICO!!!           |\n" +
            "=================================================\n"
        );
        while (exe){
            System.out.print(
                "\nMenu Principal:\n" +
                "1 - Gerenciar clientes\n" +
                "2 - Gerenciar funcionarios\n" +
                "3 - Gerenciar vendas\n" +
                "4 - Controle de estoque dos produtos\n" +
                "5 - Gerenciar fornecedores\n" +
                "6 - Gerenciar fornecimentos\n" +
                "0 - Sair\n" +
                "Digite o número da opção desejada: "
            );
            intBuffer = input.nextInt(); input.nextLine();
            switch (intBuffer) {
                case 0:
                    exe = false;
                    break;
                case 1:
                    Menu.menuCliente(input);
                    break;
                case 2:
                    Menu.menuFuncionario(input);
                    break;
                case 3:
                    Menu.menuVenda(input);
                    break;
                case 4:
                    Menu.menuProduto(input);
                    break;
                case 5:
                    Menu.menuFornecedor(input);
                    break;
                case 6:
                    Menu.menuFornecimento(input);
                default:
                    System.out.println("Opção Inválida! Retornando ao Menu Principal!");
                    break;
            }
        }
        System.out.println("Obrigado por utilizar o sistema!!!");
        input.close();
    }
}
