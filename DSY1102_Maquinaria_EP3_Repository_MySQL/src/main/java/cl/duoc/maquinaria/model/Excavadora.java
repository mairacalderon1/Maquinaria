package cl.duoc.maquinaria.model;


public class Excavadora extends Maquina implements Certificable {

    public static final String TIPO = "Excavadora";
    private static final double COSTO_BASE = 150000;
    private static final double RECARGO = 0.25;

    private double pesoToneladas;
    private boolean mantencionAlDia;
    private boolean certificacion;

    public Excavadora(String codigo, int horasUso, double potencia, double pesoToneladas, boolean mantencionAlDia, boolean certificacion) {
        super(codigo, horasUso, potencia);
        setPesoToneladas(pesoToneladas);
        this.mantencionAlDia = mantencionAlDia;
        this.certificacion = certificacion;
    }

    public Excavadora(int id, String codigo, int horasUso, double potencia, double pesoToneladas,
                      boolean mantencionAlDia, boolean certificacion) {
        super(id, codigo, horasUso, potencia);
        setPesoToneladas(pesoToneladas);
        this.mantencionAlDia = mantencionAlDia;
        this.certificacion = certificacion;
    }

    public double getPesoToneladas() {
        return pesoToneladas;
    }

    public void setPesoToneladas(double pesoToneladas) {
        if (pesoToneladas <= 0) {
            throw new IllegalArgumentException("El peso debe ser mayor que cero.");
        }
        this.pesoToneladas = pesoToneladas;
    }

    public boolean isMantencionAlDia() {
        return mantencionAlDia;
    }

    public void setMantencionAlDia(boolean mantencionAlDia) {
        this.mantencionAlDia = mantencionAlDia;
    }

    @Override
    public boolean isCertificacion() {
        return certificacion;
    }

    @Override
    public void setCertificacion(boolean certificacion) {
        this.certificacion = certificacion;
    }

    @Override
    public String getTipo() {
        return TIPO;
    }

    @Override
    public double calcularCostoServicio() {
        double costo = COSTO_BASE;
        if (!mantencionAlDia) {
            costo += COSTO_BASE * RECARGO;
        }
        return costo;
    }
}
