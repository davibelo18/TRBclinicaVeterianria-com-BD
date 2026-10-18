
package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.Tutor;

public class TutorDBDAO implements TutorDAO {

    @Override
    public void insere(Tutor tutor) throws SQLException {
        String sql = """
            INSERT INTO tutor
                (nome, cpf, telefone, email, endereco)
            VALUES (?, ?, ?, ?, ?)
            RETURNING id_tutor
            """;

        try (Connection con = Conexao.getConexao();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setString(1, tutor.getNome());
            stmt.setString(2, tutor.getCpf());
            stmt.setString(3, tutor.getTelefone());
            stmt.setString(4, tutor.getEmail());
            stmt.setString(5, tutor.getEndereco());

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    tutor.setTutorID(rs.getInt("id_tutor"));
                }
            }
        }
    }

    @Override
    public void atualiza(Tutor tutor) throws SQLException {
        String sql = """
            UPDATE tutor
            SET nome = ?, cpf = ?, telefone = ?,
                email = ?, endereco = ?
            WHERE id_tutor = ? """;

        try (Connection con = Conexao.getConexao();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setString(1, tutor.getNome());
            stmt.setString(2, tutor.getCpf());
            stmt.setString(3, tutor.getTelefone());
            stmt.setString(4, tutor.getEmail());
            stmt.setString(5, tutor.getEndereco());
            stmt.setInt(6, tutor.getTutorID());

            stmt.executeUpdate();
        }
    }

    @Override
    public void remove(Tutor tutor) throws SQLException {
        String sql = "DELETE FROM tutor WHERE id_tutor = ?";

        try (Connection con = Conexao.getConexao();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setInt(1, tutor.getTutorID());
            stmt.executeUpdate();
        }
    }

    @Override
    public Tutor buscaPorCodigo(int tutorID) throws SQLException {
        String sql = "SELECT * FROM tutor WHERE id_tutor = ?";

        try (Connection con = Conexao.getConexao();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setInt(1, tutorID);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return criarTutor(rs);
                }
            }
        }

        return null;
    }

    @Override
    public List<Tutor> listaTodos() throws SQLException {
        String sql = "SELECT * FROM tutor ORDER BY id_tutor";
        List<Tutor> tutores = new ArrayList<>();

        try (Connection con = Conexao.getConexao();
             PreparedStatement stmt = con.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                tutores.add(criarTutor(rs));
            }
        }

        return tutores;
    }

    private Tutor criarTutor(ResultSet rs) throws SQLException {
        Tutor tutor = new Tutor();

        tutor.setTutorID(rs.getInt("id_tutor"));
        tutor.setNome(rs.getString("nome"));
        tutor.setCpf(rs.getString("cpf"));
        tutor.setTelefone(rs.getString("telefone"));
        tutor.setEmail(rs.getString("email"));
        tutor.setEndereco(rs.getString("endereco"));

        return tutor;
    }
}
