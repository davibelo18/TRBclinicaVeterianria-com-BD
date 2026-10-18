package Controller;

import App.App;
import dao.AnimalDAO;
import dao.AnimalDBDAO;
import dao.TutorDAO;
import dao.TutorDBDAO;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Animal;
import model.Tutor;
import dao.VeterinarioDAO;
import dao.VeterinarioDBDAO;
import model.Veterinario;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class AnimalController {

    @FXML private TextField txtNome;
    @FXML private TextField txtEspecie;
    @FXML private TextField txtRaca;
    @FXML private DatePicker dpNascimento;
    @FXML private ComboBox<String> cbSexo;
    @FXML private ComboBox<Tutor> cbTutor;
    @FXML private TableView<Animal> tabela;
    @FXML private TableColumn<Animal, String> colNome;
    @FXML private TableColumn<Animal, String> colEspecie;
    @FXML private TableColumn<Animal, String> colRaca;
    @FXML private TableColumn<Animal, LocalDate> colNascimento;
    @FXML private TableColumn<Animal, String> colSexo;
    @FXML private TableColumn<Animal, String> colTutor;

    private final AnimalDAO dao = new AnimalDBDAO();
    private final TutorDAO tutorDAO = new TutorDBDAO();
    private final Map<Integer, String> nomesTutores = new HashMap<>();



    @FXML
    public void initialize() {
        somenteLetras(txtRaca);
        somenteLetras(txtNome);
        somenteLetras(txtEspecie);
        cbSexo.getItems().addAll("Macho", "Fêmea");
        colNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colEspecie.setCellValueFactory(new PropertyValueFactory<>("especie"));
        colRaca.setCellValueFactory(new PropertyValueFactory<>("raca"));
        colNascimento.setCellValueFactory(new PropertyValueFactory<>("dataNascimento"));
        colSexo.setCellValueFactory(new PropertyValueFactory<>("sexo"));
        // o Animal só guarda o ID do tutor então busca o nome
        colTutor.setCellValueFactory(c -> new SimpleStringProperty(
                nomesTutores.getOrDefault(c.getValue().getTutorID(), "?")));



        tabela.getSelectionModel().selectedItemProperty().addListener((obs, antigo, a) -> {
            if (a != null) {
                txtNome.setText(a.getNome());
                txtEspecie.setText(a.getEspecie());
                txtRaca.setText(a.getRaca());
                dpNascimento.setValue(a.getDataNascimento());
                cbSexo.setValue(a.getSexo());
                cbTutor.getItems().stream()
                        .filter(t -> t.getTutorID() == a.getTutorID())
                        .findFirst()
                        .ifPresent(cbTutor::setValue);
                cbVeterinario.setValue(null);
                if (a.getVeterinarioID() != null) {
                    cbVeterinario.getItems().stream()
                            .filter(v -> v != null && v.getId_veterinario() == a.getVeterinarioID())
                            .findFirst()
                            .ifPresent(cbVeterinario::setValue);
                }
            }
        });

        carregarTutores();
        carregarVeterinarios();
        onConsultar();
    }

    @FXML
    private void onInserir() {
        try {
            if (camposInvalidos()) return;
            Animal a = new Animal();
            lerCampos(a);
            dao.insere(a);
            onConsultar();
            onLimpar();
        } catch (Exception e) {
            erro(e);
        }
    }
    @FXML
    private void onConsultar() {
        try {
            tabela.getItems().setAll(dao.listaTodos());
        } catch (Exception e) {
            erro(e);
        }
    }

    @FXML
    private void onAtualizar() {
        Animal a = tabela.getSelectionModel().getSelectedItem();
        if (a == null) {
            aviso("Selecione um animal na tabela para atualizar.");
            return;
        }
        if (camposInvalidos()) return;
        try {
            lerCampos(a);
            dao.atualiza(a);
            onConsultar();
            onLimpar();
        } catch (Exception e) {
            erro(e);
        }
    }

    @FXML
    private void onExcluir() {
        Animal a = tabela.getSelectionModel().getSelectedItem();
        if (a == null) {
            aviso("Selecione um animal na tabela para excluir.");
            return;
        }
        Alert confirmacao = new Alert(Alert.AlertType.CONFIRMATION, "Excluir o animal " + a.getNome() + "?");
        if (confirmacao.showAndWait().orElse(ButtonType.CANCEL) != ButtonType.OK) return;

        try {
            dao.remove(a);
            onConsultar();
            onLimpar();
        } catch (Exception e) {
            erro(e);
        }
    }
    @FXML
    private void onLimpar() {
        txtNome.clear();
        txtEspecie.clear();
        txtRaca.clear();
        dpNascimento.setValue(null);
        cbSexo.setValue(null);
        cbTutor.setValue(null);
        tabela.getSelectionModel().clearSelection();
        cbVeterinario.setValue(null);
    }

    @FXML
    private void onIrParaTutores() {
        try {
            App.trocarTela("TutorView.fxml", "Clínica Veterinária - Tutores");
        } catch (Exception e) {
            erro(e);
        }
    }

    @FXML
    private void onIrParaVeterinarios() {
        try {
            App.trocarTela("VeterinarioView.fxml", "Clínica Veterinária - Veterinários");
        } catch (Exception e) {
            erro(e);
        }
    }
    @FXML private ComboBox<Veterinario> cbVeterinario;
    private final VeterinarioDAO veterinarioDAO = new VeterinarioDBDAO();

    // Aceita só letras
    private void somenteLetras(TextField campo) {
        campo.setTextFormatter(new TextFormatter<String>(change ->
                change.getControlNewText().matches("[\\p{L} '\\-]*") ? change : null));
    }



    private void carregarTutores() {
        try {
            cbTutor.getItems().setAll(tutorDAO.listaTodos());
            nomesTutores.clear();
            for (Tutor t : cbTutor.getItems()) {
                nomesTutores.put(t.getTutorID(), t.getNome());
            }
        } catch (Exception e) {
            erro(e);
        }
    }

    private void carregarVeterinarios() {
        try {
            cbVeterinario.getItems().clear();
            cbVeterinario.getItems().add(null); // opção vazia: animal sem veterinário
            cbVeterinario.getItems().addAll(veterinarioDAO.listaTodos());
        } catch (Exception e) {
            erro(e);
        }
    }
    private void lerCampos(Animal a) {
        a.setNome(txtNome.getText().trim());
        a.setEspecie(txtEspecie.getText().trim());
        a.setRaca(txtRaca.getText().isBlank() ? null : txtRaca.getText().trim());
        a.setDataNascimento(dpNascimento.getValue());
        a.setSexo(cbSexo.getValue());
        a.setTutorID(cbTutor.getValue().getTutorID());
        a.setVeterinarioID(cbVeterinario.getValue() != null
                ? cbVeterinario.getValue().getId_veterinario()
                : null);
    }

    private boolean camposInvalidos() {
        if (txtNome.getText().isBlank() || txtEspecie.getText().isBlank() || cbTutor.getValue() == null) {
            aviso("Nome, espécie e tutor são obrigatórios.");
            return true;
        }
        return false;
    }

    private void aviso(String mensagem) {
        new Alert(Alert.AlertType.WARNING, mensagem).showAndWait();
    }

    private void erro(Exception e) {
        e.printStackTrace();
        String msg = e.getMessage();
        if (e instanceof SQLException sql && "23503".equals(sql.getSQLState())) {
            msg = "Este veterinário possui animais ou consultas cadastrados e não pode ser excluído.";
        }
        new Alert(Alert.AlertType.ERROR, msg).showAndWait();
    }
}
