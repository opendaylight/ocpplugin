package org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.fault.mgmt.rev150811;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.common.types.rev150811.FaultId;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.common.types.rev150811.FaultSeverity;
import org.opendaylight.yangtools.yang.binding.AugmentationHolder;
import java.util.HashMap;
import org.opendaylight.yangtools.concepts.Builder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.common.types.rev150811.FaultState;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.xsd.types.rev150811.XsdDateTime;
import org.opendaylight.yangtools.yang.binding.Augmentation;
import org.opendaylight.yang.gen.v1.urn.opendaylight.inventory.rev130819.NodeRef;
import org.opendaylight.yangtools.yang.binding.DataObject;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.common.types.rev150811.ObjId;
import java.util.Objects;
import java.util.List;
import java.util.Collections;
import java.util.Map;


/**
 * Class that builds {@link org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.fault.mgmt.rev150811.FaultInd} instances.
 *
 * @see org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.fault.mgmt.rev150811.FaultInd
 *
 */
public class FaultIndBuilder implements Builder <org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.fault.mgmt.rev150811.FaultInd> {

    private List<java.lang.String> _affectedObj;
    private java.lang.String _descr;
    private FaultId _faultId;
    private NodeRef _node;
    private ObjId _objId;
    private FaultSeverity _severity;
    private FaultState _state;
    private XsdDateTime _timestamp;

    Map<java.lang.Class<? extends Augmentation<org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.fault.mgmt.rev150811.FaultInd>>, Augmentation<org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.fault.mgmt.rev150811.FaultInd>> augmentation = Collections.emptyMap();

    public FaultIndBuilder() {
    }
    public FaultIndBuilder(org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.common.types.rev150811.FaultInd arg) {
        this._objId = arg.getObjId();
        this._faultId = arg.getFaultId();
        this._state = arg.getState();
        this._severity = arg.getSeverity();
        this._timestamp = arg.getTimestamp();
        this._descr = arg.getDescr();
        this._affectedObj = arg.getAffectedObj();
    }
    public FaultIndBuilder(org.opendaylight.yang.gen.v1.urn.opendaylight.inventory.rev130819.NodeContextRef arg) {
        this._node = arg.getNode();
    }

    public FaultIndBuilder(FaultInd base) {
        this._affectedObj = base.getAffectedObj();
        this._descr = base.getDescr();
        this._faultId = base.getFaultId();
        this._node = base.getNode();
        this._objId = base.getObjId();
        this._severity = base.getSeverity();
        this._state = base.getState();
        this._timestamp = base.getTimestamp();
        if (base instanceof FaultIndImpl) {
            FaultIndImpl impl = (FaultIndImpl) base;
            if (!impl.augmentation.isEmpty()) {
                this.augmentation = new HashMap<>(impl.augmentation);
            }
        } else if (base instanceof AugmentationHolder) {
            @SuppressWarnings("unchecked")
            AugmentationHolder<org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.fault.mgmt.rev150811.FaultInd> casted =(AugmentationHolder<org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.fault.mgmt.rev150811.FaultInd>) base;
            if (!casted.augmentations().isEmpty()) {
                this.augmentation = new HashMap<>(casted.augmentations());
            }
        }
    }

    /**
     *Set fields from given grouping argument. Valid argument is instance of one of following types:
     * <ul>
     * <li>org.opendaylight.yang.gen.v1.urn.opendaylight.inventory.rev130819.NodeContextRef</li>
     * <li>org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.common.types.rev150811.FaultInd</li>
     * </ul>
     *
     * @param arg grouping object
     * @throws IllegalArgumentException if given argument is none of valid types
    */
    public void fieldsFrom(DataObject arg) {
        boolean isValidArg = false;
        if (arg instanceof org.opendaylight.yang.gen.v1.urn.opendaylight.inventory.rev130819.NodeContextRef) {
            this._node = ((org.opendaylight.yang.gen.v1.urn.opendaylight.inventory.rev130819.NodeContextRef)arg).getNode();
            isValidArg = true;
        }
        if (arg instanceof org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.common.types.rev150811.FaultInd) {
            this._objId = ((org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.common.types.rev150811.FaultInd)arg).getObjId();
            this._faultId = ((org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.common.types.rev150811.FaultInd)arg).getFaultId();
            this._state = ((org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.common.types.rev150811.FaultInd)arg).getState();
            this._severity = ((org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.common.types.rev150811.FaultInd)arg).getSeverity();
            this._timestamp = ((org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.common.types.rev150811.FaultInd)arg).getTimestamp();
            this._descr = ((org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.common.types.rev150811.FaultInd)arg).getDescr();
            this._affectedObj = ((org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.common.types.rev150811.FaultInd)arg).getAffectedObj();
            isValidArg = true;
        }
        if (!isValidArg) {
            throw new IllegalArgumentException(
              "expected one of: [org.opendaylight.yang.gen.v1.urn.opendaylight.inventory.rev130819.NodeContextRef, org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.common.types.rev150811.FaultInd] \n" +
              "but was: " + arg
            );
        }
    }

    public List<java.lang.String> getAffectedObj() {
        return _affectedObj;
    }
    
    public java.lang.String getDescr() {
        return _descr;
    }
    
    public FaultId getFaultId() {
        return _faultId;
    }
    
    public NodeRef getNode() {
        return _node;
    }
    
    public ObjId getObjId() {
        return _objId;
    }
    
    public FaultSeverity getSeverity() {
        return _severity;
    }
    
    public FaultState getState() {
        return _state;
    }
    
    public XsdDateTime getTimestamp() {
        return _timestamp;
    }
    
    @SuppressWarnings("unchecked")
    public <E extends Augmentation<org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.fault.mgmt.rev150811.FaultInd>> E getAugmentation(java.lang.Class<E> augmentationType) {
        if (augmentationType == null) {
            throw new IllegalArgumentException("Augmentation Type reference cannot be NULL!");
        }
        return (E) augmentation.get(augmentationType);
    }

     
    public FaultIndBuilder setAffectedObj(final List<java.lang.String> value) {
        this._affectedObj = value;
        return this;
    }
    
     
    public FaultIndBuilder setDescr(final java.lang.String value) {
        this._descr = value;
        return this;
    }
    
     
    public FaultIndBuilder setFaultId(final FaultId value) {
        this._faultId = value;
        return this;
    }
    
     
    public FaultIndBuilder setNode(final NodeRef value) {
        this._node = value;
        return this;
    }
    
     
    public FaultIndBuilder setObjId(final ObjId value) {
        this._objId = value;
        return this;
    }
    
     
    public FaultIndBuilder setSeverity(final FaultSeverity value) {
        this._severity = value;
        return this;
    }
    
     
    public FaultIndBuilder setState(final FaultState value) {
        this._state = value;
        return this;
    }
    
     
    public FaultIndBuilder setTimestamp(final XsdDateTime value) {
        this._timestamp = value;
        return this;
    }
    
    public FaultIndBuilder addAugmentation(java.lang.Class<? extends Augmentation<org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.fault.mgmt.rev150811.FaultInd>> augmentationType, Augmentation<org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.fault.mgmt.rev150811.FaultInd> augmentation) {
        if (augmentation == null) {
            return removeAugmentation(augmentationType);
        }
    
        if (!(this.augmentation instanceof HashMap)) {
            this.augmentation = new HashMap<>();
        }
    
        this.augmentation.put(augmentationType, augmentation);
        return this;
    }
    
    public FaultIndBuilder removeAugmentation(java.lang.Class<? extends Augmentation<org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.fault.mgmt.rev150811.FaultInd>> augmentationType) {
        if (this.augmentation instanceof HashMap) {
            this.augmentation.remove(augmentationType);
        }
        return this;
    }

    public FaultInd build() {
        return new FaultIndImpl(this);
    }

    private static final class FaultIndImpl implements FaultInd {

        public java.lang.Class<org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.fault.mgmt.rev150811.FaultInd> getImplementedInterface() {
            return org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.fault.mgmt.rev150811.FaultInd.class;
        }

        private final List<java.lang.String> _affectedObj;
        private final java.lang.String _descr;
        private final FaultId _faultId;
        private final NodeRef _node;
        private final ObjId _objId;
        private final FaultSeverity _severity;
        private final FaultState _state;
        private final XsdDateTime _timestamp;

        private Map<java.lang.Class<? extends Augmentation<org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.fault.mgmt.rev150811.FaultInd>>, Augmentation<org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.fault.mgmt.rev150811.FaultInd>> augmentation = Collections.emptyMap();

        private FaultIndImpl(FaultIndBuilder base) {
            this._affectedObj = base.getAffectedObj();
            this._descr = base.getDescr();
            this._faultId = base.getFaultId();
            this._node = base.getNode();
            this._objId = base.getObjId();
            this._severity = base.getSeverity();
            this._state = base.getState();
            this._timestamp = base.getTimestamp();
            switch (base.augmentation.size()) {
            case 0:
                this.augmentation = Collections.emptyMap();
                break;
            case 1:
                final Map.Entry<java.lang.Class<? extends Augmentation<org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.fault.mgmt.rev150811.FaultInd>>, Augmentation<org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.fault.mgmt.rev150811.FaultInd>> e = base.augmentation.entrySet().iterator().next();
                this.augmentation = Collections.<java.lang.Class<? extends Augmentation<org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.fault.mgmt.rev150811.FaultInd>>, Augmentation<org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.fault.mgmt.rev150811.FaultInd>>singletonMap(e.getKey(), e.getValue());
                break;
            default :
                this.augmentation = new HashMap<>(base.augmentation);
            }
        }

        @Override
        public List<java.lang.String> getAffectedObj() {
            return _affectedObj;
        }
        
        @Override
        public java.lang.String getDescr() {
            return _descr;
        }
        
        @Override
        public FaultId getFaultId() {
            return _faultId;
        }
        
        @Override
        public NodeRef getNode() {
            return _node;
        }
        
        @Override
        public ObjId getObjId() {
            return _objId;
        }
        
        @Override
        public FaultSeverity getSeverity() {
            return _severity;
        }
        
        @Override
        public FaultState getState() {
            return _state;
        }
        
        @Override
        public XsdDateTime getTimestamp() {
            return _timestamp;
        }
        
        @SuppressWarnings("unchecked")
        @Override
        public <E extends Augmentation<org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.fault.mgmt.rev150811.FaultInd>> E getAugmentation(java.lang.Class<E> augmentationType) {
            if (augmentationType == null) {
                throw new IllegalArgumentException("Augmentation Type reference cannot be NULL!");
            }
            return (E) augmentation.get(augmentationType);
        }

        private int hash = 0;
        private volatile boolean hashValid = false;
        
        @Override
        public int hashCode() {
            if (hashValid) {
                return hash;
            }
        
            final int prime = 31;
            int result = 1;
            result = prime * result + Objects.hashCode(_affectedObj);
            result = prime * result + Objects.hashCode(_descr);
            result = prime * result + Objects.hashCode(_faultId);
            result = prime * result + Objects.hashCode(_node);
            result = prime * result + Objects.hashCode(_objId);
            result = prime * result + Objects.hashCode(_severity);
            result = prime * result + Objects.hashCode(_state);
            result = prime * result + Objects.hashCode(_timestamp);
            result = prime * result + Objects.hashCode(augmentation);
        
            hash = result;
            hashValid = true;
            return result;
        }

        @Override
        public boolean equals(java.lang.Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof DataObject)) {
                return false;
            }
            if (!org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.fault.mgmt.rev150811.FaultInd.class.equals(((DataObject)obj).getImplementedInterface())) {
                return false;
            }
            org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.fault.mgmt.rev150811.FaultInd other = (org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.fault.mgmt.rev150811.FaultInd)obj;
            if (!Objects.equals(_affectedObj, other.getAffectedObj())) {
                return false;
            }
            if (!Objects.equals(_descr, other.getDescr())) {
                return false;
            }
            if (!Objects.equals(_faultId, other.getFaultId())) {
                return false;
            }
            if (!Objects.equals(_node, other.getNode())) {
                return false;
            }
            if (!Objects.equals(_objId, other.getObjId())) {
                return false;
            }
            if (!Objects.equals(_severity, other.getSeverity())) {
                return false;
            }
            if (!Objects.equals(_state, other.getState())) {
                return false;
            }
            if (!Objects.equals(_timestamp, other.getTimestamp())) {
                return false;
            }
            if (getClass() == obj.getClass()) {
                // Simple case: we are comparing against self
                FaultIndImpl otherImpl = (FaultIndImpl) obj;
                if (!Objects.equals(augmentation, otherImpl.augmentation)) {
                    return false;
                }
            } else {
                // Hard case: compare our augments with presence there...
                for (Map.Entry<java.lang.Class<? extends Augmentation<org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.fault.mgmt.rev150811.FaultInd>>, Augmentation<org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.fault.mgmt.rev150811.FaultInd>> e : augmentation.entrySet()) {
                    if (!e.getValue().equals(other.getAugmentation(e.getKey()))) {
                        return false;
                    }
                }
                // .. and give the other one the chance to do the same
                if (!obj.equals(this)) {
                    return false;
                }
            }
            return true;
        }

        @Override
        public java.lang.String toString() {
            java.lang.String name = "FaultInd [";
            java.lang.StringBuilder builder = new java.lang.StringBuilder (name);
            if (_affectedObj != null) {
                builder.append("_affectedObj=");
                builder.append(_affectedObj);
                builder.append(", ");
            }
            if (_descr != null) {
                builder.append("_descr=");
                builder.append(_descr);
                builder.append(", ");
            }
            if (_faultId != null) {
                builder.append("_faultId=");
                builder.append(_faultId);
                builder.append(", ");
            }
            if (_node != null) {
                builder.append("_node=");
                builder.append(_node);
                builder.append(", ");
            }
            if (_objId != null) {
                builder.append("_objId=");
                builder.append(_objId);
                builder.append(", ");
            }
            if (_severity != null) {
                builder.append("_severity=");
                builder.append(_severity);
                builder.append(", ");
            }
            if (_state != null) {
                builder.append("_state=");
                builder.append(_state);
                builder.append(", ");
            }
            if (_timestamp != null) {
                builder.append("_timestamp=");
                builder.append(_timestamp);
            }
            final int builderLength = builder.length();
            final int builderAdditionalLength = builder.substring(name.length(), builderLength).length();
            if (builderAdditionalLength > 2 && !builder.substring(builderLength - 2, builderLength).equals(", ")) {
                builder.append(", ");
            }
            builder.append("augmentation=");
            builder.append(augmentation.values());
            return builder.append(']').toString();
        }
    }

}
