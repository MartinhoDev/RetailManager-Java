import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class FornecedorDAO {

    public static void salvar(Fornecedor fornecedor) {
        String sql = "INSERT INTO fornecedor (nome_fantasia, cnpj, telefone_contato, telefone_telegram) VALUES (?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement pstm = null;

        try {
            conn = Conexao.getConexao();
            pstm = conn.prepareStatement(sql);
            
            pstm.setString(1, fornecedor.getNomeFantasia());
            pstm.setString(2, fornecedor.getCnpj());
            pstm.setString(3, fornecedor.getTelefoneContato());
            pstm.setString(4, fornecedor.getTelefoneTelegram());
            
            pstm.execute();
            System.out.println("Fornecedor salvo com sucesso!");

            pstm.close();
            conn.close();
        } catch (SQLException e) {
            System.out.println("Erro ao salvar fornecedor: " + e.getMessage());
        }
    }

    public static ArrayList<Fornecedor> listarTodos() {
        String sql = "SELECT * FROM fornecedor";
        ArrayList<Fornecedor> lista = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;
        Fornecedor f = null;

        try {
            conn = Conexao.getConexao();
            pstm = conn.prepareStatement(sql);
            rs = pstm.executeQuery();

            while(rs.next()){
                f = new Fornecedor(
                    rs.getLong("id"),
                    rs.getString("cnpj"),
                    rs.getString("nome_fantasia"),
                    rs.getString("telefone_telegram"),
                    rs.getString("telefone_contato")
                );
                lista.add(f);
            }
            
            rs.close();
            pstm.close();
            conn.close();
        } catch (SQLException e) {
            System.out.println("Erro ao listar fornecedores: " + e.getMessage());
        }
        return lista;
    }

    public static Fornecedor buscarPorId(long id) {
        String sql = "SELECT * FROM fornecedor WHERE id = ?";
        Connection conn = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;
        Fornecedor f = null;

        try {
            conn = Conexao.getConexao();
            pstm = conn.prepareStatement(sql);
            pstm.setLong(1, id);
            rs = pstm.executeQuery();

            if(rs.next()){
                f = new Fornecedor(
                    rs.getLong("id"),
                    rs.getString("cnpj"),
                    rs.getString("nome_fantasia"),
                    rs.getString("telefone_telegram"),
                    rs.getString("telefone_contato")
                );
            }

            rs.close();
            pstm.close();
            conn.close();
        } catch (SQLException e) {
            System.out.println("Erro ao buscar fornecedor por ID: " + e.getMessage());
        }
        return f;
    }

    public static Fornecedor buscarPorCnpj(String cnpj) {
        String sql = "SELECT * FROM fornecedor WHERE cnpj = ?";
        Connection conn = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;
        Fornecedor f = null;

        try {
            conn = Conexao.getConexao();
            pstm = conn.prepareStatement(sql);
            pstm.setString(1, cnpj);
            rs = pstm.executeQuery();

            if(rs.next()){
                f = new Fornecedor(
                    rs.getLong("id"),
                    rs.getString("cnpj"),
                    rs.getString("nome_fantasia"),
                    rs.getString("telefone_telegram"),
                    rs.getString("telefone_contato")
                );
            }

            rs.close();
            pstm.close();
            conn.close();
        } catch (SQLException e) {
            System.out.println("Erro ao buscar fornecedor por CNPJ: " + e.getMessage());
        }
        return f;
    }

    public static void modificar(Fornecedor fornecedor) {
        String sql = "UPDATE fornecedor SET nome_fantasia = ?, cnpj = ?, telefone_contato = ?, telefone_telegram = ? WHERE id = ?";
        Connection conn = null;
        PreparedStatement pstm = null;

        try {
            conn = Conexao.getConexao();
            pstm = conn.prepareStatement(sql);
            
            pstm.setString(1, fornecedor.getNomeFantasia());
            pstm.setString(2, fornecedor.getCnpj());
            pstm.setString(3, fornecedor.getTelefoneContato());
            pstm.setString(4, fornecedor.getTelefoneTelegram());
            pstm.setLong(5, fornecedor.getId());
            
            pstm.executeUpdate();
            System.out.println("Fornecedor modificado com sucesso!");

            pstm.close();
            conn.close();
        } catch (SQLException e) {
            System.out.println("Erro ao modificar fornecedor: " + e.getMessage());
        }
    }

    public static void deletar(long id) {
        String sql = "DELETE FROM fornecedor WHERE id = ?";
        Connection conn = null;
        PreparedStatement pstm = null;

        try {
            conn = Conexao.getConexao();
            pstm = conn.prepareStatement(sql);
            pstm.setLong(1, id);
            
            int linhas = pstm.executeUpdate();
            if(linhas > 0) System.out.println("Fornecedor deletado com sucesso!");
            else System.out.println("Fornecedor não encontrado.");

            pstm.close();
            conn.close();
        } catch (SQLException e) {
            System.out.println("Erro ao deletar fornecedor: " + e.getMessage());
        }
    }
}