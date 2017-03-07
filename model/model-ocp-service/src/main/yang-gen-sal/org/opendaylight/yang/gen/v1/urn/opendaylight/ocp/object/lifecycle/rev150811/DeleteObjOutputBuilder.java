package org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.object.lifecycle.rev150811;
import org.opendaylight.yangtools.yang.binding.Augmentation;
import org.opendaylight.yangtools.yang.binding.AugmentationHolder;
import org.opendaylight.yangtools.yang.binding.DataObject;
import java.util.HashMap;
import org.opendaylight.yangtools.concepts.Builder;
import java.util.Objects;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.common.types.rev150811.DeleteObjRes;
import java.util.Collections;
import java.util.Map;


/**
 * Class that builds {@link org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.object.lifecycle.rev150811.DeleteObjOutput} instances.
 *
 * @see org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.object.lifecycle.rev150811.DeleteObjOutput
 *
 */
public class DeleteObjOutputBuilder implements Builder <org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.object.lifecycle.rev150811.DeleteObjOutput> {

    private DeleteObjRes _result;

    Map<java.lang.Class<? extends Augmentation<org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.object.lifecycle.rev150811.DeleteObjOutput>>, Augmentation<org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.object.lifecycle.rev150811.DeleteObjOutput>> augmentation = Collections.emptyMap();

    public DeleteObjOutputBuilder() {
    }

    public DeleteObjOutputBuilder(DeleteObjOutput base) {
        this._result = base.getResult();
        if (base instanceof DeleteObjOutputImpl) {
            DeleteObjOutputImpl impl = (DeleteObjOutputImpl) base;
            if (!impl.augmentation.isEmpty()) {
                this.augmentation = new HashMap<>(impl.augmentation);
            }
        } else if (base instanceof AugmentationHolder) {
            @SuppressWarnings("unchecked")
            AugmentationHolder<org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.object.lifecycle.rev150811.DeleteObjOutput> casted =(AugmentationHolder<org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.object.lifecycle.rev150811.DeleteObjOutput>) base;
            if (!casted.augmentations().isEmpty()) {
                this.augmentation = new HashMap<>(casted.augmentations());
            }
        }
    }


    public DeleteObjRes getResult() {
        return _result;
    }
    
    @SuppressWarnings("unchecked")
    public <E extends Augmentation<org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.object.lifecycle.rev150811.DeleteObjOutput>> E getAugmentation(java.lang.Class<E> augmentationType) {
        if (augmentationType == null) {
            throw new IllegalArgumentException("Augmentation Type reference cannot be NULL!");
        }
        return (E) augmentation.get(augmentationType);
    }

     
    public DeleteObjOutputBuilder setResult(final DeleteObjRes value) {
        this._result = value;
        return this;
    }
    
    public DeleteObjOutputBuilder addAugmentation(java.lang.Class<? extends Augmentation<org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.object.lifecycle.rev150811.DeleteObjOutput>> augmentationType, Augmentation<org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.object.lifecycle.rev150811.DeleteObjOutput> augmentation) {
        if (augmentation == null) {
            return removeAugmentation(augmentationType);
        }
    
        if (!(this.augmentation instanceof HashMap)) {
            this.augmentation = new HashMap<>();
        }
    
        this.augmentation.put(augmentationType, augmentation);
        return this;
    }
    
    public DeleteObjOutputBuilder removeAugmentation(java.lang.Class<? extends Augmentation<org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.object.lifecycle.rev150811.DeleteObjOutput>> augmentationType) {
        if (this.augmentation instanceof HashMap) {
            this.augmentation.remove(augmentationType);
        }
        return this;
    }

    public DeleteObjOutput build() {
        return new DeleteObjOutputImpl(this);
    }

    private static final class DeleteObjOutputImpl implements DeleteObjOutput {

        public java.lang.Class<org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.object.lifecycle.rev150811.DeleteObjOutput> getImplementedInterface() {
            return org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.object.lifecycle.rev150811.DeleteObjOutput.class;
        }

        private final DeleteObjRes _result;

        private Map<java.lang.Class<? extends Augmentation<org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.object.lifecycle.rev150811.DeleteObjOutput>>, Augmentation<org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.object.lifecycle.rev150811.DeleteObjOutput>> augmentation = Collections.emptyMap();

        private DeleteObjOutputImpl(DeleteObjOutputBuilder base) {
            this._result = base.getResult();
            switch (base.augmentation.size()) {
            case 0:
                this.augmentation = Collections.emptyMap();
                break;
            case 1:
                final Map.Entry<java.lang.Class<? extends Augmentation<org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.object.lifecycle.rev150811.DeleteObjOutput>>, Augmentation<org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.object.lifecycle.rev150811.DeleteObjOutput>> e = base.augmentation.entrySet().iterator().next();
                this.augmentation = Collections.<java.lang.Class<? extends Augmentation<org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.object.lifecycle.rev150811.DeleteObjOutput>>, Augmentation<org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.object.lifecycle.rev150811.DeleteObjOutput>>singletonMap(e.getKey(), e.getValue());
                break;
            default :
                this.augmentation = new HashMap<>(base.augmentation);
            }
        }

        @Override
        public DeleteObjRes getResult() {
            return _result;
        }
        
        @SuppressWarnings("unchecked")
        @Override
        public <E extends Augmentation<org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.object.lifecycle.rev150811.DeleteObjOutput>> E getAugmentation(java.lang.Class<E> augmentationType) {
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
            result = prime * result + Objects.hashCode(_result);
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
            if (!org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.object.lifecycle.rev150811.DeleteObjOutput.class.equals(((DataObject)obj).getImplementedInterface())) {
                return false;
            }
            org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.object.lifecycle.rev150811.DeleteObjOutput other = (org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.object.lifecycle.rev150811.DeleteObjOutput)obj;
            if (!Objects.equals(_result, other.getResult())) {
                return false;
            }
            if (getClass() == obj.getClass()) {
                // Simple case: we are comparing against self
                DeleteObjOutputImpl otherImpl = (DeleteObjOutputImpl) obj;
                if (!Objects.equals(augmentation, otherImpl.augmentation)) {
                    return false;
                }
            } else {
                // Hard case: compare our augments with presence there...
                for (Map.Entry<java.lang.Class<? extends Augmentation<org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.object.lifecycle.rev150811.DeleteObjOutput>>, Augmentation<org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.object.lifecycle.rev150811.DeleteObjOutput>> e : augmentation.entrySet()) {
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
            java.lang.String name = "DeleteObjOutput [";
            java.lang.StringBuilder builder = new java.lang.StringBuilder (name);
            if (_result != null) {
                builder.append("_result=");
                builder.append(_result);
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
