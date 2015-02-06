package org.pentaho.agilebi.modeler.models.annotations;

import org.apache.commons.lang.StringUtils;
import org.pentaho.agilebi.modeler.ModelerException;
import org.pentaho.agilebi.modeler.ModelerWorkspace;
import org.pentaho.di.i18n.BaseMessages;
import org.pentaho.metastore.persist.MetaStoreAttribute;
import org.pentaho.metastore.persist.MetaStoreElementType;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

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

  public static final String NAME_ID = "catalogName";
  public static final String NAME_NAME = "Catalog Name";
  public static final int NAME_ORDER = 0;

  public static final String NEW_NAME_ID = "newCatalogName";
  public static final String NEW_NAME_NAME = "New Catalog Name";
  public static final int NEW_NAME_ORDER = 1;

  @MetaStoreAttribute
  @ModelProperty( id = NAME_ID, name = NAME_NAME, order = NAME_ORDER )
  private String catalogName;

  @MetaStoreAttribute
  @ModelProperty( id = NEW_NAME_ID, name = NEW_NAME_NAME, order = NEW_NAME_ORDER )
  private String newCatalogName;

  @Override public boolean apply( ModelerWorkspace workspace, String field ) throws ModelerException {
    return false;
  }

  @Override public boolean apply( Document schema, String field ) throws ModelerException {
    if( schema == null ){
      return false;
    }

    NodeList schemaElements = schema.getElementsByTagName( SCHEMA_TAG_NAME );

    if( ( schemaElements == null ) ||
      ( schemaElements.getLength() <= 0 ) ) {
      return false;
    }

    Node schemaNode = schemaElements.item( 0 );
    NamedNodeMap namedNodeMap = schemaNode.getAttributes();
    Node nameNode = namedNodeMap.getNamedItem( SCHEMA_NODE_NAME_ATTRIBUTE );
    if ( nameNode != null ) {
      nameNode.setNodeValue( getNewCatalogName() );
      return true;
    }

    return false;
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
}
