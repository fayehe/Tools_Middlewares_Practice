package cn.itcast.framework;

import java.io.IOException;
import java.lang.reflect.Method;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.itcast.framework.bean.ActionMapping;
import cn.itcast.framework.bean.ActionMappingManager;
import cn.itcast.framework.bean.Result;

/**
 * 核心控制器， 此项目中只有这一个servlet
 * 拦截所有的 .action请求
 * @author Faye
 *
 */
public class ActionServlet extends HttpServlet{
	private static final long serialVersionUID = 1L;
	private ActionMappingManager actionMappingManager;
	
	@Override
	public void init() throws ServletException {
		System.out.println("1111111111111111ActionServlet.init()");
		actionMappingManager = new ActionMappingManager();
	}

	// http://localhost:8080/mystruts/login.action
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			//1. 获取请求URI，得到请求路径名
			String uri = request.getRequestURI();
			String actionName=uri.substring(uri.lastIndexOf("/")+1, uri.indexOf(".action"));
			
			//2. 根据路径名称 读取配置文件，得到类全名、处理方法方法
			ActionMapping actionMapping = actionMappingManager.getActionMapping(actionName);
			String className = actionMapping.getClassName();
			String method = actionMapping.getMethod();
			
			//3. 反射： 创建对象 调用方法，获取返回值
			Class<?> clazz = Class.forName(className);
			Object obj = clazz.newInstance();  //LoginAction loginAction = new LoginAction(); 多例 - 线程安全
			Method m = clazz.getDeclaredMethod(method, HttpServletRequest.class,HttpServletResponse.class );
			String returnFlag =  (String) m.invoke(obj, request, response);
			
			//4. 拿到返回值标记 读取配置文件得到对应的页面URL（page）
			Result result = actionMapping.getResults().get(returnFlag);
			String type = result.getType();
			String page = result.getPage();
			
			//5. 跳转
			if ("redirect".equals(type)) {
				response.sendRedirect(request.getContextPath() + page);
			} else {
				request.getRequestDispatcher(page).forward(request, response);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doGet(req, resp);
	}
}
