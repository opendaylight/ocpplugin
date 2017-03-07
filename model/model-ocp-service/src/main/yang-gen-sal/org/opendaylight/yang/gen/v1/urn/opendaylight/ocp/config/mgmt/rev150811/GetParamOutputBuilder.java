package org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.config.mgmt.rev150811;
import org.opendaylight.yangtools.yang.binding.Augmentation;
import org.opendaylight.yangtools.yang.binding.AugmentationHolder;
import org.opendaylight.yangtools.yang.binding.DataObject;
import java.util.HashMap;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.common.types.rev150811.getparamoutput.Obj;
import org.opendaylight.yangtools.concepts.Builder;
import java.util.Objects;
import java.util.List;
import java.util.Collections;
import java.util.Map;
import org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.common.types.rev150811.GetParamRes;


/**
 * Class that builds {@link org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.config.mgmt.rev150811.GetParamOutput} instances.
 *
 * @see org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.config.mgmt.rev150811.GetParamOutput
 *
 */
public class GetParamOutputBuilder implements Builder <org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.config.mgmt.rev150811.GetParamOutput> {

    private List<Obj> _obj;
    private GetParamRes _result;

    Map<java.lang.Class<? extends Augmentation<org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.config.mgmt.rev150811.GetParamOutput>>, Augmentation<org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.config.mgmt.rev150811.GetParamOutput>> augmentation = Collections.emptyMap();

    public GetParamOutputBuilder() {
    }
    public GetParamOutputBuilder(org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.common.types.rev150811.GetParamOutput arg) {
        this._obj = arg.getObj();
    }

    public GetParamOutputBuilder(GetParamOutput base) {
        this._obj = base.getObj();
        this._result = base.getResult();
        if (base instanceof GetParamOutputImpl) {
            GetParamOutputImpl impl = (GetParamOutputImpl) base;
            if (!impl.augmentation.isEmpty()) {
                this.augmentation = new HashMap<>(impl.augmentation);
            }
        } else if (base instanceof AugmentationHolder) {
            @SuppressWarnings("unchecked")
            AugmentationHolder<org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.config.mgmt.rev150811.GetParamOutput> casted =(AugmentationHolder<org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.config.mgmt.rev150811.GetParamOutput>) base;
            if (!casted.augmentations().isEmpty()) {
                this.augmentation = new HashMap<>(casted.augmentations());
            }
        }
    }

    /**
     *Set fields from given grouping argument. Valid argument is instance of one of following types:
     * <ul>
     * <li>org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.common.types.rev150811.GetParamOutput</li>
     * </ul>
     *
     * @param arg grouping object
     * @throws IllegalArgumentException if given argument is none of valid types
    */
    public void fieldsFrom(DataObject arg) {
        boolean isValidArg = false;
        if (arg instanceof org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.common.types.rev150811.GetParamOutput) {
            this._obj = ((org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.common.types.rev150811.GetParamOutput)arg).getObj();
            isValidArg = true;
        }
        if (!isValidArg) {
            throw new IllegalArgumentException(
              "expected one of: [org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.common.types.rev150811.GetParamOutput] \n" +
              "but was: " + arg
            );
        }
    }

    public List<Obj> getObj() {
        return _obj;
    }
    
    public GetParamRes getResult() {
        return _result;
    }
    
    @SuppressWarnings("unchecked")
    public <E extends Augmentation<org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.config.mgmt.rev150811.GetParamOutput>> E getAugmentation(java.lang.Class<E> augmentationType) {
        if (augmentationType == null) {
            throw new IllegalArgumentException("Augmentation Type reference cannot be NULL!");
        }
        return (E) augmentation.get(augmentationType);
    }

     
    public GetParamOutputBuilder setObj(final List<Obj> value) {
        this._obj = value;
        return this;
    }
    
     
    public GetParamOutputBuilder setResult(final GetParamRes value) {
        this._result = value;
        return this;
    }
    
    public GetParamOutputBuilder addAugmentation(java.lang.Class<? extends Augmentation<org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.config.mgmt.rev150811.GetParamOutput>> augmentationType, Augmentation<org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.config.mgmt.rev150811.GetParamOutput> augmentation) {
        if (augmentation == null) {
            return removeAugmentation(augmentationType);
        }
    
        if (!(this.augmentation instanceof HashMap)) {
            this.augmentation = new HashMap<>();
        }
    
        this.augmentation.put(augmentationType, augmentation);
        return this;
    }
    
    public GetParamOutputBuilder removeAugmentation(java.lang.Class<? extends Augmentation<org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.config.mgmt.rev150811.GetParamOutput>> augmentationType) {
        if (this.augmentation instanceof HashMap) {
            this.augmentation.remove(augmentationType);
        }
        return this;
    }

    public GetParamOutput build() {
        return new GetParamOutputImpl(this);
    }

    private static final class GetParamOutputImpl implements GetParamOutput {

        public java.lang.Class<org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.config.mgmt.rev150811.GetParamOutput> getImplementedInterface() {
            return org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.config.mgmt.rev150811.GetParamOutput.class;
        }

        private final List<Obj> _obj;
        private final GetParamRes _result;

        private Map<java.lang.Class<? extends Augmentation<org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.config.mgmt.rev150811.GetParamOutput>>, Augmentation<org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.config.mgmt.rev150811.GetParamOutput>> augmentation = Collections.emptyMap();

        private GetParamOutputImpl(GetParamOutputBuilder base) {
            this._obj = base.getObj();
            this._result = base.getResult();
            switch (base.augmentation.size()) {
            case 0:
                this.augmentation = Collections.emptyMap();
                break;
            case 1:
                final Map.Entry<java.lang.Class<? extends Augmentation<org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.config.mgmt.rev150811.GetParamOutput>>, Augmentation<org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.config.mgmt.rev150811.GetParamOutput>> e = base.augmentation.entrySet().iterator().next();
                this.augmentation = Collections.<java.lang.Class<? extends Augmentation<org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.config.mgmt.rev150811.GetParamOutput>>, Augmentation<org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.config.mgmt.rev150811.GetParamOutput>>singletonMap(e.getKey(), e.getValue());
                break;
            default :
                this.augmentation = new HashMap<>(base.augmentation);
            }
        }

        @Override
        public List<Obj> getObj() {
            return _obj;
        }
        
        @Override
        public GetParamRes getResult() {
            return _result;
        }
        
        @SuppressWarnings("unchecked")
        @Override
        public <E extends Augmentation<org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.config.mgmt.rev150811.GetParamOutput>> E getAugmentation(java.lang.Class<E> augmentationType) {
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
            result = prime * result + Objects.hashCode(_obj);
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
            if (!org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.config.mgmt.rev150811.GetParamOutput.class.equals(((DataObject)obj).getImplementedInterface())) {
                return false;
            }
            org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.config.mgmt.rev150811.GetParamOutput other = (org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.config.mgmt.rev150811.GetParamOutput)obj;
            if (!Objects.equals(_obj, other.getObj())) {
                return false;
            }
            if (!Objects.equals(_result, other.getResult())) {
                return false;
            }
            if (getClass() == obj.getClass()) {
                // Simple case: we are comparing against self
                GetParamOutputImpl otherImpl = (GetParamOutputImpl) obj;
                if (!Objects.equals(augmentation, otherImpl.augmentation)) {
                    return false;
                }
            } else {
                // Hard case: compare our augments with presence there...
                for (Map.Entry<java.lang.Class<? extends Augmentation<org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.config.mgmt.rev150811.GetParamOutput>>, Augmentation<org.opendaylight.yang.gen.v1.urn.opendaylight.ocp.config.mgmt.rev150811.GetParamOutput>> e : augmentation.entrySet()) {
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
            java.lang.String name = "GetParamOutput [";
            java.lang.StringBuilder builder = new java.lang.StringBuilder (name);
            if (_obj != null) {
                builder.append("_obj=");
                builder.append(_obj);
                builder.append(", ");
            }
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
