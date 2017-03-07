package org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.fault.mgmt.rev150811;
import org.opendaylight.yangtools.yang.binding.DataObject;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.common.types.rev150811.GetFaultRes;
import org.opendaylight.yangtools.yang.common.QName;
import org.opendaylight.yangtools.yang.binding.Augmentable;


/**
 * <p>This class represents the following YANG schema fragment defined in module <b>sal-fault-mgmt</b>
 * <pre>
 * container output {
 *     leaf result {
 *         type GetFaultRes;
 *     }
 *     list obj {
 *         key "id"
 *         leaf id {
 *             type ObjId;
 *         }
 *         list fault {
 *             key "id"
 *             leaf id {
 *                 type FaultId;
 *             }
 *             leaf severity {
 *                 type FaultSeverity;
 *             }
 *             leaf timestamp {
 *                 type xsd-dateTime;
 *             }
 *             leaf descr {
 *                 type string;
 *             }
 *             leaf-list affectedObj {
 *                 type string;
 *             }
 *         }
 *     }
 *     uses getFaultOutput;
 * }
 * </pre>
 * The schema path to identify an instance is
 * <i>sal-fault-mgmt/get-fault/output</i>
 *
 * <p>To create instances of this class use {@link org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.fault.mgmt.rev150811.GetFaultOutputBuilder}.
 * @see org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.fault.mgmt.rev150811.GetFaultOutputBuilder
 *
 */
public interface GetFaultOutput
    extends
    org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.common.types.rev150811.GetFaultOutput,
    DataObject,
    Augmentable<org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.fault.mgmt.rev150811.GetFaultOutput>
{



    public static final QName QNAME = org.opendaylight.yangtools.yang.common.QName.create("urn:opendaylight:ocp:fault:mgmt",
        "2015-08-11", "output").intern();

    /**
     * @return <code>org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.common.types.rev150811.GetFaultRes</code> <code>result</code>, or <code>null</code> if not present
     */
    GetFaultRes getResult();

}

