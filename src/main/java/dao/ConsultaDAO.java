package dao;

import java.sql.SQLException;
import java.util.List;
import model.Consulta;

public interface ConsultaDAO {

    void insere(Consulta consulta) throws SQLException;

    void atualiza(Consulta consulta) throws SQLException;

    void remove(Consulta consulta) throws SQLException;

    Consulta buscaPorCodigo(int consultaID) throws SQLException;

    List<Consulta> listaTodos() throws SQLException;

    List<Consulta> listaPorAnimal(int animalID) throws SQLException;

    List<Consulta> listaPorVeterinario(int veterinarioID) throws SQLException;
}



