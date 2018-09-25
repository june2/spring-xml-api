package com.blackboard.consulting.util;

import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import blackboard.platform.context.Context;
import blackboard.platform.context.ContextManagerFactory;
import blackboard.platform.intl.LocaleManagerFactory;
import blackboard.platform.plugin.PlugIn;
import blackboard.platform.plugin.PlugInManagerFactory;
import blackboard.util.StringUtil;

public class LocaleUtil {
	private final static Logger LOG = LoggerFactory.getLogger(LocaleUtil.class);

	public static Locale getLocale() {
		Locale locale = null;
		Context context = ContextManagerFactory.getInstance().getContext();
		// Do not return null context, get default if user not specified
		/*
		 * if ( null == context ) { LOG.warn(
		 * "Could not get context, returning null locale..." ); return locale; }
		 */
		locale = LocaleManagerFactory.getInstance().getLocale().getLocaleObject();
		if (locale != null) {
			LOG.info("locale: " + locale.toString());
			return locale;
		}

		String userLocale = null;
		if (context != null) {
			if (context.getUser() != null) {
				userLocale = context.getUser().getLocale();
			} else {
				userLocale = context.getLocale();
			}
		}

		if (StringUtil.isEmpty(userLocale)) {
			PlugIn b2 = PlugInManagerFactory.getInstance().getPlugIn("ppto", "panopto-online-attendance");
			locale = StringUtils.parseLocaleString(b2.getDefaultLocale());
		} else {
			locale = StringUtils.parseLocaleString(userLocale);
		}

		return locale;
	}
}