import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class TelefoneDAO {

    public static void salvar(Telefone telefone) {
        String sql = "INSERT INTO telefone (numero, pessoa_id) VALUES (?, ?)";
        Connection conn = null;
        PreparedStatement pstm = null;

        try {
            conn = Conexao.getConexao();
            pstm = conn.prepareStatement(sql);
            
            pstm.setString(1, telefone.getNumero());
            pstm.setLong(2, telefone.getIdPessoa());
            
            pstm.execute();
            System.out.println("Telefone salvo com sucesso!");

            pstm.close();
            conn.close();
        } catch (SQLException e) {
            System.out.println("Erro ao salvar telefone: " + e.getMessage());
        }
    }

    public static ArrayList<Telefone> listarTodos() {
        String sql = "SELECT * FROM telefone";
        ArrayList<Telefone> lista = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;
        Telefone t = null;

        try {
            conn = Conexao.getConexao();
            pstm = conn.prepareStatement(sql);
            rs = pstm.executeQuery();

            while(rs.next()){
                t = new Telefone(
                    rs.getLong("id"),
                    rs.getLong("pessoa_id"),
                    rs.getString("numero")
                );
                lista.add(t);
            }
            
            rs.close();
            pstm.close();
            conn.close();
        } catch (SQLException e) {
            System.out.println("Erro ao listar telefones: " + e.getMessage());
        }
        return lista;
    }

    public static ArrayList<Telefone> listarPorPessoa(long idPessoa) {
        String sql = "SELECT * FROM telefone WHERE pessoa_id = ?";
        ArrayList<Telefone> lista = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;
        Telefone t = null;

        try {
            conn = Conexao.getConexao();
            pstm = conn.prepareStatement(sql);
            pstm.setLong(1, idPessoa);
            rs = pstm.executeQuery();

            while(rs.next()){
                t = new Telefone(
                    rs.getLong("id"),
                    rs.getLong("pessoa_id"),
                    rs.getString("numero")
                );
                lista.add(t);
            }
            
            rs.close();
            pstm.close();
            conn.close();
        } catch (SQLException e) {
            System.out.println("Erro ao listar telefones da pessoa: " + e.getMessage());
        }
        return lista;
    }

    public static Telefone buscarPorId(long id) {
        String sql = "SELECT * FROM telefone WHERE id = ?";
        Connection conn = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;
        Telefone t = null;

        try {
            conn = Conexao.getConexao();
            pstm = conn.prepareStatement(sql);
            pstm.setLong(1, id);
            rs = pstm.executeQuery();

            if(rs.next()){
                t = new Telefone(
                    rs.getLong("id"),
                    rs.getLong("pessoa_id"),
                    rs.getString("numero")
                );
            }

            rs.close();
            pstm.close();
            conn.close();
        } catch (SQLException e) {
            System.out.println("Erro ao buscar telefone por ID: " + e.getMessage());
        }
        return t;
    }

    public static void modificar(Telefone telefone) {
        String sql = "UPDATE telefone SET numero = ? WHERE id = ?";
        Connection conn = null;
        PreparedStatement pstm = null;

        try {
            conn = Conexao.getConexao();
            pstm = conn.prepareStatement(sql);
            
            pstm.setString(1, telefone.getNumero());
            pstm.setLong(2, telefone.getId());
            
            pstm.executeUpdate();
            System.out.println("Telefone atualizado com sucesso!");

            pstm.close();
            conn.close();
        } catch (SQLException e) {
            System.out.println("Erro ao modificar telefone: " + e.getMessage());
        }
    }

    public static void deletar(long id) {
        String sql = "DELETE FROM telefone WHERE id = ?";
        Connection conn = null;
        PreparedStatement pstm = null;

        try {
            conn = Conexao.getConexao();
            pstm = conn.prepareStatement(sql);
            pstm.setLong(1, id);
            
            int linhas = pstm.executeUpdate();
            if(linhas > 0){
                System.out.println("Telefone deletado com sucesso!");
            } else {
                System.out.println("Telefone não encontrado.");
            }

            pstm.close();
            conn.close();
        } catch (SQLException e) {
            System.out.println("Erro ao deletar telefone: " + e.getMessage());
        }
    }
}