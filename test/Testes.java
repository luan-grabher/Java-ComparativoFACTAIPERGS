
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
    
    public static void testeCorrigeNumeroMatricula(){
        Long matricula = new Long("12783478050");
    
        // Pegar string
        String matriculaString = matricula.toString();
        
        // 1 - Inverter String
        String matriculaInvertida = new StringBuilder(matriculaString).reverse().toString();
        
        //Separa por 87 e fica no maximo com 2 separações
        String[] matriculaSeparada = matriculaInvertida.split("87", 2);
        
        //inverte segundo corte
        String segundoCorte = new StringBuilder(matriculaSeparada[1]).reverse().toString();
        
        //Para o primeiro corte (Ultimos números antes de inverter), coloque seus devidos zeros
        Valor primeiroCorte = new Valor(new StringBuilder(matriculaSeparada[0]).reverse().toString());
        if(primeiroCorte.getInteger() < 10){
            primeiroCorte.setString("0" + primeiroCorte.getInteger());
        }else{
            primeiroCorte.setString("0" + primeiroCorte.getInteger().toString().charAt(0));
        }
        
        //Rertorna segundo corte invertido + "78" + primeiro corte; 
        Valor retorno = new Valor(segundoCorte  + "78" + primeiroCorte.getString());
        
        matricula = retorno.getLong();
    }
}
