package com.studentvault.common;

import java.util.Locale;

public final class Emails {

    private Emails() {}   // utility class: no instances

    public static String normalize(String email) {
        return email == null ? null : email.trim().toLowerCase(Locale.ROOT);
    }
}