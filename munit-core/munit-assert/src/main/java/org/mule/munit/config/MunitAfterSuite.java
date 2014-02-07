/*
 *
 * (c) 2003-2012 MuleSoft, Inc. This software is protected under international
 * copyright law. All use of this software is subject to MuleSoft's Master
 * Subscription Agreement (or other Terms of Service) separately entered
 * into between you and MuleSoft. If such an agreement is not in
 * place, you may not use the software.
 */
package org.mule.munit.config;

import org.mule.api.MuleContext;

/**
 * <p>
 * After Suite Flow
 * </p>
 *
 * @author Mulesoft Inc.
 * @since 3.3.2
 */
public class MunitAfterSuite extends MunitFlow
{

    public MunitAfterSuite(String name, MuleContext muleContext)
    {
        super(name, muleContext);
    }
}
