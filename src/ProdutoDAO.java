import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ProdutoDAO {

    public static void salvar(Produto produto) {
        String sql = "INSERT INTO produto (nome, qtd_estoque, preco_atual) VALUES (?, ?, ?)";
        Connection conn = null;
        PreparedStatement pstm = null;

        try {
            conn = Conexao.getConexao();
            pstm = conn.prepareStatement(sql);
            
            pstm.setString(1, produto.getNome());
            pstm.setLong(2, produto.getQtdEstoque());
            pstm.setDouble(3, produto.getPrecoAtual());
            
            pstm.execute();
            System.out.println("Produto salvo com sucesso!");

            pstm.close();
            conn.close();
        } catch (SQLException e) {
            System.out.println("Erro ao salvar produto: " + e.getMessage());
        }
    }

    public static ArrayList<Produto> listarTodos() {
        String sql = "SELECT * FROM produto";
        ArrayList<Produto> lista = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;
        Produto p = null;

        try {
            conn = Conexao.getConexao();
            pstm = conn.prepareStatement(sql);
            rs = pstm.executeQuery();

            while(rs.next()){
                p = new Produto(
                    rs.getLong("id"),
                    rs.getLong("qtd_estoque"),
                    rs.getString("nome"),
                    rs.getDouble("preco_atual")
                );
                lista.add(p);
            }
            
            rs.close();
            pstm.close();
            conn.close();
        } catch (SQLException e) {
            System.out.println("Erro ao listar produtos: " + e.getMessage());
        }
        return lista;
    }

    public static Produto buscarPorId(long id) {
        String sql = "SELECT * FROM produto WHERE id = ?";
        Connection conn = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;
        Produto p = null;

        try {
            conn = Conexao.getConexao();
            pstm = conn.prepareStatement(sql);
            pstm.setLong(1, id);
            rs = pstm.executeQuery();

            if(rs.next()){
                p = new Produto(
                    rs.getLong("id"),
                    rs.getLong("qtd_estoque"),
                    rs.getString("nome"),
                    rs.getDouble("preco_atual")
                );
            }

            rs.close();
            pstm.close();
            conn.close();
        } catch (SQLException e) {
            System.out.println("Erro ao buscar produto por ID: " + e.getMessage());
        }
        return p;
    }

    public static ArrayList<Produto> buscarPorNome(String nome) {
        String sql = "SELECT * FROM produto WHERE nome LIKE ?";
        ArrayList<Produto> lista = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;
        Produto p = null;

        try {
            conn = Conexao.getConexao();
            pstm = conn.prepareStatement(sql);
            pstm.setString(1, "%" + nome + "%");
            rs = pstm.executeQuery();

            while(rs.next()){
                p = new Produto(
                    rs.getLong("id"),
                    rs.getLong("qtd_estoque"),
                    rs.getString("nome"),
                    rs.getDouble("preco_atual")
                );
                lista.add(p);
            }
            
            rs.close();
            pstm.close();
            conn.close();
        } catch (SQLException e) {
            System.out.println("Erro ao buscar produto por nome: " + e.getMessage());
        }
        return lista;
    }

    public static void modificar(Produto produto) {
        String sql = "UPDATE produto SET nome = ?, qtd_estoque = ?, preco_atual = ? WHERE id = ?";
        Connection conn = null;
        PreparedStatement pstm = null;

        try {
            conn = Conexao.getConexao();
            pstm = conn.prepareStatement(sql);
            
            pstm.setString(1, produto.getNome());
            pstm.setLong(2, produto.getQtdEstoque());
            pstm.setDouble(3, produto.getPrecoAtual());
            pstm.setLong(4, produto.getId());
            
            pstm.executeUpdate();
            System.out.println("Produto atualizado com sucesso!");

            pstm.close();
            conn.close();
        } catch (SQLException e) {
            System.out.println("Erro ao modificar produto: " + e.getMessage());
        }
    }

    public static void deletar(long id) {
        String sql = "DELETE FROM produto WHERE id = ?";
        Connection conn = null;
        PreparedStatement pstm = null;

        try {
            conn = Conexao.getConexao();
            pstm = conn.prepareStatement(sql);
            pstm.setLong(1, id);
            
            int linhas = pstm.executeUpdate();
            if (linhas > 0) System.out.println("Produto deletado com sucesso!");
            else System.out.println("Produto não encontrado.");

            pstm.close();
            conn.close();
        } catch (SQLException e) {
            System.out.println("Erro ao deletar produto: " + e.getMessage());
        }
    }

    public static ArrayList<Produto> listarMaisCarosQueMedia() {
        String sql = "SELECT * FROM produto WHERE preco_atual > (SELECT AVG(preco_atual) FROM produto)";
        ArrayList<Produto> lista = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;
        Produto p = null;

        try {
            conn = Conexao.getConexao();
            pstm = conn.prepareStatement(sql);
            rs = pstm.executeQuery();

            while(rs.next()){
                p = new Produto(
                    rs.getLong("id"),
                    rs.getLong("qtd_estoque"),
                    rs.getString("nome"),
                    rs.getDouble("preco_atual")
                );
                lista.add(p);
            }
            
            rs.close();
            pstm.close();
            conn.close();
        } catch (SQLException e) {
            System.out.println("Erro ao listar produtos acima da média: " + e.getMessage());
        }
        return lista;
    }

    public static ArrayList<Produto> listarEstoqueBaixo() {
        String sql = "SELECT * FROM produto WHERE qtd_estoque < 20";
        ArrayList<Produto> lista = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;
        Produto p = null;

        try {
            conn = Conexao.getConexao();
            pstm = conn.prepareStatement(sql);
            rs = pstm.executeQuery();

            while(rs.next()){
                p = new Produto(
                    rs.getLong("id"),
                    rs.getLong("qtd_estoque"),
                    rs.getString("nome"),
                    rs.getDouble("preco_atual")
                );
                lista.add(p);
            }
            
            rs.close();
            pstm.close();
            conn.close();
        } catch (SQLException e) {
            System.out.println("Erro ao listar produtos com estoque baixo: " + e.getMessage());
        }
        return lista;
    }
}