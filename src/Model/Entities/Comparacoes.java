package Model.Entities;

import java.math.BigDecimal;

public class Comparacoes {
    
    private final Long matricula;//MATRÍCULA
    private final String nome;//NOME
    private final String situacao;//SITUAÇÃO SISTEMA SINAPERS
    private final BigDecimal valorParcela;//PARCELAS
    private final BigDecimal valorFinanciado;//PMT
    private final BigDecimal ipergs;//IPERGS
    
    private BigDecimal diferencaPMT;//DIFERENÇA DE PMT
    private BigDecimal valorFinanciado_1perc;//PMT X TOTAL 1%
    private BigDecimal valorParcela_menos1perc_meioPerc;// PMT 0,5%

    public Comparacoes(Long matricula, String nome, String situacao, BigDecimal valorParcela, BigDecimal valorFinanciado, BigDecimal ipergs) {
        this.matricula = matricula;
        this.nome = nome;
        this.situacao = situacao;
        this.valorParcela = valorParcela;
        this.valorFinanciado = valorFinanciado;
        this.ipergs = ipergs;
        
        fazerCalculos();
    }
    
    private void fazerCalculos(){
        BigDecimal umPorcento = new BigDecimal(0.01);
        BigDecimal meioPorcento = new BigDecimal(0.005);
        
        diferencaPMT = ipergs.subtract(valorParcela);
        valorFinanciado_1perc = valorFinanciado.multiply(umPorcento);
        valorParcela_menos1perc_meioPerc = valorParcela.subtract(valorParcela.multiply(umPorcento)).multiply(meioPorcento);
    }

    public Long getMatricula() {
        return matricula;
    }

    public String getNome() {
        return nome;
    }

    public String getSituacao() {
        return situacao;
    }

    public BigDecimal getValorParcela() {
        return valorParcela;
    }

    public BigDecimal getValorFinanciado() {
        return valorFinanciado;
    }

    public BigDecimal getIpergs() {
        return ipergs;
    }

    public BigDecimal getDiferencaPMT() {
        return diferencaPMT;
    }

    public BigDecimal getValorFinanciado_1perc() {
        return valorFinanciado_1perc;
    }

    public BigDecimal getValorParcela_menos1perc_meioPerc() {
        return valorParcela_menos1perc_meioPerc;
    } 
    
}
