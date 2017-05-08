/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pagos.bean;

import com.pagos.Dao.DaoCliente;
import com.pagos.Dao.DaoTipodocumento;
import com.pagos.Pojos.Cliente;
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
 * @author William Sanchez
 */
@ManagedBean
@ViewScoped
public class MbCliente {

    /**
     * Creates a new instance of MbCliente
     */
    private Session session;
    private Transaction transaccion;
    private Cliente cliente;
    private List<Cliente> listaCliente;
    private List<Cliente> listaClienteFiltrado;
    private int codigoCliente;
    private Cliente client1;

    public MbCliente() {
        this.cliente = new Cliente();
        this.client1 = new Cliente();
    }

    public void registrar() {
        this.session = null;
        this.transaccion = null;

        try {
            if (this.cliente.getNombre().equals("") || this.cliente.getNombre().length() < 3) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error:", "El campo nombres es muy corto"));
                return;
            }

            ValidacionTexto valida = new ValidacionTexto();

            if (!valida.validar(this.cliente.getNombre())) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error:", "El campo nombres no puede llevar numeros ni caracteres especiales"));

                return;
            }

            if (this.cliente.getNumeroDocumentoC().equals("") || this.cliente.getNumeroDocumentoC().length() < 1) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error:", "El numero de documento es obligatorio y debe contener minimo 1 caracteres"));
                return;
            }
            if (this.cliente.getTelefono().equals("")) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error:", "El numero de telefono es obligatorio y debe contener minimo 5 caracteres"));
                return;
            }

            DaoCliente daocliente = new DaoCliente();

            System.out.println("despuesde numerode documento" + this.client1.getNumeroDocumentoC());

            this.session = HibernateUtil.getSessionFactory().openSession();
            this.transaccion = session.beginTransaction();

            daocliente.registar(this.session, this.cliente);

            this.transaccion.commit();

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Correcto:", "El registro fue realizado correctamente"));

            this.cliente = new Cliente();
            this.cliente.setIdcliente(Integer.MIN_VALUE);

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

    public void actualizar() {
        this.session = null;
        this.transaccion = null;

        try {
            DaoCliente daoCliente = new DaoCliente();

            this.session = HibernateUtil.getSessionFactory().openSession();
            this.transaccion = session.beginTransaction();

            daoCliente.actualizar(this.session, this.cliente);

            this.transaccion.commit();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Correcto:", "Los cambios fueron guardados correctamente"));
        } catch (Exception ex) {
            if (this.transaccion != null) {
                this.transaccion.rollback();
            }

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error fatal:", "Por favor contacte con su administrador " + ex.getMessage() + "causa " + ex.getCause()));
        } finally {
            if (this.session != null) {
                this.session.close();
            }
        }
    }
        public void Eliminar(int codigoClie) {
        this.session = null;
        this.transaccion = null;

        try {
            DaoCliente daoCliente = new DaoCliente();

            this.session = HibernateUtil.getSessionFactory().openSession();
            this.transaccion = session.beginTransaction();
            this.client1= daoCliente.getById(this.session, codigoClie);

            daoCliente.eliminar(this.session, this.client1);

            this.transaccion.commit();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Correcto:", "Se ha Eliminado el cliente"));
        } catch (Exception ex) {
            if (this.transaccion != null) {
                this.transaccion.rollback();
            }

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error fatal:", "Por favor contacte con su administrador " + ex.getMessage() + "causa " + ex.getCause()));
        } finally {
            if (this.session != null) {
                this.session.close();
            }
        }
    }

    public void cargarClienteEditar(int codigoCliente) {
        this.session = null;
        this.transaccion = null;
        this.client1 = new Cliente();
        try {
            DaoCliente daocliente = new DaoCliente();

            this.session = HibernateUtil.getSessionFactory().openSession();
            this.transaccion = session.beginTransaction();

            this.cliente = daocliente.getById(this.session, codigoCliente);

            RequestContext.getCurrentInstance().update("frmEditarCliente:panelEditarCliente");
            RequestContext.getCurrentInstance().execute("PF('dialogoEditarCliente').show()");

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

    public List<Cliente> getAll() {
        this.session = null;
        this.transaccion = null;
        try {
            DaoCliente dao = new DaoCliente();
            this.session = HibernateUtil.getSessionFactory().openSession();
            this.transaccion = this.session.beginTransaction();

            this.listaCliente = dao.getAll(this.session);
            this.transaccion.commit();
            return this.listaCliente;
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

    public Cliente getById(int id) {
        this.session = null;
        this.transaccion = null;
        try {
            this.session = HibernateUtil.getSessionFactory().openSession();
            this.transaccion = this.session.beginTransaction();
            DaoCliente daoCliente = new DaoCliente();

            Cliente client = daoCliente.getById(this.session, id);
            this.transaccion.commit();
            return client;

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

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public List<Cliente> getListaCliente() {
        return listaCliente;
    }

    public void setListaCliente(List<Cliente> listaCliente) {
        this.listaCliente = listaCliente;
    }

    public List<Cliente> getListaClienteFiltrado() {
        return listaClienteFiltrado;
    }

    public void setListaClienteFiltrado(List<Cliente> listaClienteFiltrado) {
        this.listaClienteFiltrado = listaClienteFiltrado;
    }

    public int getCodigoCliente() {
        return codigoCliente;
    }

    public void setCodigoCliente(int codigoCliente) {
        this.codigoCliente = codigoCliente;
    }

    public Cliente getClient1() {
        return client1;
    }

    public void setClient1(Cliente client1) {
        this.client1 = client1;
    }

}
