package Model.Entities;

public class Associado {

    //Matriculas
    private final Long codigoAssociado;
    private final Long matriculaEstado; //03
    private final Long nroFuncionarioEstado; //132
    private final Long matriculaIpe; //02
    private final Long matriculaEstadoAntiga; //83
    private final Long matriculaIpe2; //91

    private final Long nroVinculo;
    private final Long nroPensionista;

    private final String situacao;
    private final String nome;
    private final String cpf;
    private final String dtCadastro;
    private final String dtExclusao;
    private final String dtObito;

    public Associado(Long codigoAssociado, String matriculaEstado, String nroFuncionarioEstado, String matriculaIpe, String matriculaEstadoAntiga, String matriculaIpe2,
            String nroVinculo, String nroPensionista, String situacao, String nome, String cpf, String dtCadastro, String dtExclusao, String dtObito) {
        this.matriculaEstado = getNumber(matriculaEstado);
        this.nroFuncionarioEstado = getNumber(nroFuncionarioEstado);
        this.matriculaIpe = getNumber(matriculaIpe);
        this.matriculaEstadoAntiga = getNumber(matriculaEstadoAntiga);
        this.matriculaIpe2 = getNumber(matriculaIpe2);
        this.situacao = situacao.trim();
        this.codigoAssociado = codigoAssociado;

        this.nroVinculo = getNumber(nroVinculo);
        this.nroPensionista = getNumber(nroPensionista);

        this.nome = nome.trim();
        this.cpf = cpf.trim().replaceAll("[^0-9]", "");
        this.dtCadastro = dtCadastro.trim();
        this.dtExclusao = dtExclusao.trim();
        this.dtObito = dtObito.trim();
    }

    public Long getCodigoAssociado() {
        return codigoAssociado;
    }

    private Long getNumber(String str) {
        try {
            return Long.valueOf(str.trim().replaceAll("[^0-9]", ""));
        } catch (NumberFormatException e) {
            return (long) 0;
        }
    }

    public String getCpf() {
        return cpf;
    }

    public String getDtCadastro() {
        return dtCadastro;
    }

    public String getDtExclusao() {
        return dtExclusao;
    }

    public String getDtObito() {
        return dtObito;
    }

    public String getNome() {
        return nome;
    }

    public Long getMatriculaEstado() {
        return matriculaEstado;
    }

    public Long getNroFuncionarioEstado() {
        return nroFuncionarioEstado;
    }

    public Long getMatriculaIpe() {
        return matriculaIpe;
    }

    public Long getMatriculaEstadoAntiga() {
        return matriculaEstadoAntiga;
    }

    public Long getMatriculaIpe2() {
        return matriculaIpe2;
    }

    public Long getNroVinculo() {
        return nroVinculo;
    }

    public Long getNroPensionista() {
        return nroPensionista;
    }

    public String getSituacao() {
        return situacao;
    }

}
