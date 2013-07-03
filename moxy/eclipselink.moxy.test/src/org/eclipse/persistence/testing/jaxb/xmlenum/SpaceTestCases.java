/*******************************************************************************
 * Copyright (c) 2013 Oracle and/or its affiliates. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 and Eclipse Distribution License v. 1.0
 * which accompanies this distribution.
 * The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html
 * and the Eclipse Distribution License is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 *
 * Contributors:
 *     Blaise Doughan - 2.5.1 - initial implementation
 ******************************************************************************/
package org.eclipse.persistence.testing.jaxb.xmlenum;

import java.io.InputStream;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.persistence.testing.jaxb.JAXBWithJSONTestCases;
import org.w3c.dom.Document;

public class SpaceTestCases extends JAXBWithJSONTestCases {
    private final static String XML_RESOURCE = "org/eclipse/persistence/testing/jaxb/xmlenum/space.xml";
    private final static String XML_RESOURCE_WRITE = "org/eclipse/persistence/testing/jaxb/xmlenum/space_write.xml";
    private final static String JSON_RESOURCE = "org/eclipse/persistence/testing/jaxb/xmlenum/space.json";
    private final static String JSON_RESOURCE_WRITE = "org/eclipse/persistence/testing/jaxb/xmlenum/space_write.json";

    public SpaceTestCases(String name) throws Exception {
        super(name);
        setControlDocument(XML_RESOURCE);
        setWriteControlDocument(XML_RESOURCE_WRITE);
        setControlJSON(JSON_RESOURCE);
        Class[] classes = new Class[1];
        classes[0] = SpaceRoot.class;
        setClasses(classes);
    }

    protected SpaceRoot getControlObject() {
        SpaceRoot root = new SpaceRoot();
        root.spaceEnum = SpaceEnum.FOO;
        return root;
    }

    @Override
    public String getWriteControlJSON() {
        return JSON_RESOURCE_WRITE;
    }

}