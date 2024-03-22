// strongly inspired by https://www.sothawo.com/2016/09/how-to-implement-a-javafx-ui-where-the-language-can-be-changed-dynamically/
package client.services;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class I18N {
    private static final ObjectProperty<Locale> locale;

    static {
        locale = new SimpleObjectProperty<>(getDefaultLocale());
        locale.addListener((observable, oldVal, newVal) -> Locale.setDefault(newVal));
    }

    /**
     * get the supported Locales.
     *
     * @return List of all locales supported by the application.
     */
    public static List<Locale> getSupportedLocales() {
        return new ArrayList<>(Arrays.asList(Locale.ENGLISH, Locale.GERMAN));
    }

    /**
     * get the default locale. This is the systems default if contained in the supported locales, english otherwise.
     *
     * @return the systems default language if supported and else English.
     */
    public static Locale getDefaultLocale() {
        Locale sysDefault = Locale.getDefault();
        return getSupportedLocales().contains(sysDefault) ? sysDefault : Locale.ENGLISH;
    }
}
