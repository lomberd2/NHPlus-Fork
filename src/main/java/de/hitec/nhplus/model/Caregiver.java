package de.hitec.nhplus.model;

import de.hitec.nhplus.utils.DateConverter;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Patients live in a NURSING home and are treated by nurses.
 */
public class Caregiver extends Person {
    private SimpleLongProperty cid;
    private final SimpleStringProperty telephone;

    /**
     * Constructor to initiate an object of class <code>Caregiver</code> with the given parameter. Use this constructor
     * to initiate objects, which are not persisted yet, because it will not have a caregiver id (cid).
     *
     * @param firstName First name of the caregiver.
     * @param surname Last name of the caregiver.
     * @param telephone telephone number of the caregiver.
     */
    public Caregiver(String firstName, String surname, String telephone) {
        super(firstName, surname);
        this.telephone = new SimpleStringProperty(telephone);
    }

    /**
     * Constructor to initiate an object of class <code>Caregiver</code> with the given parameter. Use this constructor
     * to initiate objects, which are already persisted and have a caregiver id (cid).
     *
     * @param cid caregiver id.
     * @param firstName First name of the caregiver.
     * @param surname Last name of the caregiver.
     * @param telephone Date of birth of the caregiver.
     */
    public Caregiver(long cid, String firstName, String surname, String telephone) {
        super(firstName, surname);
        this.cid = new SimpleLongProperty(cid);
        this.telephone = new SimpleStringProperty(telephone);
    }

    public long getCid() {
        return cid.get();
    }

    public String getTelephone() {
        return telephone.get();
    }


    public String toString() {
        return "Pfleger" + "\nMNID: " + this.cid +
                "\nFirstname: " + this.getFirstName() +
                "\nSurname: " + this.getSurname() +
                "\nTelephone: " + this.telephone +
                "\n";
    }
}