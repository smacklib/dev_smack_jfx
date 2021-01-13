/*
 * $Id$
 * Released under Gnu Public License
 * Copyright © 2019 Michael G. Binz
 */
module framework.smack_jfx {
    requires framework.smack;
    requires transitive java.desktop;
    requires java.logging;
    requires java.prefs;
    requires transitive javafx.base;
    requires javafx.controls;
    requires javafx.swing;
    requires transitive javafx.graphics;

    uses org.smack.util.resource.ResourceConverterExtension;

    // All converters have to be registered here.  The ServiceLoader
    // uses this list. The ServiceLoader configuration file is not
    // needed with the module system.
    provides org.smack.util.resource.ResourceConverterExtension with
        org.smack.fx.converters.Registrar;

    exports org.jdesktop.beans;
    exports org.smack.fx;
    exports org.smack.fx.converters;

    // Needed for testing.
    opens org.smack.fx;
}
