package model;

import java.time.LocalDateTime;

public class Consulta{

    // Valores aceitos pelo CHECK do banco
    public static final String AGENDADA = "Agendada";
    public static final String REALIZADA = "Realizada";
    //public static final String CANCELADA = "Cancelada";

    private int consultaID;
    private int animalID;
    private int veterinarioID;
    private LocalDateTime dataHora;
    private String motivo;
    private String diagnostico;
    private String status = AGENDADA;

    // Só para exibição na tela que sao prenchidos pelo JOIN
    private String nomeAnimal;
    private String nomeVeterinario;

    public Consulta() {}

    public Consulta(int animalID, int veterinarioID, LocalDateTime dataHora, String motivo){
        this.animalID = animalID;
        this.veterinarioID = veterinarioID;
        this.dataHora = dataHora;
        this.motivo = motivo;
    }

    public int getConsultaID() {
        return consultaID;
    }
    public void setConsultaID(int consultaID) {
        this.consultaID = consultaID;
    }

    public int getAnimalID() {
        return animalID;
    }
    public void setAnimalID(int animalID){
        this.animalID = animalID;
    }

    public int getVeterinarioID(){
        return veterinarioID;
    }
    public void setVeterinarioID(int veterinarioID){
        this.veterinarioID = veterinarioID;
    }

    public LocalDateTime getDataHora(){
        return dataHora;
    }
    public void setDataHora(LocalDateTime dataHora){
        this.dataHora = dataHora;
    }

    public String getMotivo(){
        return motivo;
    }
    public void setMotivo(String motivo){
        this.motivo = motivo;
    }

    public String getDiagnostico(){
        return diagnostico;
    }
    public void setDiagnostico(String diagnostico){
        this.diagnostico = diagnostico;
    }

    public String getStatus(){
        return status;
    }
    public void setStatus(String status){
        this.status = status;
    }

    public String getNomeAnimal(){
        return nomeAnimal;
    }
    public void setNomeAnimal(String nomeAnimal){
        this.nomeAnimal = nomeAnimal;
    }

    public String getNomeVeterinario(){
        return nomeVeterinario;
    }
    public void setNomeVeterinario(String nomeVeterinario){
        this.nomeVeterinario = nomeVeterinario;
    }

    @Override
    public String toString() {
        return dataHora + " - " + nomeAnimal + " (" + status + ")";
    }
}