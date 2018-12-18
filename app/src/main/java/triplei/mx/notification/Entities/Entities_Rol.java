package triplei.mx.notification.Entities;

import android.os.Parcelable;

import java.io.Serializable;

public class Entities_Rol implements Serializable {

    public String rolId;
    public String IdUsuario;
    public String IdPeriodo;
    public String nombreRol;
    public String Fecha_Inicio;
    public String Fecha_Fin;
    public Integer total;
    public String NombreUsuario;
    public String Telefono;

    public Entities_Rol() {

    }

    public String getRolId() {
        return rolId;
    }

    public void setRolId(String rolId) {
        this.rolId = rolId;
    }

    public String getNombreRol() {
        return nombreRol;
    }

    public void setNombreRol(String nombreRol) {
        this.nombreRol = nombreRol;
    }

    public String getFecha_Inicio() {
        return Fecha_Inicio;
    }

    public void setFecha_Inicio(String fecha_Inicio) {
        Fecha_Inicio = fecha_Inicio;
    }

    public String getFecha_Fin() {
        return Fecha_Fin;
    }

    public void setFecha_Fin(String fecha_Fin) {
        Fecha_Fin = fecha_Fin;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public String getIdPeriodo() {
        return IdPeriodo;
    }

    public void setIdPeriodo(String idPeriodo) {
        IdPeriodo = idPeriodo;
    }

    public String getIdUsuario() {
        return IdUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        IdUsuario = idUsuario;
    }

    public String getNombreUsuario() {
        return NombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        NombreUsuario = nombreUsuario;
    }

    public String getTelefono() {
        return Telefono;
    }

    public void setTelefono(String telefono) {
        Telefono = telefono;
    }
}
