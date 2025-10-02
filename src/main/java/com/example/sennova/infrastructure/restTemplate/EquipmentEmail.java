package com.example.sennova.infrastructure.restTemplate;

import com.example.sennova.domain.model.EquipmentModel;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EquipmentEmail {
    private final JavaMailSender mailSender;

    public EquipmentEmail(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendEmailMaintenance(String to, List<EquipmentModel> equipos) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom("no-reply@sennova.com");
            helper.setTo(to);
            helper.setSubject("🛠️ Equipos que requieren mantenimiento");

            // Construir HTML con lista de equipos
            StringBuilder equipmentListHtml = new StringBuilder("<ul>");
            for (EquipmentModel equipo : equipos) {
                equipmentListHtml.append("<li>")
                        .append("Código: ").append(equipo.getInternalCode())
                        .append(" - Nombre: ").append(equipo.getEquipmentName())
                        .append("</li>");
            }
            equipmentListHtml.append("</ul>");

            String htmlContent = """
                    <div style="font-family: Arial, sans-serif; color: #333;">
                        <h2 style="color: #2196F3;">🔧 Lista de Equipos para Mantenimiento</h2>
                        <p>Hola, esta es la lista de equipos que requieren mantenimiento:</p>
                        %s
                        <p style="font-size: 12px; color: #888;">
                            Este mensaje es automático, no respondas.
                        </p>
                    </div>
                    """.formatted(equipmentListHtml.toString());

            helper.setText(htmlContent, true);
            mailSender.send(message);
            System.out.println("Correo enviado a " + to);

        } catch (MessagingException e) {
            throw new RuntimeException("Error enviando el correo de mantenimiento", e);
        }
    }


}
