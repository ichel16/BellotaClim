package es.ljaraizc.bellotaclim;

public class Material {

    private String idMaterial;
    private String tipo;
    private String marca;
    private String modelo;
    private String talla;
    private String emailEscalador;
    private int imagen;

    public Material() {
    }

    public Material(String idMaterial, String tipo, String marca, String modelo, String talla, String emailEscalador, int imagen) {
        this.idMaterial = idMaterial;
        this.tipo = tipo;
        this.marca = marca;
        this.modelo = modelo;
        this.talla = talla;
        this.emailEscalador = emailEscalador;
        this.imagen = imagen;
    }

    public String getIdMaterial(){return idMaterial; }

    public void setIdMaterial(String idMaterial){this.idMaterial = idMaterial; }

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

    public String getEmailEscalador() {
        return emailEscalador;
    }

    public void setEmailEscalador(String emailEscalador) {
        this.emailEscalador = emailEscalador;
    }

    public int getImagen() {
        return imagen;
    }

    public void setImagen(int imagen) {
        this.imagen = imagen;
    }
}
