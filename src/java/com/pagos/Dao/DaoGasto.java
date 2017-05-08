/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pagos.Dao;

import com.pagos.Interface.InterfaceGastos;
import com.pagos.Pojos.Gasto;
import java.util.Date;
import java.util.List;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 *
 * @author williamSanchez
 */
public class DaoGasto implements InterfaceGastos{

    @Override
    public boolean registar(Session session, Gasto gasto) throws Exception {
session.save(gasto);
        return true;
    }

    @Override
    public boolean actualizar(Session session, Gasto gasto) throws Exception {
session.update(gasto);
return true;
    }

    @Override
    public boolean eliminar(Session session, Gasto gasto) throws Exception {
session.delete(gasto);
return true;
    }

    @Override
    public Gasto getById(Session session, int id) throws Exception {
  return (Gasto) session.get(Gasto.class, id);    }

    @Override
    public List<Gasto> getAll(Session session) throws Exception {
   String hql = "FROM Gasto";
        Query query = session.createQuery(hql);
        return (List<Gasto>) query.list();   
    }

    @Override
    public Gasto getByUltimoRegistro(Session session) throws Exception {
   String hql="from Gasto order by idgasto desc";
        Query query=session.createQuery(hql).setMaxResults(1);
        
        return (Gasto) query.uniqueResult();    }

    @Override
    public List<Gasto> getAllFecha(Session session, Date fechaInicio, Date fechaFin) throws Exception {
   String hql = "from Gasto where fecha between :fechaInicio and :fechaFin";
        Query query = session.createQuery(hql);
            query.setParameter("fechaInicio", fechaInicio);
            query.setParameter("fechaFin", fechaFin);
        return (List<Gasto>) query.list();  
    }

    
}
