package org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.fault.mgmt.rev150811;
import org.opendaylight.yang.gen.v1.urn.opendaylight.inventory.rev130819.NodeContextRef;
import org.opendaylight.yangtools.yang.binding.DataObject;
import org.opendaylight.yangtools.yang.common.QName;
import org.opendaylight.yangtools.yang.binding.Augmentable;


/**
 * <p>This class represents the following YANG schema fragment defined in module <b>sal-fault-mgmt</b>
 * <pre>
 * container input {
 *     leaf node {
 *         type node-ref;
 *     }
 *     leaf objId {
 *         type ObjId;
 *     }
 *     leaf eventDrivenReporting {
 *         type boolean;
 *     }
 *     uses getFaultInput;
 *     uses node-context-ref;
 * }
 * </pre>
 * The schema path to identify an instance is
 * <i>sal-fault-mgmt/get-fault/input</i>
 *
 * <p>To create instances of this class use {@link org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.fault.mgmt.rev150811.GetFaultInputBuilder}.
 * @see org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.fault.mgmt.rev150811.GetFaultInputBuilder
 *
 */
public interface GetFaultInput
    extends
    org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.common.types.rev150811.GetFaultInput,
    NodeContextRef,
    DataObject,
    Augmentable<org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.fault.mgmt.rev150811.GetFaultInput>
{



    public static final QName QNAME = org.opendaylight.yangtools.yang.common.QName.create("urn:opendaylight:ocp:fault:mgmt",
        "2015-08-11", "input").intern();


}

