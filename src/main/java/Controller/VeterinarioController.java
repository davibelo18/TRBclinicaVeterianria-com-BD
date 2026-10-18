package Controller;

import App.App;

import dao.AnimalDAO;
import dao.AnimalDBDAO;
import dao.TutorDAO;
import dao.TutorDBDAO;
import model.Animal;
import model.Tutor;
import java.util.HashMap;
import java.util.Map;
import dao.VeterinarioDAO;
import dao.VeterinarioDBDAO;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Veterinario;
import java.sql.SQLException;


public class VeterinarioController {

    //tabela de veterinários
    @FXML private TextField txtNome;
    @FXML private TextField txtCrmv;
    @FXML private TextField txtTelefone;
    @FXML private TextField txtEmail;
    @FXML private TableView<Veterinario> tabela;
    @FXML private TableColumn<Veterinario, String> colNome;
    @FXML private TableColumn<Veterinario, String> colCrmv;
    @FXML private TableColumn<Veterinario, String> colTelefone;
    @FXML private TableColumn<Veterinario, String> colEmail;

    // animais acompanhados consultas do veterinário selecionado
    @FXML private Label lblAnimais;
    @FXML private TableView<Animal> tabelaAnimais;
    @FXML private TableColumn<Animal, String> colAnimal;
    @FXML private TableColumn<Animal, String> colEspecie;
    @FXML private TableColumn<Animal, String> colRaca;
    @FXML private TableColumn<Animal, String> colTutor;

    private final VeterinarioDAO dao = new VeterinarioDBDAO();
    private final AnimalDAO animalDAO = new AnimalDBDAO();
    private final TutorDAO tutorDAO = new TutorDBDAO();
    private final Map<Integer, String> nomesTutores = new HashMap<>();

    private static final String TEXTO_PADRAO = "Selecione um veterinário para ver os animais que ele acompanha";

    @FXML
    public void initialize() {
        // tabela de veterinários
        somenteLetras(txtNome);
        somenteNumeros(txtTelefone);
        colNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colCrmv.setCellValueFactory(new PropertyValueFactory<>("crvm"));
        colTelefone.setCellValueFactory(new PropertyValueFactory<>("telefone"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));

        // tabela de consultas eanimais
        colAnimal.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colEspecie.setCellValueFactory(new PropertyValueFactory<>("especie"));
        colRaca.setCellValueFactory(new PropertyValueFactory<>("raca"));
        colTutor.setCellValueFactory(c -> new SimpleStringProperty(
                nomesTutores.getOrDefault(c.getValue().getTutorID(), "?")));
        // ao clicar em veterinário preenche o formulário e lista os animais dele
        tabela.getSelectionModel().selectedItemProperty().addListener((obs, antigo, v) -> {
            if (v != null) {
                txtNome.setText(v.getNome());
                txtCrmv.setText(v.getCrmv());
                txtTelefone.setText(v.getTelefone());
                txtEmail.setText(v.getEmail());
                carregarAnimais(v);


            } else {
                tabelaAnimais.getItems().clear();
                lblAnimais.setText(TEXTO_PADRAO);
            }

        });

        onConsultar();
    }

    @FXML
    private void onInserir() {
        try {
            if (camposInvalidos()) return;
            Veterinario v = new Veterinario();
            lerCampos(v);
            dao.insere(v);
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
        Veterinario v = tabela.getSelectionModel().getSelectedItem();
        if (v == null) {
            aviso("Selecione um veterinário na tabela para atualizar.");
            return;
        }
        if (camposInvalidos()) return;
        try {
            lerCampos(v);
            dao.atualiza(v);
            onConsultar();
            onLimpar();
        } catch (Exception e) {
            erro(e);
        }
    }

    @FXML
    private void onExcluir() {
        Veterinario v = tabela.getSelectionModel().getSelectedItem();
        if (v == null) {
            aviso("Selecione um veterinário na tabela para excluir.");
            return;
        }
        Alert confirmacao = new Alert(Alert.AlertType.CONFIRMATION, "Excluir o veterinário " + v.getNome() + "?");
        if (confirmacao.showAndWait().orElse(ButtonType.CANCEL) != ButtonType.OK) return;

        try {
            dao.remove(v);
            onConsultar();
            onLimpar();
        } catch (Exception e) {
            erro(e);
        }
    }
    @FXML
    private void onLimpar() {
        txtNome.clear();
        txtCrmv.clear();
        txtTelefone.clear();
        txtEmail.clear();
        tabela.getSelectionModel().clearSelection();
    }

    @FXML
    private void onIrParaTutores() {
        trocarTela("TutorView.fxml", "Clínica Veterinária - Tutores");
    }

    @FXML
    private void onIrParaAnimais() {
        trocarTela("AnimalView.fxml", "Clínica Veterinária - Animais");
    }

    //Auxiliares
    private void somenteLetras(TextField campo) {
        campo.setTextFormatter(new TextFormatter<String>(change ->
                change.getControlNewText().matches("[\\p{L} '\\-]*") ? change : null));
    }
    private void somenteNumeros(TextField campo) {
        campo.setTextFormatter(new TextFormatter<String>(change ->
                change.getControlNewText().matches("[0-9]*") ? change : null));
    }

    /*private void carregarTutores() {
        try {
            nomesTutores.clear();
            for (Tutor t : tutorDAO.listaTodos()) {
                nomesTutores.put(t.getTutorID(), t.getNome());
            }
        } catch (Exception e) {
            erro(e);
        }
    }*/


    private void lerCampos(Veterinario v) {
        v.setNome(txtNome.getText().trim());
        v.setCrmv(txtCrmv.getText().trim());
        v.setTelefone(vazioParaNull(txtTelefone.getText()));
        v.setEmail(vazioParaNull(txtEmail.getText()));
    }

    private String vazioParaNull(String texto) {
        return (texto == null || texto.isBlank()) ? null : texto.trim();
    }

    private boolean camposInvalidos() {
        if (txtNome.getText().isBlank() || txtCrmv.getText().isBlank()) {
            aviso("Nome e CRMV são obrigatórios.");
            return true;
        }
        return false;
    }

    private void trocarTela(String fxml, String titulo) {
        try {
            App.trocarTela(fxml, titulo);
        } catch (Exception e) {
            erro(e);
        }
    }

    private void aviso(String mensagem) {
        new Alert(Alert.AlertType.WARNING, mensagem).showAndWait();
    }

    private void carregarAnimais(Veterinario v) {
        System.out.println("Buscando animais do veterinário id=" + v.getId_veterinario());
        try {
            tabelaAnimais.getItems().setAll(animalDAO.listaPorVeterinario(v.getId_veterinario()));
            lblAnimais.setText("Animais acompanhados por " + v.getNome());
        } catch (Exception e) {
            erro(e);
        }
    }
    private void erro(Exception e) {
        e.printStackTrace();
        String msg = e.getMessage();
        if (e instanceof SQLException sql) {
            if ("23505".equals(sql.getSQLState())) {
                msg = "Já existe um veterinário com esse CRMV.";
            } else if ("23503".equals(sql.getSQLState())) {
                msg = "Este veterinário possui consultas cadastradas e não pode ser excluído.";
            }
        }
        new Alert(Alert.AlertType.ERROR, msg).showAndWait();
    }
}