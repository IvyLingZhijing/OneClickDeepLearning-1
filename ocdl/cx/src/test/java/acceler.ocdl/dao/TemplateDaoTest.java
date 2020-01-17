package acceler.ocdl.dao;

import acceler.ocdl.entity.Project;
import acceler.ocdl.entity.Template;
import acceler.ocdl.entity.TemplateCategory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Transactional
public class TemplateDaoTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private TemplateDao dao;

    @Autowired
    private ProjectDao projectDao;

    private Template test;

    @Before
    public void before() {

        Project project = projectDao.findByName("ocdl").get();

        // init user data
        test = Template.builder()
                .name("readfile")
                .description("readfile")
                .project(project)
                .refId("abck")
                .suffix("ipynb")
                .build();
    }

    @Test
    public void testCRUD() {
        testCreate();
        testRead();
        testUpdate();
        testDelete();
    }

    private void testCreate() {

        Template objInDb = dao.save(test);
        assertEquals(objInDb.getName(), test.getName());
        assertEquals(objInDb.getDescription(), test.getDescription());
        assertEquals(objInDb.getProject().getId(), test.getProject().getId());
        assertEquals(objInDb.getRefId(), test.getRefId());
        assertEquals(objInDb.getSuffix(), test.getSuffix());
    }

    private void testRead() {
        // read
        Template objInDb = dao.findByRefId(test.getRefId()).get();
        assertEquals(objInDb.getName(), test.getName());
        assertEquals(objInDb.getDescription(), test.getDescription());
        assertEquals(objInDb.getProject().getId(), test.getProject().getId());
        assertEquals(objInDb.getRefId(), test.getRefId());
        assertEquals(objInDb.getSuffix(), test.getSuffix());
    }

    private void testUpdate() {

        Template objInDb = dao.findByRefId(test.getRefId()).get();
        String name = "data";
        Long id = objInDb.getId();
        objInDb.setName(name);
        objInDb = dao.save(objInDb);

        assertEquals(objInDb.getId(), id);
        assertEquals(objInDb.getName(), name);
        assertEquals(objInDb.getDescription(), test.getDescription());
        assertEquals(objInDb.getProject().getId(), test.getProject().getId());
        assertEquals(objInDb.getRefId(), test.getRefId());
        assertEquals(objInDb.getSuffix(), test.getSuffix());
    }

    private void testDelete() {

        Template objInDb = dao.findByRefId(test.getRefId()).get();
        dao.delete(objInDb);
        Boolean exist = dao.findByRefId(test.getRefId()).isPresent();
        assertEquals(exist, false);
    }
}