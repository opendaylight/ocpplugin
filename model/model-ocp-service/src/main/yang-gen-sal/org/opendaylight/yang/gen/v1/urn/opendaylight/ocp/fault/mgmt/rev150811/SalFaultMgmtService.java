package org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.fault.mgmt.rev150811;
import org.opendaylight.yangtools.yang.binding.RpcService;
import org.opendaylight.yangtools.yang.common.RpcResult;
import java.util.concurrent.Future;


/**
 * Interface for implementing the following YANG RPCs defined in module <b>sal-fault-mgmt</b>
 * <pre>
 * rpc get-fault {
 *     input {
 *         leaf node {
 *             type node-ref;
 *         }
 *         leaf objId {
 *             type ObjId;
 *         }
 *         leaf eventDrivenReporting {
 *             type boolean;
 *         }
 *     }
 *     
 *     output {
 *         leaf result {
 *             type GetFaultRes;
 *         }
 *         list obj {
 *             key "id"
 *             leaf id {
 *                 type ObjId;
 *             }
 *             list fault {
 *                 key "id"
 *                 leaf id {
 *                     type FaultId;
 *                 }
 *                 leaf severity {
 *                     type FaultSeverity;
 *                 }
 *                 leaf timestamp {
 *                     type xsd-dateTime;
 *                 }
 *                 leaf descr {
 *                     type string;
 *                 }
 *                 leaf-list affectedObj {
 *                     type string;
 *                 }
 *             }
 *         }
 *     }
 * }
 * </pre>
 *
 */
public interface SalFaultMgmtService
    extends
    RpcService
{




    /**
     * @return <code>java.util.concurrent.Future</code> <code>fault</code>, or <code>null</code> if not present
     */
    Future<RpcResult<GetFaultOutput>> getFault(GetFaultInput input);

}

