
package com.quickstart.test.webservice.example.client;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Logger;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceFeature;

/**
 * This class was generated by the JAX-WS RI. JAX-WS RI 2.1.6 in JDK 6 Generated source version: 2.1
 * 
 */
@WebServiceClient(name = "ServiceHelloService", targetNamespace = "http://service.example.webservice.test.yang.com/", wsdlLocation = "http://localhost:9001/Service/ServiceHello?wsdl")
public class ServiceHelloService extends Service {

    private final static URL SERVICEHELLOSERVICE_WSDL_LOCATION;
    private final static Logger logger = Logger.getLogger(com.quickstart.test.webservice.example.client.ServiceHelloService.class.getName());

    static {
        URL url = null;
        try {
            URL baseUrl;
            baseUrl = com.quickstart.test.webservice.example.client.ServiceHelloService.class.getResource(".");
            url = new URL(baseUrl, "http://localhost:9001/Service/ServiceHello?wsdl");
        } catch (MalformedURLException e) {
            logger.warning("Failed to create URL for the wsdl Location: 'http://localhost:9001/Service/ServiceHello?wsdl', retrying as a local file");
            logger.warning(e.getMessage());
        }
        SERVICEHELLOSERVICE_WSDL_LOCATION = url;
    }

    public ServiceHelloService(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    public ServiceHelloService() {
        super(SERVICEHELLOSERVICE_WSDL_LOCATION, new QName("http://service.example.webservice.test.yang.com/", "ServiceHelloService"));
    }

    /**
     * 
     * @return returns ServiceHello
     */
    @WebEndpoint(name = "ServiceHelloPort")
    public ServiceHello getServiceHelloPort() {
        return super.getPort(new QName("http://service.example.webservice.test.yang.com/", "ServiceHelloPort"), ServiceHello.class);
    }

    /**
     * 
     * @param features A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy. Supported features not in the <code>features</code> parameter will have their default values.
     * @return returns ServiceHello
     */
    @WebEndpoint(name = "ServiceHelloPort")
    public ServiceHello getServiceHelloPort(WebServiceFeature... features) {
        return super.getPort(new QName("http://service.example.webservice.test.yang.com/", "ServiceHelloPort"), ServiceHello.class, features);
    }

}
