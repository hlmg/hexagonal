package hlmg.hexagonal.domain.member;

import org.jspecify.annotations.Nullable;

import java.util.regex.Pattern;

public record Profile(@Nullable String address) {

    private static final Pattern PROFILE_ADDRESS_PATTERN = Pattern.compile("[a-z0-9]{1,15}");

    public Profile {
        if (address == null || address.isEmpty()) {
            address = null;
        } else {
            checkPattern(address);
        }
    }

    private void checkPattern(String address) {
        if (!PROFILE_ADDRESS_PATTERN.matcher(address).matches()) {
            throw new IllegalArgumentException("invalid profile address: " + address);
        }
    }

    public String url() {
        return "@" + address;
    }

}
