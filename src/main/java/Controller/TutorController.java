package Controller;

import App.App;
import dao.TutorDAO;
import dao.TutorDBDAO;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Tutor;

import java.sql.SQLException;

public class TutorController {

    // precisam ser iguais aos do Scene Builder
    @FXML private TextField txtNome;
    @FXML private TextField txtCpf;
    @FXML private TextField txtTelefone;
    @FXML private TextField txtEmail;
    @FXML private TextField txtEndereco;
    @FXML private TableView<Tutor> tabela;
    @FXML private TableColumn<Tutor, String> colNome;
    @FXML private TableColumn<Tutor, String> colCpf;
    @FXML private TableColumn<Tutor, String> colTelefone;
    @FXML private TableColumn<Tutor, String> colEmail;
    @FXML private TableColumn<Tutor, String> colEndereco;

    private final TutorDAO dao = new TutorDBDAO();

    // Roda sozinho quando a tela abre
    @FXML
    public void initialize() {
        // cada coluna mostra um atributo da classe Tutor
        somenteLetras(txtNome);
        somenteNumeros(txtCpf);
        somenteNumeros(txtTelefone);
        colNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colCpf.setCellValueFactory(new PropertyValueFactory<>("cpf"));
        colTelefone.setCellValueFactory(new PropertyValueFactory<>("telefone"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colEndereco.setCellValueFactory(new PropertyValueFactory<>("endereco"));
        txtCpf.textProperty().addListener((obs, antigo, novo) -> {

        });
        // ao clicar numa linha, os campos são preenchidos para editar
        tabela.getSelectionModel().selectedItemProperty().addListener((obs, antigo, t) -> {
            if (t != null) {
                txtNome.setText(t.getNome());
                txtCpf.setText(t.getCpf());
                txtTelefone.setText(t.getTelefone());
                txtEmail.setText(t.getEmail());
                txtEndereco.setText(t.getEndereco());
            }
        });

        onConsultar(); // já mostra os registros ao abrir
    }

    @FXML
    private void onInserir() {
        if (camposInvalidos()) return;
        try {
            Tutor t = new Tutor();
            lerCampos(t);
            dao.insere(t);
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
        Tutor t = tabela.getSelectionModel().getSelectedItem();
        if (t == null) {
            aviso("Selecione um tutor na tabela para atualizar.");
            return;
        }
        if (camposInvalidos()) return;
        try {
            lerCampos(t);
            dao.atualiza(t);
            onConsultar();
            onLimpar();
        } catch (Exception e) {
            erro(e);
        }
    }

    @FXML
    private void onExcluir() {
        Tutor t = tabela.getSelectionModel().getSelectedItem();
        if (t == null) {
            aviso("Selecione um tutor na tabela para excluir.");
            return;
        }
        Alert confirmacao = new Alert(Alert.AlertType.CONFIRMATION, "Excluir o tutor " + t.getNome() + "?");
        if (confirmacao.showAndWait().orElse(ButtonType.CANCEL) != ButtonType.OK) return;

        try {
            dao.remove(t);
            onConsultar();
            onLimpar();
        } catch (Exception e) {
            erro(e);
        }
    }

    @FXML
    private void onLimpar() {
        txtNome.clear();
        txtCpf.clear();
        txtTelefone.clear();
        txtEmail.clear();
        txtEndereco.clear();
        tabela.getSelectionModel().clearSelection();
    }

    @FXML
    private void onIrParaAnimais() {
        try {
            App.trocarTela("AnimalView.fxml", "Clínica Veterinária - Animais");
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

    // auxiliares
    private void somenteLetras(TextField campo) {
        campo.setTextFormatter(new TextFormatter<String>(change ->
                change.getControlNewText().matches("[\\p{L} '\\-]*") ? change : null));
    }
    private void somenteNumeros(TextField campo) {
        campo.setTextFormatter(new TextFormatter<String>(change ->
                change.getControlNewText().matches("[0-9]*") ? change : null));
    }


    private void lerCampos(Tutor t) {
        t.setNome(txtNome.getText().trim());
        t.setCpf(vazioParaNull(txtCpf.getText()));
        t.setTelefone(txtTelefone.getText().trim());
        t.setEmail(vazioParaNull(txtEmail.getText()));
        t.setEndereco(vazioParaNull(txtEndereco.getText()));
    }

    // CPF vazio precisa virar NULL, senão o UNIQUE do banco recusa o segundo tutor sem CPF
    private String vazioParaNull(String texto) {
        return (texto == null || texto.isBlank()) ? null : texto.trim();
    }

    private boolean camposInvalidos() {
        if (txtNome.getText().isBlank() || txtTelefone.getText().isBlank()) {
            aviso("Nome e telefone são obrigatórios.");
            return true;
        }
        return false;
    }

    private void aviso(String mensagem) {
        new Alert(Alert.AlertType.WARNING, mensagem).showAndWait();
    }

    private void erro(Exception e) {
        e.printStackTrace(); // mostra o erro completo no console
        String msg = e.getMessage();
        if (e instanceof SQLException sql) {
            if ("23505".equals(sql.getSQLState())) {
                msg = "Já existe um tutor com esse CPF.";
            } else if ("23503".equals(sql.getSQLState())) {
                msg = "Este tutor possui animais cadastrados e não pode ser excluído.";
            }
        }
        new Alert(Alert.AlertType.ERROR, msg).showAndWait();
    }
}