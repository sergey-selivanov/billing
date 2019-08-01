package sssii.billing.ui.event;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.SubscriberExceptionContext;
import com.google.common.eventbus.SubscriberExceptionHandler;

import sssii.billing.ui.BillingUI;

public class BillingEventBus implements SubscriberExceptionHandler {

    private final EventBus eventBus = new EventBus(this);

    public static void post(final Object event) {
        BillingUI.getBillingEventbus().eventBus.post(event);
    }

    public static void register(final Object object) {
        BillingUI.getBillingEventbus().eventBus.register(object);
    }

    public static void unregister(final Object object) {
        BillingUI.getBillingEventbus().eventBus.unregister(object);
    }

    @Override
    public final void handleException(final Throwable exception, final SubscriberExceptionContext context) {
        exception.printStackTrace();
    }
}
