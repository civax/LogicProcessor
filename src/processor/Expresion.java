/*
 *  @version 1.0
 *  @author Carlos Iván Castillo Sepúlveda
 */
package processor;

/**
 * Esta clase sirve como interfaz para manipular la expresion dada
 * @author Carlos Iván Castillo Sepúlveda
 */
public class Expresion{  
    /**
     * Normaliza la cadena de entrada y sustituye caracteres de entrada por operadores validos
     * @param input cadena a normalizar
     * @return regresa la cadena de entrada normalizada
     */
    public static String normalizarTexto(String input){
        input=input.replace(" ", "");
        input=input.toLowerCase();
        input=input.replace("<->", Operador.$BICONDITIONAL);
        input=input.replace("->", Operador.$CONDITIONAL);
        input=input.replace("=", "≡");
        return input;
    }    
    /**
     * Remueve los parentesis exteriores de una cadena dada
     * @param expresion cadena a simplificar
     * @return regresa la cadena dada, sin parentesis externos
     */
    public static String  removerParentesisExterno(String expresion){
        if((expresion.charAt(0)=='(')&&(expresion.charAt(expresion.length()-1)==')')){
            expresion=expresion.substring(1, expresion.length()-1);
        }
        return expresion;
    }
    private String expresion;
    private String equivalencia;
    private Simplificador simplificador;
    public String convertir(String equivalencia,String expresion){
        this.equivalencia=simplificador.aplicar(equivalencia, expresion);
        return this.equivalencia;
    }

    private Expresion() {
    }

    public Expresion(String expresion) {
        this.expresion = expresion;
        simplificador=Simplificador.getInstance();
    }
}
