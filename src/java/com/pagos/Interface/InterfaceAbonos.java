/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pagos.Interface;

import com.pagos.Pojos.Abonos;
import java.util.Date;
import java.util.List;
import org.hibernate.Session;

/**
 *
 * @author William Sanchez
 */
public interface InterfaceAbonos {
       public boolean registar(Session session,Abonos abonos)throws Exception;
    public boolean actualizar(Session session,Abonos abonos)throws Exception;
    public boolean eliminar(Session session,Abonos abonos)throws Exception;
    public Abonos getById(Session session,int id)throws Exception;
    public List<Abonos> getAll(Session session)throws Exception;   
    public Abonos getByUltimoRegistro(Session session)throws Exception;
    public List<Abonos> getAllFecha(Session session, Date fechaInicio, Date fechaFin) throws Exception;
    public List<Abonos> getAllByCliente(Session session,String numeroDocumento)throws Exception;
      public List<Abonos> getAllAbonos(Session session)throws Exception;  
}
