package gp.web.audit;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

import com.gp.common.IdKey;
import com.gp.common.IdKeys;
import com.gp.common.GPrincipal;
import com.gp.common.ServiceContext;
import com.gp.dao.AuditDAO;
import com.gp.info.InfoId;
import com.gp.svc.CommonService;

@ContextConfiguration(locations = "/mysql-test.xml")
public class AuditDAOTest extends AbstractJUnit4SpringContextTests{

	GPrincipal principal = new GPrincipal("demouser");
	ServiceContext svcctx ;
	@Autowired
    private AuditDAO auditdao;
	
	@Autowired
    private CommonService idService;
	
	@Test
	public void test() throws Exception{
		
		InfoId<Long> ukey = IdKeys.getInfoId(IdKey.GP_USERS,123l);
		principal.setUserId(ukey);
		svcctx = new ServiceContext(principal);
		
	}

}
