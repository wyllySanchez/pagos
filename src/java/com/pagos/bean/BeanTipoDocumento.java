/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pagos.bean;

import com.pagos.Dao.DaoTipodocumento;
import com.pagos.Pojos.Tipodocumento;
import com.pagos.clase.ValidacionTexto;
import com.pagos.util.HibernateUtil;
import java.util.ArrayList;
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
 * @author William sanchez
 */
@ManagedBean
@ViewScoped
public class BeanTipoDocumento {

    /**
     * Creates a new instance of BeanTipoDocumento
     */
    private Session session;
    private Transaction transaccion;
    private Tipodocumento tipoDocumento;
    private List<Tipodocumento> listaTipodocumento;
    private List<Tipodocumento> listaTipodocumentoFiltrado;
    private String nombreTipoDocumento;
  

    public BeanTipoDocumento() {
        this.tipoDocumento = new Tipodocumento();
        this.listaTipodocumento = new ArrayList<>();
    }

    public void registrar() {
        this.session = null;
        this.transaccion = null;

        try {

            if (this.tipoDocumento.getNombretd().equals("") || this.tipoDocumento.getNombretd().length() < 4) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error:", "El campo nombres es muy corto"));
                return;
            }

            ValidacionTexto valida = new ValidacionTexto();

            if (!valida.validar(this.tipoDocumento.getNombretd())) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error:", "El campo nombres no puede llevar numeros ni caracteres especiales"));

                return;
            }

            DaoTipodocumento daoTipodocumento = new DaoTipodocumento();

            this.session = HibernateUtil.getSessionFactory().openSession();
            this.transaccion = session.beginTransaction();

//            if (daoRol.getByRol(this.session, this.codigoRol) != null) {
//                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error:", "El tipoDocumento ya se encuentra registrado en el sistema"));
//
//                return "/administrador/tipoDocumentoes/registrarRol";
//            }
            daoTipodocumento.registrar(this.session, this.tipoDocumento);

            this.transaccion.commit();

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Correcto:", "El registro fue realizado correctamente"));

            this.tipoDocumento = new Tipodocumento();
            this.listaTipodocumento = new ArrayList<>();

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
            DaoTipodocumento daoTipodocumento = new DaoTipodocumento();

            this.session = HibernateUtil.getSessionFactory().openSession();
            this.transaccion = session.beginTransaction();

            if (this.tipoDocumento.getNombretd().equals("")) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error:", "El campo nombres es obligatorio"));
                return;
            }

            daoTipodocumento.update(this.session, this.tipoDocumento);

            this.transaccion.commit();

//            HttpSession httpSession = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
//            httpSession.setAttribute("nombreRol", this.tipoDocumento.getNombreRol());
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

    public void cargarRolEditar(int codigoRol) {
        this.session = null;
        this.transaccion = null;

        try {
            DaoTipodocumento daoTipodocumento = new DaoTipodocumento();

            this.session = HibernateUtil.getSessionFactory().openSession();
            this.transaccion = session.beginTransaction();

            this.tipoDocumento = daoTipodocumento.getByCodigoTipodocumeno(this.session, codigoRol);

            RequestContext.getCurrentInstance().update("frmEditarTipoDocumento:panelEditarTipoDocumento");
            RequestContext.getCurrentInstance().execute("PF('dialogoEditarTipoDocumento').show()");

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

    public List<Tipodocumento> getAll() {
        this.session = null;
        this.transaccion = null;
        try {
            DaoTipodocumento daoTipodocumento = new DaoTipodocumento();
            this.session = HibernateUtil.getSessionFactory().openSession();
            this.transaccion = this.session.beginTransaction();

            this.listaTipodocumento = daoTipodocumento.getAll(this.session);
            this.transaccion.commit();
            return this.listaTipodocumento;
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

    public Tipodocumento getById(int id) {
        this.session = null;
        this.transaccion = null;
        try {
            this.session = HibernateUtil.getSessionFactory().openSession();
            this.transaccion = this.session.beginTransaction();

            DaoTipodocumento daoTipodocumento = new DaoTipodocumento();

            Tipodocumento tipoDocumentoe = daoTipodocumento.getByCodigoTipodocumeno(this.session, id);
            this.transaccion.commit();
            return tipoDocumentoe;

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

    public Tipodocumento getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(Tipodocumento tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    public List<Tipodocumento> getListaTipodocumento() {
        return listaTipodocumento;
    }

    public void setListaTipodocumento(List<Tipodocumento> listaTipodocumento) {
        this.listaTipodocumento = listaTipodocumento;
    }

    public List<Tipodocumento> getListaTipodocumentoFiltrado() {
        return listaTipodocumentoFiltrado;
    }

    public void setListaTipodocumentoFiltrado(List<Tipodocumento> listaTipodocumentoFiltrado) {
        this.listaTipodocumentoFiltrado = listaTipodocumentoFiltrado;
    }


    public String getNombreTipoDocumento() {
        return nombreTipoDocumento;
    }

    public void setNombreTipoDocumento(String nombreTipoDocumento) {
        this.nombreTipoDocumento = nombreTipoDocumento;
    }

 

  

}
