import java.util.ArrayList;

public abstract class Pessoa {
    //Atributos
    private long id;
    private String nome;
    private String cpf;
    private ArrayList<Telefone> telefones;

    //Setters
    public void setId(long id){
        if(id >= 0){
            this.id = id;
        }
    }

    public void setNome(String nome){
        if(nome != null && !nome.isEmpty() && nome.length() <= 1024){
            this.nome = nome;
        }
    }

    public void setCpf(String cpf){
        if(Pessoa.validaCpf(cpf)){
            this.cpf = cpf;
        }
    }

    public void setTelefones(ArrayList<Telefone> telefones){
        this.telefones = telefones;
    }

    public void adicionarTelefone(Telefone t){
        if(this.telefones == null){
            this.telefones = new ArrayList<>();
        }
        this.telefones.add(t);
    }

    //Getters
    public long getId(){
        return this.id;
    }

    public String getCpf(){
        return this.cpf;
    }

    public String getNome(){
        return this.nome;
    }

    public ArrayList<Telefone> getTelefones(){
        return this.telefones;
    }

    //Metodos
    public static boolean validaCpf(String cpf){
        char caractere;
        int i;

        if(cpf == null || cpf.isEmpty() || cpf.length() != 11){
            return false;
        }
        for(i = 0; i < cpf.length(); i++){
            caractere = cpf.charAt(i);
            if(!Character.isDigit(caractere)){
                return false;
            }
        }
        return true;
    }

    public boolean validaAtributos(String nome, String cpf){
        if(this.nome.equals(nome) && this.cpf.equals(cpf)){
            return true;
        }
        return false;
    }

    //Construtores
    public Pessoa(){
        this.id = 0;
        this.nome = "Nome Generico";
        this.cpf = "00000000000";
        this.telefones = new ArrayList<>();
    }

    public Pessoa(long id, String nome, String cpf){
        this();
        this.setId(id);
        this.setCpf(cpf);
        this.setNome(nome);
    }
}