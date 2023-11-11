package es.ljaraizc.bellotaclim;

public class Material {

    private String tipo;
    private String marca;
    private String modelo;
    private String talla;
    private String nombreEscalador;
    private int imagen;

    public Material() {
    }

    public Material(String tipo, String marca, String modelo, String talla, String nombreEscalador, int imagen) {
        this.tipo = tipo;
        this.marca = marca;
        this.modelo = modelo;
        this.talla = talla;
        this.nombreEscalador = nombreEscalador;
        this.imagen = imagen;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public String getTalla() {
        return talla;
    }

    public void setTalla(String talla) {
        this.talla = talla;
    }

    public String getNombreEscalador() {
        return nombreEscalador;
    }

    public void setNombreEscalador(String nombreEscalador) {
        this.nombreEscalador = nombreEscalador;
    }

    public int getImagen() {
        return imagen;
    }

    public void setImagen(int imagen) {
        this.imagen = imagen;
    }
}