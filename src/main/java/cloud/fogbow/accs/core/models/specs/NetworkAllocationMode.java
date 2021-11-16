package cloud.fogbow.accs.core.models.specs;

import com.fasterxml.jackson.annotation.JsonValue;
import com.google.gson.annotations.SerializedName;

public enum NetworkAllocationMode {
    @SerializedName("dynamic")
	DYNAMIC("dynamic"),
	@SerializedName("static")
    STATIC("static");

    private String value;

    NetworkAllocationMode(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }
}
