package cn.itcast.framework.bean;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

public class ActionMappingManager {

	private Map<String,ActionMapping> allActions ;// 保存action的集合
	
	public ActionMappingManager(){
		allActions = new HashMap<String,ActionMapping>();
		this.init();
	}
	
	public ActionMapping getActionMapping(String actionName) {
		if (actionName == null) throw new RuntimeException("传入的参数有误，请查看struts.xml。。。");
		
		ActionMapping actionMapping = allActions.get(actionName);
		
		if (actionMapping == null) throw new RuntimeException("struts.xml has no this action name, pls check...");
		
		return actionMapping;
	}
	
	private void init() {
		try {
			SAXReader reader = new SAXReader();
			InputStream inStream = this.getClass().getResourceAsStream("/mystruts.xml");
			Document doc = reader.read(inStream);// 加载文件
			
			Element root = doc.getRootElement();// 获取根
			
			Element ele_package = root.element("package");// 得到package节点
			
			List<Element> listAction = ele_package.elements("action");// 得到package节点下所有action子节点
			
			for (Element ele_action : listAction) {//遍历 封装
				ActionMapping actionMapping = new ActionMapping();
				actionMapping.setName(ele_action.attributeValue("name"));
				actionMapping.setClassName(ele_action.attributeValue("class"));
				actionMapping.setMethod(ele_action.attributeValue("method"));
				
				Map<String,Result> results = new HashMap<String, Result>();
				
				Iterator<Element> it = ele_action.elementIterator("result");
				while (it.hasNext()) {
					 Element ele_result = it.next();
					 
					 Result res = new Result();
					 res.setName(ele_result.attributeValue("name"));
					 res.setType(ele_result.attributeValue("type"));
					 res.setPage(ele_result.getTextTrim());
					 
					 results.put(res.getName(), res);
				 }
				
				actionMapping.setResults(results);
				
				allActions.put(actionMapping.getName(), actionMapping);
			}
			
			
		} catch (Exception e) {
			throw new RuntimeException("启动初始化错误：",e);
		}
	}
}
