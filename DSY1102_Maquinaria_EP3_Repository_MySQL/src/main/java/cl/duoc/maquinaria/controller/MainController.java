package cl.duoc.maquinaria.controller;

import cl.duoc.maquinaria.repository.MaquinaRepository;
import java.sql.SQLException;
import cl.duoc.maquinaria.model.Maquina;
import cl.duoc.maquinaria.model.Excavadora;
import cl.duoc.maquinaria.model.CargadorFrontal;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import java.util.Locale;


public class MainController {

    // Repositorio: una sola clase encargada de ejecutar las consultas SQL.
    private final MaquinaRepository repositorio = new MaquinaRepository();

    private static final Locale LOCALE_CL = Locale.forLanguageTag("es-CL");

    @FXML private ComboBox<String> cbTipo;
    @FXML private TextField txtCodigo;
    @FXML private TextField txtHorasUso;
    @FXML private TextField txtPotencia;
    @FXML private TextField txtPesoToneladas;
    @FXML private CheckBox chkMantencionAlDia;
    @FXML private CheckBox chkCertificacion;
    @FXML private TextField txtCapacidadBalde;
    @FXML private Button btnRegistrar;
    @FXML private Button btnCancelar;

    @FXML private TableView<Maquina> tabla;
    @FXML private TableColumn<Maquina, Integer> colId;
    @FXML private TableColumn<Maquina, String> colTipo;
    @FXML private TableColumn<Maquina, String> colCodigo;
    @FXML private TableColumn<Maquina, Integer> colHorasUso;
    @FXML private TableColumn<Maquina, Double> colPotencia;
    @FXML private TableColumn<Maquina, String> colCosto;

    @FXML private Label lblMensaje;

    /** Registro que se está editando; null cuando el formulario registra uno nuevo. */
    private Maquina enEdicion;

    @FXML
    private void initialize() {
        cbTipo.setItems(FXCollections.observableArrayList(Excavadora.TIPO, CargadorFrontal.TIPO));
        cbTipo.valueProperty().addListener((obs, anterior, actual) -> actualizarCamposPorTipo());

        colId.setCellValueFactory(c -> new SimpleObjectProperty<>(c.getValue().getId()));
        colTipo.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getTipo()));
        colCodigo.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getCodigo()));
        colHorasUso.setCellValueFactory(c -> new SimpleObjectProperty<>(c.getValue().getHorasUso()));
        colPotencia.setCellValueFactory(c -> new SimpleObjectProperty<>(c.getValue().getPotencia()));
        colCosto.setCellValueFactory(c ->
                new SimpleStringProperty(formatearCosto(c.getValue().calcularCostoServicio())));
        tabla.setPlaceholder(new Label("No hay máquinas para mostrar."));

        limpiarFormulario();
        cargarTabla();
    }

    // ---------------------------------------------------------------- acciones

    @FXML
    private void onRegistrar() {
                Maquina maquina;
        try {
            maquina = crearDesdeFormulario();
        } catch (IllegalArgumentException e) {
            mostrarError(e.getMessage());
            return;
        }

        try {
            if (enEdicion == null) {
                repositorio.insertar(maquina);
                cargarTabla();
                limpiarFormulario();
                mostrarExito(describir(maquina) + " registrada correctamente.");
            } else {
                maquina.setId(enEdicion.getId());
                repositorio.actualizar(maquina);
                cargarTabla();
                limpiarFormulario();
                mostrarExito(describir(maquina) + " actualizada correctamente.");
            }
        } catch (SQLException e) {
            mostrarError(e.getMessage());
        }
    }

    @FXML
    private void onEditar() {
        Maquina seleccionada = tabla.getSelectionModel().getSelectedItem();
        if (seleccionada == null) {
            mostrarError("Selecciona una máquina de la tabla para editar.");
            return;
        }
        enEdicion = seleccionada;
        cbTipo.setValue(seleccionada.getTipo());
        cbTipo.setDisable(true);
        txtCodigo.setText(seleccionada.getCodigo());
        txtHorasUso.setText(String.valueOf(seleccionada.getHorasUso()));
        txtPotencia.setText(String.valueOf(seleccionada.getPotencia()));
        if (seleccionada instanceof Excavadora excavadora) {
            txtPesoToneladas.setText(String.valueOf(excavadora.getPesoToneladas()));
            chkMantencionAlDia.setSelected(excavadora.isMantencionAlDia());
            chkCertificacion.setSelected(excavadora.isCertificacion());
        } else if (seleccionada instanceof CargadorFrontal cargadorFrontal) {
            txtCapacidadBalde.setText(String.valueOf(cargadorFrontal.getCapacidadBalde()));
        }
        btnRegistrar.setText("Guardar cambios");
        btnCancelar.setDisable(false);
        txtCodigo.requestFocus();
        mostrarInfo("Editando " + describir(seleccionada)
                + ". Modifica los datos y presiona Guardar cambios.");
    }

    @FXML
    private void onCancelar() {
        limpiarFormulario();
        mostrarInfo("Edición cancelada.");
    }

    @FXML
    private void onEliminar() {
                Maquina seleccionada = tabla.getSelectionModel().getSelectedItem();
        if (seleccionada == null) {
            mostrarError("Selecciona una máquina de la tabla para eliminar.");
            return;
        }
        try {
            repositorio.eliminar(seleccionada.getId());
            if (enEdicion != null && enEdicion.getId() == seleccionada.getId()) {
                limpiarFormulario();
            }
            cargarTabla();
            mostrarExito(describir(seleccionada) + " eliminada correctamente.");
        } catch (SQLException e) {
            mostrarError(e.getMessage());
        }
    }

    // ---------------------------------------------------------------- apoyo

    /** Vuelve a leer todos los registros desde el almacenamiento y los muestra en la tabla. */
    private void cargarTabla() {
        try {
            tabla.setItems(FXCollections.observableArrayList(repositorio.obtenerTodos()));
        } catch (SQLException e) {
            tabla.getItems().clear();
            mostrarError("Error al leer la información: " + e.getMessage());
        }
    }

    private Maquina crearDesdeFormulario() {
        String tipo = cbTipo.getValue();
        if (tipo == null) {
            throw new IllegalArgumentException("Selecciona el tipo.");
        }
        String codigo = txtCodigo.getText();
        int horasUso = leerEntero(txtHorasUso.getText(), "Horas de uso: debe ser un número entero.");
        double potencia = leerDecimal(txtPotencia.getText(), "Potencia: debe ser un número.");

        if (Excavadora.TIPO.equals(tipo)) {
            return new Excavadora(codigo, horasUso, potencia, leerDecimal(txtPesoToneladas.getText(), "Peso: debe ser un número."), chkMantencionAlDia.isSelected(), chkCertificacion.isSelected());
        }
        return new CargadorFrontal(codigo, horasUso, potencia, leerDecimal(txtCapacidadBalde.getText(), "Capacidad balde: debe ser un número."));
    }

    private int leerEntero(String texto, String mensajeError) {
        try {
            return Integer.parseInt(texto.trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(mensajeError);
        }
    }

    private double leerDecimal(String texto, String mensajeError) {
        try {
            return Double.parseDouble(texto.trim().replace(',', '.'));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(mensajeError);
        }
    }

    private void actualizarCamposPorTipo() {
        boolean esTipo1 = Excavadora.TIPO.equals(cbTipo.getValue());
        boolean esTipo2 = CargadorFrontal.TIPO.equals(cbTipo.getValue());
        txtPesoToneladas.setDisable(!esTipo1);
        chkMantencionAlDia.setDisable(!esTipo1);
        chkCertificacion.setDisable(!esTipo1);
        txtCapacidadBalde.setDisable(!esTipo2);
    }

    private void limpiarFormulario() {
        enEdicion = null;
        cbTipo.setDisable(false);
        cbTipo.setValue(null);
        txtCodigo.clear();
        txtHorasUso.clear();
        txtPotencia.clear();
        txtPesoToneladas.clear();
        chkMantencionAlDia.setSelected(false);
        chkCertificacion.setSelected(false);
        txtCapacidadBalde.clear();
        btnRegistrar.setText("Registrar");
        btnCancelar.setDisable(true);
        actualizarCamposPorTipo();
        tabla.getSelectionModel().clearSelection();
    }

    private String describir(Maquina registro) {
        return registro.getCodigo() + " (" + registro.getTipo() + ")";
    }

    private String formatearCosto(double costo) {
        return String.format(LOCALE_CL, "$%,.0f", costo);
    }

    private void mostrarExito(String mensaje) {
        mostrarMensaje(mensaje, "#eef5ec", "#a9cfa1", "#1e4620");
    }

    private void mostrarInfo(String mensaje) {
        mostrarMensaje(mensaje, "#eef3f8", "#a8c1d9", "#1f3a56");
    }

    private void mostrarError(String mensaje) {
        mostrarMensaje(mensaje, "#fbeeee", "#e0a3a3", "#8a1f1f");
    }

    private void mostrarMensaje(String mensaje, String fondo, String borde, String texto) {
        lblMensaje.setStyle("-fx-padding: 8 10 8 10; -fx-background-color: " + fondo
                + "; -fx-border-color: " + borde + "; -fx-text-fill: " + texto + ";");
        lblMensaje.setText(mensaje);
    }
}
