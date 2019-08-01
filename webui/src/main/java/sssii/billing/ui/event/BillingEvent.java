package sssii.billing.ui.event;

import sssii.billing.ui.view.BillingViewType;

public abstract class BillingEvent {

    public static final class PostViewChangeEvent {
        private final BillingViewType view;

        public PostViewChangeEvent(final BillingViewType view) {
            this.view = view;
        }

        public BillingViewType getView() {
            return view;
        }
    }

    public static class BrowserResizeEvent {
    }

    public static class CloseOpenWindowsEvent {
    }

    public static final class UserLoginRequestedEvent {
        private final String userName, password;
        private final Boolean rememberMe;

        public UserLoginRequestedEvent(final String userName,
                final String password, final Boolean rememberMe) {
            this.userName = userName;
            this.password = password;
            this.rememberMe = rememberMe;
        }

        public String getUserName() {
            return userName;
        }

        public String getPassword() {
            return password;
        }

        public Boolean getRememberMe() {
            return rememberMe;
        }
    }

    public static class UserLoggedOutEvent {
    }

}
