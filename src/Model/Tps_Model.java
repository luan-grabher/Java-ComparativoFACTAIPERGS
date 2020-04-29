package Model;

import Model.Entities.Associado;
import SimpleDotEnv.Env;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import nl.cad.tpsparse.tps.TpsFile;
import nl.cad.tpsparse.tps.TpsHeader;
import nl.cad.tpsparse.tps.record.FieldDefinitionRecord;
import nl.cad.tpsparse.tps.record.TableDefinitionRecord;

public class Tps_Model {

    private static final String tpsFolder = Env.get("tpsFolder");
    private static final List<Associado> associados = new ArrayList<>();

    private static List<List<Object>> getTableData(String tableName) {
        List<List<Object>> returned = new ArrayList<>();

        try {
            File file = new File(tpsFolder + "\\" + tableName + ".tps");
            TpsFile tpsFile = new TpsFile(file);

            Map<Integer, TableDefinitionRecord> tables = tpsFile.getTableDefinitions(false);
            tables.entrySet().forEach((entry) -> {
                //COLUNAS
                returned.add(getTableFields(entry));

                //ENTRADAS
                tpsFile.getDataRecords(entry.getKey(), entry.getValue(), false).forEach((rec) -> {
                    returned.add(rec.getValues());
                });
            });
        } catch (Exception e) {
        }

        return returned;
    }

    private static List<Object> getTableFields(Map.Entry<Integer, TableDefinitionRecord> entry) {
        List<Object> fieldList = new ArrayList<>();
        
        List<FieldDefinitionRecord> fields = entry.getValue().getFields();
        fields.forEach((field) -> {
            fieldList.add(field.getFieldName());
        });
        
        return fieldList;
    }

    public static void setContratos() {
        try {
            String tableName = "ASSEMPRE";

            List<List<Object>> rows = getTableData(tableName);
            for (List<Object> row : rows) {
                associados.add(new Associado(
                        row.get(132).toString(), //MAT ESTADO
                        row.get(3).toString(), //FUNCIONARIO ESTADO
                        row.get(2).toString(), //MAT IPE
                        row.get(83).toString(), //MAT ESTADO ANTIGA
                        row.get(91).toString(), //MAT IPE 2
                        row.get(84).toString(), //VINCULO
                        row.get(85).toString(), //PENSIONISTA
                        row.get(5).toString(), //situacao
                        row.get(7).toString(), //NOME
                        row.get(20).toString(), //CPF
                        row.get(14).toString(), //DATA CADASTRO
                        row.get(15).toString(), //DATA EXCLUSAO
                        row.get(16).toString() //DATA OBITO
                ));
            }
        } catch (Exception e) {
        }
    }

    public static void setAssociados() {
        try {
            String tableName = "Associad";

            List<List<Object>> rows = getTableData(tableName);
            for (List<Object> row : rows) {
                associados.add(new Associado(
                        row.get(132).toString(), //MAT ESTADO
                        row.get(3).toString(), //FUNCIONARIO ESTADO
                        row.get(2).toString(), //MAT IPE
                        row.get(83).toString(), //MAT ESTADO ANTIGA
                        row.get(91).toString(), //MAT IPE 2
                        row.get(84).toString(), //VINCULO
                        row.get(85).toString(), //PENSIONISTA
                        row.get(5).toString(), //situacao
                        row.get(7).toString(), //NOME
                        row.get(20).toString(), //CPF
                        row.get(14).toString(), //DATA CADASTRO
                        row.get(15).toString(), //DATA EXCLUSAO
                        row.get(16).toString() //DATA OBITO
                ));
            }
        } catch (Exception e) {
        }
    }

    public static Associado getAssociado(Long matricula, Long vinculo, Long pensionista) {
        try {
            Long m = matricula;
            Long v = vinculo;
            Long p = pensionista;

            //try{ m = matricula != 0? matricula.toString():"";}catch(Exception e){}
            //try{ v = vinculo != 0? vinculo.toString():"";}catch(Exception e){}
            //try{ p = pensionista != 0? pensionista.toString():"";}catch(Exception e){}
            for (int i = 0; i < associados.size(); i++) {

                Associado a = associados.get(i);
                if ((a.getMatriculaEstado().equals(m) | a.getNroFuncionarioEstado().equals(m)
                        | a.getMatriculaIpe().equals(m) | a.getMatriculaIpe2().equals(m)
                        | a.getMatriculaEstadoAntiga().equals(m))
                        & a.getNroVinculo().equals(v)
                        & a.getNroPensionista().equals(p)) {
                    return a;
                }
            }
            return new Associado(matricula.toString(), "", "", "", "", "", "", "", "", "", "", "", "");
        } catch (Exception e) {
            return new Associado(matricula.toString(), "", "", "", "", "", "", "", "", "", "", "", "");
        }

    }
}
