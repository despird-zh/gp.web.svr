package gp.core;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

import com.gp.audit.AccessPoint;
import com.gp.common.Principal;
import com.gp.common.GroupUsers;
import com.gp.core.SecurityFacade;
import com.gp.exception.CoreException;
import com.gp.dao.info.UserInfo;
import com.gp.svc.SystemService;

@ContextConfiguration(locations = "/mysql-test.xml")
public class SecurityFacadeTest  extends AbstractJUnit4SpringContextTests{
	
	@Autowired
	SystemService systemservice;
	
	@Test
	public void createAdmin(){
		systemservice.hashCode();
		AccessPoint ap = new AccessPoint(
				"pc",
				"127.0.0.1",
				"web",
				"0.1");
		Principal principal = GroupUsers.PSEUDO_USER;
		
		UserInfo admin = new UserInfo();
		admin.setAccount("admin");
		admin.setEmail("admin@cnet.com");
		admin.setFullName("Administrator");
		admin.setLanguage("en_US");
		admin.setTimeZone("GMT+08:00");
		admin.setPassword("1");
		admin.setMobile("18601253554");
		admin.setPhone("52436454");
		admin.setType(GroupUsers.UserType.INLINE.name());
		admin.setState(GroupUsers.UserState.ACTIVE.name());
		
		try {
			SecurityFacade.newAccount(ap, principal, admin, null, null);
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
