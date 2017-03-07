package org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.device.mgmt.rev150811;
import org.opendaylight.yangtools.yang.binding.Augmentation;
import org.opendaylight.yang.gen.v1.urn.opendaylight.inventory.rev130819.NodeRef;
import org.opendaylight.yangtools.yang.binding.AugmentationHolder;
import org.opendaylight.yangtools.yang.binding.DataObject;
import java.util.HashMap;
import org.opendaylight.yangtools.concepts.Builder;
import java.util.Objects;
import java.util.Collections;
import java.util.Map;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.xsd.types.rev150811.XsdUnsignedShort;


/**
 * Class that builds {@link org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.device.mgmt.rev150811.HealthCheckInput} instances.
 *
 * @see org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.device.mgmt.rev150811.HealthCheckInput
 *
 */
public class HealthCheckInputBuilder implements Builder <org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.device.mgmt.rev150811.HealthCheckInput> {

    private NodeRef _node;
    private XsdUnsignedShort _tcpLinkMonTimeout;

    Map<java.lang.Class<? extends Augmentation<org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.device.mgmt.rev150811.HealthCheckInput>>, Augmentation<org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.device.mgmt.rev150811.HealthCheckInput>> augmentation = Collections.emptyMap();

    public HealthCheckInputBuilder() {
    }
    public HealthCheckInputBuilder(org.opendaylight.yang.gen.v1.urn.opendaylight.inventory.rev130819.NodeContextRef arg) {
        this._node = arg.getNode();
    }

    public HealthCheckInputBuilder(HealthCheckInput base) {
        this._node = base.getNode();
        this._tcpLinkMonTimeout = base.getTcpLinkMonTimeout();
        if (base instanceof HealthCheckInputImpl) {
            HealthCheckInputImpl impl = (HealthCheckInputImpl) base;
            if (!impl.augmentation.isEmpty()) {
                this.augmentation = new HashMap<>(impl.augmentation);
            }
        } else if (base instanceof AugmentationHolder) {
            @SuppressWarnings("unchecked")
            AugmentationHolder<org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.device.mgmt.rev150811.HealthCheckInput> casted =(AugmentationHolder<org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.device.mgmt.rev150811.HealthCheckInput>) base;
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

    public NodeRef getNode() {
        return _node;
    }
    
    public XsdUnsignedShort getTcpLinkMonTimeout() {
        return _tcpLinkMonTimeout;
    }
    
    @SuppressWarnings("unchecked")
    public <E extends Augmentation<org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.device.mgmt.rev150811.HealthCheckInput>> E getAugmentation(java.lang.Class<E> augmentationType) {
        if (augmentationType == null) {
            throw new IllegalArgumentException("Augmentation Type reference cannot be NULL!");
        }
        return (E) augmentation.get(augmentationType);
    }

     
    public HealthCheckInputBuilder setNode(final NodeRef value) {
        this._node = value;
        return this;
    }
    
     
    public HealthCheckInputBuilder setTcpLinkMonTimeout(final XsdUnsignedShort value) {
        this._tcpLinkMonTimeout = value;
        return this;
    }
    
    public HealthCheckInputBuilder addAugmentation(java.lang.Class<? extends Augmentation<org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.device.mgmt.rev150811.HealthCheckInput>> augmentationType, Augmentation<org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.device.mgmt.rev150811.HealthCheckInput> augmentation) {
        if (augmentation == null) {
            return removeAugmentation(augmentationType);
        }
    
        if (!(this.augmentation instanceof HashMap)) {
            this.augmentation = new HashMap<>();
        }
    
        this.augmentation.put(augmentationType, augmentation);
        return this;
    }
    
    public HealthCheckInputBuilder removeAugmentation(java.lang.Class<? extends Augmentation<org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.device.mgmt.rev150811.HealthCheckInput>> augmentationType) {
        if (this.augmentation instanceof HashMap) {
            this.augmentation.remove(augmentationType);
        }
        return this;
    }

    public HealthCheckInput build() {
        return new HealthCheckInputImpl(this);
    }

    private static final class HealthCheckInputImpl implements HealthCheckInput {

        public java.lang.Class<org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.device.mgmt.rev150811.HealthCheckInput> getImplementedInterface() {
            return org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.device.mgmt.rev150811.HealthCheckInput.class;
        }

        private final NodeRef _node;
        private final XsdUnsignedShort _tcpLinkMonTimeout;

        private Map<java.lang.Class<? extends Augmentation<org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.device.mgmt.rev150811.HealthCheckInput>>, Augmentation<org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.device.mgmt.rev150811.HealthCheckInput>> augmentation = Collections.emptyMap();

        private HealthCheckInputImpl(HealthCheckInputBuilder base) {
            this._node = base.getNode();
            this._tcpLinkMonTimeout = base.getTcpLinkMonTimeout();
            switch (base.augmentation.size()) {
            case 0:
                this.augmentation = Collections.emptyMap();
                break;
            case 1:
                final Map.Entry<java.lang.Class<? extends Augmentation<org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.device.mgmt.rev150811.HealthCheckInput>>, Augmentation<org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.device.mgmt.rev150811.HealthCheckInput>> e = base.augmentation.entrySet().iterator().next();
                this.augmentation = Collections.<java.lang.Class<? extends Augmentation<org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.device.mgmt.rev150811.HealthCheckInput>>, Augmentation<org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.device.mgmt.rev150811.HealthCheckInput>>singletonMap(e.getKey(), e.getValue());
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
        public XsdUnsignedShort getTcpLinkMonTimeout() {
            return _tcpLinkMonTimeout;
        }
        
        @SuppressWarnings("unchecked")
        @Override
        public <E extends Augmentation<org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.device.mgmt.rev150811.HealthCheckInput>> E getAugmentation(java.lang.Class<E> augmentationType) {
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
            result = prime * result + Objects.hashCode(_tcpLinkMonTimeout);
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
            if (!org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.device.mgmt.rev150811.HealthCheckInput.class.equals(((DataObject)obj).getImplementedInterface())) {
                return false;
            }
            org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.device.mgmt.rev150811.HealthCheckInput other = (org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.device.mgmt.rev150811.HealthCheckInput)obj;
            if (!Objects.equals(_node, other.getNode())) {
                return false;
            }
            if (!Objects.equals(_tcpLinkMonTimeout, other.getTcpLinkMonTimeout())) {
                return false;
            }
            if (getClass() == obj.getClass()) {
                // Simple case: we are comparing against self
                HealthCheckInputImpl otherImpl = (HealthCheckInputImpl) obj;
                if (!Objects.equals(augmentation, otherImpl.augmentation)) {
                    return false;
                }
            } else {
                // Hard case: compare our augments with presence there...
                for (Map.Entry<java.lang.Class<? extends Augmentation<org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.device.mgmt.rev150811.HealthCheckInput>>, Augmentation<org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.device.mgmt.rev150811.HealthCheckInput>> e : augmentation.entrySet()) {
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
            java.lang.String name = "HealthCheckInput [";
            java.lang.StringBuilder builder = new java.lang.StringBuilder (name);
            if (_node != null) {
                builder.append("_node=");
                builder.append(_node);
                builder.append(", ");
            }
            if (_tcpLinkMonTimeout != null) {
                builder.append("_tcpLinkMonTimeout=");
                builder.append(_tcpLinkMonTimeout);
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
