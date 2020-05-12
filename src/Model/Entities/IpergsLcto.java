package Model.Entities;

import java.math.BigDecimal;

public class IpergsLcto {
    private long matricula;
    private long associadoCodigo;
    private String nome;
    private String cpf;
    private String situacao;
    private BigDecimal valor;

    public IpergsLcto(long associadoCodigo, long matricula, String nome, String cpf, String situacao, BigDecimal valor) {
        this.associadoCodigo = associadoCodigo;
        this.matricula = matricula;
        this.nome = nome;
        this.cpf = cpf;
        this.situacao = situacao;
        this.valor = valor;
    }

    public long getAssociadoCodigo() {
        return associadoCodigo;
    }    

    public long getMatricula() {
        return matricula;
    }

    public String getNome() {
        return nome;
    }

    public String getCpf() {
        return cpf;
    }

    public String getSituacao() {
        return situacao;
    }

    public BigDecimal getValor() {
        return valor;
    }

    
    
    
}
