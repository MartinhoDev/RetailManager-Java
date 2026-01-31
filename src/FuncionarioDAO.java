import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedHashMap; // Importante para manter a ordem do ranking

public class FuncionarioDAO {

    // ... (Outros métodos salvar, listarTodos, buscarPorCpf, buscarPorId, modificar, deletar mantidos iguais) ...
    // Vou reimprimir apenas os métodos existentes para contexto e adicionar o novo no final

    public static void salvar(Funcionario funcionario) {
        String sqlPessoa = "INSERT INTO pessoa (nome, cpf) VALUES (?, ?)";
        String sqlFunc = "INSERT INTO funcionario (pessoa_id, salario) VALUES (?, ?)";
        String sqlTelefone = "INSERT INTO telefone (numero, pessoa_id) VALUES (?, ?)";
        Connection conn = null;
        PreparedStatement pstmPessoa = null;
        PreparedStatement pstmFunc = null;
        PreparedStatement pstmTelefone = null;
        ResultSet rs = null;
        long idPessoaGerado = 0;

        try {
            conn = Conexao.getConexao();
            conn.setAutoCommit(false);

            pstmPessoa = conn.prepareStatement(sqlPessoa, Statement.RETURN_GENERATED_KEYS);
            pstmPessoa.setString(1, funcionario.getNome());
            pstmPessoa.setString(2, funcionario.getCpf());
            pstmPessoa.executeUpdate();

            rs = pstmPessoa.getGeneratedKeys();
            if (rs.next()) {
                idPessoaGerado = rs.getLong(1);
            }

            pstmFunc = conn.prepareStatement(sqlFunc);
            pstmFunc.setLong(1, idPessoaGerado);
            pstmFunc.setDouble(2, funcionario.getSalario());
            pstmFunc.executeUpdate();

            if(funcionario.getTelefones() != null && !funcionario.getTelefones().isEmpty()){
                pstmTelefone = conn.prepareStatement(sqlTelefone);
                for(Telefone t : funcionario.getTelefones()){
                    pstmTelefone.setString(1, t.getNumero());
                    pstmTelefone.setLong(2, idPessoaGerado);
                    pstmTelefone.executeUpdate();
                }
            }

            conn.commit();
            System.out.println("Funcionario salvo com sucesso!");

        } catch (SQLException e) {
            try { if (conn != null) conn.rollback(); } catch (SQLException ex) {}
            System.out.println("Erro ao salvar funcionario: " + e.getMessage());
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmPessoa != null) pstmPessoa.close();
                if (pstmFunc != null) pstmFunc.close();
                if (pstmTelefone != null) pstmTelefone.close();
                if (conn != null) conn.close();
            } catch (SQLException e) { e.printStackTrace(); }
        }
    }

    public static ArrayList<Funcionario> listarTodos() {
        String sql = "SELECT p.id as pid, p.nome, p.cpf, f.id as fid, f.salario " +
                     "FROM pessoa p INNER JOIN funcionario f ON p.id = f.pessoa_id";
        ArrayList<Funcionario> lista = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;
        Funcionario f = null;

        try {
            conn = Conexao.getConexao();
            pstm = conn.prepareStatement(sql);
            rs = pstm.executeQuery();

            while (rs.next()) {
                f = new Funcionario();
                f.setId(rs.getLong("pid")); 
                f.setNome(rs.getString("nome"));
                f.setCpf(rs.getString("cpf"));
                f.setIdFuncionario(rs.getLong("fid"));
                f.setSalario(rs.getDouble("salario"));
                f.setTelefones(TelefoneDAO.listarPorPessoa(f.getId()));
                lista.add(f);
            }
        } catch (SQLException e) {
            System.out.println("Erro ao listar funcionarios: " + e.getMessage());
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstm != null) pstm.close();
                if (conn != null) conn.close();
            } catch (SQLException e) { e.printStackTrace(); }
        }
        return lista;
    }

    public static Funcionario buscarPorCpf(String cpf) {
        String sql = "SELECT p.id as pid, p.nome, p.cpf, f.id as fid, f.salario " +
                     "FROM pessoa p INNER JOIN funcionario f ON p.id = f.pessoa_id " +
                     "WHERE p.cpf = ?";
        Connection conn = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;
        Funcionario f = null;

        try {
            conn = Conexao.getConexao();
            pstm = conn.prepareStatement(sql);
            pstm.setString(1, cpf);
            rs = pstm.executeQuery();

            if (rs.next()) {
                f = new Funcionario();
                f.setId(rs.getLong("pid"));
                f.setNome(rs.getString("nome"));
                f.setCpf(rs.getString("cpf"));
                f.setIdFuncionario(rs.getLong("fid"));
                f.setSalario(rs.getDouble("salario"));
                f.setTelefones(TelefoneDAO.listarPorPessoa(f.getId()));
            }
        } catch (SQLException e) {
            System.out.println("Erro ao buscar por CPF: " + e.getMessage());
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstm != null) pstm.close();
                if (conn != null) conn.close();
            } catch (SQLException e) { e.printStackTrace(); }
        }
        return f;
    }

    public static Funcionario buscarPorId(long id) {
        String sql = "SELECT p.id as pid, p.nome, p.cpf, f.id as fid, f.salario " +
                     "FROM pessoa p INNER JOIN funcionario f ON p.id = f.pessoa_id " +
                     "WHERE p.id = ?";
        Connection conn = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;
        Funcionario f = null;

        try {
            conn = Conexao.getConexao();
            pstm = conn.prepareStatement(sql);
            pstm.setLong(1, id);
            rs = pstm.executeQuery();

            if (rs.next()) {
                f = new Funcionario();
                f.setId(rs.getLong("pid"));
                f.setNome(rs.getString("nome"));
                f.setCpf(rs.getString("cpf"));
                f.setIdFuncionario(rs.getLong("fid"));
                f.setSalario(rs.getDouble("salario"));
                f.setTelefones(TelefoneDAO.listarPorPessoa(f.getId()));
            }
        } catch (SQLException e) {
            System.out.println("Erro ao buscar por ID: " + e.getMessage());
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstm != null) pstm.close();
                if (conn != null) conn.close();
            } catch (SQLException e) { e.printStackTrace(); }
        }
        return f;
    }

    public static Funcionario buscarPorIdInterno(long idFuncionario) {
        String sql = "SELECT p.id as pid, p.nome, p.cpf, f.id as fid, f.salario " +
                     "FROM funcionario f INNER JOIN pessoa p ON f.pessoa_id = p.id " +
                     "WHERE f.id = ?";
        Connection conn = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;
        Funcionario f = null;

        try {
            conn = Conexao.getConexao();
            pstm = conn.prepareStatement(sql);
            pstm.setLong(1, idFuncionario);
            rs = pstm.executeQuery();

            if (rs.next()) {
                f = new Funcionario();
                f.setId(rs.getLong("pid")); 
                f.setNome(rs.getString("nome"));
                f.setCpf(rs.getString("cpf"));
                f.setIdFuncionario(rs.getLong("fid"));
                f.setSalario(rs.getDouble("salario"));
            }
        } catch (SQLException e) {
            System.out.println("Erro ao buscar por ID do Funcionario: " + e.getMessage());
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstm != null) pstm.close();
                if (conn != null) conn.close();
            } catch (SQLException e) { e.printStackTrace(); }
        }
        return f;
    }

    public static void modificar(Funcionario funcionario) {
        String sqlPessoa = "UPDATE pessoa SET nome = ?, cpf = ? WHERE id = ?";
        String sqlFunc = "UPDATE funcionario SET salario = ? WHERE pessoa_id = ?";
        Connection conn = null;
        PreparedStatement pstmPessoa = null;
        PreparedStatement pstmFunc = null;

        try {
            conn = Conexao.getConexao();
            conn.setAutoCommit(false);

            pstmPessoa = conn.prepareStatement(sqlPessoa);
            pstmPessoa.setString(1, funcionario.getNome());
            pstmPessoa.setString(2, funcionario.getCpf());
            pstmPessoa.setLong(3, funcionario.getId());
            pstmPessoa.executeUpdate();

            pstmFunc = conn.prepareStatement(sqlFunc);
            pstmFunc.setDouble(1, funcionario.getSalario());
            pstmFunc.setLong(2, funcionario.getId()); 
            pstmFunc.executeUpdate();

            conn.commit();
            System.out.println("Funcionario modificado com sucesso!");

        } catch (SQLException e) {
            try { if (conn != null) conn.rollback(); } catch (SQLException ex) {}
            System.out.println("Erro ao modificar: " + e.getMessage());
        } finally {
            try {
                if (pstmPessoa != null) pstmPessoa.close();
                if (pstmFunc != null) pstmFunc.close();
                if (conn != null) conn.close();
            } catch (SQLException e) { e.printStackTrace(); }
        }
    }

    public static void deletar(long id) {
        String sql = "DELETE FROM pessoa WHERE id = ?";
        Connection conn = null;
        PreparedStatement pstm = null;

        try {
            conn = Conexao.getConexao();
            pstm = conn.prepareStatement(sql);
            pstm.setLong(1, id);
            
            int linhas = pstm.executeUpdate();
            if(linhas > 0) {
                System.out.println("Funcionario deletado com sucesso!");
            } else {
                System.out.println("ID nao encontrado.");
            }
        } catch (SQLException e) {
            System.out.println("Erro ao deletar: " + e.getMessage());
        } finally {
            try {
                if (pstm != null) pstm.close();
                if (conn != null) conn.close();
            } catch (SQLException e) { e.printStackTrace(); }
        }
    }

    public static LinkedHashMap<String, Double> listarRankingReceita() {
        String sql = "SELECT p.nome, SUM(ip.qtd * ip.preco_praticado) as receita_total " +
                     "FROM funcionario f " +
                     "INNER JOIN pessoa p ON f.pessoa_id = p.id " +
                     "INNER JOIN venda v ON v.funcionario_id = f.id " +
                     "INNER JOIN item_produto ip ON ip.venda_id = v.id " +
                     "GROUP BY f.id " +
                     "ORDER BY receita_total DESC";
        Connection conn = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;
        LinkedHashMap<String, Double> ranking = new LinkedHashMap<>();

        try {
            conn = Conexao.getConexao();
            pstm = conn.prepareStatement(sql);
            rs = pstm.executeQuery();

            while(rs.next()){
                ranking.put(rs.getString("nome"), rs.getDouble("receita_total"));
            }

        } catch (SQLException e) {
            System.out.println("Erro ao gerar ranking: " + e.getMessage());
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstm != null) pstm.close();
                if (conn != null) conn.close();
            } catch (SQLException e) { e.printStackTrace(); }
        }
        return ranking;
    }

    public static ArrayList<Funcionario> listarSalariosAcimaMedia() {
        String sql = "SELECT p.id as pid, p.nome, p.cpf, f.id as fid, f.salario " +
                     "FROM pessoa p INNER JOIN funcionario f ON p.id = f.pessoa_id " +
                     "WHERE f.salario > (SELECT AVG(salario) FROM funcionario)";
                     
        ArrayList<Funcionario> lista = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;
        Funcionario f = null;

        try {
            conn = Conexao.getConexao();
            pstm = conn.prepareStatement(sql);
            rs = pstm.executeQuery();

            while (rs.next()) {
                f = new Funcionario();
                f.setId(rs.getLong("pid")); 
                f.setNome(rs.getString("nome"));
                f.setCpf(rs.getString("cpf"));
                f.setIdFuncionario(rs.getLong("fid"));
                f.setSalario(rs.getDouble("salario"));
                f.setTelefones(TelefoneDAO.listarPorPessoa(f.getId()));
                lista.add(f);
            }
            
            rs.close(); pstm.close(); conn.close();
        } catch (SQLException e) {
            System.out.println("Erro ao listar salarios acima da media: " + e.getMessage());
        }
        return lista;
    }
}