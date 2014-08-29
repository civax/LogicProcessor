/*
 *  @version 1.0
 *  @author Carlos Iván Castillo Sepúlveda
 */

package processor;

import java.util.HashMap;
import java.util.Map;

/**
 * Esta clase es un singleton que define las equivalencias lógicas soportadas por la aplicación.
 * @author Carlos Iván Castillo Sepúlveda
 */
public class Simplificador {
    /**
     * Contiene un mapa para cada equivalencia soportada, y su respectiva implementación en lambda expression
     * 
     */
    public final Map<String,Equivalencia<String>> equivalenciasMap=new HashMap<>();
    private static Simplificador simplificador;
    /**
     * Contiene la regex para un operando valido
     */
    public static String operando="(~?[a-z&&[^v]]+)";
        /**
     * Contiene la regex para un operador valido
     */
    public static String operador="([\\^v"+Operador.$BICONDITIONAL+Operador.$CONDITIONAL+"])";
    /**
     * Contiene la regex para una operacion valido
     */
    public static String operacion=operando+"|"+
                     "(~?\\("+operando+"\\)"+"|"+
                     "~?\\("+operando+operador+operando+"\\))";
    
    private void initOperations(){
        Equivalencia<String> $morgan = expresion -> {
            String output="";
            int estado=0;
            String $P="";
            String $Q="";
            String $OP="";
            for(char car:expresion.toCharArray()){
                switch(estado){
                    case 0:
                        if(car=='~'){
                            estado=1;
                        }
                        break;
                    case 1:
                        if(car=='('){
                            estado=2;
                        }
                        break;
                    case 2:
                        if(car=='v'){
                            if($P.matches(operacion)){
                             estado=3;
                                $OP="^";   
                            }else{
                                $P+=car;
                            } 
                        }else if(car=='^'){
                            if($P.matches(operacion)){
                                estado=3;
                                $OP="v";
                            }else{
                                $P+=car;
                            }
                        }else{
                            $P+=car;
                        }
                        break;
                    case 3:
                        if(car==')'){
                            if($Q.matches(operacion)){
                                estado=4;
                            }else{
                                $Q+=car;
                            }
                        }
                        else{
                           $Q+=car; 
                        }
                        break;
                }
            }
            if(estado==4&&$P.matches(operacion)&&$Q.matches(operacion)){
                output="(~"+$P+$OP+"~"+$Q+")";
            }else{
                throw new IllegalArgumentException("No se puede aplcar Morgan a la expresion dada");
            }
            return output;
        };
        Equivalencia<String> _morgan =expresion -> {
                        String output="";
            int estado=0;
            String $P="";
            String $Q="";
            String $OP="";
            for(char car:expresion.toCharArray()){
                switch(estado){
                    case 0:
                        if(car=='~'){
                            estado=1;
                        }
                        break;
                    case 1:
                        if(car=='v'){
                            if($P.matches(operacion)){
                             estado=2;
                                $OP="^";   
                            }else{
                                $P+=car;
                            } 
                        }else if(car=='^'){
                            if($P.matches(operacion)){
                                estado=2;
                                $OP="v";
                            }else{
                                $P+=car;
                            }
                        }else{
                            $P+=car;
                        }
                        break;
                    case 2:
                        if(car=='~'){
                            estado=3;
                        }
                        break;
                    case 3:
                        $Q+=car; 
                        break;
                }
            }
            if(estado==3&&$P.matches(operacion)&&$Q.matches(operacion)){
                output="(~("+$P+$OP+$Q+"))";
            }else{
                throw new IllegalArgumentException("No se puede revertir Morgan en la expresion dada");
            }
            return output;
        };
        Equivalencia<String> $bicondicional = expresion -> {
            String output="";
            int estado=0;
            String $P="";
            String $Q="";
            //expresion=removerParentesisExterno(expresion);
            for(char car:expresion.toCharArray()){
                switch(estado){
                    case 0:
                        if(car==Operador.$BICONDITIONAL.charAt(0)){
                            if($P.matches(operacion)){
                                estado=1; 
                            }else{
                                $P+=car;
                            }
                        }else{
                            $P+=car;
                        } 
                        break;
                    case 1:
                        $Q+=car;
                        break;
                }
            }
            if(estado==1&&$P.matches(operacion)&&$Q.matches(operacion)){
                output="("+$P+Operador.$CONDITIONAL+$Q+")^("+$Q+Operador.$CONDITIONAL+$P+")";
            }else{
                throw new IllegalArgumentException("No se puede aplcar Bicondicional a la expresion dada");
            }
            return output;                
        };
        Equivalencia<String> _bicondicional = expresion -> {
            String output="";
            int estado=0;
            String $P="";
            String $Q="";
            String $R="";
            String $S="";
            for(char car:expresion.toCharArray()){
                switch(estado){
                    case 0:
                        if(car=='('){
                            estado=1;
                        }
                        break;
                    case 1:
                        if(car==Operador.$CONDITIONAL.charAt(0)){
                            if($P.matches(operacion)){
                             estado=2; 
                            }else{
                                $P+=car;
                            } 
                        }else{
                            $P+=car;
                        }
                        break;
                    case 2:
                        if(car==')'){
                            if($Q.matches(operacion)){
                                estado=3;
                            }else{
                                $Q+=car;
                            }
                        }
                        else{
                           $Q+=car; 
                        }                        
                        break;
                    case 3:
                        if(car=='^'){
                            estado=4;
                        }
                        break;
                    case 4:
                        if(car=='('){
                            estado=5;
                        }
                        break;
                    case 5:
                        if(car==Operador.$CONDITIONAL.charAt(0)){
                            if($R.matches(operacion)){
                             estado=6; 
                            }else{
                                $R+=car;
                            } 
                        }else{
                            $R+=car;
                        }
                        break;
                    case 6:
                        if(car==')'){
                            if($S.matches(operacion)){
                                estado=7;
                            }else{
                                $S+=car;
                            }
                        }
                        else{
                           $S+=car; 
                        }                        
                        break;
                }
            }
            if(estado==7&&$P.matches(operacion)&&$Q.matches(operacion)&&$R.matches(operacion)&&$S.matches(operacion)){
                if($P.equals($S)&&$Q.equals($R)){
                    output="("+$P+Operador.$BICONDITIONAL+$Q+")";
                }else{
                   throw new IllegalArgumentException("No se puede Revertir Bicondicional en la expresion dada"); 
                }
                
            }else{
                throw new IllegalArgumentException("No se puede Revertir Bicondicional en la expresion dada verifica");
            }
            return output;
        };
        Equivalencia<String> $implicacion = expresion -> {
            String output="";
            int estado=0;
            String $P="";
            String $Q="";
           // expresion=removerParentesisExterno(expresion);
            for(char car:expresion.toCharArray()){
                switch(estado){
                    case 0:
                        if(car==Operador.$CONDITIONAL.charAt(0)){
                            if($P.matches(operacion)){
                                estado=1; 
                            }else{
                                $P+=car;
                            }
                        }else{
                            $P+=car;
                        } 
                        break;
                    case 1:
                        $Q+=car;
                        break;
                }
            }
            if(estado==1&&$P.matches(operacion)&&$Q.matches(operacion)){
                output="(~"+$P+"v"+$Q+")";
            }else{
                throw new IllegalArgumentException("No se puede aplcar Condicional a la expresion");
            }
            return output;
        };
        Equivalencia<String> _disimplicacion =  expresion -> {
            String output="";
            int estado=1;
            String $P="";
            String $Q="";
            for(char car:expresion.toCharArray()){
                switch(estado){
                    case 1:
                        if(car=='v'){
                            if($P.matches(operacion)){
                                estado=2; 
                            }else{
                                $P+=car;
                            }
                        }else{
                            $P+=car;
                        } 
                        break;
                    case 2:
                        $Q+=car;
                        break;
                }
            }
            if(estado==2&&$P.matches(operacion)&&$Q.matches(operacion)){
                output="(~"+$P+Operador.$CONDITIONAL+$Q+")";
            }else{
                throw new IllegalArgumentException("No se puede aplicar disyuncion a implicacion a la expresion dada");
            }
            return output;  
        };
        Equivalencia<String> _implicacion =  expresion -> {
            String output="";
            int estado=0;
            String $P="";
            String $Q="";
            for(char car:expresion.toCharArray()){
                switch(estado){
                    case 0:
                        if(car=='~'){
                            estado=1;
                        }
                        break;
                    case 1:
                        if(car=='v'){
                            if($P.matches(operacion)){
                                estado=2; 
                            }else{
                                $P+=car;
                            }
                        }else{
                            $P+=car;
                        } 
                        break;
                    case 2:
                        $Q+=car;
                        break;
                }
            }
            if(estado==2&&$P.matches(operacion)&&$Q.matches(operacion)){
                output="("+$P+Operador.$CONDITIONAL+$Q+")";
            }else{
                throw new IllegalArgumentException("No se puede aplcar Bicondicional a la expresion dada");
            }
            return output;  
        };
        Equivalencia<String> $contrapositiva =expresion -> {
            String output="";
            int estado=0;
            String $P="";
            String $Q="";
//            expresion=removerParentesisExterno(expresion);
            for(char car:expresion.toCharArray()){
                switch(estado){
                    case 0:
                        if(car==Operador.$CONDITIONAL.charAt(0)){
                            if($P.matches(operacion)){
                                estado=1; 
                            }else{
                                $P+=car;
                            }
                        }else{
                            $P+=car;
                        } 
                        break;
                    case 1:
                        $Q+=car;
                        break;
                }
            }
            if(estado==1&&$P.matches(operacion)&&$Q.matches(operacion)){
                output="(~"+$Q+Operador.$CONDITIONAL+"~"+$P+")";
            }else{
                throw new IllegalArgumentException("No se puede aplcar Contrapositiva a la expresion dada");
            }
            return output;
        };
        Equivalencia<String> $conmutativa =expresion -> {
            String output="";
            int estado=0;
            String $P="";
            String $Q="";
            String $OP="";
            //expresion=removerParentesisExterno(expresion);
            for(char car:expresion.toCharArray()){
                switch(estado){
                    case 0:
                        if(car=='v'||car=='^'){
                            if($P.matches(operacion)){
                                $OP=car+"";  
                                estado=1;
                            }else{
                                $P+=car;
                            }
                          
                        }else{
                            $P+=car;
                        } 
                        break;
                    case 1:
                        $Q+=car;
                        break;
                }
            }
            if(estado==1&&$P.matches(operacion)&&$Q.matches(operacion)){
                output="("+$Q+$OP+$P+")";
            }else{
                throw new IllegalArgumentException("No se puede aplcar Contrapositiva a la expresion dada");
            }
            return output;
        };
        /*Equivalencia<String> $asociativa =expresion -> {
            return expresion;
        };*/
        Equivalencia<String> $distributiva =expresion -> {
            String output="";
            int estado=0;
            String $P="";
            String $Q="";
            String $R="";
            String $OP1="";
            String $OP2="";
            for(char car:expresion.toCharArray()){
                switch(estado){
                    case 0:
                        if(car=='v'){
                            if($P.matches(operacion)){
                                estado=1;
                                $OP1="v";   
                            }else{
                                $P+=car;
                            } 
                        }else if(car=='^'){
                            if($P.matches(operacion)){
                                estado=1;
                                $OP1="^";
                            }else{
                                $P+=car;
                            }
                        }else{
                            $P+=car;
                        }
                    break;
                    case 1:
                        if(car=='('){
                           estado=2;
                        }
                        break;
                    case 2:
                    if(car=='v'){
                        if($Q.matches(operacion)){
                            estado=3;
                            $OP2="v";   
                        }else{
                        $Q+=car;
                        } 
                    }else if(car=='^'){
                        if($Q.matches(operacion)){
                            estado=3;
                            $OP2="^";
                        }else{
                            $Q+=car;
                        }
                    }else{
                        $Q+=car;
                    }
                    break;
                    case 3:
                        if(car==')'){
                            if($R.matches(operacion)){
                                estado=4;
                            }else{
                                $R+=car;
                            }
                        }
                        else{
                           $R+=car; 
                        }
                        break;
                }
            }
            if(estado==4&&$P.matches(operacion)&&$Q.matches(operacion)&&$R.matches(operacion)){
                output="(("+$P+$OP1+$Q+")"+$OP2+"("+$P+$OP1+$R+"))";
            }else{
                throw new IllegalArgumentException("No se puede aplcar Distributiva a la expresion dada");
            }
            return output;
        };
        Equivalencia<String> _distributiva =expresion -> {
            String output="";
            int estado=0;
            String $P="";
            String $Q="";
            String $R="";
            String $S="";
            String $OP1="";
            String $OP2="";
            for(char car:expresion.toCharArray()){
                switch(estado){
                    case 0:
                        if(car=='('){
                            estado=1;
                        }
                        break;
                    case 1:
                        if(car=='^'||car=='v'){
                            if($P.matches(operacion)){
                             estado=2; 
                             $OP1+=car;
                            }else{
                                $P+=car;
                            } 
                        }else{
                            $P+=car;
                        }
                        break;
                    case 2:
                        if(car==')'){
                            if($Q.matches(operacion)){
                                estado=3;
                            }else{
                                $Q+=car;
                            }
                        }
                        else{
                           $Q+=car; 
                        }                        
                        break;
                    case 3:
                        if(car=='^'||car=='v'){
                            $OP2+=car;
                            if(!$OP1.equals($OP2)){
                                estado=4; 
                            }else{
                                throw new IllegalArgumentException("No se puede Revertir Distributiva en la expresion dada"); 
                            }
                            
                        }
                        break;
                    case 4:
                        if(car=='('){
                            estado=5;
                        }
                        break;
                    case 5:
                        if(car=='^'||car=='v'){
                            if($R.matches(operacion)){
                                if($OP1.equals(car+"")){
                                    estado=6; 
                                }else{
                                    throw new IllegalArgumentException("No se puede Revertir Distributiva en la expresion dada"); 
                                }
                            }else{
                                $R+=car;
                            } 
                        }else{
                            $R+=car;
                        }
                        break;
                    case 6:
                        if(car==')'){
                            if($S.matches(operacion)){
                                estado=7;
                            }else{
                                $S+=car;
                            }
                        }
                        else{
                           $S+=car; 
                        }                        
                        break;
                }
            }
            if(estado==7&&$P.matches(operacion)&&$Q.matches(operacion)&&$R.matches(operacion)&&$S.matches(operacion)){
                if($P.equals($R)){
                    output="("+$P+$OP1+"("+$Q+$OP2+$S+"))";
                }else{
                   throw new IllegalArgumentException("No se puede Revertir Distributiva en la expresion dada"); 
                }
                
            }else{
                throw new IllegalArgumentException("No se puede Revertir Distributiva en la expresion dada");
            }
            return output;
        };
        Equivalencia<String> $identidad =expresion -> {
            String output="";
            int estado=0;
            String $P="";
            String $Q="";
            String $OP="";
//            expresion=removerParentesisExterno(expresion);
            for(char car:expresion.toCharArray()){
                switch(estado){
                    case 0:
                        if(car=='^'||car=='v'){
                            if($P.matches(operacion)){
                                estado=1;
                                $OP+=car;
                            }else{
                                $P+=car;
                            }
                        }else{
                            $P+=car;
                        } 
                        break;
                    case 1:
                        $Q+=car;
                        break;
                }
            }
            if(estado==1&&$P.matches(operacion)&&$Q.matches(operacion)){
                if($P.equals("c")&&!$Q.equals("c")){
                    if($OP.equals("v")){
                        output=$Q;
                    }else{
                        throw new IllegalArgumentException("No se puede aplcar Identidad a la expresion dada");
                    }
                } else if(!$P.equals("c")&&$Q.equals("c")){
                    if($OP.equals("v")){
                        output=$P;
                    }else{
                        throw new IllegalArgumentException("No se puede aplcar Identidad a la expresion dada");
                    }
                }else if($P.equals("t")&&!$Q.equals("t")){
                    if($OP.equals("^")){
                        output=$Q;
                    }else{
                        throw new IllegalArgumentException("No se puede aplcar Identidad a la expresion dada");
                    }
                }else if(!$P.equals("t")&&$Q.equals("t")){
                    if($OP.equals("^")){
                        output=$P;
                    }else{
                        throw new IllegalArgumentException("No se puede aplcar Identidad a la expresion dada");
                    }
                }else{
                    throw new IllegalArgumentException("No se puede aplcar Identidad a la expresion dada");
                }
 
            }else{
                throw new IllegalArgumentException("No se puede aplcar Identidad a la expresion dada");
            }
            return output;
        };
        Equivalencia<String> $negacion =expresion -> {
            String output="";
            int estado=0;
            String $P="";
            String $Q="";
            String $OP="";
//            expresion=removerParentesisExterno(expresion);
            for(char car:expresion.toCharArray()){
                switch(estado){
                    case 0:
                        if(car=='^'||car=='v'){
                            if($P.matches(operacion)){
                                estado=1;
                                $OP+=car;
                            }else{
                                $P+=car;
                            }
                        }else{
                            $P+=car;
                        } 
                        break;
                    case 1:
                        $Q+=car;
                        break;
                }
            }
            if(estado==1&&$P.matches(operacion)&&$Q.matches(operacion)){
                if(("~"+$P).equals($Q)||("~"+$Q).equals($P)){
                    if($OP.equals("^")){
                        output="c";
                    }else if($OP.equals("v")){
                        output="t";
                    }else{
                        throw new IllegalArgumentException("No se puede aplcar Negacion a la expresion dada");
                    }
                } else{
                    throw new IllegalArgumentException("No se puede aplcar Negacion a la expresion dada");
                }
 
            }else{
                throw new IllegalArgumentException("No se puede aplcar Negacion a la expresion dada");
            }
            return output;
        };
        Equivalencia<String> $doble =expresion -> {
                        String output="";
            int estado=0;
            String $P="";
            for(char car:expresion.toCharArray()){
                switch(estado){
                    case 0:
                        if(car=='~'){
                            estado=1;
                        }
                        break;
                    case 1:
                        if(car=='~'){
                            estado=2;
                        }else if(car=='('){
                            estado=3;
                        }
                        break;
                    case 2:
                        $P+=car;
                        break;
                    case 3:
                        if(car=='~'){
                            estado=4;
                        }
                        break;
                    case 4:
                        if(car==')'){
                            estado=5;
                        }else{
                            $P+=car; 
                        }
                        break;
                }
            }
            if((estado==2||estado==5)&&$P.matches(operacion)){
                output="("+$P+")";
            }else{
                throw new IllegalArgumentException("No se puede aplcar Doble Negacion a la expresion dada");
            }
            return output;
        };
        Equivalencia<String> $idempotencia =expresion -> {
            String output="";
            int estado=0;
            String $P="";
            String $Q="";
//            expresion=removerParentesisExterno(expresion);
            for(char car:expresion.toCharArray()){
                switch(estado){
                    case 0:
                        if(car=='v'||car=='^'){
                            if($P.matches(operacion)){
                                estado=1; 
                            }else{
                                $P+=car;
                            }
                        }else{
                            $P+=car;
                        } 
                        break;
                    case 1:
                        $Q+=car;
                        break;
                }
            }
            if(estado==1&&$P.matches(operacion)&&$Q.matches(operacion)){
                if($P.equals($Q)){
                  output="("+$P+")";  
                }else{
                    throw new IllegalArgumentException("No se puede aplcar Idempotencia a la expresion dada");
                }
            }else{
                throw new IllegalArgumentException("No se puede aplcar Idempotencia a la expresion dada");
            }
            return output;
        };
        Equivalencia<String> $acotadas =expresion -> {
            String output="";
            int estado=0;
            String $P="";
            String $Q="";
            String $OP="";
//            expresion=removerParentesisExterno(expresion);
            for(char car:expresion.toCharArray()){
                switch(estado){
                    case 0:
                        if(car=='^'||car=='v'){
                            if($P.matches(operacion)){
                                estado=1;
                                $OP+=car;
                            }else{
                                $P+=car;
                            }
                        }else{
                            $P+=car;
                        } 
                        break;
                    case 1:
                        $Q+=car;
                        break;
                }
            }
            if(estado==1&&$P.matches(operacion)&&$Q.matches(operacion)){
                if(($P.equals("c")&&!$Q.equals("c"))||(!$P.equals("c")&&$Q.equals("c"))){
                    if($OP.equals("^")){
                        output="c";
                    }else{
                        throw new IllegalArgumentException("No se puede aplcar Universales Acotadas a la expresion dada");
                    }
                }else if(($P.equals("t")&&!$Q.equals("t"))||(!$P.equals("t")&&$Q.equals("t"))){
                    if($OP.equals("v")){
                        output="t";
                    }else{
                        throw new IllegalArgumentException("No se puede aplcar Universales Acotadas a la expresion dada");
                    }
                } else{
                    throw new IllegalArgumentException("No se puede aplcar Universales Acotadas a la expresion dada");
                }
 
            }else{
                throw new IllegalArgumentException("No se puede aplcar Universales Acotadas a la expresion dada");
            }
            return output;
        };
        Equivalencia<String> $absorcion =expresion -> {
            String output="";
            int estado=0;
            String $P="";
            String $Q="";
            String $R="";
            String $OP1="";
            String $OP2="";
            for(char car:expresion.toCharArray()){
                switch(estado){
                    case 0:
                        if(car=='v'){
                            if($P.matches(operacion)){
                                estado=1;
                                $OP1="v";   
                            }else{
                                $P+=car;
                            } 
                        }else if(car=='^'){
                            if($P.matches(operacion)){
                                estado=1;
                                $OP1="^";
                            }else{
                                $P+=car;
                            }
                        }else{
                            $P+=car;
                        }
                    break;
                    case 1:
                        if(car=='('){
                           estado=2;
                        }
                        break;
                    case 2:
                    if(car=='v'){
                        if($Q.matches(operacion)){
                            estado=3;
                            $OP2="v";   
                        }else{
                        $Q+=car;
                        } 
                    }else if(car=='^'){
                        if($Q.matches(operacion)){
                            estado=3;
                            $OP2="^";
                        }else{
                            $Q+=car;
                        }
                    }else{
                        $Q+=car;
                    }
                    break;
                    case 3:
                        if(car==')'){
                            estado=4;
                        }else{
                            $R+=car; 
                        }
                        break;
                }
            }
            if(estado==4&&$P.matches(operacion)&&$Q.matches(operacion)&&$R.matches(operacion)){
                if(($P.equals($Q)||$P.equals($R))){
                   output="("+$P+")"; 
                }else{
                    throw new IllegalArgumentException("No se puede aplcar Absorcion a la expresion dada");
                } 
            }else{
                throw new IllegalArgumentException("No se puede aplcar Absorcion a la expresion dada");
            }
            return output;
        };
        
        equivalenciasMap.put("Bicondicional",$bicondicional);
        equivalenciasMap.put("Revertir Bicondicional",_bicondicional);
        equivalenciasMap.put("Implicacion",$implicacion);
        equivalenciasMap.put("Disyuncion a implicacion",_disimplicacion);
        equivalenciasMap.put("Revertir Implicacion",_implicacion);
        equivalenciasMap.put("Contrapositiva",$contrapositiva);
        equivalenciasMap.put("Conmutativa",$conmutativa);
//        equivalenciasMap.put("Asociativa",$asociativa);
        equivalenciasMap.put("Distributiva",$distributiva);
        equivalenciasMap.put("Revertir Distributiva",_distributiva);
        equivalenciasMap.put("Identidad",$identidad);
        equivalenciasMap.put("Negación",$negacion);
        equivalenciasMap.put("Doble Negación",$doble);
        equivalenciasMap.put("Idempotencia",$idempotencia);
        equivalenciasMap.put("Universales acotadas",$acotadas);
        equivalenciasMap.put("Morgan",$morgan);
        equivalenciasMap.put("Revertir Morgan",_morgan);
        equivalenciasMap.put("Absorción",$absorcion);
    }
    private Simplificador(){
        initOperations();       
    }
    /**
     * Obtiene el equivalente logico de la expresion dada
     * @param equivalencia Indica la equivalencia que se busca obtener (ej. Morgan, distriburiva, etc)
     * @param expresion Expresion a evaluar
     * @return regresa la equivalencia logica de la expresion dada
     * @throws IllegalArgumentException si la equivalencia no puede ser obtenida de la expresion dada
     */
    public String aplicar(String equivalencia,String expresion)throws IllegalArgumentException{
        return equivalenciasMap.get(equivalencia).aplicar(expresion);
    }
    /**
     * Singleton, devuelve una instancia de Simplificador
     * @return devuelve una instancia de Simplificador
     *
     */
    public static Simplificador getInstance(){
        if(simplificador==null){
            simplificador=new Simplificador();
        }
        return simplificador;
    }
}
