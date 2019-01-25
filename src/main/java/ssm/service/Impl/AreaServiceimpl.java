package ssm.service.Impl;

import org.omg.CORBA.PRIVATE_MEMBER;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ssm.dao.AreaDao;
import ssm.model.Area;
import ssm.service.AreaService;

import java.util.List;

@Service("areaService")
public class AreaServiceimpl implements AreaService {
    @Autowired
    private AreaDao areaDao;
    @Override
    public List<Area> getAreaList() {
        return areaDao.queryArea();
    }
}
