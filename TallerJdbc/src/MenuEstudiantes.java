// Teller JDBC 
// Oscar David Castillo y 
// Gabriel Elías Valdelamar Caldera

import java.sql.*;
import java.util.Scanner;

public class MenuEstudiantes {

    private static final String URL = "jdbc:postgresql://localhost:5432/escuela";
    private static final String USER = "postgres";
    private static final String PASSWORD = "1234";

    private static Connection conectar() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    private static void insertarEstudiante(Scanner sc) {
        System.out.print("Nombre: ");
        String nombre = sc.nextLine();

        System.out.print("Apellido: ");
        String apellido = sc.nextLine();

        System.out.print("Correo: ");
        String correo = sc.nextLine();

        System.out.print("Edad: ");
        int edad = Integer.parseInt(sc.nextLine());

        System.out.print("Estado civil (SOLTERO, CASADO, VIUDO, UNION_LIBRE, DIVORCIADO): ");
        String estadoCivil = sc.nextLine().toUpperCase();

        String sql = "INSERT INTO Estudiantes  (nombre, apellido, correo, edad, estado_civil) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = conectar(); PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setString(1, nombre);
            pst.setString(2, apellido);
            pst.setString(3, correo);
            pst.setInt(4, edad);
            pst.setString(5, estadoCivil);

            pst.executeUpdate();
            System.out.println("Estudiante insertado correctamente.");
        } catch (SQLException e) {
            System.out.println("Error al insertar estudiante: " + e.getMessage());
        }
    }

    private static void actualizarEstudiante(Scanner sc) {
        System.out.print("Correo del estudiante a actualizar: ");
        String correo = sc.nextLine();

        System.out.print("Nuevo nombre: ");
        String nombre = sc.nextLine();

        System.out.print("Nuevo apellido: ");
        String apellido = sc.nextLine();

        System.out.print("Nueva edad: ");
        int edad = Integer.parseInt(sc.nextLine());

        System.out.print("Nuevo estado civil (SOLTERO, CASADO, VIUDO, UNION_LIBRE, DIVORCIADO): ");
        String estadoCivil = sc.nextLine().toUpperCase();

        String sql = "UPDATE Estudiantes SET nombre = ?, apellido = ?, edad = ?, estado_civil = ? WHERE correo = ?";

        try (Connection conn = conectar(); PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setString(1, nombre);
            pst.setString(2, apellido);
            pst.setInt(3, edad);
            pst.setString(4, estadoCivil);
            pst.setString(5, correo);

            int filas = pst.executeUpdate();
            if (filas > 0) {
                System.out.println("Estudiante actualizado correctamente.");
            } else {
                System.out.println("No se encontró estudiante con ese correo.");
            }
        } catch (SQLException e) {
            System.out.println("Error al actualizar estudiante: " + e.getMessage());
        }
    }

    private static void eliminarEstudiante(Scanner sc) {
        System.out.print("Correo del estudiante a eliminar: ");
        String correo = sc.nextLine();

        String sql = "DELETE FROM Estudiantes WHERE correo = ?";

        try (Connection conn = conectar(); PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setString(1, correo);

            int filas = pst.executeUpdate();
            if (filas > 0) {
                System.out.println("Estudiante eliminado correctamente.");
            } else {
                System.out.println("No se encontró estudiante con ese correo.");
            }
        } catch (SQLException e) {
            System.out.println("Error al eliminar estudiante: " + e.getMessage());
        }
    }

    private static void consultarTodos() {
        String sql = "SELECT * FROM Estudiantes";

        try (Connection conn = conectar(); Statement st = conn.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                System.out.printf("ID: %d, Nombre: %s, Apellido: %s, Correo: %s, Edad: %d, Estado Civil: %s\n",
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getString("apellido"),
                        rs.getString("correo"),
                        rs.getInt("edad"),
                        rs.getString("estado_civil"));
            }
        } catch (SQLException e) {
            System.out.println("Error al consultar Estudiantes: " + e.getMessage());
        }
    }

    private static void consultarPorEmail(Scanner sc) {
        System.out.print("Correo del estudiante a buscar: ");
        String correo = sc.nextLine();

        String sql = "SELECT * FROM Estudiantes WHERE correo = ?";

        try (Connection conn = conectar(); PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setString(1, correo);

            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    System.out.printf("ID: %d, Nombre: %s, Apellido: %s, Correo: %s, Edad: %d, Estado Civil: %s\n",
                            rs.getInt("id"),
                            rs.getString("nombre"),
                            rs.getString("apellido"),
                            rs.getString("correo"),
                            rs.getInt("edad"),
                            rs.getString("estado_civil"));
                } else {
                    System.out.println("No se encontró estudiante con ese correo.");
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al consultar estudiante: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String opcion;

        do {
            System.out.println("\nMenu:");
            System.out.println("1. Insertar Estudiante");
            System.out.println("2. Actualizar Estudiante");
            System.out.println("3. Eliminar Estudiante");
            System.out.println("4. Consultar todos los Estudiantes");
            System.out.println("5. Consultar Estudiante por email");
            System.out.println("6. Salir");
            System.out.print("Elige una opción: ");

            opcion = sc.nextLine();

            switch (opcion) {
                case "1":
                    insertarEstudiante(sc);
                    break;
                case "2":
                    actualizarEstudiante(sc);
                    break;
                case "3":
                    eliminarEstudiante(sc);
                    break;
                case "4":
                    consultarTodos();
                    break;
                case "5":
                    consultarPorEmail(sc);
                    break;
                case "6":
                    System.out.println("Saliendo del programa...");
                    break;
                default:
                    System.out.println("Opción no válida, intenta de nuevo.");
            }
        } while (!opcion.equals("6"));

        sc.close();
    }
}
