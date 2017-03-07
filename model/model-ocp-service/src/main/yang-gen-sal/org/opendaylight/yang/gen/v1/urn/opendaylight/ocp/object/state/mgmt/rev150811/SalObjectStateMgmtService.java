package org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.object.state.mgmt.rev150811;
import org.opendaylight.yangtools.yang.binding.RpcService;
import org.opendaylight.yangtools.yang.common.RpcResult;
import java.util.concurrent.Future;


/**
 * Interface for implementing the following YANG RPCs defined in module <b>sal-object-state-mgmt</b>
 * <pre>
 * rpc modify-state {
 *     input {
 *         leaf node {
 *             type node-ref;
 *         }
 *         leaf objId {
 *             type ObjId;
 *         }
 *         leaf stateType {
 *             type StateType;
 *         }
 *         leaf stateValue {
 *             type StateVal;
 *         }
 *     }
 *     
 *     output {
 *         leaf result {
 *             type ModifyStateRes;
 *         }
 *         leaf objId {
 *             type ObjId;
 *         }
 *         leaf stateType {
 *             type StateType;
 *         }
 *         leaf stateValue {
 *             type StateVal;
 *         }
 *     }
 * }
 * rpc get-state {
 *     input {
 *         leaf node {
 *             type node-ref;
 *         }
 *         leaf objId {
 *             type ObjId;
 *         }
 *         leaf stateType {
 *             type StateAllType;
 *         }
 *         leaf eventDrivenReporting {
 *             type boolean;
 *         }
 *     }
 *     
 *     output {
 *         leaf result {
 *             type GetStateRes;
 *         }
 *         list obj {
 *             key "id"
 *             leaf id {
 *                 type ObjId;
 *             }
 *             list state {
 *                 key "type"
 *                 leaf type {
 *                     type StateType;
 *                 }
 *                 leaf value {
 *                     type StateVal;
 *                 }
 *             }
 *         }
 *     }
 * }
 * </pre>
 *
 */
public interface SalObjectStateMgmtService
    extends
    RpcService
{




    Future<RpcResult<ModifyStateOutput>> modifyState(ModifyStateInput input);
    
    /**
     * @return <code>java.util.concurrent.Future</code> <code>state</code>, or <code>null</code> if not present
     */
    Future<RpcResult<GetStateOutput>> getState(GetStateInput input);

}

