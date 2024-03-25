import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.scheduling.annotation.SchedulingConfiguration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;

import static org.mockito.Mockito.*;

public class SchedulerTest {

    @Mock
    private CertificateController certificateController;

    @InjectMocks
    private Scheduler scheduler;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ThreadPoolTaskScheduler taskScheduler = new ThreadPoolTaskScheduler();
        taskScheduler.initialize();
        scheduler.setTaskScheduler(taskScheduler);
        scheduler.setCronTrigger(new CronTrigger("0 0 * * * *"));
    }

    @Test
    public void testScheduledTask() {
        // Verificar que el método loadAndCheck() se llama durante la tarea programada
        scheduler.schedule();
        verify(certificateController, times(1)).loadAndCheck();
    }
}
