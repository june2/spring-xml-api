/**
 * IUsageReporting.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.panopto.services;

import java.rmi.RemoteException;

public interface IUsageReporting extends java.rmi.Remote {
	public ListUsageReportingResponse getSessionDetailedUsage(com.panopto.services.AuthenticationInfo auth, java.lang.String sessionId, Pagination pagination, java.lang.String beginRange, java.lang.String endRange) throws java.rmi.RemoteException;	
	public ListUsageReportingResponse getSessionUserDetailedUsage(com.panopto.services.AuthenticationInfo auth, java.lang.String sessionId, java.lang.String userId, Pagination pagination) throws java.rmi.RemoteException;
}
