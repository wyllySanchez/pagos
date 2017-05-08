/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pagos.Interface;

import com.pagos.Pojos.Rol;
import java.util.List;
import org.hibernate.Session;

/**
 *
 * @author Sebastian
 */
public interface InterfaceRol {
       public boolean registrar(Session session, Rol rol) throws Exception;

    public List<Rol> getAll(Session session) throws Exception;
    

    public Rol getByCodigoEmpleado(Session session, int codigoEmpleado) throws Exception;

    public  Rol getByRol(Session session, int codigoEmpleado)throws Exception;

    public boolean update(Session session, Rol rol) throws Exception;

    public boolean delete(Session session, Rol rol) throws Exception;

    public Rol getByDocumento(Session session, String numeroDocumento) throws Exception;
        public Rol getById(Session session,int id)throws Exception;
    
    
}
