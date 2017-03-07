package org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.object.state.mgmt.rev150811;
import org.opendaylight.yang.gen.v1.urn.opendaylight.inventory.rev130819.NodeContextRef;
import org.opendaylight.yangtools.yang.binding.DataObject;
import org.opendaylight.yangtools.yang.common.QName;
import org.opendaylight.yangtools.yang.binding.Augmentable;


/**
 * <p>This class represents the following YANG schema fragment defined in module <b>sal-object-state-mgmt</b>
 * <pre>
 * container input {
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
 *     uses modifyStateInput;
 *     uses node-context-ref;
 * }
 * </pre>
 * The schema path to identify an instance is
 * <i>sal-object-state-mgmt/modify-state/input</i>
 *
 * <p>To create instances of this class use {@link org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.object.state.mgmt.rev150811.ModifyStateInputBuilder}.
 * @see org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.object.state.mgmt.rev150811.ModifyStateInputBuilder
 *
 */
public interface ModifyStateInput
    extends
    org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.common.types.rev150811.ModifyStateInput,
    NodeContextRef,
    DataObject,
    Augmentable<org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.object.state.mgmt.rev150811.ModifyStateInput>
{



    public static final QName QNAME = org.opendaylight.yangtools.yang.common.QName.create("urn:opendaylight:ocp:object:state:mgmt",
        "2015-08-11", "input").intern();


}

