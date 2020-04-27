package Model;

import Model.Entities.FactaLctos;
import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import main.Arquivo;

public class Facta_Model {

    public String status = "";

    private File arquivo;
    private File arquivoEsquecidos;
    private List<FactaLctos> lctos = new ArrayList<>();

    public Facta_Model(File arquivo, File esquecidos) {
        this.arquivo = arquivo;
        arquivoEsquecidos = esquecidos;
        //montar a lista de lctos
        status = adicionarEsquecidos();
        if (status.equals("")) {
            status = montarListaLctosTXT();
        }
    }

    private String adicionarEsquecidos() {
        try {
            String textoArquivo = Arquivo.ler(arquivoEsquecidos.getAbsolutePath());
            String[] linhas = textoArquivo.split("\n");
            for (String linha : linhas) {
                String[] colunas = linha.split(";");
                if (colunas.length == 5) {
                    lctos.add(
                            new FactaLctos(
                                    Long.valueOf(colunas[0]),
                                    colunas[1],
                                    colunas[2],
                                    new BigDecimal(colunas[3]),
                                    new BigDecimal(colunas[4])
                            )
                    );
                }

            }
            
            return "";
        } catch (Exception e) {
            return "Erro ao adicionar esquecidos FACTA: " + e;
        }
    }

    private String montarListaLctosTXT() {
        String r = "";
        try {
            //Abre arquivo
            String arquivoTexto = Arquivo.ler(arquivo.getAbsolutePath());
            String[] arquivoLinhas = arquivoTexto.split("\n");

            //Percorre todas as linhas
            for (int i = 0; i < arquivoLinhas.length; i++) {
                try {
                    String arquivoLinha = arquivoLinhas[i];

                    //Se tiver no minimo 160 posicoes
                    if (arquivoLinha.length() >= 160) {
                        String nome = arquivoLinha.substring(6, 88).replaceAll("[^a-zA-Z ]+", "").trim();

                        //Verifica se nome existe
                        if (!nome.equals("")) {
                            Long matricula = Long.valueOf(arquivoLinha.substring(88, 111).replaceAll("[^0-9]", "").trim());

                            //Verifica matricula
                            if (matricula > 1000000) {
                                String valorFinanciadoString = arquivoLinha.substring(111, 142).replaceAll("[^0-9.]", "").trim();
                                BigDecimal valorFinanciado = new BigDecimal(valorFinanciadoString);

                                //Verifica valor financiado
                                //if (valorFinanciado.compareTo(BigDecimal.ZERO) == 1) {
                                if (valorFinanciado.compareTo(BigDecimal.ZERO) != 2) { //Desativado verificação por zero

                                    //Pega valor parcela e cpf
                                    String valorParcela_cpf_String = arquivoLinha.substring(142).replaceAll("[^0-9.]", "").trim();
                                    String[] splitParcela_Cpf = valorParcela_cpf_String.split("\\.");

                                    BigDecimal valorParcela = new BigDecimal(
                                            splitParcela_Cpf[0]
                                            + "."
                                            + splitParcela_Cpf[1].substring(0, 2)
                                    );

                                    String cpf = "";
                                    try {
                                        cpf = splitParcela_Cpf[1].substring(2);
                                    } catch (Exception e) {
                                    }

                                    //System.out.println(matricula + " - " + nome);
                                    lctos.add(new FactaLctos(matricula, nome, cpf, valorFinanciado, valorParcela));
                                }
                            }
                        }
                    }
                } catch (Exception e) {
                    //Se der algum erro não vai adicionar
                    //System.out.println("Erro: " + e);
                }
            }

        } catch (Exception e) {
        }

        return r;
    }

    /**
     * Usando o arquivo Excel da FACTA adiciona linhas na lista de lançamentos
     * facta
     */
//    private String montarListaLctosXLSX() {
//        try {
//            //abrir arquivo
//            FileInputStream arquivo = new FileInputStream(this.arquivo);
//
//            XSSFWorkbook workbook = new XSSFWorkbook(arquivo);
//            XSSFSheet sheet = workbook.getSheetAt(0);
//
//            for (int i = 0; i < sheet.getLastRowNum(); i++) {
//                try {
//                    Row row = sheet.getRow(i);
//
//                    long matricula;
//                    String nome;
//                    int prazo;
//                    double pmt;
//
//                    matricula = (long) row.getCell(3).getNumericCellValue();
//                    nome = row.getCell(5).getStringCellValue();
//                    prazo = (int) row.getCell(10).getNumericCellValue();
//                    pmt = row.getCell(11).getNumericCellValue();
//
//                    lctos.add(new FactaLctos(matricula, nome, prazo, pmt));
//                } catch (Exception e) {
//                }
//            }
//            workbook.close();
//
//            return "";
//        } catch (Exception e) {
//            return "Erro ao montar lista de lançamentos Facta! Erro: " + e;
//        }
//    }
    public List<FactaLctos> getLctos() {
        return lctos;
    }

}
