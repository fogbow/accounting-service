package accouting.models;

public enum AccountingOperationType {
    OWN_BILLING("OWN BILLING"),
    OTHERS_BILLING("OTHERS BILLING");

    private final String repr;

    AccountingOperationType(String repr) {
        this.repr = repr;
    }

}
