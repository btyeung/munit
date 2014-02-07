/*
 *
 * (c) 2003-2012 MuleSoft, Inc. This software is protected under international
 * copyright law. All use of this software is subject to MuleSoft's Master
 * Subscription Agreement (or other Terms of Service) separately entered
 * into between you and MuleSoft. If such an agreement is not in
 * place, you may not use the software.
 */
package org.mule.munit.common.mocking;

/**
 * <p>
 * This class is a marker to know that the payload must not be overridden
 * </p>
 *
 * @author Mulesoft Inc.
 * @since 3.3.2
 */
public class NotDefinedPayload
{

    /**
     * <p>
     * Creates an instance of the non defined payload
     * </p>
     *
     * @return <p>
     *         A NotDefinedPayload instance
     *         </p>
     */
    public static NotDefinedPayload getInstance()
    {
        return new NotDefinedPayload();
    }

    /**
     * <p>
     * Checks if a payload is defined or not
     * </p>
     * <p/>
     * <p>
     * A payload is not defined if it is a NullPayload or if it is non defined
     * </p>
     *
     * @param payload <p>
     *                The payload to validate
     *                </p>
     * @return <p>
     *         true/false based on the payload check
     *         </p>
     */
    public static boolean isNotDefined(Object payload)
    {
        return (payload instanceof NotDefinedPayload);
    }
}
