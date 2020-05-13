
import Auxiliar.Valor;
import View.FactaView;
import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Testes {

    public static void main(String[] args) {
        factaView();
    }

    public static void factaView() {
        List<Valor> totals =  new ArrayList<>();
        List<Object[]> monthContracts = new ArrayList<>();
        File saveFolder = new File("C:\\Users\\ti01\\Documents\\NetBeans Projects\\Java-ComparativoFACTAIPERGS");
        Calendar monthWorked =  Calendar.getInstance();
        
        FactaView view = new FactaView(totals, monthContracts,saveFolder, monthWorked);
        view.createExcelFile();
        
    }
}
