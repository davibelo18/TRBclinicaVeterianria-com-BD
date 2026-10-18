package dao;

import java.sql.SQLException;
import java.util.List;
import model.Animal;

public interface AnimalDAO {

    void insere(Animal animal) throws SQLException;

    void atualiza(Animal animal) throws SQLException;

    void remove(Animal animal) throws SQLException;

    Animal buscaPorCodigo(int animalId) throws SQLException;

    List<Animal> listaTodos() throws SQLException;

    List<Animal> listaPorTutor(int tutorId) throws SQLException;

    List<Animal> listaPorVeterinario(int veterinarioId) throws SQLException;
}