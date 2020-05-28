package Control;

import Entity.Executavel;
import Model.Entities.FactaLcto;
import Model.Entities.IpergsLcto;
import Model.Entities.MonthContract;
import Model.Entities.WarningOrError;
import Model.FactaModel;
import Model.IpergsModel;
import Model.WarningsModel;
import View.FactaView;
import View.WarningsView;
import java.io.File;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import tpsdb.Model.Tps_Model;

public class Controller {
    private Calendar monthWorked;
    
    private List<FactaLcto> factaLctos;
    private List<IpergsLcto> ipergsLctos;
    private List<MonthContract> monthContracts;

    private List<WarningOrError> warnings;

    private Map<String, BigDecimal> totals;

    public class setMonthWorked extends Executavel{
        private Calendar month;
        public setMonthWorked(Calendar month) {
            this.month = month;
            name =  "Definindo mês dos arquivos";
        }

        @Override
        public void run() {
            monthWorked = month;
        }
        
    }
    
    public class setFactaLctos extends Executavel {

        File file;

        public setFactaLctos(File file) {
            this.file = file;
            name =  "Buscar informações do arquivo FACTA";
        }

        @Override
        public void run() {
            factaLctos = FactaModel.getFileList(file);
            factaLctos.sort(Comparator.comparing(FactaLcto::getNome));
        }

    }

    public class setIpergsLctos extends Executavel {

        File file;

        public setIpergsLctos(File file) {
            this.file = file;
            name =  "Buscar informações do arquivo IPERGS";
        }

        @Override
        public void run() {
            ipergsLctos = IpergsModel.getFileList(file);
            ipergsLctos.sort(Comparator.comparing(IpergsLcto::getNome));
        }

    }

    public class setAssociados extends Executavel {

        public setAssociados() {
            name =  "Buscando informações dos associados no banco de dados.";
        }

        @Override
        public void run() {
            Tps_Model.setAssociates();
        }

    }

    public class setContratos extends Executavel {

        public setContratos() {
            name =  "Buscando informações dos contratos dos associados do banco de dados";
        }

        @Override
        public void run() {
            Tps_Model.setContracts();
        }
    }

    public class setListMonthPersons extends Executavel {

        Calendar monthWork;

        public setListMonthPersons(Calendar monthWork) {
            this.monthWork = monthWork;
            name =  "Definindo lista de pessoas que estão no IPERGS e tem contrato no mês informado";
        }

        @Override
        public void run() {
            monthContracts = FactaModel.getMonthIPERGSContracts(ipergsLctos, monthWork);
            monthContracts.sort(Comparator.comparing(MonthContract::getName));
        }

    }

    public class setListTotals extends Executavel {

        public setListTotals() {
            name =  "Definindo totais";
        }

        @Override
        public void run() {
            totals = FactaModel.getTotals(ipergsLctos, monthContracts);
        }

    }

    public class createFactaFinalView extends Executavel {

        private final File saveFolder;
        private final Calendar monthWorked;

        public createFactaFinalView(File saveFolder, Calendar monthWorked) {
            this.saveFolder = saveFolder;
            this.monthWorked = monthWorked;
            name =  "Criando arquivo final FACTA";
        }

        @Override
        public void run() {
            FactaView view = new FactaView(totals, monthContracts, saveFolder, monthWorked);
            view.createExcelFile();
        }

    }

    public class createWarnings extends Executavel {

        private final Calendar monthWorked;

        public createWarnings(Calendar monthWorked) {

            this.monthWorked = monthWorked;
            name =  "Criando avisos e erros.";
        }

        @Override
        public void run() {
            WarningsModel model = new WarningsModel(ipergsLctos, monthWorked);
            model.setIpergsWarnings();
            model.setContractsWarnings();
            warnings = model.getWarnings();
        }

    }

    public class createWarningsView extends Executavel {

        private final File saveFolder;

        public createWarningsView(File saveFolder) {
            this.saveFolder = saveFolder;
            name =  "Criando arquivo de avisos e erros.";
        }

        @Override
        public void run() {
            WarningsView view = new WarningsView(saveFolder, warnings, monthWorked);
            view.createExcelFile();
        }

    }

}
