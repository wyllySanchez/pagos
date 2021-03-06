package com.pagos.Pojos;
// Generated 8/05/2017 04:50:17 PM by Hibernate Tools 4.3.1


import java.util.Date;

/**
 * Caja generated by hbm2java
 */
public class Caja  implements java.io.Serializable {


     private Integer idcaja;
     private Empleado empleado;
     private long valor;
     private Date fecha;

    public Caja() {
    }

	
    public Caja(long valor) {
        this.valor = valor;
    }
    public Caja(Empleado empleado, long valor, Date fecha) {
       this.empleado = empleado;
       this.valor = valor;
       this.fecha = fecha;
    }
   
    public Integer getIdcaja() {
        return this.idcaja;
    }
    
    public void setIdcaja(Integer idcaja) {
        this.idcaja = idcaja;
    }
    public Empleado getEmpleado() {
        return this.empleado;
    }
    
    public void setEmpleado(Empleado empleado) {
        this.empleado = empleado;
    }
    public long getValor() {
        return this.valor;
    }
    
    public void setValor(long valor) {
        this.valor = valor;
    }
    public Date getFecha() {
        return this.fecha;
    }
    
    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }




}


