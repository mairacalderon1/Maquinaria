package cl.duoc.maquinaria.model;


public abstract class Maquina {

    private int id;
    private String codigo;
    private int horasUso;
    private double potencia;

    protected Maquina(String codigo, int horasUso, double potencia) {
        setCodigo(codigo);
        setHorasUso(horasUso);
        setPotencia(potencia);
    }

    protected Maquina(int id, String codigo, int horasUso, double potencia) {
        setId(id);
        setCodigo(codigo);
        setHorasUso(horasUso);
        setPotencia(potencia);
    }

    /** @return identificador (0 si aún no ha sido almacenado). */
    public int getId() {
        return id;
    }

    /** @param id identificador asignado al almacenar el registro (no negativo). */
    public void setId(int id) {
        if (id < 0) {
            throw new IllegalArgumentException("El identificador no puede ser negativo.");
        }
        this.id = id;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        if (codigo == null || codigo.isBlank()) {
            throw new IllegalArgumentException("El código no puede estar vacío.");
        }
        this.codigo = codigo.trim();
    }

    public int getHorasUso() {
        return horasUso;
    }

    public void setHorasUso(int horasUso) {
        if (horasUso < 0 || horasUso > 20000) {
            throw new IllegalArgumentException("Las horas de uso deben estar entre 0 y 20000.");
        }
        this.horasUso = horasUso;
    }

    public double getPotencia() {
        return potencia;
    }

    public void setPotencia(double potencia) {
        if (potencia <= 0) {
            throw new IllegalArgumentException("La potencia debe ser mayor que cero.");
        }
        this.potencia = potencia;
    }

    /** @return tipo del registro: "Excavadora" o "Cargador Frontal". */
    public abstract String getTipo();

    /** @return costo del servicio, en pesos. */
    public abstract double calcularCostoServicio();
}
