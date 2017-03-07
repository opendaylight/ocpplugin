package org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.device.mgmt.rev150811;
import org.opendaylight.yangtools.yang.binding.DataObject;
import org.opendaylight.yangtools.yang.common.QName;
import org.opendaylight.yang.gen.v1.urn.opendaylight.inventory.rev130819.NodeId;
import org.opendaylight.yangtools.yang.binding.Augmentable;
import org.opendaylight.yangtools.yang.binding.Notification;


/**
 * <p>This class represents the following YANG schema fragment defined in module <b>sal-device-mgmt</b>
 * <pre>
 * notification device-disconnected {
 *     leaf nodeId {
 *         type node-id;
 *     }
 * }
 * </pre>
 * The schema path to identify an instance is
 * <i>sal-device-mgmt/device-disconnected</i>
 *
 * <p>To create instances of this class use {@link org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.device.mgmt.rev150811.DeviceDisconnectedBuilder}.
 * @see org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.device.mgmt.rev150811.DeviceDisconnectedBuilder
 *
 */
public interface DeviceDisconnected
    extends
    DataObject,
    Augmentable<org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.device.mgmt.rev150811.DeviceDisconnected>,
    Notification
{



    public static final QName QNAME = org.opendaylight.yangtools.yang.common.QName.create("urn:opendaylight:ocp:device:mgmt",
        "2015-08-11", "device-disconnected").intern();

    /**
     * @return <code>org.opendaylight.yang.gen.v1.urn.opendaylight.inventory.rev130819.NodeId</code> <code>nodeId</code>, or <code>null</code> if not present
     */
    NodeId getNodeId();

}

