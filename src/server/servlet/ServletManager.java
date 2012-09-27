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


/**
 * Singleton. Manages the servlets, keeping track of the request patterns for each, fetching the servlets themselves, and providing 
 * a means for determining whether a request corresponds to a servlet
 * 
 * @author dkirby
 *
 */
public class ServletManager {
	
	/**
	 * Protected container class for a servlet's name and class path
	 *
	 */
	protected class ServletFields {
		private String name;
		private String classPath;
		
		public ServletFields(String name, String classPath) {
			this.name = name;
			this.classPath = classPath;
		}
		
		public String getName() 
		{
			return name;
		}
		
		public String getClassPath()
		{
			return classPath;
		}
	}
	
	static private ServletManager _manager = null;
	
	private final String CONFIG = System.getProperty("user.dir") + "/web.xml"; 
	private HashMap<String, ServletFields> _servlets;
	
	/**
	 * Constructor
	 */
	private ServletManager()
	{		
		_servlets = new HashMap<>();
	}
	
	/**
	 * Gets the singleton instance of the ServletManager
	 * 
	 * @return the singleton instance of the ServletManager
	 */
	static public ServletManager getInstance()
	{
		if (_manager == null)
			_manager = new ServletManager();
		
		return _manager;
	}
	
	/**
	 * Determines whether the input url string corresponds to a servlet request
	 * 
	 * @param url the url string of the request
	 * 
	 * @return true if the request is for a servlet, false otherwise
	 * 
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 */
	public boolean isServletRequest(String url) throws ParserConfigurationException, SAXException, IOException
	{
		if (_servlets.isEmpty())
			_servlets = getServlets();
		
		return _servlets.containsKey(url);
	}
	
	/**
	 * Gets the servlet corresponding to the input url string.
	 * 
	 * @param url the url of the request
	 * 
	 * @return the servlet requested
	 * 
	 * @throws ClassNotFoundException if the servlet doesn't exist
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws IOException
	 * @throws SAXException 
	 * @throws ParserConfigurationException 
	 */
	public ISimpleServlet getServlet(String url) throws ClassNotFoundException, InstantiationException, IllegalAccessException, IOException, ParserConfigurationException, SAXException
	{		
		if (!isServletRequest(url))
			throw new ClassNotFoundException("Servlet not found");
		
		// get the servlet data fields
		ServletFields sf = _servlets.get(url);
		
		// record the name and classpath
		String name = sf.getName();
		String classPath = sf.getClassPath();
		
		// get the ServletCache
		ServletCache cache = getServletCache();
		
		// try to get the CacheableServlet from the cache
		CacheableServlet cServlet = (CacheableServlet)cache.get(name);
		
		// if the CacheableServlet has not been cached, load it and cache it
		if (cServlet == null)
		{
			cServlet = loadServlet(classPath);
			
			cache.cache(name, cServlet);
		}
		
		// return the servlet
		return cServlet.get_Servlet();
	}
	
	/**
	 * Gets the ServletCache
	 * 
	 * @return the ServletCache
	 */
	ServletCache getServletCache()
	{
		return ServletCache.getInstance();
	}
	
	/**
	 * Gets the DOM object for the servlet configuration file
	 * 
	 * @return the DOM object for the servlet configuration file
	 * 
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 */
	Document getDom() throws ParserConfigurationException, SAXException, IOException
	{
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		
		DocumentBuilder db = dbf.newDocumentBuilder();
		
		return db.parse(CONFIG);
	}
	
	/**
	 * Parses the DOM object for the servlet configuration file and records the name, classpath, and request pattern for each
	 * servlet
	 * 
	 * @return a HashMap mapping the servlet data fields to the url request pattern for each servlet
	 * 
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 */
	HashMap<String, ServletFields> getServlets() throws ParserConfigurationException, SAXException, IOException
	{
		Document dom = getDom();
		
		HashMap<String, ServletFields> servlets = new HashMap<>();
		
		// get the root element
		Element docEle = dom.getDocumentElement();

		// get a list of all servlet-mapping elements
		NodeList nl = docEle.getElementsByTagName("servlet-mapping");
		
		// loop through the list, extracting the request pattern and servlet name from each, and generating a ServletFields
		// object for that servlet's data fields. Add the resulting request pattern-servletFields pair to the HashMap
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
		
		// return the HashMap
		return servlets;
	}
	
	/**
	 * Gets the text value for a given element and field name
	 * 
	 * @param el the element
	 * @param field the field
	 * 
	 * @return the text value for the given element field
	 */
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
	
	/**
	 * Generates a ServletFields object for the given servlet name
	 * 
	 * @param root the root element of the servlet configuration file
	 * @param name the servlet name
	 * 
	 * @return a ServletFields object containing the servlet's data fields, null if the servlet name doesn't exist in the configuration file
	 */
	ServletFields buildServletFields(Element root, String name)
	{
		ServletFields sf = null;
		
		// get a list of all 'servlet' elements
		NodeList nl = root.getElementsByTagName("servlet");
		
		// loop through the list, searching for the given servlet name
		if(nl != null && nl.getLength() > 0) 
		{			
			for(int i = 0; i < nl.getLength(); i++) 
			{
				Element el = (Element)nl.item(i);
			
				String nodeName = getTextValue(el, "servlet-name");
				
				// if the current element is the servlet we're looking for...
				if (nodeName.equals(name))
				{
					// get the servlet class path
					String classPath = getTextValue(el, "servlet-class");
					
					// store the name and class path in a new ServletFields object and break out of the loop
					sf = new ServletFields(name, classPath);					
					break;
				}
			}
		}
		
		// return the new ServletFields object
		return sf;
	}
	
	/**
	 * Loads the specified CacheableServlet from the given class path
	 * 
	 * @param classPath the classPath of the servlet to load
	 * 
	 * @return the CacheableServlet object
	 * 
	 * @throws ClassNotFoundException if the servlet does not exist at the specified class path
	 * @throws InstantiationException 
	 * @throws IllegalAccessException
	 * @throws IOException
	 */
	CacheableServlet loadServlet(String classPath) throws ClassNotFoundException, InstantiationException, IllegalAccessException, IOException
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
			
			return new CacheableServlet(servlet);
		}
		finally
		{
			loader.close();
		}
	}
}
