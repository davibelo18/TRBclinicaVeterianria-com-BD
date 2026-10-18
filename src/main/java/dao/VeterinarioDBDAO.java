package dao;

import model.Veterinario;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VeterinarioDBDAO implements VeterinarioDAO {

    @Override
    public void insere(Veterinario veterinario) throws SQLException {
        String sql = "INSERT INTO veterinario (nome, crmv, telefone, email) VALUES (?, ?, ?, ?)";

        try (Connection con = Conexao.getConexao();
             PreparedStatement stmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, veterinario.getNome());
            stmt.setString(2, veterinario.getCrmv());
            stmt.setString(3, veterinario.getTelefone());
            stmt.setString(4, veterinario.getEmail());
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    veterinario.setId_veterinario(rs.getInt("id_veterinario"));
                }
            }
        }
    }

    @Override
    public void atualiza(Veterinario veterinario) throws SQLException {
        String sql = "UPDATE veterinario SET nome = ?, Crmv = ?, telefone = ?, email = ? "
                + "WHERE id_veterinario = ?";

        try (Connection con = Conexao.getConexao();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setString(1, veterinario.getNome());
            stmt.setString(2, veterinario.getCrmv());
            stmt.setString(3, veterinario.getTelefone());
            stmt.setString(4, veterinario.getEmail());
            stmt.setInt(5, veterinario.getId_veterinario());
            stmt.executeUpdate();
        }
    }

    @Override
    public void remove(Veterinario veterinario) throws SQLException {
        String sql = "DELETE FROM veterinario WHERE id_veterinario = ?";

        try (Connection con = Conexao.getConexao();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setInt(1, veterinario.getId_veterinario());
            stmt.executeUpdate();
        }
    }

    @Override
    public Veterinario buscaPorCodigo(int veterinarioId) throws SQLException {
        String sql = "SELECT * FROM veterinario WHERE id_veterinario = ?";

        try (Connection con = Conexao.getConexao();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setInt(1, veterinarioId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return criarVeterinario(rs);
                }
            }
        }
        return null; // não encontrado
    }

    @Override
    public List<Veterinario> listaTodos() throws SQLException {
        String sql = "SELECT * FROM veterinario ORDER BY nome";
        List<Veterinario> veterinarios = new ArrayList<>();

        try (Connection con = Conexao.getConexao();
             PreparedStatement stmt = con.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                veterinarios.add(criarVeterinario(rs));
            }
        }
        return veterinarios;
    }

    private Veterinario criarVeterinario(ResultSet rs) throws SQLException {
        Veterinario v = new Veterinario();
        v.setId_veterinario(rs.getInt("id_veterinario"));
        v.setNome(rs.getString("nome"));
        v.setCrmv(rs.getString("crmv"));
        v.setTelefone(rs.getString("telefone"));
        v.setEmail(rs.getString("email"));
        return v;
    }
}