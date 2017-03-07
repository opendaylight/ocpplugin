package org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.device.mgmt.rev150811;
import org.opendaylight.yangtools.yang.binding.Augmentation;
import org.opendaylight.yangtools.yang.binding.AugmentationHolder;
import org.opendaylight.yangtools.yang.binding.DataObject;
import java.util.HashMap;
import org.opendaylight.yangtools.concepts.Builder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.inventory.rev130819.NodeId;
import java.util.Objects;
import java.util.Collections;
import java.util.Map;
import org.opendaylight.yang.gen.v1.urn.ietf.params.xml.ns.yang.ietf.inet.types.rev130715.Ipv4Address;


/**
 * Class that builds {@link org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.device.mgmt.rev150811.DeviceConnected} instances.
 *
 * @see org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.device.mgmt.rev150811.DeviceConnected
 *
 */
public class DeviceConnectedBuilder implements Builder <org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.device.mgmt.rev150811.DeviceConnected> {

    private NodeId _nodeId;
    private Ipv4Address _reIpAddr;

    Map<java.lang.Class<? extends Augmentation<org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.device.mgmt.rev150811.DeviceConnected>>, Augmentation<org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.device.mgmt.rev150811.DeviceConnected>> augmentation = Collections.emptyMap();

    public DeviceConnectedBuilder() {
    }

    public DeviceConnectedBuilder(DeviceConnected base) {
        this._nodeId = base.getNodeId();
        this._reIpAddr = base.getReIpAddr();
        if (base instanceof DeviceConnectedImpl) {
            DeviceConnectedImpl impl = (DeviceConnectedImpl) base;
            if (!impl.augmentation.isEmpty()) {
                this.augmentation = new HashMap<>(impl.augmentation);
            }
        } else if (base instanceof AugmentationHolder) {
            @SuppressWarnings("unchecked")
            AugmentationHolder<org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.device.mgmt.rev150811.DeviceConnected> casted =(AugmentationHolder<org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.device.mgmt.rev150811.DeviceConnected>) base;
            if (!casted.augmentations().isEmpty()) {
                this.augmentation = new HashMap<>(casted.augmentations());
            }
        }
    }


    public NodeId getNodeId() {
        return _nodeId;
    }
    
    public Ipv4Address getReIpAddr() {
        return _reIpAddr;
    }
    
    @SuppressWarnings("unchecked")
    public <E extends Augmentation<org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.device.mgmt.rev150811.DeviceConnected>> E getAugmentation(java.lang.Class<E> augmentationType) {
        if (augmentationType == null) {
            throw new IllegalArgumentException("Augmentation Type reference cannot be NULL!");
        }
        return (E) augmentation.get(augmentationType);
    }

     
    public DeviceConnectedBuilder setNodeId(final NodeId value) {
        this._nodeId = value;
        return this;
    }
    
     
    public DeviceConnectedBuilder setReIpAddr(final Ipv4Address value) {
        this._reIpAddr = value;
        return this;
    }
    
    public DeviceConnectedBuilder addAugmentation(java.lang.Class<? extends Augmentation<org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.device.mgmt.rev150811.DeviceConnected>> augmentationType, Augmentation<org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.device.mgmt.rev150811.DeviceConnected> augmentation) {
        if (augmentation == null) {
            return removeAugmentation(augmentationType);
        }
    
        if (!(this.augmentation instanceof HashMap)) {
            this.augmentation = new HashMap<>();
        }
    
        this.augmentation.put(augmentationType, augmentation);
        return this;
    }
    
    public DeviceConnectedBuilder removeAugmentation(java.lang.Class<? extends Augmentation<org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.device.mgmt.rev150811.DeviceConnected>> augmentationType) {
        if (this.augmentation instanceof HashMap) {
            this.augmentation.remove(augmentationType);
        }
        return this;
    }

    public DeviceConnected build() {
        return new DeviceConnectedImpl(this);
    }

    private static final class DeviceConnectedImpl implements DeviceConnected {

        public java.lang.Class<org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.device.mgmt.rev150811.DeviceConnected> getImplementedInterface() {
            return org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.device.mgmt.rev150811.DeviceConnected.class;
        }

        private final NodeId _nodeId;
        private final Ipv4Address _reIpAddr;

        private Map<java.lang.Class<? extends Augmentation<org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.device.mgmt.rev150811.DeviceConnected>>, Augmentation<org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.device.mgmt.rev150811.DeviceConnected>> augmentation = Collections.emptyMap();

        private DeviceConnectedImpl(DeviceConnectedBuilder base) {
            this._nodeId = base.getNodeId();
            this._reIpAddr = base.getReIpAddr();
            switch (base.augmentation.size()) {
            case 0:
                this.augmentation = Collections.emptyMap();
                break;
            case 1:
                final Map.Entry<java.lang.Class<? extends Augmentation<org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.device.mgmt.rev150811.DeviceConnected>>, Augmentation<org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.device.mgmt.rev150811.DeviceConnected>> e = base.augmentation.entrySet().iterator().next();
                this.augmentation = Collections.<java.lang.Class<? extends Augmentation<org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.device.mgmt.rev150811.DeviceConnected>>, Augmentation<org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.device.mgmt.rev150811.DeviceConnected>>singletonMap(e.getKey(), e.getValue());
                break;
            default :
                this.augmentation = new HashMap<>(base.augmentation);
            }
        }

        @Override
        public NodeId getNodeId() {
            return _nodeId;
        }
        
        @Override
        public Ipv4Address getReIpAddr() {
            return _reIpAddr;
        }
        
        @SuppressWarnings("unchecked")
        @Override
        public <E extends Augmentation<org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.device.mgmt.rev150811.DeviceConnected>> E getAugmentation(java.lang.Class<E> augmentationType) {
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
            result = prime * result + Objects.hashCode(_nodeId);
            result = prime * result + Objects.hashCode(_reIpAddr);
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
            if (!org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.device.mgmt.rev150811.DeviceConnected.class.equals(((DataObject)obj).getImplementedInterface())) {
                return false;
            }
            org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.device.mgmt.rev150811.DeviceConnected other = (org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.device.mgmt.rev150811.DeviceConnected)obj;
            if (!Objects.equals(_nodeId, other.getNodeId())) {
                return false;
            }
            if (!Objects.equals(_reIpAddr, other.getReIpAddr())) {
                return false;
            }
            if (getClass() == obj.getClass()) {
                // Simple case: we are comparing against self
                DeviceConnectedImpl otherImpl = (DeviceConnectedImpl) obj;
                if (!Objects.equals(augmentation, otherImpl.augmentation)) {
                    return false;
                }
            } else {
                // Hard case: compare our augments with presence there...
                for (Map.Entry<java.lang.Class<? extends Augmentation<org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.device.mgmt.rev150811.DeviceConnected>>, Augmentation<org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.device.mgmt.rev150811.DeviceConnected>> e : augmentation.entrySet()) {
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
            java.lang.String name = "DeviceConnected [";
            java.lang.StringBuilder builder = new java.lang.StringBuilder (name);
            if (_nodeId != null) {
                builder.append("_nodeId=");
                builder.append(_nodeId);
                builder.append(", ");
            }
            if (_reIpAddr != null) {
                builder.append("_reIpAddr=");
                builder.append(_reIpAddr);
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
