package Model.Entities;

import Auxiliar.Valor;
import java.math.BigDecimal;

public class FactaLctos {

    private Long matricula;
    private String nome;
    private String cpf;
    private BigDecimal valorFinanciado;
    private BigDecimal valorParcela;

    public FactaLctos(Long matricula, String nome, String cpf, BigDecimal valorFinanciado, BigDecimal valorParcela) {
        this.matricula = matricula;
        this.nome = nome;
        this.cpf = cpf;
        this.valorFinanciado = valorFinanciado;
        this.valorParcela = valorParcela;

        setMatricula();
    }

    private void setMatricula() {
        /*
        Ex: xxxx7801 --> 01
            xxxx781  --> 01
            xxxx7809 --> 09
            xxxx789  --> 09
            xxxx7897  --> 09
            2.000.000.000
        Se os últimos 2 números forem maiores que 10, coloca um zero antes do último número
        
         */
        // Pegar string
        String matriculaString = matricula.toString();

        // 1 - Inverter String
        String matriculaInvertida = new StringBuilder(matriculaString).reverse().toString();

        //Separa por 87 e fica no maximo com 2 separações
        String[] matriculaSeparada = matriculaInvertida.split("87", 2);

        //inverte segundo corte
        String segundoCorte = new StringBuilder(matriculaSeparada[1]).reverse().toString();

        //Para o primeiro corte (Ultimos números antes de inverter), coloque seus devidos zeros
        Valor primeiroCorte = new Valor(new StringBuilder(matriculaSeparada[0]).reverse().toString());
        if (primeiroCorte.getInteger() < 10) {
            primeiroCorte.setString("0" + primeiroCorte.getInteger());
        } else {
            primeiroCorte.setString("0" + primeiroCorte.getInteger().toString().charAt(0));
        }

        //Rertorna segundo corte invertido + "78" + primeiro corte; 
        Valor retorno = new Valor(segundoCorte + "78" + primeiroCorte.getString());

        matricula = retorno.getLong();
    }

    public String getCpf() {
        return cpf;
    }

    public Long getMatricula() {
        return matricula;
    }

    public String getNome() {
        return nome;
    }

    public BigDecimal getValorFinanciado() {
        return valorFinanciado;
    }

    public BigDecimal getValorParcela() {
        return valorParcela;
    }
}
