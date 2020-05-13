package Control;

import Auxiliar.Valor;
import Entity.Executavel;
import Model.Entities.FactaLcto;
import Model.Entities.IpergsLcto;
import Model.FactaModel;
import Model.IpergsModel;
import View.FactaView;
import java.io.File;
import java.util.Calendar;
import java.util.List;
import tpsdb.Model.Tps_Model;

public class Controller {
    private List<FactaLcto> factaLctos;
    private List<IpergsLcto> ipergsLctos;
    private List<Object[]> monthContracts;
    
    private List<Valor> totals;
    
    public class setFactaLctos extends Executavel{
        File file;

        public setFactaLctos(File file) {
            this.file = file;
            nome = "Buscar informações do arquivo FACTA";
        }
        
        @Override
        public void run() {
            factaLctos = FactaModel.getFileList(file);
        }
        
    }
    
    public class setIpergsLctos extends Executavel{
        File file;

        public setIpergsLctos(File file) {            
            this.file = file;
            nome = "Buscar informações do arquivo IPERGS";
        }
        
        @Override
        public void run() {
            ipergsLctos = IpergsModel.getFileList(file);
        }
        
    }
    
    public class setAssociados extends Executavel{

        public setAssociados() {
            nome = "Buscando informações dos associados no banco de dados.";
        }

        @Override
        public void run() {
            Tps_Model.setAssociados();
        }
        
    }
    
    public class setContratos extends Executavel{

        public setContratos() {
            nome = "Buscando informações dos contratos dos associados do banco de dados";
        }

        @Override
        public void run() {
            Tps_Model.setContratos();
        }
    }
    
    public class setTPSDatabase extends Executavel{

        public setTPSDatabase() {
            nome = "Buscando dados do banco de dados...";
        }

        @Override
        public void run() {
            Tps_Model.setAssociados();
            Tps_Model.setContratos();
        }
        
    }
    
    public class setListMonthPersons extends Executavel{
        Calendar monthWork;
        
        public setListMonthPersons(Calendar monthWork) {
            this.monthWork = monthWork;
            nome = "Definindo lista de pessoas que estão no IPERGS e tem contrato no mês informado";
        }

        @Override
        public void run() {
            monthContracts = FactaModel.getMonthIPERGSContracts(ipergsLctos, monthWork);
        }
        
    }
    
    public class setListTotals extends Executavel{

        public setListTotals() {
            nome = "Definindo totais";
        }

        @Override
        public void run() {
            totals = FactaModel.getTotals(ipergsLctos, monthContracts);
        }
        
    }
    
    public class createFactaFinalView extends Executavel{

        public createFactaFinalView() {
            nome = "Criando arquivo final FACTA";
        }

        @Override
        public void run() {
            FactaView view = new FactaView(totals, monthContracts);
            view.createExcelFile();
        }
        
    }
}
