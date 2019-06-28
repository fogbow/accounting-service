package cloud.fogbow.accs.core.models;

import org.hibernate.annotations.NaturalId;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class UserIdentity implements Serializable {
    @NotNull
    @Column(name = "provider_id")
    @NaturalId
    private String providerId;

    @NotNull
    @Column(name = "user_key")
    @NaturalId
    private String userKey;

    public UserIdentity() {}

    public UserIdentity(String providerId, String userKey) {
        this.providerId = providerId;
        this.userKey = userKey;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserIdentity that = (UserIdentity) o;
        return Objects.equals(providerId, that.providerId) &&
                Objects.equals(userKey, that.userKey);
    }

    @Override
    public int hashCode() {
        return Objects.hash(providerId, userKey);
    }

    public String getProviderId() {
        return providerId;
    }

    public void setProviderId(String providerId) {
        this.providerId = providerId;
    }

    public String getUserKey() {
        return userKey;
    }

    public void setUserKey(String userKey) {
        this.userKey = userKey;
    }
}
