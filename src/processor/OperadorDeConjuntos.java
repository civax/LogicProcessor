/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package processor;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.TreeSet;

/**
 *
 * @author carcasti
 */
public class OperadorDeConjuntos {
    private static OperadorDeConjuntos operador;
    /**
     *  Obtiene la Union de una lista de conjuntos
     * @param conjuntos Lista enlazada de conjuntos
     * @return regresa un conjunto ordenado con la union de los conjuntos dados
     */
    public TreeSet<String> getUnion(List<List<String>> conjuntos){
        TreeSet<String> resultado=new TreeSet<>();
        for(List<String> conjunto:conjuntos){
            resultado.addAll(conjunto);
        }
        return resultado;
    }
    private TreeSet<String> getInterseccion(TreeSet<String> conjuntoA,TreeSet<String> conjuntoB){
        List<String> listaA=new LinkedList<>();
        List<String> listaB=new LinkedList<>();
        conjuntoA.forEach(elem->listaA.add(elem));
        conjuntoB.forEach(elem->listaB.add(elem));
        LinkedList<List<String>> lista=new LinkedList<>();
        lista.add(listaA);
        lista.add(listaB);
        return getInterseccion(lista);
    }
    /**
     *  Obtiene la interseccion de una lista de conjuntos
     * @param conjuntos Lista enlazada de conjuntos
     * @return regresa un conjunto ordenado con la interseccion de los conjuntos dados
     */
    public TreeSet<String> getInterseccion(LinkedList<List<String>> conjuntos){
        TreeSet<String> resultado=new TreeSet<>();
        List<String> resultadoParcial=new LinkedList<>();
        List<String> resultadoParcial2=new LinkedList<>();
        resultadoParcial=conjuntos.poll();
        for(List<String> conjunto:conjuntos){
            for(String elementoA:resultadoParcial){
                for(String elementoB:conjunto){
                    if(elementoA.equals(elementoB)){
                        resultadoParcial2.add(elementoA);
                        break;
                    }
                }
            }
            resultadoParcial=resultadoParcial2;
            resultadoParcial2=new LinkedList<>();
        }
        resultado.addAll(resultadoParcial);
        return resultado;
    }
    /**
     *  Obtiene el complemento del conjunto dado
     * @param universo el universo de la operacion
     * @param conjunto Conjunto del cual se obtendra su complemento
     * @return regresa el complemento del conjunto dado
     */
    public TreeSet<String> getComplemento(TreeSet<String> universo,List<String> conjunto){
        List<String> universoL=new LinkedList<>();
        universo.forEach(elem->universoL.add(elem));
        return getDiferencia(universoL,conjunto);
    }
     /**
     *  Obtiene la diferencia de el conjunto A y el conjunto B de la forma A-B, donde el primer parametro sera el conjunto A y el segundo parametro el conjunto B
     * @param conjuntoA Conjunto A
     * @param conjuntoB Conjunto B
     * @return regresa un conjunto ordenado con la diferencia de los conjuntos dados
     */
    public TreeSet<String> getDiferencia(List<String> conjuntoA,List<String> conjuntoB){
        TreeSet<String> resultado=new TreeSet<>();
        List<String> resultadoParcial=new LinkedList<>();
        principal:
        for(String elementoA:conjuntoA){
            for(String elementoB:conjuntoB){
                if(elementoA.equals(elementoB)){
                    continue principal;
                }
            }
            resultadoParcial.add(elementoA); 
        }
        resultado.addAll(resultadoParcial);
        return resultado;
    }
    /**
     *  Obtiene la diferencia simetrica de el conjunto A y el conjunto B de la forma A-B, donde el primer parametro sera el conjunto A y el segundo parametro el conjunto B
     * @param conjuntoA Conjunto A
     * @param conjuntoB Conjunto B
     * @return regresa un conjunto ordenado con la diferencia simetrica de los conjuntos dados
     */
    public TreeSet<String> getDiferenciaSimetrica(List<String> conjuntoA,List<String> conjuntoB){
        TreeSet<String> parcialA=getDiferencia(conjuntoA,conjuntoB);
        TreeSet<String> parcialB=getDiferencia(conjuntoB,conjuntoA);
        return getUnion(parcialA,parcialB);
    }
    private OperadorDeConjuntos(){}
    /**
     * Singleton, devuelve una instancia de OperadorDeConjuntos
     * @return devuelve una instancia de OperadorDeConjuntos
     *
     */
    public static OperadorDeConjuntos getInstance(){
        if(Objects.isNull(operador)){
            operador=new OperadorDeConjuntos();
        }
        return operador;
    }

    private TreeSet<String> getUnion(TreeSet<String> conjuntoA, TreeSet<String> conjuntoB) {
        List<String> listaA=new LinkedList<>();
        List<String> listaB=new LinkedList<>();
        conjuntoA.forEach(elem->listaA.add(elem));
        conjuntoB.forEach(elem->listaB.add(elem));
        LinkedList<List<String>> lista=new LinkedList<>();
        lista.add(listaA);
        lista.add(listaB);
        return getUnion(lista);
    }
}
