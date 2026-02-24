package com.inmobiliaria.comprobante.gestion_comprobantes;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.awt.*;

@SpringBootApplication
public class GestionComprobantesApplication {

    public static void main(String[] args) {
        // Configuramos para que Java permita interfaces gráficas (necesario para el Tray)
        System.setProperty("java.awt.headless", "false");

        ConfigurableApplicationContext context = SpringApplication.run(GestionComprobantesApplication.class, args);

        configurarTrayIcon();
    }

    private static void configurarTrayIcon() {
        if (!SystemTray.isSupported()) return;

        try {
            SystemTray tray = SystemTray.getSystemTray();

            // Buscamos el recurso
            java.net.URL imageURL = GestionComprobantesApplication.class.getResource("/static/logo.png");
            Image image;

            if (imageURL != null) {
                // Si lo encuentra, lo carga
                image = Toolkit.getDefaultToolkit().createImage(imageURL);
            } else {
                // Si NO lo encuentra, crea un puntito rojo para que NO de NullPointerException
                image = new java.awt.image.BufferedImage(16, 16, java.awt.image.BufferedImage.TYPE_INT_RGB);
                Graphics g = image.getGraphics();
                g.setColor(Color.RED);
                g.fillRect(0, 0, 16, 16);
                g.dispose();
                System.out.println("ADVERTENCIA: No se encontró logo.png en /static/");
            }

            PopupMenu menu = new PopupMenu();

            MenuItem abrirItem = new MenuItem("Abrir InmoDoc");
            abrirItem.addActionListener(e -> abrirNavegador());
            menu.add(abrirItem);

            menu.addSeparator();

            MenuItem salirItem = new MenuItem("Cerrar Sistema");
            salirItem.addActionListener(e -> System.exit(0));
            menu.add(salirItem);

            TrayIcon trayIcon = new TrayIcon(image, "InmoDoc - Lily Cirigliano", menu);
            trayIcon.setImageAutoSize(true);

            tray.add(trayIcon);

        } catch (Exception e) {
            System.err.println("Error al iniciar el Tray: " + e.getMessage());
        }
    }

    private static void abrirNavegador() {
        try {
            java.awt.Desktop.getDesktop().browse(new java.net.URI("http://localhost:8080"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
