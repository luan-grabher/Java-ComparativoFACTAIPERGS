
import java.io.File;
import Executor.View.View;
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

        if(localSalvar.exists() & arquivoFacta.exists() & arquivoIpergrs.exists() ){
            ComparativoFACTAIPERGS.definirArquivos(arquivoFacta, arquivoIpergrs, localSalvar);
            ComparativoFACTAIPERGS.executar();
        }else{
            View.render("Algum dos arquivos não existe filha da puta!", "error");
        }
    }
}
