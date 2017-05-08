/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pagos.Interface;

import com.pagos.Pojos.Abonosecundario;
import java.util.Date;
import java.util.List;
import org.hibernate.Session;

/**
 *
 * @author ander
 */
public interface InterfaceAbonoSecundario {
    public boolean registar(Session session,Abonosecundario abonosecundario)throws Exception;
    public boolean actualizar(Session session,Abonosecundario abonosecundario)throws Exception;
    public boolean eliminar(Session session,Abonosecundario abonosecundario)throws Exception;
    public Abonosecundario getById(Session session,int id)throws Exception;
    public List<Abonosecundario> getAll(Session session)throws Exception;  
    public List<Abonosecundario> getAllByIdAbonos(Session session,int id)throws Exception;
    public List<Abonosecundario> getAllByDate(Session session,Date fechaInicio,Date fechaFin)throws Exception;
        public List<Abonosecundario> getAllByAbonos(Session session,String codigo)throws Exception;

}
