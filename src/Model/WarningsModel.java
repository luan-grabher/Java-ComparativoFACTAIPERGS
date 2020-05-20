package Model;

import Dates.Dates;
import Model.Entities.IpergsLcto;
import Model.Entities.WarningOrError;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import tpsdb.Model.Entities.Contrato;
import tpsdb.Model.Tps_Model;

public class WarningsModel {

    private final List<IpergsLcto> ipergsLctos;
    private final List<Contrato> contracts;
    private List<WarningOrError> warnings = new ArrayList<>();
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

    public List<WarningOrError> getWarnings() {
        return warnings;
    }

    public void setIpergsWarnings() {
        //Ordena contratos para facilitar verificações
        //contracts.sort(Comparator.comparing(Contrato::getNumeroProposta));
        String locate = "Arquivo IPERGS";

        //Percorre lançamentos
        for (IpergsLcto ipergsLcto : ipergsLctos) {
            String identificator = "Matricula " + ipergsLcto.getMatricula();

            String warn = "Matricula do arquivo do IPERGS '" + ipergsLcto.getMatricula() + "' ";
            //Verifica se encontrou associado
            if (ipergsLcto.getNome().equals("")) {
                WarningOrError error = new WarningOrError();
                error.setType("Erro");
                error.setLocate(locate);
                error.setIdentificator(identificator);
                error.setDescription("Matricula não encontrada no sistema");
                error.setDetail("Observação no arquivo do IPERGS: " + ipergsLcto.getObs());

                warnings.add(error);
            } else {
                identificator += " - " + ipergsLcto.getNome();

                //Procura contratos do associado
                List<Contrato> associateContracts = contracts.stream().filter(c -> c.getAssociadoCodigo() == ipergsLcto.getAssociadoCodigo()).collect(Collectors.toList());

                if (associateContracts.isEmpty()) {
                    WarningOrError error = new WarningOrError();
                    error.setType("Erro");
                    error.setLocate(locate);
                    error.setIdentificator(identificator);
                    error.setDescription("O Associado não possui contratos no sistema em nenhuma data");
                    
                    warnings.add(error);
                } else {
                    //Avisa se tiver mais de um contrato
                    if (associateContracts.size() > 1) {
                        WarningOrError error = new WarningOrError();
                        error.setType("Erro");
                        error.setLocate(locate);
                        error.setIdentificator(identificator);
                        error.setDescription("O Associado possui mais de 1 contrato no sistema");
                        error.setDetail("Total de Contratos: " + associateContracts.size());

                        warnings.add(error);
                    }

                    //Percorre contratos para buscar um contrato de meses futuros
                    for (Contrato associateContract : associateContracts) {
                        if (associateContract.getDataProposta().after(nextMonth)) {
                            WarningOrError error = new WarningOrError();
                            error.setType("Erro");
                            error.setLocate(locate);
                            error.setIdentificator(identificator);
                            error.setDescription("O Associado possui um contrato futuro");
                            error.setDetail("Número Contrato: " + associateContract.getNumeroProposta());

                            warnings.add(error);
                        }
                    }
                }
            }
        }
    }

    public void setContractsWarnings() {
        String locate = "Contratos";
        for (Contrato contract : contracts) {
            String identificator = "Contrato " + contract.getNumeroProposta();

            Calendar date = contract.getDataProposta();
            if (date.get(Calendar.MONTH) == month.get(Calendar.MONTH) && date.get(Calendar.YEAR) == month.get(Calendar.YEAR)) {
                List<IpergsLcto> listIpergs = ipergsLctos.stream().filter(i -> i.getAssociadoCodigo() == contract.getAssociadoCodigo()).collect(Collectors.toList());

                if (!ipergsLctos.stream().anyMatch(i -> i.getAssociadoCodigo() == contract.getAssociadoCodigo())) {
                    WarningOrError error = new WarningOrError();
                    error.setType("Erro");
                    error.setLocate(locate);
                    error.setIdentificator(identificator);
                    error.setDescription("O Contrato possui a data da proposta no mês " + (month.get(Calendar.MONTH) + 1) + " e não aparece no arquivo do IPERGS.");
                    error.setDetail("Data proposta: " + (new SimpleDateFormat("dd/mm/YYYY").format(date.getTime())));
                    
                    warnings.add(error);
                }
            }
        }
    }
}
