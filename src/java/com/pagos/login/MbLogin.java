/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pagos.login;

import com.pagos.Dao.DaoEmpleado;
import com.pagos.Pojos.Empleado;
import com.pagos.clase.Encrypt;
import com.pagos.util.HibernateUtil;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 *
 * @author William Sanchez
 */
@ManagedBean
@SessionScoped
public class MbLogin {

    private String correoElectronico;
    private String password;
    private Session session;
    private Transaction transaccion;
    private boolean sessionIniciada;
    private String mensaje;

    /**
     * Creates a new instance of MbLogin
     */
    public MbLogin() {
        HttpSession miSession = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
        miSession.setMaxInactiveInterval(5000);
        this.mensaje="";
    }

    public String login() {

        this.session = null;
        this.transaccion = null;
        this.mensaje="";
        try {
            DaoEmpleado daoempleado = new DaoEmpleado();

            this.session = HibernateUtil.getSessionFactory().openSession();
            this.transaccion = this.session.beginTransaction();

            Empleado empleado = daoempleado.getByCorreoElectronico(this.session, this.correoElectronico);

                if (empleado != null) {
                if (empleado.getRol().getIdrol() == 1) {
                    if (empleado.getPassword().equals(Encrypt.sha512(this.password))) {
                        HttpSession httpSession = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
                        httpSession.setAttribute("correoElectronico", this.correoElectronico);
                        this.sessionIniciada = true;

                        return "/administrador/abonos/registroAbono.xhtml";
                     }
                } else {
                    if (empleado.getRol().getIdrol() == 2) {
                         if (empleado.getPassword().equals(Encrypt.sha512(this.password))) {
                        HttpSession httpSession = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
                        httpSession.setAttribute("correoElectronico", this.correoElectronico);
//                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Correcto:", "Se ha iniciado session."));
                        this.sessionIniciada = true;

                        return "/administrador/abonos/listaAbonos.xhtml";

                    }

                }
            }
                    
                }
            this.transaccion.commit();

            this.correoElectronico = null;
            this.password = null;

           this.mensaje="Usuario o Contraseña Incorrectos";
            return "/index";

        } catch (Exception ex) {
            if (this.transaccion != null) {
                this.transaccion.rollback();
            }

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error fatal:", "Por favor contacte con su administrador " + ex.getMessage()));

            return null;
        } finally {
            if (this.session != null) {
                this.session.close();
            }
        }
    }
    

    public String cerrarSesion() {
        this.correoElectronico = null;
        this.password = null;
        this.sessionIniciada = false;

        HttpSession httpSession = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
        httpSession.invalidate();

        return "/index";
    }

    public String getCorreoElectronico() {
        return correoElectronico;
    }

    public void setCorreoElectronico(String correoElectronico) {
        this.correoElectronico = correoElectronico;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    

    public boolean isSessionIniciada() {
        return sessionIniciada;
    }

    public void setSessionIniciada(boolean sessionIniciada) {
        this.sessionIniciada = sessionIniciada;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }
    
    
    
}

