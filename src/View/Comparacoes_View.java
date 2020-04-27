package View;

import Model.Entities.Comparacoes;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFFormulaEvaluator;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class Comparacoes_View {

    private boolean criarTabelaTotaisFinal = true;
    private String nomeLista;
    public String status = "";
    private File localSalvar;
    private List<Comparacoes> listaComparacoes;

    public Comparacoes_View(List<Comparacoes> listaComparacoes, File localSalvar, String nomeLista) {
        Constructor(listaComparacoes, localSalvar, nomeLista);
    }

    public Comparacoes_View(List<Comparacoes> listaComparacoes, File localSalvar, String nomeLista, boolean criarTotais) {
        this.criarTabelaTotaisFinal = criarTotais;
        Constructor(listaComparacoes, localSalvar, nomeLista);
    }

    private void Constructor(List<Comparacoes> listaComparacoes, File localSalvar, String nomeLista) {
        this.listaComparacoes = listaComparacoes;
        this.localSalvar = localSalvar;
        this.nomeLista = nomeLista;

        gerarArquivoExcel();
    }

    /**
     * Coloca valores IPERGS e FACTA nas linhas, define estilos, cria totais
     */
    private void gerarArquivoExcel() {
        try {
            //Abre arquivo template
            File arquivoTemplate = new File("Template Comparativo.xlsx");
            if (arquivoTemplate.exists()) {
                FileInputStream arquivoWk = new FileInputStream(arquivoTemplate);

                XSSFWorkbook workbook = new XSSFWorkbook(arquivoWk);
                XSSFSheet sheet = workbook.getSheetAt(0);

                /**
                 * Cria Carregamento
                 */
                Carregamento carregamento = new Carregamento();
                carregamento.setVisible(true);

                final int maxComp = listaComparacoes.size();
                Carregamento.iniciar("Criando comparação", 0, maxComp + 3);

                //Percorre todas comparações
                for (int i = 0; i < maxComp; i++) {
                    /*Altera comparação*/
                    Carregamento.atualizar("Criando comparação " + i + " de " + maxComp, i);

                    Comparacoes comparacao = listaComparacoes.get(i);

                    //21335807804
                    //Soma totais
                    //totalPmt = totalPmt.add(comparacao.getPmt());
                    //totalIpergs = totalIpergs.add(comparacao.getIpergs());
                    try {
                        Row row = sheet.createRow(i + 1);

                        row.createCell(0).setCellValue(i + 1);
                        row.createCell(1).setCellValue(comparacao.getMatricula());
                        row.createCell(2).setCellValue(comparacao.getNome());
                        row.createCell(3).setCellValue(comparacao.getSituacao());

                        row.createCell(4).setCellValue(getMoney(comparacao.getDiferencaPMT()));
                        row.createCell(5).setCellValue(getMoney(comparacao.getIpergs()));
                        row.createCell(6).setCellValue(getMoney(comparacao.getValorParcela()));

                        row.createCell(7).setCellValue(getMoney(comparacao.getValorFinanciado()));

                        //row.createCell(8).setCellValue(getMoney(comparacao.getValorFinanciado_1perc()));
                        //row.createCell(9).setCellValue(getMoney(comparacao.getValorParcela_menos1perc_meioPerc()));
                    } catch (Exception e) {
                        System.out.println("Erro na linha " + i);
                    }
                }

                Carregamento.atualizar("Adicionando totais...", maxComp + 1);
                adicionaTotalLinhas(workbook, sheet);

                Carregamento.atualizar("Definindo estilos...", maxComp + 2);
                definirEstilos(workbook, sheet);

                Carregamento.atualizar("Salvando arquivo...", maxComp + 3);
                String nomeArquivo = "Comparação FACTA e IPERGS " + nomeLista + ".xlsx";
                if (!salvarWorkbookComo(arquivoWk, workbook, localSalvar.getAbsolutePath() + "/" + nomeArquivo)) {
                    status = "Erro ao salvar arquivo: " + nomeArquivo + "\nVocê está com ele aberto?";
                }

                carregamento.dispose();
            } else {
                status = "Template Comparativo Inexistente na pasta do programa!";
            }
        } catch (Exception e) {
            status = "Erro ao gerar arquivo excel com comparações: " + e;
        } catch (Error e) {
            status = "Erro ao gerar arquivo excel com comparações: " + e;
        }
    }

    /**
     * Formata toda a planilha Excel
     */
    private void definirEstilos(Workbook workbook, XSSFSheet sheet) {
        //Lista de estilos
        List<CellStyle> estilos = new ArrayList<>();
        int STYLE_bold_r$ = 0;
        int STYLE_bold_normal = 1;
        int STYLE_r$ = 2;
        int STYLE_normal = 3;
        int STYLE_bold_r$_red = 4;

        //Popula lista
        for (int i = 0; i < 5; i++) {
            estilos.add(workbook.createCellStyle());
            estilos.get(i).setAlignment(HorizontalAlignment.CENTER);
            estilos.get(i).setVerticalAlignment(VerticalAlignment.CENTER);
            estilos.get(i).setWrapText(true);
            setCellBorders(estilos.get(i));
        }

        //Fontes Bold
        Font bold = workbook.createFont();
        bold.setBold(true);
        Font bold_red = workbook.createFont();
        bold_red.setBold(true);
        bold_red.setColor(IndexedColors.RED.index);

        //Define formato R$ e Bold
        estilos.get(STYLE_r$).setDataFormat((short) 8);
        estilos.get(STYLE_bold_r$).setDataFormat((short) 8);
        estilos.get(STYLE_bold_r$_red).setDataFormat((short) 8);

        estilos.get(STYLE_bold_normal).setFont(bold);
        estilos.get(STYLE_bold_r$).setFont(bold);
        estilos.get(STYLE_bold_r$_red).setFont(bold_red);

        //Define linhas e totais
        int linhaTotais = listaComparacoes.size() + 1;
        int primeiraColunaLctos = 0;
        int ultimaColunaLctos = 3;
        int primeiraColunaValores = 4;
        int ultimaColunaValores = 7;

        //MENSALIDADES / PARCELAS
        definirEstiloEmUmIntervalo(sheet, estilos.get(STYLE_normal), 1, linhaTotais - 1, primeiraColunaLctos, ultimaColunaLctos);
        definirEstiloEmUmIntervalo(sheet, estilos.get(STYLE_r$), 1, linhaTotais - 1, primeiraColunaValores, ultimaColunaValores);

        //LINHA TOTAIS
        definirEstiloEmUmIntervalo(sheet, estilos.get(STYLE_bold_normal), linhaTotais, linhaTotais, primeiraColunaLctos, ultimaColunaLctos);
        definirEstiloEmUmIntervalo(sheet, estilos.get(STYLE_bold_r$), linhaTotais, linhaTotais, primeiraColunaValores, ultimaColunaValores);

        //Mescla celulas
        /* total ultima linha*/
        sheet.addMergedRegion(new CellRangeAddress(linhaTotais, linhaTotais, 0, 3));

        if (criarTabelaTotaisFinal) {

            //TABELA TOTAIS
            /* 1ª Parte totais titulos*/
            definirEstiloEmUmIntervalo(sheet, estilos.get(STYLE_bold_normal), linhaTotais + 2, linhaTotais + 4, 1, 3);
            /* 2ª Parte totais titulos*/
            definirEstiloEmUmIntervalo(sheet, estilos.get(STYLE_bold_normal), linhaTotais + 6, linhaTotais + 9, 1, 3);
            /* 3ª Parte totais titulos*/
            definirEstiloEmUmIntervalo(sheet, estilos.get(STYLE_bold_normal), linhaTotais + 11, linhaTotais + 12, 1, 3);


            /* totais R$ */
            definirEstiloEmUmIntervalo(sheet, estilos.get(STYLE_bold_r$), linhaTotais + 7, linhaTotais + 8, 1, 3);
            definirEstiloEmUmIntervalo(sheet, estilos.get(STYLE_bold_r$), linhaTotais + 6, linhaTotais + 9, 1, 3);
            definirEstiloEmUmIntervalo(sheet, estilos.get(STYLE_bold_r$), linhaTotais + 11, linhaTotais + 12, 1, 3);

            //Mescla celulas
            /* tabela totais*/
            for (int i = 2; i <= 4; i++) {
                sheet.addMergedRegion(new CellRangeAddress(linhaTotais + i, linhaTotais + i, 1, 2));
            }
            for (int i = 6; i <= 9; i++) {
                sheet.addMergedRegion(new CellRangeAddress(linhaTotais + i, linhaTotais + i, 1, 2));
            }
            for (int i = 11; i <= 12; i++) {
                sheet.addMergedRegion(new CellRangeAddress(linhaTotais + i, linhaTotais + i, 1, 2));
            }

        }
    }

    private void definirEstiloEmUmIntervalo(XSSFSheet sheet, CellStyle estilo,
            int primeira_linha, int ultima_linha,
            int primeira_coluna, int ultima_coluna) {
        for (int linha = primeira_linha; linha <= ultima_linha; linha++) {
            for (int coluna = primeira_coluna; coluna <= ultima_coluna; coluna++) {
                sheet.getRow(linha).getCell(coluna).setCellStyle(estilo);
            }
        }
    }

    private void adicionaTotalLinhas(Workbook workbook, Sheet sheet) {
        try {

            //Cria estilo
            Font font = workbook.createFont();
            font.setBold(true);

            CellStyle styleBoldCenter = workbook.createCellStyle();
            styleBoldCenter.setAlignment(HorizontalAlignment.CENTER);
            styleBoldCenter.setFont(font);

            //Cria Linha de totais
            criarLinhaDeTotais(sheet);

            //Cria tabelas de totais
            if (criarTabelaTotaisFinal) {
                criarTabelasDeTotais(sheet);
            }

            //Calcula formulas
            XSSFFormulaEvaluator.evaluateAllFormulaCells(workbook);
        } catch (Exception e) {
            System.out.println("Erro: " + e);
            e.printStackTrace();
        }
    }

    private boolean criarLinhaDeTotais(Sheet sheet) {
        try {
            int linha = listaComparacoes.size() + 1;

            Row row = sheet.createRow(linha);
            for (int i = 0; i < 8; i++) {
                row.createCell(i);
            }

            //Define valores
            row.getCell(0).setCellValue("TOTAL");
            row.getCell(4).setCellFormula("SUM(E2:E" + (linha) + ")");
            row.getCell(5).setCellFormula("SUM(F2:F" + (linha) + ")");
            row.getCell(6).setCellFormula("SUM(G2:G" + (linha) + ")");
            row.getCell(7).setCellFormula("SUM(H2:H" + (linha) + ")");
            //row.getCell(8).setCellFormula("SUM(I2:I" + (linha) + ")");
            //row.getCell(9).setCellFormula("SUM(J2:J" + (linha) + ")");

            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private boolean criarTabelasDeTotais(Sheet sheet) {
        try {
            int start = listaComparacoes.size() + 2;

            //Cria +12 linhas para popular mais tarde
            for (int i = start; i < (start + 12); i++) {
                Row row = sheet.createRow(i);
                //Cria 4 celulas para popular mais tarde
                for (int j = 0; j < 4; j++) {
                    row.createCell(j);
                }
            }

            //Define Titulos
            sheet.getRow(start + 1).getCell(1).setCellValue("VALOR TOTAL IPERGS(Total PMT):");
            sheet.getRow(start + 2).getCell(1).setCellValue("VALOR TOTAL IPERGS - 1% SEFAZ");
            sheet.getRow(start + 3).getCell(1).setCellValue("VALOR LÍQUIDO");

            sheet.getRow(start + 5).getCell(1).setCellValue("VALOR PMT SINAPERS 0,5%");
            sheet.getRow(start + 6).getCell(1).setCellValue("TOTAL VENDAS");
            sheet.getRow(start + 7).getCell(1).setCellValue("VALOR VENDA 1%");
            sheet.getRow(start + 8).getCell(1).setCellValue("ACERTO MENSALIDADES NÃO AVERBADAS");

            sheet.getRow(start + 10).getCell(1).setCellValue("TOTAL REPASSE SINAPERS");
            sheet.getRow(start + 11).getCell(1).setCellValue("TOTAL REPASSE FACTA");

            //Define valores
            sheet.getRow(start + 1).getCell(3).setCellFormula("F" + (start));
            sheet.getRow(start + 2).getCell(3).setCellFormula("ROUND(D" + (start + 2) + " * 0.01,2)");
            sheet.getRow(start + 3).getCell(3).setCellFormula("D" + (start + 2) + "-D" + (start + 3));

            sheet.getRow(start + 5).getCell(3).setCellFormula("ROUND(D" + (start + 4) + " * 0.005,2)");
            sheet.getRow(start + 6).getCell(3).setCellFormula("H" + (start));
            sheet.getRow(start + 7).getCell(3).setCellFormula("ROUND(D" + (start + 7) + " * 0.01,2)");
            sheet.getRow(start + 8).getCell(3).setCellValue(0);

            sheet.getRow(start + 10).getCell(3).setCellFormula("D" + (start + 6) + "+D" + (start + 8) + "+D" + (start + 9));
            sheet.getRow(start + 11).getCell(3).setCellFormula("D" + (start + 4) + "-D" + (start + 11));

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private void setCellBorders(CellStyle style) {
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setBorderBottom(BorderStyle.THIN);
    }

    private void removeCellBorders(CellStyle style) {
        style.setBorderTop(BorderStyle.NONE);
        style.setBorderLeft(BorderStyle.NONE);
        style.setBorderRight(BorderStyle.NONE);
        style.setBorderBottom(BorderStyle.NONE);
    }

    private boolean salvarWorkbookComo(FileInputStream file, Workbook workbook, String pathFileSalvar) {
        try {
            if (file != null) {
                file.close();
            }

            FileOutputStream outFile = new FileOutputStream(new File(pathFileSalvar));
            workbook.write(outFile);
            outFile.close();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private double getMoney(BigDecimal big) {
        //NumberFormat nf = NumberFormat.getCurrencyInstance();
        //return nf.format(big);
        BigDecimal newBig = big.setScale(2, BigDecimal.ROUND_HALF_EVEN);
        return newBig.doubleValue();
    }
}
