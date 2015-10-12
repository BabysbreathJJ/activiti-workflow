package me.kafeitu.demo.activiti.cmd;

import org.activiti.engine.impl.interceptor.AbstractCommandInterceptor;
import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.impl.interceptor.CommandConfig;

public class MyPostCommandInterceptor extends AbstractCommandInterceptor {

	@Override
	public <T> T execute(CommandConfig config, Command<T> command) {
		// TODO Auto-generated method stub
		System.out.println("ºóÖÃÃüÁîÀ¹½ØÆ÷->" + command);
		return next.execute(config, command);
	}

}
