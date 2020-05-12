package Model;

import Entity.Executavel;
import Executor.View.View;
import java.io.File;
import java.util.Calendar;

public class UserInputs_Model {

    private File factaFile;
    private File ipergsFile;
    private File saveFolderFile;
    
    private Calendar filesMonth;

    public class setFactaFile extends Executavel {

        public setFactaFile() {
            nome = "Pegando arquivo da FACTA";
        }

        @Override
        public void run() {
            FileSolicitation solicitation = new FileSolicitation();
            solicitation.setFileName("Arquivo de texto FACTA");
            solicitation.setFileTipeWithoutDot("txt");
            solicitation.setMessage("Por favor, escolha a seguir o arquivo .TXT (Texto) das mensalidades FACTA obtido no sistema interno!\n"
                    + "OS ARQUIVOS GERADOS PELO PROGRAMA ESTARÃO NA MESMA PASTA DESTE AQRQUIVO!");

            factaFile = solicitation.get();

            saveFolderFile = factaFile.getParentFile();
        }

    }

    public class setIpergsFile extends Executavel {

        public setIpergsFile() {
            nome = "Pegando arquivo da IPERGS";
        }

        @Override
        public void run() {
            FileSolicitation solicitation = new FileSolicitation();
            solicitation.setFileName("Arquivo de texto IPERGS");
            solicitation.setFileTipeWithoutDot("txt");
            solicitation.setMessage("Por favor, escolha a seguir o arquivo .TXT (Texto) de empréstimos do IPERGS!");

            ipergsFile = solicitation.get();
        }

    }

    public class setMonthWorked extends Executavel {

        public setMonthWorked() {
            nome = "Definindo mês dos arquivos";
        }

        @Override
        public void run() {
            Integer thisYear = Calendar.getInstance().get(Calendar.YEAR);
            
            String[] monthList = new String[]{"Janeiro","Fevereiro","Março","Abril","Maio","Junho","Julho","Agosto","Setembro","Outubro","Novembro","Dezembro"};
            
            String[] yearsList = new String[]{String.valueOf(thisYear), String.valueOf(thisYear-1), String.valueOf(thisYear-2),String.valueOf(thisYear-3)};

            Integer yearSelected = View.chooseOption("Escolha o ano", "Escolha o ano em que os valores pertencem", yearsList);
            Integer monthSelected = View.chooseOption("Escolha o mês", "Escolha o mês em que os valores pertencem", monthList);
        }

    }

    public File getFactaFile() {
        return factaFile;
    }

    public File getIpergsFile() {
        return ipergsFile;
    }

    public File getSaveFolderFile() {
        return saveFolderFile;
    }

    public Calendar getFilesMonth() {
        return filesMonth;
    }
    
}
