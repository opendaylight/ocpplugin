package org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.object.lifecycle.rev150811;
import org.opendaylight.yangtools.yang.binding.DataObject;
import org.opendaylight.yangtools.yang.common.QName;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.common.types.rev150811.DeleteObjRes;
import org.opendaylight.yangtools.yang.binding.Augmentable;


/**
 * <p>This class represents the following YANG schema fragment defined in module <b>sal-object-lifecycle</b>
 * <pre>
 * container output {
 *     leaf result {
 *         type DeleteObjRes;
 *     }
 * }
 * </pre>
 * The schema path to identify an instance is
 * <i>sal-object-lifecycle/delete-obj/output</i>
 *
 * <p>To create instances of this class use {@link org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.object.lifecycle.rev150811.DeleteObjOutputBuilder}.
 * @see org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.object.lifecycle.rev150811.DeleteObjOutputBuilder
 *
 */
public interface DeleteObjOutput
    extends
    DataObject,
    Augmentable<org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.object.lifecycle.rev150811.DeleteObjOutput>
{



    public static final QName QNAME = org.opendaylight.yangtools.yang.common.QName.create("urn:opendaylight:ocp:object:lifecycle",
        "2015-08-11", "output").intern();

    /**
     * @return <code>org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.common.types.rev150811.DeleteObjRes</code> <code>result</code>, or <code>null</code> if not present
     */
    DeleteObjRes getResult();

}

