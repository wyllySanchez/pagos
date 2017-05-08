/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pagos.Dao;

import com.pagos.Interface.InterfaceCaja;
import com.pagos.Pojos.Caja;
import java.util.Date;
import java.util.List;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 *
 * @author williamSanchez
 */
public class DaoCaja implements InterfaceCaja {

    @Override
    public boolean registar(Session session, Caja caja) throws Exception {
        session.save(caja);
        return true;
    }

    @Override
    public boolean actualizar(Session session, Caja caja) throws Exception {
        session.update(caja);
        return true;
    }

    @Override
    public boolean eliminar(Session session, Caja caja) throws Exception {
        session.delete(caja);
        return true;
    }

    @Override
    public Caja getById(Session session, int id) throws Exception {
        return (Caja) session.get(Caja.class, id);
    }

    @Override
    public List<Caja> getAll(Session session) throws Exception {
        String hql = "FROM Caja";
        Query query = session.createQuery(hql);
        return (List<Caja>) query.list();
    }

    @Override
    public Caja getByUltimoRegistro(Session session) throws Exception {
        String hql = "from Caja order by idcaja desc";
        Query query = session.createQuery(hql).setMaxResults(1);

        return (Caja) query.uniqueResult();
    }

    @Override
    public List<Caja> getAllFecha(Session session, Date fechaInicio, Date fechaFin) throws Exception {
        String hql = "from Caja where fecha between :fechaInicio and :fechaFin";
        Query query = session.createQuery(hql);
        query.setParameter("fechaInicio", fechaInicio);
        query.setParameter("fechaFin", fechaFin);
        return (List<Caja>) query.list();
    }

}
