package Model;

import Model.Entities.Associado;
import SimpleDotEnv.Env;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import nl.cad.tpsparse.tps.TpsFile;
import nl.cad.tpsparse.tps.record.TableDefinitionRecord;

public class Associados_Model {

    private String tpsFolder = Env.get("tpsFolder");
    private List<Associado> associados;

    public Associados_Model() {
        try {
            associados = new ArrayList<>();

            TpsFile tpsFile = new TpsFile(new File(tpsFolder + "\\Associad.tps"));

            Map<Integer, TableDefinitionRecord> tables = tpsFile.getTableDefinitions(false);
            tables.entrySet().forEach((entry) -> {
                tpsFile.getDataRecords(entry.getKey(), entry.getValue(), false).forEach((rec) -> {
                    List<Object> values = rec.getValues();
                    //if(/*"A".equals(values.get(5).toString()) & */ "78".equals(values.get(84).toString())){
                    associados.add(new Associado(
                            values.get(132).toString(), //MAT ESTADO
                            values.get(3).toString(), //FUNCIONARIO ESTADO
                            values.get(2).toString(), //MAT IPE
                            values.get(83).toString(), //MAT ESTADO ANTIGA
                            values.get(91).toString(), //MAT IPE 2
                            values.get(84).toString(), //VINCULO
                            values.get(85).toString(), //PENSIONISTA
                            values.get(5).toString(), //situacao
                            values.get(7).toString(), //NOME
                            values.get(20).toString(), //CPF
                            values.get(14).toString(), //DATA CADASTRO
                            values.get(15).toString(), //DATA EXCLUSAO
                            values.get(16).toString() //DATA OBITO
                    ));
                    //}
                });
            });
        } catch (Exception e) {
            associados = null;
        }

    }

    public Associado getAssociado(Long matricula, Long vinculo, Long pensionista) {
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
            return new Associado(matricula.toString(), "", "", "", "", "", "", "", "MATRICULA INEXISTENTE", "", "", "", "");
        } catch (Exception e) {
            return new Associado(matricula.toString(), "", "", "", "", "", "", "", "MATRICULA INEXISTENTE", "", "", "", "");
        }

    }
}
