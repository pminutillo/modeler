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
import org.pentaho.agilebi.modeler.ModelerPerspective;
import org.pentaho.agilebi.modeler.ModelerWorkspace;
import org.pentaho.agilebi.modeler.util.ModelerWorkspaceUtil;
import org.pentaho.di.i18n.BaseMessages;
import org.pentaho.metadata.model.Domain;
import org.pentaho.metadata.model.LogicalModel;
import org.pentaho.metadata.model.concept.types.LocalizedString;
import org.pentaho.metastore.persist.MetaStoreAttribute;
import org.pentaho.metastore.persist.MetaStoreElementType;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.logging.Logger;

/**
 * Annotation to rename a catalog
 *
 * Created by pminutillo on 2/2/15.
 */
@MetaStoreElementType( name = "RenameCatalog", description = "RenameCatalog Annotation" )
public class RenameCatalog extends AnnotationType {
  private static final long serialVersionUID = 5169827225345800226L;
  private static transient Logger logger = Logger.getLogger( AnnotationType.class.getName() );

  private static String SCHEMA_TAG_NAME = "Schema";
  private static String SCHEMA_NODE_NAME_ATTRIBUTE = "name";

  private static String OLAP_MODEL_NAME_PROPERTY = "name";
  private static String OLAP_MODEL_CUBES_PROPERTY = "olap_cubes";

  public static final String NAME_ID = "catalogName";
  public static final String NAME_NAME = "Catalog Name";
  public static final int NAME_ORDER = 0;

  public static final String NEW_NAME_ID = "newCatalogName";
  public static final String NEW_NAME_NAME = "New Catalog Name";
  public static final int NEW_NAME_ORDER = 1;

  private static String MONDRIAN_CATALOG_REF = "MondrianCatalogRef";

  @MetaStoreAttribute
  @ModelProperty( id = NAME_ID, name = NAME_NAME, order = NAME_ORDER )
  private String catalogName;

  @MetaStoreAttribute
  @ModelProperty( id = NEW_NAME_ID, name = NEW_NAME_NAME, order = NEW_NAME_ORDER )
  private String newCatalogName;

  /**
   *
   * @param workspace
   * @param field
   * @return
   * @throws ModelerException
   */
  @Override public boolean apply( ModelerWorkspace workspace, String field ) throws ModelerException {
    // also need to update mondrian catalog
//    try {
//      String mondrianSchemaXml =
//        ModelerWorkspaceUtil.getMondrianSchemaXml( workspace, workspace.getWorkspaceHelper().getLocale() );
//
//      Document mondrianSchemaDoc = parseXmlToDoc( new ByteArrayInputStream( mondrianSchemaXml.getBytes() ) );
//      updateSchemaNodeName( mondrianSchemaDoc );
//    }
//    catch( Exception e ){
//
//    }
//
////    Domain domain = workspace.getDomain();
//    LogicalModel olapModel = workspace.getLogicalModel( ModelerPerspective.ANALYSIS );
//
//    // set mondrian catalog ref
//    olapModel.setProperty( MONDRIAN_CATALOG_REF, getNewCatalogName() );
//
//    // set localized name property
//    olapModel.setProperty( OLAP_MODEL_NAME_PROPERTY,
//      new LocalizedString(
//        workspace.getWorkspaceHelper().getLocale(),
//        getNewCatalogName()
//      )
//    );
//
//    workspace.getWorkspaceHelper().populateDomain( workspace );

    // set olap_cubes
    // OLAP_MODEL_CUBES_PROPERTY


    return true;

  }

  /**
   *
   * @param schema
   * @param field
   * @return
   * @throws ModelerException
   */
  @Override public boolean apply( Document schema, String field ) throws ModelerException {
    if( schema == null ){
      return false;
    }

    updateSchemaNodeName( schema );

    return true;
  }

  /**
   *
   * @param schema
   * @return
   */
  private Document updateSchemaNodeName( Document schema ){
    NodeList schemaElements = schema.getElementsByTagName( SCHEMA_TAG_NAME );

    if( ( schemaElements == null ) ||
      ( schemaElements.getLength() <= 0 ) ) {
      return null;
    }

    Node schemaNode = schemaElements.item( 0 );
    NamedNodeMap namedNodeMap = schemaNode.getAttributes();
    Node nameNode = namedNodeMap.getNamedItem( SCHEMA_NODE_NAME_ATTRIBUTE );
    if ( nameNode != null ) {
      nameNode.setNodeValue( getNewCatalogName() );
    }

    return schema;
  }

  @Override public void validate() throws ModelerException {
    if( StringUtils.isBlank( getCatalogName() ) ){
      throw new ModelerException( BaseMessages
        .getString( MSG_CLASS, "ModelAnnotation.RenameCatalog.validation.CATALOG_NAME_REQUIRED" ) );
    }

    if( StringUtils.isBlank( getNewCatalogName() ) ){
      throw new ModelerException( BaseMessages
        .getString( MSG_CLASS, "ModelAnnotation.RenameCatalog.validation.NEW_CATALOG_NAME_REQUIRED" ) );
    }
  }

  @Override public ModelAnnotation.Type getType() {
    return ModelAnnotation.Type.RENAME_CATALOG;
  }

  @Override public String getSummary() {
    return BaseMessages.getString( MSG_CLASS, "Modeler.RenameCatalog.Summary", getCatalogName(), getNewCatalogName() );
  }

  @Override public String getName() {
    return null;
  }

  public String getCatalogName() {
    return catalogName;
  }

  public void setCatalogName( String catalogName ) {
    this.catalogName = catalogName;
  }

  public String getNewCatalogName() {
    return newCatalogName;
  }

  public void setNewCatalogName( String newCatalogName ) {
    this.newCatalogName = newCatalogName;
  }

  private Document parseXmlToDoc( InputStream inputStream ) throws Exception {
    Document result;

    DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
    DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
    result = dBuilder.parse( inputStream );
    inputStream.close();
    result.getDocumentElement().normalize();

    return result;
  }
}
