/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pagos.bean;

import com.pagos.Dao.DaoAbonoSecundario;
import com.pagos.Dao.DaoAbonos;
import com.pagos.Dao.DaoCliente;
import com.pagos.Dao.DaoEmpleado;
import com.pagos.Pojos.Abonos;
import com.pagos.Pojos.Abonosecundario;
import com.pagos.Pojos.Cliente;
import com.pagos.util.HibernateUtil;
import java.text.SimpleDateFormat;
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

/**
 *
 * @author ander
 */
@ManagedBean
@ViewScoped
public class MbAbonoSecundario {
    
    private Session session;
    private Transaction transaccion;
    private int idEmpleado;
    private int idAbono;
    private int idAbonoSecundario;
    private String numeroDocumento;
    private int codigoEmpleado;
    private List<Abonos> listaAbonos;
    private List<Abonosecundario> listaAbonoSecundario;
    private List<Abonosecundario> listaAbonoFiltradoSecundario;
    private Cliente cliente;
    private Abonos abonos;
    private List<Abonosecundario> listaAbonoSecundarioByFecha;
    private Date fechaFin;
    private Date fechaInicio;
    private long totalVentasFecha;
    private Abonosecundario abonoSelect;
    private Abonosecundario abonosecundario;
    private String codigo;

    /**
     * Creates a new instance of MbAbonoSecundario
     */
    public MbAbonoSecundario() {
        this.abonosecundario = new Abonosecundario();
        this.abonosecundario.setIdabonosecun(Integer.MIN_VALUE);
        this.listaAbonos = new ArrayList<>();
        this.cliente = new Cliente();
        this.listaAbonoSecundario = new ArrayList<>();
        this.abonos = new Abonos();
        this.codigoEmpleado = 0;
        
    }
    
    public void noPago() {
        this.listaAbonos = new ArrayList<>();
        this.session = null;
        this.transaccion = null;
        try {
            this.session = HibernateUtil.getSessionFactory().openSession();
            this.transaccion = this.session.beginTransaction();
            DaoEmpleado daoEmpleado = new DaoEmpleado();
            DaoAbonos daoAbonos = new DaoAbonos();
            abonos = daoAbonos.getById(this.session, this.idAbono);
            abonos.setCuotaVencida(abonos.getCuotaVencida() + 1);
            daoAbonos.actualizar(this.session, abonos);
            this.listaAbonos.addAll(daoAbonos.getAllByCliente(this.session, this.numeroDocumento));
            
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Correcto", "Se registro una cuota no paga"));
            this.transaccion.commit();
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Contacte con su administrador" + e.getMessage()));
            if (this.transaccion != null) {
                this.transaccion.rollback();
                this.session.close();
            }
        }
    }
    
    public void save() {
        this.listaAbonos = new ArrayList<>();
        this.session = null;
        this.transaccion = null;
        try {
            this.session = HibernateUtil.getSessionFactory().openSession();
            this.transaccion = this.session.beginTransaction();
            
            DaoAbonoSecundario daoAbonoSecundario = new DaoAbonoSecundario();
            DaoEmpleado daoEmpleado = new DaoEmpleado();
            DaoAbonos daoAbonos = new DaoAbonos();
            Abonos abonos = new Abonos();
            System.out.println("antes de la operacion de descontar");
            abonos = daoAbonos.getById(this.session, this.idAbono);
            System.out.println("id del abono......" + abonos.getIdabonos());
            
            if (abonos.getSaldofinal() >= this.abonosecundario.getValorAbono()) {
                abonos.setSaldofinal(abonos.getSaldofinal() - this.abonosecundario.getValorAbono());
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "El monto de abono es mayor que el saldo.." + abonos.getSaldofinal()));
                return;
            }
            System.out.println("antes de la condicion");
            if (abonos.getSaldofinal() - this.abonosecundario.getValorAbono() <= 0) {
                System.out.println("antes de el estado ");
                abonos.setEstado("CANCELADO");
                System.out.println("despues de el estado  y cuota vencidad" + abonos.getEstado());
                abonos.setCuotaVencida(0);
                System.out.println("despues de el estado  y cuota vencida" + abonos.getCuotaVencida());
                
                System.out.println("despues  de el estado y cuota en 0" + abonos.getCuota());
            }
            
            if (this.abonosecundario.getValorAbono() / abonos.getValorCuota() == 1) {
                abonos.setCuota(abonos.getCuota() - 1);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Correcto", "Se agrego una nueva Cuota"));
            } else if (this.abonosecundario.getValorAbono() / abonos.getValorCuota() == 2) {
                abonos.setCuota(abonos.getCuota() - 2);
                if (abonos.getCuotaVencida() >= 1) {
                    abonos.setCuotaVencida(this.abonos.getCuotaVencida() - 1);
                }
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Correcto", "Se agregaron 2 Cuotas"));
            } else if (this.abonosecundario.getValorAbono() / abonos.getValorCuota() == 3) {
                abonos.setCuota(abonos.getCuota() - 3);
                if (abonos.getCuotaVencida() >= 1) {
                    abonos.setCuotaVencida(this.abonos.getCuotaVencida() - 2);
                }
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Correcto", "Se agregaron 3 Cuotas"));
            } else if (this.abonosecundario.getValorAbono() / abonos.getValorCuota() == 4) {
                abonos.setCuota(abonos.getCuota() - 4);
                if (abonos.getCuotaVencida() >= 1) {
                    abonos.setCuotaVencida(this.abonos.getCuotaVencida() - 3);
                }
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Correcto", "Se agregaron 4 Cuotas"));
            } else if (this.abonosecundario.getValorAbono() / abonos.getValorCuota() == 5) {
                abonos.setCuota(abonos.getCuota() - 5);
                if (abonos.getCuotaVencida() >= 1) {
                    abonos.setCuotaVencida(this.abonos.getCuotaVencida() - 4);
                }
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Correcto", "Se agregaron 5 Cuotas"));
            } else if (this.abonosecundario.getValorAbono() / abonos.getValorCuota() == 6) {
                abonos.setCuota(abonos.getCuota() - 6);
                if (abonos.getCuotaVencida() >= 1) {
                    abonos.setCuotaVencida(this.abonos.getCuotaVencida() - 5);
                }
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Correcto", "Se agregaron 6 Cuotas"));
            } else if (this.abonosecundario.getValorAbono() / abonos.getValorCuota() == 7) {
                abonos.setCuota(abonos.getCuota() - 7);
                if (abonos.getCuotaVencida() >= 1) {
                    abonos.setCuotaVencida(this.abonos.getCuotaVencida() - 6);
                }
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Correcto", "Se agregaron 7 Cuotas"));
            } else if (this.abonosecundario.getValorAbono() / abonos.getValorCuota() == 8) {
                abonos.setCuota(abonos.getCuota() - 8);
                if (abonos.getCuotaVencida() >= 1) {
                    abonos.setCuotaVencida(this.abonos.getCuotaVencida() - 7);
                }
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Correcto", "Se agregaron 8Cuotas"));
            } else if (this.abonosecundario.getValorAbono() / abonos.getValorCuota() == 9) {
                abonos.setCuota(abonos.getCuota() - 9);
                if (abonos.getCuotaVencida() >= 1) {
                    abonos.setCuotaVencida(this.abonos.getCuotaVencida() - 8);
                }
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Correcto", "Se agregaron 9 Cuotas"));
            } else if (this.abonosecundario.getValorAbono() / abonos.getValorCuota() == 10) {
                abonos.setCuota(abonos.getCuota() - 10);
                if (abonos.getCuotaVencida() >= 1) {
                    abonos.setCuotaVencida(this.abonos.getCuotaVencida() - 9);
                }
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Correcto", "Se agregaron 10 Cuotas"));
            } else if (this.abonosecundario.getValorAbono() / abonos.getValorCuota() == 11) {
                abonos.setCuota(abonos.getCuota() - 11);
                if (abonos.getCuotaVencida() >= 1) {
                    abonos.setCuotaVencida(this.abonos.getCuotaVencida() - 10);
                }
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Correcto", "Se agregaron 11 Cuotas"));
            } else if (this.abonosecundario.getValorAbono() / abonos.getValorCuota() == 12) {
                abonos.setCuota(abonos.getCuota() - 12);
                if (abonos.getCuotaVencida() >= 1) {
                    abonos.setCuotaVencida(this.abonos.getCuotaVencida() - 11);
                }
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Correcto", "Se agregaron 12 Cuotas"));
            } else if (this.abonosecundario.getValorAbono() / abonos.getValorCuota() == 13) {
                abonos.setCuota(abonos.getCuota() - 13);
                if (abonos.getCuotaVencida() >= 1) {
                    abonos.setCuotaVencida(this.abonos.getCuotaVencida() - 12);
                }
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Correcto", "Se agregaron 13 Cuotas"));
            } else if (this.abonosecundario.getValorAbono() / abonos.getValorCuota() == 14) {
                abonos.setCuota(abonos.getCuota() - 14);
                if (abonos.getCuotaVencida() >= 1) {
                    abonos.setCuotaVencida(this.abonos.getCuotaVencida() - 13);
                }
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Correcto", "Se agregaron 14 Cuotas"));
            } else if (this.abonosecundario.getValorAbono() / abonos.getValorCuota() == 15) {
                abonos.setCuota(abonos.getCuota() - 15);
                if (abonos.getCuotaVencida() >= 1) {
                    abonos.setCuotaVencida(this.abonos.getCuotaVencida() - 14);
                }
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Correcto", "Se agregaron 15 Cuotas"));
            } else if (abonos.getSaldofinal() - this.abonosecundario.getValorAbono() <= 0) {
                abonos.setEstado("CANCELADO");
                System.out.println("despues de el estado  y cuota vencidad" + abonos.getEstado());
                abonos.setCuotaVencida(0);
                abonos.setCuota(0);
                abonos.setSaldofinal(0l);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Correcto", "Se agrego una nueva cuota vencida."));
                
            }
            
            daoAbonos.actualizar(this.session, abonos);
            System.out.println("despues de la resta");
            long suma = 0;
            this.abonosecundario.setAbonos(abonos);
            System.out.println("antes de la empleado");
            this.abonosecundario.setEmpleado(daoEmpleado.getById(this.session, this.codigoEmpleado));
            suma = abonos.getSaldofinal();
            this.abonosecundario.setSaldoTotaL(suma);
            
            System.out.println("se registro");
            
            if (this.abonosecundario.getValorAbono() / abonos.getValorCuota() == 1) {
                this.abonosecundario.setCuota(1);
            } else if (this.abonosecundario.getValorAbono() / abonos.getValorCuota() == 2) {
                this.abonosecundario.setCuota(2);
            } else if (this.abonosecundario.getValorAbono() / abonos.getValorCuota() == 3) {
                this.abonosecundario.setCuota(3);
            } else if (this.abonosecundario.getValorAbono() / abonos.getValorCuota() == 4) {
                this.abonosecundario.setCuota(4);
            } else if (this.abonosecundario.getValorAbono() / abonos.getValorCuota() == 5) {
                this.abonosecundario.setCuota(5);
            } else if (this.abonosecundario.getValorAbono() / abonos.getValorCuota() == 6) {
                this.abonosecundario.setCuota(6);
            } else if (this.abonosecundario.getValorAbono() / abonos.getValorCuota() == 7) {
                this.abonosecundario.setCuota(7);
            } else if (this.abonosecundario.getValorAbono() / abonos.getValorCuota() == 8) {
                this.abonosecundario.setCuota(8);
            } else if (this.abonosecundario.getValorAbono() / abonos.getValorCuota() == 9) {
                this.abonosecundario.setCuota(9);
            } else if (this.abonosecundario.getValorAbono() / abonos.getValorCuota() == 10) {
                this.abonosecundario.setCuota(10);
            } else if (this.abonosecundario.getValorAbono() / abonos.getValorCuota() == 11) {
                this.abonosecundario.setCuota(11);
            } else if (this.abonosecundario.getValorAbono() / abonos.getValorCuota() == 12) {
                this.abonosecundario.setCuota(12);
            } else if (this.abonosecundario.getValorAbono() / abonos.getValorCuota() == 13) {
                this.abonosecundario.setCuota(13);
            } else if (this.abonosecundario.getValorAbono() / abonos.getValorCuota() == 14) {
                this.abonosecundario.setCuota(14);
            } else if (this.abonosecundario.getValorAbono() / abonos.getValorCuota() == 15) {
                this.abonosecundario.setCuota(15);
            } else if (this.abonosecundario.getValorAbono() / abonos.getValorCuota() == 16) {
                this.abonosecundario.setCuota(16);
            } else if (this.abonosecundario.getValorAbono() / abonos.getValorCuota() == 17) {
                this.abonosecundario.setCuota(17);
            } else if (this.abonosecundario.getValorAbono() / abonos.getValorCuota() == 18) {
                this.abonosecundario.setCuota(18);
            } else if (this.abonosecundario.getValorAbono() / abonos.getValorCuota() == 19) {
                this.abonosecundario.setCuota(19);
            } else if (this.abonosecundario.getValorAbono() / abonos.getValorCuota() == 20) {
                this.abonosecundario.setCuota(20);
            }
            
            System.out.println("antes de la lista de clientes");
            this.listaAbonos.addAll(daoAbonos.getAllByCliente(this.session, this.numeroDocumento));
            this.abonosecundario.setFechaRegistro(new Date());
            daoAbonoSecundario.registar(this.session, this.abonosecundario);
            System.out.println("antes del commic");
            this.transaccion.commit();
            System.out.println("despues del commic");
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "correcto", "Se ha registrado"));
            System.out.println("despues del registro");
            this.abonosecundario = new Abonosecundario();
            this.numeroDocumento = "";
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Contacte con su administrador" + e.getMessage()));
            if (this.transaccion != null) {
                this.transaccion.rollback();
                this.session.close();
            }
        }
    }
    
    public void selectAbono(int id) {
        this.idAbono = id;
    }
    
    public void selectAbonoSecundario(int id) {
        this.idAbonoSecundario = id;
    }
    
    public void searchByDocumento() {
        this.listaAbonos = new ArrayList<>();
        this.session = null;
        this.transaccion = null;
        try {
            this.session = HibernateUtil.getSessionFactory().openSession();
            this.transaccion = this.session.beginTransaction();
            
            DaoCliente daoCliente = new DaoCliente();
            DaoAbonos daoAbonos = new DaoAbonos();
            this.cliente = daoCliente.getByNumeroDocumento(this.session, this.numeroDocumento);
            this.listaAbonos.addAll(daoAbonos.getAllByCliente(this.session, this.numeroDocumento));
            
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Contacte con su administrador" + e.getMessage()));
            this.listaAbonos = null;
            this.cliente = null;
            if (this.transaccion != null) {
                this.transaccion.rollback();
                this.session.close();
            }
        }
    }
    
    
      public void searchByCodigoClente() {
        this.listaAbonos = new ArrayList<>();
        this.session = null;
        this.transaccion = null;
        try {
            this.session = HibernateUtil.getSessionFactory().openSession();
            this.transaccion = this.session.beginTransaction();
            
            DaoCliente daoCliente = new DaoCliente();
            DaoAbonos daoAbonos = new DaoAbonos();
            this.cliente = daoCliente.getByCodigo(this.session, this.codigo);
            this.listaAbonos.addAll(daoAbonos.getAllByCliente(this.session, this.codigo));
            
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Contacte con su administrador" + e.getMessage()));
            this.listaAbonos = null;
            this.cliente = null;
            if (this.transaccion != null) {
                this.transaccion.rollback();
                this.session.close();
            }
        }
    }
    
    public void BuscarBYcodigoAbonoSecundario(int id) {
        this.listaAbonoSecundario = new ArrayList<>();
        this.session = null;
        this.transaccion = null;
        try {
            this.session = HibernateUtil.getSessionFactory().openSession();
            this.transaccion = this.session.beginTransaction();
            DaoAbonoSecundario daoAbonoSecundario = new DaoAbonoSecundario();
            this.listaAbonoSecundario.clear();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "el id es" + id));
            System.out.println("el id es" + id);
            this.listaAbonoSecundario.addAll(daoAbonoSecundario.getAllByIdAbonos(this.session, id));
            
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Contacte con su administrador" + e.getMessage()));
            this.listaAbonoSecundario = null;
            this.abonos = null;
            if (this.transaccion != null) {
                this.transaccion.rollback();
                this.session.close();
            }
        }
    }
    
    public void BuscarBYcodigoAbono(int id) {
        this.session = null;
        this.transaccion = null;
        this.abonoSelect = null;
        try {
            this.session = HibernateUtil.getSessionFactory().openSession();
            this.transaccion = this.session.beginTransaction();
            
            DaoAbonoSecundario daoAbonoSecundario = new DaoAbonoSecundario();
            
            this.abonoSelect = daoAbonoSecundario.getById(this.session, id);
            this.transaccion.commit();
            
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Contacte con su administrador" + e.getMessage()));
            if (this.transaccion != null) {
                this.transaccion.rollback();
                this.session.close();
            }
        }
    }
    
    public List<Abonosecundario> getAll() {
        this.session = null;
        this.transaccion = null;
        try {
            
            DaoAbonoSecundario daoAbonoSecundario = new DaoAbonoSecundario();
            
            this.session = HibernateUtil.getSessionFactory().openSession();
            this.transaccion = this.session.beginTransaction();
            
            this.listaAbonoSecundario = daoAbonoSecundario.getAll(this.session);
            this.transaccion.commit();
            return this.listaAbonoSecundario;
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
    
    public List<Abonosecundario> getListAbonoSecundarioByFecha() {
        if (listaAbonoSecundarioByFecha == null) {
            listaAbonoSecundarioByFecha = new ArrayList<>();
        }
        return listaAbonoSecundarioByFecha;
    }
    
    public void consultarVentas() {
        this.session = null;
        this.transaccion = null;
        try {
            this.session = HibernateUtil.getSessionFactory().openSession();
            this.transaccion = this.session.beginTransaction();
            
            DaoAbonoSecundario daoAbonoSecundario = new DaoAbonoSecundario();
            this.listaAbonoSecundarioByFecha = daoAbonoSecundario.getAllByDate(this.session, this.fechaInicio, this.fechaFin);
            
            totalVentasFecha = 0;
            for (Abonosecundario abonosecundari : listaAbonoSecundarioByFecha) {
                totalVentasFecha = totalVentasFecha + (abonosecundari.getValorAbono());
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
    
    public String convertFecha(Date fecha) {
        if (fecha != null) {
            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
            return format.format(fecha);
        }
        return "";
    }
    
    public Abonosecundario getAbonosecundario() {
        return abonosecundario;
    }
    
    public void setAbonosecundario(Abonosecundario abonosecundario) {
        this.abonosecundario = abonosecundario;
    }
    
    public String getNumeroDocumento() {
        return numeroDocumento;
    }
    
    public void setNumeroDocumento(String numeroDocumento) {
        this.numeroDocumento = numeroDocumento;
    }
    
    public List<Abonos> getListaAbonos() {
        return listaAbonos;
    }
    
    public void setListaAbonos(List<Abonos> listaAbonos) {
        this.listaAbonos = listaAbonos;
    }
    
    public int getIdEmpleado() {
        return idEmpleado;
    }
    
    public void setIdEmpleado(int idEmpleado) {
        this.idEmpleado = idEmpleado;
    }
    
    public int getIdAbono() {
        return idAbono;
    }
    
    public void setIdAbono(int idAbono) {
        this.idAbono = idAbono;
    }
    
    public Cliente getCliente() {
        return cliente;
    }
    
    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }
    
    public List<Abonosecundario> getListaAbonoSecundario() {
        return listaAbonoSecundario;
    }
    
    public void setListaAbonoSecundario(List<Abonosecundario> listaAbonoSecundario) {
        this.listaAbonoSecundario = listaAbonoSecundario;
    }
    
    public List<Abonosecundario> getListaAbonoFiltradoSecundario() {
        return listaAbonoFiltradoSecundario;
    }
    
    public void setListaAbonoFiltradoSecundario(List<Abonosecundario> listaAbonoFiltradoSecundario) {
        this.listaAbonoFiltradoSecundario = listaAbonoFiltradoSecundario;
    }
    
    public Abonos getAbonos() {
        return abonos;
    }
    
    public void setAbonos(Abonos abonos) {
        this.abonos = abonos;
    }
    
    public List<Abonosecundario> getListaAbonoSecundarioByFecha() {
        return listaAbonoSecundarioByFecha;
    }
    
    public void setListaAbonoSecundarioByFecha(List<Abonosecundario> listaAbonoSecundarioByFecha) {
        this.listaAbonoSecundarioByFecha = listaAbonoSecundarioByFecha;
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
    
    public int getIdAbonoSecundario() {
        return idAbonoSecundario;
    }
    
    public Abonosecundario getAbonoSelect() {
        return abonoSelect;
    }
    
    public void setAbonoSelect(Abonosecundario abonoSelect) {
        this.abonoSelect = abonoSelect;
    }
    
    public void setIdAbonoSecundario(int idAbonoSecundario) {
        this.idAbonoSecundario = idAbonoSecundario;
    }
    
    public int getCodigoEmpleado() {
        return codigoEmpleado;
    }
    
    public void setCodigoEmpleado(int codigoEmpleado) {
        this.codigoEmpleado = codigoEmpleado;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }
    
}
