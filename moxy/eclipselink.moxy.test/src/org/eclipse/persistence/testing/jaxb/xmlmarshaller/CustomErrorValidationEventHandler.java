/*******************************************************************************
 * Copyright (c) 1998, 2010 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the 
 * terms of the Eclipse Public License v1.0 and Eclipse Distribution License v. 1.0 
 * which accompanies this distribution. 
 * The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html
 * and the Eclipse Distribution License is available at 
 * http://www.eclipse.org/org/documents/edl-v10.php.
 *
 * Contributors:
 *     Oracle - initial API and implementation from Oracle TopLink
 ******************************************************************************/  
package org.eclipse.persistence.testing.jaxb.xmlmarshaller;

import javax.xml.bind.ValidationEventHandler;
import javax.xml.bind.ValidationEvent;

public class CustomErrorValidationEventHandler implements ValidationEventHandler {
  public boolean handleEvent(ValidationEvent event) {

		if(event.getSeverity() != ValidationEvent.ERROR)
		{
			return false;
		}
		
    Exception ex = (Exception)event.getLinkedException();
    String message = ex.getMessage();
    System.out.println("Handling Event: " + message);
    if(message.indexOf("Invalid ") != -1) {
      System.out.println("Recoverable Error, Ignoring...");
      return true;
    }
    return false;
  }
}

