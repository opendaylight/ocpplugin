package org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.object.lifecycle.rev150811;
import org.opendaylight.yang.gen.v1.urn.opendaylight.inventory.rev130819.NodeContextRef;
import org.opendaylight.yangtools.yang.binding.DataObject;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.common.types.rev150811.ObjId;
import org.opendaylight.yangtools.yang.common.QName;
import org.opendaylight.yangtools.yang.binding.Augmentable;


/**
 * <p>This class represents the following YANG schema fragment defined in module <b>sal-object-lifecycle</b>
 * <pre>
 * container input {
 *     leaf node {
 *         type node-ref;
 *     }
 *     leaf obj-id {
 *         type ObjId;
 *     }
 *     uses node-context-ref;
 * }
 * </pre>
 * The schema path to identify an instance is
 * <i>sal-object-lifecycle/delete-obj/input</i>
 *
 * <p>To create instances of this class use {@link org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.object.lifecycle.rev150811.DeleteObjInputBuilder}.
 * @see org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.object.lifecycle.rev150811.DeleteObjInputBuilder
 *
 */
public interface DeleteObjInput
    extends
    NodeContextRef,
    DataObject,
    Augmentable<org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.object.lifecycle.rev150811.DeleteObjInput>
{



    public static final QName QNAME = org.opendaylight.yangtools.yang.common.QName.create("urn:opendaylight:ocp:object:lifecycle",
        "2015-08-11", "input").intern();

    /**
     * @return <code>org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.common.types.rev150811.ObjId</code> <code>objId</code>, or <code>null</code> if not present
     */
    ObjId getObjId();

}

