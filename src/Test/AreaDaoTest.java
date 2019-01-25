import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ssm.dao.AreaDao;
import ssm.model.Area;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class AreaDaoTest extends BaseTest{
    @Autowired
    private AreaDao areaDao;

    @Test
    public void testQueryArea(){
        List<Area>  areaList =areaDao.queryArea();
        assertEquals(2,areaList.size());
    }
}
