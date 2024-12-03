package hospitalsystem.persistence;

import hospitalsystem.persistence.entity.Hospital;
import hospitalsystem.persistence.entity.Patient;
import hospitalsystem.persistence.service.HospitalService;
import hospitalsystem.persistence.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

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

		Hospital hospital1 = new Hospital("Klinikum", " ", "1523");
		Hospital hospital2 = new Hospital("Center", " ", "1253");

		hospitalService.saveHospital(hospital1);
		hospitalService.saveHospital(hospital2);

		Patient patient1 = new Patient("Max", "Tum", new Date(), "max.tum@tum.de");
		Patient patient2 = new Patient("Felix", "Tum", new Date(), "felix.tum@tum.de");

		patientService.savePatient(patient1);
		patientService.savePatient(patient2);

		patientService.registerPatient(patient1, hospital1);
		patientService.registerPatient(patient1, hospital2);
		patientService.registerPatient(patient2, hospital1);

		patientService.savePatient(patient1);
		patientService.savePatient(patient2);



	}
}
