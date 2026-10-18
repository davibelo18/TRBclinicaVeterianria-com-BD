
package model;

import java.time.LocalDate;

public class Animal {

    private int animalID;
    private String nome;
    private String especie;
    private String raca;
    private LocalDate dataNascimento;
    private String sexo;
    private int tutorID;
   // private Integer vetrianrioID;

    public Animal() {}

    public Animal(String nome, String especie, int tutorID) {
        this.nome = nome;
        this.especie = especie;
        this.tutorID = tutorID;
    }

    public Animal(int animalID, String nome, String especie,
                  String raca, LocalDate dataNascimento,
                  String sexo, int tutorID) {
        this.animalID = animalID;
        this.nome = nome;
        this.especie = especie;
        this.raca = raca;
        this.dataNascimento = dataNascimento;
        this.sexo = sexo;
        this.tutorID = tutorID;
    }

    public int getAnimalID() {
        return animalID;
    }

    public void setAnimalID(int animalID) {
        this.animalID = animalID;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEspecie() {
        return especie;
    }

    public void setEspecie(String especie) {
        this.especie = especie;
    }

    public String getRaca() {
        return raca;
    }

    public void setRaca(String raca) {
        this.raca = raca;
    }

    public LocalDate getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(LocalDate dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public int getTutorID() {
        return tutorID;
    }

    public void setTutorID(int tutorID) {
        this.tutorID = tutorID;
    }

    private Integer veterinarioID; // null = sem veterinário

    public Integer getVeterinarioID() {
        return veterinarioID;
    }

    public void setVeterinarioID(Integer veterinarioID) {
        this.veterinarioID = veterinarioID;
    }

    @Override
    public String toString() {
        return nome + " (" + especie + ")";
    }
}
