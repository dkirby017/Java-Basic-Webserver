package server.servlet;

import static org.junit.Assert.*;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import server.io.ClientConnection;
import server.io.IRequest;
import server.io.IResponse;
import server.io.RequestMethods.Method;
import servlet.ISimpleServlet;

public class ServletProcessor_test {

	private ServletProcessor _Processor;
	private ClientConnection _Connection;
	private IRequest _Request;
	private IResponse _Response;
	private ISimpleServlet _Servlet;
	
	@Before
	public void setUp() throws Exception {
		_Request = mock(IRequest.class);
		when(_Request.getUrlPath()).thenReturn("");
		
		_Response = mock(IResponse.class);
		
		_Connection = mock(ClientConnection.class);
		when(_Connection.getRequest()).thenReturn(_Request);
		when(_Connection.getResponse()).thenReturn(_Response);
		
		_Servlet = mock(ISimpleServlet.class);
		
		ServletManager manager = mock(ServletManager.class);
		when(manager.getServlet("")).thenReturn(_Servlet);
		
		_Processor = spy(new ServletProcessor());
		doReturn(manager).when(_Processor).getServletManager();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	/**
	 * Test that process() invokes doGet() for a GET request
	 */
	public void test_process_GET() {
		try {
			// setup for this test only
			when(_Request.getMethod()).thenReturn(Method.GET);
			
			// test
			_Processor.process(_Connection);
			
			verify(_Servlet).doGet(_Request, _Response);
			verify(_Servlet, times(0)).doPost(_Request, _Response);
			
		} catch (Exception e) {
			fail();
		}
		
	}
	
	@Test
	/**
	 * Test that process() invokes doPost() for a POST request
	 */
	public void test_process_POST() {
		try {
			// setup for this test only
			when(_Request.getMethod()).thenReturn(Method.POST);
			
			// test
			_Processor.process(_Connection);
						
			verify(_Servlet).doPost(_Request, _Response);
			verify(_Servlet, times(0)).doGet(_Request, _Response);
			
		} catch (Exception e) {
			fail();
		}
		
	}

}
