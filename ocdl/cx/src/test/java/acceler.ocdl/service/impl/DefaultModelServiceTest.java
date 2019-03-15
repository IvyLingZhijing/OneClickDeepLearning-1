package acceler.ocdl.service.impl;

import acceler.ocdl.model.User;
import acceler.ocdl.service.ModelService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.*;

public class DefaultModelServiceTest {

    @Test
    public void copyModels() {
        DefaultModelService service = new DefaultModelService();
        User user1 = new User();
        user1.setProjectId(1L);
        user1.setUserId(1001L);
        User user2 = new User();
        user2.setProjectId(2L);
        user2.setUserId(1002L);

        service.copyModels(user1);
        service.copyModels(user1);

    }
}