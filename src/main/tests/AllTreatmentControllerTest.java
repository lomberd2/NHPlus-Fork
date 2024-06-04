import de.hitec.nhplus.controller.AllTreatmentController;
import de.hitec.nhplus.model.Treatment;
import de.hitec.nhplus.datastorage.ArchivedTreatmentDao;
import de.hitec.nhplus.datastorage.DaoFactory;
import de.hitec.nhplus.datastorage.TreatmentDao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

public class AllTreatmentControllerTest {

//    private AllTreatmentController controller;
//    private TreatmentDao treatmentDaoMock;
//    private ArchivedTreatmentDao archivedTreatmentDaoMock;
//
//    @BeforeEach
//    public void setup() {
//        controller = new AllTreatmentController();
//        treatmentDaoMock = mock(TreatmentDao.class);
//        archivedTreatmentDaoMock = mock(ArchivedTreatmentDao.class);
//        DaoFactory daoFactoryMock = mock(DaoFactory.class);
//
//        when(daoFactoryMock.createTreatmentDao()).thenReturn(treatmentDaoMock);
//        when(daoFactoryMock.createArchivedTreatmentDao()).thenReturn(archivedTreatmentDaoMock);
//        DaoFactory.setDaoFactory(daoFactoryMock);
//    }
//
//    @Test
//    public void testArchiveTreatment() throws SQLException {
//        Treatment treatment = new Treatment();
//        treatment.setTid(1);
//
//        controller.archiveTreatment(treatment);
//
//        verify(archivedTreatmentDaoMock, times(1)).insert(treatment);
//        verify(treatmentDaoMock, times(1)).deleteById(treatment.getTid());
//    }

}