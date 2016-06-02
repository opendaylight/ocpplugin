/*
 * Copyright (c) 2013 Cisco Systems, Inc. and others.  All rights reserved.
 * Copyright (c) 2015 Foxconn Corporation
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.ocpplugin.ocp.it;


import static org.ops4j.pax.exam.CoreOptions.mavenBundle;

import org.opendaylight.controller.test.sal.binding.it.TestHelper;
import org.ops4j.pax.exam.CoreOptions;
import org.ops4j.pax.exam.Option;
import org.ops4j.pax.exam.options.DefaultCompositeOption;

/**
 * The main wiring is assembled in #ocpPluginBundles()
 * @author mirehak
 * @author Richard Chien 
 */
public abstract class OcpPaxOptionsAssistant {

    /** system property required to enable osgi console and remote debugging, only presence matters */
    private static final String INSPECT_OSGI = "inspectOsgi";
    /** default remote debug port */
    public static final String DEBUG_PORT = "6000";
    /** base controller package */
    public static final String CONTROLLER = "org.opendaylight.controller";
    /** base controller md-sal package */
    public static final String CONTROLLER_MD = "org.opendaylight.controller.md";
    /** OCPLibrary package */
    public static final String OCPLIBRARY = "org.opendaylight.ocpplugin.ocpjava";
    /** OCPPlugin package */
    public static final String OCPPLUGIN = "org.opendaylight.ocpplugin";
    /** OCPPlugin applications package */
    public static final String OCPPLUGIN_APPS = "org.opendaylight.ocpplugin.applications";
    /** OCPPlugin model package */
    public static final String OCPPLUGIN_MODEL = "org.opendaylight.ocpplugin.model";
    /** controller.model package */
    public static final String CONTROLLER_MODEL = "org.opendaylight.controller.model";

    public static final String YANGTOOLS = "org.opendaylight.yangtools";


    /**
     * Works only if property -DinspectOsgi is used
     * @return equinox console setup (in order to inspect running IT through osgi console (telnet))
     * and remote debugging on port #DEBUG_PORT
     */
    public static Option osgiConsoleBundles() {
        DefaultCompositeOption option = new DefaultCompositeOption();
        if (System.getProperty(INSPECT_OSGI) != null) {
            option
            .add(CoreOptions.vmOption("-Xrunjdwp:transport=dt_socket,server=y,suspend=n,address="+DEBUG_PORT))
            .add(CoreOptions.mavenBundle("equinoxSDK381", "org.eclipse.equinox.console").versionAsInProject())
            .add(CoreOptions.mavenBundle("equinoxSDK381", "org.apache.felix.gogo.shell").versionAsInProject())
            .add(CoreOptions.mavenBundle("equinoxSDK381", "org.apache.felix.gogo.runtime").versionAsInProject())
            .add(CoreOptions.mavenBundle("equinoxSDK381", "org.apache.felix.gogo.command").versionAsInProject());
        }

        return option;
    }

    /**
     * @return OCPLibrary bundles
     */
    public static Option ocpLibraryBundles() {
        return new DefaultCompositeOption(
                mavenBundle(OCPLIBRARY, "util").versionAsInProject(),
                mavenBundle(OCPLIBRARY, "ocp-protocol-impl").versionAsInProject(),
                mavenBundle(OCPLIBRARY, "ocp-protocol-api").versionAsInProject(),
                mavenBundle(OCPLIBRARY, "ocp-protocol-spi").versionAsInProject(),
                mavenBundle(OCPLIBRARY, "simple-client").versionAsInProject().start());
    }

    /**
     * Here we construct whole wiring
     * @return OCPLibrary + OCPPlugin bundles
     */
    public static Option ocpPluginBundles() {
        return new DefaultCompositeOption(
                baseSalBundles(),
                mdSalApiBundles(),
                mdSalImplBundles(),
                mdSalBaseModelBundles(),
                ocpLibraryBundles(),
                mavenBundle(CONTROLLER_MODEL, "model-inventory").versionAsInProject(),
                mavenBundle(OCPPLUGIN, "ocpplugin-api").versionAsInProject(),
                mavenBundle(OCPPLUGIN, "ocpplugin-impl").versionAsInProject(),
                mavenBundle(OCPPLUGIN_MODEL, "model-ocp-service").versionAsInProject(),
                mavenBundle(OCPPLUGIN_APPS, "ocp-service", "0.1.0-SNAPSHOT"),
                mavenBundle("openexi", "nagasena").versionAsInProject()
                );
    }

    /**
     * @return logging bundles
     */
    public static Option loggingBudles() {
        return new DefaultCompositeOption(
                mavenBundle("org.slf4j", "slf4j-api").versionAsInProject(),
                mavenBundle("org.slf4j", "log4j-over-slf4j").versionAsInProject(),
                mavenBundle("ch.qos.logback", "logback-core").versionAsInProject(),
                mavenBundle("ch.qos.logback", "logback-classic").versionAsInProject());
    }

    /**
     * @return sal + dependencymanager
     */
    public static Option baseSalBundles() {
        return new DefaultCompositeOption(
//                mavenBundle("org.apache.felix", "org.apache.felix.dependencymanager").versionAsInProject(),
//                mavenBundle(CONTROLLER, "sal").versionAsInProject(),
                mavenBundle("io.netty", "netty-common").versionAsInProject(), //
                mavenBundle("io.netty", "netty-buffer").versionAsInProject(), //
                mavenBundle("io.netty", "netty-handler").versionAsInProject(), //
                mavenBundle("io.netty", "netty-codec").versionAsInProject(), //
                mavenBundle("io.netty", "netty-transport").versionAsInProject(), //
                mavenBundle("org.antlr", "antlr4-runtime").versionAsInProject());
    }

    /**
     * @return sal + dependencymanager
     */
    public static Option mdSalApiBundles() {
        return new DefaultCompositeOption(
                TestHelper.junitAndMockitoBundles(),
                TestHelper.mdSalCoreBundles(),
                TestHelper.configMinumumBundles(),
                mavenBundle("org.antlr", "antlr4-runtime").versionAsInProject());
    }

    private static Option mdSalImplBundles() {
        return new DefaultCompositeOption(
                TestHelper.bindingAwareSalBundles()
        );
    }

    private static Option mdSalBaseModelBundles() {
        return new DefaultCompositeOption(
                TestHelper.baseModelBundles(),
                mavenBundle(CONTROLLER_MODEL, "model-inventory").versionAsInProject()
        );
    }

}

