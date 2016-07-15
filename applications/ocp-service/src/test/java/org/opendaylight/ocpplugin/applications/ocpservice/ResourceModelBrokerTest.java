/*
 * Copyright (c) 2015 Foxconn Corporation and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.ocpplugin.applications.ocpservice;

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
import org.opendaylight.yang.gen.v1.urn.ietf.params.xml.ns.yang.ietf.inet.types.rev130715.Ipv4Address;
import org.opendaylight.yang.gen.v1.urn.ietf.params.xml.ns.yang.ietf.yang.types.rev130715.MacAddress;
import java.math.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

/*
 * @author Jason Yuan <jason.cw.yuan@foxconn.com>
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class ResourceModelBrokerTest {

    /** timeout of final step [ms] */
    private static final int FINAL_STEP_TIMEOUT = 500;
    private ResourceModelBroker resourceModelBroker;
    @Mock
    private DataBroker dataBroker;
    @Mock
    private ReadWriteTransaction readWriteTransaction;

    /**
     * before each test method
     */
    @Before
    public void setUp() {
        resourceModelBroker = new ResourceModelBroker(new NodeId("ut-node:123"), dataBroker);
        Mockito.when(dataBroker.newReadWriteTransaction()).thenReturn(readWriteTransaction);
    }

    /**
     * after each test method
     * @throws InterruptedException
     */
    @After
    public void tearDown() throws InterruptedException {
        Thread.sleep(200L);
    }

    /**
     * Test method for org.opendaylight.ocpplugin.applications.ocpservice.resourceModelBroker#DeleteNode
     * @throws InterruptedException
     */
    @Test
    public void testDelete() throws InterruptedException {
        resourceModelBroker.deleteObj("antPort:0");
        resourceModelBroker.deleteObj("TxSigPath_UTRAFDD:0");
        resourceModelBroker.deleteObj("TxSigPath_EUTRAFDD:0");
        resourceModelBroker.deleteObj("TxSigPath_EUTRATDD:0");
        resourceModelBroker.deleteObj("TxSigPath_GSM:0");
        resourceModelBroker.deleteObj("RxSigPath_UTRAFDD:0");
        resourceModelBroker.deleteObj("RxSigPath_EUTRAFDD:0");
        resourceModelBroker.deleteObj("RxSigPath_EUTRATDD:0");
        resourceModelBroker.deleteObj("RxSigPath_GSM:0");
        resourceModelBroker.deleteObj("aisgPort:0");
        resourceModelBroker.deleteObj("aisgPort:0/aisgALD:3");
        resourceModelBroker.deleteObj("ALL");
        resourceModelBroker.deleteObj("Default_case");        
    }

    /**
     * Test method for org.opendaylight.ocpplugin.applications.ocpservice.resourceModelBroker#UpdateObj
     * @throws InterruptedException
     */
    @Test
    public void testUpdate() throws InterruptedException {
        List<org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.common.types.rev150811.getparamoutput.obj.Param> outputParams = 
                new ArrayList<org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.common.types.rev150811.getparamoutput.obj.Param>();    

        org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.common.types.rev150811.getparamoutput.obj.ParamBuilder outputParamBuilder =    
            new org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.common.types.rev150811.getparamoutput.obj.ParamBuilder();
        /* test for RE */
        outputParamBuilder.setName("vendorID");
        outputParamBuilder.setValue("123");
        outputParams.add(outputParamBuilder.build());         

        outputParamBuilder.setName("productID");
        outputParamBuilder.setValue("123");
        outputParams.add(outputParamBuilder.build());         

        outputParamBuilder.setName("productRev");
        outputParamBuilder.setValue("123");
        outputParams.add(outputParamBuilder.build());    

        outputParamBuilder.setName("serialNumber");
        outputParamBuilder.setValue("123");
        outputParams.add(outputParamBuilder.build());    

        outputParamBuilder.setName("protocolVer");
        outputParamBuilder.setValue("123");
        outputParams.add(outputParamBuilder.build());    

        outputParamBuilder.setName("agcTargetLevCfgGran");
        outputParamBuilder.setValue("RE");
        outputParams.add(outputParamBuilder.build());    

        outputParamBuilder.setName("agcSettlTimeCfgGran");
        outputParamBuilder.setValue("RE");
        outputParams.add(outputParamBuilder.build());    

        outputParamBuilder.setName("agcSettlTimeCap");
        outputParamBuilder.setValue("1FFF");
        outputParams.add(outputParamBuilder.build());    

        outputParamBuilder.setName("Default_test");
        outputParamBuilder.setValue("test");
        outputParams.add(outputParamBuilder.build());   
        
        resourceModelBroker.updateObj(outputParams, "RE:0");     
        
        /* test for antPort */        
        outputParams = 
                new ArrayList<org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.common.types.rev150811.getparamoutput.obj.Param>();     

        outputParamBuilder.setName("portLabel");
        outputParamBuilder.setValue("123");
        outputParams.add(outputParamBuilder.build());   

        outputParamBuilder.setName("Default_test");
        outputParamBuilder.setValue("123");
        outputParams.add(outputParamBuilder.build());   
        
        resourceModelBroker.updateObj(outputParams, "antPort:0"); 
        
        /* test for TxSigPath_UTRAFDD */   
        outputParams = 
                new ArrayList<org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.common.types.rev150811.getparamoutput.obj.Param>();     

        outputParamBuilder.setName("maxTxPwr");
        outputParamBuilder.setValue("123");
        outputParams.add(outputParamBuilder.build());   

        outputParamBuilder.setName("dlCalRFMax");
        outputParamBuilder.setValue("123");
        outputParams.add(outputParamBuilder.build());   

        outputParamBuilder.setName("t2a");
        outputParamBuilder.setValue("123");
        outputParams.add(outputParamBuilder.build());   

        outputParamBuilder.setName("dlCalRF");
        outputParamBuilder.setValue("123");
        outputParams.add(outputParamBuilder.build());

        outputParamBuilder.setName("antPort");
        outputParamBuilder.setValue("antPort:0");
        outputParams.add(outputParamBuilder.build());   

        outputParamBuilder.setName("uarfcn");
        outputParamBuilder.setValue("123");
        outputParams.add(outputParamBuilder.build());
        
        resourceModelBroker.updateObj(outputParams, "TxSigPath_UTRAFDD:0");            

        /* test for TxSigPath_EUTRAFDD */   
        outputParams = 
                new ArrayList<org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.common.types.rev150811.getparamoutput.obj.Param>();     

        outputParamBuilder.setName("chanBW");
        outputParamBuilder.setValue("123");
        outputParams.add(outputParamBuilder.build());   

        outputParamBuilder.setName("maxTxPwr");
        outputParamBuilder.setValue("123");
        outputParams.add(outputParamBuilder.build());   

        outputParamBuilder.setName("earfcn");
        outputParamBuilder.setValue("123");
        outputParams.add(outputParamBuilder.build());   

        outputParamBuilder.setName("dlCalREMax");
        outputParamBuilder.setValue("123");
        outputParams.add(outputParamBuilder.build());

        outputParamBuilder.setName("t2a");
        outputParamBuilder.setValue("123");
        outputParams.add(outputParamBuilder.build());   

        outputParamBuilder.setName("dlCalRE");
        outputParamBuilder.setValue("123");
        outputParams.add(outputParamBuilder.build());

        outputParamBuilder.setName("axcW");
        outputParamBuilder.setValue("123");
        outputParams.add(outputParamBuilder.build());

        outputParamBuilder.setName("axcB");
        outputParamBuilder.setValue("123");
        outputParams.add(outputParamBuilder.build());
        
        outputParamBuilder.setName("oriLink");
        outputParamBuilder.setValue("link:0");
        outputParams.add(outputParamBuilder.build());
        
        outputParamBuilder.setName("sigmaIQ");
        outputParamBuilder.setValue("123");
        outputParams.add(outputParamBuilder.build());

        outputParamBuilder.setName("antPort");
        outputParamBuilder.setValue("antPort:0");
        outputParams.add(outputParamBuilder.build());
      
        outputParamBuilder.setName("enableIQDLComp");
        outputParamBuilder.setValue("123");
        outputParams.add(outputParamBuilder.build());
        
        outputParamBuilder.setName("Default_test");
        outputParamBuilder.setValue("123");
        outputParams.add(outputParamBuilder.build());
        
        resourceModelBroker.updateObj(outputParams, "TxSigPath_EUTRAFDD:0");   

        /* test for TxSigPath_EUTRATDD */   
        outputParams = 
                new ArrayList<org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.common.types.rev150811.getparamoutput.obj.Param>();     

        outputParamBuilder.setName("chanBW");
        outputParamBuilder.setValue("123");
        outputParams.add(outputParamBuilder.build());   

        outputParamBuilder.setName("maxTxPwr");
        outputParamBuilder.setValue("123");
        outputParams.add(outputParamBuilder.build());   

        outputParamBuilder.setName("earfcn");
        outputParamBuilder.setValue("123");
        outputParams.add(outputParamBuilder.build());   

        outputParamBuilder.setName("dlCalREMax");
        outputParamBuilder.setValue("123");
        outputParams.add(outputParamBuilder.build());

        outputParamBuilder.setName("t2a");
        outputParamBuilder.setValue("123");
        outputParams.add(outputParamBuilder.build());   

        outputParamBuilder.setName("dlCalRE");
        outputParamBuilder.setValue("123");
        outputParams.add(outputParamBuilder.build());

        outputParamBuilder.setName("axcW");
        outputParamBuilder.setValue("123");
        outputParams.add(outputParamBuilder.build());

        outputParamBuilder.setName("axcB");
        outputParamBuilder.setValue("123");
        outputParams.add(outputParamBuilder.build());
        
        outputParamBuilder.setName("oriLink");
        outputParamBuilder.setValue("link:0");
        outputParams.add(outputParamBuilder.build());
        
        outputParamBuilder.setName("sigmaIQ");
        outputParamBuilder.setValue("123");
        outputParams.add(outputParamBuilder.build());

        outputParamBuilder.setName("antPort");
        outputParamBuilder.setValue("antPort:0");
        outputParams.add(outputParamBuilder.build());
      
        outputParamBuilder.setName("enableIQDLComp");
        outputParamBuilder.setValue("123");
        outputParams.add(outputParamBuilder.build());
        
        outputParamBuilder.setName("Default_test");
        outputParamBuilder.setValue("123");
        outputParams.add(outputParamBuilder.build());
        
        resourceModelBroker.updateObj(outputParams, "TxSigPath_EUTRATDD:0"); 

        /* test for TxSigPath_GSM */   
        outputParams = 
                new ArrayList<org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.common.types.rev150811.getparamoutput.obj.Param>();     

        outputParamBuilder.setName("freqBandInd");
        outputParamBuilder.setValue("GSM850");
        outputParams.add(outputParamBuilder.build());   

        outputParamBuilder.setName("maxTxPwr");
        outputParamBuilder.setValue("123");
        outputParams.add(outputParamBuilder.build());   

        outputParamBuilder.setName("dlCalREMax");
        outputParamBuilder.setValue("123");
        outputParams.add(outputParamBuilder.build());

        outputParamBuilder.setName("t2a");
        outputParamBuilder.setValue("123");
        outputParams.add(outputParamBuilder.build());   

        outputParamBuilder.setName("dlCalRE");
        outputParamBuilder.setValue("123");
        outputParams.add(outputParamBuilder.build());

        outputParamBuilder.setName("axcW");
        outputParamBuilder.setValue("123");
        outputParams.add(outputParamBuilder.build());

        outputParamBuilder.setName("axcB");
        outputParamBuilder.setValue("123");
        outputParams.add(outputParamBuilder.build());
        
        outputParamBuilder.setName("oriLink");
        outputParamBuilder.setValue("link:0");
        outputParams.add(outputParamBuilder.build());

        outputParamBuilder.setName("antPort");
        outputParamBuilder.setValue("antPort:0");
        outputParams.add(outputParamBuilder.build());
        
        outputParamBuilder.setName("Default_test");
        outputParamBuilder.setValue("123");
        outputParams.add(outputParamBuilder.build());
        
        resourceModelBroker.updateObj(outputParams, "TxSigPath_GSM:0");     

        /* test for RxSigPath_UTRAFDD */   
        outputParams = 
                new ArrayList<org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.common.types.rev150811.getparamoutput.obj.Param>();     

        outputParamBuilder.setName("uarfcn");
        outputParamBuilder.setValue("123");
        outputParams.add(outputParamBuilder.build());    

        outputParamBuilder.setName("ulCalREMax");
        outputParamBuilder.setValue("123");
        outputParams.add(outputParamBuilder.build());   

        outputParamBuilder.setName("ta3");
        outputParamBuilder.setValue("123");
        outputParams.add(outputParamBuilder.build());   

        outputParamBuilder.setName("ulCalRE");
        outputParamBuilder.setValue("123");
        outputParams.add(outputParamBuilder.build());

        outputParamBuilder.setName("axcW");
        outputParamBuilder.setValue("123");
        outputParams.add(outputParamBuilder.build());

        outputParamBuilder.setName("axcB");
        outputParamBuilder.setValue("123");
        outputParams.add(outputParamBuilder.build());

        outputParamBuilder.setName("rtwpGroup");
        outputParamBuilder.setValue("123");
        outputParams.add(outputParamBuilder.build());
        
        outputParamBuilder.setName("oriLink");
        outputParamBuilder.setValue("link:0");
        outputParams.add(outputParamBuilder.build());

        outputParamBuilder.setName("antPort");
        outputParamBuilder.setValue("antPort:0");
        outputParams.add(outputParamBuilder.build());
                
        outputParamBuilder.setName("ulFeedAdj");
        outputParamBuilder.setValue("123");
        outputParams.add(outputParamBuilder.build());
        
        outputParamBuilder.setName("ulTgtRMSLvl");
        outputParamBuilder.setValue("123");
        outputParams.add(outputParamBuilder.build());
        
        outputParamBuilder.setName("ulAGCSetlgTime");
        outputParamBuilder.setValue("123");
        outputParams.add(outputParamBuilder.build());
        
        outputParamBuilder.setName("Default_test");
        outputParamBuilder.setValue("123");
        outputParams.add(outputParamBuilder.build());      
        
        resourceModelBroker.updateObj(outputParams, "RxSigPath_UTRAFDD:0");  

        /* test for RxSigPath_EUTRAFDD */   
        outputParams = 
                new ArrayList<org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.common.types.rev150811.getparamoutput.obj.Param>();     

        outputParamBuilder.setName("chanBW");
        outputParamBuilder.setValue("123");
        outputParams.add(outputParamBuilder.build());   

        outputParamBuilder.setName("earfcn");
        outputParamBuilder.setValue("123");
        outputParams.add(outputParamBuilder.build()); 
        
        outputParamBuilder.setName("ulCalREMax");
        outputParamBuilder.setValue("123");
        outputParams.add(outputParamBuilder.build());   

        outputParamBuilder.setName("ta3");
        outputParamBuilder.setValue("123");
        outputParams.add(outputParamBuilder.build());   

        outputParamBuilder.setName("ulCalRE");
        outputParamBuilder.setValue("123");
        outputParams.add(outputParamBuilder.build());

        outputParamBuilder.setName("sigmaIQ");
        outputParamBuilder.setValue("123");
        outputParams.add(outputParamBuilder.build());   

        outputParamBuilder.setName("axcW");
        outputParamBuilder.setValue("123");
        outputParams.add(outputParamBuilder.build());

        outputParamBuilder.setName("axcB");
        outputParamBuilder.setValue("123");
        outputParams.add(outputParamBuilder.build());
        
        outputParamBuilder.setName("oriLink");
        outputParamBuilder.setValue("link:0");
        outputParams.add(outputParamBuilder.build());

        outputParamBuilder.setName("antPort");
        outputParamBuilder.setValue("antPort:0");
        outputParams.add(outputParamBuilder.build());
      
        outputParamBuilder.setName("enableIQDLComp");
        outputParamBuilder.setValue("123");
        outputParams.add(outputParamBuilder.build());
        
        outputParamBuilder.setName("Default_test");
        outputParamBuilder.setValue("123");
        outputParams.add(outputParamBuilder.build()); 
        
        resourceModelBroker.updateObj(outputParams, "RxSigPath_EUTRAFDD:0");   

        /* test for RxSigPath_EUTRATDD */   
        outputParams = 
                new ArrayList<org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.common.types.rev150811.getparamoutput.obj.Param>();     

        outputParamBuilder.setName("chanBW");
        outputParamBuilder.setValue("123");
        outputParams.add(outputParamBuilder.build());   

        outputParamBuilder.setName("tddULDLConfig");
        outputParamBuilder.setValue("123");
        outputParams.add(outputParamBuilder.build()); 
        
        outputParamBuilder.setName("tddSpecialSFConfig");
        outputParamBuilder.setValue("123");
        outputParams.add(outputParamBuilder.build());   

        outputParamBuilder.setName("tddCPLengthUL");
        outputParamBuilder.setValue("Normal");
        outputParams.add(outputParamBuilder.build());   

        outputParamBuilder.setName("ulCalREMax");
        outputParamBuilder.setValue("123");
        outputParams.add(outputParamBuilder.build());

        outputParamBuilder.setName("ta3");
        outputParamBuilder.setValue("123");
        outputParams.add(outputParamBuilder.build());   

        outputParamBuilder.setName("axcW");
        outputParamBuilder.setValue("123");
        outputParams.add(outputParamBuilder.build());

        outputParamBuilder.setName("axcB");
        outputParamBuilder.setValue("123");
        outputParams.add(outputParamBuilder.build());
        
        outputParamBuilder.setName("oriLink");
        outputParamBuilder.setValue("link:0");
        outputParams.add(outputParamBuilder.build());

        outputParamBuilder.setName("antPort");
        outputParamBuilder.setValue("antPort:0");
        outputParams.add(outputParamBuilder.build());
        
        outputParamBuilder.setName("earfcn");
        outputParamBuilder.setValue("123");
        outputParams.add(outputParamBuilder.build());
        
        outputParamBuilder.setName("sigmaIQ");
        outputParamBuilder.setValue("123");
        outputParams.add(outputParamBuilder.build());
        
        outputParamBuilder.setName("enableIQDLComp");
        outputParamBuilder.setValue("123");
        outputParams.add(outputParamBuilder.build());
        
        outputParamBuilder.setName("Default_test");
        outputParamBuilder.setValue("123");
        outputParams.add(outputParamBuilder.build()); 
        
        resourceModelBroker.updateObj(outputParams, "RxSigPath_EUTRATDD:0");  

        /* test for RxSigPath_GSM */   
        outputParams = 
                new ArrayList<org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.common.types.rev150811.getparamoutput.obj.Param>();     

        outputParamBuilder.setName("freqBandInd");
        outputParamBuilder.setValue("GSM850");
        outputParams.add(outputParamBuilder.build());   

        outputParamBuilder.setName("ulCalREMax");
        outputParamBuilder.setValue("123");
        outputParams.add(outputParamBuilder.build()); 
        
        outputParamBuilder.setName("ta3");
        outputParamBuilder.setValue("123");
        outputParams.add(outputParamBuilder.build());   

        outputParamBuilder.setName("ulCalRE");
        outputParamBuilder.setValue("123");
        outputParams.add(outputParamBuilder.build());   

        outputParamBuilder.setName("axcW");
        outputParamBuilder.setValue("123");
        outputParams.add(outputParamBuilder.build());

        outputParamBuilder.setName("axcB");
        outputParamBuilder.setValue("123");
        outputParams.add(outputParamBuilder.build());   
        
        outputParamBuilder.setName("oriLink");
        outputParamBuilder.setValue("link:0");
        outputParams.add(outputParamBuilder.build());

        outputParamBuilder.setName("antPort");
        outputParamBuilder.setValue("antPort:0");
        outputParams.add(outputParamBuilder.build());
        
        outputParamBuilder.setName("ulFeedAdj");
        outputParamBuilder.setValue("123");
        outputParams.add(outputParamBuilder.build());
        
        outputParamBuilder.setName("Default_test");
        outputParamBuilder.setValue("123");
        outputParams.add(outputParamBuilder.build()); 
        
        resourceModelBroker.updateObj(outputParams, "RxSigPath_GSM:0");             
        
        /* test for aisgPort */   
        outputParams = 
                new ArrayList<org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.common.types.rev150811.getparamoutput.obj.Param>();     
        
        outputParamBuilder.setName("busPowerEnable");
        outputParamBuilder.setValue("true");
        outputParams.add(outputParamBuilder.build());
        
        outputParamBuilder.setName("portLabel");
        outputParamBuilder.setValue("123");
        outputParams.add(outputParamBuilder.build()); 

        outputParamBuilder.setName("Default_test");
        outputParamBuilder.setValue("123");
        outputParams.add(outputParamBuilder.build());
        
        resourceModelBroker.updateObj(outputParams, "aisgPort:0"); 

        /* test for aisgALD */   
        outputParams = 
                new ArrayList<org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.common.types.rev150811.getparamoutput.obj.Param>();      

        outputParamBuilder.setName("deviceType");
        outputParamBuilder.setValue("123");
        outputParams.add(outputParamBuilder.build()); 
        
        outputParamBuilder.setName("UID");
        outputParamBuilder.setValue("123");
        outputParams.add(outputParamBuilder.build());   

        outputParamBuilder.setName("releaseID");
        outputParamBuilder.setValue("123");
        outputParams.add(outputParamBuilder.build());   

        outputParamBuilder.setName("aisgVersion");
        outputParamBuilder.setValue("123");
        outputParams.add(outputParamBuilder.build());

        outputParamBuilder.setName("deviceTypeVersion");
        outputParamBuilder.setValue("123");
        outputParams.add(outputParamBuilder.build());   
        
        outputParamBuilder.setName("frameLength");
        outputParamBuilder.setValue("123");
        outputParams.add(outputParamBuilder.build());

        outputParamBuilder.setName("hdlcAddress");
        outputParamBuilder.setValue("123");
        outputParams.add(outputParamBuilder.build());
        
        outputParamBuilder.setName("Default_test");
        outputParamBuilder.setValue("123");
        outputParams.add(outputParamBuilder.build()); 
        
        resourceModelBroker.updateObj(outputParams, "aisgPort:0/aisgALD:0");         
    }
}