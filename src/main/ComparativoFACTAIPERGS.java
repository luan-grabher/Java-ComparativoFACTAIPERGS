package main;

import Control.Controller;
import Entity.Executavel;
import java.io.File;
import Executor.Execution;
import Model.UserInputs_Model;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ComparativoFACTAIPERGS {

    private static File arquivoFacta;
    private static File arquivoIpergrs;
    private static File localSalvar;
    private static Calendar monthWork;

    public static void main(String[] args) {
        //Controle Aplicação
        if (!setFilesWithUserInputs().hasErrorBreak()) {
            createFinalFiles();
        }

        System.exit(0);
    }

    public static void setArquivoFacta(File arquivoFacta) {
        ComparativoFACTAIPERGS.arquivoFacta = arquivoFacta;
    }

    public static void setArquivoIpergrs(File arquivoIpergrs) {
        ComparativoFACTAIPERGS.arquivoIpergrs = arquivoIpergrs;
    }

    public static void setLocalSalvar(File localSalvar) {
        ComparativoFACTAIPERGS.localSalvar = localSalvar;
    }

    public static void setFilesMonth(Calendar filesMonth) {
        ComparativoFACTAIPERGS.monthWork = filesMonth;
    }
    
    public static Execution setFilesWithUserInputs(){
        UserInputs_Model model = new UserInputs_Model();
        List<Executavel> execs = new ArrayList<>();
        
        execs.add(model.new setFactaFile());
        execs.add(model.new setIpergsFile());
        execs.add(model.new setMonthWorked());
        
        Execution exec = new Execution("Pegando informações com o usuário");
        exec.setExecutaveis(execs);
        exec.rodarExecutaveis();
        exec.finalizar();
        
        //Define arquivos
        arquivoFacta = model.getFactaFile();
        arquivoIpergrs = model.getIpergsFile();
        localSalvar = model.getSaveFolderFile();
        monthWork = model.getMonthWork();
        
        System.out.println(monthWork.toString());
        
        return exec;
    }

    public static Execution createFinalFiles() {
        
        Controller controller = new Controller();
        List<Executavel> execs = new ArrayList<>();
        
        execs.add(controller.new setFactaLctos(arquivoFacta));
        execs.add(controller.new setAssociados());
        execs.add(controller.new setContratos());
        execs.add(controller.new setIpergsLctos(arquivoIpergrs));
        execs.add(controller.new setTPSDatabase());
        execs.add(controller.new setListMonthPersons(monthWork));
        execs.add(controller.new setListTotals());
        execs.add(controller.new createFactaFinalView(localSalvar, monthWork));
        
        Execution exec = new Execution("Comparativo FACTA x IPERGS");
        exec.setExecutaveis(execs);
        exec.rodarExecutaveis();
        exec.finalizar();
        
        return exec;
    }

}
