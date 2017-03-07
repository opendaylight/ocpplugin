package org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.device.mgmt.rev150811;
import org.opendaylight.yangtools.yang.binding.NotificationListener;


/**
 * Interface for receiving the following YANG notifications defined in module <b>sal-device-mgmt</b>
 * <pre>
 * notification device-connected {
 *     leaf nodeId {
 *         type node-id;
 *     }
 *     leaf reIpAddr {
 *         type ipv4-address;
 *     }
 * }
 * notification device-disconnected {
 *     leaf nodeId {
 *         type node-id;
 *     }
 * }
 * </pre>
 *
 */
public interface SalDeviceMgmtListener
    extends
    NotificationListener
{




    void onDeviceConnected(DeviceConnected notification);
    
    void onDeviceDisconnected(DeviceDisconnected notification);

}

