/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pagos.Interface;

import com.pagos.Pojos.Tipodocumento;
import java.util.List;
import org.hibernate.Session;

/**
 *
 * @author Sebastian
 */
public interface InterfaceTipodocumento {
    
    public boolean registrar(Session session, Tipodocumento tipodocumento)throws Exception;
    public List<Tipodocumento> getAll(Session session)throws Exception;
    public Tipodocumento getByCodigoTipodocumeno(Session session, int codigoEmpleado)throws Exception;
    public boolean update(Session session, Tipodocumento tipodocumento)throws Exception; 
}
