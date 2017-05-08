/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pagos.Dao;


import com.pagos.Interface.Empleados;
import com.pagos.Pojos.Empleado;
import java.util.List;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 *
 * @author ander
 */
public class DaoEmpleado implements Empleados {

    @Override
    public boolean registar(Session session, Empleado empleado) throws Exception {
        session.save(empleado);
        return true;
    }

    @Override
    public boolean actualizar(Session session, Empleado empleado) throws Exception {
        session.update(empleado);
        return true;
    }

    @Override
    public boolean eliminar(Session session, Empleado empleado) throws Exception {
        session.delete(empleado);
        return true;
    }

    @Override
    public Empleado getById(Session session, int id) throws Exception {
        return (Empleado) session.get(Empleado.class, id);
    }

    @Override
    public List<Empleado> getAll(Session session) throws Exception {
        String hql = "FROM Empleado";
        Query query = session.createQuery(hql);
        return (List<Empleado>) query.list();
    }

    @Override
    public Empleado getByCorreoElectronico(Session session, String correoElectronico) throws Exception {
      String hql = "from Empleado e inner join fetch e.rol  where e.correoElectronico=:correoElectronico";
        Query query = session.createQuery(hql);
        query.setParameter("correoElectronico", correoElectronico);

        Empleado empleado = (Empleado) query.uniqueResult();

        return empleado; 
    }

    @Override
    public Empleado getByNumeroDocumento(Session session, String numeroDocumento) throws Exception {
 String hql = "from Empleado e inner join fetch e.rol  where e.numeroDocumento=:numeroDocumento";
        Query query = session.createQuery(hql);
        query.setParameter("numeroDocumento", numeroDocumento);

        Empleado empleado = (Empleado) query.uniqueResult();

        return empleado;  
    }

    @Override
    public Empleado getAllByIdSueldo(Session session, int id) throws Exception {
 String hql="FROM Empleado a inner join fetch a.abonos ab where ab.idabonos=:id ";
   Query query= session.createQuery(hql);
            query.setParameter("id", id);   
    Empleado empleado = (Empleado) query.uniqueResult();

        return empleado; 
    }



//    public Empleado getByCorreoElectronico(Session session, String correo) throws Exception {
// String hql = "from Empleado e inner join fetch e.rol  where e.correo=:correo";
//        Query query = session.createQuery(hql);
//        query.setParameter("correo", correo);
//
//        Empleado empleado = (Empleado) query.uniqueResult();
//
//        return empleado;     }
//  

}