package View;

import Auxiliar.Valor;
import java.util.List;
import tpsdb.Model.Entities.Contrato;

public class FactaView {
    private final List<Valor> totals;
    private final List<Object[]> monthContracts;

    public FactaView(List<Valor> totals, List<Object[]> monthContracts) {
        this.totals = totals;
        this.monthContracts = monthContracts;
    }
    
    public void createExcelFile(){
        
    }
    
}
