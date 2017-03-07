package org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.object.state.mgmt.rev150811;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.common.types.rev150811.GetStateRes;
import org.opendaylight.yangtools.yang.binding.DataObject;
import org.opendaylight.yangtools.yang.common.QName;
import org.opendaylight.yangtools.yang.binding.Augmentable;


/**
 * <p>This class represents the following YANG schema fragment defined in module <b>sal-object-state-mgmt</b>
 * <pre>
 * container output {
 *     leaf result {
 *         type GetStateRes;
 *     }
 *     list obj {
 *         key "id"
 *         leaf id {
 *             type ObjId;
 *         }
 *         list state {
 *             key "type"
 *             leaf type {
 *                 type StateType;
 *             }
 *             leaf value {
 *                 type StateVal;
 *             }
 *         }
 *     }
 *     uses getStateOutput;
 * }
 * </pre>
 * The schema path to identify an instance is
 * <i>sal-object-state-mgmt/get-state/output</i>
 *
 * <p>To create instances of this class use {@link org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.object.state.mgmt.rev150811.GetStateOutputBuilder}.
 * @see org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.object.state.mgmt.rev150811.GetStateOutputBuilder
 *
 */
public interface GetStateOutput
    extends
    org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.common.types.rev150811.GetStateOutput,
    DataObject,
    Augmentable<org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.object.state.mgmt.rev150811.GetStateOutput>
{



    public static final QName QNAME = org.opendaylight.yangtools.yang.common.QName.create("urn:opendaylight:ocp:object:state:mgmt",
        "2015-08-11", "output").intern();

    /**
     * @return <code>org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.common.types.rev150811.GetStateRes</code> <code>result</code>, or <code>null</code> if not present
     */
    GetStateRes getResult();

}

