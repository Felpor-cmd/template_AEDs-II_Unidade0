import java.time.LocalDate;

public class ProdutoPerecivel extends Produto {
    private static final double DESCONTO = 0.25;
    private static final int PRAZO_DESCONTO = 7;
    private LocalDate dataValidade;

    public ProdutoPerecivel(String desc, double precoCusto, double margemLucro, LocalDate dataValidade){
        super(desc, precoCusto, margemLucro);

        if (dataValidade.isAfter(LocalDate.now())) {
            this.dataValidade = dataValidade;
        } else {
            throw new IllegalArgumentException("Data de validade inválida.");
        }
    }

    public double valorVenda(){
        double valorVenda = 0;
        if (dataValidade.isBefore(LocalDate.now()))
            throw new IllegalArgumentException("Produto vencido.");
        
        if (dataValidade.isBefore(LocalDate.now().plusDays(PRAZO_DESCONTO))) {
            valorVenda = super.valorDeVenda() * (DESCONTO);
        } else {
            valorVenda = super.valorDeVenda();
        }

        return valorVenda;
    }

    public String toString(){
        return super.toString() + " - Validade: " + dataValidade;
    }
}
