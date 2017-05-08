/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pagos.clase;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.faces.validator.FacesValidator;

/**
 *
 * @author anderson
 */
@FacesValidator("validatorTexto")

public class ValidacionTexto {
    
    public boolean validar(String texto){
        
        boolean var=false;
           Pattern pattern = Pattern.compile("([a-z]|[A-Z]|\\s)+");
            Matcher matcher = pattern.matcher(texto);
            if (!matcher.matches()) {
               return var;
            }
            var=true;
            return var;
            
    }
}
