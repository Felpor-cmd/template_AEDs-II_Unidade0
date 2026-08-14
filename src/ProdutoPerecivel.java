import java.time.LocalDate;

public class ProdutoPerecivel extends Produto {
    private static final double DESCONTO = 0.25;
    private static final int PRAZO_DESCONTO = 7;
    private LocalDate dataValidade;

    public ProdutoPerecivel(String desc, double precoCusto, double margemLucro, LocalDate dataValidade){
        super(desc, precoCusto, margemLucro);

        if dataValidade.isAfter(LocalDate.now()) {
            this.dataValidade = dataValidade;
        } else {
            throw new IllegalArgumentException("Data de validade inválida.");
        }
    }

    public valorVenda(){
        if (dataValidade.)
    }

    @Override
    public String toString(){

    }
}
