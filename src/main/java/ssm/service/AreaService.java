package ssm.service;

import org.springframework.stereotype.Component;
import ssm.model.Area;

import java.util.List;

@Component
public interface AreaService {
    List<Area> getAreaList();
}
