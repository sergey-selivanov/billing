package sssii.billing.common;

public class PermissionsException extends Exception
{
    private static final long serialVersionUID = 1L;

    public PermissionsException(String reasonPhrase) {
        super(reasonPhrase);
    }
}
