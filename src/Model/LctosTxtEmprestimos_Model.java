package Model;

import Model.Entities.LctoTxt;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import main.Arquivo;

public class LctosTxtEmprestimos_Model {

    private String textOfFile = "";
    private List<LctoTxt> lctos;
    public Boolean status = false;
    public Associados_Model associados = new Associados_Model();

    public LctosTxtEmprestimos_Model(File arquivo) {
        this.lctos = new ArrayList<>();
        textOfFile = Arquivo.ler(arquivo.getAbsolutePath());
        status = constructList();
    }

    public List<LctoTxt> getLctos() {
        return lctos;
    }

//    public void filtrarLctos(List<Filtro> filtros) {
//        for (Filtro filtro : filtros) {
//            switch (filtro.getTipo()) {
//                case "Igual a":
//                    lctos = lctos.stream().filter(
//                            l -> l.getAtributo(filtro.getColuna()).equals(filtro.getFiltro())
//                    ).collect(Collectors.toList());
//                    break;
//                case "Diferente de":
//                    lctos = lctos.stream().filter(
//                            l -> !l.getAtributo(filtro.getColuna()).equals(filtro.getFiltro())
//                    ).collect(Collectors.toList());
//                    break;
//                case "Contém o filtro":
//                    lctos = lctos.stream().filter(
//                            l ->  naString(l.getAtributo(filtro.getColuna()), filtro.getFiltro())
//                    ).collect(Collectors.toList());
//                    break;
//            }
//        }
//    }

    private boolean constructList() {
        boolean b = false;
        try {
            String[] linhasArquivo = textOfFile.split("\n");
            for (String linha : linhasArquivo) {
                String mat_parcial = linha.substring(0, 12);
                LctoTxt lcto = new LctoTxt(mat_parcial, /*matricula*/
                        linha.substring(15, 26), /*tipo lançamento*/
                        linha.substring(26, 29), /*nro lcto*/
                        Float.valueOf(linha.substring(41, 50)), /*Valor*/
                        Float.valueOf(linha.substring(50, 59)), /*Valor Recebido*/
                        Float.valueOf(linha.substring(59, 68)), /*Valor em Aberto*/
                        linha.substring(68, 98), /*OBS 1*/
                        linha.substring(98, 123) /*OBS 2*/
                );
                lcto.setNomeAssociado(associados);
                lctos.add(lcto);
            }
            b = true;
        } catch (Exception e) {
        }
        return b;
    }

    private boolean naString(String str, String filtroPossuiENaoPossui) {
        boolean b = true;

        //Arruma case sensitive
        str = str.toLowerCase();
        filtroPossuiENaoPossui = filtroPossuiENaoPossui.toLowerCase();

        try {
            //Dividir filtros
            String[] filtros = filtroPossuiENaoPossui.split("#", -1);

            //Verifica possui
            String[] possuis = filtros[0].split(";", -1);
            for (String possui : possuis) {
                if (!str.contains(possui)) {
                    b = false;
                    break;
                }
            }

            //Se tiver todos possui e tiver exclusoes a fazer
            if (b & filtros.length == 2) {
                String[] naopossuis = filtros[1].split(";", -1);
                for (String naopossui : naopossuis) {
                    if (str.contains(naopossui)) {
                        b = false;
                        break;
                    }
                }
            }
        } catch (Exception e) {
        }

        return b;
    }
}
