
package dao;

import java.sql.SQLException;
import java.util.List;
import model.Tutor;

public interface TutorDAO {

    void insere(Tutor tutor) throws SQLException;

    void atualiza(Tutor tutor) throws SQLException;

    void remove(Tutor tutor) throws SQLException;

    Tutor buscaPorCodigo(int tutorID) throws SQLException;

    List<Tutor> listaTodos() throws SQLException;
}
