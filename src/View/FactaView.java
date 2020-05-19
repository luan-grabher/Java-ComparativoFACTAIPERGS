package View;

import Auxiliar.Valor;
import JExcel.JExcel;
import JExcel.JExcelStyles;
import Model.Entities.IpergsLcto;
import Model.Entities.MonthContract;
import java.io.File;
import java.io.FileInputStream;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Color;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFFormulaEvaluator;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import tpsdb.Model.Entities.Contrato;

public class FactaView {

    private final Map<String, BigDecimal> totals;
    private final List<MonthContract> monthContracts;
    private final Calendar monthWorked;
    private final Calendar lastMonth;
    private final File saveFolder;

    private final Integer initialRow = 2;
    private Integer countContracts = 0;

    private final String titleCellName = "E";
    private final String titleCellValue = "F";

    private final Integer titleCellNameInt = JExcel.Cell(titleCellName);
    private final Integer titleCellValueInt = JExcel.Cell(titleCellValue);

    private XSSFWorkbook workbook;
    private XSSFSheet sheet;

    public FactaView(Map<String, BigDecimal> totals, List<MonthContract> monthContracts, File saveFolder, Calendar monthWorked) {
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
        printTitle();
        printMonthContracts();
        printTotals();
        setStyles();

        XSSFFormulaEvaluator.evaluateAllFormulaCells(workbook);
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

    private void printTitle() {
        String defaultTitle = "REPASSE REFERENTE À INCLUSÃO #lastMonthName REF. #month/#year";

        String title = defaultTitle.replaceAll("#lastMonthName", lastMonth.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault()));
        title = title.replaceAll("#month", "" + (monthWorked.get(Calendar.MONTH) + 1));
        title = title.replaceAll("#year", "" + monthWorked.get(Calendar.YEAR));
        title = title.toUpperCase();

        sheet.getRow(0).getCell(0).setCellValue(title);
    }

    private void printMonthContracts() {
        for (MonthContract monthContract : monthContracts) {
            countContracts++;

            IpergsLcto ipergs = monthContract.getIpergs();
            Contrato contract = monthContract.getContract();

            XSSFRow row = sheet.createRow(initialRow + countContracts);
            row.createCell(0).setCellValue(countContracts);
            row.createCell(1).setCellValue(contract.getNumeroProposta());
            row.createCell(2).setCellValue(ipergs.getMatricula());
            row.createCell(3).setCellValue(ipergs.getCpf());
            row.createCell(4).setCellValue(ipergs.getNome());
            row.createCell(5).setCellValue(contract.getQtdParcelas());
            row.createCell(6).setCellValue(ipergs.getValor().doubleValue());
            row.createCell(7).setCellValue(contract.getValorFinanciado().doubleValue());
        }
        
        //imprime totalizador
        XSSFRow rowTotalizer = sheet.createRow(initialRow + countContracts + 1);
        XSSFCell cellTotalizer =  rowTotalizer.createCell(7);
        cellTotalizer.setCellFormula("SUM(H" + (initialRow + 1) + ":H" + (initialRow + countContracts + 1) + ")");
        cellTotalizer.setCellStyle(getStyle_Value());
        cellTotalizer.getCellStyle().setFont(getFont_Bold());
    }

    private void printTotals() {
        Integer totalsStartRow = initialRow + countContracts + 2;

        //Defini totais
        //Auxiliares
        //BigDecimal onePercent = new BigDecimal("0.01");
        //BigDecimal halfPercent = new BigDecimal("0.005");
        //Totals
        BigDecimal ipergs = totals.get("ipergs");
        //BigDecimal sefaz = ipergs.multiply(onePercent);
        //BigDecimal ipergsLiquid = ipergs.add(sefaz.negate());

        //BigDecimal sinapersHalfPercent = ipergsLiquid.multiply(halfPercent);
        BigDecimal financed = totals.get("financed");
        //BigDecimal salesSinapersPercent = financed.multiply(onePercent);

        //Rows
        Integer rowIpergs = totalsStartRow + 2;
        Integer rowSefaz = totalsStartRow + 3;
        Integer rowIpergsLiquid = totalsStartRow + 4;

        Integer rowPmt = totalsStartRow + 6;
        Integer rowSales = totalsStartRow + 7;
        Integer rowSalesOnePercent = totalsStartRow + 8;
        Integer rowSettingUnrecordedMonthlyFees = totalsStartRow + 9;

        Integer rowSinapers = totalsStartRow + 11;
        Integer rowFacta = totalsStartRow + 12;

        //Define Titulos
        printTotal(rowIpergs - 1, "Total IPERGS", ipergs.doubleValue());
        printTotal(rowSefaz - 1, "Total IPERGS SEFAZ 1%", "ROUND(" + titleCellValue + rowIpergs + " * 0.01,2)");
        printTotal(rowIpergsLiquid - 1, "Total IPERGS Liquido", titleCellValue + rowIpergs + "-" + titleCellValue + rowSefaz);

        printTotal(rowPmt - 1, "PMT SINAPERS 0,5%", "ROUND(" + titleCellValue + rowIpergsLiquid + " * 0.005,2)");
        printTotal(rowSales - 1, "Total Venda", financed.doubleValue()); //Financiado
        printTotal(rowSalesOnePercent - 1, "Valor Venda 1%", "ROUND(" + titleCellValue + rowSales + " * 0.01,2)");
        printTotal(rowSettingUnrecordedMonthlyFees - 1, "Acerto Mensalidades Não Averbadas", new Double("0"));

        printTotal(rowSinapers - 1, "Total SINAPERS", titleCellValue + rowPmt + "+" + titleCellValue + rowSalesOnePercent + "+" + titleCellValue + rowSettingUnrecordedMonthlyFees);
        printTotal(rowFacta - 1, "Total Repasse FACTA", titleCellValue + rowIpergsLiquid + "-" + titleCellValue + rowSinapers);
    }

    private void printTotal(Integer rowNumber, String nameValue, Double value) {
        printTotal(rowNumber, nameValue, "", value);
    }

    private void printTotal(Integer rowNumber, String nameValue, String formula) {
        printTotal(rowNumber, nameValue, formula, null);
    }

    private void printTotal(Integer rowNumber, String nameValue, String formula, Double value) {
        XSSFRow row = sheet.createRow(rowNumber);
        row.createCell(titleCellNameInt).setCellValue(nameValue);
        if (!formula.equals("")) {
            row.createCell(titleCellValueInt).setCellFormula(formula);
        } else {
            row.createCell(titleCellValueInt).setCellValue(value);
        }
    }

    private void setStyles() {
        for (int i = 0; i <= sheet.getLastRowNum(); i++) {
            XSSFRow row = sheet.getRow(i);

            if (row != null) {
                //Se tiver valor nas 3 primeiras colunas
                if ((new Valor(JExcel.getStringCell(row.getCell(0)))).getBigDecimal().compareTo(BigDecimal.ZERO) == 1
                        && !JExcel.getStringCell(row.getCell(1)).equals("")
                        && !JExcel.getStringCell(row.getCell(2)).equals("")) {
                    
                    JExcelStyles.setStylesInRange(sheet, getStyle_Default(), i, i, 0, 5);
                    JExcelStyles.setStylesInRange(sheet, getStyle_Value(), i, i, 6, 7);
                    
                } else if (!JExcel.getStringCell(row.getCell(titleCellNameInt)).equals("")
                        && (!JExcel.getStringCell(row.getCell(titleCellValueInt)).replaceAll("[^0-9]", "").equals("")
                        || row.getCell(titleCellValueInt).getCellType() == CellType.FORMULA)) {
                    
                    row.getCell(titleCellNameInt).setCellStyle(getStyle_Default());
                    row.getCell(titleCellValueInt).setCellStyle(getStyle_Value());
                    
                    if(row.getCell(titleCellNameInt).getStringCellValue().equals("Total Repasse FACTA")){
                        row.getCell(titleCellNameInt).getCellStyle().setFont(getFont_Bold());
                        row.getCell(titleCellValueInt).getCellStyle().setFont(getFont_Bold());
                    }
                }
            }
        }
    }
    
    private XSSFCellStyle getStyle_Value() {
        XSSFCellStyle style = getStyle_Default();
        style.setDataFormat(8);//8 is the data format to money

        return style;
    }

    private XSSFCellStyle getStyle_Default() {
        XSSFCellStyle style = workbook.createCellStyle();
        JExcelStyles.setCellBorders(style);
        return style;
    }
    
    private XSSFFont getFont_Bold(){
        XSSFFont font = workbook.createFont();
        font.setBold(true);
        return font;
    }
    
    private XSSFFont getFont_Red(){
        XSSFFont font = workbook.createFont();
        font.setColor(IndexedColors.BLACK.index);
        return font;
    }

    private void saveFile() {
        String defaultName = "Repasse FACTA " + (monthWorked.get(Calendar.MONTH) + 1) + " " + monthWorked.get(Calendar.YEAR);
        JExcel.saveWorkbookAs(new File(saveFolder.getAbsolutePath() + "\\" + defaultName + ".xlsx"), workbook);
    }
}
