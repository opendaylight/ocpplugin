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
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.ocp.applications.ocp.service.rev150811.*;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.ocp.applications.ocp.service.rev150811.OcpServiceService;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.ocp.applications.ocp.resourcemodel.rev150811.resourcemodel.*;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.ocp.applications.ocp.resourcemodel.rev150811.resourcemodel.radiohead.*;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.ocp.applications.ocp.resourcemodel.rev150811.resourcemodel.radiohead.aisgport.*;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.ocp.applications.ocp.resourcemodel.rev150811.ResourceModel;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.xsd.types.rev150811.*;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.config.mgmt.rev150811.*;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.fault.mgmt.rev150811.*;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.device.mgmt.rev150811.*;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.object.state.mgmt.rev150811.*;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.common.types.rev150811.OriRes;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.common.types.rev150811.getparamoutput.obj.Param;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.common.types.rev150811.ModifyParamRes;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.common.types.rev150811.ObjId;
import org.opendaylight.controller.md.sal.common.api.data.DataReader;
import org.opendaylight.yangtools.yang.binding.DataObject;
import org.opendaylight.yangtools.yang.binding.InstanceIdentifier;
import org.opendaylight.controller.sal.binding.api.data.DataBrokerService;
import org.opendaylight.controller.md.sal.binding.api.DataBroker;
import org.opendaylight.controller.md.sal.binding.api.ReadWriteTransaction;
import org.opendaylight.controller.md.sal.common.api.data.LogicalDatastoreType;
import java.util.*;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.ocp.applications.ocp.resourcemodel.rev150811.resourcemodel.radiohead.TxSigPathGSM.FreqBandInd;
import org.opendaylight.yang.gen.v1.urn.opendaylight.inventory.rev130819.nodes.NodeKey;
import org.opendaylight.yang.gen.v1.urn.opendaylight.inventory.rev130819.NodeRef;
import org.opendaylight.yang.gen.v1.urn.opendaylight.inventory.rev130819.NodeId;
import org.opendaylight.yang.gen.v1.urn.opendaylight.inventory.rev130819.Nodes;
import org.opendaylight.yang.gen.v1.urn.opendaylight.inventory.rev130819.nodes.Node;
import org.opendaylight.yang.gen.v1.urn.ietf.params.xml.ns.yang.ietf.inet.types.rev100924.Ipv4Address;
import org.opendaylight.yang.gen.v1.urn.ietf.params.xml.ns.yang.ietf.yang.types.rev100924.MacAddress;
import java.math.BigDecimal;

/*
 * @author Jason Yuan <jason.cw.yuan@foxconn.com>
 *
 */
public class ResourceModelBroker5G {
    private DataBroker dataBroker;
    private InstanceIdentifier<RadioHead> rootPath;
    
    public ResourceModelBroker5G(InstanceIdentifier<RadioHead> path,final DataBroker salservice) {
        this.dataBroker = salservice;
        this.rootPath = path;            
    }
    
    private static final Logger LOG = LoggerFactory.getLogger(ResourceModelBroker5G.class);
    public void deleteObj (String objId) {
        int i = 0;       
        ArrayList<Long> instanceNumbers = new ArrayList<Long>();   
        ArrayList<String> objTypes = new ArrayList<String>();         
        
        parseObjId(objTypes, instanceNumbers, objId);     
        ReadWriteTransaction readWriteTransaction = dataBroker.newReadWriteTransaction(); 
        try {        
            LOG.debug("Deleting objId Name: {}", objTypes.get((objTypes.size())-1));  
            switch (objTypes.get((objTypes.size())-1)) {  
                case "RxSigPath_5G":           
                        readWriteTransaction.delete(LogicalDatastoreType.OPERATIONAL,
                            rootPath.child(RxSigPath5G.class, new RxSigPath5GKey(new XsdUnsignedInt(instanceNumbers.get(instanceNumbers.size()-1)))));   
                        readWriteTransaction.submit();                                                        
                    break;
                case "TxSigPath_5G":           
                        readWriteTransaction.delete(LogicalDatastoreType.OPERATIONAL,
                            rootPath.child(TxSigPath5G.class, new TxSigPath5GKey(new XsdUnsignedInt(instanceNumbers.get(instanceNumbers.size()-1)))));   
                        readWriteTransaction.submit();                                                        
                    break;
                case "dataLink":           
                        readWriteTransaction.delete(LogicalDatastoreType.OPERATIONAL,
                            rootPath.child(DataLink.class, new DataLinkKey(new XsdUnsignedInt(instanceNumbers.get(instanceNumbers.size()-1)))));   
                        readWriteTransaction.submit();                                                        
                    break;
                case "ethLink":           
                        readWriteTransaction.delete(LogicalDatastoreType.OPERATIONAL,
                            rootPath.child(EthLink.class, new EthLinkKey(new XsdUnsignedInt(instanceNumbers.get(instanceNumbers.size()-1)))));   
                        readWriteTransaction.submit();                                                        
                    break;                    
                default:
                    LOG.debug("ObjId = {} does not exist in Re", objTypes.get((objTypes.size())-1) );                   
            }
        } catch (Exception exc) {
            LOG.error("Data delete failed, at {}, Error: {}", objTypes, exc);
        }
        
    }             
    
    public void updateObj (List<Param> params, String objId) {
        int i = 0;

        ArrayList<Long> instanceNumbers = new ArrayList<Long>();   
        ArrayList<String> objTypes = new ArrayList<String>();         
        
        parseObjId(objTypes, instanceNumbers, objId);
        
        ReadWriteTransaction readWriteTransaction = dataBroker.newReadWriteTransaction(); 
        
        switch (objTypes.get((objTypes.size())-1)) {  
            case "TxSigPath_5G":  
                try {
                    TxSigPath5GBuilder txSigPath5G = new TxSigPath5GBuilder();  
                    txSigPath5G.setKey(new TxSigPath5GKey(new XsdUnsignedInt(instanceNumbers.get(instanceNumbers.size()-1))));                     
                    i = 0;
                    while (i < params.size()) {
                        LOG.debug("Processing param Name: {} in TxSigPath5G", params.get(i).getName());                    
                        switch (params.get(i).getName()) {
                            case "flowID":
                                txSigPath5G.setFlowID(params.get(i).getValue());
                                break;    
                            case "protocol":
                                org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.ocp.applications.ocp.resourcemodel.rev150811.resourcemodel.radiohead.TxSigPath5G.Protocol protocol = 
                                org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.ocp.applications.ocp.resourcemodel.rev150811.resourcemodel.radiohead.TxSigPath5G.Protocol.valueOf(params.get(i).getValue());
                                txSigPath5G.setProtocol(protocol);
                                break;
                            case "direction":
                                org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.ocp.applications.ocp.resourcemodel.rev150811.resourcemodel.radiohead.TxSigPath5G.Direction direction = 
                                org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.ocp.applications.ocp.resourcemodel.rev150811.resourcemodel.radiohead.TxSigPath5G.Direction.valueOf(params.get(i).getValue());
                                txSigPath5G.setDirection(direction);
                                break;                                
                            case "address":
                                txSigPath5G.setAddress(new Ipv4Address(params.get(i).getValue()));  
                                break;
                            case "bbuPort":
                                txSigPath5G.setBbuPort(new XsdUnsignedShort(Integer.valueOf(params.get(i).getValue())));
                                break;
                            case "rrhPort":
                                txSigPath5G.setRrhPort(new XsdUnsignedShort(Integer.valueOf(params.get(i).getValue())));                          
                                break;                                
                            case "dataLink":
                                txSigPath5G.setDataLink(new ObjId(params.get(i).getValue()));
                                break;
                            case "antPort":
                                txSigPath5G.setAntPort(new ObjId(params.get(i).getValue()));
                                break; 
                            case "resourceAllocationNominator":
                                txSigPath5G.setResourceAllocationNominator(new XsdUnsignedShort(Integer.valueOf(params.get(i).getValue())));
                                break;
                            case "firstResource":
                                txSigPath5G.setFirstResource(new XsdUnsignedShort(Integer.valueOf(params.get(i).getValue())));                          
                                break; 
                            case "centerFrequency":
                                txSigPath5G.setCenterFrequency(new BigDecimal(params.get(i).getValue()));
                                break;
                            case "bandwidth":
                                txSigPath5G.setBandwidth(new BigDecimal(params.get(i).getValue()));                          
                                break;                                 
                            default:
                                LOG.debug("ObjId = {} does not exist in txSigPath5G", params.get(i).getName());                                   
                        }
                        i++;
                    } 
                    readWriteTransaction.merge(LogicalDatastoreType.OPERATIONAL,
                        rootPath.child(TxSigPath5G.class, new TxSigPath5GKey(new XsdUnsignedInt(instanceNumbers.get(instanceNumbers.size()-1)))), txSigPath5G.build(), true);   
                    readWriteTransaction.submit();                                   
                }catch (Exception exc) {
                    LOG.info("Data update Failed{}", exc);                 
                }                        
                break;   
            case "RxSigPath_5G":  
                try {
                    RxSigPath5GBuilder rxSigPath5G = new RxSigPath5GBuilder();  
                    rxSigPath5G.setKey(new RxSigPath5GKey(new XsdUnsignedInt(instanceNumbers.get(instanceNumbers.size()-1))));                     
                    i = 0;
                    while (i < params.size()) {
                        LOG.debug("Processing param Name: {} in rxSigPath5G", params.get(i).getName());                    
                        switch (params.get(i).getName()) {
                            case "flowID":
                                rxSigPath5G.setFlowID(params.get(i).getValue());
                                break;    
                            case "protocol":
                                org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.ocp.applications.ocp.resourcemodel.rev150811.resourcemodel.radiohead.RxSigPath5G.Protocol protocol = 
                                org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.ocp.applications.ocp.resourcemodel.rev150811.resourcemodel.radiohead.RxSigPath5G.Protocol.valueOf(params.get(i).getValue());
                                rxSigPath5G.setProtocol(protocol);
                                break;
                            case "direction":
                                org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.ocp.applications.ocp.resourcemodel.rev150811.resourcemodel.radiohead.RxSigPath5G.Direction direction = 
                                org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.ocp.applications.ocp.resourcemodel.rev150811.resourcemodel.radiohead.RxSigPath5G.Direction.valueOf(params.get(i).getValue());
                                rxSigPath5G.setDirection(direction);
                                break;                                
                            case "address":
                                rxSigPath5G.setAddress(new Ipv4Address(params.get(i).getValue()));  
                                break;
                            case "bbuPort":
                                rxSigPath5G.setBbuPort(new XsdUnsignedShort(Integer.valueOf(params.get(i).getValue())));
                                break;
                            case "rrhPort":
                                rxSigPath5G.setRrhPort(new XsdUnsignedShort(Integer.valueOf(params.get(i).getValue())));                          
                                break;                                
                            case "dataLink":
                                rxSigPath5G.setDataLink(new ObjId(params.get(i).getValue()));
                                break;
                            case "antPort":
                                rxSigPath5G.setAntPort(new ObjId(params.get(i).getValue()));
                                break; 
                            case "resourceAllocationNominator":
                                rxSigPath5G.setResourceAllocationNominator(new XsdUnsignedShort(Integer.valueOf(params.get(i).getValue())));
                                break;
                            case "firstResource":
                                rxSigPath5G.setFirstResource(new XsdUnsignedShort(Integer.valueOf(params.get(i).getValue())));                          
                                break; 
                            case "centerFrequency":
                                rxSigPath5G.setCenterFrequency(new BigDecimal(params.get(i).getValue()));
                                break;
                            case "bandwidth":
                                rxSigPath5G.setBandwidth(new BigDecimal(params.get(i).getValue()));                          
                                break;                                 
                            default:
                                LOG.debug("ObjId = {} does not exist in rxSigPath5G", params.get(i).getName());                                   
                        }
                        i++;
                    } 
                    readWriteTransaction.merge(LogicalDatastoreType.OPERATIONAL,
                        rootPath.child(RxSigPath5G.class, new RxSigPath5GKey(new XsdUnsignedInt(instanceNumbers.get(instanceNumbers.size()-1)))), rxSigPath5G.build(), true);   
                    readWriteTransaction.submit();                                   
                }catch (Exception exc) {
                    LOG.info("Data update Failed{}", exc);                 
                }                        
                break;     
            case "dataLink":  
                try {
                    DataLinkBuilder dataLink = new DataLinkBuilder();  
                    dataLink.setKey(new DataLinkKey(new XsdUnsignedInt(instanceNumbers.get(instanceNumbers.size()-1))));                     
                    i = 0;
                    while (i < params.size()) {
                        LOG.debug("Processing param Name: {} in DataLink", params.get(i).getName());                    
                        switch (params.get(i).getName()) {
                            case "portLabel":
                                dataLink.setPortLabel(params.get(i).getValue());
                                break;    
                            case "type":
                                org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.ocp.applications.ocp.resourcemodel.rev150811.resourcemodel.radiohead.DataLink.Type type = 
                                org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.ocp.applications.ocp.resourcemodel.rev150811.resourcemodel.radiohead.DataLink.Type.valueOf(params.get(i).getValue());
                                dataLink.setType(type);
                                break;                                                         
                            case "reference":
                                dataLink.setReference(new ObjId(params.get(i).getValue()));
                                break;                              
                            default:
                                LOG.debug("ObjId = {} does not exist in DataLink", params.get(i).getName());                                   
                        }
                        i++;
                    } 
                    readWriteTransaction.merge(LogicalDatastoreType.OPERATIONAL,
                        rootPath.child(DataLink.class, new DataLinkKey(new XsdUnsignedInt(instanceNumbers.get(instanceNumbers.size()-1)))), dataLink.build(), true);   
                    readWriteTransaction.submit();                                   
                }catch (Exception exc) {
                    LOG.info("Data update Failed{}", exc);                 
                }                        
                break;  
            case "ethLink":  
                try {
                    EthLinkBuilder ethLink = new EthLinkBuilder();  
                    ethLink.setKey(new EthLinkKey(new XsdUnsignedInt(instanceNumbers.get(instanceNumbers.size()-1))));                     
                    i = 0;
                    while (i < params.size()) {
                        LOG.debug("Processing param Name: {} in ethLink", params.get(i).getName());                    
                        switch (params.get(i).getName()) {
                            case "portLabel":
                                ethLink.setPortLabel(params.get(i).getValue());
                                break;    
                            case "portRoleCapability":
                                org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.ocp.applications.ocp.resourcemodel.rev150811.resourcemodel.radiohead.EthLink.PortRoleCapability portRoleCapability = 
                                org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.ocp.applications.ocp.resourcemodel.rev150811.resourcemodel.radiohead.EthLink.PortRoleCapability.valueOf(params.get(i).getValue());
                                ethLink.setPortRoleCapability(portRoleCapability);
                                break;                                                         
                            case "portRole":
                                org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.ocp.applications.ocp.resourcemodel.rev150811.resourcemodel.radiohead.EthLink.PortRole portRole = 
                                org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.ocp.applications.ocp.resourcemodel.rev150811.resourcemodel.radiohead.EthLink.PortRole.valueOf(params.get(i).getValue());
                                ethLink.setPortRole(portRole);
                                break;      
                            case "localAddress":
                                ethLink.setLocalAddress(new MacAddress(params.get(i).getValue()));  
                                break;                                
                            default:
                                LOG.debug("ObjId = {} does not exist in ethLink", params.get(i).getName());                                   
                        }
                        i++;
                    } 
                    readWriteTransaction.merge(LogicalDatastoreType.OPERATIONAL,
                        rootPath.child(EthLink.class, new EthLinkKey(new XsdUnsignedInt(instanceNumbers.get(instanceNumbers.size()-1)))), ethLink.build(), true);   
                    readWriteTransaction.submit();                                   
                }catch (Exception exc) {
                    LOG.info("Data update Failed{}", exc);                 
                }                        
                break;                  
            default:
                    LOG.info("ObjId {} does not exist", objTypes.get((objTypes.size())-1) );        
                             
        }             
    }
        
        
    private void parseObjId(List<String> objTypes, List<Long> instanceNumbers, String objId) {    
        String[] objIdTokens = objId.split("/");       
        for (String token : objIdTokens) {
            String[] tokenArray = token.split(":");       
            try{
                objTypes.add(tokenArray[0]);
                instanceNumbers.add(Long.valueOf(tokenArray[1]));
            } catch (Exception exc) {
                LOG.error("ObjId parse failed {}, {}", exc, objId);                
            }
        }
    
    
    }
}
