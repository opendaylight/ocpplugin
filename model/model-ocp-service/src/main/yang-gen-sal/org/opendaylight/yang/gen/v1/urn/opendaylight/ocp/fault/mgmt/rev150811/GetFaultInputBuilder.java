package org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.fault.mgmt.rev150811;
import org.opendaylight.yangtools.yang.binding.Augmentation;
import org.opendaylight.yang.gen.v1.urn.opendaylight.inventory.rev130819.NodeRef;
import org.opendaylight.yangtools.yang.binding.AugmentationHolder;
import org.opendaylight.yangtools.yang.binding.DataObject;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.common.types.rev150811.ObjId;
import java.util.HashMap;
import org.opendaylight.yangtools.concepts.Builder;
import java.util.Objects;
import java.util.Collections;
import java.util.Map;


/**
 * Class that builds {@link org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.fault.mgmt.rev150811.GetFaultInput} instances.
 *
 * @see org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.fault.mgmt.rev150811.GetFaultInput
 *
 */
public class GetFaultInputBuilder implements Builder <org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.fault.mgmt.rev150811.GetFaultInput> {

    private NodeRef _node;
    private ObjId _objId;
    private java.lang.Boolean _eventDrivenReporting;

    Map<java.lang.Class<? extends Augmentation<org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.fault.mgmt.rev150811.GetFaultInput>>, Augmentation<org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.fault.mgmt.rev150811.GetFaultInput>> augmentation = Collections.emptyMap();

    public GetFaultInputBuilder() {
    }
    public GetFaultInputBuilder(org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.common.types.rev150811.GetFaultInput arg) {
        this._objId = arg.getObjId();
        this._eventDrivenReporting = arg.isEventDrivenReporting();
    }
    public GetFaultInputBuilder(org.opendaylight.yang.gen.v1.urn.opendaylight.inventory.rev130819.NodeContextRef arg) {
        this._node = arg.getNode();
    }

    public GetFaultInputBuilder(GetFaultInput base) {
        this._node = base.getNode();
        this._objId = base.getObjId();
        this._eventDrivenReporting = base.isEventDrivenReporting();
        if (base instanceof GetFaultInputImpl) {
            GetFaultInputImpl impl = (GetFaultInputImpl) base;
            if (!impl.augmentation.isEmpty()) {
                this.augmentation = new HashMap<>(impl.augmentation);
            }
        } else if (base instanceof AugmentationHolder) {
            @SuppressWarnings("unchecked")
            AugmentationHolder<org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.fault.mgmt.rev150811.GetFaultInput> casted =(AugmentationHolder<org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.fault.mgmt.rev150811.GetFaultInput>) base;
            if (!casted.augmentations().isEmpty()) {
                this.augmentation = new HashMap<>(casted.augmentations());
            }
        }
    }

    /**
     *Set fields from given grouping argument. Valid argument is instance of one of following types:
     * <ul>
     * <li>org.opendaylight.yang.gen.v1.urn.opendaylight.inventory.rev130819.NodeContextRef</li>
     * <li>org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.common.types.rev150811.GetFaultInput</li>
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
        if (arg instanceof org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.common.types.rev150811.GetFaultInput) {
            this._objId = ((org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.common.types.rev150811.GetFaultInput)arg).getObjId();
            this._eventDrivenReporting = ((org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.common.types.rev150811.GetFaultInput)arg).isEventDrivenReporting();
            isValidArg = true;
        }
        if (!isValidArg) {
            throw new IllegalArgumentException(
              "expected one of: [org.opendaylight.yang.gen.v1.urn.opendaylight.inventory.rev130819.NodeContextRef, org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.common.types.rev150811.GetFaultInput] \n" +
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
    
    public java.lang.Boolean isEventDrivenReporting() {
        return _eventDrivenReporting;
    }
    
    @SuppressWarnings("unchecked")
    public <E extends Augmentation<org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.fault.mgmt.rev150811.GetFaultInput>> E getAugmentation(java.lang.Class<E> augmentationType) {
        if (augmentationType == null) {
            throw new IllegalArgumentException("Augmentation Type reference cannot be NULL!");
        }
        return (E) augmentation.get(augmentationType);
    }

     
    public GetFaultInputBuilder setNode(final NodeRef value) {
        this._node = value;
        return this;
    }
    
     
    public GetFaultInputBuilder setObjId(final ObjId value) {
        this._objId = value;
        return this;
    }
    
     
    public GetFaultInputBuilder setEventDrivenReporting(final java.lang.Boolean value) {
        this._eventDrivenReporting = value;
        return this;
    }
    
    public GetFaultInputBuilder addAugmentation(java.lang.Class<? extends Augmentation<org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.fault.mgmt.rev150811.GetFaultInput>> augmentationType, Augmentation<org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.fault.mgmt.rev150811.GetFaultInput> augmentation) {
        if (augmentation == null) {
            return removeAugmentation(augmentationType);
        }
    
        if (!(this.augmentation instanceof HashMap)) {
            this.augmentation = new HashMap<>();
        }
    
        this.augmentation.put(augmentationType, augmentation);
        return this;
    }
    
    public GetFaultInputBuilder removeAugmentation(java.lang.Class<? extends Augmentation<org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.fault.mgmt.rev150811.GetFaultInput>> augmentationType) {
        if (this.augmentation instanceof HashMap) {
            this.augmentation.remove(augmentationType);
        }
        return this;
    }

    public GetFaultInput build() {
        return new GetFaultInputImpl(this);
    }

    private static final class GetFaultInputImpl implements GetFaultInput {

        public java.lang.Class<org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.fault.mgmt.rev150811.GetFaultInput> getImplementedInterface() {
            return org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.fault.mgmt.rev150811.GetFaultInput.class;
        }

        private final NodeRef _node;
        private final ObjId _objId;
        private final java.lang.Boolean _eventDrivenReporting;

        private Map<java.lang.Class<? extends Augmentation<org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.fault.mgmt.rev150811.GetFaultInput>>, Augmentation<org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.fault.mgmt.rev150811.GetFaultInput>> augmentation = Collections.emptyMap();

        private GetFaultInputImpl(GetFaultInputBuilder base) {
            this._node = base.getNode();
            this._objId = base.getObjId();
            this._eventDrivenReporting = base.isEventDrivenReporting();
            switch (base.augmentation.size()) {
            case 0:
                this.augmentation = Collections.emptyMap();
                break;
            case 1:
                final Map.Entry<java.lang.Class<? extends Augmentation<org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.fault.mgmt.rev150811.GetFaultInput>>, Augmentation<org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.fault.mgmt.rev150811.GetFaultInput>> e = base.augmentation.entrySet().iterator().next();
                this.augmentation = Collections.<java.lang.Class<? extends Augmentation<org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.fault.mgmt.rev150811.GetFaultInput>>, Augmentation<org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.fault.mgmt.rev150811.GetFaultInput>>singletonMap(e.getKey(), e.getValue());
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
        public java.lang.Boolean isEventDrivenReporting() {
            return _eventDrivenReporting;
        }
        
        @SuppressWarnings("unchecked")
        @Override
        public <E extends Augmentation<org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.fault.mgmt.rev150811.GetFaultInput>> E getAugmentation(java.lang.Class<E> augmentationType) {
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
            result = prime * result + Objects.hashCode(_eventDrivenReporting);
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
            if (!org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.fault.mgmt.rev150811.GetFaultInput.class.equals(((DataObject)obj).getImplementedInterface())) {
                return false;
            }
            org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.fault.mgmt.rev150811.GetFaultInput other = (org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.fault.mgmt.rev150811.GetFaultInput)obj;
            if (!Objects.equals(_node, other.getNode())) {
                return false;
            }
            if (!Objects.equals(_objId, other.getObjId())) {
                return false;
            }
            if (!Objects.equals(_eventDrivenReporting, other.isEventDrivenReporting())) {
                return false;
            }
            if (getClass() == obj.getClass()) {
                // Simple case: we are comparing against self
                GetFaultInputImpl otherImpl = (GetFaultInputImpl) obj;
                if (!Objects.equals(augmentation, otherImpl.augmentation)) {
                    return false;
                }
            } else {
                // Hard case: compare our augments with presence there...
                for (Map.Entry<java.lang.Class<? extends Augmentation<org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.fault.mgmt.rev150811.GetFaultInput>>, Augmentation<org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.fault.mgmt.rev150811.GetFaultInput>> e : augmentation.entrySet()) {
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
            java.lang.String name = "GetFaultInput [";
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
            if (_eventDrivenReporting != null) {
                builder.append("_eventDrivenReporting=");
                builder.append(_eventDrivenReporting);
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
