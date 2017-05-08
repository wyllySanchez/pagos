/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pagos.bean;

import com.pagos.Dao.DaoAbonoSecundario;
import com.pagos.Dao.DaoAbonos;
import com.pagos.Dao.DaoCaja;
import com.pagos.Dao.DaoGasto;
import com.pagos.Pojos.Abonos;
import com.pagos.Pojos.Abonosecundario;
import com.pagos.Pojos.Caja;
import com.pagos.Pojos.Gasto;
import com.pagos.util.HibernateUtil;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 *
 * @author williamSanchez
 */
@ManagedBean
@ViewScoped
public class MbBalances {

    /**
     * Creates a new instance of MbBalances
     */
    private Session session;
    private Transaction transaction;
    private Date fechaFin;
    private Date fechaInicio;
    
    private long totalVentasFecha;
    private long totalAbonosFecha;
    private long totalCajaFecha;
    private long totalAbonoSecundarioFecha;
    private long totalGastosFecha;
    
    private long totalBalance;
    private long totalFecha;
    
    private List<Abonos> listaAbonosByFecha;
    private List<Abonosecundario> listaAbonoSEcundarioByFecha;
    private List<Gasto> listaGastosByFecha;
    private List<Caja> listaCajaByFecha;
    private List listaBalance;
    
    private long totalFormaPago;

    public MbBalances() {
        this.listaBalance = new ArrayList();
    }

    public List<Abonos> getListAbonosByFecha() {

        if (listaAbonosByFecha == null) {
            listaAbonosByFecha = new ArrayList<>();
        }
        return listaAbonosByFecha;
    }

    public List<Abonosecundario> getListAbonoSecundarioByFecha() {

        if (listaAbonoSEcundarioByFecha == null) {
            listaAbonoSEcundarioByFecha = new ArrayList<>();
        }
        return listaAbonoSEcundarioByFecha;
    }

    public List<Gasto> getListGastosByFecha() {

        if (listaGastosByFecha == null) {
            listaGastosByFecha = new ArrayList<>();
        }
        return listaGastosByFecha;
    }

    public List<Caja> getListCajaByFecha() {

        if (listaCajaByFecha == null) {
            listaCajaByFecha = new ArrayList<>();
        }
        return listaCajaByFecha;
    }

    public void consultarVentas() {

        this.session = null;
        this.transaction = null;

        try {

            this.session = HibernateUtil.getSessionFactory().openSession();
            this.transaction = this.session.beginTransaction();
            DaoAbonos daoAbonos = new DaoAbonos();
            DaoAbonoSecundario daoAbonoSecundario = new DaoAbonoSecundario();
            DaoGasto daoGasto = new DaoGasto();
            DaoCaja daoCaja = new DaoCaja();
//            this.listaVentasByFecha = daoFactura.getAllFecha(this.session, this.fechaInicio, this.fechaFin);
            this.listaAbonosByFecha = daoAbonos.getAllFecha(this.session, this.fechaInicio, this.fechaFin);
            this.listaAbonoSEcundarioByFecha = daoAbonoSecundario.getAllByDate(this.session, this.fechaInicio, this.fechaFin);
            this.listaGastosByFecha = daoGasto.getAllFecha(this.session, this.fechaInicio, this.fechaFin);
            this.listaCajaByFecha = daoCaja.getAllFecha(this.session, this.fechaInicio, this.fechaFin);
            totalAbonosFecha = 0;
            totalAbonoSecundarioFecha = 0;
            totalGastosFecha = 0;
            totalCajaFecha=0;

            for (Caja caja : listaCajaByFecha) {
                totalCajaFecha = totalCajaFecha + (caja.getValor());
            }
            for (Abonos Abono : listaAbonosByFecha) {
                totalAbonosFecha = totalAbonosFecha + (Abono.getPrecioTotal());
            }
            for (Abonosecundario abonosecundario : listaAbonoSEcundarioByFecha) {
                totalAbonoSecundarioFecha = totalAbonoSecundarioFecha + (abonosecundario.getValorAbono());
            }
            for (Gasto gasto : listaGastosByFecha) {
                totalGastosFecha = totalGastosFecha + (gasto.getValor());
            }
            this.totalBalance = (totalCajaFecha + totalAbonoSecundarioFecha) - (totalGastosFecha + totalAbonosFecha);

            this.transaction.commit();
        } catch (Exception ex) {
            if (this.transaction != null) {
                this.transaction.rollback();
            }
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error fatal:", "Por favor intente mas tarde " + ex.getMessage()));
        } finally {
            if (this.session != null) {
                this.session.close();
            }
        }
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

    public List<Abonos> getListaAbonosByFecha() {
        return listaAbonosByFecha;
    }

    public void setListaAbonosByFecha(List<Abonos> listaAbonosByFecha) {
        this.listaAbonosByFecha = listaAbonosByFecha;
    }

    public List<Gasto> getListaGastosByFecha() {
        return listaGastosByFecha;
    }

    public void setListaGastosByFecha(List<Gasto> listaGastosByFecha) {
        this.listaGastosByFecha = listaGastosByFecha;
    }

    public long getTotalFecha() {
        return totalFecha;
    }

    public void setTotalFecha(long totalFecha) {
        this.totalFecha = totalFecha;
    }

    public List getListaBalance() {
        return listaBalance;
    }

    public void setListaBalance(List listaBalance) {
        this.listaBalance = listaBalance;
    }

    public long getTotalVentasFecha() {
        return totalVentasFecha;
    }

    public void setTotalVentasFecha(long totalVentasFecha) {
        this.totalVentasFecha = totalVentasFecha;
    }

    public long getTotalAbonosFecha() {
        return totalAbonosFecha;
    }

    public void setTotalAbonosFecha(long totalAbonosFecha) {
        this.totalAbonosFecha = totalAbonosFecha;
    }

    public long getTotalGastosFecha() {
        return totalGastosFecha;
    }

    public void setTotalGastosFecha(long totalGastosFecha) {
        this.totalGastosFecha = totalGastosFecha;
    }

    public long getTotalBalance() {
        return totalBalance;
    }

    public void setTotalBalance(long totalBalance) {
        this.totalBalance = totalBalance;
    }

    public long getTotalAbonoSecundarioFecha() {
        return totalAbonoSecundarioFecha;
    }

    public void setTotalAbonoSecundarioFecha(long totalAbonoSecundarioFecha) {
        this.totalAbonoSecundarioFecha = totalAbonoSecundarioFecha;
    }

    public List<Abonosecundario> getListaAbonoSEcundarioByFecha() {
        return listaAbonoSEcundarioByFecha;
    }

    public void setListaAbonoSEcundarioByFecha(List<Abonosecundario> listaAbonoSEcundarioByFecha) {
        this.listaAbonoSEcundarioByFecha = listaAbonoSEcundarioByFecha;
    }

    public long getTotalFormaPago() {
        return totalFormaPago;
    }

    public void setTotalFormaPago(long totalFormaPago) {
        this.totalFormaPago = totalFormaPago;
    }

    public long getTotalCajaFecha() {
        return totalCajaFecha;
    }

    public void setTotalCajaFecha(long totalCajaFecha) {
        this.totalCajaFecha = totalCajaFecha;
    }

    public List<Caja> getListaCajaByFecha() {
        return listaCajaByFecha;
    }

    public void setListaCajaByFecha(List<Caja> listaCajaByFecha) {
        this.listaCajaByFecha = listaCajaByFecha;
    }

}
