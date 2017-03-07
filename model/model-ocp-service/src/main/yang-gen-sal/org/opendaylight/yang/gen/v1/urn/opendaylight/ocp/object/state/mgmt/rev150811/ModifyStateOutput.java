package org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.object.state.mgmt.rev150811;
import org.opendaylight.yangtools.yang.binding.DataObject;
import org.opendaylight.yangtools.yang.common.QName;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.common.types.rev150811.ModifyStateRes;
import org.opendaylight.yangtools.yang.binding.Augmentable;


/**
 * <p>This class represents the following YANG schema fragment defined in module <b>sal-object-state-mgmt</b>
 * <pre>
 * container output {
 *     leaf result {
 *         type ModifyStateRes;
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
 *     uses modifyStateOutput;
 * }
 * </pre>
 * The schema path to identify an instance is
 * <i>sal-object-state-mgmt/modify-state/output</i>
 *
 * <p>To create instances of this class use {@link org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.object.state.mgmt.rev150811.ModifyStateOutputBuilder}.
 * @see org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.object.state.mgmt.rev150811.ModifyStateOutputBuilder
 *
 */
public interface ModifyStateOutput
    extends
    org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.common.types.rev150811.ModifyStateOutput,
    DataObject,
    Augmentable<org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.object.state.mgmt.rev150811.ModifyStateOutput>
{



    public static final QName QNAME = org.opendaylight.yangtools.yang.common.QName.create("urn:opendaylight:ocp:object:state:mgmt",
        "2015-08-11", "output").intern();

    /**
     * @return <code>org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.common.types.rev150811.ModifyStateRes</code> <code>result</code>, or <code>null</code> if not present
     */
    ModifyStateRes getResult();

}

