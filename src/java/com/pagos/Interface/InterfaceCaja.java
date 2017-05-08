/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pagos.Interface;

import com.pagos.Pojos.Caja;
import java.util.Date;
import java.util.List;
import org.hibernate.Session;

/**
 *
 * @author williamSanchez
 */
public interface InterfaceCaja {
       public boolean registar(Session session, Caja caja) throws Exception;

    public boolean actualizar(Session session, Caja caja) throws Exception;

    public boolean eliminar(Session session, Caja caja) throws Exception;

    public Caja getById(Session session, int id) throws Exception;

    public List<Caja> getAll(Session session) throws Exception;

    public Caja getByUltimoRegistro(Session session) throws Exception;

    public List<Caja> getAllFecha(Session session, Date fechaInicio, Date fechaFin) throws Exception;
}
