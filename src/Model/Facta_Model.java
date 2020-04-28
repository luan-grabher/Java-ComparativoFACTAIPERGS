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
        montarListaLctosTXT();
    }

    private void montarListaLctosTXT() {
        try {
            Integer minCol = 137;

            Integer[] mapNome = {0, 39};
            Integer[] mapMatricula = {39, 60};
            Integer[] mapValorFinanciado = {61, 76};
            Integer[] mapValorParcela = {77, 90};
            Integer[] mapNroParcelas = {91, 99};
            Integer[] mapCpf = {110, 130};
            Integer[] mapProposta = {130};

            //Abre arquivo
            String texto = Arquivo.ler(arquivo.getAbsolutePath());
            String[] linhas = texto.split("\n");

            //Percorre todas as linhas
            for (String linha : linhas) {
                try {
                    String[] baseLinha = linha.split("");
                    
                    //Se tiver no minimo 160 posicoes
                    if (linha.length() >= minCol) {
                        String nome = linha.substring(mapNome[0], mapNome[1]).replaceAll("[^a-zA-Z ]+", "").trim();
                        Long matricula = Long.valueOf(linha.substring(mapMatricula[0], mapMatricula[1]).replaceAll("[^0-9]", "").trim());
                        BigDecimal valorFinanciado = new BigDecimal(linha.substring(mapValorFinanciado[0], mapValorFinanciado[1]).replaceAll("[^0-9.]", "").trim());
                        BigDecimal valorParcela = new BigDecimal(linha.substring(mapValorParcela[0], mapValorParcela[1]).replaceAll("[^0-9.]", "").trim());
                        Integer nroParcelas = Integer.valueOf(linha.substring(mapNroParcelas[0], mapNroParcelas[1]).replaceAll("[^0-9]", "").trim());
                        String cpf = linha.substring(mapCpf[0], mapCpf[1]).replaceAll("[^0-9]", "").trim();
                        Long proposta = Long.valueOf(linha.substring(mapProposta[0]).replaceAll("[^0-9]", "").trim());
                        
                        if (!nome.equals("") & matricula > 1000000 & nroParcelas > 0 & !cpf.equals("") & proposta > 0) {
                            lctos.add(new FactaLctos(matricula, nome, cpf, valorFinanciado, valorParcela,nroParcelas, proposta));
                        }
                    }
                } catch (Exception e) {
                }
            }

        } catch (Exception e) {
        }
    }

    public List<FactaLctos> getLctos() {
        return lctos;
    }

}
