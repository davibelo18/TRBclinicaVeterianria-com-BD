package dao;

import model.Consulta;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ConsultaDBDAO implements ConsultaDAO {

    // Consulta base com os nomes do animal e do veterinário
    private static final String SELECT_BASE = """
        SELECT c.*, a.nome AS nome_animal, v.nome AS nome_veterinario
        FROM consulta c
        JOIN animal a ON a.id_animal = c.id_animal
        JOIN veterinario v ON v.id_veterinario = c.id_veterinario
        """;

    @Override
    public void insere(Consulta consulta) throws SQLException {
        String sql = """
            INSERT INTO consulta
                (id_animal, id_veterinario, data_hora, motivo, diagnostico, status)
            VALUES (?, ?, ?, ?, ?, ?)
            RETURNING id_consulta
            """;

        try (Connection con = Conexao.getConexao();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            preencherParametros(stmt, consulta);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    consulta.setConsultaID(rs.getInt("id_consulta"));
                }
            }
        }
    }

    @Override
    public void atualiza(Consulta consulta) throws SQLException {
        String sql = """
            UPDATE consulta
            SET id_animal = ?, id_veterinario = ?, data_hora = ?,
                motivo = ?, diagnostico = ?, status = ?
            WHERE id_consulta = ?
            """;

        try (Connection con = Conexao.getConexao();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            preencherParametros(stmt, consulta);
            stmt.setInt(7, consulta.getConsultaID());
            stmt.executeUpdate();
        }
    }

    @Override
    public void remove(Consulta consulta) throws SQLException {
        String sql = "DELETE FROM consulta WHERE id_consulta = ?";

        try (Connection con = Conexao.getConexao();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setInt(1, consulta.getConsultaID());
            stmt.executeUpdate();
        }
    }

    @Override
    public Consulta buscaPorCodigo(int consultaID) throws SQLException {
        String sql = SELECT_BASE + " WHERE c.id_consulta = ?";

        try (Connection con = Conexao.getConexao();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setInt(1, consultaID);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return criarConsulta(rs);
                }
            }
        }
        return null;
    }

    @Override
    public List<Consulta> listaTodos() throws SQLException {
        String sql = SELECT_BASE + " ORDER BY c.data_hora DESC";
        List<Consulta> consultas = new ArrayList<>();

        try (Connection con = Conexao.getConexao();
             PreparedStatement stmt = con.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                consultas.add(criarConsulta(rs));
            }
        }
        return consultas;
    }

    @Override
    public List<Consulta> listaPorAnimal(int animalID) throws SQLException {
        String sql = SELECT_BASE + " WHERE c.id_animal = ? ORDER BY c.data_hora DESC";
        List<Consulta> consultas = new ArrayList<>();

        try (Connection con = Conexao.getConexao();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setInt(1, animalID);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    consultas.add(criarConsulta(rs));
                }
            }
        }
        return consultas;
    }
    @Override
    public List<Consulta> listaPorVeterinario(int veterinarioID) throws SQLException {
        String sql = SELECT_BASE + " WHERE c.id_veterinario = ? ORDER BY c.data_hora DESC";
        List<Consulta> consultas = new ArrayList<>();

        try (Connection con = Conexao.getConexao();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setInt(1, veterinarioID);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    consultas.add(criarConsulta(rs));
                }
            }
        }
        return consultas;
    }

    // Parâmetros 1 a 6, iguais no INSERT e no UPDATE
    private void preencherParametros(PreparedStatement stmt, Consulta consulta) throws SQLException {
        stmt.setInt(1, consulta.getAnimalID());
        stmt.setInt(2, consulta.getVeterinarioID());
        stmt.setTimestamp(3, Timestamp.valueOf(consulta.getDataHora()));
        stmt.setString(4, consulta.getMotivo());
        stmt.setString(5, vazioParaNull(consulta.getDiagnostico()));
        stmt.setString(6, consulta.getStatus());
    }

    private String vazioParaNull(String texto) {
        return (texto == null || texto.isBlank()) ? null : texto;
    }

    private Consulta criarConsulta(ResultSet rs) throws SQLException {
        Consulta c = new Consulta();
        c.setConsultaID(rs.getInt("id_consulta"));
        c.setAnimalID(rs.getInt("id_animal"));
        c.setVeterinarioID(rs.getInt("id_veterinario"));
        c.setDataHora(rs.getTimestamp("data_hora").toLocalDateTime());
        c.setMotivo(rs.getString("motivo"));
        c.setDiagnostico(rs.getString("diagnostico"));
        c.setStatus(rs.getString("status"));
        c.setNomeAnimal(rs.getString("nome_animal"));
        c.setNomeVeterinario(rs.getString("nome_veterinario"));
        return c;
    }
}