package main;

import Control.Controller;
import Entity.Executavel;
import Model.Comparacoes_Model;
import Model.Facta_Model;
import Model.Ipergs_Model;
import View.Comparacoes_View;
import java.io.File;
import Executor.Execution;
import Executor.View.View;
import java.util.ArrayList;
import java.util.List;

public class ComparativoFACTAIPERGS {

    private static File arquivoFacta;
    private static File arquivoIpergrs;
    private static File localSalvar;

    public static void main(String[] args) {
        //Controle Aplicação
        if (pegarArquivos()) {
            executar();
        }

        System.exit(0);
    }

    public static void definirArquivos(File arquivoFACTA, File arquivoIPERGS, File localSalvar) {
        arquivoFacta = arquivoFACTA;
        arquivoIpergrs = arquivoIPERGS;
        ComparativoFACTAIPERGS.localSalvar = localSalvar;
    }

    public static void executar() {
        
        String nome = "Comparativo FACTA x IPERGS";
        
        Controller controller = new Controller();
        List<Executavel> execs = new ArrayList<>();
        
        execs.add(controller.new setFactaLctos(arquivoFacta));
        execs.add(controller.new setIpergsLctos(arquivoIpergrs));
        
        Execution exec = new Execution(nome);
        exec.setExecutaveis(execs);
        exec.rodarExecutaveis();
        exec.finalizar();
    }

    private static boolean pegarArquivos() {
        //Escolhe facta
        View.render("Por favor, escolha a seguir o arquivo .TXT (Texto) das mensalidades FACTA obtido no sistema interno!", "question");
        arquivoFacta = Selector.Arquivo.selecionar("C:\\Users", "Arquivo de Texto FACTA", "txt");
        if (Selector.Arquivo.verifica(arquivoFacta.getAbsolutePath(), ".txt")) {
            //Escolhe IPERGS
            View.render("Por favor, escolha a seguir o arquivo .TXT (Texto) de empréstimos do IPERGS!", "question");
            arquivoIpergrs = Selector.Arquivo.selecionar("C:\\Users", "Arquivo de Texto IPERGS", "txt");
            if (Selector.Arquivo.verifica(arquivoIpergrs.getAbsolutePath(), ".txt")) {   
                View.render("Por favor, escolha a pasta onde o programa irá salvar o comparativo!", "question");
                localSalvar = Selector.Pasta.selecionar();
                return Selector.Pasta.verifica(localSalvar.getAbsolutePath());
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

}
