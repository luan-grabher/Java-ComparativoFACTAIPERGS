package Model;

import Model.Entities.Comparacoes;
import Model.Entities.FactaLctos;
import Model.Entities.IpergsLctos;
import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import main.Arquivo;

public class Comparacoes_Model {

    private final List<FactaLctos> facta;
    private final List<IpergsLctos> ipergs;
    private final List<FactaLctos> esquecidosFacta = new ArrayList<>();
    private List<Comparacoes> comparacoes = new ArrayList<>();

    public Comparacoes_Model(List<FactaLctos> facta, List<IpergsLctos> ipergs) {
        this.facta = facta;
        this.ipergs = ipergs;
    }

    /**
     * Cria uma lista de comparação a partir da lista do IPERGS com as
     * matriculas que aparecem
     */
    public String gerarComparacoes() {
        try {
            //Cria Lista de comparação a partir do ipergrs
            for (int i = 0; i < ipergs.size(); i++) {
                IpergsLctos ipe = ipergs.get(i);

                try {
                    //Define valores IPERGS
                    Long matricula = ipe.getMatricula();
                    String nome = ipe.getNome();
                    BigDecimal ipergsValor = new BigDecimal(ipe.getValor());

                    //Vai pegar com PMT
                    String situacao = "IPERGS";
                    BigDecimal valorParcelaFacta = new BigDecimal(0);
                    BigDecimal valorFinanciado = new BigDecimal(0);

                    for (int j = 0; j < facta.size(); j++) {
                        FactaLctos fac = facta.get(j);
                        //se matricula for igual da facta
                        if (fac.getMatricula() == ipe.getMatricula()) {
                            //pega entidade facta
                            nome += " - " + fac.getCpf();
                            valorParcelaFacta = fac.getValorParcela();
                            valorFinanciado = fac.getValorFinanciado();
                            situacao = getSituacao(ipe.getSituacao());
                            break;
                        }
                    }

                    comparacoes.add(new Comparacoes(matricula, nome, situacao, valorParcelaFacta, valorFinanciado, ipergsValor));

                } catch (Exception e) {
                }
            }

            //Ordem alfabética
            comparacoes.sort(Comparator.comparing(Comparacoes::getNome));
            return "";
        } catch (Exception e) {
            return "Erro ao gerar comparações!";
        }
    }

    public String salvarEsquecidosFacta(File localSalvar) {
        try {
            //Cria lista de esquecidos
            for (FactaLctos f : facta) {
                if (ipergs.stream().filter(i -> i.getMatricula() == f.getMatricula()).count() == 0) {
                    esquecidosFacta.add(f);
                }
            }

            //Salva lista
            StringBuilder texto = new StringBuilder();
            for (FactaLctos esquecido : esquecidosFacta) {
                texto.append("\n");
                texto.append(esquecido.getMatricula());
                texto.append(";");
                texto.append(esquecido.getNome());
                texto.append(";");
                texto.append(esquecido.getCpf());
                texto.append(";");
                texto.append(esquecido.getValorParcela());
                texto.append(";");
                texto.append(esquecido.getValorFinanciado());
            }

            if (Arquivo.salvar(localSalvar.getAbsolutePath() + "/Esquecidos FACTA 1.csv", texto.toString())) {
                return "";
            } else {
                return "Erro ao salvar esquecidos FACTA na pasta " + localSalvar.getAbsolutePath() + "\nVocê está com o arquivo aberto?";
            }
        } catch (Exception e) {
            return "Erro ao salvar esquecidos FACTA: " + e;
        }
    }

    private String getSituacao(String situacaoString) {
        switch (situacaoString) {
            case "A":
                return "Ativo";
            case "I":
                return "Inativo";
            default:
                return "Não cadastrado";
        }
    }

    public List<Comparacoes> getComparacoes() {
        return getComparacoes("");
    }

    public List<Comparacoes> getComparacoes(String tipo) {
        switch (tipo) {
            case "Ativos":
                //Faz filtro e retorna filtro
                List<Comparacoes> listaAtivos = comparacoes.stream().filter(c -> c.getSituacao().equals("Ativo")).collect(Collectors.toList());
                return listaAtivos;
            default:
                return comparacoes;
        }
    }

}
