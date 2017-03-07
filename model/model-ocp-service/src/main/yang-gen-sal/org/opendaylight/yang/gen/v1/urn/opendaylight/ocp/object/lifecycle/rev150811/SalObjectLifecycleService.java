package org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.object.lifecycle.rev150811;
import org.opendaylight.yangtools.yang.binding.RpcService;
import org.opendaylight.yangtools.yang.common.RpcResult;
import java.util.concurrent.Future;


/**
 * Interface for implementing the following YANG RPCs defined in module <b>sal-object-lifecycle</b>
 * <pre>
 * rpc delete-obj {
 *     input {
 *         leaf node {
 *             type node-ref;
 *         }
 *         leaf obj-id {
 *             type ObjId;
 *         }
 *     }
 *     
 *     output {
 *         leaf result {
 *             type DeleteObjRes;
 *         }
 *     }
 * }
 * rpc create-obj {
 *     input {
 *         leaf node {
 *             type node-ref;
 *         }
 *         leaf objType {
 *             type ObjType;
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
 *             type CreateObjGlobRes;
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
 *                 type CreateObjRes;
 *             }
 *         }
 *     }
 * }
 * </pre>
 *
 */
public interface SalObjectLifecycleService
    extends
    RpcService
{




    Future<RpcResult<DeleteObjOutput>> deleteObj(DeleteObjInput input);
    
    Future<RpcResult<CreateObjOutput>> createObj(CreateObjInput input);

}

