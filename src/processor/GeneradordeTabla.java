/**
 *  @version 1.0
 *  @author Carlos Iván Castillo Sepúlveda
 */

package processor;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.StringTokenizer;
/**
 *
 * Esta clase contiene los métodos de parsing y evaluación de la expresión dada
 * @author Carlos Iván Castillo Sepúlveda
 * 
 */
public class GeneradordeTabla {
    
    private List<Map<String,Boolean>> inputList;
    private String inputString;
    private Operacion operation;
    private GeneradordeTabla(){super();}
    private final String separators="~()^v"+Operador.$BICONDITIONAL+Operador.$CONDITIONAL;
    /**
     * Construye un objeto Expression, para la evaluacion de la expresion dada
     * @param input Expresion proposicional inicial
     */
    public GeneradordeTabla(String input){
        this();
        outputMap=new LinkedHashMap<>();
        outputMap.put(true, "V ");
        outputMap.put(false, "F ");
        inputString=input;
        formatInputString();
        operation=Operacion.getInstance();
    }
    private void formatInputString(){
        inputString=inputString.replace(" ", "");
        inputString=inputString.toLowerCase();
        inputString=inputString.replace("<->", Operador.$BICONDITIONAL);
        inputString=inputString.replace("->", Operador.$CONDITIONAL);
        System.out.println("[LOG] Evaluando la expresion: "+inputString);
    }
  //Jerarquia de los operadores
  private int getPriority(String operador) {
    int precedencia = 0;
    switch(operador){
        case "~":
            precedencia=1;
            break;
        case "^":
            precedencia=2;
            break;
        case "v":
            precedencia=3;
            break;
        case Operador.$CONDITIONAL:
            precedencia=4;
            break;
        case Operador.$BICONDITIONAL:
            precedencia=5;
            break;
        case Operador.$XOR:
            precedencia=5;
            break;
        case "(":
            precedencia=0;
            break;
        case ")":
            precedencia=0;
            break;
    }

    return precedencia;
  }
  /**
   *    Este método realiza la evaluación de la expresión dada, 
   *    - Realiza el parsing de la cadena inicial
   *    - Genera una tabla de entrada con las combinaciones posibles para las proposiciones dadas.
   *    - Genera la tabla de verdad resultante despues de haber evaluado la expresion dada. 
   */
  public void evaluarExpresion(){
        
        // obtener tokens incluyendo separadores
        StringTokenizer tokens=new StringTokenizer(inputString,separators,true);
        // obtener tokens excluyendo separadores, para obtener asi las proposiciones que intervienen
        StringTokenizer proposition=new StringTokenizer(inputString,separators,false);
        
        LinkedList<String> stack;
        LinkedList<String> elements;
        
        stack=tokenToList(proposition,false);
        //obtener expresion en notacion post fija
        elements=parseInfix(tokenToList(tokens,true));
        // inicializamos la tabla de verdad, todas las combinaciones posibles con las proposiciones dadas
        initInputList(stack);
        System.out.println("[LOG] Expresion en notacion postfija: "+elements);
        inputList.stream().forEach((map) -> {
            calculateTable(elements,map);
        });
               
    }
  // determina si un token dado es un operador valido
    private boolean isOperator(String op){
        return (op.equals("^")||
                op.equals("v")||
                op.equals("~")||
                op.equals(Operador.$CONDITIONAL)||
                op.equals(Operador.$BICONDITIONAL)||
                op.equals(Operador.$XOR)
                );
    }
    /**
     * Obtiene la ultima columna de la tabla de verdad
     * @return list una lista que contiene la ultima colmna de la tabla de verdad
     */
    private List<Boolean> getLastColumn(){
        Boolean last=false;
        List<Boolean> list=new ArrayList<>();
        for(Map<String,Boolean> map:inputList){
            for(Boolean val:map.values())
                last=val;
            list.add(last);
        }
        return list;
    }
    /**
     * Convierte una expresion en notacion infija a una expresion en notacion prefija
     * implementacion de algoritmo shunting yard
     * fuente: http://es.wikipedia.org/wiki/Algoritmo_shunting_yard
     * @param input Expresion en notacion infija
     * @return Expresion en notacion prefija
     */
    private LinkedList<String> parseInfix(LinkedList<String> input){
        LinkedList<String> stack= new LinkedList<>();
        LinkedList<String> outputQueue= new LinkedList<>();
        input.stream().forEach(
        token ->{
            if(token.equals("(")){
                stack.push(token);
            }else if(isOperator(token)){
                while(!stack.isEmpty()&&isOperator(stack.peek()) && (getPriority(stack.peek())<getPriority(token) ) ){
                    outputQueue.add(stack.pop());
                }
                stack.push(token);
            }else if(token.equals(")")){
                while(!stack.peek().equals("(")){
                    outputQueue.add(stack.pop());
                }
                stack.pop();
            }else{
                outputQueue.add(token);
            }
        }
        );
        while(!stack.isEmpty()){
            outputQueue.add(stack.pop());
        }
        return outputQueue;
    }
    /**
     * Evalua la tabla de verdad y determina si esta es una tautologia
     * @return true si el resultado de la operacion es una tautologia
     */
    public boolean esTautologia(){
        Boolean test=true;
        test = getLastColumn().stream()
                .map((last) -> last)
                .reduce(test, 
                        (accumulator, _item) -> accumulator & _item);
        return test;
    }
    /**
     * Evalua la tabla de verdad y determina si esta es una Contradiccion
     * @return true si el resultado de la operacion es una Contradiccion
     */
    public boolean esContradiccion(){
        Boolean test=true;
        test = getLastColumn().stream()
                .map((last) -> !last)
                .reduce(test, 
                        (accumulator, _item) -> accumulator & _item);
        return test;
    }
    /**
     * Evalua la tabla de verdad y determina si esta es una Contingencia
     * @return true si el resultado de la operacion es una Contingencia
     */
    public boolean esContingencia(){
        return (!esTautologia())&&(!esContradiccion());
    }
    public void resaltarContraejemplo(){
        
    }
    /**permite extraer los tokens te una expresion, y evitar duplicar proposisiones
     * 
     * @param tokens Enumeration de tokens de entrada
     * @param duplicates true si se quiere la expresion completa, false para omitir repetidos
     * @return lista de tokens
     */
    private  LinkedList<String> tokenToList(StringTokenizer tokens,boolean duplicates){
       LinkedList<String> list=new LinkedList<>();
       while(tokens.hasMoreTokens()){
            String aux=tokens.nextToken();
            if(duplicates){
                list.add(aux);
            }else{
                if(!list.contains(aux)){
                   list.add(aux); 
                }
            }
        } 
       return list;
    }
    /**
     * resuelve la expresion dada
     * implementa el algoritmo RPN
     * fuente: http://es.wikipedia.org/wiki/Notación_polaca_inversa
     * 
     * @param tokens lista con los tokens de la expression
     * @param map tabla de verdad, para obtener operandos y colocar resultado
     * 
     */
    private void calculateTable(List<String> tokens,Map<String,Boolean> map){
        LinkedList<Boolean> stack=new LinkedList<>();
        tokens.stream().forEach(
            token ->{
                if(isOperator(token)){
                    stack.push(operation.test(token, stack));
                }else{
                    stack.push(map.get(token));
                }
            }
        );
        map.put(inputString, stack.pop());
    }
    LinkedHashMap<Boolean,String> outputMap=new LinkedHashMap<>();
    /**
     * Da formato a la tabla de verdad para su impresion
     * resaltando los contraejemplos encontrados en la tabla de verdad
     * 
     * @return tabla con formato de impresion
     */
    public String getFormateTableWithFalseEvaluations(){
        String out="";
        out = inputList.get(0)
                       .keySet()
                       .stream()
                       .map((value) -> value+" ")
                       .reduce(out, String::concat);
        out+="\n";
        out+="---------------------------------------------------------\n";
        
        for(Map<String,Boolean> map:inputList){
            if(!map.get(inputString)){
              out+="---------------------------------------------------------\n";  
            }
            out = map.values()
                     .stream()
                     .map((value) -> outputMap.get(value))
                     .reduce(out, String::concat);
            if(!map.get(inputString)){
              out+="\n---------------------------------------------------------";  
            }
            // agregando algunos espacios para mayor legibilidad
            out=out.substring(0,out.length()-2)+
                    "   "+
                    out.substring(out.length()-2,out.length());
            out+="\n";
        }
        return out;
    }
    /**
     * Da formato a la tabla de verdad para su impresion
     * @return tabla con formato de impresion
     */
    public String getFormatedTable(){
        String out="";
        out = inputList.get(0)
                       .keySet()
                       .stream()
                       .map((value) -> value+" ")
                       .reduce(out, String::concat);
        out+="\n";
        out+="---------------------------------------------------------\n";
        for(Map<String,Boolean> map:inputList){
            out = map.values()
                     .stream()
                     .map((value) -> outputMap.get(value))
                     .reduce(out, String::concat);
            // agregando algunos espacios para mayor legibilidad
            out=out.substring(0,out.length()-2)+
                    "   "+
                    out.substring(out.length()-2,out.length());
            out+="\n";
        }
        return out;
    }
    /** Permite clonar 1 mapa y sus valores
     *  @param map1 mapa a clonar
     */
    private Map<String,Boolean> clone(Map<String,Boolean> map1){
        Map<String,Boolean> map2 = new LinkedHashMap<>();
        Set<Entry<String,Boolean>> set1 = map1.entrySet();
        set1.stream().forEach((e) -> {
            map2.put(e.getKey(), e.getValue());
        });
        return map2;
    }
    private void initInputList(LinkedList<String> stack){
        inputList=buildTable(stack);
    }
    /**
     * Construye una tabla de verdad para las posibles combinaciones con las proposiciones dadas 
     */
    private List<Map<String,Boolean>> buildTable(LinkedList<String> queue){
        List<Map<String,Boolean>> localList=new ArrayList<>();
        Map<String, Boolean> auxMap;
        String operand=queue.pollLast();
        if(queue.isEmpty()){
            auxMap=new LinkedHashMap<>();
            auxMap.put(operand,true);
            localList.add(auxMap);
            
            auxMap=new LinkedHashMap<>();
            auxMap.put(operand,false);
            localList.add(auxMap);
        }else{
            for(Map map:buildTable(queue)){
                auxMap=map;
                auxMap.put(operand,true);
                localList.add(auxMap);
                auxMap=clone(map);
                auxMap.put(operand,false);
                localList.add(auxMap);
            }
        }
        return localList;
    }
    
}
