package org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.config.mgmt.rev150811;
import org.opendaylight.yangtools.yang.binding.DataObject;
import org.opendaylight.yangtools.yang.common.QName;
import org.opendaylight.yangtools.yang.binding.Augmentable;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.common.types.rev150811.GetParamRes;


/**
 * <p>This class represents the following YANG schema fragment defined in module <b>sal-config-mgmt</b>
 * <pre>
 * container output {
 *     leaf result {
 *         type GetParamRes;
 *     }
 *     list obj {
 *         key "id"
 *         leaf id {
 *             type ObjId;
 *         }
 *         list param {
 *             key "name"
 *             leaf name {
 *                 type string;
 *             }
 *             leaf value {
 *                 type string;
 *             }
 *         }
 *     }
 *     uses getParamOutput;
 * }
 * </pre>
 * The schema path to identify an instance is
 * <i>sal-config-mgmt/get-param/output</i>
 *
 * <p>To create instances of this class use {@link org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.config.mgmt.rev150811.GetParamOutputBuilder}.
 * @see org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.config.mgmt.rev150811.GetParamOutputBuilder
 *
 */
public interface GetParamOutput
    extends
    org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.common.types.rev150811.GetParamOutput,
    DataObject,
    Augmentable<org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.config.mgmt.rev150811.GetParamOutput>
{



    public static final QName QNAME = org.opendaylight.yangtools.yang.common.QName.create("urn:opendaylight:ocp:config:mgmt",
        "2015-08-11", "output").intern();

    /**
     * @return <code>org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.common.types.rev150811.GetParamRes</code> <code>result</code>, or <code>null</code> if not present
     */
    GetParamRes getResult();

}

