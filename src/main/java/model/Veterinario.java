package model;

public class Veterinario {
    private int id_veterinario;
    private String nome;
    private String Crmv;
    private String telefone;
    private String email;

    public Veterinario(){}

    public Veterinario(String Nome, String telefone,String Crmv,String email, int id_veterinario){
        this.nome=Nome;
        this.telefone=telefone;
        this.Crmv=Crmv;
        this.email=email;
        this.id_veterinario=id_veterinario;
    }
    public int getId_veterinario(){
        return id_veterinario;
    }
    public void setId_veterinario(int id_veterinario){
        this.id_veterinario=id_veterinario;
    }
    public String getNome() {
        return nome;
    }
    public void setNome(String nome){
        this.nome=nome;
    }
    public String getCrmv() {
        return Crmv;
    }
    public void setCrmv(String Crmv){
        this.Crmv=Crmv;
    }

    public String getTelefone() {
        return telefone;
    }
    public void  setTelefone(String telefone){
        this.telefone=telefone;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    @Override
    public String toString() {
        return nome;
    }
}
