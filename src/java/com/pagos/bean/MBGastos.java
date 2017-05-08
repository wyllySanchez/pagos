/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pagos.bean;

import com.pagos.Dao.DaoEmpleado;
import com.pagos.Dao.DaoGasto;
import com.pagos.Pojos.Gasto;
import com.pagos.clase.ValidacionTexto;
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
public class MBGastos {

    /**
     * Creates a new instance of MBGastos
     */
    private Session session;
    private Transaction transaccion;
    private Gasto gasto;
    private List<Gasto> listaGasto;
    private List<Gasto> listaGastoFiltrado;
    private List<Gasto> listaGastoByFecha;
    private int codigoGasto;
    private Gasto gasto1;
    private int idGasto;
    private long totalVentasFecha;
    private Date fechaFin;
    private Date fechaInicio;
    private int codigoEmpleado;

    public MBGastos() {
        this.listaGasto = new ArrayList<>();
        this.gasto = new Gasto();
         this.listaGastoByFecha = new ArrayList<>();
    }

    public void registrar() {
        this.session = null;
        this.transaccion = null;
     
        try {
            DaoEmpleado daoEmpleado = new DaoEmpleado();
            if (this.gasto.getNombre().equals("") || this.gasto.getNombre().length() < 3) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error:", "El campo nombres es muy corto"));
                return;
            }

            ValidacionTexto valida = new ValidacionTexto();

            if (!valida.validar(this.gasto.getNombre())) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error:", "El campo nombres no puede llevar numeros ni caracteres especiales"));

                return;
            }

            if (this.gasto.getImporte().equals("") || this.gasto.getImporte().intValue() < 2) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error:", "El numero de documento es obligatorio y debe contener minimo 5 caracteres"));
                return;
            }

            DaoGasto daoGasto = new DaoGasto();
            this.session = HibernateUtil.getSessionFactory().openSession();
            this.transaccion = session.beginTransaction();
            Gasto gast = new Gasto();
            gast = daoGasto.getByUltimoRegistro(this.session);
            int idBdFactura = 0;
            if (gast != null) {
                idBdFactura = gast.getIdgasto();
            }
            this.gasto.setIdgasto(idBdFactura + 1);
            this.gasto.setEmpleado(daoEmpleado.getById(this.session, this.codigoEmpleado));
            System.out.println(".........4");
            this.gasto.setFecha(new Date());
            daoGasto.registar(this.session, this.gasto);

            this.transaccion.commit();

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Correcto:", "El registro fue realizado correctamente"));

            this.gasto = new Gasto();
            this.gasto.setIdgasto(Integer.MIN_VALUE);
            this.gasto.setImporte(0);
            this.gasto.setNombre("");
            this.gasto.setValor(0);

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
            DaoGasto daoGasto = new DaoGasto();

            this.session = HibernateUtil.getSessionFactory().openSession();
            this.transaccion = session.beginTransaction();

            daoGasto.actualizar(this.session, this.gasto1);

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

    public void cargarClienteEditar(int codigoCliente) {
        this.session = null;
        this.transaccion = null;
        this.gasto1 = new Gasto();
        try {
            DaoGasto daoGasto = new DaoGasto();

            this.session = HibernateUtil.getSessionFactory().openSession();
            this.transaccion = session.beginTransaction();

            this.gasto1 = daoGasto.getById(this.session, codigoGasto);

            RequestContext.getCurrentInstance().update("frmEditarGasto:panelEditarGasto");
            RequestContext.getCurrentInstance().execute("PF('dialogoEditarGasto').show()");

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

    public List<Gasto> getAll() {
        this.session = null;
        this.transaccion = null;
        try {
            DaoGasto daoGasto = new DaoGasto();
            this.session = HibernateUtil.getSessionFactory().openSession();
            this.transaccion = this.session.beginTransaction();

            this.listaGasto = daoGasto.getAll(this.session);
            this.transaccion.commit();
            return this.listaGasto;
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

    public Gasto getById(int id) {
        this.session = null;
        this.transaccion = null;
        try {
            this.session = HibernateUtil.getSessionFactory().openSession();
            this.transaccion = this.session.beginTransaction();
            DaoGasto daoGasto = new DaoGasto();

            Gasto gast = daoGasto.getById(this.session, id);
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

    public List<Gasto> getListVentasByFecha() {

        if (listaGastoByFecha == null) {
            listaGastoByFecha = new ArrayList<>();
        }
        return listaGastoByFecha;
    }

    public void consultarGastos() {

        this.session = null;
        this.transaccion = null;

        try {

            this.session = HibernateUtil.getSessionFactory().openSession();
            this.transaccion = this.session.beginTransaction();
            DaoGasto daoGasto = new DaoGasto();
            this.listaGastoByFecha = daoGasto.getAllFecha(this.session, this.fechaInicio, this.fechaFin);
            totalVentasFecha = 0;

            for (Gasto gasto : listaGastoByFecha) {
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

    public void selectFactura(int id) {
        this.idGasto = id;
    }

    public Gasto getGasto() {
        return gasto;
    }

    public void setGasto(Gasto gasto) {
        this.gasto = gasto;
    }

    public List<Gasto> getListaGasto() {
        return listaGasto;
    }

    public void setListaGasto(List<Gasto> listaGasto) {
        this.listaGasto = listaGasto;
    }

    public List<Gasto> getListaGastoFiltrado() {
        return listaGastoFiltrado;
    }

    public void setListaGastoFiltrado(List<Gasto> listaGastoFiltrado) {
        this.listaGastoFiltrado = listaGastoFiltrado;
    }

    public int getCodigoGasto() {
        return codigoGasto;
    }

    public void setCodigoGasto(int codigoGasto) {
        this.codigoGasto = codigoGasto;
    }

    public Gasto getGasto1() {
        return gasto1;
    }

    public void setGasto1(Gasto gasto1) {
        this.gasto1 = gasto1;
    }

    public List<Gasto> getListaGastoByFecha() {
        return listaGastoByFecha;
    }

    public void setListaGastoByFecha(List<Gasto> listaGastoByFecha) {
        this.listaGastoByFecha = listaGastoByFecha;
    }

    public int getIdGasto() {
        return idGasto;
    }

    public void setIdGasto(int idGasto) {
        this.idGasto = idGasto;
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
