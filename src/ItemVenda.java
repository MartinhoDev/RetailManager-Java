public class ItemVenda {
    //Atributos
    private long id;
    private long idVenda;
    private long idProduto;
    private double precoPraticado;
    private long qtd;

    //Setters
    public void setId(long id){
        if(id >= 0){
            this.id = id;
        }
    }

    public void setIdVenda(long idVenda){
        if(id >= 0){
            this.idVenda = idVenda;
        }
    }

    public void setIdProduto(long idProduto){
        if(idProduto >= 0){
            this.idProduto = idProduto;
        }
    }

    public void setPrecoPreticado(double precoPraticado){
        if(precoPraticado >= 0.0){
            this.precoPraticado = precoPraticado;
        }
    }

    public void setQtd(long qtd){
        if(qtd >= 0){
            this.qtd = qtd;
        }
    }

    //Getters
    public long getId(){
        return this.id;
    }

    public long getIdVenda(){
        return this.idVenda;
    }

    public long getIdProduto(){
        return this.idProduto;
    }

    public long getQtd(){
        return this.qtd;
    }

    public double getPrecoPraticado(){
        return this.precoPraticado;
    }

    //Construtores
    public ItemVenda(){
        this.id = 0;
        this.idProduto = 0;
        this.idVenda = 0;
        this.qtd = 0;
        this.precoPraticado = 0.0;
    }

    public ItemVenda(
        long id, long idProduto, long idVenda,
        long qtd, double precoPraticado
    ){
        this();
        this.setId(id);
        this.setIdProduto(idProduto);
        this.setIdVenda(idVenda);
        this.setQtd(qtd);
        this.setPrecoPreticado(precoPraticado);
    }
}
