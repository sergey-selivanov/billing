package sssii.billing.ui.view;

import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;

import sssii.billing.ui.BillingNavigator;

@SuppressWarnings("serial")
public class MainView extends HorizontalLayout
{
    public MainView() {
        setSizeFull();
        addStyleName("mainview");

        addComponent(new BillingMenu());

        ComponentContainer content = new CssLayout();
        content.addStyleName("view-content");
        content.setSizeFull();
        addComponent(content);
        setExpandRatio(content, 1.0f);

        new BillingNavigator(content);
        //new Navigator(UI.getCurrent(), content);
    }
}
