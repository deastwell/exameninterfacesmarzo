package com.example.examen2interfaces;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;

import java.io.Serializable;
import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;

public class HelloController implements Initializable {

    @FXML
    private TextField txtNombre;
    @FXML
    private TextField txtPeso;
    @FXML
    private TextField txtEdad;
    @FXML
    private TextField txtTalla;
    @FXML
    private TextField txtObservaciones;
    @FXML
    private ChoiceBox cbSexo;
    @FXML
    private ChoiceBox cbActividad;
    @FXML
    private Label labelInfo;
    @FXML
    private Button btnGuardar;
    @FXML
    private Button btnDescargar;

    @FXML
    public void guardar(ActionEvent actionEvent) {
        // Verificar qué campos faltan
        StringBuilder camposFaltantes = new StringBuilder();

        if (txtNombre.getText().isEmpty()) {
            camposFaltantes.append("Nombre, ");
        }
        if (txtPeso.getText().isEmpty()) {
            camposFaltantes.append("Peso, ");
        }
        if (txtEdad.getText().isEmpty()) {
            camposFaltantes.append("Edad, ");
        }
        if (txtTalla.getText().isEmpty()) {
            camposFaltantes.append("Talla, ");
        }
        if (cbSexo.getValue() == null) {
            camposFaltantes.append("Sexo, ");
        }
        if (cbActividad.getValue() == null) {
            camposFaltantes.append("Actividad, ");
        }

        // Si hay campos faltantes, mostrar la alerta
        if (camposFaltantes.length() > 0) {
            String mensajeAlerta = "Los siguientes campos son obligatorios: " + camposFaltantes.substring(0, camposFaltantes.length() - 2);
            labelInfo.setText(mensajeAlerta);
        } else {
            // Obtener valores del formulario
            String sexo = (String) cbSexo.getValue();
            int edad = Integer.parseInt(txtEdad.getText());
            int peso = Integer.parseInt(txtPeso.getText());
            int talla = Integer.parseInt(txtTalla.getText());
            String nivelActividad = (String) cbActividad.getValue();

            // Calcular el GER
            double ger;
            if (sexo.equalsIgnoreCase("Hombre")) {
                ger = 66.473 + (13.751 * peso) + (5.003 * talla) - (6.755 * edad);
            } else {
                ger = 655.0955 + (9.453 * peso) + (1.8496 * talla) - (4.6756 * edad);
            }

            // Calcular el factor de actividad
            double factorActividad;
            switch (nivelActividad) {
                case "Sedentario":
                    factorActividad = sexo.equalsIgnoreCase("Hombre") ? 1.3 : 1.3;
                    break;
                case "Moderado":
                    factorActividad = sexo.equalsIgnoreCase("Hombre") ? 1.6 : 1.5;
                    break;
                case "Activo":
                    factorActividad = sexo.equalsIgnoreCase("Hombre") ? 1.7 : 1.6;
                    break;
                case "Muy activo":
                    factorActividad = sexo.equalsIgnoreCase("Hombre") ? 2.1 : 1.9;
                    break;
                default:
                    factorActividad = 1.0;
            }

            // Calcular el GET
            double get = ger * factorActividad;

            // Mostrar el resultado en la interfaz de usuario
            labelInfo.setText("GER: " + ger + ", GET: " + get);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        cbSexo.getItems().addAll("Hombre", "Mujer");

        // Configurar opciones para el ChoiceBox de actividad
        cbActividad.getItems().addAll("Sedentario", "Moderado", "Activo", "Muy activo");

    }

    @FXML
    public void descargar(ActionEvent actionEvent) {
        try {
            JasperPrint jasperPrint = JasperFillManager.fillReport("examenjasper.jasper", new HashMap<>(), MYSQLConnection.getConexion());

            JRPdfExporter exporter = new JRPdfExporter();
            exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
            exporter.setExporterOutput(new SimpleOutputStreamExporterOutput("INFORMECLIENTES.pdf"));
            exporter.exportReport();

            // Notificar al usuario que el archivo se ha descargado
            System.out.println("El informe se ha descargado correctamente como INFORMECLIENTES.pdf");
        } catch (JRException e) {
            e.printStackTrace();
        }
    }


}