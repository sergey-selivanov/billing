package sssii.billing.ui.view;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.eventbus.Subscribe;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.ThemeResource;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.MenuBar.Command;
import com.vaadin.ui.MenuBar.MenuItem;
import com.vaadin.ui.UI;
import com.vaadin.ui.themes.ValoTheme;

import sssii.billing.ui.event.BillingEvent.PostViewChangeEvent;
import sssii.billing.ui.event.BillingEvent.UserLoggedOutEvent;
import sssii.billing.common.BillingRole;
import sssii.billing.common.entity.rs.User;
import sssii.billing.ui.BillingUIServlet;
import sssii.billing.ui.VaadinSessionHelper;
import sssii.billing.ui.event.BillingEventBus;

@SuppressWarnings("serial")
public class BillingMenu extends CustomComponent
{
    private static Logger log = LoggerFactory.getLogger(BillingMenu.class);

    public static final String ID = "dashboard-menu";
    private static final String STYLE_VISIBLE = "valo-menu-visible";

    private MenuItem settingsItem;

    public BillingMenu() {
        setPrimaryStyleName("valo-menu");
        setId(ID);
        setSizeUndefined();

        // There's only one DashboardMenu per UI so this doesn't need to be
        // unregistered from the UI-scoped DashboardEventBus.
        BillingEventBus.register(this);

        setCompositionRoot(buildContent());
    }

    private Component buildContent() {
        final CssLayout menuContent = new CssLayout();
        menuContent.addStyleName("sidebar");
        menuContent.addStyleName(ValoTheme.MENU_PART);
        menuContent.addStyleName("no-vertical-drag-hints");
        menuContent.addStyleName("no-horizontal-drag-hints");
        menuContent.setWidth(null);
        menuContent.setHeight("100%");

        menuContent.addComponent(buildTitle());
        menuContent.addComponent(buildUserMenu());
        menuContent.addComponent(buildToggleButton());
        menuContent.addComponent(buildMenuItems());

        return menuContent;
    }

    private Component buildTitle() {
        Label logo = new Label("Billing <strong>" + BillingUIServlet.getOptions().getProperty("title.branding") + "</strong>", ContentMode.HTML);
        logo.setSizeUndefined();
        HorizontalLayout logoWrapper = new HorizontalLayout(logo);
        logoWrapper.setComponentAlignment(logo, Alignment.MIDDLE_CENTER);
        logoWrapper.addStyleName("valo-menu-title");
        return logoWrapper;
    }

    private Component buildUserMenu() {
        final MenuBar settings = new MenuBar();
        settings.addStyleName("user-menu");
//        final User user = getCurrentUser();
        settingsItem = settings.addItem("", new ThemeResource("img/profile-pic-300px.jpg"), null);
        //updateUserName(null);

        //settingsItem.setText("John Doe");
        User u = VaadinSessionHelper.getUser();
        settingsItem.setText(u.getFirstName() + " " + u.getLastName());

//        settingsItem.addItem("Edit Profile", new Command() {
//            @Override
//            public void menuSelected(final MenuItem selectedItem) {
//                ProfilePreferencesWindow.open(user, false);
//            }
//        });
//        settingsItem.addItem("Preferences", new Command() {
//            @Override
//            public void menuSelected(final MenuItem selectedItem) {
//                ProfilePreferencesWindow.open(user, true);
//            }
//        });

        //settingsItem.addSeparator();

        settingsItem.addItem("Sign Out", new Command() {
            @Override
            public void menuSelected(final MenuItem selectedItem) {
                BillingEventBus.post(new UserLoggedOutEvent());
            }
        });
        return settings;
    }

    private Component buildToggleButton() {
        Button valoMenuToggleButton = new Button("Menu", new ClickListener() {
            @Override
            public void buttonClick(final ClickEvent event) {
                if (getCompositionRoot().getStyleName().contains(STYLE_VISIBLE)) {
                    getCompositionRoot().removeStyleName(STYLE_VISIBLE);
                } else {
                    getCompositionRoot().addStyleName(STYLE_VISIBLE);
                }
            }
        });
        //valoMenuToggleButton.setIcon(FontAwesome.LIST);
        valoMenuToggleButton.setIcon(VaadinIcons.LIST);
        valoMenuToggleButton.addStyleName("valo-menu-toggle");
        valoMenuToggleButton.addStyleName(ValoTheme.BUTTON_BORDERLESS);
        valoMenuToggleButton.addStyleName(ValoTheme.BUTTON_SMALL);
        return valoMenuToggleButton;
    }

    private Component buildMenuItems() {

        CssLayout menuItemsLayout = new CssLayout();
        menuItemsLayout.addStyleName("valo-menuitems");

//        for (final BillingViewType view : BillingViewType.values()) {
//            Component menuItemComponent = new ValoMenuItemButton(view);
//            menuItemsLayout.addComponent(menuItemComponent);
//        }

        User u = VaadinSessionHelper.getUser();

        Component menuItemComponent;

        menuItemComponent = new ValoMenuItemButton(BillingViewType.HOME);
        menuItemsLayout.addComponent(menuItemComponent);

        menuItemComponent = new ValoMenuItemButton(BillingViewType.PROJECT_CHOOSER);
        menuItemsLayout.addComponent(menuItemComponent);

        menuItemComponent = new ValoMenuItemButton(BillingViewType.INVOICES);
        menuItemsLayout.addComponent(menuItemComponent);

        if(u.getAuthorities().contains(BillingRole.ADMIN)) {

            menuItemComponent = new ValoMenuItemButton(BillingViewType.PROJECTS);
            menuItemsLayout.addComponent(menuItemComponent);

            menuItemComponent = new ValoMenuItemButton(BillingViewType.CUSTOMERS);
            menuItemsLayout.addComponent(menuItemComponent);

        }

        menuItemComponent = new ValoMenuItemButton(BillingViewType.SETTINGS);
        menuItemsLayout.addComponent(menuItemComponent);

        menuItemComponent = new ValoMenuItemButton(BillingViewType.ABOUT);
        menuItemsLayout.addComponent(menuItemComponent);

//        Label divider = new Label("<hr/>", ContentMode.HTML);
//        divider.setWidth(100, Unit.PERCENTAGE);
//        menuItemsLayout.addComponent(divider);


        return menuItemsLayout;

    }

//    @Subscribe
//    public void updateUserName(final ProfileUpdatedEvent event) {
//        User user = getCurrentUser();
//        settingsItem.setText(user.getFirstName() + " " + user.getLastName());
//    }

    public final class ValoMenuItemButton extends Button {

        private static final String STYLE_SELECTED = "selected";

        private final BillingViewType view;

        public ValoMenuItemButton(final BillingViewType view) {
            this.view = view;
            setPrimaryStyleName("valo-menu-item");
            setIcon(view.getIcon());
            //setCaption(view.getViewName().substring(0, 1).toUpperCase() + view.getViewName().substring(1));
            setCaption(view.getMenuLabel());
            BillingEventBus.register(this);
            addClickListener(new ClickListener() {
                @Override
                public void buttonClick(final ClickEvent event) {
                    String targetView = view.getViewName();
                    log.debug("menu target: " + targetView);
                    UI.getCurrent().getNavigator().navigateTo(view.getViewName());
                }
            });

        }

        @Subscribe
        public void postViewChange(final PostViewChangeEvent event) {
            removeStyleName(STYLE_SELECTED);
            if (event.getView() == view) {
                addStyleName(STYLE_SELECTED);
            }
        }
    }
}
