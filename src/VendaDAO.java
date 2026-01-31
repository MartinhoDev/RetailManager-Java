import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.sql.Date;

public class VendaDAO {

    public static void salvar(Venda venda) {
        String sqlVenda = "INSERT INTO venda (cliente_id, funcionario_id, data_hora, comissao) VALUES (?, ?, ?, ?)";
        String sqlItem = "INSERT INTO item_produto (venda_id, produto_id, qtd, preco_praticado) VALUES (?, ?, ?, ?)";
        String sqlEstoque = "UPDATE produto SET qtd_estoque = qtd_estoque - ? WHERE id = ?";
        Connection conn = null;
        PreparedStatement pstmVenda = null;
        PreparedStatement pstmItem = null;
        PreparedStatement pstmEstoque = null;
        ResultSet rs = null;
        long idVendaGerada = 0;

        try {
            conn = Conexao.getConexao();
            conn.setAutoCommit(false);

            pstmVenda = conn.prepareStatement(sqlVenda, Statement.RETURN_GENERATED_KEYS);
            pstmVenda.setLong(1, venda.getIdCliente());
            pstmVenda.setLong(2, venda.getIdFuncionario());
            pstmVenda.setTimestamp(3, Timestamp.valueOf(venda.getDataHora()));
            pstmVenda.setFloat(4, venda.getComissao());
            pstmVenda.executeUpdate();

            rs = pstmVenda.getGeneratedKeys();
            if(rs.next()) idVendaGerada = rs.getLong(1);

            pstmItem = conn.prepareStatement(sqlItem);
            pstmEstoque = conn.prepareStatement(sqlEstoque);

            for(ItemVenda item : venda.getItens()){
                pstmItem.setLong(1, idVendaGerada);
                pstmItem.setLong(2, item.getIdProduto());
                pstmItem.setLong(3, item.getQtd());
                pstmItem.setDouble(4, item.getPrecoPraticado());
                pstmItem.executeUpdate();

                pstmEstoque.setLong(1, item.getQtd());
                pstmEstoque.setLong(2, item.getIdProduto());
                pstmEstoque.executeUpdate();
            }

            conn.commit();
            System.out.println("Venda realizada com sucesso! ID: " + idVendaGerada);

        } catch (SQLException e) {
            try { if(conn != null) conn.rollback(); } catch (SQLException ex) {}
            System.out.println("Erro ao realizar venda: " + e.getMessage());
        } finally {
            try {
                if(rs != null) rs.close();
                if(pstmVenda != null) pstmVenda.close();
                if(pstmItem != null) pstmItem.close();
                if(pstmEstoque != null) pstmEstoque.close();
                if(conn != null) conn.close();
            } catch (SQLException e) { e.printStackTrace(); }
        }
    }

    public static ArrayList<Venda> listarTodas() {
        String sql = "SELECT * FROM venda ORDER BY data_hora DESC";
        ArrayList<Venda> lista = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;
        Venda v;

        try {
            conn = Conexao.getConexao();
            pstm = conn.prepareStatement(sql);
            rs = pstm.executeQuery();

            while(rs.next()){
                v = new Venda();
                v.setId(rs.getLong("id"));
                v.setIdCliente(rs.getLong("cliente_id"));
                v.setIdFuncionario(rs.getLong("funcionario_id"));
                v.setDataHora(rs.getTimestamp("data_hora").toLocalDateTime());
                v.setComissao(rs.getFloat("comissao"));
                lista.add(v);
            }
        } catch (SQLException e) {
            System.out.println("Erro ao listar vendas: " + e.getMessage());
        } finally {
            try { if(conn!=null) conn.close(); } catch(SQLException e){}
        }
        return lista;
    }

    public static Venda buscarPorId(long id) {
        String sqlVenda = "SELECT * FROM venda WHERE id = ?";
        String sqlItens = "SELECT * FROM item_produto WHERE venda_id = ?";
        Connection conn = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;
        Venda v = null;

        try {
            conn = Conexao.getConexao();
            
            pstm = conn.prepareStatement(sqlVenda);
            pstm.setLong(1, id);
            rs = pstm.executeQuery();

            if(rs.next()){
                v = new Venda();
                v.setId(rs.getLong("id"));
                v.setIdCliente(rs.getLong("cliente_id"));
                v.setIdFuncionario(rs.getLong("funcionario_id"));
                v.setDataHora(rs.getTimestamp("data_hora").toLocalDateTime());
                v.setComissao(rs.getFloat("comissao"));
            }
            rs.close();
            pstm.close();

            if(v != null){
                pstm = conn.prepareStatement(sqlItens);
                pstm.setLong(1, id);
                rs = pstm.executeQuery();
                
                while(rs.next()){
                    ItemVenda item = new ItemVenda(
                        rs.getLong("id"),
                        rs.getLong("produto_id"),
                        rs.getLong("venda_id"),
                        rs.getLong("qtd"),
                        rs.getDouble("preco_praticado")
                    );
                    v.adicionarItem(item);
                }
            }
            conn.close();
        } catch (SQLException e) {
            System.out.println("Erro ao buscar venda: " + e.getMessage());
        }
        return v;
    }

    public static Map<Date, Double> listarMediaVendasPorDia() {
        String sql = "SELECT DATE(v.data_hora) as dia, " +
                     "SUM(i.qtd * i.preco_praticado) / COUNT(DISTINCT v.id) as ticket_medio " +
                     "FROM venda v JOIN item_produto i ON v.id = i.venda_id " +
                     "GROUP BY dia ORDER BY dia DESC";
        
        Map<Date, Double> mapa = new HashMap<>();
        
        try(Connection conn = Conexao.getConexao();
            PreparedStatement pstm = conn.prepareStatement(sql);
            ResultSet rs = pstm.executeQuery()){
            
            while(rs.next()){
                mapa.put(rs.getDate("dia"), rs.getDouble("ticket_medio"));
            }
        } catch(Exception e){ e.printStackTrace(); }
        return mapa;
    }

    public static ArrayList<Venda> buscarPorMesAno(int mes, int ano) {
        String sql = "SELECT * FROM venda WHERE MONTH(data_hora) = ? AND YEAR(data_hora) = ?";
        ArrayList<Venda> lista = new ArrayList<>();
        try(Connection conn = Conexao.getConexao();
            PreparedStatement pstm = conn.prepareStatement(sql)){
            pstm.setInt(1, mes);
            pstm.setInt(2, ano);
            ResultSet rs = pstm.executeQuery();
            while(rs.next()){
                Venda v = new Venda();
                v.setId(rs.getLong("id"));
                v.setDataHora(rs.getTimestamp("data_hora").toLocalDateTime());
                lista.add(v);
            }
        } catch(Exception e){ e.printStackTrace(); }
        return lista;
    }

    public static String buscarDiaComMaisVendas() {
        String sql = "SELECT DAYNAME(data_hora) as dia, COUNT(*) as qtd FROM venda GROUP BY dia ORDER BY qtd DESC LIMIT 1";
        String dia = "Desconhecido";
        try(Connection conn = Conexao.getConexao();
            PreparedStatement pstm = conn.prepareStatement(sql);
            ResultSet rs = pstm.executeQuery()){
            if(rs.next()) dia = rs.getString("dia");
        } catch(Exception e){ e.printStackTrace(); }
        return dia;
    }

    public static Venda buscarMaiorVenda() {
        String sql = "SELECT v.id, SUM(i.qtd * i.preco_praticado) as total " +
                     "FROM venda v JOIN item_produto i ON v.id = i.venda_id " +
                     "GROUP BY v.id ORDER BY total DESC LIMIT 1";
        long idVenda = 0;
        try(Connection conn = Conexao.getConexao();
            PreparedStatement pstm = conn.prepareStatement(sql);
            ResultSet rs = pstm.executeQuery()){
            if(rs.next()) idVenda = rs.getLong("id");
        } catch(Exception e){ e.printStackTrace(); }
        
        if(idVenda > 0) return buscarPorId(idVenda);
        return null;
    }
}