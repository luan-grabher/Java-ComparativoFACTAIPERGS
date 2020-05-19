package Model;

import Model.Entities.IpergsLcto;
import java.util.Calendar;
import java.util.List;
import tpsdb.Model.Entities.Contrato;
import tpsdb.Model.Tps_Model;

public class WarningsModel {
    private List<IpergsLcto> ipergsLctos;
    private List<Contrato> contracts;
    private List<String> warnings;

    public WarningsModel(List<IpergsLcto> ipergsLctos) {
        this.ipergsLctos = ipergsLctos;
        this.contracts = Tps_Model.getContratos();
    }

    public List<String> getWarnings() {
        return warnings;
    }
    
    public void setIpergsWarnings(Calendar month){
        //Percorre lançamentos
        for (IpergsLcto ipergsLcto : ipergsLctos) {
            //Verifica se encontrou associado
            if(ipergsLcto.getNome().equals("")){
                warnings.add("Matricula do arquivo do IPERGS '" + ipergsLcto.getMatricula() + "' não encontrada no sistema. A observação no arquivo é: " + ipergsLcto.getSituacao());
            }else{
                //Procura contratos do associado
                
            }
        }
    }
    
    
}
