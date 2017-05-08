/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pagos.Dao;


import com.pagos.Interface.InterfaceAbonoSecundario;
import com.pagos.Pojos.Abonosecundario;
import java.util.Date;
import java.util.List;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 *
 * @author ander
 */
public class DaoAbonoSecundario implements InterfaceAbonoSecundario{

    @Override
    public boolean registar(Session session, Abonosecundario abonosecundario) throws Exception {
   session.save(abonosecundario);
    return true;}

    @Override
    public boolean actualizar(Session session, Abonosecundario abonosecundario) throws Exception {
   session.update(abonosecundario);
    return true;}

    @Override
    public boolean eliminar(Session session, Abonosecundario abonosecundario) throws Exception {
 session.update(abonosecundario); 
    return true;
    }

    @Override
    public Abonosecundario getById(Session session, int id) throws Exception {
   return (Abonosecundario) session.get(Abonosecundario.class, id);
    }

    @Override
    public List<Abonosecundario> getAll(Session session) throws Exception {
 String hql="FROM Abonosecundario";
    Query query= session.createQuery(hql);
   return (List<Abonosecundario>) query.list();}

    @Override
    public List<Abonosecundario> getAllByIdAbonos(Session session, int id) throws Exception {
   String hql="FROM Abonosecundario a inner join fetch a.abonos ab where ab.idabonos=:id ";
   Query query= session.createQuery(hql);
            query.setParameter("id", id);   
   return (List<Abonosecundario>)query.list();
   
    }

    @Override
    public List<Abonosecundario> getAllByDate(Session session, Date fechaInicio, Date fechaFin) throws Exception {
String hql = "from Abonosecundario where fechaRegistro between :fechaInicio and :fechaFin";
        Query query = session.createQuery(hql);
            query.setParameter("fechaInicio", fechaInicio);
            query.setParameter("fechaFin", fechaFin);
        return (List<Abonosecundario>) query.list( );
    }

    @Override
    public List<Abonosecundario> getAllByAbonos(Session session, String codigo) throws Exception {
String hql = "FROM Abonosecundario a inner join fetch a.abonos al where al.idabonos=:codigo";
        Query query = session.createQuery(hql);
            query.setParameter("codigo", codigo);
           
        return (List<Abonosecundario>) query.list();  
    }

    
}
