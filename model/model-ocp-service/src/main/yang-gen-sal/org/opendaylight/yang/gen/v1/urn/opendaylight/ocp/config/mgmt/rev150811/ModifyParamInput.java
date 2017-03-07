package org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.config.mgmt.rev150811;
import org.opendaylight.yang.gen.v1.urn.opendaylight.inventory.rev130819.NodeContextRef;
import org.opendaylight.yangtools.yang.binding.DataObject;
import org.opendaylight.yangtools.yang.common.QName;
import org.opendaylight.yangtools.yang.binding.Augmentable;


/**
 * <p>This class represents the following YANG schema fragment defined in module <b>sal-config-mgmt</b>
 * <pre>
 * container input {
 *     leaf node {
 *         type node-ref;
 *     }
 *     leaf objId {
 *         type ObjId;
 *     }
 *     list param {
 *         key "name"
 *         leaf name {
 *             type string;
 *         }
 *         leaf value {
 *             type string;
 *         }
 *     }
 *     uses modifyParamInput;
 *     uses node-context-ref;
 * }
 * </pre>
 * The schema path to identify an instance is
 * <i>sal-config-mgmt/modify-param/input</i>
 *
 * <p>To create instances of this class use {@link org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.config.mgmt.rev150811.ModifyParamInputBuilder}.
 * @see org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.config.mgmt.rev150811.ModifyParamInputBuilder
 *
 */
public interface ModifyParamInput
    extends
    org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.common.types.rev150811.ModifyParamInput,
    NodeContextRef,
    DataObject,
    Augmentable<org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.config.mgmt.rev150811.ModifyParamInput>
{



    public static final QName QNAME = org.opendaylight.yangtools.yang.common.QName.create("urn:opendaylight:ocp:config:mgmt",
        "2015-08-11", "input").intern();


}

