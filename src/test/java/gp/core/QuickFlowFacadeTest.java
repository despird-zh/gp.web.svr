package gp.core;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.gp.config.RootConfigurer;
import com.gp.config.ServiceConfigurer;
import com.gp.core.QuickFlowFacade;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes={RootConfigurer.class, 
		ServiceConfigurer.class})
public class QuickFlowFacadeTest {

	@Test
	public void test(){
		QuickFlowFacade.demo();
	}
}
