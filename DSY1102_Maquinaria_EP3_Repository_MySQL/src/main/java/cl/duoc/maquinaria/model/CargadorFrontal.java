package cl.duoc.maquinaria.model;


public class CargadorFrontal extends Maquina {

    public static final String TIPO = "Cargador Frontal";
    private static final double COSTO_BASE = 120000;
    private static final double RECARGO = 0.2;

    private double capacidadBalde;

    public CargadorFrontal(String codigo, int horasUso, double potencia, double capacidadBalde) {
        super(codigo, horasUso, potencia);
        setCapacidadBalde(capacidadBalde);
    }

    public CargadorFrontal(int id, String codigo, int horasUso, double potencia, double capacidadBalde) {
        super(id, codigo, horasUso, potencia);
        setCapacidadBalde(capacidadBalde);
    }

    public double getCapacidadBalde() {
        return capacidadBalde;
    }

    public void setCapacidadBalde(double capacidadBalde) {
        if (capacidadBalde <= 0) {
            throw new IllegalArgumentException("La capacidad del balde debe ser mayor que cero.");
        }
        this.capacidadBalde = capacidadBalde;
    }

    @Override
    public String getTipo() {
        return TIPO;
    }

    @Override
    public double calcularCostoServicio() {
        double costo = COSTO_BASE;
        if (capacidadBalde > 3) {
            costo += COSTO_BASE * RECARGO;
        }
        return costo;
    }
}
