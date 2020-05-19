package Model;

import Model.Entities.IpergsLcto;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;
import tpsdb.Model.Entities.Contrato;
import tpsdb.Model.Tps_Model;

public class WarningsModel {
    private final List<IpergsLcto> ipergsLctos;
    private final List<Contrato> contracts;
    private List<String> warnings = new ArrayList<>();
    private final Calendar month;
    private final Calendar nextMonth;

    public WarningsModel(List<IpergsLcto> ipergsLctos, Calendar month) {
        this.ipergsLctos = ipergsLctos;
        this.contracts = Tps_Model.getContratos();
        this.month = month;
        
        nextMonth = Calendar.getInstance();
        nextMonth.setTime(this.month.getTime());
        nextMonth.add(Calendar.MONTH, 1); //proximo mes
        nextMonth.add(Calendar.DAY_OF_MONTH, -1); //ultimo dia do mes
    }

    public List<String> getWarnings() {
        return warnings;
    }
    
    public void setIpergsWarnings(){
        //Percorre lançamentos
        for (IpergsLcto ipergsLcto : ipergsLctos) {
            String warn = "Matricula do arquivo do IPERGS '" + ipergsLcto.getMatricula() + "' ";
            //Verifica se encontrou associado
            if(ipergsLcto.getNome().equals("")){
                warnings.add(warn + "não encontrada no sistema. A observação no arquivo é: " + ipergsLcto.getObs());
            }else{
                warn += " com associado de nome '" + ipergsLcto.getNome() + "' ";
                
                //Procura contratos do associado
                List<Contrato> associateContracts = contracts.stream().filter(c -> c.getAssociadoCodigo() == ipergsLcto.getAssociadoCodigo()).collect(Collectors.toList());
                
                if(associateContracts.isEmpty()){
                    warnings.add(warn + "não possui contratos no sistema em nenhuma data.");
                }else{
                    //Avisa se tiver mais de um contrato
                    if(associateContracts.size() > 1){
                        warnings.add(warn + "possui mais de 1 contrato no sistema. Ao total são " + associateContracts.size());
                    }
                    
                    //Percorre contratos para buscar um contrato de meses futuros
                    for (Contrato associateContract : associateContracts) {
                        if(associateContract.getDataProposta().after(nextMonth)){
                            warnings.add(warn + " possui um contrato futuro de número " + associateContract.getNumeroProposta());
                        }
                    }
                }
            }
        }
    }
    
    public void setContractsWarnings(){
        for (Contrato contract : contracts) {
            String warn = "O contrato de número " + contract.getNumeroProposta();
            Calendar date = contract.getDataProposta();
            if(date.get(Calendar.MONTH) == month.get(Calendar.MONTH) && date.get(Calendar.YEAR) == month.get(Calendar.YEAR)){
                List<IpergsLcto> listIpergs = ipergsLctos.stream().filter(i -> i.getAssociadoCodigo() == contract.getAssociadoCodigo()).collect(Collectors.toList());
                
                if(!ipergsLctos.stream().anyMatch(i -> i.getAssociadoCodigo() == contract.getAssociadoCodigo())){
                    warnings.add(warn + "possui a data da proposta no mês " + month.get(Calendar.MONTH) + " e não aparece no arquivo do IPERGS.");
                }
            }
        }
    }
}
