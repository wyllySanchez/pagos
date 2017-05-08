/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pagos.Dao;


import com.pagos.Interface.InterfaceTipodocumento;
import com.pagos.Pojos.Tipodocumento;
import java.util.List;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 *
 * @author Sebastian
 */
public class DaoTipodocumento implements InterfaceTipodocumento{

    @Override
    public boolean registrar(Session session, Tipodocumento tipodocumento) throws Exception {
   session.save(tipodocumento);
        return true;   
    }

    @Override
    public List<Tipodocumento> getAll(Session session) throws Exception {
        String hql = "from Tipodocumento";
        Query query = session.createQuery(hql);
        List<Tipodocumento> listaTipodocumento = (List<Tipodocumento>) query.list();
        return listaTipodocumento; 
    }

   

    @Override
    public Tipodocumento getByCodigoTipodocumeno(Session session, int codigoTipodocumento) throws Exception {
       return (Tipodocumento) session.get(Tipodocumento.class, codigoTipodocumento); 
    }

    @Override
    public boolean update(Session session, Tipodocumento tipodocumento) throws Exception {
   session.update(tipodocumento);
        return true;  
    }

 
    
}
