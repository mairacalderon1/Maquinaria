
package cl.duoc.maquinaria.repository;

import cl.duoc.maquinaria.conexion.ConexionBD;
import cl.duoc.maquinaria.model.CargadorFrontal;
import cl.duoc.maquinaria.model.Excavadora;
import cl.duoc.maquinaria.model.Maquina;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MaquinaRepository {

    public List<Maquina> obtenerTodos() throws SQLException {

        // La lista se crea nuevamente en cada consulta.
        List<Maquina> maquinas = new ArrayList<>();

        String sql = "SELECT * FROM maquina";

        try (Connection cnx = ConexionBD.getConnection();
             Statement sentencia = cnx.createStatement();
             ResultSet resultado = sentencia.executeQuery(sql)) {

            while (resultado.next()) {

                int id = resultado.getInt("id");
                String codigo = resultado.getString("codigo");
                int horasUso = resultado.getInt("horas_uso");
                double potencia = resultado.getDouble("potencia");

                if (resultado.getInt("id_tipo") == 1) {

                    double pesoToneladas = resultado.getDouble("peso_toneladas");
                    boolean mantencionAlDia = resultado.getBoolean("mantencion_al_dia");
                    boolean certificacion = resultado.getBoolean("certificacion");

                    Excavadora exc = new Excavadora(
                            id, codigo, horasUso, potencia,
                            pesoToneladas, mantencionAlDia, certificacion
                    );

                    maquinas.add(exc);

                } else if (resultado.getInt("id_tipo") == 2) {

                    double capacidadBalde = resultado.getDouble("capacidad_balde");

                    CargadorFrontal cf = new CargadorFrontal(
                            id, codigo, horasUso, potencia, capacidadBalde
                    );

                    maquinas.add(cf);
                }
            }
        }

        return maquinas;
    }

    public void insertar(Maquina maquina) throws SQLException {

        String sql = "INSERT INTO maquina " +
                "(id_tipo, codigo, horas_uso, potencia, peso_toneladas, " +
                "mantencion_al_dia, certificacion, capacidad_balde) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection cnx = ConexionBD.getConnection();
             PreparedStatement sentencia = cnx.prepareStatement(sql)) {

            sentencia.setString(2, maquina.getCodigo());
            sentencia.setInt(3, maquina.getHorasUso());
            sentencia.setDouble(4, maquina.getPotencia());

            if (maquina instanceof Excavadora) {

                Excavadora exc = (Excavadora) maquina;

                sentencia.setInt(1, 1);
                sentencia.setDouble(5, exc.getPesoToneladas());
                sentencia.setBoolean(6, exc.isMantencionAlDia());
                sentencia.setBoolean(7, exc.isCertificacion());
                sentencia.setNull(8, Types.DECIMAL);

            } else if (maquina instanceof CargadorFrontal) {

                CargadorFrontal cf = (CargadorFrontal) maquina;

                sentencia.setInt(1, 2);
                sentencia.setNull(5, Types.DECIMAL);
                sentencia.setNull(6, Types.BOOLEAN);
                sentencia.setNull(7, Types.BOOLEAN);
                sentencia.setDouble(8, cf.getCapacidadBalde());

            } else {
                throw new IllegalArgumentException("Tipo de máquina no válido.");
            }

            sentencia.executeUpdate();
        }
    }

    public void actualizar(Maquina maquina) throws SQLException {

        String sql = "UPDATE maquina SET " +
                "codigo = ?, horas_uso = ?, potencia = ?, " +
                "peso_toneladas = ?, mantencion_al_dia = ?, " +
                "certificacion = ?, capacidad_balde = ? " +
                "WHERE id = ?";

        try (Connection cnx = ConexionBD.getConnection();
             PreparedStatement sentencia = cnx.prepareStatement(sql)) {

            // Parámetros comunes
            sentencia.setString(1, maquina.getCodigo());
            sentencia.setInt(2, maquina.getHorasUso());
            sentencia.setDouble(3, maquina.getPotencia());

            if (maquina instanceof Excavadora) {

                Excavadora exc = (Excavadora) maquina;

                sentencia.setDouble(4, exc.getPesoToneladas());
                sentencia.setBoolean(5, exc.isMantencionAlDia());
                sentencia.setBoolean(6, exc.isCertificacion());
                sentencia.setNull(7, Types.DECIMAL);

            } else if (maquina instanceof CargadorFrontal) {

                CargadorFrontal cf = (CargadorFrontal) maquina;

                sentencia.setNull(4, Types.DECIMAL);
                sentencia.setNull(5, Types.BOOLEAN);
                sentencia.setNull(6, Types.BOOLEAN);
                sentencia.setDouble(7, cf.getCapacidadBalde());

            } else {
                throw new IllegalArgumentException("Tipo de máquina no válido.");
            }

            // Identificador del registro que se actualizará
            sentencia.setInt(8, maquina.getId());

            sentencia.executeUpdate();
        }
    }

    public void eliminar(int id) throws SQLException {

        String sql = "DELETE FROM maquina WHERE id = ?";

        try (Connection cnx = ConexionBD.getConnection();
             PreparedStatement sentencia = cnx.prepareStatement(sql)) {

            sentencia.setInt(1, id);

            sentencia.executeUpdate();
        }
    }
}
