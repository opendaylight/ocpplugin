package org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.device.mgmt.rev150811;
import org.opendaylight.yangtools.yang.binding.DataObject;
import org.opendaylight.yangtools.yang.common.QName;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.common.types.rev150811.OriRes;
import org.opendaylight.yangtools.yang.binding.Augmentable;


/**
 * <p>This class represents the following YANG schema fragment defined in module <b>sal-device-mgmt</b>
 * <pre>
 * container output {
 *     leaf result {
 *         type OriRes;
 *     }
 * }
 * </pre>
 * The schema path to identify an instance is
 * <i>sal-device-mgmt/set-time/output</i>
 *
 * <p>To create instances of this class use {@link org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.device.mgmt.rev150811.SetTimeOutputBuilder}.
 * @see org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.device.mgmt.rev150811.SetTimeOutputBuilder
 *
 */
public interface SetTimeOutput
    extends
    DataObject,
    Augmentable<org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.device.mgmt.rev150811.SetTimeOutput>
{



    public static final QName QNAME = org.opendaylight.yangtools.yang.common.QName.create("urn:opendaylight:ocp:device:mgmt",
        "2015-08-11", "output").intern();

    /**
     * @return <code>org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.common.types.rev150811.OriRes</code> <code>result</code>, or <code>null</code> if not present
     */
    OriRes getResult();

}

