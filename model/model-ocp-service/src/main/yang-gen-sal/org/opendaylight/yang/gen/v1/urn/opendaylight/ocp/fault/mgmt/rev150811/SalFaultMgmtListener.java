package org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.fault.mgmt.rev150811;
import org.opendaylight.yangtools.yang.binding.NotificationListener;


/**
 * Interface for receiving the following YANG notifications defined in module <b>sal-fault-mgmt</b>
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
 *
 */
public interface SalFaultMgmtListener
    extends
    NotificationListener
{




    void onFaultInd(FaultInd notification);

}

