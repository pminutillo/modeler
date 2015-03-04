//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.1.2-b01-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.02.19 at 02:03:45 PM EST 
//


package org.pentaho.agilebi.modeler.models.annotations.util;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;
import java.math.BigInteger;


/**
 * <p>Java class for NumberFormat complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="NumberFormat">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="formatExpression" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="formatCategory" type="{http://www.pentaho.com}FormatCategoryEnum" default="Default" />
 *       &lt;attribute name="formatScale" type="{http://www.pentaho.com}FormatScaleEnum" default="0" />
 *       &lt;attribute name="formatShortcut" type="{http://www.pentaho.com}FormatShortcutEnum" default="NONE" />
 *       &lt;attribute name="currencySymbol" type="{http://www.w3.org/2001/XMLSchema}string" default="$" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "NumberFormat", propOrder = {
    "formatExpression"
})
public class NumberFormat
    implements Serializable
{

    private final static long serialVersionUID = 1L;
    protected String formatExpression;
    @XmlAttribute
    protected FormatCategoryEnum formatCategory;
    @XmlAttribute
    protected BigInteger formatScale;
    @XmlAttribute
    protected FormatShortcutEnum formatShortcut;
    @XmlAttribute
    protected String currencySymbol;

    /**
     * Gets the value of the formatExpression property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFormatExpression() {
        return formatExpression;
    }

    /**
     * Sets the value of the formatExpression property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFormatExpression(String value) {
        this.formatExpression = value;
    }

    /**
     * Gets the value of the formatCategory property.
     * 
     * @return
     *     possible object is
     *     {@link FormatCategoryEnum }
     *     
     */
    public FormatCategoryEnum getFormatCategory() {
        if (formatCategory == null) {
            return FormatCategoryEnum.DEFAULT;
        } else {
            return formatCategory;
        }
    }

    /**
     * Sets the value of the formatCategory property.
     * 
     * @param value
     *     allowed object is
     *     {@link FormatCategoryEnum }
     *     
     */
    public void setFormatCategory(FormatCategoryEnum value) {
        this.formatCategory = value;
    }

    /**
     * Gets the value of the formatScale property.
     * 
     * @return
     *     possible object is
     *     {@link java.math.BigInteger }
     *
     */
    public BigInteger getFormatScale() {
        if (formatScale == null) {
            return new BigInteger("0");
        } else {
            return formatScale;
        }
    }

    /**
     * Sets the value of the formatScale property.
     *
     * @param value
     *     allowed object is
     *     {@link java.math.BigInteger }
     *     
     */
    public void setFormatScale(BigInteger value) {
        this.formatScale = value;
    }

    /**
     * Gets the value of the formatShortcut property.
     * 
     * @return
     *     possible object is
     *     {@link FormatShortcutEnum }
     *     
     */
    public FormatShortcutEnum getFormatShortcut() {
        if (formatShortcut == null) {
            return FormatShortcutEnum.NONE;
        } else {
            return formatShortcut;
        }
    }

    /**
     * Sets the value of the formatShortcut property.
     * 
     * @param value
     *     allowed object is
     *     {@link FormatShortcutEnum }
     *     
     */
    public void setFormatShortcut(FormatShortcutEnum value) {
        this.formatShortcut = value;
    }

    /**
     * Gets the value of the currencySymbol property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCurrencySymbol() {
        if (currencySymbol == null) {
            return "$";
        } else {
            return currencySymbol;
        }
    }

    /**
     * Sets the value of the currencySymbol property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCurrencySymbol(String value) {
        this.currencySymbol = value;
    }

}
