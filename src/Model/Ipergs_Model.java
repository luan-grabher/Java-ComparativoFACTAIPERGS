package Model;

import Model.Entities.IpergsLctos;
import Model.Entities.LctoTxt;
import java.io.File;
import java.math.BigDecimal;
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
                    lctos.add(
                            new IpergsLctos(
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
    }

    private String getStringDouble(double number) {
        return String.format("%.2f", number);
    }

    private String getNumberNN(int number) {
        return (number < 10 ? "0" : "") + number;
    }

    public List<IpergsLctos> getLctos() {
        return lctos;
    }
}
