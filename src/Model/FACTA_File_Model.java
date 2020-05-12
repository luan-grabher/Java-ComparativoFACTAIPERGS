package Model;

import Model.Entities.IpergsLctos;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;
import tpsdb.Model.Entities.Contrato;
import tpsdb.Model.Tps_Model;

public class FACTA_File_Model {
    public void getMonthIPERGSContracts(List<IpergsLctos> ipergsLctos, Calendar monthWork){
        List<Object[]> monthContracts = new ArrayList<>();
        List<Contrato> contracts = Tps_Model.getContratos();
        
        //Percorre todos lançamentos IPERGS
        for (IpergsLctos ipergsLcto : ipergsLctos) {
            //Procura contratos no mês para aquele codigo de associado
            Long associado = ipergsLcto.getAssociadoCodigo();
            
            Optional<Contrato> contractOptional = contracts.stream().filter(
                    c -> c.getAssociadoCodigo() == associado 
                    && isCalendarsInTheSameMonth(c.getDataProposta(),monthWork)
            ).findFirst();
            
            if(contractOptional.isPresent()){
                Contrato contract = contractOptional.get();
                monthContracts.add(new Object[]{ipergsLcto, contract});
            }
        }
    }
    
    private boolean isCalendarsInTheSameMonth(Calendar calOne, Calendar calTwo){
        return calOne.get(Calendar.YEAR) == calTwo.get(Calendar.YEAR) && calOne.get(Calendar.MONTH) == calTwo.get(Calendar.MONTH);
    }
}
