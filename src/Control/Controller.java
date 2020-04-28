package Control;

import Entity.Executavel;
import Model.Entities.FactaLctos;
import Model.Entities.IpergsLctos;
import Model.Facta_Model;
import Model.Ipergs_Model;
import java.io.File;
import java.util.List;

public class Controller {
    private List<FactaLctos> factaLctos;
    private List<IpergsLctos> ipergsLctos;
    
    public class setFactaLctos extends Executavel{
        File file;

        public setFactaLctos(File file) {
            this.file = file;
            nome = "Buscar informações do arquivo FACTA";
        }
        
        @Override
        public void run() {
            Facta_Model model = new Facta_Model(file);
            factaLctos = model.getLctos();
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
            Ipergs_Model model = new Ipergs_Model(file);
            ipergsLctos = model.getLctos();
        }
        
    }
}
