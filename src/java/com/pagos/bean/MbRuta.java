/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pagos.bean;

import com.pagos.Dao.DaoRol;
import com.pagos.Dao.DaoRuta;
import com.pagos.Pojos.Ruta;
import com.pagos.clase.ValidacionTexto;
import com.pagos.util.HibernateUtil;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.primefaces.context.RequestContext;

/**
 *
 * @author williamSanchez
 */
@ManagedBean
@ViewScoped
public class MbRuta {

    /**
     * Creates a new instance of MbRuta
     */
    private Session session;
    private Transaction transaccion;

    private Ruta ruta;
    private List<Ruta> listaRuta;
    private List<Ruta> listaRutaFiltrado;
    private int codigoRuta;
    private String nombreRuta;

    public MbRuta() {
    }

    public void registrar() {
        this.session = null;
        this.transaccion = null;

        try {

            if (this.ruta.getNonbre().equals("") || this.ruta.getNonbre().length() < 4) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error:", "El campo nombres es muy corto"));
                return;
            }

            ValidacionTexto valida = new ValidacionTexto();

            if (!valida.validar(this.ruta.getNonbre())) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error:", "El campo nombres no puede llevar numeros ni caracteres especiales"));

                return;
            }

            DaoRuta daoRuta = new DaoRuta();

            this.session = HibernateUtil.getSessionFactory().openSession();
            this.transaccion = session.beginTransaction();

            daoRuta.registrar(this.session, this.ruta);

            this.transaccion.commit();

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Correcto:", "El registro fue realizado correctamente"));

            this.ruta = new Ruta();
            this.nombreRuta = "";

        } catch (Exception ex) {
            if (this.transaccion != null) {
                this.transaccion.rollback();
            }

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error fatal:", "Por favor contacte con su administrador " + ex.getMessage()));
        } finally {
            if (this.session != null) {
                this.session.close();
            }
        }

    }

    public void update() {
        this.session = null;
        this.transaccion = null;

        try {
            DaoRuta daoRuta = new DaoRuta();

            this.session = HibernateUtil.getSessionFactory().openSession();
            this.transaccion = session.beginTransaction();

            if (this.ruta.getNonbre().equals("")) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error:", "El campo nombres es obligatorio"));
                return;
            }

            daoRuta.update(this.session, this.ruta);

            this.transaccion.commit();

//            HttpSession httpSession = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
//            httpSession.setAttribute("nombreRol", this.rol.getNombreRol());
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Correcto:", "Los cambios fueron guardados correctamente"));
        } catch (Exception ex) {
            if (this.transaccion != null) {
                this.transaccion.rollback();
            }

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error fatal:", "Por favor contacte con su administrador " + ex.getMessage()));
        } finally {
            if (this.session != null) {
                this.session.close();
            }
        }
    }

    public void cargarRutaEditar(int codigoRuta) {
        this.session = null;
        this.transaccion = null;

        try {
            DaoRuta daoRuta = new DaoRuta();

            this.session = HibernateUtil.getSessionFactory().openSession();
            this.transaccion = session.beginTransaction();

            this.ruta = daoRuta.getById(this.session, codigoRuta);

            RequestContext.getCurrentInstance().update("frmEditarRuta:panelEditarRuta");
            RequestContext.getCurrentInstance().execute("PF('dialogoEditarRuta').show()");

            this.transaccion.commit();
        } catch (Exception ex) {
            if (this.transaccion != null) {
                this.transaccion.rollback();
            }

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error fatal:", "Por favor contacte con su administrador " + ex.getMessage()));
        } finally {
            if (this.session != null) {
                this.session.close();
            }
        }
    }

    public List<Ruta> getAll() {
        this.session = null;
        this.transaccion = null;
        try {
            DaoRuta daoRuta = new DaoRuta();
            this.session = HibernateUtil.getSessionFactory().openSession();
            this.transaccion = this.session.beginTransaction();

            this.listaRuta = daoRuta.getAll(this.session);
            this.transaccion.commit();
            return this.listaRuta;
        } catch (Exception ex) {
            if (this.transaccion != null) {
                this.transaccion.rollback();
            }

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error fatal:", "Por favor intente mas tarde " + ex.getMessage()));

            return null;
        } finally {
            if (this.session != null) {
                this.session.close();
            }
        }
    }

    public Ruta getById(int id) {
        this.session = null;
        this.transaccion = null;
        try {
            this.session = HibernateUtil.getSessionFactory().openSession();
            this.transaccion = this.session.beginTransaction();

            DaoRuta daoRuta = new DaoRuta();

            Ruta rut = daoRuta.getById(this.session, id);
            this.transaccion.commit();
            return rut;

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
}
