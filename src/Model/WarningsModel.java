package Model;

import Model.Entities.IpergsLcto;
import java.util.List;
import tpsdb.Model.Entities.Contrato;
import tpsdb.Model.Tps_Model;

public class WarningsModel {
    private List<IpergsLcto> ipergsLctos;
    private List<Contrato> contracts;
    private List<String[]> warnings;

    public WarningsModel(List<IpergsLcto> ipergsLctos) {
        this.ipergsLctos = ipergsLctos;
        this.contracts = Tps_Model.getContratos();
    }
    
    
    
    
}
