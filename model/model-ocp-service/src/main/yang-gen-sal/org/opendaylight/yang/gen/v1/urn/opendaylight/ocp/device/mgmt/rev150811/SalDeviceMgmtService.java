package org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.device.mgmt.rev150811;
import org.opendaylight.yangtools.yang.binding.RpcService;
import org.opendaylight.yangtools.yang.common.RpcResult;
import java.util.concurrent.Future;


/**
 * Interface for implementing the following YANG RPCs defined in module <b>sal-device-mgmt</b>
 * <pre>
 * rpc re-reset {
 *     input {
 *         leaf node {
 *             type node-ref;
 *         }
 *     }
 *     
 *     output {
 *         leaf result {
 *             type OriRes;
 *         }
 *     }
 * }
 * rpc set-time {
 *     input {
 *         leaf node {
 *             type node-ref;
 *         }
 *         leaf newTime {
 *             type xsd-dateTime;
 *         }
 *     }
 *     
 *     output {
 *         leaf result {
 *             type OriRes;
 *         }
 *     }
 * }
 * rpc health-check {
 *     input {
 *         leaf node {
 *             type node-ref;
 *         }
 *         leaf tcpLinkMonTimeout {
 *             type xsd-unsignedShort;
 *         }
 *     }
 *     
 *     output {
 *         leaf result {
 *             type OriRes;
 *         }
 *     }
 * }
 * </pre>
 *
 */
public interface SalDeviceMgmtService
    extends
    RpcService
{




    Future<RpcResult<ReResetOutput>> reReset(ReResetInput input);
    
    Future<RpcResult<SetTimeOutput>> setTime(SetTimeInput input);
    
    Future<RpcResult<HealthCheckOutput>> healthCheck(HealthCheckInput input);

}

