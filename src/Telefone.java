public class Telefone {
    private long id;
    private String numero;
    private long idPessoa;

    public void setId(long id){
        if(id >= 0){
            this.id = id;
        }
    }

    public void setIdPessoa(long idPessoa){
        if(idPessoa >= 0){
            this.idPessoa = idPessoa;
        }
    }

    public void setNumero(String numero){
        if(Telefone.validaNumero(numero)){
            this.numero = numero;
        }
    }

    public long getId(){
        return this.id;
    }

    public long getIdPessoa(){
        return this.idPessoa;
    }

    public String getNumero(){
        return this.numero;
    }

    public static boolean validaNumero(String numero){
        char caractere;
        int i;

        if(numero == null || numero.isEmpty() || numero.length() > 20){
            return false;
        }
        for(i = 0; i < numero.length(); i++){
            caractere = numero.charAt(i);
            if(!Character.isDigit(caractere)){
                return false;
            }
        }
        return true;
    }

    public Telefone(){
        this.id = 0;
        this.idPessoa = 0;
        this.numero = "00000000000000000000";
    }

    public Telefone(long id, long idPessoa, String numero){
        this();
        this.setId(id);
        this.setIdPessoa(idPessoa);
        this.setNumero(numero);
    }
}