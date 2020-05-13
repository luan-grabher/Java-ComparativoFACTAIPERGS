
import Auxiliar.Valor;
import View.FactaView;
import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Testes {

    public static void main(String[] args) {
        factaView();
    }

    public static void factaView() {
        Map<String, BigDecimal> totals =  new HashMap<>();
        List<Object[]> monthContracts = new ArrayList<>();
        File saveFolder = new File("C:\\Users\\ti01\\Documents\\NetBeans Projects\\Java-ComparativoFACTAIPERGS");
        Calendar monthWorked =  Calendar.getInstance();
        
        FactaView view = new FactaView(totals, monthContracts,saveFolder, monthWorked);
        view.createExcelFile();
        
    }
}
