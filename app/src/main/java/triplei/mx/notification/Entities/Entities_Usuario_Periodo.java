package triplei.mx.notification.Entities;

import java.io.Serializable;

public class Entities_Usuario_Periodo implements Serializable {

    public String IdUsuario;
    public String IdPeriodo;
    public Integer Sin_Validar;

    public Entities_Usuario_Periodo(){

    }

    public String getIdUsuario() {
        return IdUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        IdUsuario = idUsuario;
    }

    public String getIdPeriodo() {
        return IdPeriodo;
    }

    public void setIdPeriodo(String idPeriodo) {
        IdPeriodo = idPeriodo;
    }

    public Integer getSin_Validar() {
        return Sin_Validar;
    }

    public void setSin_Validar(Integer sin_Validar) {
        Sin_Validar = sin_Validar;
    }
}
