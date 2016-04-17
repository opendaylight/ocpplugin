/*
 * Copyright (c) 2015 Foxconn Corporation and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.ocpplugin.applications.ocpservice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.concurrent.Future;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.lang.Thread;
import java.lang.InterruptedException;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.ocp.applications.ocp.service.rev150811.*;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.ocp.applications.ocp.service.rev150811.OcpServiceService;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.ocp.applications.ocp.resourcemodel.rev150811.resourcemodel.*;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.ocp.applications.ocp.resourcemodel.rev150811.resourcemodel.radiohead.*;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.ocp.applications.ocp.resourcemodel.rev150811.ResourceModel;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.ocp.applications.ocp.resourcemodel.rev150811.resourcemodel.radiohead.TxSigPathGSM.FreqBandInd;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.config.mgmt.rev150811.*;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.fault.mgmt.rev150811.*;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.device.mgmt.rev150811.*;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.object.state.mgmt.rev150811.*;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.object.lifecycle.rev150811.*;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.common.types.rev150811.OriRes;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.common.types.rev150811.modifyparaminput.obj.Param;
import org.opendaylight.yangtools.yang.common.RpcResult;
import org.opendaylight.yangtools.yang.common.RpcResultBuilder;
import org.opendaylight.controller.md.sal.common.api.data.DataReader;
import org.opendaylight.yangtools.yang.binding.DataObject;
import org.opendaylight.yangtools.yang.binding.InstanceIdentifier;
import org.opendaylight.controller.sal.binding.api.data.DataBrokerService;
import org.opendaylight.controller.md.sal.binding.api.DataBroker;
import org.opendaylight.controller.md.sal.common.api.data.LogicalDatastoreType;
import org.opendaylight.controller.md.sal.common.api.data.AsyncDataChangeEvent;
import org.opendaylight.controller.md.sal.binding.api.DataChangeListener;
import org.opendaylight.controller.sal.binding.api.RpcConsumerRegistry;
import org.opendaylight.controller.sal.binding.api.NotificationProviderService;
import com.google.common.base.Function;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.base.Preconditions;
import org.opendaylight.yangtools.yang.binding.NotificationListener;
import org.opendaylight.yangtools.yang.common.RpcError;
import java.util.*;
import java.text.SimpleDateFormat;
import org.opendaylight.ocpplugin.applications.ocpservice.ResourceModelBroker;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.common.types.rev150811.ModifyParamRes;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.common.types.rev150811.CreateObjRes;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.common.types.rev150811.DeleteObjRes;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.common.types.rev150811.CreateObjGlobRes;
import org.opendaylight.yang.gen.v1.urn.opendaylight.inventory.rev130819.nodes.NodeKey;
import org.opendaylight.yang.gen.v1.urn.opendaylight.inventory.rev130819.NodeRef;
import org.opendaylight.yang.gen.v1.urn.opendaylight.inventory.rev130819.NodeId;
import org.opendaylight.yang.gen.v1.urn.opendaylight.inventory.rev130819.Nodes;
import org.opendaylight.yang.gen.v1.urn.opendaylight.inventory.rev130819.nodes.Node;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.common.types.rev150811.ObjId;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.xsd.types.rev150811.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/*
 * @author Jason Yuan <jason.cw.yuan@foxconn.com>
 *
 */
public class OcpService implements OcpServiceService, DataChangeListener, SalDeviceMgmtListener, AutoCloseable {
    private static final Logger LOG = LoggerFactory.getLogger(OcpService.class);
    private DataBroker dataBroker;
    private NotificationProviderService notificationProvider;
    private final ExecutorService executor; 
    private final SalConfigMgmtService ocpConfigMgmtService;
    private final SalFaultMgmtService ocpFaultMgmtService;
    private final SalObjectStateMgmtService ocpObjectStateMgmtService;
    private final SalDeviceMgmtService ocpDeviceMgmtService;
    private final SalObjectLifecycleService ocpObjectLifecycleService;    
    private static final InstanceIdentifier<Nodes> NODES_IDENTIFIER = InstanceIdentifier.create(Nodes.class);
    
    public OcpService(final RpcConsumerRegistry rpcRegistry) {
        executor = Executors.newFixedThreadPool(1);
        LOG.info("OcpService() created");
        this.ocpConfigMgmtService = Preconditions.checkNotNull(rpcRegistry.getRpcService(SalConfigMgmtService.class),
        "RPC OcpConfigMgmtService not found.");
        this.ocpFaultMgmtService = Preconditions.checkNotNull(rpcRegistry.getRpcService(SalFaultMgmtService.class),
        "RPC OcpFaultMgmtService not found.");
        this.ocpObjectStateMgmtService = Preconditions.checkNotNull(rpcRegistry.getRpcService(SalObjectStateMgmtService.class),
        "RPC OcpObjectStateMgmtService not found.");
        this.ocpDeviceMgmtService = Preconditions.checkNotNull(rpcRegistry.getRpcService(SalDeviceMgmtService.class),
        "RPC OcpDeviceMgmtService not found.");
        this.ocpObjectLifecycleService = Preconditions.checkNotNull(rpcRegistry.getRpcService(SalObjectLifecycleService.class),
        "RPC OcpDeviceMgmtService not found.");        
    }
    public static final InstanceIdentifier<ResourceModel> RM_IID = InstanceIdentifier.builder(ResourceModel.class).build();

    public void setDataBroker(final DataBroker dataBroker) {
        this.dataBroker = dataBroker;
    }

    public void setNotificationProvider(final NotificationProviderService notificationProvider) {
        this.notificationProvider = notificationProvider;
    }

    @Override
    public void onDataChanged( final AsyncDataChangeEvent<InstanceIdentifier<?>, DataObject> change ) {
        DataObject dataObject = change.getUpdatedSubtree();
        if( dataObject instanceof ResourceModel )
        {
            ResourceModel remodel = (ResourceModel) dataObject;
            LOG.info("onDataChanged - new dataObject config: {}", dataObject);
        }

    }    
    
    public void close() throws ExecutionException, InterruptedException {
        // When we close this service we need to shutdown our executor!
        executor.shutdown();
    }
    
    @Override
    public Future<RpcResult<GetParamNbOutput>> getParamNb(GetParamNbInput input) {
        GetParamInputBuilder inputBuilder = new GetParamInputBuilder();  
        GetParamOutput getParamOutput;
        inputBuilder.setNode(new NodeRef(NODES_IDENTIFIER.child(Node.class, new NodeKey(new NodeId(input.getNodeId())))));
        inputBuilder.setObj(input.getObj());
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
        inputBuilder.setObj(input.getObj());
        ModifyParamNbOutputBuilder outputBuilder = new  ModifyParamNbOutputBuilder();            
        ResourceModelBroker resourceModelBroker = new ResourceModelBroker(input.getNodeId() ,dataBroker);
        int objKey;
        int i = 0, j = 0;

        LOG.info("Start doing modifyParamNb with input {}", input);                                   
        try {
            RpcResult<ModifyParamOutput> result = ocpConfigMgmtService.modifyParam(inputBuilder.build()).get();
            LOG.info("Get result {}", result);       
            if (result.isSuccessful()) {
                modifyParamOutput = result.getResult();              
                while (i < input.getObj().size()) {
                    List<org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.common.types.rev150811.getparamoutput.obj.Param> outputParams = 
                        new ArrayList<org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.common.types.rev150811.getparamoutput.obj.Param>();                  
                    org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.common.types.rev150811.getparamoutput.obj.ParamBuilder outputParamBuilder = 
                        new org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.common.types.rev150811.getparamoutput.obj.ParamBuilder();                                       
                    
                    j = 0;
                    while (j < input.getObj().get(i).getParam().size()) {
                        if (modifyParamOutput.getObj().get(i).getParam().get(j).getResult() != ModifyParamRes.forValue(0)) {
                            j++;
                            continue;                    
                        }
                        outputParamBuilder.setName(input.getObj().get(i).getParam().get(j).getName());
                        outputParamBuilder.setValue(input.getObj().get(i).getParam().get(j).getValue());   
                        outputParams.add(outputParamBuilder.build()); 
                        j++;
                    }

                    LOG.debug("Updating dataObject : {}", outputParams);   
                    resourceModelBroker.updateObj(outputParams, input.getObj().get(i).getId().getValue().toString());
                    i++;
                }                
                return  RpcResultBuilder.success(outputBuilder.setGlobResult(modifyParamOutput.getGlobResult()).setObj(modifyParamOutput.getObj()).build()).buildFuture();   
            } else {
                LOG.info("ModifyParamNb Failed(SB returns error) on RE({})", input.getNodeId());  
                return  RpcResultBuilder.<ModifyParamNbOutput>failed().withError(RpcError.ErrorType.APPLICATION,
                            "Failed to modify param.(SB returns error)").buildFuture();              
            }           
        } catch (Exception exc) {  
            LOG.info("ModifyParam Failed{}", exc);     
            return  RpcResultBuilder.<ModifyParamNbOutput>failed().withError(RpcError.ErrorType.APPLICATION,
                            "Failed to  modify param.(SB RPC error)").buildFuture();        
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
        inputBuilder.setObj(input.getObj());
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
        inputBuilder.setObj(input.getObj());
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
        inputBuilder.setObj(input.getObj());
        ModifyStateNbOutputBuilder outputBuilder = new ModifyStateNbOutputBuilder();        

        LOG.info("Start doing modifyStateNb with input {}", input);          
        try {
            RpcResult<ModifyStateOutput> result = ocpObjectStateMgmtService.modifyState(inputBuilder.build()).get();
            LOG.info("Get result {}", result);       
            if (result.isSuccessful()) {
                modifyStateOutput = result.getResult();
                return  RpcResultBuilder.success(outputBuilder.setResult(modifyStateOutput.getResult()).setObj(modifyStateOutput.getObj()).build()).buildFuture();   
            } else {
                LOG.info("modifyStateNb Failed(SB returns error) on RE({})", input.getNodeId());               
                return  RpcResultBuilder.<ModifyStateNbOutput>failed().withError(RpcError.ErrorType.APPLICATION,
                            "Failed to modify state.(SB returns error)").buildFuture();              
            }                 
        } catch (Exception exc) {  
            LOG.info("modifyStateNb Failed{}", exc);     
            return  RpcResultBuilder.<ModifyStateNbOutput>failed().withError(RpcError.ErrorType.APPLICATION,
                            "Failed to modify state.(SB rpc error)").buildFuture();        
        }        
    }

    @Override
    public Future<RpcResult<CreateObjNbOutput>> createObjNb(CreateObjNbInput input) {    
        CreateObjInputBuilder inputBuilder = new CreateObjInputBuilder();  
        CreateObjOutput createObjOutput;
        inputBuilder.setNode(new NodeRef(NODES_IDENTIFIER.child(Node.class, new NodeKey(new NodeId(input.getNodeId())))));
        inputBuilder.setObj(input.getObj());
        CreateObjNbOutputBuilder outputBuilder = new  CreateObjNbOutputBuilder();         
        ResourceModelBroker resourceModelBroker = new ResourceModelBroker(input.getNodeId() ,dataBroker);
        int i = 0, j = 0;

        LOG.info("Start doing createObjNb with input {}", input);             
        try {
            RpcResult<CreateObjOutput> result = ocpObjectLifecycleService.createObj(inputBuilder.build()).get();
            LOG.info("Get result {}", result);       
            if (result.isSuccessful()) {
                createObjOutput = result.getResult();            
                while ((createObjOutput.getGlobResult() == CreateObjGlobRes.forValue(0)) && (i < input.getObj().size())) {
                    List<org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.common.types.rev150811.getparamoutput.obj.Param> outputParams = 
                        new ArrayList<org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.common.types.rev150811.getparamoutput.obj.Param>();                  
                    org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.common.types.rev150811.getparamoutput.obj.ParamBuilder outputParamBuilder = 
                        new org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.common.types.rev150811.getparamoutput.obj.ParamBuilder();                                      
                    
                    j = 0;
                    try {
                        while (j < input.getObj().get(i).getParam().size()) {
                            if (createObjOutput.getObj().get(i).getParam().get(j).getResult() != CreateObjRes.forValue(0)){
                                j++;                             
                                continue;                      
                            }
                            outputParamBuilder.setName(input.getObj().get(i).getParam().get(j).getName());
                            outputParamBuilder.setValue(input.getObj().get(i).getParam().get(j).getValue());   
                            outputParams.add(outputParamBuilder.build());
                            j++;
                        }  
                    } catch (Exception exc) { 
                        LOG.debug("No param existing in RE {}", input.getNodeId());                        
                    }
                    LOG.debug("Creating dataObject : {}", outputParams);                     
                    resourceModelBroker.updateObj(outputParams, createObjOutput.getObj().get(i).getId().getValue().toString());
                    i++;
                }                
                return  RpcResultBuilder.success(outputBuilder.setGlobResult(createObjOutput.getGlobResult()).setObj(createObjOutput.getObj()).build()).buildFuture();   
            } else {
                LOG.info("CreateObjectNb Failed(SB returns error) on RE({})", input.getNodeId());  
                return  RpcResultBuilder.<CreateObjNbOutput>failed().withError(RpcError.ErrorType.APPLICATION,
                            "Failed to create param.(SB returns error)").buildFuture();              
            }           
        } catch  (Exception exc) {  
            LOG.info("CreateObject Failed{}", exc);     
            return  RpcResultBuilder.<CreateObjNbOutput>failed().withError(RpcError.ErrorType.APPLICATION,
                            "Failed to  create param.(SB RPC error)").buildFuture();        
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
        
        Future<String> future = connectedExecutor.submit(new Callable<String>() {
            @Override
            public String call() {
                GetParamInputBuilder inputBuilder = new GetParamInputBuilder();  
                GetParamOutput getParamOutput;
                SetTimeOutput setTimeOutput;                
                GetParamNbOutputBuilder outputBuilder = new  GetParamNbOutputBuilder();      
                ResourceModelBroker resourceModelBroker = new ResourceModelBroker(notification.getNodeId() ,dataBroker);
                int i = 0;
                SetTimeInputBuilder setInputBuilder = new SetTimeInputBuilder(); 
                LOG.info("Get deviceConnected notification with notification {}", notification);                  
                List<org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.common.types.rev150811.getparaminput.obj.Param> inputParam = 
                    new ArrayList<org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.common.types.rev150811.getparaminput.obj.Param>();
                
                org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.common.types.rev150811.getparaminput.obj.ParamBuilder inputParamBuilder = 
                    new org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.common.types.rev150811.getparaminput.obj.ParamBuilder();           

                List<org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.common.types.rev150811.getparaminput.Obj> inputObjs = 
                    new ArrayList<org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.common.types.rev150811.getparaminput.Obj>();
                
                org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.common.types.rev150811.getparaminput.ObjBuilder inputObjBuilder = 
                    new org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.common.types.rev150811.getparaminput.ObjBuilder();     
                    
                try {
                    resourceModelBroker.deleteObj("ALL");                  
                } catch (Exception exc) {
                        LOG.info("Failed to delete node:({}, {})", notification.getNodeId(), exc);                 
                }
                try {
                    setInputBuilder.setNode(new NodeRef(NODES_IDENTIFIER.child(Node.class, new NodeKey(new NodeId(notification.getNodeId())))));
                    setInputBuilder.setNewTime(new XsdDateTime(getSystemTime()));                    
                    LOG.info("Starting setTime({}) on {} in alignment", getSystemTime(),notification.getNodeId());                       
                    RpcResult<SetTimeOutput> timeresult = ocpDeviceMgmtService.setTime(setInputBuilder.build()).get();
                    LOG.info("Get timeresult {}", timeresult);       
                    if (timeresult.isSuccessful()) {
                        LOG.info("SetTimeNb done on ({})", notification.getNodeId()); 
                    } else {
                        LOG.info("SetTimeNb Failed when doing alignment (SB returns error) on ({})", notification.getNodeId());            
                    }                     
                } catch (Exception exc) {
                    LOG.info("Failed to setTime on {} in alignment {}", notification.getNodeId(), exc);  
                }
                
                try {        
                    inputParam.add(inputParamBuilder.setName("ALL").build());
                    inputObjs.add(inputObjBuilder.setId(new ObjId("ALL")).setParam(inputParam).build());                      
                    inputBuilder.setNode(new NodeRef(NODES_IDENTIFIER.child(Node.class, new NodeKey(new NodeId(notification.getNodeId())))));
                    inputBuilder.setObj(inputObjs);                

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
                        LOG.info("Alignment Failed(SB returns error) on RE({})", notification.getNodeId());  
                    }           
                } catch (Exception exc) {  
                    LOG.info("Alignment Failed{}", exc); 
                }             
                return "DONE";
            }
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
