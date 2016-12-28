package com.gp.web.test;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.math.NumberUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.gp.web.BaseController;
import com.gp.web.model.TreeNode;
import com.gp.web.test.TestController.TestBean;

@Controller("test-view-ctlr")
@RequestMapping("/test")
public class TestViewController extends BaseController{
	
	@RequestMapping("test-view")
	public ModelAndView test(){
		
		ModelAndView mav = super.getJspModelView("test/test-view");
	
		return mav;
	}
	
	@RequestMapping("tree-node")
	public ModelAndView treeNode(HttpServletRequest request){
		
		String idstr = request.getParameter("id");
		Integer id = NumberUtils.toInt(idstr);

		List<TreeNode> nodes = new ArrayList<TreeNode>();
		
		TreeNode node = new TreeNode();
		node.setChildren(true);
		node.setId(String.valueOf(id + 1));
		node.setName("Name"+(id+1));
		node.setText("text"+(id+1));
		node.setPid(String.valueOf(id));
		node.setType("folder");
		nodes.add(node);
		
		node = new TreeNode();
		node.setChildren(false);
		node.setId(String.valueOf(id + 4));
		node.setName("Name"+(id+4));
		node.setText("text"+(id+4));
		node.setPid(String.valueOf(id));
		node.setType("file");
		nodes.add(node);
		
		ModelAndView mav = super.getJsonModelView(nodes);
		
		return mav;
	}
}
