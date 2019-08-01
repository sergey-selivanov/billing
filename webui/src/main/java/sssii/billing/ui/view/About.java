package sssii.billing.ui.view;

import java.util.Map;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

import sssii.billing.common.PermissionsException;
import sssii.billing.common.entity.rs.MapWrapper;
import sssii.billing.ui.BillingUIServlet;

public class About extends VerticalLayout implements View
{
    private static final long serialVersionUID = 1L;

    private Logger log = LoggerFactory.getLogger(About.class);

    public About() {
        //setSizeUndefined();

        Label lbl = new Label("Web UI");
        lbl.addStyleName(ValoTheme.LABEL_H3);
        addComponent(lbl); //lbl.addStyleName(ValoTheme.LABEL_H2);

        Properties ver = BillingUIServlet.getVersion();

        FormLayout form = new FormLayout();
        form.setSpacing(false);
        form.setMargin(false);

        lbl = new Label(ver.getProperty("version") + " " + ver.getProperty("environment.name"));
        lbl.setCaption("Version");
        form.addComponent(lbl);


        lbl = new Label(ver.getProperty("build.date"));
        lbl.setCaption("Build date");
        form.addComponent(lbl);

        lbl = new Label(ver.getProperty("git.commit") + " " + ver.getProperty("git.date") + " " + ver.getProperty("git.branch"));
        lbl.setCaption("Git revision");
        form.addComponent(lbl);

        addComponent(form);


        lbl = new Label("Server");
        lbl.addStyleName(ValoTheme.LABEL_H3);
        addComponent(lbl);


        MapWrapper mw;
        try {
            mw = BillingUIServlet.getRestEntity("server/version", MapWrapper.class);

            Map<String, String> map = mw.getMap();

            form = new FormLayout();
            form.setSpacing(false);
            form.setMargin(false);

            lbl = new Label(map.get("version") + " " + map.get("environment.name"));
            lbl.setCaption("Version");
            form.addComponent(lbl);

            lbl = new Label(map.get("build.date"));
            lbl.setCaption("Build date");
            form.addComponent(lbl);

            lbl = new Label(map.get("git.commit") + " " + map.get("git.date") + " " + map.get("git.branch"));
            lbl.setCaption("Git revision");
            form.addComponent(lbl);

            addComponent(form);
        } catch (PermissionsException e) {
            log.error("insufficient permissions: " + e.getMessage());
        }

    }

    @Override
    public void enter(ViewChangeEvent event) {
        // TODO Auto-generated method stub

    }

}
