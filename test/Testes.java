
import View.FactaView;
import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import main.ComparativoFACTAIPERGS;

public class Testes {

    public static void main(String[] args) {
        all();
    }
    
    public static void all(){
        File folder = new File("C:\\Users\\ti01\\Documents\\NetBeans Projects\\Databases");
        File facta = new File(folder.getAbsolutePath() + "\\facta.txt");
        File ipe = new File(folder.getAbsolutePath() + "\\ipe.txt");
        
        Integer year = 2020;
        Integer month = 0;
        
        Calendar filesMonth = Calendar.getInstance();
        filesMonth.set(Calendar.YEAR, year);
        filesMonth.set(Calendar.MONTH, month);
        
        ComparativoFACTAIPERGS.setArquivoFacta(facta);
        ComparativoFACTAIPERGS.setArquivoIpergrs(ipe);
        ComparativoFACTAIPERGS.setFilesMonth(filesMonth);
        ComparativoFACTAIPERGS.setLocalSalvar(folder);
        ComparativoFACTAIPERGS.createFinalFiles();
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
