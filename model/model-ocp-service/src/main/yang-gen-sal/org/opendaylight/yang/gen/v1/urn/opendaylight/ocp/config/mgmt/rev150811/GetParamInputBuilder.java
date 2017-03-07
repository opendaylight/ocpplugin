package org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.config.mgmt.rev150811;
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
 * Class that builds {@link org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.config.mgmt.rev150811.GetParamInput} instances.
 *
 * @see org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.config.mgmt.rev150811.GetParamInput
 *
 */
public class GetParamInputBuilder implements Builder <org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.config.mgmt.rev150811.GetParamInput> {

    private NodeRef _node;
    private ObjId _objId;
    private java.lang.String _paramName;

    Map<java.lang.Class<? extends Augmentation<org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.config.mgmt.rev150811.GetParamInput>>, Augmentation<org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.config.mgmt.rev150811.GetParamInput>> augmentation = Collections.emptyMap();

    public GetParamInputBuilder() {
    }
    public GetParamInputBuilder(org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.common.types.rev150811.GetParamInput arg) {
        this._objId = arg.getObjId();
        this._paramName = arg.getParamName();
    }
    public GetParamInputBuilder(org.opendaylight.yang.gen.v1.urn.opendaylight.inventory.rev130819.NodeContextRef arg) {
        this._node = arg.getNode();
    }

    public GetParamInputBuilder(GetParamInput base) {
        this._node = base.getNode();
        this._objId = base.getObjId();
        this._paramName = base.getParamName();
        if (base instanceof GetParamInputImpl) {
            GetParamInputImpl impl = (GetParamInputImpl) base;
            if (!impl.augmentation.isEmpty()) {
                this.augmentation = new HashMap<>(impl.augmentation);
            }
        } else if (base instanceof AugmentationHolder) {
            @SuppressWarnings("unchecked")
            AugmentationHolder<org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.config.mgmt.rev150811.GetParamInput> casted =(AugmentationHolder<org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.config.mgmt.rev150811.GetParamInput>) base;
            if (!casted.augmentations().isEmpty()) {
                this.augmentation = new HashMap<>(casted.augmentations());
            }
        }
    }

    /**
     *Set fields from given grouping argument. Valid argument is instance of one of following types:
     * <ul>
     * <li>org.opendaylight.yang.gen.v1.urn.opendaylight.inventory.rev130819.NodeContextRef</li>
     * <li>org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.common.types.rev150811.GetParamInput</li>
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
        if (arg instanceof org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.common.types.rev150811.GetParamInput) {
            this._objId = ((org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.common.types.rev150811.GetParamInput)arg).getObjId();
            this._paramName = ((org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.common.types.rev150811.GetParamInput)arg).getParamName();
            isValidArg = true;
        }
        if (!isValidArg) {
            throw new IllegalArgumentException(
              "expected one of: [org.opendaylight.yang.gen.v1.urn.opendaylight.inventory.rev130819.NodeContextRef, org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.common.types.rev150811.GetParamInput] \n" +
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
    
    public java.lang.String getParamName() {
        return _paramName;
    }
    
    @SuppressWarnings("unchecked")
    public <E extends Augmentation<org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.config.mgmt.rev150811.GetParamInput>> E getAugmentation(java.lang.Class<E> augmentationType) {
        if (augmentationType == null) {
            throw new IllegalArgumentException("Augmentation Type reference cannot be NULL!");
        }
        return (E) augmentation.get(augmentationType);
    }

     
    public GetParamInputBuilder setNode(final NodeRef value) {
        this._node = value;
        return this;
    }
    
     
    public GetParamInputBuilder setObjId(final ObjId value) {
        this._objId = value;
        return this;
    }
    
     
    public GetParamInputBuilder setParamName(final java.lang.String value) {
        this._paramName = value;
        return this;
    }
    
    public GetParamInputBuilder addAugmentation(java.lang.Class<? extends Augmentation<org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.config.mgmt.rev150811.GetParamInput>> augmentationType, Augmentation<org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.config.mgmt.rev150811.GetParamInput> augmentation) {
        if (augmentation == null) {
            return removeAugmentation(augmentationType);
        }
    
        if (!(this.augmentation instanceof HashMap)) {
            this.augmentation = new HashMap<>();
        }
    
        this.augmentation.put(augmentationType, augmentation);
        return this;
    }
    
    public GetParamInputBuilder removeAugmentation(java.lang.Class<? extends Augmentation<org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.config.mgmt.rev150811.GetParamInput>> augmentationType) {
        if (this.augmentation instanceof HashMap) {
            this.augmentation.remove(augmentationType);
        }
        return this;
    }

    public GetParamInput build() {
        return new GetParamInputImpl(this);
    }

    private static final class GetParamInputImpl implements GetParamInput {

        public java.lang.Class<org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.config.mgmt.rev150811.GetParamInput> getImplementedInterface() {
            return org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.config.mgmt.rev150811.GetParamInput.class;
        }

        private final NodeRef _node;
        private final ObjId _objId;
        private final java.lang.String _paramName;

        private Map<java.lang.Class<? extends Augmentation<org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.config.mgmt.rev150811.GetParamInput>>, Augmentation<org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.config.mgmt.rev150811.GetParamInput>> augmentation = Collections.emptyMap();

        private GetParamInputImpl(GetParamInputBuilder base) {
            this._node = base.getNode();
            this._objId = base.getObjId();
            this._paramName = base.getParamName();
            switch (base.augmentation.size()) {
            case 0:
                this.augmentation = Collections.emptyMap();
                break;
            case 1:
                final Map.Entry<java.lang.Class<? extends Augmentation<org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.config.mgmt.rev150811.GetParamInput>>, Augmentation<org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.config.mgmt.rev150811.GetParamInput>> e = base.augmentation.entrySet().iterator().next();
                this.augmentation = Collections.<java.lang.Class<? extends Augmentation<org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.config.mgmt.rev150811.GetParamInput>>, Augmentation<org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.config.mgmt.rev150811.GetParamInput>>singletonMap(e.getKey(), e.getValue());
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
        public java.lang.String getParamName() {
            return _paramName;
        }
        
        @SuppressWarnings("unchecked")
        @Override
        public <E extends Augmentation<org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.config.mgmt.rev150811.GetParamInput>> E getAugmentation(java.lang.Class<E> augmentationType) {
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
            result = prime * result + Objects.hashCode(_paramName);
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
            if (!org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.config.mgmt.rev150811.GetParamInput.class.equals(((DataObject)obj).getImplementedInterface())) {
                return false;
            }
            org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.config.mgmt.rev150811.GetParamInput other = (org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.config.mgmt.rev150811.GetParamInput)obj;
            if (!Objects.equals(_node, other.getNode())) {
                return false;
            }
            if (!Objects.equals(_objId, other.getObjId())) {
                return false;
            }
            if (!Objects.equals(_paramName, other.getParamName())) {
                return false;
            }
            if (getClass() == obj.getClass()) {
                // Simple case: we are comparing against self
                GetParamInputImpl otherImpl = (GetParamInputImpl) obj;
                if (!Objects.equals(augmentation, otherImpl.augmentation)) {
                    return false;
                }
            } else {
                // Hard case: compare our augments with presence there...
                for (Map.Entry<java.lang.Class<? extends Augmentation<org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.config.mgmt.rev150811.GetParamInput>>, Augmentation<org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.config.mgmt.rev150811.GetParamInput>> e : augmentation.entrySet()) {
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
            java.lang.String name = "GetParamInput [";
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
            if (_paramName != null) {
                builder.append("_paramName=");
                builder.append(_paramName);
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
