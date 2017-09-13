/*
 * Copyright (c) 2015 Foxconn Corporation and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.ocpplugin.applications.ocpservice;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import org.opendaylight.controller.md.sal.binding.api.DataBroker;
import org.opendaylight.controller.sal.binding.api.NotificationProviderService;
import org.opendaylight.yang.gen.v1.urn.opendaylight.inventory.rev130819.NodeId;
import org.opendaylight.yang.gen.v1.urn.opendaylight.inventory.rev130819.NodeRef;
import org.opendaylight.yang.gen.v1.urn.opendaylight.inventory.rev130819.Nodes;
import org.opendaylight.yang.gen.v1.urn.opendaylight.inventory.rev130819.nodes.Node;
import org.opendaylight.yang.gen.v1.urn.opendaylight.inventory.rev130819.nodes.NodeKey;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.common.types.rev150811.CreateObjGlobRes;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.common.types.rev150811.CreateObjRes;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.common.types.rev150811.DeleteObjRes;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.common.types.rev150811.ModifyParamRes;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.common.types.rev150811.ObjId;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.config.mgmt.rev150811.GetParamInputBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.config.mgmt.rev150811.GetParamOutput;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.config.mgmt.rev150811.ModifyParamInputBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.config.mgmt.rev150811.ModifyParamOutput;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.config.mgmt.rev150811.SalConfigMgmtService;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.device.mgmt.rev150811.DeviceConnected;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.device.mgmt.rev150811.DeviceDisconnected;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.device.mgmt.rev150811.HealthCheckInputBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.device.mgmt.rev150811.HealthCheckOutput;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.device.mgmt.rev150811.ReResetInputBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.device.mgmt.rev150811.ReResetOutput;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.device.mgmt.rev150811.SalDeviceMgmtListener;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.device.mgmt.rev150811.SalDeviceMgmtService;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.device.mgmt.rev150811.SetTimeInputBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.device.mgmt.rev150811.SetTimeOutput;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.fault.mgmt.rev150811.GetFaultInputBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.fault.mgmt.rev150811.GetFaultOutput;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.fault.mgmt.rev150811.SalFaultMgmtService;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.object.lifecycle.rev150811.CreateObjInputBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.object.lifecycle.rev150811.CreateObjOutput;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.object.lifecycle.rev150811.DeleteObjInputBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.object.lifecycle.rev150811.DeleteObjOutput;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.object.lifecycle.rev150811.SalObjectLifecycleService;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.object.state.mgmt.rev150811.GetStateInputBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.object.state.mgmt.rev150811.GetStateOutput;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.object.state.mgmt.rev150811.ModifyStateInputBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.object.state.mgmt.rev150811.ModifyStateOutput;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.object.state.mgmt.rev150811.SalObjectStateMgmtService;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.xsd.types.rev150811.XsdDateTime;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.xsd.types.rev150811.XsdUnsignedShort;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.ocp.applications.ocp.resourcemodel.rev150811.ResourceModel;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.ocp.applications.ocp.service.rev150811.AlignmentCompletedBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.ocp.applications.ocp.service.rev150811.CreateObjNbInput;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.ocp.applications.ocp.service.rev150811.CreateObjNbOutput;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.ocp.applications.ocp.service.rev150811.CreateObjNbOutputBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.ocp.applications.ocp.service.rev150811.DeleteObjNbInput;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.ocp.applications.ocp.service.rev150811.DeleteObjNbOutput;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.ocp.applications.ocp.service.rev150811.DeleteObjNbOutputBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.ocp.applications.ocp.service.rev150811.GetFaultNbInput;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.ocp.applications.ocp.service.rev150811.GetFaultNbOutput;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.ocp.applications.ocp.service.rev150811.GetFaultNbOutputBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.ocp.applications.ocp.service.rev150811.GetParamNbInput;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.ocp.applications.ocp.service.rev150811.GetParamNbOutput;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.ocp.applications.ocp.service.rev150811.GetParamNbOutputBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.ocp.applications.ocp.service.rev150811.GetStateNbInput;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.ocp.applications.ocp.service.rev150811.GetStateNbOutput;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.ocp.applications.ocp.service.rev150811.GetStateNbOutputBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.ocp.applications.ocp.service.rev150811.HealthCheckNbInput;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.ocp.applications.ocp.service.rev150811.HealthCheckNbOutput;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.ocp.applications.ocp.service.rev150811.HealthCheckNbOutputBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.ocp.applications.ocp.service.rev150811.ModifyParamNbInput;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.ocp.applications.ocp.service.rev150811.ModifyParamNbOutput;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.ocp.applications.ocp.service.rev150811.ModifyParamNbOutputBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.ocp.applications.ocp.service.rev150811.ModifyStateNbInput;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.ocp.applications.ocp.service.rev150811.ModifyStateNbOutput;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.ocp.applications.ocp.service.rev150811.ModifyStateNbOutputBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.ocp.applications.ocp.service.rev150811.OcpServiceService;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.ocp.applications.ocp.service.rev150811.ReResetNbInput;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.ocp.applications.ocp.service.rev150811.ReResetNbOutput;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.ocp.applications.ocp.service.rev150811.ReResetNbOutputBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.ocp.applications.ocp.service.rev150811.SetTimeNbInput;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.ocp.applications.ocp.service.rev150811.SetTimeNbOutput;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.ocp.applications.ocp.service.rev150811.SetTimeNbOutputBuilder;
import org.opendaylight.yangtools.concepts.ListenerRegistration;
import org.opendaylight.yangtools.yang.binding.InstanceIdentifier;
import org.opendaylight.yangtools.yang.binding.NotificationListener;
import org.opendaylight.yangtools.yang.common.RpcError;
import org.opendaylight.yangtools.yang.common.RpcResult;
import org.opendaylight.yangtools.yang.common.RpcResultBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/*
 * @author Jason Yuan <jason.cw.yuan@foxconn.com>
 *
 */
public class OcpService implements OcpServiceService, SalDeviceMgmtListener, AutoCloseable {
    private static final Logger LOG = LoggerFactory.getLogger(OcpService.class);
    public static final InstanceIdentifier<ResourceModel> RM_IID = InstanceIdentifier.builder(ResourceModel.class).build();

    private final DataBroker dataBroker;
    private final NotificationProviderService notificationProvider;
    private final ExecutorService executor;
    private final SalConfigMgmtService ocpConfigMgmtService;
    private final SalFaultMgmtService ocpFaultMgmtService;
    private final SalObjectStateMgmtService ocpObjectStateMgmtService;
    private final SalDeviceMgmtService ocpDeviceMgmtService;
    private final SalObjectLifecycleService ocpObjectLifecycleService;
    private static final InstanceIdentifier<Nodes> NODES_IDENTIFIER = InstanceIdentifier.create(Nodes.class);
    private boolean freshStart = true;
    private ListenerRegistration<NotificationListener> ocpListenerReg;

    public OcpService(final DataBroker dataBroker, final NotificationProviderService notificationProvider,
            final SalConfigMgmtService ocpConfigMgmtService, final SalFaultMgmtService ocpFaultMgmtService,
            final SalObjectStateMgmtService ocpObjectStateMgmtService, final SalDeviceMgmtService ocpDeviceMgmtService,
            final SalObjectLifecycleService ocpObjectLifecycleService) {
        this.dataBroker = dataBroker;
        this.notificationProvider = notificationProvider;
        this.ocpConfigMgmtService = ocpConfigMgmtService;
        this.ocpFaultMgmtService = ocpFaultMgmtService;
        this.ocpObjectStateMgmtService = ocpObjectStateMgmtService;
        this.ocpDeviceMgmtService = ocpDeviceMgmtService;
        this.ocpObjectLifecycleService = ocpObjectLifecycleService;
        executor = Executors.newFixedThreadPool(1);
        LOG.info("OcpService() created");
    }

    public void init() {
        ocpListenerReg = notificationProvider.registerNotificationListener(this);
    }

    @Override
    public void close() {
        if (ocpListenerReg != null) {
            ocpListenerReg.close();
        }

        // When we close this service we need to shutdown our executor!
        executor.shutdown();
    }

    @Override
    public Future<RpcResult<GetParamNbOutput>> getParamNb(GetParamNbInput input) {
        GetParamInputBuilder inputBuilder = new GetParamInputBuilder();
        GetParamOutput getParamOutput;
        inputBuilder.setNode(new NodeRef(NODES_IDENTIFIER.child(Node.class, new NodeKey(new NodeId(input.getNodeId())))));
        inputBuilder.setObjId(input.getObjId());
        inputBuilder.setParamName(input.getParamName());
        GetParamNbOutputBuilder outputBuilder = new  GetParamNbOutputBuilder();
        ResourceModelBroker resourceModelBroker = new ResourceModelBroker(input.getNodeId() ,dataBroker);

        int objKey;
        int i = 0;

        LOG.info("Start doing getParamNb with input {}", input);
        try {
            RpcResult<GetParamOutput> result = ocpConfigMgmtService.getParam(inputBuilder.build()).get();
            LOG.info("Get result {}", result);
            if (result.isSuccessful()) {
                getParamOutput = result.getResult();
                while (i < getParamOutput.getObj().size()) {
                    LOG.debug("Processing dataObject : {}", getParamOutput.getObj().get(i));
                    resourceModelBroker.updateObj(getParamOutput.getObj().get(i).getParam(), getParamOutput.getObj().get(i).getId().getValue().toString());
                    i++;
                }
                return  RpcResultBuilder.success(outputBuilder.setResult(getParamOutput.getResult()).setObj(getParamOutput.getObj()).build()).buildFuture();
            } else {
                LOG.info("GetParamNb Failed(SB returns error) on RE({})", input.getNodeId());
                return  RpcResultBuilder.<GetParamNbOutput>failed().withError(RpcError.ErrorType.APPLICATION,
                            "Failed to get param.(SB returns error)").buildFuture();
            }
        } catch (Exception exc) {
            LOG.info("GetParam Failed{}", exc);
            return  RpcResultBuilder.<GetParamNbOutput>failed().withError(RpcError.ErrorType.APPLICATION,
                            "Failed to  get param.(SB RPC error)").buildFuture();
        }
    }

    @Override
    public Future<RpcResult<ModifyParamNbOutput>> modifyParamNb(ModifyParamNbInput input) {
        ModifyParamInputBuilder inputBuilder = new ModifyParamInputBuilder();
        ModifyParamOutput modifyParamOutput;
        inputBuilder.setNode(new NodeRef(NODES_IDENTIFIER.child(Node.class, new NodeKey(new NodeId(input.getNodeId())))));
        inputBuilder.setObjId(input.getObjId());
        inputBuilder.setParam(input.getParam());
        ModifyParamNbOutputBuilder outputBuilder = new  ModifyParamNbOutputBuilder();
        ResourceModelBroker resourceModelBroker = new ResourceModelBroker(input.getNodeId() ,dataBroker);
        int objKey;
        int i = 0;

        LOG.debug("Start doing modifyParamNb with input {}", input);
        try {
            RpcResult<ModifyParamOutput> result = ocpConfigMgmtService.modifyParam(inputBuilder.build()).get();
            LOG.debug("modifyParam result {}", result);

            if (result.isSuccessful()) {
                modifyParamOutput = result.getResult();
                List<org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.common.types.rev150811.getparamoutput.obj.Param> outputParams =
                        new ArrayList<>();

                while (i < input.getParam().size()) {
                    if (modifyParamOutput.getParam().get(i).getResult() != ModifyParamRes.SUCCESS) {
                        i++;
                        continue;
                    }

                    org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.common.types.rev150811.getparamoutput.obj.ParamBuilder outputParamBuilder =
                        new org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.common.types.rev150811.getparamoutput.obj.ParamBuilder();
                    outputParamBuilder.setName(input.getParam().get(i).getName());
                    outputParamBuilder.setValue(input.getParam().get(i).getValue());
                    outputParams.add(outputParamBuilder.build());
                    i++;
                }

                LOG.debug("Updating dataObject : {}", outputParams);
                resourceModelBroker.updateObj(outputParams, input.getObjId().getValue().toString());

                return  RpcResultBuilder.success(outputBuilder.setGlobResult(modifyParamOutput.getGlobResult()).setObjId(modifyParamOutput.getObjId()).setParam(modifyParamOutput.getParam()).build()).buildFuture();
            } else {
                LOG.error("ModifyParamNb failed (SB returned error) on RE({})", input.getNodeId());
                return  RpcResultBuilder.<ModifyParamNbOutput>failed().withError(RpcError.ErrorType.APPLICATION,
                            "Failed to modify param (SB returned error)").buildFuture();
            }
        } catch (Exception exc) {
            LOG.error("ModifyParam failed {}", exc);
            return  RpcResultBuilder.<ModifyParamNbOutput>failed().withError(RpcError.ErrorType.APPLICATION,
                            "Failed to modify param (SB RPC error)").buildFuture();
        }
    }

    @Override
    public Future<RpcResult<HealthCheckNbOutput>> healthCheckNb(HealthCheckNbInput input) {
        HealthCheckInputBuilder inputBuilder = new HealthCheckInputBuilder();
        HealthCheckOutput healthCheckOutput;
        inputBuilder.setNode(new NodeRef(NODES_IDENTIFIER.child(Node.class, new NodeKey(new NodeId(input.getNodeId())))));
        inputBuilder.setTcpLinkMonTimeout(new XsdUnsignedShort(input.getTcpLinkMonTimeout()));
        HealthCheckNbOutputBuilder outputBuilder = new HealthCheckNbOutputBuilder();

        LOG.info("Start doing healthCheckNb with input {}", input);
        try {
            RpcResult<HealthCheckOutput> result = ocpDeviceMgmtService.healthCheck(inputBuilder.build()).get();
            LOG.info("Get result {}", result);
            if (result.isSuccessful()) {
                healthCheckOutput = result.getResult();
                return  RpcResultBuilder.success(outputBuilder.setResult(healthCheckOutput.getResult()).build()).buildFuture();
            } else {
                LOG.info("HealthCheckNb Failed(SB returns error) on RE({})", input.getNodeId());
                return  RpcResultBuilder.<HealthCheckNbOutput>failed().withError(RpcError.ErrorType.APPLICATION,
                            "Failed to do HealthCheck.(SB returns error)").buildFuture();
            }
        } catch (Exception exc) {
            LOG.info("HealthCheck Failed{}", exc);
            return  RpcResultBuilder.<HealthCheckNbOutput>failed().withError(RpcError.ErrorType.APPLICATION,
                            "Failed to do HealthCheck.(SB RPC error)").buildFuture();
        }
    }

    @Override
    public Future<RpcResult<SetTimeNbOutput>> setTimeNb(SetTimeNbInput input) {
        SetTimeInputBuilder inputBuilder = new SetTimeInputBuilder();
        SetTimeOutput setTimeOutput;
        inputBuilder.setNode(new NodeRef(NODES_IDENTIFIER.child(Node.class, new NodeKey(new NodeId(input.getNodeId())))));
        inputBuilder.setNewTime(input.getNewTime());
        SetTimeNbOutputBuilder outputBuilder = new SetTimeNbOutputBuilder();

        LOG.info("Start doing setTimeNb with input {}", input);
        try {
            RpcResult<SetTimeOutput> result = ocpDeviceMgmtService.setTime(inputBuilder.build()).get();
            LOG.info("Get result {}", result);
            if (result.isSuccessful()) {
                setTimeOutput = result.getResult();
                return  RpcResultBuilder.success(outputBuilder.setResult(setTimeOutput.getResult()).build()).buildFuture();
            } else {
                LOG.info("SetTimeNb Failed(SB returns error) on RE({})", input.getNodeId());
                return  RpcResultBuilder.<SetTimeNbOutput>failed().withError(RpcError.ErrorType.APPLICATION,
                            "Failed to set time .(SB returns error)").buildFuture();
            }
        } catch (Exception exc) {
            LOG.info("setTimeNb Failed{}", exc);
            return  RpcResultBuilder.<SetTimeNbOutput>failed().withError(RpcError.ErrorType.APPLICATION,
                            "Failed to set time.(SB RPC error)").buildFuture();
        }
    }

    @Override
    public Future<RpcResult<ReResetNbOutput>> reResetNb(ReResetNbInput input)  {
        ReResetInputBuilder inputBuilder = new ReResetInputBuilder();
        ReResetOutput reResetOutput;
        inputBuilder.setNode(new NodeRef(NODES_IDENTIFIER.child(Node.class, new NodeKey(new NodeId(input.getNodeId())))));
        ReResetNbOutputBuilder outputBuilder = new ReResetNbOutputBuilder();

        LOG.info("Start doing reResetNb with input {}", input);
        try {
            RpcResult<ReResetOutput> result = ocpDeviceMgmtService.reReset(inputBuilder.build()).get();
            LOG.info("Get result {}", result);
            if (result.isSuccessful()) {
                reResetOutput = result.getResult();
                return  RpcResultBuilder.success(outputBuilder.setResult(reResetOutput.getResult()).build()).buildFuture();
            } else {
                LOG.info("ReResetNb Failed(SB returns error) on RE({})", input.getNodeId());
                return  RpcResultBuilder.<ReResetNbOutput>failed().withError(RpcError.ErrorType.APPLICATION,
                            "Failed to reset RE.(SB returns error)").buildFuture();
            }
        } catch (Exception exc) {
            LOG.info("reResetNb Failed{}", exc);
            return  RpcResultBuilder.<ReResetNbOutput>failed().withError(RpcError.ErrorType.APPLICATION,
                            "Failed to reset RE.(SB RPC error)").buildFuture();
        }
    }

    @Override
    public Future<RpcResult<GetFaultNbOutput>> getFaultNb(GetFaultNbInput input) {
        GetFaultInputBuilder inputBuilder = new GetFaultInputBuilder();
        GetFaultOutput getFaultOutput;
        inputBuilder.setNode(new NodeRef(NODES_IDENTIFIER.child(Node.class, new NodeKey(new NodeId(input.getNodeId())))));
        inputBuilder.setObjId(input.getObjId());
        inputBuilder.setEventDrivenReporting(input.isEventDrivenReporting());
        GetFaultNbOutputBuilder outputBuilder = new GetFaultNbOutputBuilder();

        LOG.info("Start doing getFaultNb with input {}", input);
        try {
            RpcResult<GetFaultOutput> result = ocpFaultMgmtService.getFault(inputBuilder.build()).get();
            LOG.info("Get result {}", result);
            if (result.isSuccessful()) {
                getFaultOutput = result.getResult();
                return  RpcResultBuilder.success(outputBuilder.setResult(getFaultOutput.getResult()).setObj(getFaultOutput.getObj()).build()).buildFuture();
            } else {
                LOG.info("getFaultNb Failed(SB returns error) on RE({})", input.getNodeId());
                return  RpcResultBuilder.<GetFaultNbOutput>failed().withError(RpcError.ErrorType.APPLICATION,
                            "Failed to get fault.(SB returns error)").buildFuture();
            }
        } catch (Exception exc) {
            LOG.info("getFaultNb Failed{}", exc);
            return  RpcResultBuilder.<GetFaultNbOutput>failed().withError(RpcError.ErrorType.APPLICATION,
                            "Failed to get fault.(SB RPC error)").buildFuture();
        }
    }

    @Override
    public Future<RpcResult<GetStateNbOutput>> getStateNb(GetStateNbInput input) {
        GetStateInputBuilder inputBuilder = new GetStateInputBuilder();
        GetStateOutput getStateOutput;
        inputBuilder.setNode(new NodeRef(NODES_IDENTIFIER.child(Node.class, new NodeKey(new NodeId(input.getNodeId())))));
        inputBuilder.setObjId(input.getObjId());
        inputBuilder.setStateType(input.getStateType());
        inputBuilder.setEventDrivenReporting(input.isEventDrivenReporting());
        GetStateNbOutputBuilder outputBuilder = new GetStateNbOutputBuilder();

        LOG.info("Start doing getStateNb with input {}", input);
        try {
            RpcResult<GetStateOutput> result = ocpObjectStateMgmtService.getState(inputBuilder.build()).get();
            LOG.info("Get result {}", result);
            if (result.isSuccessful()) {
                getStateOutput = result.getResult();
                return  RpcResultBuilder.success(outputBuilder.setResult(getStateOutput.getResult()).setObj(getStateOutput.getObj()).build()).buildFuture();
            } else {
                LOG.info("getStateNb Failed(SB returns error) on RE({})", input.getNodeId());
                return  RpcResultBuilder.<GetStateNbOutput>failed().withError(RpcError.ErrorType.APPLICATION,
                            "Failed to modify state.(SB returns error)").buildFuture();
            }
        } catch (Exception exc) {
            LOG.info("getStateNb Failed{}", exc);
            return  RpcResultBuilder.<GetStateNbOutput>failed().withError(RpcError.ErrorType.APPLICATION,
                            "Failed to get state.(SB RPC error)").buildFuture();
        }
    }

    @Override
    public Future<RpcResult<ModifyStateNbOutput>> modifyStateNb(ModifyStateNbInput input) {
        ModifyStateInputBuilder inputBuilder = new ModifyStateInputBuilder();
        ModifyStateOutput modifyStateOutput;
        inputBuilder.setNode(new NodeRef(NODES_IDENTIFIER.child(Node.class, new NodeKey(new NodeId(input.getNodeId())))));
        inputBuilder.setObjId(input.getObjId());
        inputBuilder.setStateType(input.getStateType());
        inputBuilder.setStateValue(input.getStateValue());
        ModifyStateNbOutputBuilder outputBuilder = new ModifyStateNbOutputBuilder();

        LOG.info("Start doing modifyStateNb with input {}", input);
        try {
            RpcResult<ModifyStateOutput> result = ocpObjectStateMgmtService.modifyState(inputBuilder.build()).get();
            LOG.info("Get result {}", result);
            if (result.isSuccessful()) {
                modifyStateOutput = result.getResult();
                return  RpcResultBuilder.success(outputBuilder.setResult(modifyStateOutput.getResult()).setObjId(modifyStateOutput.getObjId()).setStateType(modifyStateOutput.getStateType()).setStateValue(modifyStateOutput.getStateValue()).build()).buildFuture();
            } else {
                LOG.error("modifyStateNb failed (SB returned error) on RE({})", input.getNodeId());
                return  RpcResultBuilder.<ModifyStateNbOutput>failed().withError(RpcError.ErrorType.APPLICATION,
                            "Failed to modify state.(SB returned error)").buildFuture();
            }
        } catch (Exception exc) {
            LOG.error("modifyStateNb failed{}", exc);
            return  RpcResultBuilder.<ModifyStateNbOutput>failed().withError(RpcError.ErrorType.APPLICATION,
                            "Failed to modify state (SB rpc error)").buildFuture();
        }
    }

    @Override
    public Future<RpcResult<CreateObjNbOutput>> createObjNb(CreateObjNbInput input) {
        CreateObjInputBuilder inputBuilder = new CreateObjInputBuilder();
        CreateObjOutput createObjOutput;
        inputBuilder.setNode(new NodeRef(NODES_IDENTIFIER.child(Node.class, new NodeKey(new NodeId(input.getNodeId())))));
        inputBuilder.setObjType(input.getObjType());
        inputBuilder.setParam(input.getParam());
        CreateObjNbOutputBuilder outputBuilder = new CreateObjNbOutputBuilder();
        ResourceModelBroker resourceModelBroker = new ResourceModelBroker(input.getNodeId(), dataBroker);
        int i = 0;

        LOG.debug("Start doing createObjNb with input {}", input);
        try {
            RpcResult<CreateObjOutput> result = ocpObjectLifecycleService.createObj(inputBuilder.build()).get();
            LOG.debug("createObj result {}", result);

            if (result.isSuccessful()) {
                createObjOutput = result.getResult();
                if (createObjOutput.getGlobResult() == CreateObjGlobRes.SUCCESS) {
                    List<org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.common.types.rev150811.getparamoutput.obj.Param> outputParams =
                        new ArrayList<>();
                    try {
                        while (i < input.getParam().size()) {
                            if (createObjOutput.getParam().get(i).getResult() != CreateObjRes.SUCCESS){
                                i++;
                                continue;
                            }
                            org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.common.types.rev150811.getparamoutput.obj.ParamBuilder outputParamBuilder = new org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.common.types.rev150811.getparamoutput.obj.ParamBuilder();
                            outputParamBuilder.setName(input.getParam().get(i).getName());
                            outputParamBuilder.setValue(input.getParam().get(i).getValue());
                            outputParams.add(outputParamBuilder.build());
                            i++;
                        }
                    } catch (Exception exc) {
                        LOG.debug("No param existing in RE {}", input.getNodeId());
                    }
                    LOG.debug("Creating dataObject : {}", outputParams);
                    resourceModelBroker.updateObj(outputParams, createObjOutput.getObjId().getValue().toString());
                }
                return  RpcResultBuilder.success(outputBuilder.setGlobResult(createObjOutput.getGlobResult()).setObjId(createObjOutput.getObjId()).setParam(createObjOutput.getParam()).build()).buildFuture();
            } else {
                LOG.error("CreateObjectNb failed (SB returned error) on RE({})", input.getNodeId());
                return  RpcResultBuilder.<CreateObjNbOutput>failed().withError(RpcError.ErrorType.APPLICATION,
                            "Failed to create object (SB returned error)").buildFuture();
            }
        } catch  (Exception exc) {
            LOG.error("CreateObject Failed{}", exc);
            return  RpcResultBuilder.<CreateObjNbOutput>failed().withError(RpcError.ErrorType.APPLICATION,
                            "Failed to create object (SB RPC error)").buildFuture();
        }
    }

    @Override
    public Future<RpcResult<DeleteObjNbOutput>> deleteObjNb(DeleteObjNbInput input) {
        DeleteObjInputBuilder inputBuilder = new DeleteObjInputBuilder();
        DeleteObjOutput deleteObjOutput;
        inputBuilder.setNode(new NodeRef(NODES_IDENTIFIER.child(Node.class, new NodeKey(new NodeId(input.getNodeId())))));
        inputBuilder.setObjId(input.getObjId());
        DeleteObjNbOutputBuilder outputBuilder = new  DeleteObjNbOutputBuilder();
        ResourceModelBroker resourceModelBroker = new ResourceModelBroker(input.getNodeId() ,dataBroker);

        LOG.info("Start doing deleteObjNb with input {}", input);
        try {
            RpcResult<DeleteObjOutput> result = ocpObjectLifecycleService.deleteObj(inputBuilder.build()).get();
            LOG.info("Get result {}", result);
            if (result.isSuccessful()) {
                deleteObjOutput = result.getResult();
                    if (deleteObjOutput.getResult() == DeleteObjRes.forValue(0)) {
                        LOG.debug("Doing deleteObjNb with input {}", input);
                        resourceModelBroker.deleteObj(input.getObjId().getValue().toString());
                    }
                return  RpcResultBuilder.success(outputBuilder.setResult(deleteObjOutput.getResult()).build()).buildFuture();
            } else {
                LOG.info("DeleteObjectNB Failed(SB returns error) on RE({})", input.getNodeId());
                return  RpcResultBuilder.<DeleteObjNbOutput>failed().withError(RpcError.ErrorType.APPLICATION,
                            "Failed to delete param.(SB returns error)").buildFuture();
            }
        } catch (Exception exc) {
            LOG.info("DeleteObject Failed{}", exc);
            return  RpcResultBuilder.<DeleteObjNbOutput>failed().withError(RpcError.ErrorType.APPLICATION,
                            "Failed to  delete param.(SB RPC error)").buildFuture();
        }
    }

    @Override
    public void onDeviceConnected(final DeviceConnected notification) {

        ExecutorService connectedExecutor = Executors.newCachedThreadPool();

        Future<String> future = connectedExecutor.submit(() -> {
            ResourceModelBroker resourceModelBroker = new ResourceModelBroker(notification.getNodeId() ,dataBroker);
            GetParamInputBuilder inputBuilder = new GetParamInputBuilder();
            GetParamOutput getParamOutput;
            GetParamNbOutputBuilder outputBuilder = new GetParamNbOutputBuilder();
            SetTimeInputBuilder setInputBuilder = new SetTimeInputBuilder();
            SetTimeOutput setTimeOutput;
            int i = 0;

            LOG.debug("Get deviceConnected notification with notification {}", notification);

            try {
                if (!freshStart) {
                    resourceModelBroker.deleteObj("ALL");
                }
                freshStart = false;
            } catch (Exception exc1) {
                    LOG.info("Failed to delete node:({}, {})", notification.getNodeId(), exc1);
            }
            try {
                setInputBuilder.setNode(new NodeRef(NODES_IDENTIFIER.child(Node.class, new NodeKey(new NodeId(notification.getNodeId())))));
                setInputBuilder.setNewTime(new XsdDateTime(getSystemTime()));
                LOG.debug("Starting setTime({}) on {} in alignment", getSystemTime(), notification.getNodeId());
                RpcResult<SetTimeOutput> timeresult = ocpDeviceMgmtService.setTime(setInputBuilder.build()).get();
                LOG.debug("SetTime result {}", timeresult);
                if (timeresult.isSuccessful()) {
                    LOG.info("SetTimeNb done on ({})", notification.getNodeId());
                } else {
                    LOG.error("SetTimeNb failed when doing alignment (SB returned error) on ({})", notification.getNodeId());
                }
            } catch (Exception exc2) {
                LOG.error("Failed to setTime on {} in alignment {}", notification.getNodeId(), exc2);
            }

            try {
                inputBuilder.setNode(new NodeRef(NODES_IDENTIFIER.child(Node.class, new NodeKey(new NodeId(notification.getNodeId())))));
                inputBuilder.setObjId(new ObjId("ALL"));
                inputBuilder.setParamName("ALL");
                RpcResult<GetParamOutput> result = ocpConfigMgmtService.getParam(inputBuilder.build()).get();
                LOG.info("Get result {}", result);

                if (result.isSuccessful()) {
                    getParamOutput = result.getResult();
                    while (i < getParamOutput.getObj().size()) {
                        LOG.debug("Updating dataObject config: {}", getParamOutput.getObj().get(i));
                        resourceModelBroker.updateObj(getParamOutput.getObj().get(i).getParam(), getParamOutput.getObj().get(i).getId().getValue().toString());
                        i++;
                    }

                    try {
                        Thread.sleep(100);
                    }
                    catch (InterruptedException e) {}

                    // notify the system that the OCP alignment procedure is done
                    AlignmentCompletedBuilder builder = new AlignmentCompletedBuilder();
                    builder.setNodeId(notification.getNodeId());
                    builder.setReIpAddr(notification.getReIpAddr());
                    notificationProvider.publish(builder.build());

                } else {
                    LOG.error("Alignment failed (SB returned error) on RE({})", notification.getNodeId());
                }
            } catch (Exception exc3) {
                LOG.error("Alignment failed {}", exc3);
            }
            return "DONE";
        });

        connectedExecutor.shutdown();
    }

    @Override
    public void onDeviceDisconnected(DeviceDisconnected notification) {
        LOG.info("Get DeviceDisconnected notification with notification {}", notification);
    }

    private String getSystemTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
        Date dt = new Date();
        String dts = sdf.format(dt);

        return dts;
    }
}
