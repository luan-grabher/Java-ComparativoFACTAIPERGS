package View;

import JExcel.JExcel;
import Model.Entities.IpergsLcto;
import Model.Entities.MonthContract;
import Model.Entities.WarningOrError;
import java.io.File;
import java.io.FileInputStream;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class WarningsView {

    private XSSFWorkbook workbook;
    private XSSFSheet sheet;

    private final Calendar inclusionCalendar;
    private final Calendar referenceCalendar;
    private final File saveFolder;
    private final List<WarningOrError> warnings;

    public WarningsView(File saveFolder, List<WarningOrError> warnings, Calendar month) {
        this.saveFolder = saveFolder;
        this.inclusionCalendar = month;
        this.warnings = warnings;
        
        this.referenceCalendar = Calendar.getInstance();
        this.referenceCalendar.setTime(month.getTime());
        this.referenceCalendar.add(Calendar.MONTH, +1);
    }

    public void createExcelFile() {
        //abre o template
        openTemplate();
        printTitle();
        printWarnings();

        saveFile();
    }

    private void openTemplate() {
        try {
            File templateFile = new File("WarningTemplate.xlsx");
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

    private void printTitle() {
        String defaultTitle = "AVISOS E ERROS REFERENTE À INCLUSÃO #inclusionMonth REF. #referenceCalendarMonth/#referenceCalendarYear";

        String title = defaultTitle.replaceAll("#inclusionMonth", inclusionCalendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault()));
        title = title.replaceAll("#referenceCalendarMonth", "" + (referenceCalendar.get(Calendar.MONTH) + 1));
        title = title.replaceAll("#referenceCalendarYear", "" + referenceCalendar.get(Calendar.YEAR));
        title = title.toUpperCase();

        sheet.getRow(0).getCell(0).setCellValue(title);
    }

    private void printWarnings() {
        Integer initialRow = 2;
        Integer count = 0;
        for (WarningOrError warning : warnings) {
            count++;
            XSSFRow row = sheet.createRow(initialRow + count);
            
            row.createCell(0).setCellValue(warning.getType());
            row.createCell(1).setCellValue(warning.getLocate());
            row.createCell(2).setCellValue(warning.getIdentificator());
            row.createCell(3).setCellValue(warning.getDescription());
            row.createCell(4).setCellValue(warning.getDetail());
        }
        
        sheet.setAutoFilter(new CellRangeAddress(initialRow, count, 0, 4));
    }

    private void saveFile() {
        String defaultName = "Avisos e Erros " + (inclusionCalendar.get(Calendar.MONTH) + 1) + " " + inclusionCalendar.get(Calendar.YEAR);
        JExcel.saveWorkbookAs(new File(saveFolder.getAbsolutePath() + "\\" + defaultName + ".xlsx"), workbook);
    }
}
