/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pagos.Interface;

import com.pagos.Pojos.Ruta;
import java.util.List;
import org.hibernate.Session;

/**
 *
 * @author williamSanchez
 */
public interface InterfaceRuta {

    public boolean registrar(Session session, Ruta ruta) throws Exception;

    public List<Ruta> getAll(Session session) throws Exception;

    public boolean update(Session session, Ruta ruta) throws Exception;

    public boolean delete(Session session, Ruta ruta) throws Exception;

    public Ruta getById(Session session, int id) throws Exception;
}
