package triplei.mx.notification.Entities;

import java.io.Serializable;

public class Entities_Periodo implements Serializable {

    public String PeriodoId;
    public String Codigo;
    public String Dias;
    public String DiasTrabajados;
    public String Mes;
    public String EjercicioFiscal;
    public String Periodicidad;
    public String FechaInicio;
    public String FechaFin;
    public String Activo;

    public Entities_Periodo() {

    }

    public String getPeriodoId() {
        return PeriodoId;
    }

    public void setPeriodoId(String periodoId) {
        PeriodoId = periodoId;
    }

    public String getCodigo() {
        return Codigo;
    }

    public void setCodigo(String codigo) {
        Codigo = codigo;
    }

    public String getDias() {
        return Dias;
    }

    public void setDias(String dias) {
        Dias = dias;
    }

    public String getDiasTrabajados() {
        return DiasTrabajados;
    }

    public void setDiasTrabajados(String diasTrabajados) {
        DiasTrabajados = diasTrabajados;
    }

    public String getMes() {
        return Mes;
    }

    public void setMes(String mes) {
        Mes = mes;
    }

    public String getEjercicioFiscal() {
        return EjercicioFiscal;
    }

    public void setEjercicioFiscal(String ejercicioFiscal) {
        EjercicioFiscal = ejercicioFiscal;
    }

    public String getPeriodicidad() {
        return Periodicidad;
    }

    public void setPeriodicidad(String periodicidad) {
        Periodicidad = periodicidad;
    }

    public String getFechaInicio() {
        return FechaInicio;
    }

    public void setFechaInicio(String fechaInicio) {
        FechaInicio = fechaInicio;
    }

    public String getFechaFin() {
        return FechaFin;
    }

    public void setFechaFin(String fechaFin) {
        FechaFin = fechaFin;
    }

    public String getActivo() {
        return Activo;
    }

    public void setActivo(String activo) {
        Activo = activo;
    }
}
