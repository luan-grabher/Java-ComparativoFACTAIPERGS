package Model.Entities;

import tpsdb.Model.Entities.Associado;
import tpsdb.Model.Tps_Model;


public final class LctoTxt {
    private String matricula;
    private int matricula_estado;
    private int matricula_vinculo;
    private int matricula_pensionista;
	
    private String situacao;
    private long associadoCodigo;
    private String nomeAssociado;
    private String cpfAssociado;
    private final String tipo;
    private final Long nroTipo;
    private final Float valor;
    private final Float valorRecebido;
    private final Float valorEmAberto;
    private final String obs;
    private final String obs1;
    private final String obs2;

    public LctoTxt(String matriculaParcial, String tipo, String nroTipo, Float valor, Float valorRecebido, Float valorEmAberto, String obs1, String obs2) {
        setMatricula(matriculaParcial);
		
        this.tipo = tipo.trim();
        this.nroTipo = Long.valueOf(nroTipo.trim().replaceAll(" ", ""));
        this.valor = valor/100;
        this.valorRecebido = valorRecebido/100;
        this.valorEmAberto = valorEmAberto/100;
        this.obs1 = obs1.trim();
        this.obs2 = obs2.trim();
        this.obs = this.obs1 + " - " + this.obs2;
    }
	
    public void setMatricula(String matriculaParcial){
       matricula_estado = Integer.valueOf(matriculaParcial.substring(0, 8));
       matricula_vinculo = Integer.valueOf(matriculaParcial.substring(8, 10));
       matricula_pensionista = Integer.valueOf(matriculaParcial.substring(10, 12));

       matricula = matricula_estado + "|" + matricula_vinculo + "|" + matricula_pensionista;
    }
	
    public void setAssociado(){
       Associado a = Tps_Model.getAssociado((long) matricula_estado, (long) matricula_vinculo, (long) matricula_pensionista);
       nomeAssociado = a.getNome();
       situacao = a.getSituacao();
       cpfAssociado = a.getCpf();
       associadoCodigo = a.getCodigoAssociado();
    }

    public long getAssociadoCodigo() {
        return associadoCodigo;
    }   
	
    public String getCSV(){
        return  matricula + ";" + nomeAssociado + ";" + 
                tipo + ";" + nroTipo + ";" + situacao + ";" +
                valor + ";" + valorRecebido + ";" + valorEmAberto + ";" +
                obs1 + ";" + obs2;
    }
    
    public String getMatricula() {
        return matricula;
    }

    public String getNomeAssociado() {
        return nomeAssociado;
    }

    public String getCpfAssociado() {
        return cpfAssociado;
    }
    

    public String getTipo() {
        return tipo;
    }

    public Long getNroTipo() {
        return nroTipo;
    }

    public Float getValor() {
        return valor;
    }

    public Float getValorRecebido() {
        return valorRecebido;
    }

    public Float getValorEmAberto() {
        return valorEmAberto;
    }

    public String getObs() {
        return obs;
    }

    public String getObs1() {
        return obs1;
    }

    public String getObs2() {
        return obs2;
    }
    
    public String getAtributo(String nomeAtributo){
        switch (nomeAtributo) {
            case "matricula":
                return matricula;
            case "nomeAssociado":
                return nomeAssociado;
            case "tipoLcto":
                return tipo;
            case "situacao":
                return situacao;
            case "valor":
                return valor.toString();
            case "valorRecebido":
                return valorRecebido.toString();
            case "valorAberto":
                return valorEmAberto.toString();
            case "obs1":
                return obs1;
            case "obs2":
                return obs2;
            default:
                return "";
        }
    }

    public int getMatricula_estado() {
        return matricula_estado;
    }

    public int getMatricula_vinculo() {
        return matricula_vinculo;
    }

    public int getMatricula_pensionista() {
        return matricula_pensionista;
    }

    public String getSituacao() {
        return situacao;
    }
    
    
    
}
