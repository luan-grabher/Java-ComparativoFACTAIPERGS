package Control;

import Entity.Executavel;
import Model.Entities.FactaLctos;
import Model.Facta_Model;
import java.io.File;
import java.util.List;

public class Controller {
    private List<FactaLctos> factaLctos;
    
    public class setFactaLctos extends Executavel{
        public String nome = "Buscando as informações do arquivo FACTA";
        File file;

        public setFactaLctos(File file) {
            this.file = file;
        }
        
        @Override
        public void run() {
            Facta_Model model = new Facta_Model(file);
            factaLctos = model.getLctos();
        }
        
    }
    
    public class setIpergsLctos extends Executavel{
        public String nome = "Buscando as informações do arquivo IPERGS";
        File file;

        public setIpergsLctos(File file) {
            this.file = file;
        }
        
        @Override
        public void run() {
            Facta_Model model = new Facta_Model(file);
            factaLctos = model.getLctos();
        }
        
    }
}
