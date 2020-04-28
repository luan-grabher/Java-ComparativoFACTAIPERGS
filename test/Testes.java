
import java.io.File;
import Auxiliar.Valor;
import java.util.ArrayList;
import java.util.Optional;
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
        String path = "test\\";
        File arquivoFacta = new File(path + "FACTA.txt");
        File arquivoIpergrs = new File(path + "IPERGS.txt");
        File localSalvar = new File(path);

        ComparativoFACTAIPERGS.definirArquivos(arquivoFacta, arquivoIpergrs, localSalvar);
        ComparativoFACTAIPERGS.executar();
    }
}
