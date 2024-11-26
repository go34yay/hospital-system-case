package hospitalsystem.HospitalSystem;

import hospitalsystem.persistence.HospitalSystemApplication;
import hospitalsystem.persistence.entity.Hospital;
import hospitalsystem.persistence.entity.Patient;
import hospitalsystem.persistence.repository.HospitalRepository;
import hospitalsystem.persistence.repository.PatientRepository;
import hospitalsystem.persistence.service.HospitalService;
import hospitalsystem.persistence.service.PatientService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.Date;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = HospitalSystemApplication.class)
class HospitalSystemApplicationTests {

	@Autowired
	private PatientService patientService;
	@Autowired
	private HospitalService hospitalService;

	@Test
	void testCreatePatient() {
		Patient patient = patientService.createPatient("Max", "Tum", new Date(), "max.tum@tum.de");

		assertNotNull(patient);
		assertEquals("Max", patient.getFirstName());
		assertEquals("Tum", patient.getLastName());
		assertEquals("max.tum@tum.de", patient.getEmail());
	}

	@Test
	void testCreateHospital() {
		Hospital hospital = hospitalService.createHospital("TUM Klinikum", "Ismaninger Straße 22", "123-456-789");

		assertNotNull(hospital);
		assertEquals("TUM Klinikum", hospital.getName());
		assertEquals("Ismaninger Straße 22", hospital.getAddress());
		assertEquals("123-456-789", hospital.getPhone());
	}

	@Test
	void testUpdatePatient() {
		Patient patient = patientService.createPatient("Max", "Tum", new Date(), "max.tum@tum.de");
		patientService.updatePatientById(patient.getId(), "Max", "Tum","max.tum@gmail.com");
		Patient updatedPatient = patientService.findPatientById(patient.getId());

		assertNotNull(updatedPatient);
		assertNotEquals("max.tum@tum.de", updatedPatient.getEmail());
		assertEquals("max.tum@gmail.com", updatedPatient.getEmail());
	}

	@Test
	void testUpdateHospital() {
		Hospital hospital = hospitalService.createHospital("TUM Klinikum", "Ismaninger Straße 22", "123-456-789");
		hospitalService.updateHospitalById(hospital.getId(), "TUM Klinikum", "Ismaninger Straße 22", "123-456-799");
		Hospital updatedHospital = hospitalService.findHospitalById(hospital.getId());

		assertNotNull(updatedHospital);
		assertNotEquals("123-456-789", updatedHospital.getPhone());
		assertEquals("123-456-799", updatedHospital.getPhone());
	}

	@Test
	void testDeletePatient() {
		Patient patient = patientService.createPatient("Max", "Tum", new Date(), "max.tum@tum.de");
		int id = patient.getId();
		patientService.deletePatientById(id);

		assertNull(patientService.findPatientById(id));
	}

	@Test
	void testDeleteHospital() {
		Hospital hospital = hospitalService.createHospital("TUM Klinikum", "Ismaninger Straße 22", "123-456-789");
		int id = hospital.getId();
		hospitalService.deleteHospitalById(id);

		assertNull(hospitalService.findHospitalById(id));
	}

	@Test
	void testRegisterPatient() {
		Patient patient = patientService.createPatient("Max", "Tum", new Date(), "max.tum@tum.de");
		Hospital hospital = hospitalService.createHospital("TUM Klinikum", "Ismaninger Straße 22", "123-456-789");

		patientService.registerPatient(patient, hospital);

		assertTrue(hospital.getPatients().contains(patient));
		assertTrue(patient.getHospitals().contains(hospital));
	}

	@Test
	void testPatientsOfHospital() {
		Hospital hospital = hospitalService.createHospital("TUM Klinikum", "Ismaninger Straße 22", "123-456-789");
		Patient patient1 = patientService.createPatient("Max", "Tum", new Date(), "max.tum@tum.de");
		Patient patient2 = patientService.createPatient("Felix", "Mann", new Date(), "felix.mann@tum.de");

		patientService.registerPatient(patient1, hospital);
		patientService.registerPatient(patient2, hospital);

		assertEquals(2, hospital.getPatients().size());
	}

	@Test
	void testHospitalsOfPatient() {
		Patient patient = patientService.createPatient("Max", "Tum", new Date(), "max.tum@tum.de");
		Hospital hospital1 = hospitalService.createHospital("TUM Klinikum", "Ismaninger Straße 22", "123-456-789");
		Hospital hospital2 = hospitalService.createHospital("LMU Klinikum", "Garchinger Straße 33", "789-456-123");

		patientService.registerPatient(patient, hospital1);
		patientService.registerPatient(patient, hospital2);

		assertEquals(2, patient.getHospitals().size());
	}

	@Test
	void testPatientsInMultipleHospitals() {

		Patient patient1 = patientService.createPatient("Max", "Tum", new Date(), "max.tum@tum.de");
		Patient patient2 = patientService.createPatient("Felix", "Mann", new Date(), "felix.mann@tum.de");
		Hospital hospital1 = hospitalService.createHospital("TUM Klinikum", "Ismaninger Straße 22", "123-456-789");
		Hospital hospital2 = hospitalService.createHospital("LMU Klinikum", "Garchinger Straße 33", "789-456-123");

		patientService.registerPatient(patient1, hospital1);
		patientService.registerPatient(patient1, hospital2);
		patientService.registerPatient(patient2, hospital2);

		assertTrue(hospital1.getPatients().contains(patient1));
		assertTrue(hospital2.getPatients().contains(patient1));
		assertTrue(hospital2.getPatients().contains(patient2));
	}
}
