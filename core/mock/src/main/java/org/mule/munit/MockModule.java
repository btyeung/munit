/**
 * This file was automatically generated by the Mule Development Kit
 */
package org.mule.munit;

import org.mule.DefaultMuleMessage;
import org.mule.api.*;
import org.mule.api.annotations.Module;
import org.mule.api.annotations.Processor;
import org.mule.api.annotations.param.Optional;
import org.mule.api.context.MuleContextAware;
import org.mule.api.el.ExpressionLanguageContext;
import org.mule.api.el.ExpressionLanguageExtension;
import org.mule.api.processor.MessageProcessor;
import org.mule.api.transport.PropertyScope;
import org.mule.munit.common.mocking.*;
import org.mule.munit.functions.*;

import java.util.*;

/**
 * <p>
 *     Munit module for mocking message processors.
 *
 * </p>
 *
 * @author Federico, Fernando
 * @version since 3.3.2
 */
@Module(name="mock", schemaVersion="3.3")
public class MockModule implements MuleContextAware, ExpressionLanguageExtension
{
    private MuleContext muleContext;


    /**
     * <p>Define what the mock must return on a message processor call.</p>
     * <p/>
     * <p>If the message processor doesn't return any value then there is no need to define an expect.</p>
     * <p/>
     * <p>You can define the message processor parameters in the same order they appear in the API documentation. In
     * order to define the behaviour on that particular case.</p>
     * <p/>
     * {@sample.xml ../../../doc/mock-connector.xml.sample mock:expect}
     *
     * @param messageProcessor Message processor name.
     * @param thenReturn         Expected return value.
     * @param withAttributes       Message processor parameters.
     */
    @Processor
    public void when(String messageProcessor,
                     @Optional List<Attribute> withAttributes,
                     @Optional MunitMuleMessage thenReturn) {
        MunitMuleMessage munitMuleMessage = thenReturn == null ? new MunitMuleMessage() : thenReturn;

        mocker().when(getName(messageProcessor))
                    .ofNamespace(getNamespace(messageProcessor))
                    .withAttributes(createAttributes(withAttributes))
                    .thenReturn(createMuleMessageFrom(munitMuleMessage.getPayload(),
                            munitMuleMessage.getInboundProperties(),
                            munitMuleMessage.getOutboundProperties(),
                            munitMuleMessage.getSessionProperties(),
                            munitMuleMessage.getInvocationProperties()));

    }

    /**
     * <p>Define what the mock must return on a message processor call.</p>
     * <p/>
     * <p>If the message processor doesn't return any value then there is no need to define an expect.</p>
     * <p/>
     * <p>You can define the message processor parameters in the same order they appear in the API documentation. In
     * order to define the behaviour on that particular case.</p>
     * <p/>
     * {@sample.xml ../../../doc/mock-connector.xml.sample mock:spy}
     *
     * @param messageProcessor Message processor name.
     * @param assertionsBeforeCall Expected return value.
     * @param assertionsAfterCall  Message processor parameters.
     */
    @Processor
    public void spy(String messageProcessor,
                    @Optional List<NestedProcessor> assertionsBeforeCall,
                    @Optional List<NestedProcessor> assertionsAfterCall) {

            new MunitSpy(muleContext).spyMessageProcessor(getName(messageProcessor))
                    .ofNamespace(getNamespace(messageProcessor))
                    .running(createSpyAssertion(createMessageProcessorsFrom(assertionsBeforeCall)),
                            createSpyAssertion(createMessageProcessorsFrom(assertionsAfterCall)));
    }



    /**
     * <p>Expect to throw an exception when message processor is called. </p>
     * <p/>
     * {@sample.xml ../../../doc/mock-connector.xml.sample mock:expectFail}
     *
     * @param exception Java Exception full qualified name.
     * @param whenCalling   Message processor name.
     * @param withAttributes list of expected attributes
     */
    @Processor
    public void throwAn(Throwable exception, String whenCalling,
                        @Optional List<Attribute> withAttributes) {
            mocker().when(getName(whenCalling))
                    .ofNamespace(getNamespace(whenCalling))
                    .withAttributes(createAttributes(withAttributes))
                    .thenThrow(exception);
    }


    /**
     * Check that the message processor was called with some specified parameters
     * <p/>
     * {@sample.xml ../../../doc/mock-connector.xml.sample mock:verifyCall}
     *
     * @param messageProcessor Message processor Id
     * @param attributes       Message processor parameters.
     * @param times            Number of times the message processor has to be called
     * @param atLeast          Number of time the message processor has to be called at least.
     * @param atMost           Number of times the message processor has to be called at most.
     */
    @Processor
    public void verifyCall(String messageProcessor, @Optional List<Attribute> attributes,
                           @Optional Integer times,
                           @Optional Integer atLeast, @Optional Integer atMost) {

        MunitVerifier mockVerifier =
        new MunitVerifier(muleContext).verifyCallOfMessageProcessor(getName(messageProcessor))
                .ofNamespace(getNamespace(messageProcessor))
                .withAttributes(createAttributes(attributes));

        if (times != null) {
            mockVerifier.times(times);

        } else if (atLeast != null) {
            mockVerifier.atLeast(atLeast);
        } else if (atMost != null) {
            mockVerifier.atMost(atMost);
        } else {
            mockVerifier.atLeastOnce();
        }

    }

    /**
     * Reset mock behaviour
     *
     * {@sample.xml ../../../doc/mock-connector.xml.sample mock:outboundEndpoint}
     *
     * @param address the address
     * @param returnPayload the Return Payload
     * @param returnInboundProperties inbound properties
     * @param returnInvocationProperties invocation properties
     * @param returnSessionProperties invocation session properties
     * @param returnOutboundProperties oubound properties
     * @param assertions assertions
     */
    @Processor
    public void outboundEndpoint(String address,
                                 @Optional Object returnPayload,
                                 @Optional Map<String, Object> returnInvocationProperties,
                                 @Optional Map<String, Object> returnInboundProperties,
                                 @Optional Map<String, Object> returnSessionProperties,
                                 @Optional Map<String, Object> returnOutboundProperties,
                                 @Optional List<NestedProcessor> assertions) {

        new EndpointMocker(muleContext).expectEndpointWithAddress(address)
                .withIncomingMessageSatisfying(createSpyAssertion(createMessageProcessorsFrom(assertions)))
                .toReturn(createMuleMessageFrom(returnPayload, 
                        returnInboundProperties, 
                        returnOutboundProperties,
                        returnSessionProperties,
                        returnInvocationProperties));
    }

    @Override
    public void setMuleContext(MuleContext muleContext) {
        this.muleContext = muleContext;
    }

    @Override
    public void configureContext(ExpressionLanguageContext context) {
        context.declareFunction("eq", new EqMatcherFunction());
        context.declareFunction("anyBoolean", new AnyMatcherFunction(Boolean.class));
        context.declareFunction("anyByte", new AnyMatcherFunction(Byte.class));
        context.declareFunction("anyInt", new AnyMatcherFunction(Integer.class));
        context.declareFunction("anyDouble", new AnyMatcherFunction(Double.class));
        context.declareFunction("anyFloat", new AnyMatcherFunction(Float.class));
        context.declareFunction("anyShort", new AnyMatcherFunction(Short.class));
        context.declareFunction("anyObject", new AnyMatcherFunction(Object.class));
        context.declareFunction("anyString", new AnyMatcherFunction(String.class));
        context.declareFunction("anyList", new AnyMatcherFunction(List.class));
        context.declareFunction("anySet", new AnyMatcherFunction(Set.class));
        context.declareFunction("anyMap", new AnyMatcherFunction(Map.class));
        context.declareFunction("anyCollection", new AnyMatcherFunction(Collection.class));
        context.declareFunction("isNull", new NullMatcherFunction());
        context.declareFunction("isNotNull", new NotNullMatcherFunction());
        context.declareFunction("any", new AnyClassMatcherFunction());
        context.declareFunction("resultOfScript", new FlowResultFunction(muleContext));
		context.declareFunction("getResource", new GetResourceFunction());
    }

   
    private MuleMessage createMuleMessageFrom(Object payload, 
                                              Map<String,Object> inboundProperties,
                                              Map<String,Object> outboundProperties,
                                              Map<String,Object> sessionProperties,
                                              Map<String,Object> invocationProperties
                                              ) {
        Object definedPayload = payload;
        if ( payload == null ){
            definedPayload = NotDefinedPayload.getInstance();
        }
        DefaultMuleMessage message = new DefaultMuleMessage(definedPayload, muleContext);

        if ( inboundProperties != null ){
            for (String property : inboundProperties.keySet() ){
                message.setInboundProperty(property, inboundProperties.get(property));
            }
        }

        if ( outboundProperties != null ){
            for (String property : outboundProperties.keySet() ){
                message.setOutboundProperty(property, outboundProperties.get(property));
            }
        }

        if ( invocationProperties != null ){
            for (String property : invocationProperties.keySet() ){
                message.setInvocationProperty(property, invocationProperties.get(property));
            }
        }

        // TODO: how we can set the session properties?
//        if ( sessionProperties != null ){
//            for (String property : sessionProperties.keySet() ){
//                message.setProperty(property, sessionProperties.get(property), PropertyScope.SESSION);
//            }
//        }
        return message;
    }


    private List<MessageProcessor> createMessageProcessorsFrom(List<NestedProcessor> assertions) {
        if (assertions == null) {
            return null;
        }


        List<MessageProcessor> mps = new ArrayList<MessageProcessor>();
        for (NestedProcessor nestedProcessor : assertions) {
            mps.add(new NestedMessageProcessor(nestedProcessor));
        }

        return mps;
    }

    private Map<String, Object> createAttributes(List<Attribute> attributes) {
        Map<String, Object> attrs = new HashMap<String, Object>();
        if ( attributes == null ){
            return attrs;
        }

        for ( Attribute attr : attributes ){
            attrs.put(attr.getName(), attr.getWhereValue());
        }

        return attrs;
    }

    private String getNamespace(String when) {
        String[] split = when.split(":");
        if (split.length > 1) {
            return split[0];
        }

        return "mule";
    }

    private String getName(String when) {
        String[] split = when.split(":");
        if (split.length > 1) {
            return split[1];
        }

        return split[0];
    }

    private List<SpyProcess> createSpyAssertion(final List<MessageProcessor> messageProcessorsFrom) {
        List<SpyProcess> mps = new ArrayList<SpyProcess>();
        mps.add(createSpy(messageProcessorsFrom));
        return mps;
    }

    private SpyProcess createSpy(final List<MessageProcessor> messageProcessorsFrom) {
        return new SpyProcess(){

            @Override
            public void spy(MuleEvent event) throws MuleException {
                for ( MessageProcessor mp : messageProcessorsFrom ){
                    mp.process(event);
                }
            }
        };
    }


    protected MessageProcessorMocker mocker() {
        return new MessageProcessorMocker(muleContext);
    }

}
