package com.gp.web.service;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.gp.web.BaseController;
import com.gp.web.servlet.ServiceFilter;

@Controller
@RequestMapping(ServiceFilter.FILTER_PREFIX)
public class CommonController extends BaseController{

}
