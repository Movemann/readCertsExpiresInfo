import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.ArrayList;

import static org.mockito.Mockito.*;

public class CertificateLoaderTest {

    @Test
    public void testLoadCerts() {
        // Configurar mocks necesarios para la prueba
        CertificateReader certificateReader = mock(CertificateReader.class);
        CertificateLoader certificateLoader = new CertificateLoader();
        certificateLoader.setCertificateReader(certificateReader);

        // Simular lista de archivos
        File mockFile1 = mock(File.class);
        File mockFile2 = mock(File.class);
        File[] files = {mockFile1, mockFile2};

        // Ejecutar el método bajo prueba
        certificateLoader.processFiles(files, "mockPassword");

        // Verificar que se llama al método readCert() de CertificateReader para cada archivo
        verify(certificateReader, times(1)).readCert(mockFile1, "mockPassword");
        verify(certificateReader, times(1)).readCert(mockFile2, "mockPassword");
    }
}
