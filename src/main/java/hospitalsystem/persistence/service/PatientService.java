package hospitalsystem.persistence.service;

import hospitalsystem.persistence.entity.Hospital;
import hospitalsystem.persistence.entity.Patient;
import hospitalsystem.persistence.repository.PatientRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class PatientService {
    @Autowired
    private PatientRepository patientRepository;

    /**
     * Creates a new patient and saves it to the database.
     *
     * @param firstName   the first name of the patient
     * @param lastName    the last name of the patient
     * @param dateOfBirth the date of birth of the patient
     * @param email       the email address of the patient
     * @return the saved Patient object
     */

    @Transactional
    public Patient createPatient(String firstName, String lastName, Date dateOfBirth, String email) {
        return patientRepository.save(new Patient(firstName, lastName, dateOfBirth, email));
    }

    /**
     * Finds a patient by their ID.
     *
     * @param id the unique ID of the patient
     * @return the Patient object if found, or null if no patient exists with the given ID
     */

    public Patient findPatientById(int id) {
        Optional<Patient> foundPatient = patientRepository.findById(id);
        return foundPatient.orElse(null);
    }

    /**
     * Updates the details of an existing patient.
     *
     * @param id          the unique ID of the patient
     * @param firstName   the updated first name of the patient
     * @param lastName    the updated last name of the patient
     * @param email       the updated email address of the patient
     * @throws RuntimeException if the patient with the given ID is not found
     */

    @Transactional
    public void updatePatientById(int id, String firstName, String lastName, String email) {
        Patient patient = findPatientById(id);
        try {
            patient.setFirstName(firstName);
            patient.setLastName(lastName);
            patient.setEmail(email);
            savePatient(patient);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Deletes a patient by their ID. Also removes all associations with hospitals.
     *
     * @param id the unique ID of the patient
     * @throws RuntimeException if the patient cannot be deleted
     */

    @Transactional
    public void deletePatientById(int id) {
        Patient patient = findPatientById(id);
        try {
            for (Hospital hospital : patient.getHospitals()) {
                hospital.getPatients().remove(patient);
            }
            patient.getHospitals().clear();
            patientRepository.delete(patient);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * Registers a patient to a hospital. Creates a bidirectional association between the patient and the hospital.
     *
     * @param patient  the patient to register
     * @param hospital the hospital to register the patient to
     */

    @Transactional
    public void registerPatient(Patient patient, Hospital hospital) {
        patient.getHospitals().add(hospital);
        hospital.getPatients().add(patient);
    }

    /**
     * Adds or updates a diagnosis for a patient.
     *
     * @param id        the unique ID of the patient
     * @param diagnosis the new or updated diagnosis for the patient
     * @throws RuntimeException if the patient with the given ID is not found
     */

    @Transactional
    public void addDiagnosisById(int id, String diagnosis) {
        Patient patient = findPatientById(id);
        try {
            patient.setDiagnosis(diagnosis);
            savePatient(patient);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Retrieves the list of hospitals associated with a given patient.
     *
     * @param patient the patient whose list of hospitals is to be retrieved
     * @return a set of Hospital objects associated with the patient
     */

    public Set<Hospital> listHospitalByPatient(Patient patient) {
        return patient.getHospitals();
    }

    /**
     * Saves a patient object to the database.
     *
     * @param patient the patient object to save
     */

    public void savePatient(Patient patient) {
        patientRepository.save(patient);
    }

    /**
     * Retrieves all patients from the database.
     *
     * @return a list of all patients
     */

    public List<Patient> findAllPatients() {
        return patientRepository.findAll();
    }
}
