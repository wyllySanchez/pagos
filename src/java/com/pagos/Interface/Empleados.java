/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pagos.Interface;

import com.pagos.Pojos.Empleado;
import java.util.List;
import org.hibernate.Session;

/**
 *
 * @author ander
 */
public interface Empleados {

    public boolean registar(Session session, Empleado empleado) throws Exception;

    public boolean actualizar(Session session, Empleado empleado) throws Exception;

    public boolean eliminar(Session session, Empleado empleado) throws Exception;

    public Empleado getByCorreoElectronico(Session session, String correoElectronico) throws Exception;

    public Empleado getById(Session session, int id) throws Exception;

    public List<Empleado> getAll(Session session) throws Exception;

    public Empleado getByNumeroDocumento(Session session, String numeroDocumento) throws Exception;

    public Empleado getAllByIdSueldo(Session session, int id) throws Exception;

}
