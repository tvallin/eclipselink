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
 *     05/16/2008-1.0M8 Guy Pelletier 
 *       - 218084: Implement metadata merging functionality between mapping files
 *     12/12/2008-1.1 Guy Pelletier 
 *       - 249860: Implement table per class inheritance support.
 ******************************************************************************/  
package org.eclipse.persistence.internal.jpa.metadata.accessors.mappings;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.persistence.exceptions.ValidationException;
import org.eclipse.persistence.internal.helper.DatabaseField;
import org.eclipse.persistence.internal.jpa.metadata.MetadataLogger;

import org.eclipse.persistence.internal.jpa.metadata.accessors.classes.ClassAccessor;
import org.eclipse.persistence.internal.jpa.metadata.accessors.objects.MetadataAccessibleObject;

import org.eclipse.persistence.mappings.OneToOneMapping;

/**
 * A one to one relationship accessor. A OneToOne annotation currently is not
 * required to be on the accessible object, that is, a 1-1 can default.
 * 
 * @author Guy Pelletier
 * @since TopLink EJB 3.0 Reference Implementation
 */
public class OneToOneAccessor extends ObjectAccessor {
    private String m_mappedBy;
    
    /**
     * INTERNAL:
     * Used for OX mapping.
     */
    public OneToOneAccessor() {
        super("<one-to-one>");
    }
    
    /**
     * INTERNAL:
     */
    public OneToOneAccessor(Annotation oneToOne, MetadataAccessibleObject accessibleObject, ClassAccessor classAccessor) {
        super(oneToOne, accessibleObject, classAccessor);
        
        m_mappedBy = (oneToOne == null) ? "" : (String) MetadataHelper.invokeMethod("mappedBy", oneToOne);
    }
    
    /**
     * INTERNAL:
     * Return the logging context for this accessor.
     */
    protected String getLoggingContext() {
        return MetadataLogger.ONE_TO_ONE_MAPPING_REFERENCE_CLASS;
    }
    
    /**
     * INTERNAL:
     * Used for OX mapping.
     */
    public String getMappedBy() {
        return m_mappedBy;
    }
    
    /**
     * INTERNAL:
     */
    @Override
    public boolean isOneToOne() {
        return true;
    }
    
    /**
     * INTERNAL:
     * Process a one to one setting into an EclipseLink OneToOneMapping.
     */
    public void process() {
        // Initialize our mapping now with what we found.
        OneToOneMapping mapping = initOneToOneMapping();
        
        if (m_mappedBy == null || m_mappedBy.equals("")) {
            // Owning side, look for JoinColumns or PrimaryKeyJoinColumns.
            processOwningMappingKeys(mapping);
        } else {    
            // Non-owning side, process the foreign keys from the owner.
            OneToOneMapping ownerMapping = null;
            if (getOwningMapping(m_mappedBy).isOneToOneMapping()){
                ownerMapping = (OneToOneMapping)getOwningMapping(m_mappedBy);
            } else {
                // If improper mapping encountered, throw an exception.
                throw ValidationException.invalidMapping(getJavaClass(), getReferenceClass());
            }

            Map<DatabaseField, DatabaseField> targetToSourceKeyFields;
            Map<DatabaseField, DatabaseField> sourceToTargetKeyFields;
            
            // If we are within a table per class strategy we have to update
            // the primary key field to point to our own database table. 
            if (getDescriptor().usesTablePerClassInheritanceStrategy()) {
                targetToSourceKeyFields = new HashMap<DatabaseField, DatabaseField>();
                sourceToTargetKeyFields = new HashMap<DatabaseField, DatabaseField>();
                
                for (DatabaseField fkField : ownerMapping.getSourceToTargetKeyFields().keySet()) {
                    // We need to update the pk field to be to our table.
                    DatabaseField pkField = (DatabaseField) ownerMapping.getSourceToTargetKeyFields().get(fkField).clone();
                    pkField.setTable(getDescriptor().getPrimaryTable());
                    sourceToTargetKeyFields.put(fkField, pkField);
                    targetToSourceKeyFields.put(pkField, fkField);
                }
            } else {
                targetToSourceKeyFields = ownerMapping.getTargetToSourceKeyFields();
                sourceToTargetKeyFields = ownerMapping.getSourceToTargetKeyFields();
            }
            
            mapping.setSourceToTargetKeyFields(targetToSourceKeyFields);
            mapping.setTargetToSourceKeyFields(sourceToTargetKeyFields);
        }
        
        // Process properties
        processProperties(mapping);

        // Add the mapping to the descriptor.
        getDescriptor().addMapping(mapping);
    }
    
    /**
     * INTERNAL:
     * Used for OX mapping.
     */
    public void setMappedBy(String mappedBy) {
        m_mappedBy = mappedBy;
    }
}
