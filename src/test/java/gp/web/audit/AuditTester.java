package gp.web.audit;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.mutable.MutableObject;

import com.gp.audit.AccessPoint;
import com.gp.audit.AuditConverter;
import com.gp.audit.AuditEventLoad;
import com.gp.common.GeneralContext.ExecState;
import com.gp.common.IdKey;
import com.gp.info.InfoId;

import junit.framework.TestCase;

public class AuditTester extends TestCase{
	
	static String json = null;
	
	public void testConverter() throws IOException{
		
		InfoId<String > id1 = IdKey.ATTACH_REL.getInfoId("000101M");
		AuditEventLoad ad = new AuditEventLoad("user1","verb1",id1);
		AccessPoint ap = new AccessPoint("browser","1.2.3.4","dapp","v0.1");
		ad.setAccessPoint(ap);
		Map<String,String> operpredicates = new HashMap<String,String>();
		operpredicates.put("operk1", "ov1");
		operpredicates.put("operk2", "ov2");
		operpredicates.put("operk3", "ov3");
		operpredicates.put("operk4", "ov4");
		ad.addPredicates(operpredicates);

		ad.endAuditVerb(ExecState.SUCCESS.toString(), "demo operat message str.");
		
		json = (String)AuditConverter.auditToJson(ad).getValue();
		
		System.out.println(json);
		
		AuditEventLoad nad = AuditConverter.jsonToAudit(new MutableObject(json));
		
		System.out.println(nad);
	}

}
