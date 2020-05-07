package Model.Entities;

import java.math.BigDecimal;
import java.util.Calendar;

public class Contrato{
    private long associadoCodigo;
    private long numeroProposta;
    private Calendar dataProposta;
    private BigDecimal valorFinanciado;
    private Integer qtdParcelas;
    private BigDecimal valorParcela;
    private Calendar primeiroVencimento;
    private Calendar ultimoVencimento;
    private Calendar encerramento;
    private Calendar dataVencimentoRefinanciamento;
    private String contratoRecebido;

    public Contrato() {
    }
    
    public Contrato(long associadoCodigo, long numeroProposta, Calendar dataProposta, BigDecimal valorFinanciado, Integer qtdParcelas, BigDecimal valorParcela, Calendar primeiroVencimento, Calendar ultimoVencimento, Calendar encerramento, Calendar dataVencimentoRefinanciamento, String contratoRecebido) {
        this.associadoCodigo = associadoCodigo;
        this.numeroProposta = numeroProposta;
        this.dataProposta = dataProposta;
        this.valorFinanciado = valorFinanciado;
        this.qtdParcelas = qtdParcelas;
        this.valorParcela = valorParcela;
        this.primeiroVencimento = primeiroVencimento;
        this.ultimoVencimento = ultimoVencimento;
        this.encerramento = encerramento;
        this.dataVencimentoRefinanciamento = dataVencimentoRefinanciamento;
        this.contratoRecebido = contratoRecebido;
    }

    public void setAssociadoCodigo(long associadoCodigo) {
        this.associadoCodigo = associadoCodigo;
    }

    public void setNumeroProposta(long numeroProposta) {
        this.numeroProposta = numeroProposta;
    }

    public void setDataProposta(Calendar dataProposta) {
        this.dataProposta = dataProposta;
    }

    public void setValorFinanciado(BigDecimal valorFinanciado) {
        this.valorFinanciado = valorFinanciado;
    }

    public void setQtdParcelas(Integer qtdParcelas) {
        this.qtdParcelas = qtdParcelas;
    }

    public void setValorParcela(BigDecimal valorParcela) {
        this.valorParcela = valorParcela;
    }

    public void setPrimeiroVencimento(Calendar primeiroVencimento) {
        this.primeiroVencimento = primeiroVencimento;
    }

    public void setUltimoVencimento(Calendar ultimoVencimento) {
        this.ultimoVencimento = ultimoVencimento;
    }

    public void setEncerramento(Calendar encerramento) {
        this.encerramento = encerramento;
    }

    public void setDataVencimentoRefinanciamento(Calendar dataVencimentoRefinanciamento) {
        this.dataVencimentoRefinanciamento = dataVencimentoRefinanciamento;
    }

    public void setContratoRecebido(String contratoRecebido) {
        this.contratoRecebido = contratoRecebido;
    }

    
    
    public long getAssociadoCodigo() {
        return associadoCodigo;
    }

    public long getNumeroProposta() {
        return numeroProposta;
    }

    public Calendar getDataProposta() {
        return dataProposta;
    }

    public BigDecimal getValorFinanciado() {
        return valorFinanciado;
    }

    public Integer getQtdParcelas() {
        return qtdParcelas;
    }

    public BigDecimal getValorParcela() {
        return valorParcela;
    }

    public Calendar getPrimeiroVencimento() {
        return primeiroVencimento;
    }

    public Calendar getUltimoVencimento() {
        return ultimoVencimento;
    }

    public Calendar getEncerramento() {
        return encerramento;
    }

    public Calendar getDataVencimentoRefinanciamento() {
        return dataVencimentoRefinanciamento;
    }

    public String getContratoRecebido() {
        return contratoRecebido;
    }
    
    
}