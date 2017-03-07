package org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.fault.mgmt.rev150811;
import org.opendaylight.yangtools.yang.binding.DataObject;
import org.opendaylight.yang.gen.v1.urn.opendaylight.inventory.rev130819.NodeContextRef;
import org.opendaylight.yangtools.yang.common.QName;
import org.opendaylight.yangtools.yang.binding.Augmentable;
import org.opendaylight.yangtools.yang.binding.Notification;


/**
 * <p>This class represents the following YANG schema fragment defined in module <b>sal-fault-mgmt</b>
 * <pre>
 * notification fault-ind {
 *     leaf node {
 *         type node-ref;
 *     }
 *     leaf objId {
 *         type ObjId;
 *     }
 *     leaf faultId {
 *         type FaultId;
 *     }
 *     leaf state {
 *         type FaultState;
 *     }
 *     leaf severity {
 *         type FaultSeverity;
 *     }
 *     leaf timestamp {
 *         type xsd-dateTime;
 *     }
 *     leaf descr {
 *         type string;
 *     }
 *     leaf-list affectedObj {
 *         type string;
 *     }
 *     uses faultInd;
 *     uses node-context-ref;
 * }
 * </pre>
 * The schema path to identify an instance is
 * <i>sal-fault-mgmt/fault-ind</i>
 *
 * <p>To create instances of this class use {@link org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.fault.mgmt.rev150811.FaultIndBuilder}.
 * @see org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.fault.mgmt.rev150811.FaultIndBuilder
 *
 */
public interface FaultInd
    extends
    DataObject,
    Augmentable<org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.fault.mgmt.rev150811.FaultInd>,
    org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.common.types.rev150811.FaultInd,
    NodeContextRef,
    Notification
{



    public static final QName QNAME = org.opendaylight.yangtools.yang.common.QName.create("urn:opendaylight:ocp:fault:mgmt",
        "2015-08-11", "fault-ind").intern();


}

