package com.pagos.Pojos;
// Generated 1/05/2017 01:09:13 PM by Hibernate Tools 4.3.1


import java.util.Date;

/**
 * Abonosecundario generated by hbm2java
 */
public class Abonosecundario  implements java.io.Serializable {


     private Integer idabonosecun;
     private Abonos abonos;
     private Empleado empleado;
     private long valorAbono;
     private Long saldoTotaL;
     private int cuota;
     private Date fechaRegistro;

    public Abonosecundario() {
    }

	
    public Abonosecundario(Abonos abonos, Empleado empleado, long valorAbono, int cuota, Date fechaRegistro) {
        this.abonos = abonos;
        this.empleado = empleado;
        this.valorAbono = valorAbono;
        this.cuota = cuota;
        this.fechaRegistro = fechaRegistro;
    }
    public Abonosecundario(Abonos abonos, Empleado empleado, long valorAbono, Long saldoTotaL, int cuota, Date fechaRegistro) {
       this.abonos = abonos;
       this.empleado = empleado;
       this.valorAbono = valorAbono;
       this.saldoTotaL = saldoTotaL;
       this.cuota = cuota;
       this.fechaRegistro = fechaRegistro;
    }
   
    public Integer getIdabonosecun() {
        return this.idabonosecun;
    }
    
    public void setIdabonosecun(Integer idabonosecun) {
        this.idabonosecun = idabonosecun;
    }
    public Abonos getAbonos() {
        return this.abonos;
    }
    
    public void setAbonos(Abonos abonos) {
        this.abonos = abonos;
    }
    public Empleado getEmpleado() {
        return this.empleado;
    }
    
    public void setEmpleado(Empleado empleado) {
        this.empleado = empleado;
    }
    public long getValorAbono() {
        return this.valorAbono;
    }
    
    public void setValorAbono(long valorAbono) {
        this.valorAbono = valorAbono;
    }
    public Long getSaldoTotaL() {
        return this.saldoTotaL;
    }
    
    public void setSaldoTotaL(Long saldoTotaL) {
        this.saldoTotaL = saldoTotaL;
    }
    public int getCuota() {
        return this.cuota;
    }
    
    public void setCuota(int cuota) {
        this.cuota = cuota;
    }
    public Date getFechaRegistro() {
        return this.fechaRegistro;
    }
    
    public void setFechaRegistro(Date fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }




}


