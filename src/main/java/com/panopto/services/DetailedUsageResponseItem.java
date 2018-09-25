/**
 * Group.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.panopto.services;

import com.blackboard.consulting.util.CommonUtil;

public class DetailedUsageResponseItem implements java.io.Serializable {
	private java.lang.Double secondsViewed;

	private java.lang.String sessionId;

	private java.lang.Double startPosition;

	private java.lang.String time;

	private java.lang.String userId;

	public DetailedUsageResponseItem() {
	}

	public DetailedUsageResponseItem(
			java.lang.Double secondsViewed, 
			java.lang.String sessionId, 
			java.lang.Double startPosition,
			java.lang.String time, String userId) {
		this.secondsViewed = secondsViewed;
		this.sessionId = sessionId;
		this.startPosition = startPosition;
		this.time = time;
		this.userId = userId;
	}

	public java.lang.Double getSecondsViewed() {
		return secondsViewed;
	}

	public void setSecondsViewed(java.lang.Double secondsViewed) {
		this.secondsViewed = secondsViewed;
	}

	public java.lang.String getSessionId() {
		return sessionId;
	}

	public void setSessionId(java.lang.String sessionId) {
		this.sessionId = sessionId;
	}

	public java.lang.Double getStartPosition() {
		return startPosition;
	}

	public void setStartPosition(java.lang.Double startPosition) {
		this.startPosition = startPosition;
	}

	public java.lang.String getTime() {
		return time;
	}

	public void setTime(java.lang.String time) {
		CommonUtil util = CommonUtil.getInstance();
		this.time = util.utcToLocaltime(util.convertToDate(time.replace("T", " ")));
	}

	public java.lang.String getUserId() {
		return userId;
	}

	public void setUserId(java.lang.String userId) {
		this.userId = userId;
	}

	private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof DetailedUsageResponseItem)) return false;
        DetailedUsageResponseItem other = (DetailedUsageResponseItem) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.secondsViewed==null && other.getSecondsViewed()==null) || 
             (this.secondsViewed!=null &&
              this.secondsViewed.equals(other.getSecondsViewed()))) &&
            ((this.sessionId==null && other.getSessionId()==null) || 
             (this.sessionId!=null &&
              this.sessionId.equals(other.getSessionId()))) &&      
            ((this.userId==null && other.getUserId()==null) || 
                (this.userId!=null &&
                 this.userId.equals(other.getUserId()))) &&
            ((this.time==null && other.getTime()==null) || 
                (this.time!=null &&
                 this.time.equals(other.getTime()))) &&     
            ((this.startPosition==null && other.getStartPosition()==null) || 
             (this.startPosition!=null &&
              this.startPosition.equals(other.getStartPosition())));
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
        
        if (getSecondsViewed() != null) {
        		_hashCode += getSecondsViewed().hashCode();
        }
		if (getSessionId() != null) {
			_hashCode += getSessionId().hashCode();
		}
        if (getStartPosition() != null) {
            _hashCode += getStartPosition().hashCode();
        }
        if (getTime() != null) {
            _hashCode += getTime().hashCode();
        }
        if (getUserId() != null) {
            _hashCode += getUserId().hashCode();
        }
		__hashCodeCalc = false;
		return _hashCode;
	}

	// Type metadata
	private static org.apache.axis.description.TypeDesc typeDesc =
	        new org.apache.axis.description.TypeDesc(DetailedUsageResponseItem.class, true);

	static {
		typeDesc.setXmlType(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/Panopto.Server.Services.PublicAPI.V40", "DetailedUsageResponseItem"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("secondsViewed");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/Panopto.Server.Services.PublicAPI.V40", "SecondsViewed"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "double"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("sessionId");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/Panopto.Server.Services.PublicAPI.V40", "SessionId"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("startPosition");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/Panopto.Server.Services.PublicAPI.V40", "StartPosition"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "double"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("time");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/Panopto.Server.Services.PublicAPI.V40", "Time"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("userId");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/Panopto.Server.Services.PublicAPI.V40", "UserId"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
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

	@Override
	public String toString() {
		return "DetailedUsageResponseItem [secondsViewed=" + secondsViewed + ", sessionId=" + sessionId
				+ ", startPosition=" + startPosition + ", time=" + time + ", userId=" + userId + "]";
	}
}
