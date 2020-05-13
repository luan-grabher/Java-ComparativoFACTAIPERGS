package View;

import Auxiliar.Valor;
import JExcel.JExcel;
import Model.Entities.IpergsLcto;
import java.io.File;
import java.io.FileInputStream;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import tpsdb.Model.Entities.Contrato;

public class FactaView {

    private final List<Valor> totals;
    private final List<Object[]> monthContracts;
    private final Calendar monthWorked;
    private final Calendar lastMonth;
    private final File saveFolder;
    
    private final Integer initialRow = 2;
    private Integer countContracts = 0;
    
    private XSSFWorkbook workbook;
    private XSSFSheet sheet;

    public FactaView(List<Valor> totals, List<Object[]> monthContracts,File saveFolder, Calendar monthWorked) {
        this.totals = totals;
        this.monthContracts = monthContracts;
        this.saveFolder = saveFolder;
        this.monthWorked = monthWorked;
        
        this.lastMonth = Calendar.getInstance();
        this.lastMonth.setTime(monthWorked.getTime());
        this.lastMonth.add(Calendar.MONTH, -1);
    }

    public void createExcelFile() {
        //abre o template
        openTemplate();
        setTitle();
        printMonthContracts();
        saveFile();
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
        String defaultTitle = "REPASSE REFERENTE À INCLUSÃO #lastMonthName REF. #month/#year";
        
        String title = defaultTitle.replaceAll("#lastMonthName",lastMonth.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault()));
        title = title.replaceAll("#month","" + (monthWorked.get(Calendar.MONTH)+1));
        title = title.replaceAll("#year","" + monthWorked.get(Calendar.YEAR));
        title = title.toUpperCase();
        
        sheet.getRow(0).getCell(0).setCellValue(title);
    }
    
    private void printMonthContracts(){                
        for (Object[] monthContract : monthContracts) {
            countContracts++;
            
            IpergsLcto ipergs = (IpergsLcto) monthContract[1];
            Contrato contract = (Contrato) monthContract[1];
            
            XSSFRow row = sheet.getRow(initialRow + countContracts);
            row.getCell(0).setCellValue(countContracts);
            row.getCell(1).setCellValue(contract.getNumeroProposta());
            row.getCell(2).setCellValue(ipergs.getMatricula());
            row.getCell(3).setCellValue(ipergs.getCpf());
            row.getCell(4).setCellValue(ipergs.getNome());
            row.getCell(5).setCellValue(contract.getQtdParcelas());
            row.getCell(6).setCellValue(ipergs.getValor().doubleValue());
            row.getCell(7).setCellValue(contract.getValorFinanciado().doubleValue());
        }
    }
    
    private void printTitles(){
        
    }
    
    private void saveFile(){
        String defaultName = "Repasse FACTA " + (monthWorked.get(Calendar.MONTH) + 1) + " " + monthWorked.get(Calendar.YEAR);
        JExcel.saveWorkbookAs(new File(saveFolder.getAbsolutePath() + "\\" + defaultName + ".xlsx"), workbook);
    }
}
