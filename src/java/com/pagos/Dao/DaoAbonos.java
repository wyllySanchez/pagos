/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pagos.Dao;

import com.pagos.Interface.InterfaceAbonos;
import com.pagos.Pojos.Abonos;
import java.util.Date;
import java.util.List;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 *
 * @author William Sanchez
 */
public class DaoAbonos  implements InterfaceAbonos{

    @Override
    public boolean registar(Session session, Abonos abonos) throws Exception {
    session.save(abonos);
    return true;
    }

    @Override
    public boolean actualizar(Session session, Abonos abonos) throws Exception {
    session.merge(abonos);
    return true;
    }

    @Override
    public boolean eliminar(Session session, Abonos abonos) throws Exception {
    session.delete(abonos);
    return true;
    }

    @Override
    public Abonos getById(Session session, int id) throws Exception {
        return (Abonos) session.get(Abonos.class, id);    }

    @Override
    public List<Abonos> getAll(Session session) throws Exception {
   String hql = "FROM Abonos where estado='Vigente' and saldofinal>0";
        Query query = session.createQuery(hql);
        return (List<Abonos>) query.list();  
    }

    @Override
    public Abonos getByUltimoRegistro(Session session) throws Exception {
  String hql="from Abonos order by idabonos desc";
        Query query=session.createQuery(hql).setMaxResults(1);
        
        return (Abonos) query.uniqueResult();   
    }

    @Override
    public List<Abonos> getAllFecha(Session session, Date fechaInicio, Date fechaFin) throws Exception {
 String hql = "from Abonos where fecharegistro between :fechaInicio and :fechaFin";
        Query query = session.createQuery(hql);
            query.setParameter("fechaInicio", fechaInicio);
            query.setParameter("fechaFin", fechaFin);
        return (List<Abonos>) query.list();  
    }

    @Override
    public List<Abonos> getAllByCliente(Session session, String numeroDocumento) throws Exception {
  String hql = "FROM Abonos a inner join fetch a.cliente ac where ac.numeroDocumentoC=:numeroDocumento";
        Query query = session.createQuery(hql);
            query.setParameter("numeroDocumento", numeroDocumento);
           
        return (List<Abonos>) query.list();  
    }
   @Override
    public List<Abonos> getAllAbonos(Session session) throws Exception {
   String hql = "FROM Abonos";
        Query query = session.createQuery(hql);
        return (List<Abonos>) query.list();  
    }
    
    
    
}
