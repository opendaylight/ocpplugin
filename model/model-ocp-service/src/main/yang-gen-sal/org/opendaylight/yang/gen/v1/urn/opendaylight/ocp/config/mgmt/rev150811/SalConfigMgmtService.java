package org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.config.mgmt.rev150811;
import org.opendaylight.yangtools.yang.binding.RpcService;
import org.opendaylight.yangtools.yang.common.RpcResult;
import java.util.concurrent.Future;


/**
 * Interface for implementing the following YANG RPCs defined in module <b>sal-config-mgmt</b>
 * <pre>
 * rpc modify-param {
 *     input {
 *         leaf node {
 *             type node-ref;
 *         }
 *         leaf objId {
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
 *     
 *     output {
 *         leaf globResult {
 *             type ModifyParamGlobRes;
 *         }
 *         leaf objId {
 *             type ObjId;
 *         }
 *         list param {
 *             key "name"
 *             leaf name {
 *                 type string;
 *             }
 *             leaf result {
 *                 type ModifyParamRes;
 *             }
 *         }
 *     }
 * }
 * rpc get-param {
 *     input {
 *         leaf node {
 *             type node-ref;
 *         }
 *         leaf objId {
 *             type ObjId;
 *         }
 *         leaf paramName {
 *             type string;
 *         }
 *     }
 *     
 *     output {
 *         leaf result {
 *             type GetParamRes;
 *         }
 *         list obj {
 *             key "id"
 *             leaf id {
 *                 type ObjId;
 *             }
 *             list param {
 *                 key "name"
 *                 leaf name {
 *                     type string;
 *                 }
 *                 leaf value {
 *                     type string;
 *                 }
 *             }
 *         }
 *     }
 * }
 * </pre>
 *
 */
public interface SalConfigMgmtService
    extends
    RpcService
{




    Future<RpcResult<ModifyParamOutput>> modifyParam(ModifyParamInput input);
    
    /**
     * @return <code>java.util.concurrent.Future</code> <code>param</code>, or <code>null</code> if not present
     */
    Future<RpcResult<GetParamOutput>> getParam(GetParamInput input);

}

