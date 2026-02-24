import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class ProdutoPerecivel extends Produto {
    private static final double DESCONTO = 0.25;
    private static final int PRAZO_DESCONTO = 7;
    private LocalDate dataDeValidade;
    

    protected ProdutoPerecivel(String desc, double precoCusto, double margemLucro, LocalDate validade) {
        super(desc, precoCusto, margemLucro);
        if (validade.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("O produto esta vencido!");
        }
        this.dataDeValidade = validade;
    }
 
    @Override
    public double valorVenda() {
        LocalDate dataHoje = LocalDate.now();
        long diasAteValidade = java.time.temporal.ChronoUnit.DAYS.between(dataHoje, dataDeValidade);
        if (diasAteValidade <= PRAZO_DESCONTO && diasAteValidade >= 0){
            return super.valorVenda() * (1 - DESCONTO);
        }
        return super.valorVenda();
    }

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return super.toString() + String.format(", Validade: %s", dataDeValidade.format(formatter));
    }

}
