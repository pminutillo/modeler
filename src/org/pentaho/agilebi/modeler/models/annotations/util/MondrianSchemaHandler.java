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

package org.pentaho.agilebi.modeler.models.annotations.util;

import mondrian.olap.MondrianDef;
import org.pentaho.agilebi.modeler.ModelerException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

/**
 * This class will attempt to encapsulate and abstract from the user
 * the nuances of manipulating an existing Mondrian schema
 *
 * Created by pminutillo on 3/7/15.
 */
public class MondrianSchemaHandler {
  private static final String CUBE_XPATH_EXPR = "/Schema/Cube";

  public static final String MEASURE_ELEMENT_NAME = "Measure";
  public static final String MEASURE_NAME_ATTRIBUTE = "name";
  public static final String MEASURE_COLUMN_ATTRIBUTE = "column";
  public static final String MEASURE_AGGREGATOR_ATTRIBUTE = "aggregator";

  public static final String CALCULATED_MEMBER_ELEMENT_NAME = "CalculatedMember";
  public static final String CALCULATED_MEMBER_CAPTION_ATTRIBUTE = "caption";
  public static final String CALCULATED_MEMBER_DESCRIPTION_ATTRIBUTE = "description";
  public static final String CALCULATED_MEMBER_DIMENSION_ATTRIBUTE = "dimension";
  public static final String CALCULATED_MEMBER_FORMULA_ATTRIBUTE = "formula";
  public static final String CALCULATED_MEMBER_NAME_ATTRIBUTE = "name";
  public static final String CALCULATED_MEMBER_VISIBLE_ATTRIBUTE = "visible";

  private Document schema;

  public MondrianSchemaHandler(){

  }

  public MondrianSchemaHandler( Document schema ){
    this.schema = schema;
  }

  /**
   *
   * @param cubeName
   * @param measure
   * @throws ModelerException
   */
  public void addMeasure( String cubeName, MondrianDef.Measure measure) throws ModelerException {
    try {
      XPathFactory xPathFactory = XPathFactory.newInstance();
      XPath xPath = xPathFactory.newXPath();
      StringBuffer xPathExpr = new StringBuffer();
      xPathExpr.append( CUBE_XPATH_EXPR ); // TODO: Handle multiple cubes...
      XPathExpression xPathExpression = xPath.compile( xPathExpr.toString() );
      Node cube = (Node) xPathExpression.evaluate( this.schema, XPathConstants.NODE );
      Element measureElement;
      measureElement = this.schema.createElement( MEASURE_ELEMENT_NAME );

      // check if cube contains calculated members
      NodeList calculatedMemberNodeList = this.schema.getElementsByTagName( CALCULATED_MEMBER_ELEMENT_NAME );
      if( ( calculatedMemberNodeList != null )&&( calculatedMemberNodeList.getLength() > 0 ) ){
        // get the first calculated member
        Node firstCalculatedMemberNode = calculatedMemberNodeList.item( 0 );
        // insert measure before the first calculated member
        cube.insertBefore( measureElement, firstCalculatedMemberNode );
      }
      else{
        cube.appendChild( measureElement );
      }

      measureElement.setAttribute( MEASURE_NAME_ATTRIBUTE, measure.name);
      measureElement.setAttribute( MEASURE_COLUMN_ATTRIBUTE, measure.column );
      measureElement.setAttribute( MEASURE_AGGREGATOR_ATTRIBUTE,  measure.aggregator );
    } catch ( XPathExpressionException e ) {
      throw new ModelerException( e );
    }

  }

  /**
   *
   * @param cubeName
   * @param calculatedMember
   * @throws ModelerException
   */
  public void addCalculatedMember( String cubeName, MondrianDef.CalculatedMember calculatedMember ) throws ModelerException {
    try {
      XPathFactory xPathFactory = XPathFactory.newInstance();
      XPath xPath = xPathFactory.newXPath();
      StringBuffer xPathExpr = new StringBuffer();
      xPathExpr.append( CUBE_XPATH_EXPR ); // TODO: Handle multiple cubes...
      XPathExpression xPathExpression = xPath.compile( xPathExpr.toString() );
      Node cube = (Node) xPathExpression.evaluate( this.schema, XPathConstants.NODE );
      Element measureElement;
      measureElement = this.schema.createElement( CALCULATED_MEMBER_ELEMENT_NAME );
      cube.appendChild( measureElement ); // TODO: Measures need to come after calculated measures
      measureElement.setAttribute( CALCULATED_MEMBER_NAME_ATTRIBUTE, calculatedMember.name );
      measureElement.setAttribute( CALCULATED_MEMBER_CAPTION_ATTRIBUTE, calculatedMember.caption );
      measureElement.setAttribute( CALCULATED_MEMBER_DESCRIPTION_ATTRIBUTE, calculatedMember.description );
      measureElement.setAttribute( CALCULATED_MEMBER_DIMENSION_ATTRIBUTE, calculatedMember.dimension );
      measureElement.setAttribute( CALCULATED_MEMBER_FORMULA_ATTRIBUTE, calculatedMember.formula );
      measureElement.setAttribute( CALCULATED_MEMBER_VISIBLE_ATTRIBUTE, Boolean.toString( calculatedMember.visible ) );
    } catch ( XPathExpressionException e ) {
      throw new ModelerException( e );
    }
  }

  public Document getSchema() {
    return schema;
  }

  public void setSchema( Document schema ) {
    this.schema = schema;
  }
}
