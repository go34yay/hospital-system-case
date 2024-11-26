package hospitalsystem.persistence;

import hospitalsystem.persistence.service.HospitalService;
import hospitalsystem.persistence.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

@SpringBootApplication
public class HospitalSystemApplication {
	@Autowired
	PatientService patientService;
	@Autowired
	HospitalService hospitalService;

	public static void main(String[] args) {
		SpringApplication.run(HospitalSystemApplication.class, args);
	}

	@EventListener(ApplicationReadyEvent.class)
	public void execCodeAfterStartup() {
		System.out.println("The app is running...");
	}
}
