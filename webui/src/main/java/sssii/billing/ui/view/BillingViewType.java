package sssii.billing.ui.view;

import com.vaadin.navigator.View;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.Resource;

public enum BillingViewType {

    // https://vaadin.com/icons
    // https://vaadin.com/elements/vaadin-icons/html-examples/icons-basic-demos
    HOME("HOME", "Home", HomeView.class, VaadinIcons.HOME, false),
    PROJECT_CHOOSER("PROJECT_CHOOSER", "New Invoice", ProjectChooserView.class, VaadinIcons.DOLLAR, true),    // FILE_ADD
    INVOICE_ITEMS_CHOOSER("INVOICE_ITEMS_CHOOSER", "", InvoiceItemsChooserView.class, VaadinIcons.DOLLAR, true),
    INVOICE_DETAILS_EDIT("INVOICE_DETAILS_EDIT", "", InvoiceDetailsEditView.class, VaadinIcons.DOLLAR, true),
    INVOICE_COMPLETE("INVOICE_COMPLETE", "", InvoiceCompleteView.class, VaadinIcons.DOLLAR, true),
    INVOICES("INVOICES", "Invoices", Invoices.class, VaadinIcons.BOOK_DOLLAR, true), // LINES_LIST COIN_PILES invoice book-dollar
    PROJECTS("PROJECTS", "Projects", Projects.class, VaadinIcons.FOLDER_OPEN, true),    // folder-open records cubes book briefcase
    CUSTOMERS("CUSTOMERS", "Customers", Customers.class, VaadinIcons.USERS, true),    // user-card group
    SETTINGS("SETTINGS", "Settings", SettingsView.class, VaadinIcons.COG, true), // cog cog-o controller
    ABOUT("ABOUT", "About", About.class, VaadinIcons.INFO_CIRCLE, false);


    private final String viewName;
    private final Class<? extends View> viewClass;
    private final Resource icon;
    private final boolean stateful;
    private final String menuLabel;

    private BillingViewType(final String viewName, final String menuLabel,
            final Class<? extends View> viewClass, final Resource icon,
            final boolean stateful) {
        this.viewName = viewName;
        this.menuLabel = menuLabel;
        this.viewClass = viewClass;
        this.icon = icon;
        this.stateful = stateful;
    }

    public boolean isStateful() {
        return stateful;
    }

    public String getViewName() {
        return viewName;
    }

    public Class<? extends View> getViewClass() {
        return viewClass;
    }

    public Resource getIcon() {
        return icon;
    }

    public static BillingViewType getByViewName(final String viewName) {
        BillingViewType result = null;
        for (BillingViewType viewType : values()) {
            if (viewType.getViewName().equals(viewName)) {
                result = viewType;
                break;
            }
        }
        return result;
    }

    public String getMenuLabel() {
        return menuLabel;
    }

}
