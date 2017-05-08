/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pagos.Dao;

import com.pagos.Interface.InterfaceRuta;
import com.pagos.Pojos.Ruta;
import java.util.List;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 *
 * @author williamSanchez
 */
public class DaoRuta implements InterfaceRuta {

    @Override
    public boolean registrar(Session session, Ruta ruta) throws Exception {
        session.save(ruta);
        return true;
    }

    @Override
    public List<Ruta> getAll(Session session) throws Exception {
        String hql = "FROM Ruta";
        Query query = session.createQuery(hql);
        List<Ruta> listaRuta = (List<Ruta>) query.list();
        return listaRuta;
    }

    @Override
    public boolean update(Session session, Ruta ruta) throws Exception {
        session.update(ruta);
        return true;
    }

    @Override
    public boolean delete(Session session, Ruta ruta) throws Exception {
        session.delete(ruta);
        return true;
    }

    @Override
    public Ruta getById(Session session, int id) throws Exception {
        return (Ruta) session.get(Ruta.class, id);
    }

}
