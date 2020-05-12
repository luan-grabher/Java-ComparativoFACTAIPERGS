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
    private static Calendar filesMonth;

    public static void main(String[] args) {
        //Controle Aplicação
        if (setFilesWithUserInputs()) {
            //createFinalFiles();
        }

        System.exit(0);
    }

    public static void setFiles(File arquivoFACTA, File arquivoIPERGS, File localSalvar) {
        arquivoFacta = arquivoFACTA;
        arquivoIpergrs = arquivoIPERGS;
        ComparativoFACTAIPERGS.localSalvar = localSalvar;
    }
    
    public static boolean setFilesWithUserInputs(){
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
        filesMonth = model.getFilesMonth();
        
        System.out.println(filesMonth.toString());
        
        return exec.hasErrorBreak();
    }

    public static boolean createFinalFiles() {
        
        Controller controller = new Controller();
        List<Executavel> execs = new ArrayList<>();
        
        execs.add(controller.new setFactaLctos(arquivoFacta));
        execs.add(controller.new setAssociados());
        execs.add(controller.new setContratos());
        execs.add(controller.new setIpergsLctos(arquivoIpergrs));
        
        Execution exec = new Execution("Comparativo FACTA x IPERGS");
        exec.setExecutaveis(execs);
        exec.rodarExecutaveis();
        exec.finalizar();
        
        return exec.hasErrorBreak();
    }

}
