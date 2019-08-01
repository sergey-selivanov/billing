package sssii.billing.ui.view;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

import sssii.billing.common.entity.rs.User;
import sssii.billing.ui.BillingUIServlet;
import sssii.billing.ui.VaadinSessionHelper;

public class HomeView extends VerticalLayout implements View
{
    private static final long serialVersionUID = 1L;

    public HomeView() {
        //setSizeUndefined();


        Label lbl = new Label("Billing " + BillingUIServlet.getOptions().getProperty("title.branding"));
        addComponent(lbl);
        lbl.addStyleName(ValoTheme.LABEL_H2);

        User u = VaadinSessionHelper.getUser();

        lbl = new Label("Welcome " + u.getFirstName() + "!");
        addComponent(lbl);
    }

    @Override
    public void enter(ViewChangeEvent event) {
        // TODO Auto-generated method stub

    }
}
