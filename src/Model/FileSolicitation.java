package Model;

import SimpleView.View;
import java.io.File;


public class FileSolicitation {
    private String message;
    String fileName;
    String fileTypeWithoutDot;

    public void setMessage(String message) {
        this.message = message;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void setFileTipeWithoutDot(String fileTipeWithoutDot) {
        this.fileTypeWithoutDot = fileTipeWithoutDot;
    }
    
    public File get(){
        File file = new File("C:/inexistentLocal");
        try {
            View.render(message, "question");
            file  = Selector.Arquivo.selecionar("C:\\Users", fileName, fileTypeWithoutDot);
            if(!Selector.Arquivo.verifica(file.getAbsolutePath(), "."  + fileTypeWithoutDot)){
                throw new Error("Arquivo " + fileName + " selecionado inválido!" );
            }
        } catch (Exception e) {
            throw new Error("Arquivo " + fileName + " selecionado inválido!" );
        }
        
        return file;
    }
}
