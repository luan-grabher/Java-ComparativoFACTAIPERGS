package Model;

import Model.Entities.FactaLcto;
import Model.Entities.IpergsLcto;
import Model.Entities.MonthContract;
import fileManager.FileManager;
import java.io.File;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;
import tpsdb.Model.Entities.Associate;
import tpsdb.Model.Entities.Contract;
import tpsdb.Model.Tps_Model;

public class FactaModel {

    public static List<MonthContract> getMonthIPERGSContracts(List<IpergsLcto> ipergsLctos, Calendar monthWork) {
        List<MonthContract> monthContracts = new ArrayList<>();

        //Percorre todos lançamentos IPERGS
        for (IpergsLcto ipergsLcto : ipergsLctos) {
            //Valor maior que 0
            if (ipergsLcto.getValor().compareTo(BigDecimal.ZERO) == 1) {
                if (ipergsLcto.getMatricula() == Long.valueOf("39815927802")) {
                    System.out.println("Aqui");
                }

                //Procura contratos no mês para aquele codigo de associado
                Long associateCode = ipergsLcto.getAssociadoCodigo();
                Optional<Associate> associateOptional = Tps_Model.getAssociates().stream().filter(a -> Objects.equals(a.getCodigoAssociado(), associateCode)).findFirst();

                if (associateOptional.isPresent()) {
                    Associate associate = associateOptional.get();

                    getAssociateContractsStream(associateCode, monthWork).forEach(associadoContract -> {
                        MonthContract monthContract = new MonthContract();
                        monthContract.setAssociate(associate);
                        monthContract.setContract(associadoContract);
                        monthContract.setIpergs(ipergsLcto);
                        monthContract.setName(ipergsLcto.getNome());

                        monthContracts.add(monthContract);

                    });
                }
            }
        }

        //Ordem Alfabética
        monthContracts.sort(Comparator.comparing(MonthContract::getName));

        return monthContracts;
    }

    private static Stream<Contract> getAssociateContractsStream(Long associateCode, Calendar calendar) {
        return Tps_Model.getContracts().stream().filter(
                c -> c.getAssociadoCodigo() == associateCode
                && isCalendarsInTheSameMonth(c.getDataProposta(), calendar)
        );
    }

    private static boolean isCalendarsInTheSameMonth(Calendar calOne, Calendar calTwo) {
        return calOne.get(Calendar.YEAR) == calTwo.get(Calendar.YEAR) && calOne.get(Calendar.MONTH) == calTwo.get(Calendar.MONTH);
    }

    public static List<FactaLcto> getFileList(File file) {
        List<FactaLcto> lctos = new ArrayList<>();

        try {
            Integer minCol = 137;

            Integer[] mapNome = {0, 39};
            Integer[] mapMatricula = {39, 60};
            Integer[] mapValorFinanciado = {61, 76};
            Integer[] mapValorParcela = {77, 90};
            Integer[] mapNroParcelas = {91, 99};
            Integer[] mapCpf = {110, 130};
            Integer[] mapProposta = {130};

            //Abre arquivo
            String texto = FileManager.getText(file.getAbsolutePath());
            String[] linhas = texto.split("\n");

            //Percorre todas as linhas
            for (String linha : linhas) {
                try {
                    String[] baseLinha = linha.split("");

                    //Se tiver no minimo 160 posicoes
                    if (linha.length() >= minCol) {
                        String nome = linha.substring(mapNome[0], mapNome[1]).replaceAll("[^a-zA-Z ]+", "").trim();
                        Long matricula = Long.valueOf(linha.substring(mapMatricula[0], mapMatricula[1]).replaceAll("[^0-9]", "").trim());
                        BigDecimal valorFinanciado = new BigDecimal(linha.substring(mapValorFinanciado[0], mapValorFinanciado[1]).replaceAll("[^0-9.]", "").trim());
                        BigDecimal valorParcela = new BigDecimal(linha.substring(mapValorParcela[0], mapValorParcela[1]).replaceAll("[^0-9.]", "").trim());
                        Integer nroParcelas = Integer.valueOf(linha.substring(mapNroParcelas[0], mapNroParcelas[1]).replaceAll("[^0-9]", "").trim());
                        String cpf = linha.substring(mapCpf[0], mapCpf[1]).replaceAll("[^0-9]", "").trim();
                        Long proposta = Long.valueOf(linha.substring(mapProposta[0]).replaceAll("[^0-9]", "").trim());

                        if (!nome.equals("") & matricula > 1000000 & nroParcelas > 0 & !cpf.equals("") & proposta > 0) {
                            lctos.add(new FactaLcto(matricula, nome, cpf, valorFinanciado, valorParcela, nroParcelas, proposta));
                        }
                    }
                } catch (Exception e) {
                }
            }

        } catch (Exception e) {
        }

        return lctos;
    }

    public static Map<String, BigDecimal> getTotals(List<IpergsLcto> ipergsLctos, List<MonthContract> monthContracts) {
        Map<String, BigDecimal> totals = new HashMap<>();

        BigDecimal totalIpergs = new BigDecimal(IpergsModel.getTotal(ipergsLctos).toString());

        //Auxiliares
        //BigDecimal onePercent = new BigDecimal("0.01");
        //BigDecimal halfPercent = new BigDecimal("0.005");
        //Totals
        //BigDecimal sefaz = totalIpergs.multiply(onePercent);
        //BigDecimal ipergsLiquid = totalIpergs.add(sefaz.negate());
        //BigDecimal sinapersHalfPercent = ipergsLiquid.multiply(halfPercent);
        BigDecimal totalFinanced = getTotalFinanced(monthContracts);
        //BigDecimal salesSinapersPercent = totalFinanced.multiply(onePercent);

        //Add to map
        totals.put("ipergs", totalIpergs);
        //totals.add(new Valor(totalIpergs,"ipergs"));
        //totals.add(new Valor(sefaz,"Total IPERGS SEFAZ 1%"));
        //totals.add(new Valor(ipergsLiquid,"Total IPERGS Liquido"));

        //totals.add(new Valor(sinapersHalfPercent,"PMT SINAPERS 0,5%"));
        //totals.add(new Valor(totalFinanced,"financed")); //Valor total financiado no mÊs
        totals.put("financed", totalFinanced);
        //totals.add(new Valor(salesSinapersPercent,"Valor Venda 1%")); //repasse sinapers

        return totals;
    }

    private static BigDecimal getTotalFinanced(List<MonthContract> monthContracts) {
        //Usando lista de contratos do mês pega totalfinanciado
        BigDecimal total = new BigDecimal(BigInteger.ZERO);

        for (MonthContract monthContract : monthContracts) {
            Contract contract = monthContract.getContract();
            total = total.add(contract.getValorFinanciado());
        }

        return total;
    }
}
