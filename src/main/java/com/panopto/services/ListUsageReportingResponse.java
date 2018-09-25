/**
 * ListReportingsResponse.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.panopto.services;

public class ListUsageReportingResponse  implements java.io.Serializable {
    private com.panopto.services.DetailedUsageResponseItem[] pagedResponses;

    private java.lang.Integer totalNumberResponses;

    public ListUsageReportingResponse() {
    }

    public ListUsageReportingResponse(
           com.panopto.services.DetailedUsageResponseItem[] pagedResponses,
           java.lang.Integer totalNumberResponses) {
           this.pagedResponses = pagedResponses;
           this.totalNumberResponses = totalNumberResponses;
    }


    /**
     * Gets the PagedResponses value for this ListReportingsResponse.
     * 
     * @return PagedResponses
     */
    public com.panopto.services.DetailedUsageResponseItem[] getPagedResponses() {
        return pagedResponses;
    }


    /**
     * Sets the PagedResponses value for this ListReportingsResponse.
     * 
     * @param PagedResponses
     */
    public void setPagedResponses(com.panopto.services.DetailedUsageResponseItem[] PagedResponses) {
        this.pagedResponses = PagedResponses;
    }


    /**
     * Gets the totalNumberResponses value for this ListReportingsResponse.
     * 
     * @return totalNumberResponses
     */
    public java.lang.Integer getTotalNumberResponses() {
        return totalNumberResponses;
    }


    /**
     * Sets the totalNumberResponses value for this ListReportingsResponse.
     * 
     * @param totalNumberResponses
     */
    public void settotalNumberResponses(java.lang.Integer totalNumberResponses) {
        this.totalNumberResponses = totalNumberResponses;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof ListUsageReportingResponse)) return false;
        ListUsageReportingResponse other = (ListUsageReportingResponse) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.pagedResponses==null && other.getPagedResponses()==null) || 
             (this.pagedResponses!=null &&
              java.util.Arrays.equals(this.pagedResponses, other.getPagedResponses()))) &&
            ((this.totalNumberResponses==null && other.getTotalNumberResponses()==null) || 
             (this.totalNumberResponses!=null &&
              this.totalNumberResponses.equals(other.getTotalNumberResponses())));
        __equalsCalc = null;
        return _equals;
    }

    private boolean __hashCodeCalc = false;
    public synchronized int hashCode() {
        if (__hashCodeCalc) {
            return 0;
        }
        __hashCodeCalc = true;
        int _hashCode = 1;
        if (getPagedResponses() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getPagedResponses());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getPagedResponses(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getTotalNumberResponses() != null) {
            _hashCode += getTotalNumberResponses().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(ListUsageReportingResponse.class, true);

    static {    	
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/Panopto.Server.Services.PublicAPI.V40", "ListUsageReportingResponse"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("pagedResponses");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/Panopto.Server.Services.PublicAPI.V40", "PagedResponses"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/Panopto.Server.Services.PublicAPI.V40", "DetailedUsageResponseItem"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        elemField.setItemQName(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/Panopto.Server.Services.PublicAPI.V40", "DetailedUsageResponseItem"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("totalNumberResponses");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/Panopto.Server.Services.PublicAPI.V40", "TotalNumberResponses"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
    }

    /**
     * Return type metadata object
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }

    /**
     * Get Custom Serializer
     */
    public static org.apache.axis.encoding.Serializer getSerializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanSerializer(
            _javaType, _xmlType, typeDesc);
    }

    /**
     * Get Custom Deserializer
     */
    public static org.apache.axis.encoding.Deserializer getDeserializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanDeserializer(
            _javaType, _xmlType, typeDesc);
    }

}
