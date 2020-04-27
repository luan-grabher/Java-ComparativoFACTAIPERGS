package Model;

import Model.Entities.FactaLctos;
import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import main.Arquivo;

public class Facta_Model {

    public String status = "";

    private final File arquivo;
    private final List<FactaLctos> lctos = new ArrayList<>();

    public Facta_Model(File arquivo) {
        this.arquivo = arquivo;
        //montar a lista de lctos
        status = montarListaLctosTXT();
    }

    private String montarListaLctosTXT() {
        String r = "";
        try {
            //Abre arquivo
            String arquivoTexto = Arquivo.ler(arquivo.getAbsolutePath());
            String[] arquivoLinhas = arquivoTexto.split("\n");

            //Percorre todas as linhas
            for (String arquivoLinha1 : arquivoLinhas) {
                try {
                    String arquivoLinha = arquivoLinha1;
                    //Se tiver no minimo 160 posicoes
                    if (arquivoLinha.length() >= 160) {
                        String nome = arquivoLinha.substring(6, 88).replaceAll("[^a-zA-Z ]+", "").trim();

                        //Verifica se nome existe
                        if (!nome.equals("")) {
                            Long matricula = Long.valueOf(arquivoLinha.substring(88, 111).replaceAll("[^0-9]", "").trim());

                            //Verifica matricula
                            if (matricula > 1000000) {
                                String valorFinanciadoString = arquivoLinha.substring(111, 142).replaceAll("[^0-9.]", "").trim();
                                BigDecimal valorFinanciado = new BigDecimal(valorFinanciadoString);

                                //Verifica valor financiado
                                //if (valorFinanciado.compareTo(BigDecimal.ZERO) == 1) {
                                if (valorFinanciado.compareTo(BigDecimal.ZERO) != 2) { //Desativado verificação por zero

                                    //Pega valor parcela e cpf
                                    String valorParcela_cpf_String = arquivoLinha.substring(142).replaceAll("[^0-9.]", "").trim();
                                    String[] splitParcela_Cpf = valorParcela_cpf_String.split("\\.");

                                    BigDecimal valorParcela = new BigDecimal(
                                            splitParcela_Cpf[0]
                                                    + "."
                                                    + splitParcela_Cpf[1].substring(0, 2)
                                    );

                                    String cpf = "";
                                    try {
                                        cpf = splitParcela_Cpf[1].substring(2);
                                    } catch (Exception e) {
                                    }

                                    //System.out.println(matricula + " - " + nome);
                                    lctos.add(new FactaLctos(matricula, nome, cpf, valorFinanciado, valorParcela));
                                }
                            }
                        }
                    }
                }catch (Exception e) {
                    //Se der algum erro não vai adicionar
                    //System.out.println("Erro: " + e);
                }
            }

        } catch (Exception e) {
        }

        return r;
    }

    public List<FactaLctos> getLctos() {
        return lctos;
    }

}
