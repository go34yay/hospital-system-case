package hospitalsystem.persistence.service;

import hospitalsystem.persistence.entity.Hospital;
import hospitalsystem.persistence.entity.Patient;
import hospitalsystem.persistence.repository.HospitalRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class HospitalService {
    @Autowired
    private HospitalRepository hospitalRepository;

    /**
     * Creates a new hospital and saves it to the database.
     *
     * @param name    the name of the hospital
     * @param address the address of the hospital
     * @param phone   the phone number of the hospital
     * @return the saved Hospital object
     */

    @Transactional
    public Hospital createHospital(String name, String address, String phone) {
        return hospitalRepository.save(new Hospital(name, address, phone));
    }

    /**
     * Finds a hospital by its ID.
     *
     * @param id the unique ID of the hospital
     * @return the Hospital object if found, or null if there is no hospital with the given ID.
     */

    public Hospital findHospitalById(int id) {
        Optional<Hospital> foundHospital = hospitalRepository.findById(id);
        return foundHospital.orElse(null);
    }

    /**
     * Updates the details of an existing hospital.
     *
     * @param id      the unique ID of the hospital
     * @param name    the updated name of the hospital
     * @param address the updated address of the hospital
     * @param phone   the updated phone number of the hospital
     * @throws RuntimeException if the hospital with the given ID is not found
     */

    @Transactional
    public void updateHospitalById(int id, String name, String address, String phone) {
        Hospital hospital = findHospitalById(id);
        try {
            hospital.setName(name);
            hospital.setAddress(address);
            hospital.setPhone(phone);
            saveHospital(hospital);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * Deletes a hospital by its ID. Also removes all associations with patients.
     * The patients will not be deleted.
     *
     * @param id the unique ID of the hospital
     * @throws RuntimeException if the hospital cannot be deleted
     */

    @Transactional
    public void deleteHospitalById(int id) {
        Hospital hospital = findHospitalById(id);
        try {
            for (Patient patient : hospital.getPatients()) {
                patient.getHospitals().remove(hospital);
            }
            hospital.getPatients().clear();
            hospitalRepository.delete(hospital);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * Retrieves the list of all patients of a hospital.
     *
     * @param hospital the hospital in which the patients have been registered
     * @return a set of Patient objects associated with the hospital
     */

    public Set<Patient> listPatientsByHospital(Hospital hospital, Patient patient) {
     return hospital.getPatients();
    }

    /**
     * Saves a hospital object to the database.
     *
     * @param hospital the hospital object to save
     */

    public void saveHospital(Hospital hospital) {
        hospitalRepository.save(hospital);
    }

    /**
     * Retrieves all hospitals from the database.
     *
     * @return a list of all hospitals
     */


    public List<Hospital> findAllHospitals() {
        return hospitalRepository.findAll();
    }
}
