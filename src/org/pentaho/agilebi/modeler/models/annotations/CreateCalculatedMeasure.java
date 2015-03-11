/*!
 * PENTAHO CORPORATION PROPRIETARY AND CONFIDENTIAL
 *
 * Copyright 2002 - 2015 Pentaho Corporation (Pentaho). All rights reserved.
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

import org.apache.commons.lang.StringUtils;
import org.pentaho.agilebi.modeler.ModelerException;
import org.pentaho.agilebi.modeler.ModelerWorkspace;
import org.pentaho.agilebi.modeler.models.annotations.util.MondrianSchemaHandler;
import org.pentaho.agilebi.modeler.models.annotations.util.NumberFormat;
import org.pentaho.agilebi.modeler.models.annotations.util.schema.CalculatedMember;
import org.pentaho.di.i18n.BaseMessages;
import org.pentaho.metastore.persist.MetaStoreAttribute;
import org.pentaho.metastore.persist.MetaStoreElementType;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.util.logging.Logger;

/**
 * Annotation to create a calculated measure
 * <p/>
 * Created by pminutillo on 2/2/15.
 */
@MetaStoreElementType( name = "CreatedCalculatedMeasure", description = "CreatedCalculatedMeasure Annotation" )
public class CreateCalculatedMeasure extends AnnotationType {
  private static final long serialVersionUID = 5169827225345800226L;
  private static transient Logger logger = Logger.getLogger( AnnotationType.class.getName() );

  private static final String NAME_ID = "name";
  private static final String NAME_NAME = "Name";
  private static final int NAME_ORDER = 0;

  private static final String CAPTION_ID = "caption";
  private static final String CAPTION_NAME = "Caption";
  private static final int CAPTION_ORDER = 0;

  private static final String DESCRIPTION_ID = "description";
  private static final String DESCRIPTION_NAME = "Description";
  private static final int DESCRIPTION_ORDER = 0;

  private static final String FORMULA_ID = "formula";
  private static final String FORMULA_NAME = "Formula";
  private static final int FORMULA_ORDER = 0;

  private static final String DIMENSION_ID = "dimension";
  private static final String DIMENSION_NAME = "Dimension";
  private static final int DIMENSION_ORDER = 0;

  private static final String VISIBLE_ID = "visible";
  private static final String VISIBLE_NAME = "Visible";
  private static final int VISIBLE_ORDER = 0;

  public static final String NUMBER_FORMAT_ID = "numberFormat";
  public static final String NUMBER_FORMAT_NAME = "Number Format";
  public static final int NUMBER_FORMAT_ORDER = 1;

  public static final String DECIMAL_PLACES_ID = "decimalPlaces";
  public static final String DECIMAL_PLACES_NAME = "Decimal Places";
  public static final int DECIMAL_PLACES_ORDER = 2;

  public static final String MEASURE_CONTENT_ID = "measureContent";
  public static final String MEASURE_CONTENT_NAME = "Measure Content";
  public static final int MEASURE_CONTENT_ORDER = 3;

  public static final String CALCULATE_SUBTOTALS_ID = "calculateSubtotals";
  public static final String CALCULATE_SUBTOTALS_NAME = "Calculate Subtotals";
  public static final int CALCULATE_SUBTOTALS_ORDER = 4;

  public static final String CATALOG_NAME_ID = "catalogName";
  public static final String CATALOG_NAME_NAME = "Catalog Name";
  public static final int CATALOG_NAME_ORDER = 5;

  public static final String CALCULATED_MEMBER_NODE_NAME = "CalculatedMember";
  public static final String CALCULATED_MEMBER_NAME_ATTRIB = "name";
  public static final String CALCULATED_MEMBER_COLUMN_ATTRIB = "column";
  public static final String CALCULATED_MEMBER_AGGREGATOR_ATTRIB = "aggregator";
  public static final String CUBE_XPATH_EXPR = "/Schema/Cube";

  @MetaStoreAttribute
  @ModelProperty( id = NAME_ID, name = NAME_NAME, order = NAME_ORDER )
  private String name;

  @MetaStoreAttribute
  @ModelProperty( id = CAPTION_ID, name = CAPTION_NAME, order = CAPTION_ORDER )
  private String caption;

  @MetaStoreAttribute
  @ModelProperty( id = DESCRIPTION_ID, name = DESCRIPTION_NAME, order = DESCRIPTION_ORDER )
  private String description;

  @MetaStoreAttribute
  @ModelProperty( id = FORMULA_ID, name = FORMULA_NAME, order = FORMULA_ORDER )
  private String formula;

  @MetaStoreAttribute
  @ModelProperty( id = DIMENSION_ID, name = DIMENSION_NAME, order = DIMENSION_ORDER )
  private String dimension;

  @MetaStoreAttribute
  @ModelProperty( id = VISIBLE_ID, name = VISIBLE_NAME, order = VISIBLE_ORDER )
  private boolean visible;

  @MetaStoreAttribute
  @ModelProperty( id = NUMBER_FORMAT_ID, name = NUMBER_FORMAT_NAME, order = NUMBER_FORMAT_ORDER )
  private NumberFormat numberFormat;

  @MetaStoreAttribute
  @ModelProperty( id = DECIMAL_PLACES_ID, name = DECIMAL_PLACES_NAME, order = DECIMAL_PLACES_ORDER )
  private int decimalPlaces;

  @MetaStoreAttribute
  @ModelProperty( id = MEASURE_CONTENT_ID, name = MEASURE_CONTENT_NAME, order = MEASURE_CONTENT_ORDER )
  private String measureContent;

  @MetaStoreAttribute
  @ModelProperty( id = CALCULATE_SUBTOTALS_ID, name = CALCULATE_SUBTOTALS_NAME, order = CALCULATE_SUBTOTALS_ORDER)
  private boolean calculateSubtotals;

  /**
   * @param workspace
   * @param field
   * @return
   * @throws org.pentaho.agilebi.modeler.ModelerException
   */
  @Override public boolean apply( ModelerWorkspace workspace, String field ) throws ModelerException {

    return true;

  }

  /**
  /**
   * @param schema
   * @param field
   * @return
   * @throws org.pentaho.agilebi.modeler.ModelerException
   */
  @Override public boolean apply( Document schema, String field ) throws ModelerException {
    if ( schema == null ) {
      return false;
    }

    MondrianSchemaHandler mondrianSchemaHandler = new MondrianSchemaHandler( schema );

    CalculatedMember calculatedMember = new CalculatedMember();
    calculatedMember.setName( this.getName() );
    calculatedMember.setCaption( this.getCaption() );
    calculatedMember.setDescription( this.getDescription() );
    calculatedMember.setDimension( this.getDimension() );
    calculatedMember.setFormula( this.getFormula() );
    calculatedMember.setVisible( this.isVisible() );

    mondrianSchemaHandler.addCalculatedMeasure( null, calculatedMember );

    return true;
  }

  private void insertCalculatedMeasureNode( Node cube, Element measureElement ){

  }

  @Override public void validate() throws ModelerException {
    if ( StringUtils.isBlank( getName() ) ) {
      throw new ModelerException( BaseMessages
        .getString( MSG_CLASS, "ModelAnnotation.CreateCalculatedMeasure.validation.CATALOG_NAME_REQUIRED" ) );
    }
  }

  @Override public ModelAnnotation.Type getType() {
    return ModelAnnotation.Type.CREATE_CALCULATED_MEASURE;
  }

  @Override public String getSummary() {
    return BaseMessages.getString( MSG_CLASS, "Modeler.CreateCalculatedMeasure.Summary", getName() );
  }

  public String getName() {
    return name;
  }

  public void setName( String name ) {
    this.name = name;
  }

  public String getCaption() {
    return caption;
  }

  public void setCaption( String caption ) {
    this.caption = caption;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription( String description ) {
    this.description = description;
  }

  public String getFormula() {
    return formula;
  }

  public void setFormula( String formula ) {
    this.formula = formula;
  }

  public String getDimension() {
    return dimension;
  }

  public void setDimension( String dimension ) {
    this.dimension = dimension;
  }

  public boolean isVisible() {
    return visible;
  }

  public void setVisible( boolean visible ) {
    this.visible = visible;
  }

  public NumberFormat getNumberFormat() {
    return numberFormat;
  }

  public void setNumberFormat( NumberFormat numberFormat ) {
    this.numberFormat = numberFormat;
  }

  public int getDecimalPlaces() {
    return decimalPlaces;
  }

  public void setDecimalPlaces( int decimalPlaces ) {
    this.decimalPlaces = decimalPlaces;
  }

  public void setMeasureContent( String measureContent ) {
    this.measureContent = measureContent;
  }

  public boolean isCalculateSubtotals() {
    return calculateSubtotals;
  }

  public void setCalculateSubtotals( boolean calculateSubtotals ) {
    this.calculateSubtotals = calculateSubtotals;
  }
}
