package main;

import Model.Comparacoes_Model;
import Model.Facta_Model;
import Model.Ipergs_Model;
import View.Comparacoes_View;
import View.View;
import java.io.File;
import Executor.Execution;

public class ComparativoFACTAIPERGS {

    private static File arquivoFacta;
    private static File arquivoEsquecidosFACTA;
    private static File arquivoIpergrs;
    private static File localSalvar;

    public static void main(String[] args) {
        //Controle Aplicação
        if (pegarArquivos()) {
            executar();
        }

        System.exit(0);
    }

    public static void definirArquivos(File arquivoFACTA, File arquivoIPERGS, File arquivoEsquecidosFACTA, File localSalvar) {
        arquivoFacta = arquivoFACTA;
        arquivoIpergrs = arquivoIPERGS;
        ComparativoFACTAIPERGS.arquivoEsquecidosFACTA = arquivoEsquecidosFACTA;
        ComparativoFACTAIPERGS.localSalvar = localSalvar;
    }

    public static void executar() {
        Execution execução = new Execution("Comparar empréstimos FACTA e IPERGS", 6);
        try {
            execução.atualizarVisão("Buscando informações FACTA...");
            Facta_Model facta = new Facta_Model(arquivoFacta, arquivoEsquecidosFACTA);
            execução.executar(facta.status);

            execução.atualizarVisão("Buscando informações IPERGS...");
            Ipergs_Model ipergs = new Ipergs_Model(arquivoIpergrs);
            execução.executar(ipergs.status);

            execução.atualizarVisão("Fazendo cruzamento dos arquivos...");
            Comparacoes_Model comparacoes = new Comparacoes_Model(facta.getLctos(), ipergs.getLctos());
            execução.executar(comparacoes.gerarComparacoes());

            execução.atualizarVisão("Gerando arquivo esquecidos FACTA");
            execução.executar(comparacoes.salvarEsquecidosFacta(localSalvar));

            execução.atualizarVisão("Criando visualização da comparação geral...");
            Comparacoes_View comparacoesView = new Comparacoes_View(comparacoes.getComparacoes(), localSalvar, "");
            execução.executar(comparacoesView.status);

            execução.atualizarVisão("Criando visualização da comparação dos ativos...");
            Comparacoes_View comparacoesViewAtivos = new Comparacoes_View(comparacoes.getComparacoes("Ativos"), localSalvar, "Ativos",false);
            execução.executar(comparacoesViewAtivos.status);

        } catch (Error e) {
        } finally {
            execução.finalizar();
        }
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

                View.render("Por favor, escolha o arquivo de Esquecidos FACTA gerado pelo programa no mês anterior...", "question");
                arquivoEsquecidosFACTA = Selector.Arquivo.selecionar("C:\\Users", "Arquivo CSV Esquecidos FACTA", "csv");
                if (Selector.Arquivo.verifica(arquivoEsquecidosFACTA.getAbsolutePath(), ".csv")) {
                    
                    View.render("Por favor, escolha a pasta onde o programa irá salvar o comparativo!", "question");
                    localSalvar = Selector.Pasta.selecionar();
                    return Selector.Pasta.verifica(localSalvar.getAbsolutePath());
                }else{
                    return false;
                }
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

}
