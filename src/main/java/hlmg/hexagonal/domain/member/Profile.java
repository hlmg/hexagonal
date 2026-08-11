package hlmg.hexagonal.domain.member;

import java.util.regex.Pattern;

public record Profile(String address) {

    private static final Pattern PROFILE_ADDRESS_PATTERN = Pattern.compile("[a-z0-9]{1,15}");

    public Profile {
        if (!PROFILE_ADDRESS_PATTERN.matcher(address).matches()) {
            throw new IllegalArgumentException("invalid profile address: " + address);
        }
    }

    public String url() {
        return "@" + address;
    }

}
