package server.servlet;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import server.memory.ObjectCache;
import servlet.ISimpleServlet;

public class ServletManager {
	
	private class ServletFields {
		String name;
		String classPath;
		
		public ServletFields(String name, String classPath) {
			this.name = name;
			this.classPath = classPath;
		}
	}
	
	static private ServletManager _manager = null;
	
	private final String CONFIG = System.getProperty("user.dir") + "/web.xml"; 
	private HashMap<String, ServletFields> _servlets;
	
	private ServletManager()
	{		
		_servlets = new HashMap<>();
	}
	
	static public ServletManager getInstance()
	{
		if (_manager == null)
			_manager = new ServletManager();
		
		return _manager;
	}
	
	public boolean isServletRequest(String url) throws ParserConfigurationException, SAXException, IOException
	{
		if (_servlets.isEmpty())
			_servlets = getServlets();
		
		return _servlets.containsKey(url);
	}
	
	public ISimpleServlet getServlet(String url) throws ClassNotFoundException, InstantiationException, IllegalAccessException, IOException
	{
		if (!_servlets.containsKey(url))
			return null;
		
		ServletFields sf = _servlets.get(url);
		
		String name = sf.name;
		String classPath = sf.classPath;
		
		ObjectCache cache = ServletCache.getInstance();
		
		ISimpleServlet servlet = (ISimpleServlet)cache.get(name);
		
		if (servlet == null)
		{
			servlet = loadServlet(classPath);
			
			cache.cache(name, servlet);
		}
		
		return servlet;
	}
	
	Document getDom() throws ParserConfigurationException, SAXException, IOException
	{
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		
		DocumentBuilder db = dbf.newDocumentBuilder();
		
		return db.parse(CONFIG);
	}
	
	HashMap<String, ServletFields> getServlets() throws ParserConfigurationException, SAXException, IOException
	{
		Document dom = getDom();
		
		HashMap<String, ServletFields> servlets = new HashMap<>();
		
		Element docEle = dom.getDocumentElement();

		NodeList nl = docEle.getElementsByTagName("servlet-mapping");
		
		if(nl != null && nl.getLength() > 0) 
		{			
			for(int i = 0; i < nl.getLength(); i++) 
			{
				Element el = (Element)nl.item(i);

				String pattern = getTextValue(el, "url-pattern");
				String name = getTextValue(el, "servlet-name");
				
				ServletFields sf = buildServletFields(docEle, name);

				servlets.put(pattern, sf);
			}
		}
		
		return servlets;
	}
	
	String getTextValue(Element el, String field)
	{
		String textVal = null;
		
		NodeList nl = el.getElementsByTagName(field);
		
		if(nl != null && nl.getLength() > 0) 
		{
			Element ele = (Element)nl.item(0);
			textVal = ele.getFirstChild().getNodeValue();
		}

		return textVal;
	}
	
	ServletFields buildServletFields(Element root, String name)
	{
		ServletFields sf = null;
		
		NodeList nl = root.getElementsByTagName("servlet");
		
		if(nl != null && nl.getLength() > 0) 
		{			
			for(int i = 0; i < nl.getLength(); i++) 
			{
				Element el = (Element)nl.item(i);
			
				String nodeName = getTextValue(el, "servlet-name");
				
				if (nodeName.equals(name))
				{
					String classPath = getTextValue(el, "servlet-class");
					
					sf = new ServletFields(name, classPath);					
					break;
				}
			}
		}
		
		return sf;
	}
	
	ISimpleServlet loadServlet(String classPath) throws ClassNotFoundException, InstantiationException, IllegalAccessException, IOException
	{
		ISimpleServlet servlet = null;
			
		File fileDir = new File(System.getProperty("user.dir"));
			
		// build a url for the classloader
		URL[] url = new URL[1];			
		url[0] = fileDir.toURI().toURL();					
		URLClassLoader loader = new URLClassLoader(url);
		
		try
		{
			// attempt to load the servlet class.
			Class<?> myClass = loader.loadClass(classPath);
			
			servlet = (ISimpleServlet)myClass.newInstance();
			
			return servlet;
		}
		finally
		{
			loader.close();
		}
	}
}
