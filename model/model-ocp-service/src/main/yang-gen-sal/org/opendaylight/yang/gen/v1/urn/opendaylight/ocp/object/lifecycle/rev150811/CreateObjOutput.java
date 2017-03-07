package org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.object.lifecycle.rev150811;
import org.opendaylight.yangtools.yang.binding.DataObject;
import org.opendaylight.yangtools.yang.common.QName;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.common.types.rev150811.CreateObjGlobRes;
import org.opendaylight.yangtools.yang.binding.Augmentable;


/**
 * <p>This class represents the following YANG schema fragment defined in module <b>sal-object-lifecycle</b>
 * <pre>
 * container output {
 *     leaf globResult {
 *         type CreateObjGlobRes;
 *     }
 *     leaf objId {
 *         type ObjId;
 *     }
 *     list param {
 *         key "name"
 *         leaf name {
 *             type string;
 *         }
 *         leaf result {
 *             type CreateObjRes;
 *         }
 *     }
 *     uses createObjOutput;
 * }
 * </pre>
 * The schema path to identify an instance is
 * <i>sal-object-lifecycle/create-obj/output</i>
 *
 * <p>To create instances of this class use {@link org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.object.lifecycle.rev150811.CreateObjOutputBuilder}.
 * @see org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.object.lifecycle.rev150811.CreateObjOutputBuilder
 *
 */
public interface CreateObjOutput
    extends
    org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.common.types.rev150811.CreateObjOutput,
    DataObject,
    Augmentable<org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.object.lifecycle.rev150811.CreateObjOutput>
{



    public static final QName QNAME = org.opendaylight.yangtools.yang.common.QName.create("urn:opendaylight:ocp:object:lifecycle",
        "2015-08-11", "output").intern();

    /**
     * @return <code>org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.common.types.rev150811.CreateObjGlobRes</code> <code>globResult</code>, or <code>null</code> if not present
     */
    CreateObjGlobRes getGlobResult();

}

