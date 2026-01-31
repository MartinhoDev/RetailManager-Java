import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;

public class FornecimentoDAO {

    public static void salvar(Fornecimento fornecimento) {
        String sqlInsert = "INSERT INTO fornecimento (fornecedor_id, produto_id, qtd, preco_custo, data_hora) VALUES (?, ?, ?, ?, ?)";
        String sqlUpdateEstoque = "UPDATE produto SET qtd_estoque = qtd_estoque + ? WHERE id = ?";
        Connection conn = null;
        PreparedStatement pstmInsert = null;
        PreparedStatement pstmUpdate = null;

        try {
            conn = Conexao.getConexao();
            conn.setAutoCommit(false); 

            pstmInsert = conn.prepareStatement(sqlInsert);
            pstmInsert.setLong(1, fornecimento.getIdFornecedor());
            pstmInsert.setLong(2, fornecimento.getIdProduto());
            pstmInsert.setLong(3, fornecimento.getQtd());
            pstmInsert.setDouble(4, fornecimento.getPrecoCusto());
            pstmInsert.setTimestamp(5, Timestamp.valueOf(fornecimento.getDataHora()));
            pstmInsert.executeUpdate();

            pstmUpdate = conn.prepareStatement(sqlUpdateEstoque);
            pstmUpdate.setLong(1, fornecimento.getQtd());
            pstmUpdate.setLong(2, fornecimento.getIdProduto());
            pstmUpdate.executeUpdate();

            conn.commit();
            System.out.println("Fornecimento registrado e estoque atualizado com sucesso!");

        } catch (SQLException e) {
            try { 
                if(conn != null) conn.rollback(); 
            } catch (SQLException ex) { ex.printStackTrace(); }
            System.out.println("Erro ao registrar fornecimento: " + e.getMessage());
        } finally {
            try {
                if(pstmInsert != null) pstmInsert.close();
                if(pstmUpdate != null) pstmUpdate.close();
                if(conn != null) conn.close();
            } catch (SQLException e) { e.printStackTrace(); }
        }
    }

    public static ArrayList<Fornecimento> listarTodos() {
        String sql = "SELECT * FROM fornecimento ORDER BY data_hora DESC";
        ArrayList<Fornecimento> lista = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;
        Fornecimento f;

        try {
            conn = Conexao.getConexao();
            pstm = conn.prepareStatement(sql);
            rs = pstm.executeQuery();

            while(rs.next()){
                f = new Fornecimento(
                    rs.getLong("id"),
                    rs.getLong("fornecedor_id"),
                    rs.getLong("produto_id"),
                    rs.getLong("qtd"),
                    rs.getDouble("preco_custo"),
                    rs.getTimestamp("data_hora").toLocalDateTime()
                );
                lista.add(f);
            }
        } catch (SQLException e) {
            System.out.println("Erro ao listar fornecimentos: " + e.getMessage());
        } finally {
            try {
                if(rs != null) rs.close();
                if(pstm != null) pstm.close();
                if(conn != null) conn.close();
            } catch (SQLException e) { e.printStackTrace(); }
        }
        return lista;
    }

    public static Fornecimento buscarPorId(long id) {
        String sql = "SELECT * FROM fornecimento WHERE id = ?";
        Connection conn = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;
        Fornecimento f = null;

        try {
            conn = Conexao.getConexao();
            pstm = conn.prepareStatement(sql);
            pstm.setLong(1, id);
            rs = pstm.executeQuery();

            if(rs.next()){
                f = new Fornecimento(
                    rs.getLong("id"),
                    rs.getLong("fornecedor_id"),
                    rs.getLong("produto_id"),
                    rs.getLong("qtd"),
                    rs.getDouble("preco_custo"),
                    rs.getTimestamp("data_hora").toLocalDateTime()
                );
            }
        } catch (SQLException e) {
            System.out.println("Erro ao buscar fornecimento: " + e.getMessage());
        } finally {
            try {
                if(rs != null) rs.close();
                if(pstm != null) pstm.close();
                if(conn != null) conn.close();
            } catch (SQLException e) { e.printStackTrace(); }
        }
        return f;
    }
}