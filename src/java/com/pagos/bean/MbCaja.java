/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pagos.bean;

import com.pagos.Dao.DaoCaja;
import com.pagos.Dao.DaoEmpleado;
import com.pagos.Pojos.Caja;
import com.pagos.util.HibernateUtil;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.primefaces.context.RequestContext;

/**
 *
 * @author williamSanchez
 */
@ManagedBean
@ViewScoped
public class MbCaja {

    /**
     * Creates a new instance of MbCaja
     */
    private Session session;
    private Transaction transaccion;
    private Caja caja;
    private List<Caja> listaCaja;
    private List<Caja> listaCajaFiltrado;
    private List<Caja> listaCajaByFecha;
    private int codigoCaja;
    private Caja caja1;
    private int idCaja;
    private long totalVentasFecha;
    private Date fechaFin;
    private Date fechaInicio;
    private int codigoEmpleado;

    public MbCaja() {
        this.listaCaja = new ArrayList<>();
        this.caja = new Caja();
        this.listaCajaByFecha = new ArrayList<>();
        this.caja.setIdcaja(Integer.MIN_VALUE);

    }

    public void registrar() {
        this.session = null;
        this.transaccion = null;
        HttpSession httpSession = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
        String correoEmple = httpSession.getAttribute("correoElectronico").toString();
        try {
            DaoEmpleado daoEmpleado = new DaoEmpleado();

            DaoCaja daoCaja = new DaoCaja();
            this.session = HibernateUtil.getSessionFactory().openSession();
            this.transaccion = session.beginTransaction();
            Caja caj = new Caja();
            System.out.println(".........1");
            caj = daoCaja.getByUltimoRegistro(this.session);
            int idBdFactura = 0;
            if (caj != null) {
                idBdFactura = caj.getIdcaja();
            }
            System.out.println(".........2");
            this.caja.setIdcaja(idBdFactura + 1);
            this.caja.setEmpleado(daoEmpleado.getByCorreoElectronico(this.session, correoEmple));
            System.out.println(".........3");
            this.caja.setFecha(new Date());
            daoCaja.registar(this.session, this.caja);

            this.transaccion.commit();

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Correcto:", "El registro fue realizado correctamente"));

            this.caja = new Caja();
            this.caja.setIdcaja(Integer.MIN_VALUE);
            this.caja.setValor(0);

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
            DaoCaja daoCaja = new DaoCaja();

            this.session = HibernateUtil.getSessionFactory().openSession();
            this.transaccion = session.beginTransaction();

            daoCaja.actualizar(this.session, this.caja1);

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

    public void cargarCajaEditar(int codigoCaja) {
        this.session = null;
        this.transaccion = null;
        this.caja1 = new Caja();
        try {
            DaoCaja daoCaja = new DaoCaja();

            this.session = HibernateUtil.getSessionFactory().openSession();
            this.transaccion = session.beginTransaction();

            this.caja1 = daoCaja.getById(this.session, codigoCaja);

            RequestContext.getCurrentInstance().update("frmEditarCaja:panelEditarCaja");
            RequestContext.getCurrentInstance().execute("PF('dialogoEditarCaja').show()");

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

    public List<Caja> getAll() {
        this.session = null;
        this.transaccion = null;
        try {
            DaoCaja daoCaja = new DaoCaja();
            this.session = HibernateUtil.getSessionFactory().openSession();
            this.transaccion = this.session.beginTransaction();

            this.listaCaja = daoCaja.getAll(this.session);
            this.transaccion.commit();
            return this.listaCaja;
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

    public Caja getById(int id) {
        this.session = null;
        this.transaccion = null;
        try {
            this.session = HibernateUtil.getSessionFactory().openSession();
            this.transaccion = this.session.beginTransaction();
            DaoCaja daoCaja = new DaoCaja();

            Caja gast = daoCaja.getById(this.session, id);
            this.transaccion.commit();
            return gast;

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

    public List<Caja> getListVentasByFecha() {

        if (listaCajaByFecha == null) {
            listaCajaByFecha = new ArrayList<>();
        }
        return listaCajaByFecha;
    }

    public void consultarCajas() {

        this.session = null;
        this.transaccion = null;

        try {

            this.session = HibernateUtil.getSessionFactory().openSession();
            this.transaccion = this.session.beginTransaction();
            DaoCaja daoCaja = new DaoCaja();
            this.listaCajaByFecha = daoCaja.getAllFecha(this.session, this.fechaInicio, this.fechaFin);
            totalVentasFecha = 0;

            for (Caja gasto : listaCajaByFecha) {
                totalVentasFecha = totalVentasFecha + (gasto.getValor());
            }
            this.transaccion.commit();
        } catch (Exception ex) {
            if (this.transaccion != null) {
                this.transaccion.rollback();
            }
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error fatal:", "Por favor intente mas tarde " + ex.getMessage()));
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
            DaoCaja daoCaja = new DaoCaja();

            this.session = HibernateUtil.getSessionFactory().openSession();
            this.transaccion = session.beginTransaction();
            this.caja1 = daoCaja.getById(this.session, codigoClie);

            daoCaja.eliminar(this.session, this.caja1);

            this.transaccion.commit();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Correcto:", "Se ha Eliminado el Registro de dinero"));
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

    public void selectFactura(int id) {
        this.idCaja = id;
    }

    public Caja getCaja() {
        return caja;
    }

    public void setCaja(Caja caja) {
        this.caja = caja;
    }

    public List<Caja> getListaCaja() {
        return listaCaja;
    }

    public void setListaCaja(List<Caja> listaCaja) {
        this.listaCaja = listaCaja;
    }

    public List<Caja> getListaCajaFiltrado() {
        return listaCajaFiltrado;
    }

    public void setListaCajaFiltrado(List<Caja> listaCajaFiltrado) {
        this.listaCajaFiltrado = listaCajaFiltrado;
    }

    public List<Caja> getListaCajaByFecha() {
        return listaCajaByFecha;
    }

    public void setListaCajaByFecha(List<Caja> listaCajaByFecha) {
        this.listaCajaByFecha = listaCajaByFecha;
    }

    public int getCodigoCaja() {
        return codigoCaja;
    }

    public void setCodigoCaja(int codigoCaja) {
        this.codigoCaja = codigoCaja;
    }

    public Caja getCaja1() {
        return caja1;
    }

    public void setCaja1(Caja caja1) {
        this.caja1 = caja1;
    }

    public int getIdCaja() {
        return idCaja;
    }

    public void setIdCaja(int idCaja) {
        this.idCaja = idCaja;
    }

    public long getTotalVentasFecha() {
        return totalVentasFecha;
    }

    public void setTotalVentasFecha(long totalVentasFecha) {
        this.totalVentasFecha = totalVentasFecha;
    }

    public Date getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(Date fechaFin) {
        this.fechaFin = fechaFin;
    }

    public Date getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(Date fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public int getCodigoEmpleado() {
        return codigoEmpleado;
    }

    public void setCodigoEmpleado(int codigoEmpleado) {
        this.codigoEmpleado = codigoEmpleado;
    }

}
