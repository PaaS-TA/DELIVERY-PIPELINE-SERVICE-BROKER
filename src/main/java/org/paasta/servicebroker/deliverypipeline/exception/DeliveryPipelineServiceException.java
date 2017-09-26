package org.paasta.servicebroker.deliverypipeline.exception;

import org.openpaas.servicebroker.exception.ServiceBrokerException;


/**
 * Exception thrown when issues with the underlying mongo service occur.
 * NOTE: com.mongodb.MongoException is a runtime exception and therefore we 
 * want to have to handle the issue.
 * 
 * @author sgreenberg@gopivotal.com
 *
 */
public class DeliveryPipelineServiceException extends ServiceBrokerException {

	private static final long serialVersionUID = 8667141725171626000L;

	public DeliveryPipelineServiceException(String message) {
		super(message);
	}
	
}
