
import java.io.File;
import Auxiliar.Valor;
import main.ComparativoFACTAIPERGS;

public class Testes {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        testarAplicacao();
    }

    public static void testarAplicacao() {
        //Define arquivos
        String path = "C:\\Dropbox\\Pessoal\\Projetos\\Java\\Arquivos de Teste\\FACTA e Emprestimos";
        File arquivoFacta = new File(path + "\\FACTA.txt");
        File arquivoIpergrs = new File(path + "\\IPERGS.txt");
        File arquivoEsquecidosFACTA = new File(path + "\\Esquecidos FACTA.csv");
        File localSalvar = new File(path);

        ComparativoFACTAIPERGS.definirArquivos(arquivoFacta, arquivoIpergrs, arquivoEsquecidosFACTA, localSalvar);
        ComparativoFACTAIPERGS.executar();
    }
}
