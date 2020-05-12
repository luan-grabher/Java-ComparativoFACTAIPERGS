package Model;

import Model.Entities.IpergsLcto;
import Model.Entities.LctoTxt;
import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class IpergsModel {
    public static List<IpergsLcto> getFileList(File file) {
        List<IpergsLcto> lctos = new ArrayList<>();

        try {
            LctosTxtEmprestimos_Model txt_model = new LctosTxtEmprestimos_Model(file);
            if (txt_model.status) {

                List<LctoTxt> lctosTxt = txt_model.getLctos();
                for (int i = 0; i < lctosTxt.size(); i++) {
                    LctoTxt lctoTxt = lctosTxt.get(i);
                    lctos.add(new IpergsLcto(
                                    lctoTxt.getAssociadoCodigo(),
                                    Long.valueOf("" + lctoTxt.getMatricula_estado()
                                            + getNumberNN(lctoTxt.getMatricula_vinculo())
                                            + getNumberNN(lctoTxt.getMatricula_pensionista())),
                                    lctoTxt.getNomeAssociado(),
                                    lctoTxt.getCpfAssociado(),
                                    lctoTxt.getSituacao(),
                                    new BigDecimal(lctoTxt.getValorRecebido())
                            )
                    );
                }

            } else {
            }
        } catch (Exception e) {
        }

        return lctos;
    }

    private static String getNumberNN(int number) {
        return (number < 10 ? "0" : "") + number;
    }
    
    public static Double getTotal(List<IpergsLcto> ipergsLctos){
        return ipergsLctos.stream().mapToDouble(i -> i.getValor().doubleValue()).sum();
    }
}
