package org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.object.state.mgmt.rev150811;
import org.opendaylight.yangtools.yang.binding.Augmentation;
import org.opendaylight.yang.gen.v1.urn.opendaylight.inventory.rev130819.NodeRef;
import org.opendaylight.yangtools.yang.binding.AugmentationHolder;
import org.opendaylight.yangtools.yang.binding.DataObject;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.common.types.rev150811.ObjId;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.common.types.rev150811.StateType;
import java.util.HashMap;
import org.opendaylight.yangtools.concepts.Builder;
import java.util.Objects;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.common.types.rev150811.StateVal;
import java.util.Collections;
import java.util.Map;


/**
 * Class that builds {@link org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.object.state.mgmt.rev150811.StateChangeInd} instances.
 *
 * @see org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.object.state.mgmt.rev150811.StateChangeInd
 *
 */
public class StateChangeIndBuilder implements Builder <org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.object.state.mgmt.rev150811.StateChangeInd> {

    private NodeRef _node;
    private ObjId _objId;
    private StateType _stateType;
    private StateVal _stateValue;

    Map<java.lang.Class<? extends Augmentation<org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.object.state.mgmt.rev150811.StateChangeInd>>, Augmentation<org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.object.state.mgmt.rev150811.StateChangeInd>> augmentation = Collections.emptyMap();

    public StateChangeIndBuilder() {
    }
    public StateChangeIndBuilder(org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.common.types.rev150811.StateChangeInd arg) {
        this._objId = arg.getObjId();
        this._stateType = arg.getStateType();
        this._stateValue = arg.getStateValue();
    }
    public StateChangeIndBuilder(org.opendaylight.yang.gen.v1.urn.opendaylight.inventory.rev130819.NodeContextRef arg) {
        this._node = arg.getNode();
    }

    public StateChangeIndBuilder(StateChangeInd base) {
        this._node = base.getNode();
        this._objId = base.getObjId();
        this._stateType = base.getStateType();
        this._stateValue = base.getStateValue();
        if (base instanceof StateChangeIndImpl) {
            StateChangeIndImpl impl = (StateChangeIndImpl) base;
            if (!impl.augmentation.isEmpty()) {
                this.augmentation = new HashMap<>(impl.augmentation);
            }
        } else if (base instanceof AugmentationHolder) {
            @SuppressWarnings("unchecked")
            AugmentationHolder<org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.object.state.mgmt.rev150811.StateChangeInd> casted =(AugmentationHolder<org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.object.state.mgmt.rev150811.StateChangeInd>) base;
            if (!casted.augmentations().isEmpty()) {
                this.augmentation = new HashMap<>(casted.augmentations());
            }
        }
    }

    /**
     *Set fields from given grouping argument. Valid argument is instance of one of following types:
     * <ul>
     * <li>org.opendaylight.yang.gen.v1.urn.opendaylight.inventory.rev130819.NodeContextRef</li>
     * <li>org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.common.types.rev150811.StateChangeInd</li>
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
        if (arg instanceof org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.common.types.rev150811.StateChangeInd) {
            this._objId = ((org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.common.types.rev150811.StateChangeInd)arg).getObjId();
            this._stateType = ((org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.common.types.rev150811.StateChangeInd)arg).getStateType();
            this._stateValue = ((org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.common.types.rev150811.StateChangeInd)arg).getStateValue();
            isValidArg = true;
        }
        if (!isValidArg) {
            throw new IllegalArgumentException(
              "expected one of: [org.opendaylight.yang.gen.v1.urn.opendaylight.inventory.rev130819.NodeContextRef, org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.common.types.rev150811.StateChangeInd] \n" +
              "but was: " + arg
            );
        }
    }

    public NodeRef getNode() {
        return _node;
    }
    
    public ObjId getObjId() {
        return _objId;
    }
    
    public StateType getStateType() {
        return _stateType;
    }
    
    public StateVal getStateValue() {
        return _stateValue;
    }
    
    @SuppressWarnings("unchecked")
    public <E extends Augmentation<org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.object.state.mgmt.rev150811.StateChangeInd>> E getAugmentation(java.lang.Class<E> augmentationType) {
        if (augmentationType == null) {
            throw new IllegalArgumentException("Augmentation Type reference cannot be NULL!");
        }
        return (E) augmentation.get(augmentationType);
    }

     
    public StateChangeIndBuilder setNode(final NodeRef value) {
        this._node = value;
        return this;
    }
    
     
    public StateChangeIndBuilder setObjId(final ObjId value) {
        this._objId = value;
        return this;
    }
    
     
    public StateChangeIndBuilder setStateType(final StateType value) {
        this._stateType = value;
        return this;
    }
    
     
    public StateChangeIndBuilder setStateValue(final StateVal value) {
        this._stateValue = value;
        return this;
    }
    
    public StateChangeIndBuilder addAugmentation(java.lang.Class<? extends Augmentation<org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.object.state.mgmt.rev150811.StateChangeInd>> augmentationType, Augmentation<org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.object.state.mgmt.rev150811.StateChangeInd> augmentation) {
        if (augmentation == null) {
            return removeAugmentation(augmentationType);
        }
    
        if (!(this.augmentation instanceof HashMap)) {
            this.augmentation = new HashMap<>();
        }
    
        this.augmentation.put(augmentationType, augmentation);
        return this;
    }
    
    public StateChangeIndBuilder removeAugmentation(java.lang.Class<? extends Augmentation<org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.object.state.mgmt.rev150811.StateChangeInd>> augmentationType) {
        if (this.augmentation instanceof HashMap) {
            this.augmentation.remove(augmentationType);
        }
        return this;
    }

    public StateChangeInd build() {
        return new StateChangeIndImpl(this);
    }

    private static final class StateChangeIndImpl implements StateChangeInd {

        public java.lang.Class<org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.object.state.mgmt.rev150811.StateChangeInd> getImplementedInterface() {
            return org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.object.state.mgmt.rev150811.StateChangeInd.class;
        }

        private final NodeRef _node;
        private final ObjId _objId;
        private final StateType _stateType;
        private final StateVal _stateValue;

        private Map<java.lang.Class<? extends Augmentation<org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.object.state.mgmt.rev150811.StateChangeInd>>, Augmentation<org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.object.state.mgmt.rev150811.StateChangeInd>> augmentation = Collections.emptyMap();

        private StateChangeIndImpl(StateChangeIndBuilder base) {
            this._node = base.getNode();
            this._objId = base.getObjId();
            this._stateType = base.getStateType();
            this._stateValue = base.getStateValue();
            switch (base.augmentation.size()) {
            case 0:
                this.augmentation = Collections.emptyMap();
                break;
            case 1:
                final Map.Entry<java.lang.Class<? extends Augmentation<org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.object.state.mgmt.rev150811.StateChangeInd>>, Augmentation<org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.object.state.mgmt.rev150811.StateChangeInd>> e = base.augmentation.entrySet().iterator().next();
                this.augmentation = Collections.<java.lang.Class<? extends Augmentation<org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.object.state.mgmt.rev150811.StateChangeInd>>, Augmentation<org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.object.state.mgmt.rev150811.StateChangeInd>>singletonMap(e.getKey(), e.getValue());
                break;
            default :
                this.augmentation = new HashMap<>(base.augmentation);
            }
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
        public StateType getStateType() {
            return _stateType;
        }
        
        @Override
        public StateVal getStateValue() {
            return _stateValue;
        }
        
        @SuppressWarnings("unchecked")
        @Override
        public <E extends Augmentation<org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.object.state.mgmt.rev150811.StateChangeInd>> E getAugmentation(java.lang.Class<E> augmentationType) {
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
            result = prime * result + Objects.hashCode(_node);
            result = prime * result + Objects.hashCode(_objId);
            result = prime * result + Objects.hashCode(_stateType);
            result = prime * result + Objects.hashCode(_stateValue);
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
            if (!org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.object.state.mgmt.rev150811.StateChangeInd.class.equals(((DataObject)obj).getImplementedInterface())) {
                return false;
            }
            org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.object.state.mgmt.rev150811.StateChangeInd other = (org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.object.state.mgmt.rev150811.StateChangeInd)obj;
            if (!Objects.equals(_node, other.getNode())) {
                return false;
            }
            if (!Objects.equals(_objId, other.getObjId())) {
                return false;
            }
            if (!Objects.equals(_stateType, other.getStateType())) {
                return false;
            }
            if (!Objects.equals(_stateValue, other.getStateValue())) {
                return false;
            }
            if (getClass() == obj.getClass()) {
                // Simple case: we are comparing against self
                StateChangeIndImpl otherImpl = (StateChangeIndImpl) obj;
                if (!Objects.equals(augmentation, otherImpl.augmentation)) {
                    return false;
                }
            } else {
                // Hard case: compare our augments with presence there...
                for (Map.Entry<java.lang.Class<? extends Augmentation<org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.object.state.mgmt.rev150811.StateChangeInd>>, Augmentation<org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.object.state.mgmt.rev150811.StateChangeInd>> e : augmentation.entrySet()) {
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
            java.lang.String name = "StateChangeInd [";
            java.lang.StringBuilder builder = new java.lang.StringBuilder (name);
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
            if (_stateType != null) {
                builder.append("_stateType=");
                builder.append(_stateType);
                builder.append(", ");
            }
            if (_stateValue != null) {
                builder.append("_stateValue=");
                builder.append(_stateValue);
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
