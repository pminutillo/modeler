/*!
 * PENTAHO CORPORATION PROPRIETARY AND CONFIDENTIAL
 *
 * Copyright 2002 - 2014 Pentaho Corporation (Pentaho). All rights reserved.
 *
 * NOTICE: All information including source code contained herein is, and
 * remains the sole property of Pentaho and its licensors. The intellectual
 * and technical concepts contained herein are proprietary and confidential
 * to, and are trade secrets of Pentaho and may be covered by U.S. and foreign
 * patents, or patents in process, and are protected by trade secret and
 * copyright laws. The receipt or possession of this source code and/or related
 * information does not convey or imply any rights to reproduce, disclose or
 * distribute its contents, or to manufacture, use, or sell anything that it
 * may describe, in whole or in part. Any reproduction, modification, distribution,
 * or public display of this information without the express written authorization
 * from Pentaho is strictly prohibited and in violation of applicable laws and
 * international treaties. Access to the source code contained herein is strictly
 * prohibited to anyone except those individuals and entities who have executed
 * confidentiality and non-disclosure agreements or other agreements with Pentaho,
 * explicitly covering such access.
 */
package org.pentaho.agilebi.modeler.models.annotations;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.commons.lang.StringUtils;
import org.pentaho.agilebi.modeler.BaseModelerWorkspaceHelper;
import org.pentaho.agilebi.modeler.ModelerException;
import org.pentaho.agilebi.modeler.ModelerPerspective;
import org.pentaho.agilebi.modeler.ModelerWorkspace;
import org.pentaho.agilebi.modeler.models.annotations.util.MondrianSchemaHandler;
import org.pentaho.agilebi.modeler.models.annotations.util.schema.Schema;
import org.pentaho.agilebi.modeler.nodes.MeasureMetaData;
import org.pentaho.di.i18n.BaseMessages;
import org.pentaho.metadata.automodel.PhysicalTableImporter;
import org.pentaho.metadata.model.LogicalColumn;
import org.pentaho.metadata.model.LogicalTable;
import org.pentaho.metadata.model.SqlPhysicalColumn;
import org.pentaho.metadata.model.concept.types.AggregationType;
import org.pentaho.metadata.util.MondrianModelExporter;
import org.pentaho.metastore.persist.MetaStoreAttribute;
import org.pentaho.metastore.persist.MetaStoreElementType;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * @author Rowell Belen
 */
@MetaStoreElementType( name = "CreateMeasure", description = "CreateMeasure Annotation" )
public class CreateMeasure extends AnnotationType {

  private static final long serialVersionUID = -2487305952482463126L;

  public static final String NAME_ID = "name";
  public static final String NAME_NAME = "Measure Name";
  public static final int NAME_ORDER = 0;

  public static final String AGGREGATE_TYPE_ID = "aggregateType";
  public static final String AGGREGATE_TYPE_NAME = "Aggregation Type";
  public static final int AGGREGATE_TYPE_ORDER = 1;

  public static final String FORMAT_STRING_ID = "formatString";
  public static final String FORMAT_STRING_NAME = "Format String";
  public static final int FORMAT_STRING_ORDER = 2;

  public static final String DESCRIPTION_ID = "description";
  public static final String DESCRIPTION_NAME = "Description";
  public static final int DESCRIPTION_ORDER = 3;

  public static final String BUSINESS_GROUP_ID = "businessGroup";
  public static final String BUSINESS_GROUP_NAME = "Business Group";
  public static final int BUSINESS_GROUP_ORDER = 4;

  @MetaStoreAttribute
  @ModelProperty( id = NAME_ID, name = NAME_NAME, order = NAME_ORDER )
  private String name;

  @MetaStoreAttribute
  @ModelProperty( id = AGGREGATE_TYPE_ID, name = AGGREGATE_TYPE_NAME, order = AGGREGATE_TYPE_ORDER )
  private AggregationType aggregateType;

  @MetaStoreAttribute
  @ModelProperty( id = FORMAT_STRING_ID, name = FORMAT_STRING_NAME, order = FORMAT_STRING_ORDER )
  private String formatString;

  @MetaStoreAttribute
  @ModelProperty( id = DESCRIPTION_ID, name = DESCRIPTION_NAME, order = DESCRIPTION_ORDER )
  private String description;

  @MetaStoreAttribute
  // Do not expose business group in the UI (for now)
  //@ModelProperty( id = BUSINESS_GROUP_ID, name = BUSINESS_GROUP_NAME, order = BUSINESS_GROUP_ORDER )
  private String businessGroup;

  public String getName() {
    return name;
  }

  public void setName( String name ) {
    this.name = name;
  }

  public AggregationType getAggregateType() {
    return aggregateType;
  }

  public void setAggregateType( AggregationType aggregateType ) {
    this.aggregateType = aggregateType;
  }

  public String getFormatString() {
    return formatString;
  }

  public void setFormatString( String formatString ) {
    this.formatString = formatString;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription( String description ) {
    this.description = description;
  }

  public String getBusinessGroup() {
    return businessGroup;
  }

  public void setBusinessGroup( String businessGroup ) {
    this.businessGroup = businessGroup;
  }

  @Override
  public boolean apply( final ModelerWorkspace workspace, final String column ) throws ModelerException {
    List<LogicalTable> logicalTables = workspace.getLogicalModel( ModelerPerspective.ANALYSIS ).getLogicalTables();
    for ( LogicalTable logicalTable : logicalTables ) {
      List<LogicalColumn> logicalColumns = logicalTable.getLogicalColumns();
      for ( LogicalColumn logicalColumn : logicalColumns ) {
        if ( columnMatches( workspace, column, logicalColumn )
            || columnMatches( workspace, PhysicalTableImporter.beautifyName( column ), logicalColumn ) )
        {
          String targetColumn =
              (String) logicalColumn.getPhysicalColumn().getProperty( SqlPhysicalColumn.TARGET_COLUMN );
          MeasureMetaData measureMetaData =
              new MeasureMetaData( targetColumn, getFormatString(), getName(), workspace.getWorkspaceHelper().getLocale() );

          LogicalColumn columnClone = (LogicalColumn) logicalColumn.clone();
          columnClone.setId( BaseModelerWorkspaceHelper.uniquify( columnClone.getId(), logicalColumns ) );
          measureMetaData.setLogicalColumn( columnClone );
          measureMetaData.setName( getName() );
          measureMetaData.setDefaultAggregation( getAggregateType() );
          removeAutoMeasure( workspace, column );
          removeMeasure( workspace, getName() );
          workspace.getModel().getMeasures().add( measureMetaData );
          removeAutoLevel( workspace, locateLevel( workspace, column ) );
          workspace.getWorkspaceHelper().populateDomain( workspace );
          return true;
        }
      }

    }
    throw new ModelerException( "Unable to apply Create Measure annotation: Column not found" );
  }

  private boolean columnMatches( final ModelerWorkspace workspace, final String column,
                                 final LogicalColumn logicalColumn ) {
    return column.equalsIgnoreCase(
        logicalColumn.getName( workspace.getWorkspaceHelper().getLocale() ) );
  }

  private void removeAutoMeasure( final ModelerWorkspace workspace, final String column ) {
    LogicalColumn logicalColumn = locateLogicalColumn( workspace, column );
    String locale = workspace.getWorkspaceHelper().getLocale();
    for ( MeasureMetaData measure : workspace.getModel().getMeasures() ) {
      if ( measure.getName().equals( column )
          && measure.getLogicalColumn().getPhysicalColumn().getName( locale ).equals(
          logicalColumn.getPhysicalColumn().getName( locale ) )
          && measure.getDefaultAggregation().equals( AggregationType.SUM ) ) {
        workspace.getModel().getMeasures().remove( measure );
        break;
      }
    }
  }

  private void removeMeasure( final ModelerWorkspace workspace, final String measureName ) {
    for ( MeasureMetaData measure : workspace.getModel().getMeasures() ) {
      if ( measure.getName().equals( measureName ) ) {
        workspace.getModel().getMeasures().remove( measure );
        break;
      }
    }
  }

  @Override
  public boolean apply( Document doc, String field ) throws ModelerException {
    // Surgically add the measure into the cube...
    MondrianSchemaHandler mondrianSchemaHandler = new MondrianSchemaHandler( doc );

    Schema.Cube.Measure measure = new Schema.Cube.Measure();
    measure.setName( this.getName() );
    measure.setColumn( field );
    measure.setAggregator( MondrianModelExporter.convertToMondrian( getAggregateType() ) );

    mondrianSchemaHandler.addMeasure( null, measure );

    return true;
  }

  @Override
  public void populate( final Map<String, Serializable> propertiesMap ) {

    super.populate( propertiesMap ); // let base class handle primitives, etc.

    // correctly convert aggregate type
    if ( propertiesMap.containsKey( AGGREGATE_TYPE_ID ) ) {
      Serializable value = propertiesMap.get( AGGREGATE_TYPE_ID );
      if ( value != null ) {
        setAggregateType( AggregationType.valueOf( value.toString() ) );
      }
    }
  }

  @Override
  public ModelAnnotation.Type getType() {
    return ModelAnnotation.Type.CREATE_MEASURE;
  }

  @Override public String getSummary() {
    if ( getAggregateType() != null ) {
      return BaseMessages.getString( MSG_CLASS, "Modeler.CreateMeasure.Summary", getName(), getAggregateType().name() );
    } else {
      return BaseMessages.getString( MSG_CLASS, "Modeler.CreateMeasure.NoAggregateSummary", getName() );
    }
  }

  @Override
  public void validate() throws ModelerException {

    if ( StringUtils.isBlank( getName() ) ) {
      throw new ModelerException( BaseMessages
          .getString( MSG_CLASS, "ModelAnnotation.CreateMeasure.validation.MEASURE_NAME_REQUIRED" ) );
    }
  }
}
