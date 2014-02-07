/*
 *
 * (c) 2003-2012 MuleSoft, Inc. This software is protected under international
 * copyright law. All use of this software is subject to MuleSoft's Master
 * Subscription Agreement (or other Terms of Service) separately entered
 * into between you and MuleSoft. If such an agreement is not in
 * place, you may not use the software.
 */
package org.mule.munit.config;

import org.mule.api.MuleEvent;
import org.mule.api.MuleException;
import org.mule.api.processor.MessageProcessor;
import org.mule.munit.MunitAssertion;


/**
 * <p>
 * Message processor that runs the custom assertion
 * </p>
 *
 * @author Mulesoft Inc.
 * @since 3.3.2
 */
public class RunAssertionMessageProcessor implements MessageProcessor
{

    /**
     * <p>
     * The assertion to be run.
     * </p>
     */
    private MunitAssertion assertion;


    @Override
    public MuleEvent process(MuleEvent event) throws MuleException
    {
        return assertion.execute(event);
    }

    public void setAssertion(MunitAssertion assertion)
    {
        this.assertion = assertion;
    }
}
