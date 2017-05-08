/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pagos.Interface;

import com.pagos.Pojos.Gasto;
import java.util.Date;
import java.util.List;
import org.hibernate.Session;

/**
 *
 * @author williamSanchez
 */
public interface InterfaceGastos {

    public boolean registar(Session session, Gasto gasto) throws Exception;

    public boolean actualizar(Session session, Gasto gasto) throws Exception;

    public boolean eliminar(Session session, Gasto gasto) throws Exception;

    public Gasto getById(Session session, int id) throws Exception;

    public List<Gasto> getAll(Session session) throws Exception;

    public Gasto getByUltimoRegistro(Session session) throws Exception;

    public List<Gasto> getAllFecha(Session session, Date fechaInicio, Date fechaFin) throws Exception;

}
