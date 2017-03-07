package org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.device.mgmt.rev150811;
import org.opendaylight.yangtools.yang.binding.Augmentation;
import org.opendaylight.yang.gen.v1.urn.opendaylight.inventory.rev130819.NodeRef;
import org.opendaylight.yangtools.yang.binding.AugmentationHolder;
import org.opendaylight.yangtools.yang.binding.DataObject;
import java.util.HashMap;
import org.opendaylight.yangtools.concepts.Builder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.xsd.types.rev150811.XsdDateTime;
import java.util.Objects;
import java.util.Collections;
import java.util.Map;


/**
 * Class that builds {@link org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.device.mgmt.rev150811.SetTimeInput} instances.
 *
 * @see org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.device.mgmt.rev150811.SetTimeInput
 *
 */
public class SetTimeInputBuilder implements Builder <org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.device.mgmt.rev150811.SetTimeInput> {

    private XsdDateTime _newTime;
    private NodeRef _node;

    Map<java.lang.Class<? extends Augmentation<org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.device.mgmt.rev150811.SetTimeInput>>, Augmentation<org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.device.mgmt.rev150811.SetTimeInput>> augmentation = Collections.emptyMap();

    public SetTimeInputBuilder() {
    }
    public SetTimeInputBuilder(org.opendaylight.yang.gen.v1.urn.opendaylight.inventory.rev130819.NodeContextRef arg) {
        this._node = arg.getNode();
    }

    public SetTimeInputBuilder(SetTimeInput base) {
        this._newTime = base.getNewTime();
        this._node = base.getNode();
        if (base instanceof SetTimeInputImpl) {
            SetTimeInputImpl impl = (SetTimeInputImpl) base;
            if (!impl.augmentation.isEmpty()) {
                this.augmentation = new HashMap<>(impl.augmentation);
            }
        } else if (base instanceof AugmentationHolder) {
            @SuppressWarnings("unchecked")
            AugmentationHolder<org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.device.mgmt.rev150811.SetTimeInput> casted =(AugmentationHolder<org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.device.mgmt.rev150811.SetTimeInput>) base;
            if (!casted.augmentations().isEmpty()) {
                this.augmentation = new HashMap<>(casted.augmentations());
            }
        }
    }

    /**
     *Set fields from given grouping argument. Valid argument is instance of one of following types:
     * <ul>
     * <li>org.opendaylight.yang.gen.v1.urn.opendaylight.inventory.rev130819.NodeContextRef</li>
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
        if (!isValidArg) {
            throw new IllegalArgumentException(
              "expected one of: [org.opendaylight.yang.gen.v1.urn.opendaylight.inventory.rev130819.NodeContextRef] \n" +
              "but was: " + arg
            );
        }
    }

    public XsdDateTime getNewTime() {
        return _newTime;
    }
    
    public NodeRef getNode() {
        return _node;
    }
    
    @SuppressWarnings("unchecked")
    public <E extends Augmentation<org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.device.mgmt.rev150811.SetTimeInput>> E getAugmentation(java.lang.Class<E> augmentationType) {
        if (augmentationType == null) {
            throw new IllegalArgumentException("Augmentation Type reference cannot be NULL!");
        }
        return (E) augmentation.get(augmentationType);
    }

     
    public SetTimeInputBuilder setNewTime(final XsdDateTime value) {
        this._newTime = value;
        return this;
    }
    
     
    public SetTimeInputBuilder setNode(final NodeRef value) {
        this._node = value;
        return this;
    }
    
    public SetTimeInputBuilder addAugmentation(java.lang.Class<? extends Augmentation<org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.device.mgmt.rev150811.SetTimeInput>> augmentationType, Augmentation<org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.device.mgmt.rev150811.SetTimeInput> augmentation) {
        if (augmentation == null) {
            return removeAugmentation(augmentationType);
        }
    
        if (!(this.augmentation instanceof HashMap)) {
            this.augmentation = new HashMap<>();
        }
    
        this.augmentation.put(augmentationType, augmentation);
        return this;
    }
    
    public SetTimeInputBuilder removeAugmentation(java.lang.Class<? extends Augmentation<org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.device.mgmt.rev150811.SetTimeInput>> augmentationType) {
        if (this.augmentation instanceof HashMap) {
            this.augmentation.remove(augmentationType);
        }
        return this;
    }

    public SetTimeInput build() {
        return new SetTimeInputImpl(this);
    }

    private static final class SetTimeInputImpl implements SetTimeInput {

        public java.lang.Class<org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.device.mgmt.rev150811.SetTimeInput> getImplementedInterface() {
            return org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.device.mgmt.rev150811.SetTimeInput.class;
        }

        private final XsdDateTime _newTime;
        private final NodeRef _node;

        private Map<java.lang.Class<? extends Augmentation<org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.device.mgmt.rev150811.SetTimeInput>>, Augmentation<org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.device.mgmt.rev150811.SetTimeInput>> augmentation = Collections.emptyMap();

        private SetTimeInputImpl(SetTimeInputBuilder base) {
            this._newTime = base.getNewTime();
            this._node = base.getNode();
            switch (base.augmentation.size()) {
            case 0:
                this.augmentation = Collections.emptyMap();
                break;
            case 1:
                final Map.Entry<java.lang.Class<? extends Augmentation<org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.device.mgmt.rev150811.SetTimeInput>>, Augmentation<org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.device.mgmt.rev150811.SetTimeInput>> e = base.augmentation.entrySet().iterator().next();
                this.augmentation = Collections.<java.lang.Class<? extends Augmentation<org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.device.mgmt.rev150811.SetTimeInput>>, Augmentation<org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.device.mgmt.rev150811.SetTimeInput>>singletonMap(e.getKey(), e.getValue());
                break;
            default :
                this.augmentation = new HashMap<>(base.augmentation);
            }
        }

        @Override
        public XsdDateTime getNewTime() {
            return _newTime;
        }
        
        @Override
        public NodeRef getNode() {
            return _node;
        }
        
        @SuppressWarnings("unchecked")
        @Override
        public <E extends Augmentation<org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.device.mgmt.rev150811.SetTimeInput>> E getAugmentation(java.lang.Class<E> augmentationType) {
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
            result = prime * result + Objects.hashCode(_newTime);
            result = prime * result + Objects.hashCode(_node);
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
            if (!org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.device.mgmt.rev150811.SetTimeInput.class.equals(((DataObject)obj).getImplementedInterface())) {
                return false;
            }
            org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.device.mgmt.rev150811.SetTimeInput other = (org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.device.mgmt.rev150811.SetTimeInput)obj;
            if (!Objects.equals(_newTime, other.getNewTime())) {
                return false;
            }
            if (!Objects.equals(_node, other.getNode())) {
                return false;
            }
            if (getClass() == obj.getClass()) {
                // Simple case: we are comparing against self
                SetTimeInputImpl otherImpl = (SetTimeInputImpl) obj;
                if (!Objects.equals(augmentation, otherImpl.augmentation)) {
                    return false;
                }
            } else {
                // Hard case: compare our augments with presence there...
                for (Map.Entry<java.lang.Class<? extends Augmentation<org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.device.mgmt.rev150811.SetTimeInput>>, Augmentation<org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.device.mgmt.rev150811.SetTimeInput>> e : augmentation.entrySet()) {
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
            java.lang.String name = "SetTimeInput [";
            java.lang.StringBuilder builder = new java.lang.StringBuilder (name);
            if (_newTime != null) {
                builder.append("_newTime=");
                builder.append(_newTime);
                builder.append(", ");
            }
            if (_node != null) {
                builder.append("_node=");
                builder.append(_node);
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
