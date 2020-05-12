
import java.io.File;
import Executor.View.View;
import main.ComparativoFACTAIPERGS;

public class Testes {

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
            ComparativoFACTAIPERGS.setFiles(arquivoFacta, arquivoIpergrs, localSalvar);
            ComparativoFACTAIPERGS.createFinalFiles();
        }else{
            View.render("Algum dos arquivos não existe filha da puta!", "error");
        }
    }
}
