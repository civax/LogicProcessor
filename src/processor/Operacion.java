/*
 *  @version 1.0
 *  @author Carlos Iván Castillo Sepúlveda
 */

package processor;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * Esta clase es un singleton que define las operaciones lógicas soportadas por la aplicación.
 * @author Carlos Iván Castillo Sepúlveda
 * 
 */
public class Operacion {
    /**
     * Contiene un mapa para cada operador soportado, y su respectiva implementación en lambda expression
     * 
     */
    public final Map<String,Operador<Boolean>> operatorMap=new HashMap<>();
    private static Operacion operation;
    private void initOperations(){
        Operador<Boolean> $and = p -> p.pop()&p.pop();
        Operador<Boolean> $or = p -> p.pop()|p.pop();
        Operador<Boolean> $if = p -> (p.pop())|(!p.pop());
        Operador<Boolean> $biconditional = p -> !(p.pop()^p.pop());
        Operador<Boolean> $xor = p -> (p.pop()^p.pop());
        Operador<Boolean> $not = p ->  !p.pop();
        
        operatorMap.put("^", $and);
        operatorMap.put("v", $or);
        operatorMap.put(Operador.$CONDITIONAL, $if);
        operatorMap.put(Operador.$BICONDITIONAL, $biconditional);
        operatorMap.put(Operador.$XOR, $xor);
        operatorMap.put("~", $not);
    }
    private Operacion(){
        initOperations();       
    }
    /**
     * Realiza una operación unaria o binaria de proposiciones
     * @param operador un operador válido {~,^,v,\u2194,\u22BB,\u2192}
     * @param operandos una pila de operandos
     * @return regresa el resultado de la operación
     */
    public Boolean test(String operador,LinkedList<Boolean> operandos){
        return operatorMap.get(operador).test(operandos);
    }
    /**
     * Singleton, devuelve una instancia de Operacion
     * @return devuelve una instancia de Operacion
     *
     */
    public static Operacion getInstance(){
        if(operation==null){
            operation=new Operacion();
        }
        return operation;
    }
}
