package com.inmobiliaria.comprobante.gestion_comprobantes.service;

import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

/**
 * Genera copias de seguridad de la base H2 usando el comando nativo
 * "BACKUP TO", que produce un archivo consistente aunque la base esté
 * en uso (no corrompe nada, a diferencia de copiar el .mv.db a mano).
 */
@Service
public class BackupService {

    // --- Configuración de backups ---
    // Carpeta donde se guardan las copias, al lado de la base de datos.
    private static final String CARPETA_BACKUPS = "./data/backups";

    // Cuántas copias viejas conservamos antes de empezar a borrar las más antiguas.
    private static final int CANTIDAD_BACKUPS_A_CONSERVAR = 15;

    private static final String PREFIJO_ARCHIVO = "backup_inmobiliaria_";
    private static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("yyyy-MM-dd_HHmmss");

    private final DataSource dataSource;

    public BackupService(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * Genera el backup del momento y, si ya hay más de
     * CANTIDAD_BACKUPS_A_CONSERVAR copias guardadas, borra las más viejas.
     */
    public void realizarBackup() {
        try {
            Files.createDirectories(Paths.get(CARPETA_BACKUPS));

            String nombreArchivo = PREFIJO_ARCHIVO + LocalDateTime.now().format(FORMATO_FECHA) + ".zip";
            Path destino = Paths.get(CARPETA_BACKUPS, nombreArchivo);

            try (Connection conn = dataSource.getConnection();
                 Statement stmt = conn.createStatement()) {
                // H2 escapa la ruta con comillas simples; duplicamos cualquier
                // comilla simple que pudiera venir en el path por seguridad.
                String rutaEscapada = destino.toString().replace("'", "''");
                stmt.execute("BACKUP TO '" + rutaEscapada + "'");
            }

            System.out.println("[Backup] Copia generada: " + destino.toAbsolutePath());
            limpiarBackupsViejos();

        } catch (SQLException | IOException e) {
            // Un fallo de backup no debe frenar el cierre de la aplicación.
            System.err.println("[Backup] No se pudo generar la copia de seguridad: " + e.getMessage());
        }
    }

    private void limpiarBackupsViejos() throws IOException {
        try (Stream<Path> archivos = Files.list(Paths.get(CARPETA_BACKUPS))) {
            List<Path> backups = archivos
                    .filter(p -> p.getFileName().toString().startsWith(PREFIJO_ARCHIVO))
                    .sorted(Comparator.comparing(p -> p.getFileName().toString()))
                    .toList();

            int sobrantes = backups.size() - CANTIDAD_BACKUPS_A_CONSERVAR;
            for (int i = 0; i < sobrantes; i++) {
                Files.deleteIfExists(backups.get(i));
                System.out.println("[Backup] Copia vieja eliminada: " + backups.get(i).getFileName());
            }
        }
    }
}
