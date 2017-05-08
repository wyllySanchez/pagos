/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pagos.Dao;


import com.pagos.Interface.InterfaceRol;
import com.pagos.Pojos.Rol;
import java.util.List;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 *
 * @author Sebastian
 */
public class DaoRol implements InterfaceRol {

    @Override
    public boolean registrar(Session session, Rol rol) throws Exception {
        session.save(rol);
        return true;
    }

    @Override
    public List<Rol> getAll(Session session) throws Exception {
        String hql = "FROM Rol";
        Query query = session.createQuery(hql);
        List<Rol> listaRol = (List<Rol>) query.list();
        return listaRol;
    }

    @Override
    public Rol getByCodigoEmpleado(Session session, int codigoEmpleado) throws Exception {
        return (Rol) session.get(Rol.class, codigoEmpleado);
    }

    @Override
    public Rol getByRol(Session session, int codigoRol) throws Exception {
        return (Rol) session.get(Rol.class, codigoRol);
    }

    @Override
    public boolean update(Session session, Rol rol) throws Exception {
        session.update(rol);
        return true;
    }

    @Override
    public boolean delete(Session session, Rol rol) throws Exception {
        session.delete(rol);
        return true;
    }

    @Override
    public Rol getByDocumento(Session session, String numeroDocumento) throws Exception {
        return (Rol) session.get(Rol.class, numeroDocumento);
    }

    @Override
    public Rol getById(Session session, int id) throws Exception {
  return (Rol) session.get(Rol.class, id);    }


}
