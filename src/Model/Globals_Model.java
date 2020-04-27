package Model;

import main.Arquivo;

public class Globals_Model {
    public static boolean set(String nome,String valor){
        return Arquivo.salvar("./Files/" + nome + ".global",valor);
    }
    public static String get(String nome){
        return Arquivo.ler("./Files/" + nome + ".global");
    }
}
