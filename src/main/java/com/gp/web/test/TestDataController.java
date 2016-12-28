package com.gp.web.test;

import java.util.ArrayList;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.gp.web.BaseController;
import com.gp.web.util.CustomWebUtils;

@Controller("test-data-ctlr")
@RequestMapping("/test")
public class TestDataController extends BaseController{
	
	static Logger LOGGER = LoggerFactory.getLogger(TestDataController.class);

	@RequestMapping("query")
	public ModelAndView doQuery(HttpServletRequest request){
		
		CustomWebUtils.dumpRequestAttributes(request);
		ModelAndView mav = super.getJsonModelView();
		ArrayList<demodata> list = new ArrayList<demodata>();
		for(int i = 1; i<100; i++){
			demodata dd = new demodata();
			dd.setFirstname("cliton"+i);
			dd.setLastname("bill"+i);
			dd.setEmail("demo"+i+"@163.com");
			dd.setSystem("system"+i);
			dd.setId(""+i);
			dd.setBirthday(new Date().toString());
			
			list.add(dd);
		}
		mav.addObject("data", list);
		return mav;
	}
	
	public static class demodata{
		
		private String firstname;
		private String lastname;
		private String email;
		private String system;
		
		private String id;
		
		private String birthday;

		public String getFirstname() {
			return firstname;
		}

		public void setFirstname(String firstname) {
			this.firstname = firstname;
		}

		public String getLastname() {
			return lastname;
		}

		public void setLastname(String lastname) {
			this.lastname = lastname;
		}

		public String getEmail() {
			return email;
		}

		public void setEmail(String email) {
			this.email = email;
		}

		public String getSystem() {
			return system;
		}

		public void setSystem(String system) {
			this.system = system;
		}

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public String getBirthday() {
			return birthday;
		}

		public void setBirthday(String birthday) {
			this.birthday = birthday;
		}
		
	}
}
