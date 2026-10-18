
package dao;

import java.sql.SQLException;
import java.util.List;
import model.Veterinario;

public interface VeterinarioDAO {

    void insere(Veterinario veterinario) throws SQLException;

    void atualiza(Veterinario veterinario) throws SQLException;

    void remove(Veterinario veterinario) throws SQLException;

   Veterinario buscaPorCodigo(int veterinarioId) throws SQLException;

    List<Veterinario> listaTodos() throws SQLException;
}
