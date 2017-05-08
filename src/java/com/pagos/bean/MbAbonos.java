/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pagos.bean;

import com.pagos.Dao.DaoAbonos;
import com.pagos.Dao.DaoCliente;
import com.pagos.Dao.DaoEmpleado;
import com.pagos.Dao.DaoRuta;
import com.pagos.Pojos.Abonos;
import com.pagos.Pojos.Abonosecundario;
import com.pagos.Pojos.Cliente;
import com.pagos.Pojos.Empleado;
import com.pagos.util.HibernateUtil;
import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.primefaces.context.RequestContext;
import org.primefaces.event.RowEditEvent;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderWidths;

/**
 *
 * @author William Sanchez
 */
@ManagedBean
@ViewScoped
public class MbAbonos implements Serializable {

    /**
     * Creates a new instance of MbAbonos
     */
    private Session session;
    private Transaction transaccion;

    private List<Abonos> listaAbonosFiltrado;
    private int tipoVidrio;
    private Abonos abonos;
    private List<Abonos> listaAbonoSel;
    private List<Abonos> listaAbono;
    private List<Abonos> listaVentasByFecha;
    private String idCliente;
    private int idEmpleado;
    private int idAbono;
    private Date fechaFin;
    private Date fechaInicio;
    private long totalVentasFecha;
    private long montoAbono;
    private List<Abonosecundario> listaAbonoSecundario;
    private int idAbonoSecundario;
    private long precioTotal;
    private String numeroDocumento;
    private Cliente cliente;
    private Abonos abonoSelect;
    private Empleado empleado;
    private Integer codigoCliente;
    private int codigoEmpleado;
    private List<Cliente> listaClientes;
    private List<Empleado> listaEmpleado;
    private int codigoAbono;
    private int codigoRuta;
    private int idProductoDevolucion;
    private String codigo;

    public MbAbonos() {

        this.listaVentasByFecha = new ArrayList<>();
        this.listaAbonoSel = new ArrayList<>();
        this.abonos = new Abonos();
        this.listaAbonoSecundario = new ArrayList<>();
        this.empleado = new Empleado();
        this.listaClientes = new ArrayList<>();
        this.listaEmpleado = new ArrayList<>();
        this.cliente = new Cliente();
        this.codigoCliente = 0;
        this.codigoEmpleado = 0;
        this.abonos = new Abonos();
        this.codigoRuta = 0;
        this.abonos.setCuota(0);
        this.abonos.setSaldofinal(0l);
        this.abonos.setPrecioTotal(0l);
        this.abonos.setTotalCuotas(0);
        this.abonos.setValorCuota(0l);

    }

    public void registroPrestamo() {
        this.session = null;
        this.transaccion = null;
        try {
            DaoCliente daoCliente = new DaoCliente();
            DaoEmpleado daoEmpleado = new DaoEmpleado();
            DaoAbonos daoAbonos = new DaoAbonos();
            DaoRuta daoRuta = new DaoRuta();

            if (this.cliente == null) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Error:", "Por favor selecione un cliente."));
                return ;
            }
            if (this.empleado == null) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Error:", "Por favor selecione un Empleado."));
                return ;
            }

            this.session = HibernateUtil.getSessionFactory().openSession();
            this.transaccion = this.session.beginTransaction();

            this.abonos.setCliente(cliente);

            this.abonos.setEmpleado(empleado);
            this.abonos.setRuta(daoRuta.getById(this.session, this.codigoRuta));
            this.abonos.setFecharegistro(new Date());
            this.abonos.setCuota(this.abonos.getTotalCuotas());
            this.abonos.setEstado("VIGENTE");
            this.abonos.setCuotaVencida(0);
            daoAbonos.registar(this.session, this.abonos);

            this.abonos = daoAbonos.getByUltimoRegistro(this.session);

            this.abonos.getFecharegistro();
            HttpSession sesson = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
            sesson.setAttribute("idfactura", this.abonos.getIdabonos());
            this.transaccion.commit();
            this.abonos = new Abonos();
            this.empleado = new Empleado();
            this.cliente = new Cliente();
            this.codigoRuta = 0;
            this.abonos.setCuota(0);
            this.abonos.setSaldofinal(0l);
            this.abonos.setPrecioTotal(0l);
            this.abonos.setTotalCuotas(0);
            this.abonos.setValorCuota(0l);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Correcto:", "Se ha registrado "));
            return ;
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

    public String convertFecha(Date fecha) {
        if (fecha != null) {
            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
            return format.format(fecha);
        }
        return "";
    }

    public Abonos getFacturaActual() {
        this.session = null;
        this.transaccion = null;

        try {
            DaoAbonos daoAbonos = new DaoAbonos();
            this.session = HibernateUtil.getSessionFactory().openSession();
            this.transaccion = this.session.beginTransaction();

            HttpSession sesson = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
            this.abonos = daoAbonos.getById(this.session, Integer.valueOf(sesson.getAttribute("idfactura").toString()));

            this.transaccion.commit();
            return this.abonos;
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

    public List<Abonos> getAll() {
        this.session = null;
        this.transaccion = null;
        try {

            DaoAbonos daoAbonos = new DaoAbonos();

            this.session = HibernateUtil.getSessionFactory().openSession();
            this.transaccion = this.session.beginTransaction();

            this.listaAbonoSel = daoAbonos.getAll(this.session);
            this.transaccion.commit();
            return this.listaAbonoSel;
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

    public List<Abonos> getAllAbonos() {
        this.session = null;
        this.transaccion = null;
        try {

            DaoAbonos daoAbonos = new DaoAbonos();

            this.session = HibernateUtil.getSessionFactory().openSession();
            this.transaccion = this.session.beginTransaction();

            this.listaAbonoSel = daoAbonos.getAllAbonos(this.session);
            this.transaccion.commit();
            return this.listaAbonoSel;
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

    public List<Abonos> getListVentasByFecha() {
        if (listaVentasByFecha == null) {
            listaVentasByFecha = new ArrayList<>();
        }
        return listaVentasByFecha;
    }

    public void consultarVentas() {
        this.session = null;
        this.transaccion = null;
        try {
            this.session = HibernateUtil.getSessionFactory().openSession();
            this.transaccion = this.session.beginTransaction();

            DaoAbonos daoAbonos = new DaoAbonos();
            this.listaVentasByFecha = daoAbonos.getAllFecha(this.session, this.fechaInicio, this.fechaFin);

            totalVentasFecha = 0;
            for (Abonos abono : listaVentasByFecha) {
                totalVentasFecha = totalVentasFecha + (abono.getPrecioTotal());
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

    public void selectAbono(int id) {
        this.idAbono = id;
    }

    public void searchByDocumento() {
        this.listaAbonoSel = new ArrayList<>();
        this.session = null;
        this.transaccion = null;
        try {
            this.session = HibernateUtil.getSessionFactory().openSession();
            this.transaccion = this.session.beginTransaction();
            DaoCliente daoCliente = new DaoCliente();
            DaoAbonos daoAbonos = new DaoAbonos();
            this.cliente = daoCliente.getByNumeroDocumento(this.session, this.numeroDocumento);
            this.listaAbonoSel.addAll(daoAbonos.getAllByCliente(this.session, this.numeroDocumento));

        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Contacte con su administrador" + e.getMessage()));
            this.listaAbonoSel = null;
            this.cliente = null;
            if (this.transaccion != null) {
                this.transaccion.rollback();
                this.session.close();
            }
        }
    }

    
      public void searchByCodigo() {
        this.listaAbonoSel = new ArrayList<>();
        this.session = null;
        this.transaccion = null;
        try {
            this.session = HibernateUtil.getSessionFactory().openSession();
            this.transaccion = this.session.beginTransaction();
            DaoCliente daoCliente = new DaoCliente();
            DaoAbonos daoAbonos = new DaoAbonos();
            this.cliente = daoCliente.getByCodigo(this.session, this.codigo);
            this.listaAbonoSel.addAll(daoAbonos.getAllByCliente(this.session, this.codigo));

        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Contacte con su administrador" + e.getMessage()));
            this.listaAbonoSel = null;
            this.cliente = null;
            if (this.transaccion != null) {
                this.transaccion.rollback();
                this.session.close();
            }
        }
    }
//
    //    aqui comienza el codigo nuevo 
    public void agregarDatosCliente(Integer codCliente) {
        this.session = null;
        this.transaccion = null;
        try {
            this.session = HibernateUtil.getSessionFactory().openSession();

            DaoCliente daoCliente = new DaoCliente();
            this.transaccion = this.session.beginTransaction();
            //obtener los datos del cliente en la variable objecto cliente segun el codigo del cliente
            this.cliente = daoCliente.getById(this.session, codCliente);

            this.transaccion.commit();
        } catch (Exception e) {
            if (this.transaccion != null) {
                System.out.println(e.getMessage());
                transaccion.rollback();
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERROR:", "El registro fue erroneo"));

            }
        } finally {
            if (this.session != null) {
                this.session.close();
            }
        }
    }

    public void agregarDatosCliente2() {
        this.session = null;
        this.transaccion = null;
        try {

            this.session = HibernateUtil.getSessionFactory().openSession();
            DaoCliente daoCliente = new DaoCliente();
            this.transaccion = this.session.beginTransaction();
            //obtener los datos del producto en la variable objecto cliente segun el codigo del cliente

            this.cliente = daoCliente.getById(this.session, this.codigoCliente);
//
//            if (this.cliente != null) {
//                this.codigoCliente = null;
//                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Correcto:", "Cliente Agregado"));
//
//            } else {
//                this.codigoCliente = null;
//                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERROR:", "El cliente no fue encontrado"));
//            }
            this.transaccion.commit();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Correcto:", "El registro fue realizado correctamente"));
        } catch (Exception e) {
            if (this.transaccion != null) {
                System.out.println(e.getMessage());
                transaccion.rollback();
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERROR:", "El registro fue erroneo"));

            }
        } finally {
            if (this.session != null) {
                this.session.close();
            }
        }
    }

    public void limpiarAbonos() {
        this.cliente = new Cliente();
        this.empleado = new Empleado();
        
        this.abonos = new Abonos();
        this.listaAbono = new ArrayList<>();
        this.montoAbono = 0;

//        invocar el metodo para destivar controles en a factura
        this.disableButton();

    }

    public void agregarDatosEmpleado(Integer codEmpleado) {
        this.session = null;
        this.transaccion = null;
        try {
            this.session = HibernateUtil.getSessionFactory().openSession();

            DaoEmpleado daoEmpleado = new DaoEmpleado();
            this.transaccion = this.session.beginTransaction();
            //obtener los datos del cliente en la variable objecto cliente segun el codigo del cliente

            this.empleado = daoEmpleado.getById(this.session, codEmpleado);

            this.transaccion.commit();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Correcto:", "El registro fue realizado correctamente"));
        } catch (Exception e) {
            if (this.transaccion != null) {
                System.out.println(e.getMessage());
                transaccion.rollback();
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERROR:", "El registro fue erroneo"));

            }
        } finally {
            if (this.session != null) {
                this.session.close();
            }
        }
    }

    public void agregarDatosEmpleado2() {
        this.session = null;
        this.transaccion = null;
        try {

            this.session = HibernateUtil.getSessionFactory().openSession();
            DaoEmpleado daoEmpleado = new DaoEmpleado();
            this.transaccion = this.session.beginTransaction();
            //obtener los datos del producto en la variable objecto cliente segun el codigo del cliente

            this.empleado = daoEmpleado.getById(this.session, this.codigoEmpleado);

//            if (this.empleado != null) {
////                this.codigoEmpleado = null;
//                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Correcto:", "Cliente Agregado"));
//
//            } else {
//                this.codigoCliente = null;
//                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERROR:", "El cliente no fue encontrado"));
//            }
            this.transaccion.commit();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Correcto:", "El registro fue realizado correctamente"));
        } catch (Exception e) {
            if (this.transaccion != null) {
                System.out.println(e.getMessage());
                transaccion.rollback();
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERROR:", "El registro fue erroneo"));

            }
        } finally {
            if (this.session != null) {
                this.session.close();
            }
        }
    }

    public List<Cliente> getAllClientes() {
        this.session = null;
        this.transaccion = null;

        try {
            this.session = HibernateUtil.getSessionFactory().openSession();

            DaoCliente daoCliente = new DaoCliente();

            this.transaccion = this.session.beginTransaction();

            this.listaClientes = daoCliente.getAll(this.session);

            this.transaccion.commit();

            return this.listaClientes;
        } catch (Exception ex) {
            if (this.transaccion != null) {
                transaccion.rollback();
            }

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error", ex.getMessage()));

            return null;
        } finally {
            if (this.session != null) {
                this.session.close();
            }
        }
    }

    public String calculaFecha(Date date) {
        String color = "";
        if (date != null) {
            //Fecha de vencimiento
            Calendar vencido = Calendar.getInstance();
            vencido.setTime(date);
            //Fecha actual
            Calendar fecha = Calendar.getInstance();

            if (fecha.after(vencido) || fecha.equals(vencido)) {
                color = "colorvencido";
            } else {
                long diferencia = vencido.getTime().getTime() - fecha.getTime().getTime();
                long diasDiferencia = diferencia / (24 * 60 * 60 * 1000);
                if (diasDiferencia <= 10 && diasDiferencia >= 6) {
                    color = "colorPendiente";
                } else if (diasDiferencia <= 5) {
                    color = "colorPendientef";
                }
            }
        }
        return color;
    }
//
//public static int diferenciaEnDias2(Date fechaMayor, Date fechaMenor) {
//long diferenciaEn_ms = fechaMayor.getTime() – fechaMenor.getTime();
//long dias = diferenciaEn_ms / (1000 * 60 * 60 * 24);
//return (int) dias;
//}

    public List<Empleado> getAllEmpleado() {
        this.session = null;
        this.transaccion = null;

        try {
            this.session = HibernateUtil.getSessionFactory().openSession();

            DaoEmpleado daoEmpleado = new DaoEmpleado();
            this.transaccion = this.session.beginTransaction();

            this.listaEmpleado = daoEmpleado.getAll(this.session);

            this.transaccion.commit();

            return this.listaEmpleado;
        } catch (Exception ex) {
            if (this.transaccion != null) {
                transaccion.rollback();
            }

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error", ex.getMessage()));

            return null;
        } finally {
            if (this.session != null) {
                this.session.close();
            }
        }
    }

    public void onRowEdit(RowEditEvent Event) {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "CORRECTO:", "Producto editado "));

//                invocamos al metodo calculartotalfactura para actualizar la factura
    }

    public void onRowEditCancel(RowEditEvent event) {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "...:", "No se hizo ningun cambio "));

    }

//    metodos para activar o desativar los botnes del sistema
    private boolean enabled;

    public boolean isEnabled() {
        return enabled;

    }

    public void enebleButton() {
        enabled = true;
    }

    public void disableButton() {
        enabled = false;
    }

    public int getTipoVidrio() {
        return tipoVidrio;
    }

    public void setTipoVidrio(int tipoVidrio) {
        this.tipoVidrio = tipoVidrio;
    }

    public Abonos getAbonos() {
        return abonos;
    }

    public void setAbonos(Abonos abonos) {
        this.abonos = abonos;
    }

    public List<Abonos> getListaAbonoSel() {
        return listaAbonoSel;
    }

    public void setListaAbonoSel(List<Abonos> listaAbonoSel) {
        this.listaAbonoSel = listaAbonoSel;
    }

    public String getNumeroDocumento() {
        return numeroDocumento;
    }

    public void setNumeroDocumento(String numeroDocumento) {
        this.numeroDocumento = numeroDocumento;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public List<Abonos> getListaVentasByFecha() {
        return listaVentasByFecha;
    }

    public void setListaVentasByFecha(List<Abonos> listaVentasByFecha) {
        this.listaVentasByFecha = listaVentasByFecha;
    }

    public String getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(String idCliente) {
        this.idCliente = idCliente;
    }

    public int getIdEmpleado() {
        return idEmpleado;
    }

    public void setIdEmpleado(int idEmpleado) {
        this.idEmpleado = idEmpleado;
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

    public long getTotalVentasFecha() {
        return totalVentasFecha;
    }

    public void setTotalVentasFecha(long totalVentasFecha) {
        this.totalVentasFecha = totalVentasFecha;
    }

    public List<Abonos> getListaAbonosFiltrado() {
        return listaAbonosFiltrado;
    }

    public void setListaAbonosFiltrado(List<Abonos> listaAbonosFiltrado) {
        this.listaAbonosFiltrado = listaAbonosFiltrado;
    }

    public long getMontoAbono() {
        return montoAbono;
    }

    public void setMontoAbono(long montoAbono) {
        this.montoAbono = montoAbono;
    }

    public int getIdAbono() {
        return idAbono;
    }

    public void setIdAbono(int idAbono) {
        this.idAbono = idAbono;
    }

    public int getIdAbonoSecundario() {
        return idAbonoSecundario;
    }

    public void setIdAbonoSecundario(int idAbonoSecundario) {
        this.idAbonoSecundario = idAbonoSecundario;
    }

    public long getPrecioTotal() {
        return precioTotal;
    }

    public void setPrecioTotal(long precioTotal) {
        this.precioTotal = precioTotal;
    }

    public Abonos getAbonoSelect() {
        return abonoSelect;
    }

    public void setAbonoSelect(Abonos abonoSelect) {
        this.abonoSelect = abonoSelect;
    }

    public Empleado getEmpleado() {
        return empleado;
    }

    public void setEmpleado(Empleado empleado) {
        this.empleado = empleado;
    }

    public Integer getCodigoCliente() {
        return codigoCliente;
    }

    public void setCodigoCliente(Integer codigoCliente) {
        this.codigoCliente = codigoCliente;
    }

    public int getCodigoEmpleado() {
        return codigoEmpleado;
    }

    public void setCodigoEmpleado(int codigoEmpleado) {
        this.codigoEmpleado = codigoEmpleado;
    }

    public List<Abonosecundario> getListaAbonoSecundario() {
        return listaAbonoSecundario;
    }

    public void setListaAbonoSecundario(List<Abonosecundario> listaAbonoSecundario) {
        this.listaAbonoSecundario = listaAbonoSecundario;
    }

    public List<Cliente> getListaClientes() {
        return listaClientes;
    }

    public void setListaClientes(List<Cliente> listaClientes) {
        this.listaClientes = listaClientes;
    }

    public List<Empleado> getListaEmpleado() {
        return listaEmpleado;
    }

    public void setListaEmpleado(List<Empleado> listaEmpleado) {
        this.listaEmpleado = listaEmpleado;
    }

    public int getCodigoAbono() {
        return codigoAbono;
    }

    public void setCodigoAbono(int codigoAbono) {
        this.codigoAbono = codigoAbono;
    }

    public List<Abonos> getListaAbono() {
        return listaAbono;
    }

    public void setListaAbono(List<Abonos> listaAbono) {
        this.listaAbono = listaAbono;
    }

    public int getCodigoRuta() {
        return codigoRuta;
    }

    public void setCodigoRuta(int codigoRuta) {
        this.codigoRuta = codigoRuta;
    }

    public int getIdProductoDevolucion() {
        return idProductoDevolucion;
    }

    public void setIdProductoDevolucion(int idProductoDevolucion) {
        this.idProductoDevolucion = idProductoDevolucion;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

}
