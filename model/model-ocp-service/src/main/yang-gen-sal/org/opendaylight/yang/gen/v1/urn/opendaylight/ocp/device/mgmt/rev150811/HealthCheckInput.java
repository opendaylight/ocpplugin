package org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.device.mgmt.rev150811;
import org.opendaylight.yang.gen.v1.urn.opendaylight.inventory.rev130819.NodeContextRef;
import org.opendaylight.yangtools.yang.binding.DataObject;
import org.opendaylight.yangtools.yang.common.QName;
import org.opendaylight.yangtools.yang.binding.Augmentable;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.xsd.types.rev150811.XsdUnsignedShort;


/**
 * <p>This class represents the following YANG schema fragment defined in module <b>sal-device-mgmt</b>
 * <pre>
 * container input {
 *     leaf node {
 *         type node-ref;
 *     }
 *     leaf tcpLinkMonTimeout {
 *         type xsd-unsignedShort;
 *     }
 *     uses node-context-ref;
 * }
 * </pre>
 * The schema path to identify an instance is
 * <i>sal-device-mgmt/health-check/input</i>
 *
 * <p>To create instances of this class use {@link org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.device.mgmt.rev150811.HealthCheckInputBuilder}.
 * @see org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.device.mgmt.rev150811.HealthCheckInputBuilder
 *
 */
public interface HealthCheckInput
    extends
    NodeContextRef,
    DataObject,
    Augmentable<org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.device.mgmt.rev150811.HealthCheckInput>
{



    public static final QName QNAME = org.opendaylight.yangtools.yang.common.QName.create("urn:opendaylight:ocp:device:mgmt",
        "2015-08-11", "input").intern();

    /**
     * @return <code>org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.xsd.types.rev150811.XsdUnsignedShort</code> <code>tcpLinkMonTimeout</code>, or <code>null</code> if not present
     */
    XsdUnsignedShort getTcpLinkMonTimeout();

}

