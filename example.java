import java.io.FileInputStream;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import org.springframework.core.io.ClassPathResource;

public class PemCertificateReader {

    public static void main(String[] args) {
        try {
            // Ruta del archivo .pem en la carpeta resources
            String pemFilePath = "classpath:archivo.pem";

            // Cargar el archivo .pem
            ClassPathResource resource = new ClassPathResource(pemFilePath);
            FileInputStream fis = new FileInputStream(resource.getFile());

            // Crear un CertificateFactory y leer el certificado X509
            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            X509Certificate cert = (X509Certificate) cf.generateCertificate(fis);

            // Obtener la fecha de expiración del certificado
            System.out.println("Fecha de expiración del certificado: " + cert.getNotAfter());

            // Cerrar el flujo de entrada
            fis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
