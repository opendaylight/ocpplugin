package org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.object.state.mgmt.rev150811;
import org.opendaylight.yangtools.yang.binding.NotificationListener;


/**
 * Interface for receiving the following YANG notifications defined in module <b>sal-object-state-mgmt</b>
 * <pre>
 * notification state-change-ind {
 *     leaf node {
 *         type node-ref;
 *     }
 *     leaf objId {
 *         type ObjId;
 *     }
 *     leaf stateType {
 *         type StateType;
 *     }
 *     leaf stateValue {
 *         type StateVal;
 *     }
 *     uses stateChangeInd;
 *     uses node-context-ref;
 * }
 * </pre>
 *
 */
public interface SalObjectStateMgmtListener
    extends
    NotificationListener
{




    void onStateChangeInd(StateChangeInd notification);

}

