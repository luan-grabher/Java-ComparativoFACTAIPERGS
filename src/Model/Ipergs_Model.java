package Model;

import Model.Entities.IpergsLctos;
import Model.Entities.LctoTxt;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Ipergs_Model {

    public String status = "";

    private File arquivo;
    private List<IpergsLctos> lctos = new ArrayList<>();

    public Ipergs_Model(File arquivo) {
        this.arquivo = arquivo;
        montarListaLctos();
    }

    private void montarListaLctos() {
        try {
            LctosTxtEmprestimos_Model txt_model = new LctosTxtEmprestimos_Model(arquivo);
            if (txt_model.status) {

                List<LctoTxt> lctosTxt = txt_model.getLctos();
                for (int i = 0; i < lctosTxt.size(); i++) {
                    LctoTxt lctoTxt = lctosTxt.get(i);
                    //if (lctoTxt.getValorRecebido() > 0) {
                        lctos.add(
                                new IpergsLctos(
                                        Long.valueOf("" + lctoTxt.getMatricula_estado()
                                                + getNumberNN(lctoTxt.getMatricula_vinculo())
                                                + getNumberNN(lctoTxt.getMatricula_pensionista())),
                                        lctoTxt.getNomeAssociado(),
                                        lctoTxt.getCpfAssociado(),
                                        lctoTxt.getSituacao(),
                                        lctoTxt.getValorRecebido())
                        );
                    //}else{
                    //    System.out.println("Valor zerado");
                    //}
                }

            } else {
            }
        } catch (Exception e) {
        }
    }
    
    private String getStringDouble(double number){
        return String.format("%.2f", number);
    }

    private String getNumberNN(int number) {
        return (number < 10 ? "0" : "") + number;
    }

    public List<IpergsLctos> getLctos() {
        return lctos;
    }

    public void testLctos() {
        for (int i = 0; i < lctos.size(); i++) {
            IpergsLctos lcto = lctos.get(i);
            System.out.println(
                    lcto.getMatricula() + " - "
                    + lcto.getNome() + " - "
                    + lcto.getSituacao() + " - "
                    + getStringDouble(lcto.getValor())
            );
        }
    }
}
