/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pagos.bean;

import com.pagos.Dao.DaoEmpleado;
import com.pagos.Dao.DaoRol;
import com.pagos.Dao.DaoTipodocumento;
import com.pagos.Pojos.Empleado;
import com.pagos.clase.Encrypt;
import com.pagos.util.HibernateUtil;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.primefaces.context.RequestContext;
import org.primefaces.model.UploadedFile;

/**
 *
 * @author William Sanchez
 */
@ManagedBean
@ViewScoped
public class BeanEmpleado {

    /**
     * Creates a new instance of BeanEmpleado
     */
    private Session session;
    private Transaction transaccion;
    private Empleado empleado;
    private List<Empleado> listaEmpleado;
    private List<Empleado> listaEmpleadoFiltrado;
    private String ContraseniaRepita;
    private String numerodocumento;
    private String correoElectronico;
    private int CodigoRol;
    private int CodigotipoDocumento;
    private Empleado empleadoSelect;
    private UploadedFile avatar;

    public BeanEmpleado() {
        this.empleadoSelect = new Empleado();
        this.empleadoSelect.setIdempleado(Integer.MIN_VALUE);
        this.empleado = new Empleado();
        this.empleado.setIdempleado(Integer.MIN_VALUE);
        this.empleado.setNombre("");
        this.empleado.setApellidos("");
        this.empleado.setCorreoElectronico("");
        this.empleado.setNumeroDocumento("");
        this.CodigoRol = 0;
        this.CodigotipoDocumento = 0;
    }

    public void register() {

        this.session = null;
        this.transaccion = null;

        try {
            if (this.empleadoSelect.getNombre().equals("") || this.empleadoSelect.getNombre().length() < 4) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error:", "El campo nombres es muy corto"));
                return;
            }
            if (this.empleadoSelect.getApellidos().equals("") || this.empleadoSelect.getApellidos().length() < 4) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error:", "El campo nombres es muy corto"));
                return;
            }
            if (this.empleadoSelect.getNumeroDocumento().equals("") || this.empleadoSelect.getNombre().length() < 6) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error:", "El campo nombres es muy corto"));
                return;
            }
            if (this.empleadoSelect.getPassword().equals("") || this.empleadoSelect.getPassword().length() < 3) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error:", "La comtraseña es muy corta"));
                return;
            }
            if (!this.empleadoSelect.getPassword().equals(this.ContraseniaRepita)) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error:", "El campo contraseña es obligatorio"));
                return;
            }
            if (this.empleadoSelect.getNumeroDocumento().equals("") || this.empleadoSelect.getNombre().length() < 6) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error:", "El campo nombres es muy corto"));
                return;
            }
            if (this.empleado.getNumeroDocumento().equals("") || this.empleadoSelect.getNombre().length() < 6) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error:", "El campo nombres es muy corto"));
                return;
            }
            if (this.CodigoRol == 0) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error:", "El por favor escoja un Rol."));
                return;
            }
            if (this.CodigotipoDocumento == 0) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error:", "El por favor escoja un Tipo de Documento."));
                return;
            }

            DaoEmpleado daoEmpleado = new DaoEmpleado();

            this.session = HibernateUtil.getSessionFactory().openSession();
            this.transaccion = session.beginTransaction();

            if (daoEmpleado.getByCorreoElectronico(this.session, this.empleadoSelect.getCorreoElectronico()) != null) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error:", "El usuario ya se encuentra registrado en el sistema"));

                return;
            }
            if (daoEmpleado.getByNumeroDocumento(this.session, this.empleadoSelect.getNumeroDocumento()) != null) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error:", "El Empleado con ese numero ya se encuentra registrado en el sistema"));

                return;
            }
            DaoRol daoRol = new DaoRol();
            DaoTipodocumento daoTipodocumento = new DaoTipodocumento();

            this.empleadoSelect.setRol(daoRol.getByRol(this.session, this.CodigoRol));
            this.empleadoSelect.setTipodocumento(daoTipodocumento.getByCodigoTipodocumeno(this.session, this.CodigotipoDocumento));
            this.empleadoSelect.setPassword(Encrypt.sha512(this.empleado.getPassword()));
            daoEmpleado.registar(this.session, this.empleadoSelect);
            this.empleadoSelect = new Empleado();
           
            this.transaccion.commit();

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Correcto:", "El registro fue realizado correctamente"));
            this.empleadoSelect = new Empleado();
            this.empleadoSelect.setIdempleado(Integer.MIN_VALUE);
            this.empleadoSelect.setNombre("");
            this.empleadoSelect.setApellidos("");
            this.empleadoSelect.setCorreoElectronico("");
            this.empleadoSelect.setNumeroDocumento("");
            this.CodigoRol = 0;
            this.CodigotipoDocumento = 0;

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

    public Empleado getById(int id) {
        this.session = null;
        this.transaccion = null;
        try {
            this.session = HibernateUtil.getSessionFactory().openSession();
            this.transaccion = this.session.beginTransaction();
            DaoEmpleado daoEmpleado = new DaoEmpleado();

            Empleado emplead = daoEmpleado.getById(this.session, id);
            this.transaccion.commit();
            return emplead;

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

    public List<Empleado> getAll() {
        this.session = null;
        this.transaccion = null;

        try {
            DaoEmpleado daoEmpleado = new DaoEmpleado();

            this.session = HibernateUtil.getSessionFactory().openSession();
            this.transaccion = this.session.beginTransaction();

            this.listaEmpleado = daoEmpleado.getAll(this.session);

            this.transaccion.commit();

            return this.listaEmpleado;
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

    public void actualizar() {
        this.session = null;
        this.transaccion = null;

        try {
            DaoEmpleado daoEmpleado = new DaoEmpleado();

            this.session = HibernateUtil.getSessionFactory().openSession();
            this.transaccion = session.beginTransaction();

            if (this.empleadoSelect.getNombre().equals("")) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error:", "El campo nombres es obligatorio"));
                return;
            }

            if (this.empleadoSelect.getApellidos().equals("")) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error:", "El campo nombres es obligatorio"));
                return;
            }

            daoEmpleado.actualizar(this.session, this.empleadoSelect);

            this.transaccion.commit();

            HttpSession httpSession = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
            httpSession.setAttribute("correoElectronico", this.empleado.getCorreoElectronico());

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

    public void cargarEmpleadoEditar(int codigoEmpleado) {
        this.session = null;
        this.transaccion = null;

        try {
            DaoEmpleado daoEmpleado = new DaoEmpleado();

            this.session = HibernateUtil.getSessionFactory().openSession();
            this.transaccion = session.beginTransaction();

            this.empleadoSelect = daoEmpleado.getById(this.session, codigoEmpleado);
//            this.transaccion.commit();
            RequestContext.getCurrentInstance().update("frmEditarEmpleado:panelEditarEmpleado");
            RequestContext.getCurrentInstance().execute("PF('dialogoEditarEmpleado').show()");

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

    public Empleado getByCorreoElectronico() {
        this.session = null;
        this.transaccion = null;

        try {
            DaoEmpleado daoEmpleado = new DaoEmpleado();

            this.session = HibernateUtil.getSessionFactory().openSession();
            this.transaccion = this.session.beginTransaction();

            HttpSession sessionEmpleado = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);

            this.empleado = daoEmpleado.getByCorreoElectronico(this.session, sessionEmpleado.getAttribute("correoElectronico").toString());

            this.transaccion.commit();

            return this.empleado;
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

    public Empleado getEmpleado() {
        return empleado;
    }

    public void setEmpleado(Empleado empleado) {
        this.empleado = empleado;
    }

    public List<Empleado> getListaEmpleado() {
        return listaEmpleado;
    }

    public void setListaEmpleado(List<Empleado> listaEmpleado) {
        this.listaEmpleado = listaEmpleado;
    }

    public List<Empleado> getListaEmpleadoFiltrado() {
        return listaEmpleadoFiltrado;
    }

    public void setListaEmpleadoFiltrado(List<Empleado> listaEmpleadoFiltrado) {
        this.listaEmpleadoFiltrado = listaEmpleadoFiltrado;
    }

    public String getContraseniaRepita() {
        return ContraseniaRepita;
    }

    public void setContraseniaRepita(String ContraseniaRepita) {
        this.ContraseniaRepita = ContraseniaRepita;
    }

    public String getNumerodocumento() {
        return numerodocumento;
    }

    public void setNumerodocumento(String numerodocumento) {
        this.numerodocumento = numerodocumento;
    }

    public String getCorreoElectronico() {
        return correoElectronico;
    }

    public void setCorreoElectronico(String correoElectronico) {
        this.correoElectronico = correoElectronico;
    }

    public int getCodigoRol() {
        return CodigoRol;
    }

    public void setCodigoRol(int CodigoRol) {
        this.CodigoRol = CodigoRol;
    }

    public int getCodigotipoDocumento() {
        return CodigotipoDocumento;
    }

    public void setCodigotipoDocumento(int CodigotipoDocumento) {
        this.CodigotipoDocumento = CodigotipoDocumento;
    }

    public Empleado getEmpleadoSelect() {
        return empleadoSelect;
    }

    public void setEmpleadoSelect(Empleado empleadoSelect) {
        this.empleadoSelect = empleadoSelect;
    }

    public UploadedFile getAvatar() {
        return avatar;
    }

    public void setAvatar(UploadedFile avatar) {
        this.avatar = avatar;
    }

}
