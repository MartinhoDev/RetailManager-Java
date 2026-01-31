import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class ClienteDAO {
    
    public static void salvar(Cliente cliente) {
        String sqlPessoa = "INSERT INTO pessoa (nome, cpf) VALUES (?, ?)";
        String sqlCliente = "INSERT INTO cliente (pessoa_id) VALUES (?)";
        String sqlTelefone = "INSERT INTO telefone (numero, pessoa_id) VALUES (?, ?)";
        Connection conn = null;
        PreparedStatement pstmPessoa = null;
        PreparedStatement pstmCliente = null;
        PreparedStatement pstmTelefone = null;
        ResultSet rs = null;
        long idPessoaGerado = 0;

        try {
            conn = Conexao.getConexao();
            conn.setAutoCommit(false);

            pstmPessoa = conn.prepareStatement(sqlPessoa, Statement.RETURN_GENERATED_KEYS);
            pstmPessoa.setString(1, cliente.getNome());
            pstmPessoa.setString(2, cliente.getCpf());
            pstmPessoa.executeUpdate();

            rs = pstmPessoa.getGeneratedKeys();
            if(rs.next()){
                idPessoaGerado = rs.getLong(1);
            }

            pstmCliente = conn.prepareStatement(sqlCliente);
            pstmCliente.setLong(1, idPessoaGerado);
            pstmCliente.executeUpdate();

            if(cliente.getTelefones() != null && !cliente.getTelefones().isEmpty()){
                pstmTelefone = conn.prepareStatement(sqlTelefone);
                for(Telefone t : cliente.getTelefones()){
                    pstmTelefone.setString(1, t.getNumero());
                    pstmTelefone.setLong(2, idPessoaGerado);
                    pstmTelefone.executeUpdate();
                }
            }

            conn.commit();
            System.out.println("Cliente salvo com sucesso!");

        } catch (SQLException e) {
            try {
                if(conn != null) conn.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            System.out.println("Erro ao salvar cliente: " + e.getMessage());
        } finally {
            try {
                if(rs != null) rs.close();
                if(pstmPessoa != null) pstmPessoa.close();
                if(pstmCliente != null) pstmCliente.close();
                if(pstmTelefone != null) pstmTelefone.close();
                if(conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static ArrayList<Cliente> listarTodos() {
        String sql = "SELECT p.id, p.nome, p.cpf FROM pessoa p INNER JOIN cliente c ON p.id = c.pessoa_id";
        ArrayList<Cliente> lista = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;
        Cliente c = null;

        try {
            conn = Conexao.getConexao();
            pstm = conn.prepareStatement(sql);
            rs = pstm.executeQuery();

            while(rs.next()){
                c = new Cliente();
                c.setId(rs.getLong("id"));
                c.setNome(rs.getString("nome"));
                c.setCpf(rs.getString("cpf"));
                
                c.setTelefones(TelefoneDAO.listarPorPessoa(c.getId()));
                
                lista.add(c);
            }
            
            rs.close();
            pstm.close();
            conn.close();
        } catch (SQLException e) {
            System.out.println("Erro ao listar clientes: " + e.getMessage());
        }
        return lista;
    }

    public static Cliente buscarPorCpf(String cpf) {
        String sql = "SELECT p.id, p.nome, p.cpf FROM pessoa p INNER JOIN cliente c ON p.id = c.pessoa_id WHERE p.cpf = ?";
        Connection conn = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;
        Cliente c = null;

        try {
            conn = Conexao.getConexao();
            pstm = conn.prepareStatement(sql);
            pstm.setString(1, cpf);
            rs = pstm.executeQuery();

            if(rs.next()){
                c = new Cliente();
                c.setId(rs.getLong("id"));
                c.setNome(rs.getString("nome"));
                c.setCpf(rs.getString("cpf"));
                
                c.setTelefones(TelefoneDAO.listarPorPessoa(c.getId()));
            }

            rs.close();
            pstm.close();
            conn.close();
        } catch (SQLException e) {
            System.out.println("Erro ao buscar por CPF: " + e.getMessage());
        }
        return c;
    }

    public static Cliente buscarPorId(long id) {
        String sql = "SELECT p.id, p.nome, p.cpf FROM pessoa p INNER JOIN cliente c ON p.id = c.pessoa_id WHERE p.id = ?";
        Connection conn = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;
        Cliente c = null;

        try {
            conn = Conexao.getConexao();
            pstm = conn.prepareStatement(sql);
            pstm.setLong(1, id);
            rs = pstm.executeQuery();

            if(rs.next()){
                c = new Cliente();
                c.setId(rs.getLong("id"));
                c.setNome(rs.getString("nome"));
                c.setCpf(rs.getString("cpf"));
                
                c.setTelefones(TelefoneDAO.listarPorPessoa(c.getId()));
            }

            rs.close();
            pstm.close();
            conn.close();
        } catch (SQLException e) {
            System.out.println("Erro ao buscar por ID: " + e.getMessage());
        }
        return c;
    }

    public static void modificar(Cliente cliente) {
        String sql = "UPDATE pessoa SET nome = ?, cpf = ? WHERE id = ?";
        Connection conn = null;
        PreparedStatement pstm = null;

        try {
            conn = Conexao.getConexao();
            pstm = conn.prepareStatement(sql);
            
            pstm.setString(1, cliente.getNome());
            pstm.setString(2, cliente.getCpf());
            pstm.setLong(3, cliente.getId());
            
            pstm.executeUpdate();
            System.out.println("Cliente modificado com sucesso!");

            pstm.close();
            conn.close();
        } catch (SQLException e) {
            System.out.println("Erro ao modificar cliente: " + e.getMessage());
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
            if(linhas > 0){
                System.out.println("Cliente deletado com sucesso!");
            } else {
                System.out.println("Cliente não encontrado.");
            }

            pstm.close();
            conn.close();
        } catch (SQLException e) {
            System.out.println("Erro ao deletar cliente: " + e.getMessage());
        }
    }

    public static ArrayList<Cliente> listarSemCompras() {
        String sql = "SELECT p.id, p.nome, p.cpf " +
                     "FROM pessoa p " +
                     "INNER JOIN cliente c ON p.id = c.pessoa_id " +
                     "WHERE c.pessoa_id NOT IN (SELECT cliente_id FROM venda WHERE cliente_id IS NOT NULL)";
        ArrayList<Cliente> lista = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;
        Cliente c = null;

        try {
            conn = Conexao.getConexao();
            pstm = conn.prepareStatement(sql);
            rs = pstm.executeQuery();

            while(rs.next()){
                c = new Cliente();
                c.setId(rs.getLong("id"));
                c.setNome(rs.getString("nome"));
                c.setCpf(rs.getString("cpf"));
                
                c.setTelefones(TelefoneDAO.listarPorPessoa(c.getId()));
                
                lista.add(c);
            }
            
            rs.close();
            pstm.close();
            conn.close();
        } catch (SQLException e) {
            System.out.println("Erro ao listar clientes sem compras: " + e.getMessage());
        }
        return lista;
    }
}