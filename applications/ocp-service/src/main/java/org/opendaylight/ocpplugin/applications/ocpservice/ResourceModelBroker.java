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
import java.math.*;

/*
 * @author Jason Yuan <jason.cw.yuan@foxconn.com>
 *
 */
public class ResourceModelBroker {
    private DataBroker dataBroker;
    private InstanceIdentifier<RadioHead> rootPath;
    
    public ResourceModelBroker(final NodeId nodeId,final DataBroker salservice) {
        this.dataBroker = salservice;
        this.rootPath = InstanceIdentifier.create(ResourceModel.class).child(RadioHead.class, new RadioHeadKey(nodeId)); 
      
    }
    
    private static final Logger LOG = LoggerFactory.getLogger(ResourceModelBroker.class);
    
    public void deleteObj (String objId) {
        int i = 0;       
        ArrayList<Long> instanceNumbers = new ArrayList<Long>();   
        ArrayList<String> objTypes = new ArrayList<String>();         
        
        parseObjId(objTypes, instanceNumbers, objId);     
        ReadWriteTransaction readWriteTransaction = dataBroker.newReadWriteTransaction(); 
        try {        
            LOG.debug("Deleting objId Name: {}", objTypes.get((objTypes.size())-1));  
            switch (objTypes.get((objTypes.size())-1)) {  
                case "antPort":  
                    readWriteTransaction.delete(LogicalDatastoreType.OPERATIONAL, rootPath.child(AntPort.class, new AntPortKey(new XsdUnsignedInt(instanceNumbers.get(instanceNumbers.size()-1)))));   
                    readWriteTransaction.submit();
                    break;
                case "TxSigPath_UTRAFDD":  
                    readWriteTransaction.delete(LogicalDatastoreType.OPERATIONAL, rootPath.child(TxSigPathUTRAFDD.class,
                                                new TxSigPathUTRAFDDKey(new XsdUnsignedInt(instanceNumbers.get(instanceNumbers.size()-1)))));   
                    readWriteTransaction.submit();                    
                    break;                
                case "TxSigPath_EUTRAFDD":  
                    readWriteTransaction.delete(LogicalDatastoreType.OPERATIONAL,
                        rootPath.child(TxSigPathEUTRAFDD.class, new TxSigPathEUTRAFDDKey(new XsdUnsignedInt(instanceNumbers.get(instanceNumbers.size()-1)))));   
                    readWriteTransaction.submit();                                                        
                break;                
                case "TxSigPath_EUTRATDD":                  
                    readWriteTransaction.delete(LogicalDatastoreType.OPERATIONAL,
                        rootPath.child(TxSigPathEUTRATDD.class, new TxSigPathEUTRATDDKey(new XsdUnsignedInt(instanceNumbers.get(instanceNumbers.size()-1)))));   
                    readWriteTransaction.submit();                                                                                       
                    break;
                case "TxSigPath_GSM":                  
                    readWriteTransaction.delete(LogicalDatastoreType.OPERATIONAL,
                        rootPath.child(TxSigPathGSM.class, new TxSigPathGSMKey(new XsdUnsignedInt(instanceNumbers.get(instanceNumbers.size()-1)))));   
                    readWriteTransaction.submit();                                          
                    break;
                case "RxSigPath_UTRAFDD":                
                    readWriteTransaction.delete(LogicalDatastoreType.OPERATIONAL,
                        rootPath.child(RxSigPathUTRAFDD.class, new RxSigPathUTRAFDDKey(new XsdUnsignedInt(instanceNumbers.get(instanceNumbers.size()-1)))));   
                    readWriteTransaction.submit();                                    
                    break;
                case "RxSigPath_EUTRAFDD":  
                    readWriteTransaction.delete(LogicalDatastoreType.OPERATIONAL,
                        rootPath.child(RxSigPathEUTRAFDD.class, new RxSigPathEUTRAFDDKey(new XsdUnsignedInt(instanceNumbers.get(instanceNumbers.size()-1)))));   
                    readWriteTransaction.submit();                                         
                    break;
                case "RxSigPath_EUTRATDD":               
                    readWriteTransaction.delete(LogicalDatastoreType.OPERATIONAL,
                        rootPath.child(RxSigPathEUTRATDD.class, new RxSigPathEUTRATDDKey(new XsdUnsignedInt(instanceNumbers.get(instanceNumbers.size()-1)))));   
                    readWriteTransaction.submit();                                    
                    break;
                case "RxSigPath_GSM":              
                    readWriteTransaction.delete(LogicalDatastoreType.OPERATIONAL,
                        rootPath.child(RxSigPathGSM.class, new RxSigPathGSMKey(new XsdUnsignedInt(instanceNumbers.get(instanceNumbers.size()-1)))));   
                    readWriteTransaction.submit();                                    
                    break;
                case "aisgPort":           
                        readWriteTransaction.delete(LogicalDatastoreType.OPERATIONAL,
                            rootPath.child(AisgPort.class, new AisgPortKey(new XsdUnsignedInt(instanceNumbers.get(instanceNumbers.size()-1)))));   
                        readWriteTransaction.submit();                                                        
                    break;
                case "aisgALD":               
                    InstanceIdentifier<AisgPort> aisgPath = rootPath.child(AisgPort.class, new AisgPortKey(new XsdUnsignedInt(instanceNumbers.get(instanceNumbers.size())-2)));
                    readWriteTransaction.delete(LogicalDatastoreType.OPERATIONAL,
                        aisgPath.child(AisgALD.class, new AisgALDKey(new XsdUnsignedInt(instanceNumbers.get(instanceNumbers.size()-1)))));   
                    readWriteTransaction.submit();                                     
                    break;   
                case "ALL":              
                    try {
                        readWriteTransaction.delete(LogicalDatastoreType.OPERATIONAL, rootPath);   
                        readWriteTransaction.submit(); 
                    } catch (Exception exc) {
                        LOG.debug("Node delte Failed{}", rootPath);                       
                    }
                    break;                       
                default:
                    ;
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
            case "RE":  
                try {
                    ReBuilder re = new ReBuilder();
                    i = 0;
                    while (i < params.size()) {
                        LOG.debug("Processing param Name: {} in Re", params.get(i).getName());    
                        switch (params.get(i).getName()) {
                            case "vendorID":
                                re.setVendorID(params.get(i).getValue());
                                break;
                            case "productID":
                                re.setProductID(params.get(i).getValue());
                                break;                                
                            case "productRev":
                                re.setProductRev(params.get(i).getValue());
                                break;
                            case "serialNumber":
                                re.setSerialNumber(params.get(i).getValue());
                                break;                                
                            case "protocolVer":
                                re.setProtocolVer(params.get(i).getValue());
                                break;                                
                            case "agcTargetLevCfgGran":
                                org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.ocp.applications.ocp.resourcemodel.rev150811.resourcemodel.radiohead.Re.AgcTargetLevCfgGran agcTargetLevCfgGran = 
                                org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.ocp.applications.ocp.resourcemodel.rev150811.resourcemodel.radiohead.Re.AgcTargetLevCfgGran.valueOf(params.get(i).getValue());
                                re.setAgcTargetLevCfgGran(agcTargetLevCfgGran);
                                break;     
                            case "agcSettlTimeCfgGran":
                                org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.ocp.applications.ocp.resourcemodel.rev150811.resourcemodel.radiohead.Re.AgcSettlTimeCfgGran agcSettlTimeCfgGran = 
                                org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.ocp.applications.ocp.resourcemodel.rev150811.resourcemodel.radiohead.Re.AgcSettlTimeCfgGran.valueOf(params.get(i).getValue());
                                re.setAgcSettlTimeCfgGran(agcSettlTimeCfgGran);
                                break; 
                            case "agcSettlTimeCap":
                                re.setAgcSettlTimeCap(params.get(i).getValue());
                                break;   
                            case "numAntPort":
                                re.setNumAntPort(new XsdUnsignedShort(Integer.valueOf(params.get(i).getValue())));
                                break;  
                            case "numDataLinks":
                                re.setNumDataLinks(new XsdUnsignedShort(Integer.valueOf(params.get(i).getValue())));
                                break;  
                            case "numSigPathPerAntenna":
                                re.setNumSigPathPerAntenna(new XsdUnsignedShort(Integer.valueOf(params.get(i).getValue())));
                                break;  
                            case "resourceAllocationDenominator":
                                re.setResourceAllocationDenominator(new XsdUnsignedShort(Integer.valueOf(params.get(i).getValue())));
                                break;
                            case "resourceAllcationDenominator":
                                re.setResourceAllocationDenominator(new XsdUnsignedShort(Integer.valueOf(params.get(i).getValue())));
                                break;                                  
                            default:
                                LOG.debug("ObjId = {} does not exist in Re", params.get(i).getName());                                    
                        }
                        i++;
                    }      
                    readWriteTransaction.merge(LogicalDatastoreType.OPERATIONAL, rootPath.child(Re.class), re.build(), true);   
                    readWriteTransaction.submit();
                } catch (Exception exc) {
                    LOG.info("Data update Failed{}", exc);                       
                }
                break;        
            case "antPort":  
                try {
                    AntPortBuilder antPort = new AntPortBuilder();
                    i = 0;
                    while (i < params.size()) {
                        LOG.debug("Processing param Name: {} in antPort", params.get(i).getName());    
                        switch (params.get(i).getName()) {
                            case "portLabel":
                                antPort.setPortLabel(params.get(i).getValue());
                                break; 
                            case "topology":
                                org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.ocp.applications.ocp.resourcemodel.rev150811.resourcemodel.radiohead.AntPort.Topology topology = 
                                org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.ocp.applications.ocp.resourcemodel.rev150811.resourcemodel.radiohead.AntPort.Topology.valueOf(params.get(i).getValue());                               
                                antPort.setTopology(topology);          
                                break;                                
                            case "direction":
                                antPort.setDirection(new XsdUnsignedShort(Integer.valueOf(params.get(i).getValue())));
                                break;  
                            case "angle":
                                antPort.setAngle(new XsdUnsignedShort(Integer.valueOf(params.get(i).getValue())));
                                break;                                  
                            default:
                                LOG.debug("ObjId = {} does not exist in antPort", params.get(i).getName());                                    
                        }                        
                        i++;
                    }                     
                    antPort.setKey(new AntPortKey(new AntPortKey(new XsdUnsignedInt(instanceNumbers.get(instanceNumbers.size()-1)))));
                    readWriteTransaction.merge(LogicalDatastoreType.OPERATIONAL, rootPath.child(AntPort.class, new AntPortKey(new XsdUnsignedInt(instanceNumbers.get(instanceNumbers.size()-1)))), antPort.build(), true);   
                    readWriteTransaction.submit();
                } catch (Exception exc) {
                    LOG.info("Data update Failed{}", exc);                       
                }
                break;
            case "TxSigPath_UTRAFDD":  
                try {
                    TxSigPathUTRAFDDBuilder txSigPathUTRAFDD = new TxSigPathUTRAFDDBuilder(); 
                    txSigPathUTRAFDD.setKey(new TxSigPathUTRAFDDKey(new XsdUnsignedInt(instanceNumbers.get(instanceNumbers.size()-1))));                                   
                    i = 0;
                    while (i < params.size()) {
                        LOG.debug("In TxSigPath_UTRAFDD, Get param Name: {}", params.get(i).getName());                        
                        switch (params.get(i).getName()) {
                            case "maxTxPwr":
                                txSigPathUTRAFDD.setMaxTxPwr(new XsdUnsignedShort(Integer.valueOf(params.get(i).getValue())));
                                break;
                            case "dlCalRFMax":
                                txSigPathUTRAFDD.setDlCalRFMax(new XsdUnsignedShort(Integer.valueOf(params.get(i).getValue())));
                                break;                                
                            case "t2a":
                                txSigPathUTRAFDD.setT2a(new XsdUnsignedInt(Long.valueOf(params.get(i).getValue())));
                                break;
                            case "dlCalRF":
                                txSigPathUTRAFDD.setDlCalRF(new XsdUnsignedShort(Integer.valueOf(params.get(i).getValue())));
                                break;                                
                            case "antPort":
                                txSigPathUTRAFDD.setAntPort(new ObjId(params.get(i).getValue()));
                                break;                                
                            case "uarfcn":
                                txSigPathUTRAFDD.setUarfcn(new XsdUnsignedShort(Integer.valueOf(params.get(i).getValue())));
                                break;
                            default:
                                LOG.debug("ObjId = {} does not exist in Re", params.get(i).getName());                                   
                        }
                        i++;
                    } 
                    readWriteTransaction.merge(LogicalDatastoreType.OPERATIONAL, rootPath.child(TxSigPathUTRAFDD.class,
                                                new TxSigPathUTRAFDDKey(new XsdUnsignedInt(instanceNumbers.get(instanceNumbers.size()-1)))), txSigPathUTRAFDD.build(), true);   
                    readWriteTransaction.submit();                    
                }catch (Exception exc) {
                    LOG.info("Data update Failed{}", exc);  
                }
                break;                
            case "TxSigPath_EUTRAFDD":  
                try {
                    TxSigPathEUTRAFDDBuilder txSigPathEUTRAFDD = new TxSigPathEUTRAFDDBuilder(); 
                    txSigPathEUTRAFDD.setKey(new TxSigPathEUTRAFDDKey(new XsdUnsignedInt(instanceNumbers.get(instanceNumbers.size()-1))));                     
                    i = 0;
                    while (i < params.size()) {
                        LOG.debug("Processing param Name: {} in TxSigPath_EUTRAFDD", params.get(i).getName());                        
                        switch (params.get(i).getName()) {
                            case "chanBW":
                                txSigPathEUTRAFDD.setChanBW(new XsdUnsignedShort(Integer.valueOf(params.get(i).getValue())));
                                break;
                            case "maxTxPwr":
                                txSigPathEUTRAFDD.setMaxTxPwr(new XsdUnsignedShort(Integer.valueOf(params.get(i).getValue())));
                                break;
                            case "earfcn":
                                txSigPathEUTRAFDD.setEarfcn(new XsdUnsignedShort(Integer.valueOf(params.get(i).getValue())));  
                                break;                                
                            case "dlCalREMax":
                                txSigPathEUTRAFDD.setDlCalREMax(new XsdUnsignedShort(Integer.valueOf(params.get(i).getValue())));
                                break;
                            case "t2a":
                                txSigPathEUTRAFDD.setT2a(new XsdUnsignedInt(Long.valueOf(params.get(i).getValue())));
                                break;
                            case "dlCalRE":
                                txSigPathEUTRAFDD.setDlCalRE(new XsdUnsignedShort(Integer.valueOf(params.get(i).getValue()))); 
                                break;
                            case "axcW":
                                txSigPathEUTRAFDD.setAxcW(new XsdUnsignedByte(Short.valueOf(params.get(i).getValue())));
                                break;
                            case "axcB":
                                txSigPathEUTRAFDD.setAxcB(new XsdUnsignedByte(Short.valueOf(params.get(i).getValue())));
                                break;
                            case "oriLink":
                                txSigPathEUTRAFDD.setOriLink(new ObjId(params.get(i).getValue())); 
                                break;
                            case "sigmaIQ":
                                txSigPathEUTRAFDD.setSigmaIQ(new XsdUnsignedShort(Integer.valueOf(params.get(i).getValue())));
                                break;
                            case "antPort":
                                txSigPathEUTRAFDD.setAntPort(new ObjId(params.get(i).getValue()));
                                break;
                            case "enableIQDLComp":
                                txSigPathEUTRAFDD.setEnableIQDLComp(Boolean.valueOf(params.get(i).getValue()));      
                                break;             
                            default:
                                LOG.debug("ObjId = {} does not exist in Re", params.get(i).getName());                                   
                        }
                        i++;
                    } 
                    readWriteTransaction.merge(LogicalDatastoreType.OPERATIONAL,
                        rootPath.child(TxSigPathEUTRAFDD.class, new TxSigPathEUTRAFDDKey(new XsdUnsignedInt(instanceNumbers.get(instanceNumbers.size()-1)))), txSigPathEUTRAFDD.build(), true);   
                    readWriteTransaction.submit();                                     
                }catch (Exception exc) {
                    LOG.info("Data update Failed{}", exc);                  
                }                      
            break;                
            case "TxSigPath_EUTRATDD":  
                try {
                    TxSigPathEUTRATDDBuilder txSigPathEUTRATDD = new TxSigPathEUTRATDDBuilder();  
                    txSigPathEUTRATDD.setKey(new TxSigPathEUTRATDDKey(new XsdUnsignedInt(instanceNumbers.get(instanceNumbers.size()-1))));                     
                    i = 0;
                    while (i < params.size()) {
                        LOG.debug("Processing param Name: {} in TxSigPath_EUTRATDD", params.get(i).getName());                      
                        switch (params.get(i).getName()) {
                            case "chanBW":
                                txSigPathEUTRATDD.setChanBW(new XsdUnsignedShort(Integer.valueOf(params.get(i).getValue())));
                                break;
                            case "maxTxPwr":
                                txSigPathEUTRATDD.setMaxTxPwr(new XsdUnsignedShort(Integer.valueOf(params.get(i).getValue())));
                                break;
                            case "tddULDLConfig":
                                txSigPathEUTRATDD.setTddULDLConfig(new XsdUnsignedByte(Short.valueOf(params.get(i).getValue())));     
                                break;
                            case "tddSpecialSFConfig":                    
                                txSigPathEUTRATDD.setTddSpecialSFConfig(new XsdUnsignedByte(Short.valueOf(params.get(i).getValue())));
                                break;
                            case "tddCPLengthDL":
                                org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.ocp.applications.ocp.resourcemodel.rev150811.resourcemodel.radiohead.TxSigPathEUTRATDD.TddCPLengthDL tddCPLengthDL = 
                                org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.ocp.applications.ocp.resourcemodel.rev150811.resourcemodel.radiohead.TxSigPathEUTRATDD.TddCPLengthDL.valueOf(params.get(i).getValue());                               
                                txSigPathEUTRATDD.setTddCPLengthDL(tddCPLengthDL);          
                                break;
                            case "earfcn":
                                txSigPathEUTRATDD.setEarfcn(new XsdUnsignedShort(Integer.valueOf(params.get(i).getValue())));  
                                break;
                            case "dlCalREMax":
                                txSigPathEUTRATDD.setDlCalREMax(new XsdUnsignedShort(Integer.valueOf(params.get(i).getValue())));
                                break;
                            case "t2a":
                                txSigPathEUTRATDD.setT2a(new XsdUnsignedInt(Long.valueOf(params.get(i).getValue())));
                                break;
                            case "dlCalRE":
                                txSigPathEUTRATDD.setDlCalRE(new XsdUnsignedShort(Integer.valueOf(params.get(i).getValue()))); 
                                break;
                            case "axcW":
                                txSigPathEUTRATDD.setAxcW(new XsdUnsignedByte(Short.valueOf(params.get(i).getValue())));
                                break;
                            case "axcB":
                                txSigPathEUTRATDD.setAxcB(new XsdUnsignedByte(Short.valueOf(params.get(i).getValue())));
                                break;
                            case "oriLink":
                                txSigPathEUTRATDD.setOriLink(new ObjId(params.get(i).getValue())); 
                                break;
                            case "sigmaIQ":
                                txSigPathEUTRATDD.setSigmaIQ(new XsdUnsignedShort(Integer.valueOf(params.get(i).getValue())));
                                break;
                            case "antPort":
                                txSigPathEUTRATDD.setAntPort(new ObjId(params.get(i).getValue()));
                                break;
                            case "enableIQDLComp":
                                txSigPathEUTRATDD.setEnableIQDLComp(Boolean.valueOf(params.get(i).getValue()));     
                                break;
                            default:
                                LOG.debug("ObjId = {} does not exist in Re", params.get(i).getName());                                   
                        }
                        i++;
                    } 
                    readWriteTransaction.merge(LogicalDatastoreType.OPERATIONAL,
                        rootPath.child(TxSigPathEUTRATDD.class, new TxSigPathEUTRATDDKey(new XsdUnsignedInt(instanceNumbers.get(instanceNumbers.size()-1)))), txSigPathEUTRATDD.build(), true);   
                    readWriteTransaction.submit();                                                                        
                }catch (Exception exc) {
                    LOG.info("Data update Failed{}", exc);                 
                }                       
                break;
            case "TxSigPath_GSM":  
                try {
                    TxSigPathGSMBuilder txSigPathGSM = new TxSigPathGSMBuilder();
                    txSigPathGSM.setKey(new TxSigPathGSMKey(new XsdUnsignedInt(instanceNumbers.get(instanceNumbers.size()-1))));                      
                    i = 0;
                    while (i < params.size()) {
                        LOG.debug("Processing param Name: {} in TxSigPath_GSM", params.get(i).getName());                    
                        switch (params.get(i).getName()) {
                            case "freqBandInd":
                                org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.ocp.applications.ocp.resourcemodel.rev150811.resourcemodel.radiohead.TxSigPathGSM.FreqBandInd freqBandInd = 
                                org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.ocp.applications.ocp.resourcemodel.rev150811.resourcemodel.radiohead.TxSigPathGSM.FreqBandInd.valueOf(params.get(i).getValue());
                                txSigPathGSM.setFreqBandInd(freqBandInd);                            
                                break;
                            case "maxTxPwr":
                                txSigPathGSM.setMaxTxPwr(new XsdUnsignedShort(Integer.valueOf(params.get(i).getValue())));
                                break;
                            case "dlCalREMax":
                                txSigPathGSM.setDlCalREMax(new XsdUnsignedShort(Integer.valueOf(params.get(i).getValue())));
                                break;
                            case "t2a":
                                txSigPathGSM.setT2a(new XsdUnsignedInt(Long.valueOf(params.get(i).getValue())));
                                break;
                            case "dlCalRE":
                                txSigPathGSM.setDlCalRE(new XsdUnsignedShort(Integer.valueOf(params.get(i).getValue()))); 
                                break;
                            case "axcW":
                                txSigPathGSM.setAxcW(new XsdUnsignedByte(Short.valueOf(params.get(i).getValue())));
                                break;
                            case "axcB":
                                txSigPathGSM.setAxcB(new XsdUnsignedByte(Short.valueOf(params.get(i).getValue())));
                                break;
                            case "oriLink":
                                txSigPathGSM.setOriLink(new ObjId(params.get(i).getValue())); 
                                break;
                            case "antPort":
                                txSigPathGSM.setAntPort(new ObjId(params.get(i).getValue()));   
                            default:
                                LOG.debug("ObjId = {} does not exist in Re", params.get(i).getName());                                   
                        }
                        i++;
                    } 
                    readWriteTransaction.merge(LogicalDatastoreType.OPERATIONAL,
                        rootPath.child(TxSigPathGSM.class, new TxSigPathGSMKey(new XsdUnsignedInt(instanceNumbers.get(instanceNumbers.size()-1)))), txSigPathGSM.build(), true);   
                    readWriteTransaction.submit();                                    
                }catch (Exception exc) {
                    LOG.info("Data update Failed{}", exc);                 
                }            
                break;
            case "RxSigPath_UTRAFDD":  
                try {
                    RxSigPathUTRAFDDBuilder rxSigPathUTRAFDD = new RxSigPathUTRAFDDBuilder();   
                    rxSigPathUTRAFDD.setKey(new RxSigPathUTRAFDDKey(new XsdUnsignedInt(instanceNumbers.get(instanceNumbers.size()-1))));                                  
                    i = 0;
                    while (i < params.size()) {
                        LOG.debug("Processing param Name: {} in RxSigPath_UTRAFDD", params.get(i).getName());                      
                        switch (params.get(i).getName()) {
                            case "uarfcn":
                                rxSigPathUTRAFDD.setUarfcn(new XsdUnsignedShort(Integer.valueOf(params.get(i).getValue())));
                                break;
                            case "ulCalREMax":
                                rxSigPathUTRAFDD.setUlCalREMax(new XsdUnsignedShort(Integer.valueOf(params.get(i).getValue())));
                                break;
                            case "ta3":
                                rxSigPathUTRAFDD.setTa3(new XsdUnsignedInt(Long.valueOf(params.get(i).getValue())));
                                break;
                            case "ulCalRE":
                                rxSigPathUTRAFDD.setUlCalRE(new XsdUnsignedShort(Integer.valueOf(params.get(i).getValue())));
                                break;
                            case "axcW":
                                rxSigPathUTRAFDD.setAxcW(new XsdUnsignedByte(Short.valueOf(params.get(i).getValue())));
                                break;
                            case "axcB":
                                rxSigPathUTRAFDD.setAxcB(new XsdUnsignedByte(Short.valueOf(params.get(i).getValue())));
                                break;
                            case "rtwpGroup":
                                rxSigPathUTRAFDD.setRtwpGroup(new XsdUnsignedByte(Short.valueOf(params.get(i).getValue())));
                                break;
                            case "oriLink":
                                rxSigPathUTRAFDD.setOriLink(new ObjId(params.get(i).getValue()));
                                break;
                            case "antPort":
                                rxSigPathUTRAFDD.setAntPort(new ObjId(params.get(i).getValue())); 
                                break;
                            case "ulFeedAdj":
                                rxSigPathUTRAFDD.setUlFeedAdj(new XsdShort(Short.valueOf(params.get(i).getValue())));
                                break;
                            case "ulTgtRMSLvl":
                                rxSigPathUTRAFDD.setUlTgtRMSLvl(new XsdUnsignedByte(Short.valueOf(params.get(i).getValue())));
                                break;
                            case "ulAGCSetlgTime":
                                rxSigPathUTRAFDD.setUlAGCSetlgTime(new XsdUnsignedByte(Short.valueOf(params.get(i).getValue())));
                                break;              
                            default:
                                LOG.debug("ObjId = {} does not exist in Re", params.get(i).getName());                                   
                                }
                        i++;
                    } 
                    readWriteTransaction.merge(LogicalDatastoreType.OPERATIONAL,
                        rootPath.child(RxSigPathUTRAFDD.class, new RxSigPathUTRAFDDKey(new XsdUnsignedInt(instanceNumbers.get(instanceNumbers.size()-1)))), rxSigPathUTRAFDD.build(), true);   
                    readWriteTransaction.submit();                                    
                }catch (Exception exc) {
                    LOG.info("Data update Failed{}", exc);                 
                }
                break;
            case "RxSigPath_EUTRAFDD":  
                try {
                    RxSigPathEUTRAFDDBuilder rxSigPathEUTRAFDD = new RxSigPathEUTRAFDDBuilder();  
                    rxSigPathEUTRAFDD.setKey(new RxSigPathEUTRAFDDKey(new XsdUnsignedInt(instanceNumbers.get(instanceNumbers.size()-1))));                     
                    i = 0;
                    while (i < params.size()) {
                        LOG.debug("Processing param Name: {} in RxSigPath_EUTRAFDD", params.get(i).getName());                    
                        switch (params.get(i).getName()) {
                            case "chanBW":
                                rxSigPathEUTRAFDD.setChanBW(new XsdUnsignedShort(Integer.valueOf(params.get(i).getValue())));
                                break;
                            case "earfcn":
                                rxSigPathEUTRAFDD.setEarfcn(new XsdUnsignedShort(Integer.valueOf(params.get(i).getValue())));
                                break;      
                            case "ulCalREMax":
                                rxSigPathEUTRAFDD.setUlCalREMax(new XsdUnsignedShort(Integer.valueOf(params.get(i).getValue())));
                                break;
                            case "ta3":
                                rxSigPathEUTRAFDD.setTa3(new XsdUnsignedInt(Long.valueOf(params.get(i).getValue())));
                                break;
                            case "ulCalRE":
                                rxSigPathEUTRAFDD.setUlCalRE(new XsdUnsignedShort(Integer.valueOf(params.get(i).getValue())));
                                break;
                            case "sigmaIQ":
                                rxSigPathEUTRAFDD.setSigmaIQ(new XsdUnsignedShort(Integer.valueOf(params.get(i).getValue())));
                                break;
                            case "axcW":
                                rxSigPathEUTRAFDD.setAxcW(new XsdUnsignedByte(Short.valueOf(params.get(i).getValue())));
                                break;
                            case "axcB":
                                rxSigPathEUTRAFDD.setAxcB(new XsdUnsignedByte(Short.valueOf(params.get(i).getValue())));
                                break;
                            case "oriLink":
                                rxSigPathEUTRAFDD.setOriLink(new ObjId(params.get(i).getValue()));
                                break;
                            case "antPort":
                                rxSigPathEUTRAFDD.setAntPort(new ObjId(params.get(i).getValue()));
                                break;
                            case "enableIQDLComp":
                                rxSigPathEUTRAFDD.setEnableIQDLComp(Boolean.valueOf(params.get(i).getValue()));
                                break;
                            default:
                                LOG.debug("ObjId = {} does not exist in Re", params.get(i).getName());                                   
                        }
                        i++;
                    } 
                    readWriteTransaction.merge(LogicalDatastoreType.OPERATIONAL,
                        rootPath.child(RxSigPathEUTRAFDD.class, new RxSigPathEUTRAFDDKey(new XsdUnsignedInt(instanceNumbers.get(instanceNumbers.size()-1)))), rxSigPathEUTRAFDD.build(), true);   
                    readWriteTransaction.submit();                                    
                }catch (Exception exc) {
                    LOG.info("Data update Failed{}", exc);                 
                }     
                break;
            case "RxSigPath_EUTRATDD":  
                try {
                    RxSigPathEUTRATDDBuilder rxSigPathEUTRATDD = new RxSigPathEUTRATDDBuilder();   
                    rxSigPathEUTRATDD.setKey(new RxSigPathEUTRATDDKey(new XsdUnsignedInt(instanceNumbers.get(instanceNumbers.size()-1))));                     
                    i = 0;
                    while (i < params.size()) {
                        LOG.debug("Processing param Name: {} in RxSigPath_EUTRATDD", params.get(i).getName());                    
                        switch (params.get(i).getName()) {
                            case "chanBW":
                                rxSigPathEUTRATDD.setChanBW(new XsdUnsignedShort(Integer.valueOf(params.get(i).getValue())));
                                break;
                            case "tddULDLConfig":
                                rxSigPathEUTRATDD.setTddULDLConfig(new XsdUnsignedShort(Integer.valueOf(params.get(i).getValue())));
                                break;
                            case "tddSpecialSFConfig":
                                rxSigPathEUTRATDD.setTddSpecialSFConfig(new XsdUnsignedShort(Integer.valueOf(params.get(i).getValue())));
                                break;
                            case "tddCPLengthUL":
                                org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.ocp.applications.ocp.resourcemodel.rev150811.resourcemodel.radiohead.RxSigPathEUTRATDD.TddCPLengthUL tddCPLengthUL = 
                                org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.ocp.applications.ocp.resourcemodel.rev150811.resourcemodel.radiohead.RxSigPathEUTRATDD.TddCPLengthUL.valueOf(params.get(i).getValue());                               
                                rxSigPathEUTRATDD.setTddCPLengthUL(tddCPLengthUL);
                                break;
                            case "ulCalREMax":
                                rxSigPathEUTRATDD.setUlCalREMax(new XsdUnsignedShort(Integer.valueOf(params.get(i).getValue())));
                                break;
                            case "ta3":
                                rxSigPathEUTRATDD.setTa3(new XsdUnsignedInt(Long.valueOf(params.get(i).getValue())));
                                break;
                            case "axcW":
                                rxSigPathEUTRATDD.setAxcW(new XsdUnsignedByte(Short.valueOf(params.get(i).getValue())));
                                break;
                            case "axcB":
                                rxSigPathEUTRATDD.setAxcB(new XsdUnsignedByte(Short.valueOf(params.get(i).getValue())));
                                break;
                            case "oriLink":
                                rxSigPathEUTRATDD.setOriLink(new ObjId(params.get(i).getValue()));
                                break;
                            case "antPort":
                                rxSigPathEUTRATDD.setAntPort(new ObjId(params.get(i).getValue()));
                                break;
                            case "earfcn":
                                rxSigPathEUTRATDD.setEarfcn(new XsdUnsignedShort(Integer.valueOf(params.get(i).getValue())));
                                break;
                            case "sigmaIQ":
                                rxSigPathEUTRATDD.setSigmaIQ(new XsdUnsignedShort(Integer.valueOf(params.get(i).getValue())));
                                break;
                            case "enableIQDLComp":
                                rxSigPathEUTRATDD.setEnableIQDLComp(Boolean.valueOf(params.get(i).getValue()));
                                break;
                            default:
                                LOG.debug("ObjId = {} does not exist in Re", params.get(i).getName());                                   
                        }
                        i++;
                    } 
                    readWriteTransaction.merge(LogicalDatastoreType.OPERATIONAL,
                        rootPath.child(RxSigPathEUTRATDD.class, new RxSigPathEUTRATDDKey(new XsdUnsignedInt(instanceNumbers.get(instanceNumbers.size()-1)))), rxSigPathEUTRATDD.build(), true);   
                    readWriteTransaction.submit();                                    
                }catch (Exception exc) {
                    LOG.info("Data update Failed{}", exc);                 
                }
                break;
            case "RxSigPath_GSM":  
                try {
                    RxSigPathGSMBuilder rxSigPathGSM = new RxSigPathGSMBuilder();   
                    rxSigPathGSM.setKey(new RxSigPathGSMKey(new XsdUnsignedInt(instanceNumbers.get(instanceNumbers.size()-1))));                      
                    i = 0;
                    while (i < params.size()) {
                        LOG.debug("Processing param Name: {} in RxSigPath_GSM", params.get(i).getName());                    
                        switch (params.get(i).getName()) {
                            case "freqBandInd":
                                org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.ocp.applications.ocp.resourcemodel.rev150811.resourcemodel.radiohead.RxSigPathGSM.FreqBandInd freqBandInd = 
                                org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.ocp.applications.ocp.resourcemodel.rev150811.resourcemodel.radiohead.RxSigPathGSM.FreqBandInd.valueOf(params.get(i).getValue());
                                rxSigPathGSM.setFreqBandInd(freqBandInd);
                                break;
                            case "ulCalREMax":
                                rxSigPathGSM.setUlCalREMax(new XsdUnsignedShort(Integer.valueOf(params.get(i).getValue()))); 
                                break;
                            case "ta3":
                                rxSigPathGSM.setTa3(new XsdUnsignedInt(Long.valueOf(params.get(i).getValue())));
                                break;
                            case "ulCalRE":
                                rxSigPathGSM.setUlCalRE(new XsdUnsignedShort(Integer.valueOf(params.get(i).getValue())));
                                break;
                            case "axcW":
                                rxSigPathGSM.setAxcW(new XsdUnsignedByte(Short.valueOf(params.get(i).getValue()))); 
                                break;
                            case "axcB":
                                rxSigPathGSM.setAxcB(new XsdUnsignedByte(Short.valueOf(params.get(i).getValue())));
                                break;
                            case "oriLink":
                                rxSigPathGSM.setOriLink(new ObjId(params.get(i).getValue())); 
                                break;
                            case "ulFeedAdj":
                                rxSigPathGSM.setUlFeedAdj(new XsdShort(Short.valueOf(params.get(i).getValue())));
                                break;
                            case "antPort":
                                rxSigPathGSM.setAntPort(new ObjId(params.get(i).getValue()));
                                break;
                            default:
                                LOG.debug("ObjId = {} does not exist in Re", params.get(i).getName());                                   
                        }
                        i++;
                    } 
                    readWriteTransaction.merge(LogicalDatastoreType.OPERATIONAL,
                        rootPath.child(RxSigPathGSM.class, new RxSigPathGSMKey(new XsdUnsignedInt(instanceNumbers.get(instanceNumbers.size()-1)))), rxSigPathGSM.build(), true);   
                    readWriteTransaction.submit();                                    
                }catch (Exception exc) {
                    LOG.info("Data update Failed{}", exc);                 
                }   
                break;
            case "aisgPort":  
                try {
                    AisgPortBuilder aisgPort = new AisgPortBuilder();  
                    aisgPort.setKey(new AisgPortKey(new XsdUnsignedInt(instanceNumbers.get(instanceNumbers.size()-1))));                     
                    i = 0;
                    while (i < params.size()) {
                        LOG.debug("Processing param Name: {} in aisgPort", params.get(i).getName());                    
                        switch (params.get(i).getName()) {
                            case "busPowerEnable":
                                aisgPort.setBusPowerEnable(Boolean.valueOf(params.get(i).getValue()));
                                break;
                            case "portLabel":
                                aisgPort.setPortLabel(params.get(i).getValue());  
                                break;
                            default:
                                LOG.debug("ObjId = {} does not exist in Re", params.get(i).getName());                                   
                        }
                        i++;
                    } 
                    readWriteTransaction.merge(LogicalDatastoreType.OPERATIONAL,
                        rootPath.child(AisgPort.class, new AisgPortKey(new XsdUnsignedInt(instanceNumbers.get(instanceNumbers.size()-1)))), aisgPort.build(), true);   
                    readWriteTransaction.submit();                                   
                }catch (Exception exc) {
                    LOG.info("Data update Failed{}", exc);                 
                }                        
                break;
            case "aisgALD":  
                try {
                    AisgALDBuilder aisgALD = new AisgALDBuilder();   
                    aisgALD.setKey(new AisgALDKey(new XsdUnsignedInt(instanceNumbers.get(instanceNumbers.size()-1))));                       
                    i = 0;
                    while (i < params.size()) {
                        LOG.debug("Processing param Name: {} in aisgALD", params.get(i).getName());                        
                        switch (params.get(i).getName()) {
                            case "deviceType":
                                aisgALD.setDeviceType(new XsdUnsignedByte(Short.valueOf(params.get(i).getValue())));
                                break;
                            case "UID":
                                aisgALD.setUID(params.get(i).getValue());   
                                break;
                            case "releaseID":
                                aisgALD.setReleaseID(new XsdUnsignedByte(Short.valueOf(params.get(i).getValue())));  
                                break;
                            case "aisgVersion":
                                aisgALD.setAisgVersion(new XsdUnsignedByte(Short.valueOf(params.get(i).getValue())));
                                break;
                            case "deviceTypeVersion":
                                aisgALD.setDeviceTypeVersion(params.get(i).getValue()); 
                                break;
                            case "frameLength":
                                aisgALD.setFrameLength(new XsdUnsignedShort(Integer.valueOf(params.get(i).getValue())));
                                break;
                            case "hdlcAddress":
                                aisgALD.setHdlcAddress(new XsdUnsignedByte(Short.valueOf(params.get(i).getValue())));      
                                break;
                            default:
                                LOG.debug("ObjId = {} does not exist in Re", params.get(i).getName());                                   
                        }
                        i++;
                    } 
                    InstanceIdentifier<AisgPort> aisgPath = rootPath.child(AisgPort.class, new AisgPortKey(new XsdUnsignedInt(instanceNumbers.get(instanceNumbers.size())-2)));
                    readWriteTransaction.merge(LogicalDatastoreType.OPERATIONAL,
                        aisgPath.child(AisgALD.class, new AisgALDKey(new XsdUnsignedInt(instanceNumbers.get(instanceNumbers.size()-1)))), aisgALD.build(), true);   
                    readWriteTransaction.submit();                                     
                }catch (Exception exc) {
                    LOG.info("Data update Failed{}", exc);                 
                }   
                break;           
            default:
                ;
        }             
    }
        
        
    private void parseObjId(List<String> objTypes, List<Long> instanceNumbers, String objId) {    
        String[] objIdTokens = objId.split("/");       
        for (String token : objIdTokens) {
            String[] tokenArray = token.split(":");       
            try{
                if (tokenArray.length > 0)
                    objTypes.add(tokenArray[0]);
                if (tokenArray.length > 1)                
                    instanceNumbers.add(Long.valueOf(tokenArray[1]));
            } catch (Exception exc) {
                LOG.error("ObjId parse failed {}, {}", exc, objId);                
            }
        }
    }   
}
