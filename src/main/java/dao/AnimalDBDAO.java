package dao;

import model.Animal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AnimalDBDAO implements AnimalDAO {

    @Override
    public void insere(Animal animal) throws SQLException {
        String sql = "INSERT INTO animal (nome, especie, raca, data_nascimento, sexo, id_tutor, id_veterinario) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection con = Conexao.getConexao();
             PreparedStatement stmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            preencherParametros(stmt, animal);
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    animal.setAnimalID(rs.getInt("id_animal"));
                }
            }
        }
    }

    @Override
    public void atualiza(Animal animal) throws SQLException {
        String sql = "UPDATE animal SET nome = ?, especie = ?, raca = ?, "
                + "data_nascimento = ?, sexo = ?, id_tutor = ?, id_veterinario = ? WHERE id_animal = ?";

        try (Connection con = Conexao.getConexao();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            preencherParametros(stmt, animal);
            stmt.setInt(8, animal.getAnimalID());
            stmt.executeUpdate();
        }
    }

    @Override
    public void remove(Animal animal) throws SQLException {
        String sql = "DELETE FROM animal WHERE id_animal = ?";

        try (Connection con = Conexao.getConexao();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setInt(1, animal.getAnimalID());
            stmt.executeUpdate();
        }
    }

    @Override
    public Animal buscaPorCodigo(int animalId) throws SQLException {
        String sql = "SELECT * FROM animal WHERE id_animal = ?";

        try (Connection con = Conexao.getConexao();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setInt(1, animalId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return criarAnimal(rs);
                }
            }
        }
        return null;
    }

    @Override
    public List<Animal> listaTodos() throws SQLException {
        String sql = "SELECT * FROM animal ORDER BY nome";
        List<Animal> animais = new ArrayList<>();

        try (Connection con = Conexao.getConexao();
             PreparedStatement stmt = con.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                animais.add(criarAnimal(rs));
            }
        }
        return animais;
    }

    @Override
    public List<Animal> listaPorTutor(int tutorId) throws SQLException {
        String sql = "SELECT * FROM animal WHERE id_tutor = ? ORDER BY nome";
        List<Animal> animais = new ArrayList<>();

        try (Connection con = Conexao.getConexao();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setInt(1, tutorId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    animais.add(criarAnimal(rs));
                }
            }
        }
        return animais;
    }
    @Override
    public List<Animal> listaPorVeterinario(int veterinarioId) throws SQLException {
        String sql = "SELECT * FROM animal WHERE id_veterinario = ? ORDER BY nome";
        List<Animal> animais = new ArrayList<>();

        try (Connection con = Conexao.getConexao();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setInt(1, veterinarioId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    animais.add(criarAnimal(rs));
                }
            }
        }
        return animais;
    }

    // Parâmetros 1 a 6, iguais no INSERT e no UPDATE
    private void preencherParametros(PreparedStatement stmt, Animal animal) throws SQLException {
        stmt.setString(1, animal.getNome());
        stmt.setString(2, animal.getEspecie());
        stmt.setString(3, animal.getRaca());

        if (animal.getDataNascimento() != null) {
            stmt.setDate(4, Date.valueOf(animal.getDataNascimento()));
        } else {
            stmt.setNull(4, Types.DATE);
        }

        stmt.setString(5, animal.getSexo());
        stmt.setInt(6, animal.getTutorID());

        if (animal.getVeterinarioID() != null) {
            stmt.setInt(7, animal.getVeterinarioID());
        } else {
            stmt.setNull(7, Types.INTEGER);
        }
    }

    private Animal criarAnimal(ResultSet rs) throws SQLException {
        Animal a = new Animal();
        a.setAnimalID(rs.getInt("id_animal"));
        a.setNome(rs.getString("nome"));
        a.setEspecie(rs.getString("especie"));
        a.setRaca(rs.getString("raca"));

        Date nascimento = rs.getDate("data_nascimento");
        if (nascimento != null) {
            a.setDataNascimento(nascimento.toLocalDate());
        }

        a.setSexo(rs.getString("sexo"));
        a.setTutorID(rs.getInt("id_tutor"));
        int idVeterinario = rs.getInt("id_veterinario");
        a.setVeterinarioID(rs.wasNull() ? null : idVeterinario);

        return a;
    }
}