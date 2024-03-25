import org.junit.jupiter.api.Test;

import java.io.File;
import java.security.KeyStoreException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CertificateReaderTest {

    @Test
    public void testP12() throws Exception {
        // Configurar mocks necesarios para la prueba
        File mockFile = mock(File.class);
        when(mockFile.getPath()).thenReturn("mockPath.p12");

        X509Certificate mockCertificate = mock(X509Certificate.class);
        when(mockCertificate.getSubjectX500Principal().getName()).thenReturn("CN=Mock");
        when(mockCertificate.getNotAfter()).thenReturn(new Date(System.currentTimeMillis() + 1000)); // Expira en 1 segundo

        CertificateReader certificateReader = new CertificateReader();
        certificateReader.readCert(mockFile, "mockPassword");

        assertEquals(1, certificateReader.getListDataRead().size());
        assertEquals("Mock", certificateReader.getHost());
        assertTrue(certificateReader.getRemaining() > 0);
    }

    @Test
    public void testCrt() throws Exception {
        // Similar a testP12(), pero para archivos .crt
    }

    @Test
    public void testPem() throws Exception {
        // Similar a testP12(), pero para archivos .pem
    }

    // Agregar más pruebas según sea necesario para cubrir los casos de borde y el comportamiento esperado
}
