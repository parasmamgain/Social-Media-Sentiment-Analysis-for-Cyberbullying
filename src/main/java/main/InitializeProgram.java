package main;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;			

import javax.jms.Destination;
import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.JMSProducer;
import javax.jms.TextMessage;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import com.ibm.msg.client.jms.JmsConnectionFactory;
import com.ibm.msg.client.jms.JmsFactoryFactory;
import com.ibm.msg.client.wmq.WMQConstants;

import jep.Jep;
import jep.python.PyObject;


public class InitializeProgram {

	// System exit status value (assume unset value to be 1)
		private static int status = 1;

		// Create variables for the connection to MQ
		private static final String HOST = "queuemanager1-3fca.qm.eu-gb.mq.appdomain.cloud"; 		// Host name or IP address
		private static final int PORT = 31294; 												 		// Listener port for your queue manager
		private static final String CHANNEL = "CLOUD.ADMIN.SVRCONN"; 						 		// Channel name
		private static final String QMGR = "QueueManager1"; 								 		// Queue manager name
		private static final String APP_USER = "CONFIGURE_USERNAME_HERE"; 								 		// User name that application uses to connect to MQ
		private static final String APP_PASSWORD = "CONFIGURE_PASSWORD_HERE";  // Password that the application uses to connect to MQ
		private static final String QUEUE_NAME = "DEV.QUEUE.1"; 									// Queue that the application uses to put and get messages to and from
		private static final String hostName = "https://<HOSTNAME>";
		//https://623aa97c8f18.ngrok.io
		/**
		 * Main method
		 *
		 */
		
		public static void main(String[] args) {
			
			
			String positiveSampleMessage = "Well, what a surprise. demo on 25 June on early morning";
			String negativeSampleMessage = "karma is a bitch";
			String negativeSampleMessage1 = "\"You know what you are? You're like a big bear with claws and with fangs... big fucking teeth  man.";
			
			sendMessage(positiveSampleMessage);
			
			//sendMessage(negativeSampleMessage1);
		}
		
		public static void sendMessage(String messageData) {
			
			//load machine learning model
			//loadModel();
			
			//parse the model built and 
			
			//pass the sender message via this model
			
			//0-positive response
			//1-negative response
			int response = predictCategory(messageData);
			if(response==1) {
				System.out.println("you are not allowed to send abusive messages.");
				return;
			} else if(response==-1) {
				System.out.println("error while predicting the category");
				return;
			}
			
			//send the message
			 sender(messageData);
			//reject the message send operation
			
		}
		
		public static void sender(String messageData) {

			// Variables
			JMSContext context = null;
			Destination destination = null;
			JMSProducer producer = null;
			JMSConsumer consumer = null;
			try {
				// Create a connection factory
				JmsFactoryFactory ff = JmsFactoryFactory.getInstance(WMQConstants.WMQ_PROVIDER);
				JmsConnectionFactory cf = ff.createConnectionFactory();

				// Set the properties
				cf.setStringProperty(WMQConstants.WMQ_HOST_NAME, HOST);
				cf.setIntProperty(WMQConstants.WMQ_PORT, PORT);
				cf.setStringProperty(WMQConstants.WMQ_CHANNEL, CHANNEL);
				cf.setIntProperty(WMQConstants.WMQ_CONNECTION_MODE, WMQConstants.WMQ_CM_CLIENT);
				cf.setStringProperty(WMQConstants.WMQ_QUEUE_MANAGER, QMGR);
				cf.setStringProperty(WMQConstants.WMQ_APPLICATIONNAME, "JmsPutGet (JMS)");
				cf.setBooleanProperty(WMQConstants.USER_AUTHENTICATION_MQCSP, true);
				cf.setStringProperty(WMQConstants.USERID, APP_USER);
				cf.setStringProperty(WMQConstants.PASSWORD, APP_PASSWORD);
				cf.setStringProperty(WMQConstants.WMQ_SSL_CIPHER_SUITE, "*TLS12");

				// Create JMS objects
				context = cf.createContext();
				destination = context.createQueue("queue:///" + QUEUE_NAME);
				
				
				
				TextMessage message = context.createTextMessage(messageData);

				producer = context.createProducer();
				producer.send(destination, message);
				System.out.println("Sent message:\n" + message);

				recordSuccess();
			} catch (JMSException jmsex) {
				recordFailure(jmsex);
			}

			System.exit(status);

		} // end main()
		
		private static int predictCategory(String messageData) {
			///
			
			String endpoint = "/predict";
			int predictedResponse = -1;
			
			try {
				// Create a neat value object to hold the URL
				// URL url = new URL(hostName+endpoint);
		        
				HttpPost post = new HttpPost(hostName+endpoint);
				URI uri = new URIBuilder(post.getURI())
					      .addParameter("message_data", messageData)
					      .build();
				((HttpRequestBase) post).setURI(uri);
			
				CloseableHttpClient httpClient = HttpClients.createDefault();
				CloseableHttpResponse response = httpClient.execute(post);
				String responseData = EntityUtils.toString(response.getEntity());
				JSONObject result = new JSONObject(responseData);
				predictedResponse = result.getInt("prediction");

			}
			catch(Exception e) {
				System.out.println(e);
			}
			return predictedResponse;
		}

		/**
		 * Record this run as successful.
		 */
		private static void recordSuccess() {
			System.out.println("SUCCESS");
			status = 0;
			return;
		}

		/**
		 * Record this run as failure.
		 *
		 * @param ex
		 */
		private static void recordFailure(Exception ex) {
			if (ex != null) {
				if (ex instanceof JMSException) {
					processJMSException((JMSException) ex);
				} else {
					System.out.println(ex);
				}
			}
			System.out.println("FAILURE");
			status = -1;
			return;
		}

		/**
		 * Process a JMSException and any associated inner exceptions.
		 *
		 * @param jmsex
		 */
		private static void processJMSException(JMSException jmsex) {
			System.out.println(jmsex);
			Throwable innerException = jmsex.getLinkedException();
			if (innerException != null) {
				System.out.println("Inner exception(s):");
			}
			while (innerException != null) {
				System.out.println(innerException);
				innerException = innerException.getCause();
			}
			return;
		}

}
