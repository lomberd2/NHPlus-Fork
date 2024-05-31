package de.hitec.nhplus.datastorage;

public class DaoFactory {

    private static DaoFactory instance;

    private DaoFactory() {
    }

    public static DaoFactory getDaoFactory() {
        if (DaoFactory.instance == null) {
            DaoFactory.instance = new DaoFactory();
        }
        return DaoFactory.instance;
    }

    public TreatmentDao createTreatmentDao() {
        return new TreatmentDao(ConnectionBuilder.getConnection());
    }

    public PatientDao createPatientDAO() {
        return new PatientDao(ConnectionBuilder.getConnection());
    }
    public CaregiverDao createCaregiverDAO() {
        return new CaregiverDao(ConnectionBuilder.getConnection());
    }

    public CryptoDao createCryptoDAO() {
        return new CryptoDao(ConnectionBuilder.getConnection());
    }
    public ArchivedTreatmentDao createArchivedTreatmentDao() {return new ArchivedTreatmentDao(ConnectionBuilder.getConnection());}

    public ArchivedPatientDao createArchivedPatientDao() {return new ArchivedPatientDao();}
}
