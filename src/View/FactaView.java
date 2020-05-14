package View;

import Auxiliar.Valor;
import JExcel.JExcel;
import Model.Entities.IpergsLcto;
import java.io.File;
import java.io.FileInputStream;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import org.apache.poi.xssf.usermodel.XSSFFormulaEvaluator;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import tpsdb.Model.Entities.Contrato;

public class FactaView {

    private final Map<String, BigDecimal> totals;
    private final List<Object[]> monthContracts;
    private final Calendar monthWorked;
    private final Calendar lastMonth;
    private final File saveFolder;

    private final Integer initialRow = 2;
    private Integer countContracts = 0;

    private final String cellName = "E";
    private final String cellValue = "F";

    private final Integer cellNameInt = JExcel.Cell(cellName);
    private final Integer cellValueInt = JExcel.Cell(cellValue);

    private XSSFWorkbook workbook;
    private XSSFSheet sheet;

    public FactaView(Map<String, BigDecimal> totals, List<Object[]> monthContracts, File saveFolder, Calendar monthWorked) {
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
        for (Object[] monthContract : monthContracts) {
            countContracts++;

            IpergsLcto ipergs = monthContract[0];
            Contrato contract = (Contrato) monthContract[1];

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
        printTotal(rowSefaz - 1, "Total IPERGS SEFAZ 1%", "ROUND(" + cellValue + rowIpergs + " * 0.01,2)");
        printTotal(rowIpergsLiquid - 1, "Total IPERGS Liquido", cellValue + rowIpergs + "-" + cellValue + rowSefaz);

        printTotal(rowPmt - 1, "PMT SINAPERS 0,5%", "ROUND(" + cellValue + rowIpergsLiquid + " * 0.005,2)");
        printTotal(rowSales - 1, "Total Venda", financed.doubleValue()); //Financiado
        printTotal(rowSalesOnePercent - 1, "Valor Venda 1%", "ROUND(" + cellValue + rowSales + " * 0.01,2)");
        printTotal(rowSettingUnrecordedMonthlyFees - 1, "Acerto Mensalidades Não Averbadas", new Double("0"));

        printTotal(rowSinapers - 1, "Total SINAPERS", cellValue + rowPmt + "+" + cellValue + rowSalesOnePercent + "+" + cellValue + rowSettingUnrecordedMonthlyFees);
        printTotal(rowFacta - 1, "Total Repasse FACTA", cellValue + rowIpergsLiquid + "-" + cellValue + rowSinapers);
    }

    private void printTotal(Integer rowNumber, String nameValue, Double value) {
        printTotal(rowNumber, nameValue, "", value);
    }

    private void printTotal(Integer rowNumber, String nameValue, String formula) {
        printTotal(rowNumber, nameValue, formula, null);
    }

    private void printTotal(Integer rowNumber, String nameValue, String formula, Double value) {
        XSSFRow row = sheet.createRow(rowNumber);
        row.createCell(cellNameInt).setCellValue(nameValue);
        if (!formula.equals("")) {
            row.createCell(cellValueInt).setCellFormula(formula);
        } else {
            row.createCell(cellValueInt).setCellValue(value);
        }
    }

    private void saveFile() {
        String defaultName = "Repasse FACTA " + (monthWorked.get(Calendar.MONTH) + 1) + " " + monthWorked.get(Calendar.YEAR);
        JExcel.saveWorkbookAs(new File(saveFolder.getAbsolutePath() + "\\" + defaultName + ".xlsx"), workbook);
    }
}
