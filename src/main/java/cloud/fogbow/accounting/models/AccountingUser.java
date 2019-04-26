package cloud.fogbow.accounting.models;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "accounting_user")
public class AccountingUser implements Serializable {
    private static final long serialVersionUID = 1L;

    private String userName;

    @EmbeddedId
    private UserIdentity userIdentity;

    public String getUserName() {
        return userName;
    }

    public AccountingUser(String userName, UserIdentity userIdentity) {
        this.userName = userName;
        this.userIdentity = userIdentity;
    }

    public AccountingUser(UserIdentity userIdentity) {
        this.userIdentity = userIdentity;
    }

    public AccountingUser() {}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AccountingUser that = (AccountingUser) o;
        return userIdentity.equals(that.userIdentity);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userIdentity);
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public UserIdentity getUserIdentity() {
        return userIdentity;
    }

    public void setUserIdentity(UserIdentity userIdentity) {
        this.userIdentity = userIdentity;
    }
}
