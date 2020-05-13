package View;

import Auxiliar.Valor;
import JExcel.JExcel;
import java.io.File;
import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class FactaView {

    private final List<Valor> totals;
    private final List<Object[]> monthContracts;
    private final Calendar monthWorked;
    private final File saveFolder;
    
    private XSSFWorkbook workbook;
    private XSSFSheet sheet;

    public FactaView(List<Valor> totals, List<Object[]> monthContracts,File saveFolder, Calendar monthWorked) {
        this.totals = totals;
        this.monthContracts = monthContracts;
        this.saveFolder = saveFolder;
        this.monthWorked = monthWorked;
    }

    public void createExcelFile() {
        //abre o template
        openTemplate();
        setTitle();
        
    }

    private void openTemplate() {
        try {
            File templateFile = new File("TemplateFacta.xlsx");
            if (templateFile.exists()) {
                FileInputStream templateFileInputStream = new FileInputStream(templateFile);

                workbook = new XSSFWorkbook(templateFileInputStream);
                sheet = workbook.getSheetAt(0);
            } else {
                throw new Error("O arquivo de template não existe na pasta do programa, por favor, reinstale o programa. Se o erro persistir, contate o programador.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new Error(e.getMessage());
        }
    }

    private void setTitle(){
        String defaultTitle = "REPASSE REFERENTE À INCLUSÃO #monthName REF. #month/#year";
        
        String title = defaultTitle.replaceAll("#monthName#",monthWorked.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault()));
        title = title.replaceAll("#month","" + monthWorked.get(Calendar.MONTH));
        title = title.replaceAll("#year","" + monthWorked.get(Calendar.YEAR));
        title = title.toUpperCase();
        
        sheet.getRow(0).getCell(0).setCellValue(title);
    }
    
    private void saveFile(){
        String defaultName = "Repasse FACTA " + monthWorked.get(Calendar.MONTH) + monthWorked.get(Calendar.YEAR);
        JExcel.saveWorkbookAs(new File(saveFolder.getAbsolutePath() + "\\" + defaultName + ".xlsx"), workbook);
    }
}
