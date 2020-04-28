package Model.Entities;

public class IpergsLctos {
    private long matricula;
    private String nome;
    private String cpf;
    private String situacao;
    private double valor;

    public IpergsLctos(long matricula, String nome, String cpf, String situacao, double valor) {
        this.matricula = matricula;
        this.nome = nome;
        this.cpf = cpf;
        this.situacao = situacao;
        this.valor = valor;
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

    public double getValor() {
        return valor;
    }

    
    
    
}
