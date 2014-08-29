/*
 *  @version 1.0
 *  @author Carlos Iván Castillo Sepúlveda
 */

package processor;

/**
 * Esta interfaz establece la plantilla para las evaluaciones de equivalencias logicas
 * @author Carlos Iván Castillo Sepúlveda
 * @param <T> tipo de la expresion a evaluar
 */
@FunctionalInterface
public interface Equivalencia<T> {
    T aplicar(T expresion);   
}
